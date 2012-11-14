package edu.hawaii.wicket.page.main;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;

/**
 * Tests the home page to see if it is rendered correctly and both the Stoplight and GridInfo pages
 * are loaded correctly after clicking on the respective links.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestHomePage {

  /**
   * Tests the home page to see if it is rendered correctly and also if the Stoplight page is loaded
   * correctly after clicking on the Stoplight link.
   */
  @Test
  public void testHomePageStoplightButton() {
    // Check if home page is rendered correctly.
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);
    // Click on Stoplight link.
    tester.clickLink("StoplightPageLink", false);
    tester.assertRenderedPage(StoplightPage.class);
  }

  /**
   * Tests the home page to see if it is rendered correctly and also if the GridInfo page is loaded
   * correctly after clicking on the GridInfo link.
   */
  @Test
  public void testHomePageGridInfoButton() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(HomePage.class);
    tester.assertRenderedPage(HomePage.class);
    tester.clickLink("GridInfoPageLink", false);
    tester.assertRenderedPage(GridInfoPage.class);
  }

}