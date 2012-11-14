package edu.hawaii.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.main.HomePage;
import edu.hawaii.wicket.page.srcsummary.SourceSummaryPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;
import edu.hawaii.wicket.page.thresholds.ThresholdsPage;
import edu.hawaii.wicket.page.visualization.VisualizationPage;

/**
 * Class required to specify the Ekolugical Carbonometer web application.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class EkolugicalCarbonometer extends WebApplication {
  /**
   * Initialize for this web application.
   * 
   */
  protected void init() {
    mountBookmarkablePage("HomePage", HomePage.class);
    mountBookmarkablePage("Stoplight", StoplightPage.class);
    mountBookmarkablePage("GridInfo", GridInfoPage.class);
    mountBookmarkablePage("Visualization", VisualizationPage.class);
    mountBookmarkablePage("Thresholds", ThresholdsPage.class);
    mountBookmarkablePage("SourceSummary", SourceSummaryPage.class);
    
  }

  /**
   * Returns the home page for this web application.
   * 
   * @return The home page.
   */
  @Override
  public Class<? extends Page> getHomePage() {
    return HomePage.class;
  }

  /**
   * Returns the application mode.
   * 
   * @return Application mode.
   */
  @Override
  public String getConfigurationType() {
    return Application.DEPLOYMENT;    
  }

  /**
   * Returns a Session object used to hold the models for each user of this web application.
   * 
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @return The session instance for this user.
   */
  @Override
  public Session newSession(Request request, Response response) {
    return new edu.hawaii.wicket.Session(this, request);
  }

}