package edu.hawaii.wicket.page.thresholds;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.wattdepot.WattDepotCommand;
import edu.hawaii.wicket.Session;
import edu.hawaii.wicket.page.stoplight.ImageSource;

/**
 * Constructs a button used to query the WattDepot server for data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class SubmitButton extends Button {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to display an error message. */
  private final Label ERROR;
  /** Used to display maximum threshold value. */
  private final Label MAX;
  /** Used to display minimum threshold value. */
  private final Label MIN;
  /** Used to display general information about the data. */
  private final Label INFO;
  /** Used to display interpretation of the data. */
  private final Label DESC;
  /** Used to get the date from the user. */
  private final DateTextField DATE;
  /** Used to format date correctly. */
  private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
  /** Used to format data to two decimal places. */
  private static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("0.00");
  /** A user's session. */
  private final Session SESSION;
  /** Units for amount of carbon emitted. */
  private static final String UNITS = " lbs CO2 / MWh";
  /** Used to display carbon emission data. */
  private final ImageSource STOPLIGHT;
  /** SRC attribute in IMG tag. */
  private static final String SRC_ATTRIBUTE = "src";
  /** STYLE attribute in IMG tag. */
  private static final String STYLE_ATTRIBUTE = "STYLE";

  /**
   * Constructs a button used to query the WattDepot server for data.
   * 
   * @param id Name of the button.
   * @param error Used to display error messages.
   * @param max Used to display maximum threshold value.
   * @param min Used to display minimum threshold value.
   * @param info Used to display general information about the data.
   * @param desc Used to display a message about the data.
   * @param date Textbox in which user enters a date.
   * @param stoplight Picture of a stoplight.
   * @param session This user's session.
   */
  public SubmitButton(String id, Label error, Label max, Label min, Label info, Label desc,
      DateTextField date, ImageSource stoplight, Session session) {
    super(id);
    this.ERROR = error;
    this.MAX = max;
    this.MIN = min;
    this.INFO = info;
    this.DESC = desc;
    this.DATE = date;
    this.STOPLIGHT = stoplight;
    this.SESSION = session;
  }

  /**
   * Gets data for one 24-hour period.
   */
  public void onSubmit() {
    resetLabels();
    SESSION.setDate(DATE_FORMAT.format(DATE.getDefaultModelObject()));
    SESSION.setMaxThreshold(2000.0);
    SESSION.setMinThreshold(1500.0);

    WattDepotCommand cli = new WattDepotCommand();
    if (cli.isWattDepotExceptionThrown()) {
      resetLabels();
      ERROR.setDefaultModelObject("Failed to connect to WattDepot server.");
      return;
    }

    XMLGregorianCalendar tempTimestamp = null;
    try {
      tempTimestamp = Tstamp.makeTimestamp(SESSION.getDate());
    }
    catch (Exception e) {
      ERROR.setDefaultModelObject("ERROR: Timestamp cannot be created.");
      return;
    }

    SESSION.setTimestamps(Timestamps.createTimestamps(tempTimestamp));

    cli.getCarbonContentData(SESSION.getTimestamps(), SESSION.getResults());

    if (cli.isTimestampsListNotFilled()) {
      resetLabels();
      ERROR.setDefaultModelObject("ERROR: List of timestamps does not contain twenty-four"
          + " timestamps.");
    }
    else if (cli.isResultsListNotEmpty()) {
      resetLabels();
      ERROR.setDefaultModelObject("ERROR: List to store results is not empty before method call.");
    }
    else if (cli.isWattDepotExceptionThrown()) {
      resetLabels();
      ERROR.setDefaultModelObject("No data available for some or all hours of "
          + (new SimpleDateFormat("MM/dd/yy", Locale.US)).format(DATE.getDefaultModelObject())
          + " because either the connection to the WattDepot server timed out or there is no"
          + " data for that day.");
    }
    else {
      Thresholds thresholds = new Thresholds();

      Double maxValue = thresholds.getMaxThreshold(SESSION.getResults());

      Double minValue = thresholds.getMinThreshold(SESSION.getResults());

      SESSION.setMinThreshold(minValue);

      SESSION.setMaxThreshold(maxValue);
      /*
       * Double maxThreshold = 0.0; Double minThreshold = Double.MAX_VALUE; for (Double value :
       * SESSION.getResults()) { if (value > SESSION.getMaxThreshold()) { if (value > maxThreshold)
       * { maxThreshold = value; } // end if if (value < minThreshold) { minThreshold = value; } //
       * end if SESSION.incrementMaxCounter(); } // end if if (SESSION.getMaxCounter() == 6) {
       * SESSION.setMaxThreshold(maxThreshold); SESSION.setMinThreshold(SESSION.getMinThreshold() +
       * (maxThreshold - minThreshold)); SESSION.resetMaxCounter(); } // end if } // end for
       */
      int numGreen = 0;
      int numYellow = 0;
      int numRed = 0;
      for (Double value : SESSION.getResults()) {
        if (value > SESSION.getMaxThreshold()/* && value > SESSION.getMinThreshold() */) {
          numRed++;
        }
        else if (value < SESSION.getMinThreshold()) {
          numGreen++;
        }
        else {
          numYellow++;
        }
      }

      displayMessage(numGreen, numYellow, numRed);

      String min =
          /* "\u2264 " + */"Minimum threshold value: "
              + TWO_DECIMAL_PLACES.format(SESSION.getMinThreshold()) + UNITS;
      MIN.setDefaultModelObject(min);

      String max =
          /* "\u2265 " + */"Maximum threshold value: "
              + TWO_DECIMAL_PLACES.format(SESSION.getMaxThreshold()) + UNITS;
      MAX.setDefaultModelObject(max);

      ERROR.setDefaultModelObject("");
    }
  }

  /**
   * Displays a message based on the number of data that fall between the minimum and maximum
   * threshold values.
   * 
   * @param numGreen Number of times data was below the minimum threshold value.
   * @param numYellow Number of times data was between the minimum and maximum threshold values.
   * @param numRed Number of times data was above the maximum threshold value.
   */
  private void displayMessage(int numGreen, int numYellow, int numRed) {
    String date = DATE.getDefaultModelObjectAsString();
    String message = "The data on the right are carbon intensity values for all hours on " + date;
    message += ". These values are colored green, black, or red if they are below the minimum";
    message += " threshold value, between the minimum and maximum threshold values, or above the";
    message += " maximum threshold value, respectively.";

    INFO.setDefaultModelObject(message);

    message = "For the most part, ";
    if (numRed >= numYellow && numRed >= numGreen) {
      message += "the carbon intensity level for the Oahu grid on " + date + " was too high. Here";
      message += " are some ways to help you greatly reduce the carbon footprint of your home or";
      message += " office:";
      DESC.setDefaultModelObject(message);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/red.png")));
    }
    else if (numYellow > numRed && numYellow >= numGreen) {
      message += "the carbon intensity level for the Oahu grid on " + date + " was reasonable.";
      message += " Here are some ways to help you reduce the carbon footprint of your home or";
      message += " office:";
      DESC.setDefaultModelObject(message);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/yellow.png")));
    }
    else if (numGreen > numRed && numGreen > numYellow) {
      message += "the carbon intensity level for the Oahu grid on " + date + " was very low. Here";
      message += " are some ways to help you reduce the carbon footprint of your home or office";
      message += " even further:";
      DESC.setDefaultModelObject(message);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/green.png")));
    }

    STOPLIGHT.add(new AttributeModifier(STYLE_ATTRIBUTE, true, new Model<String>(
        "visibility: visible")));
  }

  /**
   * Hides the labels on this page if an error occurs or there is no data.
   */
  private void resetLabels() {
    MIN.setDefaultModelObject("");
    MAX.setDefaultModelObject("");
    INFO.setDefaultModelObject("");
    DESC.setDefaultModelObject("");
    SESSION.getTimestamps().clear();
    SESSION.getResults().clear();
    STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>("")));
    STOPLIGHT.add(new AttributeModifier(STYLE_ATTRIBUTE, true, new Model<String>(
        "visibility: hidden")));
  }

}