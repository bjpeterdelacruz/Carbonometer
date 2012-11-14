package edu.hawaii.wicket.page.visualization;

import java.util.Date;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import edu.hawaii.wicket.Session;
import edu.hawaii.wicket.page.BasePage;

/**
 * Displays information about a power source.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class VisualizationPage extends BasePage {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Used to get the date from the user for the start day. */
  private final DateTextField startDay = new DateTextField("start_day");
  /** Used to get the date from the user for the end day. */
  private final DateTextField endDay = new DateTextField("end_day");
  /** A user's session. */
  private Session session = (Session) this.getSession(); 
  /**
   * Creates a page that displays information about the Oahu power grid.
   */
  public VisualizationPage() {
    Form<Object> form = new Form<Object>("visualedForm") {

      /** Support serialization. */
      private static final long serialVersionUID = 1L;
    };

    // Add date picker for startDay.
    startDay.setDefaultModel(new Model<Date>());
    startDay.add(new DatePicker());
    startDay.setRequired(true);
    form.add(startDay);    
    
    DateTime today = new DateTime().minusDays(7);    
    startDay.setDefaultModelObject(today.toDate());
    
    // Add date picker for endDay.
    endDay.setDefaultModel(new Model<Date>());
    endDay.add(new DatePicker());
    endDay.setRequired(true);
    form.add(endDay);
    today = new DateTime();
    endDay.setDefaultModelObject(today.toDate());

    // Add source drop down list.
    DropDownChoice<String> sourceChoice =
        new DropDownChoice<String>("source", new Model<String>("SIM_OAHU_GRID"), session
            .getListSources());
    sourceChoice.setRequired(true);
    form.add(sourceChoice);

    this.add(form);
  }
}