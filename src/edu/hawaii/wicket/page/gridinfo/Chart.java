package edu.hawaii.wicket.page.gridinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Processes data that are used in the chart.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class Chart {
  /** Holds value for extended encode base-64. **/
  private String charCode = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.";
  private String chxr = "1,0,50,10";

  /**
   * Creates a URI for the chart.
   * 
   * @param title Title of the chart.
   * @param labelX X-axis label.
   * @param data Data point.
   * @param color Line color for each power source.
   * @param legend String to make a chart legend.
   * @param marker String to make chart marker.
   * @return Chart URI.
   */
  public String getChartUri(String title, String labelX, String data, String color, String legend, 
      String marker) {

    String chartUri =
        "http://chart.apis.google.com/chart?chtt=" + title + "&chts=000000,17&chs=550x400"
            + "&chxt=y&chxr=" + chxr + "&chf=bg,s,ffffff" + "&chxt=x,y" + "&chxl=0:|" + labelX
            + "&cht=lc" + "&chd=e:" + data + "&chco=" + color + "&chm=" + marker 
            + "&chdl=" + legend + "&chdlp=t";
    return chartUri;
  }

  /**
   * Gets the chart title.
   * 
   * @param powerType Power type, either carbon or energy.
   * @param unit Data point unit.
   * @param startDay Start day value.
   * @param endDay End day value.
   * @return Chart title.
   */
  public String getChartTitle(String powerType, String unit, XMLGregorianCalendar startDay,
      XMLGregorianCalendar endDay) {
    String day1 = startDay.getMonth() + "-" + startDay.getDay() + "-" + startDay.getYear();
    String day2 = endDay.getMonth() + "-" + endDay.getDay() + "-" + endDay.getYear();
    return powerType + " Chart (in " + unit + ") Between " + day1 + " and " + day2;
  }

  /**
   * Gets a label from start day to end day.
   * 
   * @param startDay Start day.
   * @param endDay End day.
   * @param granularityChoice Granularity choice.
   * @return Day string formatted for chart label.
   */
  public String getDayLabel(XMLGregorianCalendar startDay, XMLGregorianCalendar endDay, 
      String granularityChoice) {

    List<String> dayList = new ArrayList<String>();
    String day = "";
    int interval; 
    
    if (granularityChoice.equalsIgnoreCase("week")) {
      interval = 7;
    }
    else {
      interval = 1;
    }

    XMLGregorianCalendar tempFrom = startDay;
    XMLGregorianCalendar tempTo = Tstamp.incrementDays(endDay, 1);

    while (Tstamp.greaterThan(tempTo, tempFrom)) {
      day = tempFrom.getMonth() + "-" + tempFrom.getDay();
      dayList.add(day);
      tempFrom = Tstamp.incrementDays(tempFrom, interval);
    }
    String dayLabel = dayList.toString();
    dayLabel = dayLabel.replace("]", "");
    dayLabel = dayLabel.replace("[", "");
    dayLabel = dayLabel.replace(", ", "|");

    return dayLabel;
  }

  /**
   * Converts data to chart string.
   * 
   * @param data Power data.
   * @return Power data as a string.
   */
  public String getDataToString(List<?> data) {

    // Convert data to data point.
    String dataPoint = data.toString();
    dataPoint = dataPoint.replace("]", "");
    dataPoint = dataPoint.replace("[", "");
    dataPoint = dataPoint.replace(", ", "");

    return dataPoint;
  }

  /**
   * Converts list of data to extended code.
   * 
   * @param dataList List of power data.
   * @return Power data as an encoded string.
   */
  public String getEncodedDataList(List<Double> dataList) {
    List<String> encodedData = new ArrayList<String>();
    double scale = 0.0;
    
    if (Collections.max(dataList) <= 10) {
      scale = 10.0;
      chxr = "1,0,10,1";
    }
    else if (Collections.max(dataList) <= 50) {
      scale = 50.0;
      chxr = "1,0,50,10";
    }
    else if (Collections.max(dataList) <= 100) {
      scale = 100.0;
      chxr = "1,0,100,10";
    }
    else {
      scale = 1000.0;
      chxr = "1,0,1000,100";
    }
    for (double data : dataList) {      
      double newData = data * (4096 / scale);
      encodedData.add(this.getEncodedBase64((int) newData));
    }

    // Convert dataList to string.
    return this.getDataToString(encodedData);
  }

  /**
   * Converts an integer to base 64. Number cannot exceed 4095.
   * 
   * @param number Power data.
   * @return Number in extended encode value.
   */
  public String getEncodedBase64(Integer number) {
    return "" + charCode.charAt(number / 64) + charCode.charAt(number % 64);
  }  
}