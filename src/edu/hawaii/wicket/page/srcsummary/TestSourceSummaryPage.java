package edu.hawaii.wicket.page.srcsummary;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;
import edu.hawaii.wicket.page.thresholds.ThresholdsPage;

/**
 * Tests the Source Summary page to see if it is rendered correctly when a user selects a power
 * source from a drop down list.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestSourceSummaryPage {

  /** Name of the form that contains the drop down list and button. */
  private static final String FORM = "sourceList";
  /** Name of the button used to query data. */
  private static final String BUTTON = "generate";
  /** ID of the drop down list that contains names of power sources. */
  private static final String SOURCE_LIST = "source";

  /**
   * Tests the Source Summary page to see if it is rendered correctly when it first loads.
   */
  @Test
  public void testSourceSummaryPageOnLoad() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.submit(BUTTON);
  }
  
  /**
   * Tests the Source Summary page to see if it is rendered correctly when the Submit button is
   * clicked.
   */
  @Test
  public void testSourceSummaryPageOnSubmitPropsVirtual() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.select(SOURCE_LIST, 15); // SIM_OAHU_GRID
    formTester.submit(BUTTON);
    tester.assertLabel("properties", "None");
  }
  
  /**
   * Tests the Source Summary page to see if it is rendered correctly when the Submit button is
   * clicked.
   */
  @Test
  public void testSourceSummaryPageOnSubmitPropsNonvirtual() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.select(SOURCE_LIST, 2); // SIM_HONOLULU_8
    formTester.submit(BUTTON);
    tester.assertLabel("properties", "Carbon Intensity: 2240 lbs CO2 / MWh, Fuel Type: LSFO");
  }
  
  /**
   * Tests the Source Summary page to see if it is rendered correctly when the Submit button is
   * clicked.
   */
  @Test
  public void testSourceSummaryPageOnSubmitLocVirtual() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.select(SOURCE_LIST, 7); // SIM_KAHE_1
    formTester.submit(BUTTON);
    tester.assertLabel("location", "Data not available at the moment.");
  }
  
  /**
   * Tests the Source Summary page to see if it is rendered correctly when the Submit button is
   * clicked.
   */
  @Test
  public void testSourceSummaryPageOnSubmitLocNonvirtual() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.select(SOURCE_LIST, 6); // SIM_KAHE
    formTester.submit(BUTTON);
    tester.assertLabel("coordinates", "Data not available at the moment.");
  }
  
  /**
   * Tests the Source Summary page to see if it is rendered correctly when the Submit button is
   * clicked.
   */
  @Test
  public void testSourceSummaryPageOnSubmitCoord() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.select(SOURCE_LIST, 1); // SIM_HONOLULU
    formTester.submit(BUTTON);
    tester.assertLabel("coordinates", "21.306278, -157.863997, 0");
  }

  /**
   * Tests the Source Summary page to see if its link to the Stoplight page works correctly.
   */
  @Test
  public void testSourceSummaryPageStoplightLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);
    tester.clickLink("StoplightPageLink", false);
    tester.assertRenderedPage(StoplightPage.class);
  }

  /**
   * Tests the Source Summary page to see if its link to the GridInfo page works correctly.
   */
  @Test
  public void testSourceSummaryPageGridInfoLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);
    tester.clickLink("GridInfoPageLink", false);
    tester.assertRenderedPage(GridInfoPage.class);
  }
  
  /**
   * Tests the Source Summary page to see if its link to the Thresholds page works correctly.
   */
  @Test
  public void testSourceSummaryPageThresholdsLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);
    tester.clickLink("ThresholdsPageLink", false);
    tester.assertRenderedPage(ThresholdsPage.class);
  }
  
  /**
   * Tests the Source Summary page to see if its link to the Source Summary page works correctly.
   */
  @Test
  public void testSourceSummaryPageSourceSummaryLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(SourceSummaryPage.class);
    tester.assertRenderedPage(SourceSummaryPage.class);
    tester.clickLink("SourceSummaryPageLink", false);
    tester.assertRenderedPage(SourceSummaryPage.class);
  }

}