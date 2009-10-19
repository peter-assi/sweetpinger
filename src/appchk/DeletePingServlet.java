package appchk;

import java.io.IOException;
import java.security.Principal;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class DeletePingServlet extends HttpServlet {

    Logger log = Logger.getLogger(DeletePingServlet.class);
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Principal user = req.getUserPrincipal();
        String email = user.getName();
        Long id = Long.parseLong(req.getParameter(Stuff.ID));
        if (log.isDebugEnabled()){
            log.debug("User "+email+" requested delete of url id:"+id);
        }
        EntityManager mgr = EMF.getManager();
        try {
            PingUrl urlToDelete = (PingUrl) mgr.createQuery("SELECT u FROM appchk.PingUrl u WHERE u.id=:id AND u.owner=:owner").
                        setParameter("id", id).setParameter("owner", email).getSingleResult();
            mgr.remove(urlToDelete);
//            List<PingUrl> list = mgr.createQuery("SELECT u FROM appchk.PingUrl WHERE u.owner=:owner").setParameter("owner", email).getResultList();
//            req.setAttribute(Stuff.LIST, list);
            if (log.isDebugEnabled()){
                log.debug("Url removed: "+urlToDelete.getUrl());
            }
            req.setAttribute(Stuff.FLASH, "Removed URL");
        } catch (Exception e) {
            System.out.println("Datastore error "+e.getMessage());
            e.printStackTrace();
            req.setAttribute(Stuff.FLASH, "Error removing URL "+e.getMessage());
        } finally {
            mgr.close();
        }
        try {
            req.getRequestDispatcher(Stuff.PINGROUTE).forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
            resp.sendRedirect("/");
        }

    }
}
