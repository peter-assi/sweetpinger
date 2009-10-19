package appchk;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class AddPingServlet extends HttpServlet {
    

    Logger log = Logger.getLogger(CronPingServlet.class);
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Principal user = req.getUserPrincipal();
        //login
        if (user == null) {
            String uri = req.getRequestURI()+"?"+req.getQueryString();
            if (log.isDebugEnabled()){
                log.debug("Requested "+uri+" sending to login first");
            }
            UserService userService = UserServiceFactory.getUserService();
            String loginUrl = userService.createLoginURL(uri);
            resp.sendRedirect(loginUrl);
            return;
        }
        //create user if first time
        String email = user.getName();
        EntityManager querymgr = EMF.getManager();
        PingUser usr = querymgr.find(PingUser.class, email);
        if (usr == null) {
            EntityManager mgr = null;
            try {
                mgr = EMF.getManager();
                System.out.println("Creating new user "+email);
                usr = new PingUser(email);
                mgr.persist(usr);
            } finally {
                EMF.safeClose(mgr);
            }
        } else {
            System.out.println("Returning user "+email);
        }
        
        //Store new url
        //check url
        String newUrl = req.getParameter(Stuff.URL);
        
        if (!Stuff.empty(newUrl)) {
            if (!newUrl.startsWith("http")){
                System.out.println("Adding http to url");
                newUrl = "http://"+newUrl;
            }
            EntityManager mgr = null;
            try {
                URL sanityCheck = new URL(newUrl);
                sanityCheck.openConnection();
                mgr = EMF.getManager();
                System.out.println("Store url "+newUrl);
                PingUrl url = new PingUrl(usr, newUrl);
                mgr.persist(url);
                log.debug("Testing new URL");
                PingResponse response = Pinger.ping(url);
                url.setLastChecked(new Date());
                url.setLastStatus(response.getStatus());
                mgr.merge(url);
            } catch (Exception e) {
                System.out.println("Failed to create URL for input:"+newUrl);
                req.setAttribute(Stuff.FLASH, "Invalid URL supplied");
                try {
                    req.getRequestDispatcher("/").forward(req, resp);
                    return;
                } catch (ServletException se) {
                    se.printStackTrace();
                    resp.sendRedirect("/");
                }
            } finally {
                EMF.safeClose(mgr);
            }
        }
                
        //fetch old
        System.out.println("Get all urls for user "+email);
        List<PingUrl> list = querymgr.createQuery("SELECT u FROM appchk.PingUrl u WHERE u.owner = :owner").setParameter("owner", email).getResultList();
        System.out.println("Found "+list.size()+" urls for user "+email);

        //List<PingUrl> list = new ArrayList<PingUrl>();
        
        req.setAttribute(Stuff.USER, usr);
        req.setAttribute(Stuff.LIST, list);
        
        try {
            req.getRequestDispatcher("/list.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
            resp.sendRedirect("/");
        } finally {
            EMF.safeClose(querymgr);
        }
    }
    
   
}
