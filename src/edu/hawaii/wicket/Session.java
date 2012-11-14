package edu.hawaii.wicket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;

/**
 * Stores data per user of this web application.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class Session extends WebSession {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Date entered by a user. */
  private String date;
  /** Start day entered by a user. */
  private String startDay;
  /** End day entered by a user. */
  private String endDay;
  /** Type of power chosen by a user. */
  private String powerType;
  /** Power source chosen by a user. */
  private String powerSource;
  /** Holds a list of timestamps for one 24-hour period, from 00:00:00 to 23:00:00. */
  private final List<XMLGregorianCalendar> TIMESTAMPS = new ArrayList<XMLGregorianCalendar>();
  /** Holds a list of data for one 24-hour period, from 00:00:00 to 23:00:00. */
  private final List<Double> RESULTS = new ArrayList<Double>();
  /** Holds the maximum threshold value. */
  private double maxThreshold;
  /** Holds the minimum threshold value. */
  private double minThreshold;
  /** Counts the number of times maximum threshold has been crossed. */
  private int maxCounter = 0;
  /** URI of the chart. */
  private String chartUri;
  /** List of power data for a given power source. */
  private List<Double> powerData = new ArrayList<Double>();

  // TODO: Use recursive method to populate list of power sources instead of hardcoding them.
  /** List of power sources. */
  private List<String> listSources =
      Arrays
          .asList("SIM_AES", "SIM_HONOLULU", "SIM_HONOLULU_8", "SIM_HONOLULU_9", "SIM_HPOWER",
              "SIM_IPP", "SIM_KAHE", "SIM_KAHE_1", "SIM_KAHE_2", "SIM_KAHE_3", "SIM_KAHE_4",
              "SIM_KAHE_5", "SIM_KAHE_6", "SIM_KAHE_7", "SIM_KALAELOA", "SIM_OAHU_GRID",
              "SIM_WAIAU", "SIM_WAIAU_10", "SIM_WAIAU_5", "SIM_WAIAU_6", "SIM_WAIAU_7",
              "SIM_WAIAU_8", "SIM_WAIAU_9");

  /** List of colors for each line that represents a power source. */
  private List<String> sourceColor =
      Arrays.asList("FF4500", "A52A2A", "4E387E", "32CD32", "6A5ACD", "DAA520", "8A2BE2", "153E7E",
          "6960EC", "8D38C9", "728FC3", "488AC7", "56A5EC", "41627E", "E4317F", "00FFFF", "4B0082",
          "7D0552", "B048B5", "6A287E", "B93B8F");

  /** List of types of data. */
  private List<String> listType = Arrays.asList("Energy", "Carbon");
  /** List of granularity options. */
  private List<String> listGranularity = Arrays.asList("Hour", "Day", "Week");
  /** True if subsource should be included, false otherwise. */
  private boolean checkSubsource;
  /** Interval of period is default to 1 day. */
  private int interval = 1;
  /** Unit for produce chart. */
  private String unit;

  /**
   * Creates a new session for a user.
   * 
   * @param application This application.
   * @param request This request.
   */
  public Session(WebApplication application, Request request) {
    super(request);
  }

  /**
   * Gets the date entered by this user.
   * 
   * @return Date entered by this user.
   */
  public String getDate() {
    return date;
  }

  /**
   * Sets the date entered by this user.
   * 
   * @param date Date entered by this user.
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * Gets a list of timestamps for one 24-hour period.
   * 
   * @return A list of timestamps for one 24-hour period.
   */
  public List<XMLGregorianCalendar> getTimestamps() {
    return TIMESTAMPS;
  }

  /**
   * Sets a list of timestamps for one 24-hour period.
   * 
   * @param timestamps A list of timestamps for one 24-hour period.
   */
  public void setTimestamps(List<XMLGregorianCalendar> timestamps) {
    TIMESTAMPS.clear();

    for (XMLGregorianCalendar tstamp : timestamps) {
      TIMESTAMPS.add(tstamp);
    }
  }

  /**
   * Gets a list of data for one 24-hour period.
   * 
   * @return A list of data for one 24-hour period.
   */
  public List<Double> getResults() {
    return RESULTS;
  }

  /**
   * Gets the maximum threshold value.
   * 
   * @return Maximum threshold value.
   */
  public double getMaxThreshold() {
    return maxThreshold;
  }

  /**
   * Sets the maximum threshold value.
   * 
   * @param max New maximum threshold value.
   */
  public void setMaxThreshold(double max) {
    maxThreshold = max;
  }

  /**
   * Gets the minimum threshold value.
   * 
   * @return Minimum threshold value.
   */
  public double getMinThreshold() {
    return minThreshold;
  }

  /**
   * Sets the minimum threshold value.
   * 
   * @param min New minimum threshold value.
   */
  public void setMinThreshold(double min) {
    minThreshold = min;
  }

  /**
   * Gets the average threshold value.
   * 
   * @return Average threshold value.
   */
  public double getAveThreshold() {
    return (maxThreshold + minThreshold) / 2.0;
  }

  /**
   * Gets the number of times maximum threshold has been crossed.
   * 
   * @return Number of times maximum threshold has been crossed.
   */
  public int getMaxCounter() {
    return maxCounter;
  }

  /**
   * Resets the number of times maximum threshold has been crossed back to 0.
   */
  public void resetMaxCounter() {
    maxCounter = 0;
  }

  /**
   * Counts the number of times maximum threshold has been crossed.
   */
  public void incrementMaxCounter() {
    maxCounter++;
  }

  /**
   * Gets the startDay value.
   * 
   * @return startDay value.
   */
  public String getStartDay() {
    return startDay;
  }

  /**
   * Sets the startDay value.
   * 
   * @param date startDay value.
   */
  public void setFromDay(String date) {
    this.startDay = date;
  }

  /**
   * Gets the endDay value.
   * 
   * @return endDay value.
   */
  public String getEndDay() {
    return this.endDay;
  }

  /**
   * Sets the endDay value.
   * 
   * @param date endDay value.
   */
  public void setToDay(String date) {
    this.endDay = date;
  }

  /**
   * Gets the endDay value.
   * 
   * @return endDay value.
   */
  public String getToDay() {
    return this.endDay;
  }

  /**
   * Gets the powerSource value.
   * 
   * @return powerSource value.
   */
  public String getPowerSource() {
    return this.powerSource;
  }

  /**
   * Sets the powerSource value.
   * 
   * @param source powerSource value.
   */
  public void setPowerSource(String source) {
    this.powerSource = source;
  }

  /**
   * Gets the chartUri value.
   * 
   * @return chartUri value.
   */
  public String getChartUri() {
    return this.chartUri;
  }

  /**
   * Sets the chartUri value.
   * 
   * @param uri chartUri value.
   */
  public void setChartUri(String uri) {
    this.chartUri = uri;
  }

  /**
   * Gets the powerData value.
   * 
   * @return powerData value.
   */
  public List<Double> getPowerData() {
    return this.powerData;
  }

  /**
   * Sets the powerData value.
   * 
   * @param data powerData value.
   */
  public void setPowerData(List<Double> data) {
    this.powerData = data;
  }

  /**
   * Gets the powerType value.
   * 
   * @return powerType value.
   */
  public String getPowerType() {
    return this.powerType;
  }

  /**
   * Sets the powerType value.
   * 
   * @param type powerType value.
   */
  public void setPowerType(String type) {
    this.powerType = type;
  }

  /**
   * Gets the list of power sources.
   * 
   * @return List of power sources.
   */
  public List<String> getListSources() {
    return this.listSources;
  }

  /**
   * Sets the list of power sources.
   * 
   * @param listSources List of power sources.
   */
  public void setListSources(List<String> listSources) {
    this.listSources.clear();
    for (String source : listSources) {
      this.listSources.add(source);
    }
  }

  /**
   * Gets the list of colors.
   * 
   * @return List of colors.
   */
  public List<String> getSourceColor() {
    return this.sourceColor;
  }

  /**
   * Gets the list of types of data.
   * 
   * @return List of types of data.
   */
  public List<String> getListType() {
    return this.listType;
  }

  /**
   * Gets the list of granularity options.
   * 
   * @return List of granularity options.
   */
  public List<String> getListGranularity() {
    return this.listGranularity;
  }

  /**
   * Returns true if subsource should be included, false otherwise.
   * 
   * @return True if subsource should be included, false otherwise.
   */
  public boolean isCheckSubsource() {
    return this.checkSubsource;
  }

  /**
   * True if subsource should be included, false otherwise.
   * 
   * @param checkSubsource True or false.
   */
  public void setCheckSubsource(boolean checkSubsource) {
    this.checkSubsource = checkSubsource;
  }

  /**
   * Gets the interval period.
   * 
   * @return Interval value.
   */
  public int getInterval() {
    return this.interval;
  }

  /**
   * Interval set to 7 for week otherwise it.
   * 
   * @param granularityChoice Granularity choice.
   */
  public void setInterval(String granularityChoice) {
    if (granularityChoice.equalsIgnoreCase("week")) {
      this.interval = 7;
    }
    else if (granularityChoice.equalsIgnoreCase("day")) {
      this.interval = 1;
    }
  }

  /**
   * Gets the unit for chart.
   * 
   * @return unit value.
   */
  public String getUnit() {
    return this.unit;
  }

  /**
   * Unit sets different for each granularity choice and power type.
   * 
   * @param granularityChoice Granularity choice.
   * @param type Power type
   */
  public void setUnit(String granularityChoice, String type) {
    if (granularityChoice.equalsIgnoreCase("hour")) {
      if (type.equalsIgnoreCase("energy")) {
        this.unit = "MW";
      }
      else {
        this.unit = "KW";
      }
    }
    else {
      if (type.equalsIgnoreCase("energy")) {
        this.unit = "GW";
      }
      else {
        this.unit = "MW";
      }
    }
  }
}