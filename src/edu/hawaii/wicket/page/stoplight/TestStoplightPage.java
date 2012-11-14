package edu.hawaii.wicket.page.stoplight;

import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;
import edu.hawaii.wicket.page.gridinfo.GridInfoPage;
import edu.hawaii.wicket.page.main.HomePage;
import edu.hawaii.wicket.page.srcsummary.SourceSummaryPage;
import edu.hawaii.wicket.page.thresholds.ThresholdsPage;

/**
 * Tests the Stoplight page to see if it is rendered correctly and all of the lights are displayed
 * correctly.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestStoplightPage {

  // TODO: Test countdown timer.

  /** Name of the image that is rendered on the Stoplight page. */
  private static final String STOPLIGHT = "stoplight";
  /** ID of stoplight image. */
  private static final String STOPLIGHT_ID = "id4";
  /** Checks if correct stoplight is displayed. */
  private static final String CHECK_STOPLIGHT = "Checking correct stoplight";
  /** Checks if img tag has src attribute. */
  private static final String CHECK_IMG = "Checking if img tag has src attribute";
  /** Start application with today's date. Change the date for testing purposes. */
  private static Calendar currentTime = Calendar.getInstance();
  /** Src attribute for the img tag. */
  private static final String SRC_ATTRIBUTE = "src";

  /**
   * Tests the Stoplight page to see if it is rendered correctly.
   */
  @Test
  public void testStoplightPageOnLoad() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    // Check that a picture of a stoplight is rendered.
    tester.assertComponent(STOPLIGHT, ImageSource.class);
  }

  /**
   * Tests the Stoplight page to see if it displays a yellow light based on the current value for
   * the minimum and maximum thresholds.
   * 
   * On November 1, 2009, at 8:00 A.M., the carbon emission level was at 1985.49 lbs CO2 / MWh,
   * which is between 1600.00 and 2000.00 lbs CO2 / MWh, so the stoplight should display a yellow
   * light.
   */
  @Test
  public void testStoplightPageYellowLight() {
    synchronized (this) {
      currentTime.set(Calendar.MONTH, 10);
      currentTime.set(Calendar.DATE, 1);
      currentTime.set(Calendar.HOUR, 8);
      currentTime.set(Calendar.MINUTE, 0);
      currentTime.set(Calendar.SECOND, 0);
      currentTime.set(Calendar.AM_PM, Calendar.AM);
    }
    StoplightPage.setDebugMode(true);

    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);

    TagTester imgTag = tester.getTagById(STOPLIGHT_ID);
    assertTrue(CHECK_IMG, imgTag.hasAttribute(SRC_ATTRIBUTE));

    // Uncomment dumpPage method to find id of img tag used to display stoplight.
    // tester.dumpPage();

    assertTrue(CHECK_STOPLIGHT, imgTag.getAttribute(SRC_ATTRIBUTE).contains("yellow"));
  }

  /**
   * Tests the Stoplight page to see if it displays a red light based on the current value for the
   * maximum threshold.
   * 
   * On November 1, 2009, at 1:00 A.M., the carbon emission level was at 2015.20 lbs CO2 / MWh,
   * which is greater than 2000.00 lbs CO2 / MWh, so the stoplight should display a red light.
   */
  @Test
  public void testStoplightPageRedLight() {
    synchronized (this) {
      currentTime.set(Calendar.MONTH, 10);
      currentTime.set(Calendar.DATE, 1);
      currentTime.set(Calendar.HOUR, 1);
      currentTime.set(Calendar.MINUTE, 0);
      currentTime.set(Calendar.SECOND, 0);
      currentTime.set(Calendar.AM_PM, Calendar.AM);
    }
    StoplightPage.setDebugMode(true);

    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);

    TagTester imgTag = tester.getTagById(STOPLIGHT_ID);
    assertTrue(CHECK_IMG, imgTag.hasAttribute(SRC_ATTRIBUTE));

    // tester.dumpPage();

    assertTrue(CHECK_STOPLIGHT, imgTag.getAttribute(SRC_ATTRIBUTE).contains("red"));
  }

  // TODO: Check minimum threshold value.
  /**
   * Tests the Stoplight page to see if it displays a green light based on the current value for the
   * minimum threshold.
   * 
   * On November 28, 2009, at 4:00 A.M., the carbon emission level was at 1806.07 lbs CO2 / MWh,
   * which is less than 2000.00 lbs CO2 / MWh, so the stoplight should display a green light.
   */
  @Test
  public void testStoplightPageGreenLight() {
    synchronized (this) {
      currentTime.set(Calendar.MONTH, 10);
      currentTime.set(Calendar.DATE, 28);
      currentTime.set(Calendar.HOUR, 4);
      currentTime.set(Calendar.MINUTE, 0);
      currentTime.set(Calendar.SECOND, 0);
      currentTime.set(Calendar.AM_PM, Calendar.AM);
    }
    StoplightPage.setDebugMode(true);

    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);

    TagTester imgTag = tester.getTagById(STOPLIGHT_ID);
    assertTrue(CHECK_IMG, imgTag.hasAttribute(SRC_ATTRIBUTE));

    // tester.dumpPage();

    assertTrue(CHECK_STOPLIGHT, imgTag.getAttribute(SRC_ATTRIBUTE).contains("green"));
  }

  /**
   * Tests the Stoplight page to see if it displays an error message and a blank stoplight.
   */
  @Test
  public void testStoplightPageOnError() {
    StoplightPage.setDebugMode(true);
    StoplightPage.setDebugTimestamp(true);

    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);

    TagTester imgTag = tester.getTagById(STOPLIGHT_ID);
    assertTrue(CHECK_IMG, imgTag.hasAttribute(SRC_ATTRIBUTE));

    // tester.dumpPage();

    assertTrue(CHECK_STOPLIGHT, imgTag.getAttribute(SRC_ATTRIBUTE).contains("stoplight"));
    tester.assertLabel("time", "[See error message below stoplight]");
    tester.assertLabel("data", "[See error message below stoplight]");
    tester.assertLabel("error", "Error: Cannot make timestamp.");
  }

  /**
   * Tests the Stoplight page to see if it displays a blank stoplight if there is no data available.
   */
  @Test
  public void testStoplightPageNoData() {
    synchronized (this) {
      currentTime.set(Calendar.YEAR, 2008);
      currentTime.set(Calendar.MONTH, 10);
      currentTime.set(Calendar.DATE, 28);
      currentTime.set(Calendar.HOUR, 4);
      currentTime.set(Calendar.MINUTE, 0);
      currentTime.set(Calendar.SECOND, 0);
      currentTime.set(Calendar.AM_PM, Calendar.AM);
    }
    StoplightPage.setDebugMode(true);
    StoplightPage.setDebugTimestamp(false);

    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);

    TagTester imgTag = tester.getTagById(STOPLIGHT_ID);
    assertTrue(CHECK_IMG, imgTag.hasAttribute(SRC_ATTRIBUTE));

    // tester.dumpPage();

    assertTrue(CHECK_STOPLIGHT, imgTag.getAttribute(SRC_ATTRIBUTE).contains("stoplight"));
    tester.assertLabel("time", "4:00:00 AM HST");
    tester.assertLabel("data", "not available");
    tester.assertLabel("error", "");
  }

  /**
   * Tests the Stoplight page to see if its link to the home page works correctly.
   */
  @Test
  public void testStoplightPageHomePageLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    // Click on home page link.
    tester.clickLink("HomePageLink", false);
    tester.assertRenderedPage(HomePage.class);
  }

  /**
   * Tests the Stoplight page to see if its link to the Stoplight page works correctly.
   */
  @Test
  public void testStoplightPageStoplightLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    tester.clickLink("StoplightPageLink", false);
    tester.assertRenderedPage(StoplightPage.class);
  }

  /**
   * Tests the Stoplight page to see if its link to the GridInfo page works correctly.
   */
  @Test
  public void testStoplightPageGridInfoLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    tester.clickLink("GridInfoPageLink", false);
    tester.assertRenderedPage(GridInfoPage.class);
  }
  
  /**
   * Tests the Stoplight page to see if its link to the Thresholds page works correctly.
   */
  @Test
  public void testStoplightPageThresholdsLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    tester.clickLink("ThresholdsPageLink", false);
    tester.assertRenderedPage(ThresholdsPage.class);
  }
  
  /**
   * Tests the Stoplight page to see if its link to the Source Summary page works correctly.
   */
  @Test
  public void testStoplightPageSourceSummaryLink() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(StoplightPage.class);
    tester.assertRenderedPage(StoplightPage.class);
    tester.clickLink("SourceSummaryPageLink", false);
    tester.assertRenderedPage(SourceSummaryPage.class);
  }

  /**
   * Returns the date used in the test cases.
   * 
   * @return Date used in the test cases.
   */
  public Date getCurrentTime() {
    return currentTime.getTime();
  }

}