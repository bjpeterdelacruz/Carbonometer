package edu.hawaii.wattdepot;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.wattdepot.client.WattDepotClient;
import org.wattdepot.client.WattDepotClientException;
import org.wattdepot.resource.property.jaxb.Property;
import org.wattdepot.resource.source.jaxb.Source;
import org.wattdepot.resource.source.summary.jaxb.SourceSummary;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * Establishes connection to the WattDepot server and provides methods used to get data from it.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class WattDepotCommand {

  /** URL of the WattDepot server. */
  private static final String HOST_URI = "http://server.wattdepot.org:8182/wattdepot/";
  /** Holds an instance of the WattDepot client. */
  protected static final WattDepotClient CLIENT = new WattDepotClient(HOST_URI);
  /** Indicates whether an error is encountered when processing a command. */
  protected boolean wattDepotExceptionThrown = false;
  /** Oahu power grid. */
  private static final String SIM_OAHU_GRID = "SIM_OAHU_GRID";
  /** Indicates whether the list of timestamps does not contain twenty-four timestamps. */
  private boolean isTimestampsListNotFilled = false;
  /** Indicates whether the list to store results is not empty. */
  private boolean isResultsListNotEmpty = false;
  /** Indicates whether no data was available. */
  private boolean noDataAvailable = false;
  /** Holds the sampling interval in minutes. */
  private int samplingInterval = 60;

  /**
   * Establishes connection to the WattDepot server.
   */
  public WattDepotCommand() {
    // Stop right away if server cannot be contacted.
    if (!CLIENT.isHealthy()) {
      wattDepotExceptionThrown = true;
    }
  }

  /**
   * Gets a list of data regarding carbon emission for one 24-hour period.
   * 
   * @param timestamps List of timestamps for one 24-hour period.
   * @param results List of data for one 24-hour period.
   */
  public void getCarbonContentData(List<XMLGregorianCalendar> timestamps, List<Double> results) {

    if (timestamps.size() != 24) {
      isTimestampsListNotFilled = true;
      return;
    }
    isTimestampsListNotFilled = false;

    if (!results.isEmpty()) {
      isResultsListNotEmpty = true;
      return;
    }
    isResultsListNotEmpty = false;

    double carbonGenerated, energyGenerated;

    try {
      for (XMLGregorianCalendar tstamp : timestamps) {
        carbonGenerated =
            CLIENT.getCarbonEmitted(SIM_OAHU_GRID, tstamp, Tstamp.incrementHours(tstamp, 1), 60);
        energyGenerated =
            CLIENT.getEnergyGenerated(SIM_OAHU_GRID, tstamp, Tstamp.incrementHours(tstamp, 1), 60);
        results.add(carbonGenerated / energyGenerated * 1000000.0);
      } // end for
    } // end try
    catch (WattDepotClientException e) {
      wattDepotExceptionThrown = true;
    }
  }

  /**
   * Gets data regarding carbon emission at the specified timestamp.
   * 
   * @param timestamp Timestamp.
   * @return Data for the Oahu power grid at the specified timestamp.
   */
  public double getCarbonContentData(XMLGregorianCalendar timestamp) {

    XMLGregorianCalendar nextTimestamp = Tstamp.incrementHours(timestamp, 1);

    double carbonGenerated, energyGenerated;

    try {
      carbonGenerated = CLIENT.getCarbonEmitted(SIM_OAHU_GRID, timestamp, nextTimestamp, 60);
      energyGenerated = CLIENT.getEnergyGenerated(SIM_OAHU_GRID, timestamp, nextTimestamp, 60);
      noDataAvailable = false;
      return (carbonGenerated / energyGenerated * 1000000.0);
    }
    catch (WattDepotClientException e) {
      wattDepotExceptionThrown = true;
      noDataAvailable = true;
      return Double.NaN;
    }
  }

  /**
   * Returns true if an error was encountered when connecting to WattDepot server or processing
   * command, false otherwise.
   * 
   * @return True if an error was encountered when connecting to WattDepot server or processing
   * command, false otherwise.
   */
  public boolean isWattDepotExceptionThrown() {
    return wattDepotExceptionThrown;
  }

  /**
   * Returns true if list of timetamps does not contain twenty-four timestamps, false otherwise.
   * 
   * @return True if list of timetamps does not contain twenty-four timestamps, false otherwise.
   */
  public boolean isTimestampsListNotFilled() {
    return isTimestampsListNotFilled;
  }

  /**
   * Returns true if list to store results is not empty, false otherwise.
   * 
   * @return True if list to store results is not empty, false otherwise.
   */
  public boolean isResultsListNotEmpty() {
    return isResultsListNotEmpty;
  }

  /**
   * Creates a list of power data for the given power source from startTime to endTime. The data
   * points are generated at a sampling interval of minutes.
   * 
   * @param startTime Start time inputed by user.
   * @param endTime End time inputed by user.
   * @param source Power source from which to get data.
   * @param powerType Type of power, carbon or energy.
   * @param granularityChoice Type of granularity, hour, day or week.
   * @return List of power data.
   */
  public List<Double> getChartData(XMLGregorianCalendar startTime, XMLGregorianCalendar endTime,
      String source, String powerType, String granularityChoice) {

    List<Double> dataList = new ArrayList<Double>();
    XMLGregorianCalendar tempStart = startTime;
    XMLGregorianCalendar tempEnd = Tstamp.incrementDays(endTime, 1);

    if (granularityChoice.equalsIgnoreCase("hour")) {
      samplingInterval = 60;
    }
    else if (granularityChoice.equalsIgnoreCase("day")) {
      samplingInterval = 60 * 24;
    }
    else if (granularityChoice.equalsIgnoreCase("week")) {
      samplingInterval = 60 * 24 * 7;
    }
    double data = 0.0;

    try {
      while (Tstamp.greaterThan(tempEnd, tempStart)) {
        if (powerType.equalsIgnoreCase("energy")) {
          data =
              CLIENT.getEnergyGenerated(source, tempStart, Tstamp.incrementHours(tempStart,
                  samplingInterval / 60), samplingInterval);
          // Convert to MW for energy.
          data /= 1000000.0;
        } // end if
        else if (powerType.equalsIgnoreCase("carbon")) {
          data =
              CLIENT.getCarbonEmitted(source, tempStart, Tstamp.incrementHours(tempStart,
                  samplingInterval / 60), samplingInterval);
          // Convert to KW because for now, the data for carbon is very low.
          data /= 1000.0;
        } // end else if
        else {
          wattDepotExceptionThrown = true;
          break;
        } // end else

        // Convert to GW for energy or MW for carbon.
        boolean granularityDay = granularityChoice.equalsIgnoreCase("day");
        if (granularityDay || granularityChoice.equalsIgnoreCase("week")) {
          data /= 1000.0;
        }

        dataList.add(data);
        tempStart = Tstamp.incrementMinutes(tempStart, samplingInterval);
      } // end while
    } // end try
    catch (WattDepotClientException e) {
      wattDepotExceptionThrown = true;
    }
    return dataList;
  }

  /**
   * Returns a list of all non-virtual power sources that are subsources of a given virtual power
   * source.
   * 
   * @param source A virtual power source.
   * @param subsources An empty list that will eventually contain non-virtual power sources.
   * @return A list of non-virtual power sources.
   */
  public List<String> getSubsources(Source source, List<String> subsources) {
    for (String subsource : source.getSubSources().getHref()) {
      subsource = subsource.substring(subsource.indexOf("SIM"));

      try {
        if (CLIENT.getSource(subsource).isVirtual()) {
          getSubsources(CLIENT.getSource(subsource), subsources);
        } // end if
        else {
          subsources.add(subsource);
        } // end else
      } // end try
      catch (WattDepotClientException wdce) {
        wattDepotExceptionThrown = true;
        return subsources;
      } // end catch
    } // end for
    return subsources;
  }

  /**
   * Returns a list of all power sources that are subsources of a given virtual power source. Only
   * returns power sources that are one level below given virtual power source.
   * 
   * @param source Virtual power source.
   * @return A list of all power sources that are subsources of given power source.
   */
  public List<String> getSubSources(Source source) {
    List<String> subsource = new ArrayList<String>();
    if (source.isSetSubSources()) {
      for (String href : source.getSubSources().getHref()) {
        subsource.add(href.substring(href.indexOf("SIM")));
      }
    }
    return subsource;
  }

  /**
   * Returns true if no data was available, false otherwise.
   * 
   * @return True if no data was available, false otherwise.
   */
  public boolean isNoDataAvailable() {
    return noDataAvailable;
  }

  /**
   * Returns the URI of the WattDepot server.
   * 
   * @return URI of the WattDepot server.
   */
  public String getHostUri() {
    return HOST_URI;
  }

  /**
   * Returns a WattDepotClient object.
   * 
   * @return WattDepotClient object.
   */
  public WattDepotClient getWattDepotClient() {
    return CLIENT;
  }

  /**
   * Returns a Source object given the name of a power source.
   * 
   * @param source Name of a power source.
   * @return A Source object representing the power source.
   */
  public Source getSource(String source) {
    Source temp = null;
    try {
      temp = CLIENT.getSource(source);
    }
    catch (WattDepotClientException wdce) {
      wdce.printStackTrace();
    }
    return temp;
  }

  /**
   * Lists information about a public power source that is available on this server.
   * 
   * @param sourceName Name of a public power source.
   * @return Information about a public power source.
   */
  public final String getSourceSummary(String sourceName) {

    Source source = null;
    try {
      source = CLIENT.getSource(sourceName);
    }
    catch (WattDepotClientException wdce) {
      wattDepotExceptionThrown = true;
      return "ERROR_1";
    }

    String format = "  %1$-20s %2$-100s" + "\n";
    StringBuffer buffer = new StringBuffer(2500);

    if (source.isSetSubSources()) {
      String subsources = "";
      for (String href : source.getSubSources().getHref()) {
        buffer.append(href.substring(href.indexOf("SIM")));
        buffer.append(", ");
      } // end for
      subsources = buffer.toString().substring(0, buffer.toString().length() - 2);
      buffer.setLength(0);
      buffer.append(String.format(format, "SubSources: ", subsources));
    } // end if
    else {
      buffer.append(String.format(format, "SubSources: ", "[ None ]"));
    }

    buffer.append(String.format(format, "Description: ", source.getDescription()));
    buffer.append(String.format(format, "Owner: ", source.getOwner()));
    buffer.append(String.format(format, "Location: ", source.getLocation()));
    buffer.append(String.format(format, "Coordinates: ", source.getCoordinates()));

    StringBuffer tempBuffer = new StringBuffer(255);
    String properties = "";
    if (source.isSetProperties()) {
      for (Property property : source.getProperties().getProperty()) {
        tempBuffer.append('(');
        tempBuffer.append(property.getKey());
        tempBuffer.append(" : ");
        tempBuffer.append(property.getValue());
        tempBuffer.append("), ");
      } // end for
      properties = tempBuffer.toString().substring(0, tempBuffer.toString().length() - 2);
    } // end if
    else {
      properties = "None";
    }
    buffer.append(String.format(format, "Properties: ", properties));

    SourceSummary sourceSummary = null;
    try {
      sourceSummary = CLIENT.getSourceSummary(sourceName);
    }
    catch (WattDepotClientException wdce) {
      wattDepotExceptionThrown = true;
      return "ERROR_2";
    }

    buffer.append(String.format(format, "Earliest data: ", sourceSummary.getFirstSensorData()
        .toString()));
    buffer.append(String.format(format, "Latest data: ", sourceSummary.getLastSensorData()
        .toString()));
    buffer
        .append(String.format(format, "Total data points: ", sourceSummary.getTotalSensorDatas()));

    return buffer.toString();
  }

}