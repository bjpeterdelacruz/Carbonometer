package edu.hawaii.wattdepot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import org.wattdepot.client.WattDepotClient;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Tests the methods in the WattDepotCommand class that are used to get data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestWattDepotCommand {

  /** Holds a list of timestamps. */
  private static final List<XMLGregorianCalendar> timestamps =
      new ArrayList<XMLGregorianCalendar>();
  /** Holds a list of data. */
  private static final List<Double> results = new ArrayList<Double>();

  /**
   * Tests the getCarbonContentData method to see if length of list of timestamps is correct.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testCarbonContentDataTimestampsList() throws Exception {
    WattDepotCommand command = new WattDepotCommand();
    String tstamp = "2009-11-01";
    timestamps.add(Tstamp.makeTimestamp(tstamp));
    command.getCarbonContentData(timestamps, results);
    assertTrue("Checking if length of list is not correct", command.isTimestampsListNotFilled());

    for (int i = 0; i < 23; i++) {
      timestamps.add(Tstamp.makeTimestamp(tstamp));
    }
    command.getCarbonContentData(timestamps, results);
    assertFalse("Checking if length of list is correct", command.isTimestampsListNotFilled());
  }

  /**
   * Tests the getCarbonContentData method to see if list to hold data is empty.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testCarbonContentDataResultsList() throws Exception {
    WattDepotCommand command = new WattDepotCommand();
    results.clear();
    command.getCarbonContentData(timestamps, results);
    assertFalse("Checking if list of results is empty", command.isResultsListNotEmpty());

    results.clear();
    results.add(new Double(0.0));
    command.getCarbonContentData(timestamps, results);
    assertTrue("Checking if list of results is not empty", command.isResultsListNotEmpty());
  }

  /**
   * Tests the getSubSources method to see if list contains all of the power sources on Oahu.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testGetSubSources() throws Exception {
    WattDepotCommand command = new WattDepotCommand();
    WattDepotClient client = new WattDepotClient("http://server.wattdepot.org:8182/wattdepot/");
    List<String> subsources =
        command.getSubsources(client.getSource("SIM_OAHU_GRID"), new ArrayList<String>());
    assertEquals("Checking number of power sources", subsources.size(), 18);
  }
  
  /**
   * Tests the getSubsources method to see if list contains subsources of a power source.
   * 
   * @throws Exception If problems occur.
   */
  @Test
  public void testGetSubsources() throws Exception {
    WattDepotCommand command = new WattDepotCommand();
    WattDepotClient client = new WattDepotClient("http://server.wattdepot.org:8182/wattdepot/");
    List<String> subsources = command.getSubSources(client.getSource("SIM_OAHU_GRID"));
    assertEquals("Checking number of power sources", subsources.size(), 4);
  }

}