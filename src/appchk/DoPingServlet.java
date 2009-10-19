package appchk;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class DoPing
 */
public class DoPingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DoPingServlet.class);
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if (log.isDebugEnabled()){
            log.debug("doGet invoked");
        }
	    String user = request.getParameter(Stuff.USER);
	    if (log.isDebugEnabled()){
	        log.debug("Get all urls for user "+user);
	    }
	    EntityManager mgr = null;
	    try {
	        mgr = EMF.getManager();
	        List<PingUrl> list = mgr.createQuery("SELECT u FROM appchk.PingUrl u WHERE u.owner = :owner").setParameter("owner", user).getResultList();
	        if (log.isDebugEnabled()){
	            log.debug("Found "+list.size()+" urls for user "+user);
	        }
	        for (PingUrl url : list ) {
	            PingResponse resp =  Pinger.ping(url);
	            url.setLastChecked(new Date());
	            url.setLastStatus(resp.getStatus());
	            mgr.merge(url);
	            if(resp.hasErrors()){
	                Mailer.mailPingResponse(resp, url);
	            }
	        }
	    } finally {
	        EMF.safeClose(mgr);
	    }
	}
	
	
}
