package appchk;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

/**
 * Servlet implementation class DoPing
 */
public class CronPingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(CronPingServlet.class);
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Add to queue for each user
	    if (log.isDebugEnabled()){
            log.debug("doGet invoked");
        }
	    Queue queue = QueueFactory.getQueue(Stuff.PINGQ);
	    EntityManager em = null;
	    try {
	        em = EMF.getManager();
	        List<PingUser> users = em.createQuery("SELECT u FROM PingUser u").getResultList();
	        for (PingUser user : users ){
	            if (log.isDebugEnabled()){
	                log.debug("Adding ping task for "+user.getEmail());
	            }
	            queue.add(url("/do/ping").method(Method.GET).param(Stuff.USER, user.getEmail()));
	        }
	        
	    } finally {
	        EMF.safeClose(em);
	    }
	     
	}
}
