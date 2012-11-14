package edu.hawaii.wicket.page.visualization;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import edu.hawaii.wicket.EkolugicalCarbonometer;

/**
 * Tests the Visualization page to see if it is rendered correctly.
 * 
 * @author BJ Peter DeLaCruz, Wahib Hanani, Lyneth Peou
 * @version 2.0
 */
public class TestVisualizationPage {

  /**
   * Tests the Visualization page to see if it is rendered correctly when it first loads.
   */
  @Test
  public void testVisualizationPageOnLoad() {
    WicketTester tester = new WicketTester(new EkolugicalCarbonometer());
    tester.startPage(VisualizationPage.class);
    tester.assertRenderedPage(VisualizationPage.class);
  }
}