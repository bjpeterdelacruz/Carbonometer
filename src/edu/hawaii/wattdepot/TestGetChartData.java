package edu.hawaii.wattdepot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import java.util.ArrayList;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Tests the getChartData method in the WattDepotCommand class that is used to get data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestGetChartData {

  /** Holds a list of power data. */
  private List<Double> result = new ArrayList<Double>();
  /** Command used to get data from the WattDepot server. */
  private WattDepotCommand command;
  /** Power source assertion message. */
  private static final String CHECK_SOURCE = "Checking if power source is valid";
  /** Hour choice from granularity. */
  private static final String HOUR = "Hour";
  /** Displayed if problems occur when creating a timestamp. */
  private static final String TSTAMP_ERROR_MESSAGE = "Cannot create timestamp.";

  /**
   * Tests for invalid period.
   */
  @Before
  @Test
  public void testInvalidPeriod() {
    result.clear();
    command = new WattDepotCommand();
    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-04");
      toDay = Tstamp.makeTimestamp("2009-11-30");
    }
    catch (Exception e) {
      fail(TSTAMP_ERROR_MESSAGE);
    }
    result = command.getChartData(fromDay, toDay, "SIM_OAHU_GRID", "Energy", HOUR);
    assertEquals("Checking if period is valid", result.size(), 0);
  }

  /**
   * Tests if power source is valid using energy option.
   */
  @Test
  public void testInvalidSource() {
    result.clear();
    command = new WattDepotCommand();
    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-01");
      toDay = Tstamp.makeTimestamp("2009-12-04");
    }
    catch (Exception e) {
      fail(TSTAMP_ERROR_MESSAGE);
    }
    result = command.getChartData(fromDay, toDay, "SIM_SEATTLE", "Energy", HOUR);
    assertEquals(CHECK_SOURCE, result.size(), 0);
  }

  /**
   * Tests if power source is valid using carbon option.
   */
  @Test
  public void testValidSourceCarbon() {
    result.clear();
    command = new WattDepotCommand();
    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-01T00:00:00.000-10:00");
      toDay = Tstamp.makeTimestamp("2009-12-02T00:00:00.000-10:00");
    }
    catch (Exception e) {
      fail(TSTAMP_ERROR_MESSAGE);
    }
    result = command.getChartData(fromDay, toDay, "SIM_OAHU_GRID", "Carbon", HOUR);
    assertFalse(CHECK_SOURCE, result.isEmpty());
  }

  /**
   * Tests if power source is valid using energy option.
   */
  @Test
  public void testValidSourceEnergy() {
    result.clear();
    command = new WattDepotCommand();
    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-02T00:00:00.000-10:00");
      toDay = Tstamp.makeTimestamp("2009-12-03T00:00:00.000-10:00");
    }
    catch (Exception e) {
      fail(TSTAMP_ERROR_MESSAGE);
    }
    result = command.getChartData(fromDay, toDay, "SIM_OAHU_GRID", "Energy", HOUR);
    assertFalse(CHECK_SOURCE, result.isEmpty());
  }

  /**
   * Tests if power source is valid using energy option.
   */
  @Test
  public void testInvalidPowerType() {
    result.clear();
    command = new WattDepotCommand();
    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp("2009-12-01T00:00:00.000-10:00");
      toDay = Tstamp.makeTimestamp("2009-12-03T00:00:00.000-10:00");
    }
    catch (Exception e) {
      fail(TSTAMP_ERROR_MESSAGE);
    }
    result = command.getChartData(fromDay, toDay, "SIM_KAHE_1", "fuel", HOUR);
    assertTrue(CHECK_SOURCE, command.isWattDepotExceptionThrown());
  }

}