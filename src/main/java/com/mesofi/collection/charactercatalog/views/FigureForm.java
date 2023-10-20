package com.mesofi.collection.charactercatalog.views;

import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class FigureForm extends FormLayout {

    private static final long serialVersionUID = -7253827208234126066L;

    TextField originalName = new TextField("Name");
    TextField baseName = new TextField("Base Name");
    ComboBox<LineUp> lineUp = new ComboBox<>("Line Up");
    ComboBox<Series> series = new ComboBox<>("Series");
    ComboBox<Group> group = new ComboBox<>("Category");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    public FigureForm() {
        addClassName("my-class");
        lineUp.setItems(LineUp.values());
        lineUp.setItemLabelGenerator(LineUp::getStringValue);

        series.setItems(Series.values());
        series.setItemLabelGenerator(Series::getStringValue);

        group.setItems(Group.values());
        group.setItemLabelGenerator(Group::getStringValue);

        add(originalName, baseName, lineUp, series, group, createButtonLayout());
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }
}
