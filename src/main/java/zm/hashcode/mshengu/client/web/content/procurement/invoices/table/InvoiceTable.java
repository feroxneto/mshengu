/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.procurement.invoices.table;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import zm.hashcode.mshengu.app.facade.procurement.RequestFacade;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.client.web.MshenguMain;
import zm.hashcode.mshengu.client.web.content.procurement.invoices.form.UpdatePaymentForm;
import zm.hashcode.mshengu.client.web.content.procurement.invoices.views.InvoicesTab;
import zm.hashcode.mshengu.domain.procurement.Request;

/**
 *
 * @author Luckbliss
 */
public class InvoiceTable extends Table {

    private DecimalFormat f = new DecimalFormat("###,###.00");
    private BigDecimal grandTotal;
    private final InvoicesTab tab;
    private final MshenguMain main;

    public InvoiceTable(InvoicesTab tab, final MshenguMain main) {
        setSizeFull();
        this.tab = tab;
        this.main = main;
        addContainerProperty("PO Date", String.class, null);
        addContainerProperty("Delivery Date", String.class, null);
        addContainerProperty("Order Number", String.class, null);
        addContainerProperty("Supplier", String.class, null);
        addContainerProperty("Invoice", String.class, null);
        addContainerProperty("Total", String.class, null);
        addContainerProperty("Update", Button.class, null);

//        String datemonth = new SimpleDateFormat("MMMM").format(new Date());
//        String dateyear = new SimpleDateFormat("YYYY").format(new Date());

        loadTable(RequestFacade.getRequestService().findAll());
    }

    public final void loadTable(List<Request> requests) {
        grandTotal = new BigDecimal("0.00");
        if (requests != null) {
            for (int i = requests.size() - 1; i >= 0; i--) {
                if (requests.get(i).getInvoiceNumber() != null && requests.get(i).getPaymentAmount() == null) {
//                    String datemonth = new SimpleDateFormat("MMMM").format(requests.get(i).getDeliveryDate());
//                    String dateyear = new SimpleDateFormat("YYYY").format(requests.get(i).getDeliveryDate());
//                    if (datemonth.equals(month) && year.equals(dateyear)) {
                    Button update = new Button("Effect Payment");
                    update.setData(requests.get(i).getId());
                    update.setStyleName(Reindeer.BUTTON_LINK);
                    update.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            String itemId = event.getButton().getData().toString();
                            Request request = RequestFacade.getRequestService().findById(itemId);
                            UpdatePaymentForm form = new UpdatePaymentForm(main, request, tab);
                            tab.removeAllComponents();
                            tab.addComponent(form);
                        }
                    });
                    addItem(new Object[]{
                        getDelivery(requests.get(i).getOrderDate()),
                        getDelivery(requests.get(i).getDeliveryDate()),
                        requests.get(i).getOrderNumber(),
                        requests.get(i).getServiceProviderName(),
                        requests.get(i).getInvoiceNumber(),
                        f.format(requests.get(i).getTotal()),
                        update,}, requests.get(i).getId());
                    grandTotal = grandTotal.add(requests.get(i).getTotal());
//                    }
                }
            }
        }
    }

    public String getGrandTotal() {
        return f.format(grandTotal);
    }

    private String getDelivery(Date date) {
        if (date != null) {
            return new DateTimeFormatHelper().getDayMonthYear(date);
        }
        return null;
    }
}