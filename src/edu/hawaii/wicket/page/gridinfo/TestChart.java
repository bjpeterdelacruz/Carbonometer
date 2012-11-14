package edu.hawaii.wicket.page.gridinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.wattdepot.WattDepotCommand;

/**
 * Tests the methods in the Chart class.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestChart {

  /**
   * Tests the getChartTitle method.
   */
  @Test
  public void testGetChartTitle() {
    Chart chart = new Chart();
    String chartTitle = "";

    try {
      chartTitle =
          chart.getChartTitle("Energy", "MW", Tstamp.makeTimestamp("2009-12-15"), Tstamp
              .makeTimestamp("2009-12-16"));
    }
    catch (Exception e) {
      fail("Cannot create timestamp.");
    }

    String expectedOutput = "Energy Chart (in MW) Between 12-15-2009 and 12-16-2009";
    assertEquals("Checking chart title", chartTitle, expectedOutput);
  }

  /**
   * Tests the getDayLabel method.
   */
  @Test
  public void testGetDayLabel() {
    Chart chart = new Chart();
    String dayLabel = "";

    try {
      dayLabel =
          chart.getDayLabel(Tstamp.makeTimestamp("2009-12-15"), Tstamp.makeTimestamp("2009-12-20"),
              "Hour");
    }
    catch (Exception e) {
      fail("Cannot create timestamp.");
    }

    assertEquals("Checking chart title", dayLabel, "12-15|12-16|12-17|12-18|12-19|12-20");
  }

  /**
   * Tests the getEncodedDataList method.
   */
  @Test
  public void testGetEncodedDataList() {
    Chart chart = new Chart();
    WattDepotCommand command = new WattDepotCommand();

    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-02T00:00:00.000-10:00");
      toDay = Tstamp.makeTimestamp("2009-12-03T00:00:00.000-10:00");
    }
    catch (Exception e) {
      fail("Cannot create timestamp.");
    }

    List<Double> results = command.getChartData(fromDay, toDay, "SIM_OAHU_GRID", "Energy", "Hour");

    String expectedOutput = "gSgKf7f1f7g5ibjhkxmhnjo1qNquqdqJqVrxzh832lnCgagIgSgKf7f1f7g5ibjhkxmh";
    expectedOutput += "njo1qNquqdqJqVrxzh832lnCgagI";

    assertEquals("Checking encoded data list", chart.getEncodedDataList(results), expectedOutput);
  }

}