/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.client.web.content.humanresources.nationality.form;


import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import zm.hashcode.mshengu.app.util.UIComboBoxHelper;
import zm.hashcode.mshengu.app.util.UIComponentHelper;
import zm.hashcode.mshengu.app.util.validation.UIValidatorHelper;
import zm.hashcode.mshengu.client.web.content.humanresources.nationality.models.CountryBean;

/**
 *
 * @author Ferox
 */
public class CountryForm  extends FormLayout {

    private UIComponentHelper UIComponent = new UIComponentHelper();
    private UIComboBoxHelper UIComboBox = new UIComboBoxHelper();
    private final CountryBean bean;
    public final BeanItem<CountryBean> item;
    public final FieldGroup binder;
    // Define Buttons
    public Button save = new Button("Save");
    public Button edit = new Button("Edit");
    public Button cancel = new Button("Cancel");
    public Button update = new Button("Update");
    public Button delete = new Button("Delete");
    public Label errorMessage;

    public CountryForm() {
        bean = new CountryBean();
        item = new BeanItem<>(bean);
        binder = new FieldGroup(item);
        HorizontalLayout buttons = getButtons();
        buttons.setSizeFull();
        // Determines which properties are shown
        update.setVisible(false);
        delete.setVisible(false);

        // UIComponent
        
        TextField countryName = UIComponent.getTextField("Country Name:", "name", CountryBean.class, binder);
        countryName = UIValidatorHelper.setRequiredTextField(countryName, "Country Name");
        TextField nationality = UIComponent.getTextField("Nationality:", "nationality", CountryBean.class, binder);
        nationality = UIValidatorHelper.setRequiredTextField(nationality, "Nationality");
        
        errorMessage = UIComponent.getErrorLabel();
        
        GridLayout grid = new GridLayout(4, 10);
        grid.setSizeFull();

        grid.addComponent(errorMessage, 1, 0, 2, 0);

        grid.addComponent(countryName, 0, 1);
        grid.addComponent(nationality, 1, 1);     

        grid.addComponent(new Label("<hr/>", ContentMode.HTML), 0, 5, 2, 5);
        grid.addComponent(buttons, 0, 6, 2, 6);

        addComponent(grid);

    }

    private HorizontalLayout getButtons() {
        HorizontalLayout buttons = new HorizontalLayout();
        save.setSizeFull();
        edit.setSizeFull();
        cancel.setSizeFull();
        update.setSizeFull();
        delete.setSizeFull();

        buttons.addComponent(save);
        buttons.addComponent(edit);
        buttons.addComponent(cancel);
        buttons.addComponent(update);
        buttons.addComponent(delete);
        return buttons;
    }
}
