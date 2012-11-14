package edu.hawaii.wicket.page;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.main.HomePage;
import edu.hawaii.wicket.page.srcsummary.SourceSummaryPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;
import edu.hawaii.wicket.page.thresholds.ThresholdsPage;
import edu.hawaii.wicket.page.visualization.VisualizationPage;

/**
 * Template for all of the pages that are going to be part of this web application.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public abstract class BasePage extends WebPage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * The layout used by all pages, which includes a title and the links that appear in the menu at
   * the top of each page.
   */
  public BasePage() {

    // Add CSS definitions that will be used on all web pages.
    add(CSSPackageResource.getHeaderContribution(edu.hawaii.wicket.page.BasePage.class,
        "stylesheets/screen.css", "screen"));
    add(CSSPackageResource.getHeaderContribution(edu.hawaii.wicket.page.BasePage.class,
        "stylesheets/print.css", "print"));
    // Add title to the page.
    add(new Label("title", "Carbonometer"));
    // Add link to home page.
    add(new BookmarkablePageLink<BasePage>("HomePageLink", HomePage.class));
    // Add link to stoplight page..
    add(new BookmarkablePageLink<BasePage>("StoplightPageLink", StoplightPage.class));
    // Add link to gridinfo page.
    add(new BookmarkablePageLink<BasePage>("GridInfoPageLink", GridInfoPage.class));
    // Add link to thresholds page.
    add(new BookmarkablePageLink<BasePage>("ThresholdsPageLink", ThresholdsPage.class));
    // Add link to source summary page.
    add(new BookmarkablePageLink<BasePage>("SourceSummaryPageLink", SourceSummaryPage.class));

    add(new BookmarkablePageLink<BasePage>("VisualizationPageLink", VisualizationPage.class));
  }
}