package edu.hawaii.wicket.page.stoplight;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

/**
 * Displays an image on a web page.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class ImageSource extends WebComponent {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;

  /**
   * Displays an image on a web page.
   * 
   * @param id Wicket ID of the IMG tag.
   * @param model URL of the image source.
   */
  public ImageSource(String id, IModel<String> model) {
    super(id, model);
  }

  /**
   * Inserts an IMG tag into a web page.
   * 
   * @param tag IMG tag.
   */
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    checkComponentTag(tag, "img");
    tag.put("src", getDefaultModelObjectAsString());
  }

}