/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.kpianalysis.setupkpis.views;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import zm.hashcode.mshengu.app.facade.kpianalysis.KPIFacade;
import zm.hashcode.mshengu.app.facade.kpianalysis.KPIItemFacade;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.kpianalysis.kpianalysis.KPIMenu;
import zm.hashcode.mshengu.client.web.content.kpianalysis.setupkpis.forms.KPIForm;
import zm.hashcode.mshengu.client.web.content.kpianalysis.setupkpis.models.KPIBean;
import zm.hashcode.mshengu.client.web.content.kpianalysis.setupkpis.tables.KPITable;
import zm.hashcode.mshengu.domain.kpianalysis.KPA;
import zm.hashcode.mshengu.domain.kpianalysis.KPIItem;

/**
 *
 * @author Luckbliss
 */
public class KPAFourTab extends VerticalLayout implements
        Button.ClickListener {

    private final MshenguMain main;
    private final KPITable table;
    private final KPIForm form;
    private KPA kpi;
    private int counter = 0;

    public KPAFourTab(MshenguMain app) {
        main = app;
        kpi = getKpi();
        table = new KPITable(getKPIItems(kpi), main);
        table.getTab(kpi.getTab(), this);
        form = new KPIForm(main, kpi);
        setSizeFull();
        addComponent(form);
        addComponent(table);
        addListeners();
        updateMessage();
    }
    
     public String getKPAName(){
        return kpi.getName();
    }

    private KPA getKpi() {
        KPA kpi = KPIFacade.getKPIService().findByTab("four");
        if (kpi != null) {
            return kpi;
        } else {
            List<KPIItem> items = getItems();
            KPA newkpi = new KPA.Builder("Maintenance Management")
                    .tab("four")
                    .items(items)
                    .build();
            KPIFacade.getKPIService().persist(newkpi);
            return newkpi;
        }
    }

    private List<KPIItem> getItems() {
        List<KPIItem> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            KPIItem kPIItem = addKPIItems(i);
            KPIItemFacade.getKPIItemService().persist(kPIItem);
            items.add(kPIItem);
        }
        return items;
    }
    
    private KPIItem addKPIItems(int i) {
        KPIItem kPIItem = null;
        int number = i + 1;
        if (i == 0) {
            kPIItem = new KPIItem.Builder("Maintenance Efficiency")
                    .detailedDescription("empty")
                    .measureType("B")
                    .uom("R/Km")
                    .kpiNumber(number)
                    .build();
            return kPIItem;
        } else if (i == 1) {
            kPIItem = new KPIItem.Builder("Spend per Service")
                    .detailedDescription("empty")
                    .measureType("B")
                    .uom("R/Service")
                    .kpiNumber(number)
                    .build();
            return kPIItem;
        } else if (i == 2) {
            kPIItem = new KPIItem.Builder("Spend per Unit")
                    .detailedDescription("empty")
                    .measureType("B")
                    .uom("R/Unit")
                    .kpiNumber(number)
                    .build();
            return kPIItem;
        } else if (i == 3) {
            kPIItem = new KPIItem.Builder("Vehicles Above Spec")
                    .detailedDescription("empty")
                    .measureType("B")
                    .uom("#")
                    .kpiNumber(number)
                    .build();
            return kPIItem;
        } else if (i == 4) {
            kPIItem = new KPIItem.Builder("Maintenance Spend")
                    .detailedDescription("empty")
                    .measureType("B")
                    .uom("R")
                    .kpiNumber(number)
                    .build();
            return kPIItem;
        }
        return null;
    }

    private List<KPIItem> getKPIItems(KPA kpi) {
        for (KPIItem item : kpi.getItems()) {
            if (!item.getShortDescription().equalsIgnoreCase("empty")) {
                counter += 1;
            }
        }
        return kpi.getItems();
    }

    private void updateMessage() {
        form.message.setValue(counter + " " + form.msg);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        if (source == form.edit) {
            edit();
        } else if (source == form.update) {
            saveForm(form.binder);
        } else if (source == form.cancel) {
            getHome();
        }
    }

    private void saveForm(FieldGroup binder) {
        try {
            binder.commit();
            KPIFacade.getKPIService().merge(getEntity(binder));
            getHome();
            Notification.show("Record Updated!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Notification.show("Values MISSING!", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private KPA getEntity(FieldGroup binder) {
        final KPIBean bean = ((BeanItem<KPIBean>) binder.getItemDataSource()).getBean();

        final KPA newkpi = new KPA.Builder(bean.getName())
                .kpi(kpi)
                .build();
        return newkpi;
    }

    private void edit() {
        form.update.setVisible(true);
        form.edit.setVisible(false);
        form.name.setReadOnly(false);
    }

    private void getHome() {
        main.content.setSecondComponent(new KPIMenu(main, "SETUP"));
    }

    private void addListeners() {
        form.edit.addClickListener((Button.ClickListener) this);
        form.update.addClickListener((Button.ClickListener) this);
        form.cancel.addClickListener((Button.ClickListener) this);
    }
}
