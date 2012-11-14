package edu.hawaii.wicket.page.thresholds;

import java.util.Collections;
import java.util.List;

/**
 * Calculates the maximum and minimum threshold values.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class Thresholds {

  /**
   * Private constructor to prevent class from being instantiated.
   */
  public Thresholds() {
    // empty constructor
  }

  /**
   * Calculates the maximum threshold value.
   * 
   * @param results Data for day.
   * @return maxThreshold Maximum threshold value.
   */
  public Double getMaxThreshold(List<Double> results) {
    // Use hardcoded maximum threshold value if data for a day is unavailable.
    if (results.isEmpty()) {
      return new Double(2000.0);
    }

    double ave = (Collections.max(results) + Collections.min(results)) / 2.0;
    double max = (ave + Collections.max(results)) / 2.0;

    return max;
  }

  /**
   * Calculates the minimum threshold value.
   * 
   * @param results Data for day.
   * @return minThreshold Minimum threshold value.
   */
  public Double getMinThreshold(List<Double> results) {
    // Use hardcoded minimum threshold value if data for a day is unavailable.
    if (results.isEmpty()) {
      return new Double(1500.0);
    }

    double ave = (Collections.max(results) + Collections.min(results)) / 2.0;
    double min = (ave + Collections.min(results)) / 2.0;

    return min;
  }

}