package edu.hawaii.wicket.page.thresholds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Tests the createTimestamps method to see if list of twenty-four timestamps is created.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestTimestamps {

  /**
   * Tests the createTimestamps method to see if list of twenty-four timestamps is created.
   */
  @Test
  public void testCreateTimestamps() {
    XMLGregorianCalendar timestamp = null;
    try {
      timestamp = Tstamp.makeTimestamp("2009-12-05");
    }
    catch (Exception e) {
      fail("Timestamp cannot be created.");
    }

    List<XMLGregorianCalendar> timestamps = Timestamps.createTimestamps(timestamp);
    assertEquals("Checking number of timestamps", timestamps.size(), 24);
    int i = 0;
    for (XMLGregorianCalendar currentTimestamp : timestamps) {
      if (!currentTimestamp.equals(Tstamp.incrementHours(timestamp, i))) {
        fail("Timestamp not equal to expected value.");
      } // end if
      i++;
    } // end for
  }

}