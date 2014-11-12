package quizsite;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ServerStartupListener
 *
 */
@WebListener
public class ServerStartupListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ServerStartupListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         // TODO 
    	ServletContext sc = arg0.getServletContext();
    	
    	DatabaseConnection dc = new DatabaseConnection();
    	sc.setAttribute("DatabaseConnection", dc);
    	
    	SiteManager sm = new SiteManager(dc);
    	sc.setAttribute("SiteManager", sm);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    	ServletContext sc = arg0.getServletContext();
    	
    	// Save all site metrics
    	SiteManager sm = (SiteManager) sc.getAttribute("SiteManager");
    	sm.updateDatabase();
    	
    	// Save other site data
    	
    	// Kill the database connection (do this last)
    	DatabaseConnection dc = (DatabaseConnection) sc.getAttribute("DatabaseConnection");
    	dc.closeConnection();
    }
	
}
