/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.fleetmanagement.fleetmaintenance.charts;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import zm.hashcode.mshengu.client.web.MshenguMain;

/**
 *
 * @author ColinWa
 */
public class DashBoardChartUI extends FormLayout {

    public VerticalLayout chartVerticalLayout = new VerticalLayout();

    public DashBoardChartUI(MshenguMain main) {
        chartVerticalLayout.setSizeFull();

        addComponent(chartVerticalLayout);
    }
}
