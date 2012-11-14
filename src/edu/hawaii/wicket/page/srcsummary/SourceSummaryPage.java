package edu.hawaii.wicket.page.srcsummary;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import edu.hawaii.wicket.Session;
import edu.hawaii.wicket.page.BasePage;

/**
 * Displays information about a power source.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class SourceSummaryPage extends BasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to display an error message. */
  private final Label ERROR = new Label("error", new Model<String>(""));
  /** Used to display information about a power source. */
  private static final String DEFAULT_STRING = "---";
  /** Used to display subsources of a power source. */
  private final Label SUBSOURCES = new Label("subsources", new Model<String>(DEFAULT_STRING));
  /** Used to display description of a power source. */
  private final Label DESCRIPTION = new Label("description", new Model<String>(DEFAULT_STRING));
  /** Used to display owner's information. */
  private final Label OWNER = new Label("owner", new Model<String>(DEFAULT_STRING));
  /** Used to display location of a power source. */
  private final Label LOCATION = new Label("location", new Model<String>(DEFAULT_STRING));
  /** Used to display coordinates of a power source. */
  private final Label COORDINATES = new Label("coordinates", new Model<String>(DEFAULT_STRING));
  /** Used to display properties of a power source. */
  private final Label PROPERTIES = new Label("properties", new Model<String>(DEFAULT_STRING));
  /** Used to display earliest data about a power source. */
  private final Label EARLIEST_DATA = new Label("earliestData", new Model<String>(DEFAULT_STRING));
  /** Used to display latest data about a power source. */
  private final Label LATEST_DATA = new Label("latestData", new Model<String>(DEFAULT_STRING));
  /** Used to display total number of data points. */
  private final Label TOTAL_DATA_POINTS =
      new Label("totalDataPoints", new Model<String>(DEFAULT_STRING));
  /** A user's session. */
  private Session session = (Session) this.getSession();

  /**
   * Displays information about a power source.
   */
  public SourceSummaryPage() {

    Form<Object> form = new Form<Object>("sourceList") {

      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /**
       * Updates the page and displays information about a power source.
       */
      public void onSubmit() {
        SUBSOURCES.modelChanged();
        DESCRIPTION.modelChanged();
        OWNER.modelChanged();
        LOCATION.modelChanged();
        COORDINATES.modelChanged();
        PROPERTIES.modelChanged();
        EARLIEST_DATA.modelChanged();
        LATEST_DATA.modelChanged();
        TOTAL_DATA_POINTS.modelChanged();
      }
    };

    // Add power source drop down list.
    Model<String> day = new Model<String>("SIM_OAHU_GRID");
    DropDownChoice<String> sourceChoice =
        new DropDownChoice<String>("source", day, session.getListSources());
    sourceChoice.setRequired(true);
    form.add(sourceChoice);

    // Add a submit button.
    form.add(new SubmitButton("generate", sourceChoice, ERROR, SUBSOURCES, DESCRIPTION, OWNER,
        LOCATION, COORDINATES, PROPERTIES, EARLIEST_DATA, LATEST_DATA, TOTAL_DATA_POINTS));

    add(form);
    add(ERROR);
    add(SUBSOURCES);
    add(DESCRIPTION);
    add(OWNER);
    add(LOCATION);
    add(COORDINATES);
    add(PROPERTIES);
    add(EARLIEST_DATA);
    add(LATEST_DATA);
    add(TOTAL_DATA_POINTS);
  }

}