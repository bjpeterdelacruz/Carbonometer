package edu.hawaii.wicket.page.stoplight;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTimeUtils;
import org.wattdepot.util.tstamp.Tstamp;
import edu.hawaii.wicket.page.BasePage;
import edu.hawaii.wattdepot.WattDepotCommand;

/**
 * Creates a page that contains a stoplight to indicate current carbon emission level.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class StoplightPage extends BasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Carbon emission data for the island of Oahu right now. */
  // TODO: Support application-level caching for the carbon emission data.
  private double carbonEmission = Double.MAX_VALUE;
  /** Formats the current date and time to HST. */
  private final DateFormat DATE_FORMAT =
      DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
  /** Formats the current date and time to Tstamp format. */
  private final DateFormat TSTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
  /** Formats the current date and time to 12-hour format. */
  private final DateFormat TWELVE_HOUR_FORMAT = new SimpleDateFormat("h:mm:ss a z", Locale.US);
  /** Formats the carbon emission data to two decimal places. */
  private final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("0.00");
  /** Used to display error messages. */
  private final Label ERROR = new Label("error", "");
  /** Used to display carbon emission data. */
  private static final ImageSource STOPLIGHT =
      new ImageSource("stoplight", new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/stoplight.png"));
  /** SRC attribute in IMG tag. */
  private static final String SRC_ATTRIBUTE = "src";
  /** Turn on or off debugging. */
  private static boolean debugMode = false;
  /** Indicates whether an error was encountered. */
  private static boolean errorEncountered = false;
  /** Used to test if timestamp was created successfully. */
  private static boolean debugTimestamp = false;
  /** Used to tell the user that no data is available. */
  private static boolean noDataAvailable = false;
  /** Informs the user that an error was encountered. */
  private static final String ERROR_MESSAGE = "[See error message below stoplight]";
  /** Informs the user that carbon emission levels are low. */
  private static final String GREEN_LIGHT_MESSAGE =
      "Right now, not that many people on Oahu are using appliances that leave carbon footprints,"
          + " so feel free to use your clothes washer, dryer, or any other carbon-emitting"
          + " machine, but please try not to use them too much or for too long.";
  /** Informs the user that carbon emission levels are neither high nor low. */
  private static final String YELLOW_LIGHT_MESSAGE =
      "Right now, a lot of people are using appliances that leave carbon footprints. It is"
          + " suggested that you may want to use those that generate very little to no carbon, or"
          + " wait until the stoplight turns green again before using your clothes washer, dryer,"
          + " or any other carbon-emitting machine.";
  /** Informs the user that carbon emission levels are high. */
  private static final String RED_LIGHT_MESSAGE =
      "Right now, the carbon emission level is too high, which means that too much carbon is being"
          + " produced as a result of too many appliances being used. Unless it is an emergency,"
          + " it is highly suggested that you should wait until the stoplight turns yellow or"
          + " green again before using your clothes washer, dryer, or any other carbon-emitting"
          + " machine.";
  /** Random number generator for generating random messages for the user. */
  private static final Random generator = new Random();
  /** Information about driving a car. */
  private static final String DRIVING_A_CAR_MESSAGE =
      "Your car is releasing 14,330 to 33,069 lbs of carbon dioxide every year with the average"
          + " run/drive of 12,000 miles per year, depending on the make and year of make of your"
          + " car.";
  /** Information about human activities. */
  private static final String HUMAN_ACTIVITIES_MESSAGE =
      "Carbon dioxide is emitted naturally through the carbon cycle and through human activities"
          + " like the burning of fossil fuels. Since the Industrial Revolution in the 1700’s,"
          + " human activities have increased CO2 concentrations in the atmosphere. In 2005,"
          + " global atmospheric concentrations of CO2 were 35% higher than they were before"
          + " the Industrial Revolution.";
  /** Information about appliances on standby mode. */
  private static final String ON_STANDY_MESSAGE =
      "One way to help reduce carbon emissions is to not leave appliances on standby. Instead,"
          + " use the \"on/off\" function on the machine itself. A TV set that's switched on for 3"
          + " hours a day and in standby mode during the remaining 21 hours uses about 40% of its"
          + " energy in standby mode.";
  /** Information about home energy audits. */
  private static final String ENERGY_STAR_MESSAGE =
      "Get a home energy audit. Many utility companies offer free home energy audits to find"
          + " where your home is poorly insulated or energy inefficient. You can save up to 30%"
          + " off your energy bill and 1,000 pounds of carbon dioxide a year. Energy Star can"
          + " help you find an energy specialist.";
  /** Used to display different message every ten seconds. */
  private int previousNumber = 0;

  /**
   * Creates a page that contains a stoplight to indicate current carbon emission level.
   */
  public StoplightPage() {

    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("HST"));

    Date today = null;
    if (debugMode) {
      // Use date and time set in the test cases.
      DateTimeUtils.setCurrentMillisFixed((new TestStoplightPage()).getCurrentTime().getTime());
      today = (new TestStoplightPage()).getCurrentTime();
    }
    else {
      // Use current date and time.
      today = new Date();
    }

    // Add current date and time to page.
    final Label date = new Label("date", DATE_FORMAT.format(today));
    date.setOutputMarkupId(true);
    add(date);

    // Add countdown timer to page.
    final Label countdown = new Label("countdown", "");
    countdown.setOutputMarkupId(true);
    add(countdown);

    // Add time when carbon emission level will be checked again to page.
    // final Label nextTime = new Label("nexttime", "");
    // nextTime.setOutputMarkupId(true);
    // add(nextTime);

    add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Updates clock on web page. */
      @Override
      protected void onTimer(AjaxRequestTarget target) {
        Calendar counter = Calendar.getInstance();
        date.setDefaultModelObject(DATE_FORMAT.format(counter.getTime()));

        // int hourToSec = 60 * 60;
        // int minToSec = counter.get(Calendar.MINUTE) * 60 + counter.get(Calendar.SECOND);
        // countdown.setDefaultModelObject(hourToSec - minToSec);
        int minLeft = 60 - counter.get(Calendar.SECOND);
        countdown.setDefaultModelObject(minLeft + " sec");

        // counter.add(Calendar.HOUR, 0);
        // counter.add(Calendar.MINUTE, 1);
        // counter.set(Calendar.SECOND, 0);
        // Date nextMinute = counter.getTime();
        // nextTime.setDefaultModelObject(TWELVE_HOUR_FORMAT.format(nextMinute));

        target.addComponent(countdown);
        target.addComponent(date);
        // target.addComponent(nextTime);
      }
    });

    if (!debugMode) {
      // Use current date and time to get carbon emission data.
      Calendar now = Calendar.getInstance();
      // now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      today = now.getTime();
    }

    // Get carbon emission data for the hour. Then check again in a minute.
    getCarbonContentData(today);

    // Add time when carbon emission level was last checked to page.
    String information = "";
    if (errorEncountered) {
      information = ERROR_MESSAGE;
    }
    else {
      information = TWELVE_HOUR_FORMAT.format(today);
    }
    final Label time = new Label("time", information);
    time.setOutputMarkupId(true);
    add(time);

    // Add carbon emission data to page.
    if (errorEncountered) {
      information = ERROR_MESSAGE;
    }
    else if (noDataAvailable) {
      information = "not available";
    }
    else {
      information = TWO_DECIMAL_PLACES.format(carbonEmission) + " lbs CO2 / MWh";
    }
    final Label data = new Label("data", information);
    data.setOutputMarkupId(true);
    add(data);

    // Add information regarding carbon emission level to page.
    final Label description = new Label("description", "");
    description.setOutputMarkupId(true);
    add(description);

    updateComponents(description);

    AttributeModifier imgAttribute =
        new AttributeModifier(SRC_ATTRIBUTE, true, new AbstractReadOnlyModel<String>() {
          /** Support serialization. */
          private static final long serialVersionUID = 1L;

          /**
           * Returns a URL to a picture of a green, yellow, or red light based on current carbon
           * emission level. If no data exists, returns a URL to a picture of no light.
           * 
           * @return A URL to a picture of a green, yellow, or red light, or no light.
           */
          @Override
          public final String getObject() {
            return updateComponents();
          }
        });

    // Add picture of stoplight to page.
    STOPLIGHT.setOutputMarkupId(true);
    add(STOPLIGHT.add(imgAttribute));

    add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Updates clock on web page. */
      @Override
      protected void onTimer(AjaxRequestTarget target) {
        Calendar currentTime = Calendar.getInstance();
        if (currentTime.get(Calendar.SECOND) != 0) {
          return;
        }

        getCarbonContentData(currentTime.getTime());

        if (errorEncountered) {
          time.setDefaultModelObject(ERROR_MESSAGE);
          data.setDefaultModelObject(ERROR_MESSAGE);
        }
        else if (noDataAvailable) {
          time.setDefaultModelObject(TWELVE_HOUR_FORMAT.format(currentTime.getTime()));
          data.setDefaultModelObject("not available");
        }
        else {
          time.setDefaultModelObject(TWELVE_HOUR_FORMAT.format(currentTime.getTime()));
          String information = TWO_DECIMAL_PLACES.format(carbonEmission) + " lbs CO2 / MWh";
          data.setDefaultModelObject(information);
        }

        updateComponents(description);

        target.addComponent(time);
        target.addComponent(data);
        target.addComponent(description);
        target.addComponent(STOPLIGHT);
        target.addComponent(ERROR);
      }
    });

    add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Displays facts regarding carbon emissions to the user. */
      @Override
      protected void onTimer(AjaxRequestTarget target) {
        Calendar currentTime = Calendar.getInstance();
        if (currentTime.get(Calendar.SECOND) != 10 && currentTime.get(Calendar.SECOND) != 20
            && currentTime.get(Calendar.SECOND) != 30 && currentTime.get(Calendar.SECOND) != 40
            && currentTime.get(Calendar.SECOND) != 50) {
          return;
        }

        displayMessage(description);

        target.addComponent(description);
      }
    });

    // Add any error messages to page.
    ERROR.setOutputMarkupId(true);
    add(ERROR);

  } // end ctor

  /**
   * Gets data from the WattDepot server regarding carbon emission level on the island of Oahu.
   * 
   * @param date Timestamp.
   */
  private void getCarbonContentData(Date date) {
    String timestamp = TSTAMP_FORMAT.format(date);
    timestamp = timestamp.replace(timestamp.charAt(10), 'T');
    if (debugTimestamp) {
      timestamp = timestamp.replace(timestamp.charAt(10), 'X');
    }
    XMLGregorianCalendar tstamp = null;
    try {
      tstamp = Tstamp.makeTimestamp(timestamp);
      ERROR.setDefaultModelObject("");
      errorEncountered = false;
    }
    catch (Exception e) {
      ERROR.setDefaultModelObject("Error: Cannot make timestamp.");
      errorEncountered = true;
      carbonEmission = Double.NaN;
      return;
    }
    WattDepotCommand wattDepot = new WattDepotCommand();
    carbonEmission = wattDepot.getCarbonContentData(tstamp);
    noDataAvailable = wattDepot.isNoDataAvailable();
  }

  /**
   * Returns a URL to a picture of a green, yellow, or red light based on current carbon emission
   * level. If no data exists, returns a URL to a picture of no light.
   * 
   * @return A URL to a picture of a green, yellow, or red light, or no light.
   */
  private String updateComponents() {
    if (carbonEmission < 1600.0) {
      return "http://www2.hawaii.edu/~bjpeter/Files/green.png";
    }
    else if (carbonEmission >= 1600.0 && carbonEmission < 2000.0) {
      return "http://www2.hawaii.edu/~bjpeter/Files/yellow.png";
    }
    else if (carbonEmission >= 2000.0) {
      return "http://www2.hawaii.edu/~bjpeter/Files/red.png";
    }
    else {
      return "http://www2.hawaii.edu/~bjpeter/Files/stoplight.png";
    }
  }

  /**
   * Updates the stoplight and the message that is displayed to the user based on the color of the
   * stoplight.
   * 
   * @param description Label that contains message for the user.
   */
  private void updateComponents(Label description) {
    if (carbonEmission < 1600.0) {
      description.setDefaultModelObject(GREEN_LIGHT_MESSAGE);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/green.png")));
    }
    else if (carbonEmission >= 1600.0 && carbonEmission < 2000.0) {
      description.setDefaultModelObject(YELLOW_LIGHT_MESSAGE);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/yellow.png")));
    }
    else if (carbonEmission >= 2000.0) {
      description.setDefaultModelObject(RED_LIGHT_MESSAGE);
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/red.png")));
    }
    else {
      STOPLIGHT.add(new AttributeModifier(SRC_ATTRIBUTE, true, new Model<String>(
          "http://www2.hawaii.edu/~bjpeter/Files/stoplight.png")));
    }
  }

  /**
   * Displays messages to the user.
   * 
   * @param description Label that contains message for the user.
   */
  private void displayMessage(Label description) {
    int number;
    while (previousNumber == (number = generator.nextInt(4) + 1)) {
      ;
    }

    switch (number) {
    case 1:
      description.setDefaultModelObject(DRIVING_A_CAR_MESSAGE);
      break;
    case 2:
      description.setDefaultModelObject(HUMAN_ACTIVITIES_MESSAGE);
      break;
    case 3:
      description.setDefaultModelObject(ON_STANDY_MESSAGE);
      break;
    case 4:
      description.setDefaultModelObject(ENERGY_STAR_MESSAGE);
      break;
    default:
      updateComponents(description);
      break;
    }

    previousNumber = number;
  }

  /**
   * Turns on or off debugging.
   * 
   * @param value True or false.
   */
  public static void setDebugMode(boolean value) {
    debugMode = value;
  }

  /**
   * Turns on or off timestamp debugging.
   * 
   * @param value True or false.
   */
  public static void setDebugTimestamp(boolean value) {
    debugTimestamp = value;
  }

}