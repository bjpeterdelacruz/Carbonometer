package edu.hawaii.wicket.page.gridinfo;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;
import edu.hawaii.wicket.page.main.HomePage;
import edu.hawaii.wicket.page.srcsummary.SourceSummaryPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;
import edu.hawaii.wicket.page.thresholds.ThresholdsPage;

/**
 * Tests the GridInfo page to see if it is rendered correctly.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestGridInfoPage {
  /** Name of form. */
  private String gridForm = "gridForm";
  /** Textbox for start_day. */
  private String fromDay = "start_day";
  /** Textbox for end_day. */
  private String toDay = "end_day";
  /** Form submit button. */
  private String button = "generate";

  /**
   * Tests the GridInfo page to see if it is rendered correctly, that is, no errors are displayed
   * and the chart is invisible.
   */
  @Test
  public void testGridInfoPageOnLoad() {
    // Start WicketTester and see if the page renders.
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    // Check that the error label and chart are not displayed.
    tester.assertLabel("status", "");
  }

  /**
   * Tests the GridInfo page to see if the chart is displayed.
   */
  @Test
  public void testGridInfoPageOnSubmit() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    // Now click on the Generate chart button, and make sure that the chart is displayed.
    FormTester formTester = tester.newFormTester(gridForm);
    formTester.setValue(fromDay, "12/1/2009");
    formTester.setValue(toDay, "12/2/2009");
    formTester.submit(button);
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
  }

  /**
   * Tests the GridInfo page to see if an invalid date is handled correctly.
   */
  @Test
  public void testGridInfoPageOnSubmitInvalidDate() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    // Enter an invalid date.
    FormTester formTester = tester.newFormTester(gridForm);
    formTester.setValue(fromDay, "12/32/2009");
    formTester.setValue(toDay, "12/1/2009");
    formTester.submit(button);
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
  }

  /**
   * Tests the GridInfo page to see if an invalid period is handled correctly.
   */
  @Test
  public void testGridInfoPageOnSubmitInvalidPeriod() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    // Invalid period.
    FormTester formTester = tester.newFormTester(gridForm);
    formTester.setValue(fromDay, "12/4/2009");
    formTester.setValue(toDay, "12/2/2009");
    formTester.submit(button);
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
  }

  /**
   * Tests the GridInfo page to see if the subsource checkbox rendered.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testGridInfoPageOnSubmitSubsource() throws Exception {

    // Start up the WicketTester and check that the page renders.
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);

    FormTester formTester = tester.newFormTester(gridForm);
    formTester.setValue(fromDay, "12/3/2009");
    formTester.setValue(toDay, "12/4/2009");
    formTester.setValue("subsource", true);
    formTester.submit(button);
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
  }
  /**
   * Tests the GridInfo page to see if its link to the home page works correctly.
   */
  @Test
  public void testGridInfoPageHomePageLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    // Click on link to home page.
    tester.clickLink("HomePageLink", false);
    tester.assertRenderedPage(HomePage.class);
  }

  /**
   * Tests the GridInfo page to see if its link to the Stoplight page works correctly.
   */
  @Test
  public void testGridInfoPageStoplightLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    tester.clickLink("StoplightPageLink", false);
    tester.assertRenderedPage(StoplightPage.class);
  }

  /**
   * Tests the GridInfo page to see if its link to the GridInfo page works correctly.
   */
  @Test
  public void testGridInfoPageGridInfoLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    tester.clickLink("GridInfoPageLink", false);
    tester.assertRenderedPage(GridInfoPage.class);
  }
  
  /**
   * Tests the GridInfo page to see if its link to the Thresholds page works correctly.
   */
  @Test
  public void testGridInfoPageThresholdsLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    tester.clickLink("ThresholdsPageLink", false);
    tester.assertRenderedPage(ThresholdsPage.class);
  }

  /**
   * Tests the GridInfo page to see if its link to the GridInfo page works correctly.
   */
  @Test
  public void testGridInfoPageSourceSummaryLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(GridInfoPage.class);
    tester.assertRenderedPage(GridInfoPage.class);
    tester.clickLink("SourceSummaryPageLink", false);
    tester.assertRenderedPage(SourceSummaryPage.class);
  }

}