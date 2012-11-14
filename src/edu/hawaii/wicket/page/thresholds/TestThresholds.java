package edu.hawaii.wicket.page.thresholds;

import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;

/**
 * Tests the methods in the Thresholds class.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestThresholds {

  /**
   * Tests the getMaxThreshold method to see if it returns maximum threshold value.
   */
  @Test
  public void testGetMaxThreshold() {
    Thresholds thresholds = new Thresholds();
    List<Double> data = new ArrayList<Double>();
    int maxThreshold = thresholds.getMaxThreshold(data).intValue();
    assertEquals("Checking max threshold", maxThreshold, 2000);
    data.add(10.0);
    data.add(10.0);
    maxThreshold = thresholds.getMaxThreshold(data).intValue();
    assertEquals("Checking max threshold", maxThreshold, 10);
  }

  /**
   * Tests the getMinThreshold method to see if it returns minimum threshold value.
   */
  @Test
  public void testGetMinThreshold() {
    Thresholds thresholds = new Thresholds();
    List<Double> data = new ArrayList<Double>();
    int minThreshold = thresholds.getMinThreshold(data).intValue();
    assertEquals("Checking min threshold", minThreshold, 1500);
    data.add(20.0);
    data.add(20.0);
    minThreshold = thresholds.getMaxThreshold(data).intValue();
    assertEquals("Checking min threshold", minThreshold, 20);
  }

}