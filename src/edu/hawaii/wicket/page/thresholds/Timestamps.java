package edu.hawaii.wicket.page.thresholds;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Creates a list of timestamps, from 00:00:00 to 23:00:00.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class Timestamps {

  /**
   * Private constructor to prevent class from being instantiated.
   */
  private Timestamps() {
    // empty constructor
  }

  /**
   * Creates a list of timestamps, from 00:00:00 to 23:00:00.
   * 
   * @param tstamp Date entered by the user in the textbox.
   * @return A list of timestamps for one 24-hour period, from 00:00:00 to 23:00:00.
   */
  public static List<XMLGregorianCalendar> createTimestamps(XMLGregorianCalendar tstamp) {
    List<XMLGregorianCalendar> timestampsList = new ArrayList<XMLGregorianCalendar>();

    for (int i = 0; i < 24; i++) {
      timestampsList.add(Tstamp.incrementHours(tstamp, i));
    }
    return timestampsList;
  }

}