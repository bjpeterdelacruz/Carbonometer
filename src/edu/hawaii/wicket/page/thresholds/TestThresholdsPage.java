package edu.hawaii.wicket.page.thresholds;

import static org.junit.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.srcsummary.SourceSummaryPage;
import edu.hawaii.wicket.page.stoplight.StoplightPage;

/**
 * Tests the Thresholds page to see if it is rendered correctly when a valid date is given, invalid
 * input is handled correctly, and the user is informed that no data is available for some or all
 * hours of the day.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestThresholdsPage {

  /** Name of the textbox in which a user can enter a date. */
  private static final String DATE_LABEL = "date";
  /** Name of the form that contains the textbox and button. */
  private static final String FORM = "form";
  /** Name of the button used to query data. */
  private static final String BUTTON = "button";

  /**
   * Tests the Thresholds page to see if it is rendered correctly when it first loads.
   */
  @Test
  public void testThresholdsPageOnLoad() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);
    tester.assertRenderedPage(ThresholdsPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    assertEquals("Checking form", (new SimpleDateFormat("MM/d/yy", Locale.US)).format(Calendar
        .getInstance().getTime()), formTester.getTextComponentValue(DATE_LABEL));
  }

  /**
   * Tests the Thresholds page to see if it is rendered correctly after the user enters a date and
   * clicks on the submit button.
   */
  @Test
  public void testThresholdsPageOnSubmit() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.setValue(DATE_LABEL, "2009-11-20");
    formTester.submit(BUTTON);
    tester.assertRenderedPage(ThresholdsPage.class);
  }

  /**
   * Tests the Thresholds page to see if it informs the user that there is no data for some or all
   * hours of the day.
   */
  @Test
  public void testThresholdsNoData() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.setValue(DATE_LABEL, "11/01/08");
    formTester.submit(BUTTON);
    tester.assertLabel("error", "No data available for some or all hours of 11/01/08 because"
        + " either the connection to the WattDepot server timed out or there is no data for that"
        + " day.");
  }
  
  /**
   * Tests the Thresholds page to see if it informs the user about the minimum and maximum
   * threshold values for a given day.
   */
  @Test
  public void testThresholdsMinMax() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);

    FormTester formTester = tester.newFormTester(FORM);
    formTester.setValue(DATE_LABEL, "12/16/09");
    formTester.submit(BUTTON);
    tester.assertLabel("min", "Minimum threshold value: 1649.52 lbs CO2 / MWh");
    tester.assertLabel("max", "Maximum threshold value: 1755.33 lbs CO2 / MWh");
  }
  
  /**
   * Tests the Thresholds page to see if its link to the Stoplight page works correctly.
   */
  @Test
  public void testThresholdsPageStoplightLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);
    tester.assertRenderedPage(ThresholdsPage.class);
    tester.clickLink("StoplightPageLink", false);
    tester.assertRenderedPage(StoplightPage.class);
  }

  /**
   * Tests the Thresholds page to see if its link to the GridInfo page works correctly.
   */
  @Test
  public void testThresholdsPageGridInfoLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);
    tester.assertRenderedPage(ThresholdsPage.class);
    tester.clickLink("GridInfoPageLink", false);
    tester.assertRenderedPage(GridInfoPage.class);
  }
  
  /**
   * Tests the Thresholds page to see if its link to the Thresholds page works correctly.
   */
  @Test
  public void testThresholdsPageThresholdsLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);
    tester.assertRenderedPage(ThresholdsPage.class);
    tester.clickLink("ThresholdsPageLink", false);
    tester.assertRenderedPage(ThresholdsPage.class);
  }
  
  /**
   * Tests the Thresholds page to see if its link to the Source Summary page works correctly.
   */
  @Test
  public void testThresholdsPageSourceSummaryLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(ThresholdsPage.class);
    tester.assertRenderedPage(ThresholdsPage.class);
    tester.clickLink("SourceSummaryPageLink", false);
    tester.assertRenderedPage(SourceSummaryPage.class);
  }

}