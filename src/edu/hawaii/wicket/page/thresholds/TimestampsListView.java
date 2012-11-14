package edu.hawaii.wicket.page.thresholds;

import java.text.DecimalFormat;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 * Constructs a list view of timestamps.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TimestampsListView extends ListView<XMLGregorianCalendar> {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a list view of timestamps.
   * 
   * @param id Table cell that contains a timestamp.
   * @param results List of timestamps for one period.
   */
  public TimestampsListView(String id, List<XMLGregorianCalendar> results) {
    super(id, results);
  }

  /**
   * Formats the timestamps that are displayed to the user.
   * 
   * @param item Table cell that contains a timestamp.
   */
  protected void populateItem(ListItem<XMLGregorianCalendar> item) {
    item.add(new Label("tstamp00-11", (new DecimalFormat("00")).format(item.getModelObject()
        .getHour())
        + ":"
        + (new DecimalFormat("00")).format(item.getModelObject().getMinute())
        + ":"
        + (new DecimalFormat("00")).format(item.getModelObject().getSecond()) + " HST") {

      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      /**
       * Colors the background red if the data is above the maximum threshold value.
       */
      protected void onComponentTag(ComponentTag tag) {
        tag.put("STYLE", "border-bottom: 1px solid #008000; font-weight: bold; text-align: right;"
            + " height: 25px; width: 100px");
      }

    });
  }

}