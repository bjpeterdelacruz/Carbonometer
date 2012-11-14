package edu.hawaii.wicket.page.gridinfo;

import java.util.Date;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import edu.hawaii.wicket.Session;
import edu.hawaii.wicket.page.BasePage;

/**
 * Creates a page that displays information about the Oahu power grid.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class GridInfoPage extends BasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to get the date from the user for the start day. */
  private final DateTextField startDay = new DateTextField("start_day");
  /** Used to get the date from the user for the end day. */
  private final DateTextField endDay = new DateTextField("end_day");
  /** Displays the start day inputed by the user. */
  private Label status = new Label("status", new Model<String>(""));
  /** Displays information about the Oahu power grid. */
  private Image chart = new Image("chart");
  /** A user's session. */
  private Session session = (Session) this.getSession();

  /**
   * Creates a page that displays information about the Oahu power grid.
   */
  public GridInfoPage() {
    status.setOutputMarkupId(true);    
    add(status);
    
    chart.setOutputMarkupId(true);    
    add(chart.add(new AttributeModifier("src", true, 
        new Model<String>("http://www2.hawaii.edu/~peou/blank.png"))));    
    add(chart);
    
    Form<Object> form = new Form<Object>("gridForm") {

      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /**
       * Updates the page and displays a chart.
       */
      public void onSubmit() {
        chart.modelChanged();
      }
    };

    // Add date picker for startDay.
    startDay.setDefaultModel(new Model<Date>());
    startDay.add(new DatePicker());
    startDay.setRequired(true);
    form.add(startDay);
    DateTime today = new DateTime();
    startDay.setDefaultModelObject(today.toDate());

    // Add date picker for endDay.
    endDay.setDefaultModel(new Model<Date>());
    endDay.add(new DatePicker());
    endDay.setRequired(true);
    form.add(endDay);
    endDay.setDefaultModelObject(today.toDate());

    // Add granularity drop down list.
    Model<String> day = new Model<String>("Hour");
    DropDownChoice<String> granularityChoice =
        new DropDownChoice<String>("granularity", day, session.getListGranularity());
    granularityChoice.setRequired(true);
    form.add(granularityChoice);

    // Add source drop down list.
    DropDownChoice<String> sourceChoice =
        new DropDownChoice<String>("source", new Model<String>("SIM_OAHU_GRID"), session
            .getListSources());
    sourceChoice.setRequired(true);
    form.add(sourceChoice);

    // Add power type drop down list.
    DropDownChoice<String> powerChoice =
        new DropDownChoice<String>("powerType", new Model<String>("Energy"), session.getListType());
    powerChoice.setRequired(true);
    form.add(powerChoice);

    // Add checkbox
    CheckBox subSource = new CheckBox("subsource", new Model<Boolean>());
    form.add(subSource);

    // Add ajax submit button.
    form.add(new SubmitButton("generate", status, startDay, endDay, sourceChoice, powerChoice,
        granularityChoice, chart, subSource, session));
    
    this.add(form);
  }
}

