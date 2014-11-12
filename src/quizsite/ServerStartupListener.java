package quizsite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import users.AchievementType;

import Quiz.QuestionType;

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
    	
    	//construct arraylist of different questiontypes
    	ArrayList<QuestionType> questiontypes = new ArrayList<QuestionType>();
    	String query = "SELECT * FROM questionTypes";
		ResultSet rs = dc.executeQuery(query);
		try {
			while(rs.next()){
				questiontypes.add(new QuestionType(rs.getInt("id"),rs.getString("name"),rs.getString("tableName")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.setAttribute("questiontypes", questiontypes);
		
		//construct arraylist of different achievementtypes
		ArrayList<AchievementType> achievementtypes = new ArrayList<AchievementType>();
    	String query2 = "SELECT * FROM achievementTypes";
		ResultSet rs2 = dc.executeQuery(query2);
		try {
			while(rs2.next()){
				achievementtypes.add(new AchievementType(rs2.getInt("id"),rs2.getString("name"),rs2.getString("description"), rs2.getString("attribute"), rs2.getInt("value"), rs2.getString("icon")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.setAttribute("achievementtypes", achievementtypes);
    	
    	
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
