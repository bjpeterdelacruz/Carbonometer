package edu.hawaii.wicket.page.thresholds;

import java.text.DecimalFormat;
import java.util.List;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import edu.hawaii.wicket.Session;

/**
 * Constructs a list view of data.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class ResultsListView extends ListView<Double> {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** Table data. */
  private static final String DATA_LABEL_1 = "data00-11";
  /** Table data element. */
  private static final String STYLE_ELEMENT = "STYLE";
  /** Used to format data to two decimal places. */
  private static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("0.00");
  /** A user's session. */
  private final Session SESSION;
  /** Units for amount of carbon emitted. */
  private static final String UNITS = " lbs CO2 / MWh";

  /**
   * Constructs a list view of data.
   * 
   * @param id Table cell that contains data.
   * @param results List of data for one period.
   * @param maxLabel Maximum threshold value.
   * @param session This user's session.
   */
  public ResultsListView(String id, List<Double> results, Label maxLabel, Session session) {
    super(id, results);
    this.SESSION = session;
  }

  /**
   * Colors the background green, yellow, or red based on carbon emission data for a specific
   * timestamp.
   * 
   * @param item Table cell that contains data.
   */
  protected void populateItem(ListItem<Double> item) {

    if (item.getModelObject() >= SESSION.getMaxThreshold()) {
      item.add(new Label(DATA_LABEL_1, TWO_DECIMAL_PLACES.format(item.getModelObject()) + UNITS) {

        /** Support serialization. */
        private static final long serialVersionUID = 1L;

        /**
         * Colors the background red if the data is above the maximum threshold value.
         */
        protected void onComponentTag(ComponentTag tag) {
          tag.put(STYLE_ELEMENT, "border-bottom: 1px solid #008000; color: #B21212;"
              + " font-weight: bold; height: 25px; text-align: center; width: 175px");
        }

      });
    } // end if
    else if (item.getModelObject() < SESSION.getMaxThreshold()
        && item.getModelObject() > SESSION.getMinThreshold()) {
      item.add(new Label(DATA_LABEL_1, TWO_DECIMAL_PLACES.format(item.getModelObject()) + UNITS) {

        /** Support serialization. */
        private static final long serialVersionUID = 1L;

        /**
         * Colors the background yellow if the data is between the maximum and minimum threshold
         * values.
         */
        protected void onComponentTag(ComponentTag tag) {
          tag.put(STYLE_ELEMENT, "border-bottom: 1px solid #008000; font-weight: bold;"
              + " height: 25px; text-align: center; width: 175px");
        }

      });
    } // end else if
    else {
      item.add(new Label(DATA_LABEL_1, TWO_DECIMAL_PLACES.format(item.getModelObject()) + UNITS) {

        /** Support serialization. */
        private static final long serialVersionUID = 1L;

        /**
         * Colors the background green if the data is below the minimum threshold value.
         */
        protected void onComponentTag(ComponentTag tag) {
          tag.put(STYLE_ELEMENT, "border-bottom: 1px solid #008000; color: green;"
              + " font-weight: bold; height: 25px; text-align: center; width: 175px");
        }
      });
    } // end else

  }

}