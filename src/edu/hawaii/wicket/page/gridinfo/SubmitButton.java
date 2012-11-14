package edu.hawaii.wicket.page.gridinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.wattdepot.resource.source.jaxb.Source;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.wattdepot.WattDepotCommand;
import edu.hawaii.wicket.Session;

/**
 * Constructs a button used to query the WattDepot server for data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class SubmitButton extends IndicatingAjaxButton {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to get the start date from the user. */
  private final DateTextField startDay;
  /** Used to get the end date from the user. */
  private final DateTextField endDay;
  /** Used to get the power source from the user. */
  private DropDownChoice<String> sourceChoice;
  /** Used to get the power type from the user. */
  private DropDownChoice<String> powerChoice;
  /** Used to get the the granularity Hour / Day or Week. */
  private DropDownChoice<String> granularityChoice;
  /** Displays information about the Oahu power grid. */
  private Image chartImage;
  /** Used to display an error message. */
  private Label status;
  /** Used to hold checkbox value. */
  private CheckBox subSource;
  /** Used to format date correctly. */
  private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
  /** A user's session. */
  private Session session;
  /** Used to indicate if an error was encountered. */
  private Boolean isError = false;

  /**
   * Constructs a button used to query the WattDepot server for data.
   * 
   * @param id Name of the button.
   * @param error Used to display error messages.
   * @param startDay Textbox in which user enters a start date.
   * @param endDay Textbox in which user enters an end date.
   * @param source Drop down list in which user selects a power source.
   * @param powerChoice Drop down list in which user selects carbon or energy.
   * @param granularityChoice Type of granularity, hour, day or week.
   * @param chartImage Image of power chart.
   * @param subSource Subsource of a power source.
   * @param session This user's session.
   */
  public SubmitButton(String id, Label error, DateTextField startDay, DateTextField endDay,
      DropDownChoice<String> source, DropDownChoice<String> powerChoice,
      DropDownChoice<String> granularityChoice, Image chartImage, CheckBox subSource,
      Session session) {

    super(id);
    this.startDay = startDay;
    this.endDay = endDay;
    this.sourceChoice = source;
    this.powerChoice = powerChoice;
    this.granularityChoice = granularityChoice;
    this.status = error;
    this.subSource = subSource;
    this.chartImage = chartImage;
    this.session = session;
  }

  /**
   * Gets data inputed by the user on the form.
   * 
   * @param target Target to output the result.
   * @param form Form.
   */
  @Override
  protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

    List<String> subsources = new ArrayList<String>();
    // Holds list of power data.
    List<String> dataText = new ArrayList<String>();
    List<String> colorList = new ArrayList<String>();
    List<String> legendList = new ArrayList<String>();
    List<String> markerList = new ArrayList<String>();
    Chart powerChart = new Chart();

    String powerData = "";
    String color = "";
    String legend = "";
    String chartTitle = "";
    String labelX = "";
    String choice = "";
    String marker = "";

    status.setOutputMarkupId(true);
    chartImage.setOutputMarkupId(true);

    WattDepotCommand cli = new WattDepotCommand();
    if (cli.isWattDepotExceptionThrown()) {
      this.isError = true;
      status.setDefaultModelObject("Failed to connect to WattDepot server.");
    }
    session.setFromDay(DATE_FORMAT.format(this.startDay.getDefaultModelObject()));
    session.setToDay(DATE_FORMAT.format(this.endDay.getDefaultModelObject()));

    choice = session.getListGranularity().get(Integer.parseInt(granularityChoice.getValue()));
    session.setInterval(choice);

    XMLGregorianCalendar fromDay = null;
    XMLGregorianCalendar toDay = null;
    try {
      fromDay = Tstamp.makeTimestamp(session.getStartDay());
      toDay = Tstamp.makeTimestamp(session.getEndDay());

      // Adjust the fromDay for the day/week period.
      if (Tstamp.equal(fromDay, toDay)) {
        if (choice.equalsIgnoreCase("week")) {
          fromDay = Tstamp.incrementDays(fromDay, ((-1) * 13));

        }
        else if (choice.equalsIgnoreCase("day")) {
          fromDay = Tstamp.incrementDays(fromDay, ((-1) * session.getInterval()));
        }
      }
      else if (Tstamp.daysBetween(fromDay, toDay) < 7) {
        if (choice.equalsIgnoreCase("week")) {
          fromDay =
              Tstamp.incrementDays(fromDay, (-1) * (13 - (Tstamp.daysBetween(fromDay, toDay) % 7)));

        }

      }
      else {
        if (choice.equalsIgnoreCase("week") && ((Tstamp.daysBetween(fromDay, toDay) % 7) >= 0)) {
          fromDay =
              Tstamp.incrementDays(fromDay, (-1) * (6 - (Tstamp.daysBetween(fromDay, toDay) % 7)));
        }
      }
      session.setFromDay(fromDay.toString());
    }
    catch (Exception e) {
      this.isError = true;
      status.setDefaultModelObject("ERROR: Invalid date.");
    }

    if (Tstamp.greaterThan(fromDay, toDay)) {
      this.isError = true;
      status.setDefaultModelObject("ERROR: Invalid period.");
    }

    if (!this.isError) {
      // Sets power source and power type for each user's session.
      session.setPowerSource(session.getListSources()
          .get(Integer.parseInt(sourceChoice.getValue())));
      session.setPowerType(session.getListType().get(Integer.parseInt(powerChoice.getValue())));
      session.setUnit(choice, session.getPowerType());

      if (subSource.getDefaultModelObjectAsString().equalsIgnoreCase("true")) {
        Source source = cli.getSource(session.getPowerSource());
        subsources = cli.getSubSources(source);
      }

      // Adds parent source to the list.
      subsources.add(session.getPowerSource());

      Iterator<String> itr = subsources.iterator();
      Integer index = 0;

      // Gets data for subsource.
      while (itr.hasNext()) {
        String temp = itr.next().toString();
        legendList.add(temp);

        dataText.add(powerChart.getEncodedDataList(cli.getChartData(fromDay, toDay, temp, session
            .getPowerType(), choice)));

        colorList.add(session.getSourceColor().get(index));
        markerList.add("s," + session.getSourceColor().get(index) + "," + index + ",-1,5.0|");
        index++;
      }
      // Cleans up string for chart.
      legend = legendList.toString();
      legend = legend.replace("[", "");
      legend = legend.replace("]", "");
      legend = legend.replace(", ", "|");

      color = colorList.toString();
      color = color.replace("[", "");
      color = color.replace("]", "");
      color = color.replace(", ", ",");

      powerData = dataText.toString();
      powerData = powerData.replace("[", "");
      powerData = powerData.replace("]", "");
      powerData = powerData.replace(", ", ",");

      marker = markerList.toString();
      marker = marker.substring(0, marker.length() - 2);
      marker = marker.replace("[", "");
      marker = marker.replace("]", "");
      marker = marker.replace("|, ", "|");

      // status.setDefaultModelObject(marker);
      chartTitle =
          powerChart.getChartTitle(session.getPowerType(), session.getUnit(), fromDay, toDay);
      labelX = powerChart.getDayLabel(fromDay, toDay, choice);
      session.setChartUri(powerChart.getChartUri(chartTitle, labelX, powerData, color, legend,
          marker));

      // Add chart image.
      // status.setDefaultModelObject("Done");

      chartImage.add(new AttributeModifier("src", true, new Model<String>(session.getChartUri())));

    }

    // Attach components to the form.
    add(status);
    add(chartImage);

    target.addComponent(status);
    target.addComponent(chartImage);
  }

}