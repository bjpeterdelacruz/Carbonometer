package edu.hawaii.wicket.page.srcsummary;

import java.text.DateFormat;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.joda.time.DateTime;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.wattdepot.WattDepotCommand;

/**
 * Constructs a button used to query the WattDepot server for data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class SubmitButton extends Button {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to get the power source from the user. */
  private DropDownChoice<String> sourceChoice;
  /** Used to display an error message. */
  private Label status;
  /** Used to display information about a power source. */
  private Label subsources;
  /** Used to display description. */
  private Label description;
  /** Used to display owner. */
  private Label owner;
  /** Used to display location. */
  private Label location;
  /** Used to display coordinates. */
  private Label coordinates;
  /** Used to display properties. */
  private Label properties;
  /** Used to display earliest data. */
  private Label earliestData;
  /** Used to display latest data. */
  private Label latestData;
  /** Used to display total data points. */
  private Label totalDataPoints;

  /** Formats the current date and time. */
  private final DateFormat DATE_FORMAT =
      DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);

  /** A user's session. */
  // private Session session;

  /**
   * Constructs a button used to query the WattDepot server for data.
   * 
   * @param id Name of the button.
   * @param source Drop down list in which user selects a power source.
   * @param status Used to display an error message.
   * @param subsources Used to display subsources.
   * @param description Used to display description.
   * @param owner Used to display owner.
   * @param location Used to display location.
   * @param coordinates Used to display coordinates.
   * @param properties Used to display properties.
   * @param earliestData Used to display earliest data.
   * @param latestData Used to display latest data.
   * @param totalDataPoints Used to display total data points.
   */
  public SubmitButton(String id, DropDownChoice<String> source, Label status, Label subsources,
      Label description, Label owner, Label location, Label coordinates, Label properties,
      Label earliestData, Label latestData, Label totalDataPoints) {

    super(id);
    this.sourceChoice = source;
    this.status = status;
    // this.session = session;
    this.subsources = subsources;
    this.description = description;
    this.owner = owner;
    this.location = location;
    this.coordinates = coordinates;
    this.properties = properties;
    this.earliestData = earliestData;
    this.latestData = latestData;
    this.totalDataPoints = totalDataPoints;
  }

  /**
   * Gets data inputed by the user on the form.
   */
  public void onSubmit() {
    resetLabels();
    WattDepotCommand command = new WattDepotCommand();
    String summary = command.getSourceSummary(sourceChoice.getDefaultModelObjectAsString());
    String sources = summary.substring(0, summary.indexOf("Description:"));
    sources = sources.substring(sources.indexOf(':') + 2);
    sources = sources.trim();
    subsources.setDefaultModelObject(sources);
    String desc = summary.substring(summary.indexOf("Description:"));
    desc = desc.substring(desc.indexOf(':') + 2, desc.indexOf("Owner:"));
    desc = desc.trim();
    description.setDefaultModelObject(desc);
    desc = summary.substring(summary.indexOf("Owner:"));
    desc = desc.substring(desc.indexOf(':') + 2, desc.indexOf("Location:"));
    desc = desc.trim();
    owner.setDefaultModelObject(desc);
    String locale = summary.substring(summary.indexOf("Location:"));
    locale = locale.substring(locale.indexOf(':') + 2, locale.indexOf("Coordinates:"));
    if (locale.contains("To be looked up later")) {
      locale = "Data not available at the moment.";
    }
    locale = locale.trim();
    location.setDefaultModelObject(locale);
    String coord = summary.substring(summary.indexOf("Coordinates:"));
    coord = coord.substring(coord.indexOf(':') + 2, coord.indexOf("Properties:"));
    coord = coord.trim();
    if (coord.contains("0,0,0")) {
      coord = "Data not available at the moment.";
    }
    else if (coord.contains(",")) {
      coord = coord.replaceAll(",", ", ");
    }
    coordinates.setDefaultModelObject(coord);
    String props = summary.substring(summary.indexOf("Properties:"));
    props = props.substring(props.indexOf(':') + 2, props.indexOf("Earliest"));
    props = props.trim();
    props = props.replaceAll("\\(", "");
    props = props.replaceAll("\\)", "");
    props = props.replaceAll(" :", ":");
    props = props.replaceAll("carbonIntensity", "Carbon Intensity");
    props = props.replaceAll("fuelType", "Fuel Type");
    if (!props.contains("None")) {
      String carbonIntensity =
          props.substring(props.indexOf("Carbon Intensity"), props.indexOf(',', props
              .indexOf("Carbon Intensity")));
      String carbonIntensityWithUnits = carbonIntensity + " lbs CO2 / MWh";
      props = props.replaceAll(carbonIntensity, carbonIntensityWithUnits);
    }
    properties.setDefaultModelObject(props);
    String data = summary.substring(summary.indexOf("Earliest"));
    data = data.substring(data.indexOf(':') + 2, data.indexOf("Latest"));
    data = data.substring(data.indexOf('2'), data.indexOf("-10:00"));
    XMLGregorianCalendar temp;
    DateTime dateTime;
    try {
      temp = Tstamp.makeTimestamp(data);
      dateTime =
          new DateTime(temp.getYear(), temp.getMonth(), temp.getDay(), temp.getHour(), temp
              .getMinute(), temp.getSecond(), temp.getMillisecond());
      earliestData.setDefaultModelObject(DATE_FORMAT.format(dateTime.toDate()));
    }
    catch (Exception e) {
      earliestData.setDefaultModelObject("Error: Timestamp cannot be made.");
      return;
    }
    data = summary.substring(summary.indexOf("Latest"));
    data = data.substring(data.indexOf(':') + 2, data.indexOf("Total"));
    data = data.substring(data.indexOf('2'), data.indexOf("-10:00"));
    try {
      temp = Tstamp.makeTimestamp(data);
      dateTime =
          new DateTime(temp.getYear(), temp.getMonth(), temp.getDay(), temp.getHour(), temp
              .getMinute(), temp.getSecond(), temp.getMillisecond());
      latestData.setDefaultModelObject(DATE_FORMAT.format(dateTime.toDate()));
    }
    catch (Exception e) {
      latestData.setDefaultModelObject("Error: Timestamp cannot be made.");
      return;
    }
    data = summary.substring(summary.indexOf("Total"));
    data = data.substring(data.indexOf(':') + 2);
    data = data.trim();
    totalDataPoints.setDefaultModelObject(data);
  }

  /**
   * Hides the labels on this page if an error occurs or there is no data.
   */
  private void resetLabels() {
    status.setDefaultModelObject("");
  }

}