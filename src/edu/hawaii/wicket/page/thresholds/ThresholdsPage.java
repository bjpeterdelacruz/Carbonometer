package edu.hawaii.wicket.page.thresholds;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import edu.hawaii.wicket.page.BasePage;
import edu.hawaii.wicket.page.stoplight.ImageSource;
import edu.hawaii.wicket.Session;

/**
 * Displays list of carbon emission data for a given day.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class ThresholdsPage extends BasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to display an error message. */
  private final Label ERROR = new Label("error", new Model<String>(""));
  /** Used to display maximum threshold value. */
  private final Label MAX = new Label("max", new Model<String>(""));
  /** Used to display minimum threshold value. */
  private final Label MIN = new Label("min", new Model<String>(""));
  /** Used to display general information about the data. */
  private final Label INFO = new Label("info", new Model<String>(""));
  /** Used to display interpretation of the data. */
  private final Label DESC = new Label("desc", new Model<String>(""));
  /** Used to display tips on how to save energy to the user. */
  private final Label TIPS = new Label("tips", new Model<String>(""));
  /** Used to get the date from the user. */
  private final DateTextField DATE = new DateTextField("date");
  /**
   * Used to update page with list of timestamps for one 24-hour period, from 00:00:00 to 23:00:00.
   */
  private final ListView<XMLGregorianCalendar> TIMESTAMPS_VIEW;
  /** Used to update page with list of data for one 24-hour period. */
  private final ListView<Double> RESULTS_VIEW;
  /** A user's session. */
  private final Session SESSION = (Session) this.getSession();
  /** Used to display carbon emission data. */
  private static final ImageSource STOPLIGHT = new ImageSource("stoplight", new Model<String>(""));
  /** Random number generator for generating random messages for the user. */
  private static final Random generator = new Random();
  /** Information about using the shower. */
  private static final String SHOWER_MESSAGE =
      "Take a shower instead of a bath. A shower takes up to four times less energy than a bath."
          + " To maximize the energy saving, avoid power showers and use low-flow showerheads,"
          + " which are cheap and provide the same comfort.";
  /** Information about using hot water. */
  private static final String HOT_WATER_MESSAGE =
      "Use less hot water. It takes a lot of energy to heat water. You can use less hot water by"
          + " installing a low flow showerhead (350 pounds of carbon dioxide are saved per year)"
          + " and washing your clothes in cold or warm water (500 pounds are saved per year).";
  /** Information about recycling. */
  private static final String RECYCLING_MESSAGE =
      "Recycle at your home or office. You can save 2,400 pounds of carbon dioxide per year by"
          + " recycling half of the waste your home or office generates.";
  /** Information about green power. */
  private static final String GREEN_POWER_MESSAGE =
      "Switch to green power. In many areas, you can switch to energy generated by clean, renewable"
          + " sources, such as wind and solar. In some of these areas, you can even get refunds by"
          + " government if you choose to switch to a clean energy producer, and you can also earn"
          + " money by selling the energy you produce and don't use for yourself.";
  /** Used to display different message every ten seconds. */
  private int previousNumber = 1;
  /** Used to determine if Get Data button is clicked. */
  private boolean isButtonClicked = false;

  /**
   * Displays a textbox in which a user can enter a valid date and tables that contain hours for one
   * 24-hour period and data associated with each hour.
   */
  public ThresholdsPage() {

    SESSION.getTimestamps().clear();
    SESSION.getResults().clear();
    STOPLIGHT.add(new AttributeModifier("src", true, new Model<String>("")));
    STOPLIGHT.add(new AttributeModifier("STYLE", true, new Model<String>("visibility: hidden")));

    add(ERROR);
    add(MAX);
    add(MIN);

    Form<Object> form = new Form<Object>("form") {

      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /**
       * Updates the page with a list of timestamps and a list of data.
       */
      public void onSubmit() {
        TIMESTAMPS_VIEW.modelChanged();
        RESULTS_VIEW.modelChanged();
        TIPS.setDefaultModelObject(SHOWER_MESSAGE);
        previousNumber = 1;
        isButtonClicked = true;
      }

    };

    DATE.setDefaultModel(new Model<Date>());
    DATE.add(new DatePicker());
    DATE.setRequired(true);
    form.add(DATE);
    DATE.setDefaultModelObject(Calendar.getInstance().getTime());

    form.add(new SubmitButton("button", ERROR, MAX, MIN, INFO, DESC, DATE, STOPLIGHT, SESSION));

    add(form);

    add(TIMESTAMPS_VIEW = new TimestampsListView("hours00-11", SESSION.getTimestamps()));

    add(RESULTS_VIEW = new ResultsListView("results00-11", SESSION.getResults(), MAX, SESSION));

    add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /** Displays facts regarding carbon emissions to the user. */
      @Override
      protected void onTimer(AjaxRequestTarget target) {
        Calendar currentTime = Calendar.getInstance();
        if (!isButtonClicked) {
          return;
        }
        if (currentTime.get(Calendar.SECOND) != 10 && currentTime.get(Calendar.SECOND) != 20
            && currentTime.get(Calendar.SECOND) != 30 && currentTime.get(Calendar.SECOND) != 40
            && currentTime.get(Calendar.SECOND) != 50) {
          return;
        }

        displayTips();

        target.addComponent(TIPS);
      }
    });

    add(STOPLIGHT);
    add(INFO);
    add(DESC);
    TIPS.setOutputMarkupId(true);
    add(TIPS);

  }

  /**
   * Displays tips on how to save energy to the user.
   */
  private void displayTips() {
    int number;
    while (previousNumber == (number = generator.nextInt(4) + 1)) {
      ;
    }

    switch (number) {
    case 1:
      TIPS.setDefaultModelObject(SHOWER_MESSAGE);
      break;
    case 2:
      TIPS.setDefaultModelObject(HOT_WATER_MESSAGE);
      break;
    case 3:
      TIPS.setDefaultModelObject(RECYCLING_MESSAGE);
      break;
    case 4:
      TIPS.setDefaultModelObject(GREEN_POWER_MESSAGE);
      break;
    default:
      TIPS.setDefaultModelObject("");
      break;
    }

    previousNumber = number;
  }

}