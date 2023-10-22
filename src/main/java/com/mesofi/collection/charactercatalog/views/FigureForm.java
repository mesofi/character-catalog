package com.mesofi.collection.charactercatalog.views;

import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.io.Serial;

public class FigureForm extends FormLayout {

    @Serial
    private static final long serialVersionUID = -7253827208234126066L;

    Binder<CharacterFigureView> binder = new BeanValidationBinder<>(CharacterFigureView.class);
    CharacterFigureView characterFigureView;

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
        binder.bindInstanceFields(this);

        lineUp.setItems(LineUp.values());
        lineUp.setItemLabelGenerator(LineUp::getStringValue);

        series.setItems(Series.values());
        series.setItemLabelGenerator(Series::getStringValue);

        group.setItems(Group.values());
        group.setItemLabelGenerator(Group::getStringValue);

        add(originalName, baseName, lineUp, series, group, createButtonLayout());
    }

    public void setCharacterFigureView(CharacterFigureView characterFigureView) {
        this.characterFigureView = characterFigureView;
        binder.readBean(characterFigureView);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, characterFigureView)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(characterFigureView);
            fireEvent(new SaveEvent(this, characterFigureView));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    @Getter
    public static abstract class FigureFormEvent extends ComponentEvent<FigureForm> {
        @Serial
        private static final long serialVersionUID = 6476547532881871346L;
        private final CharacterFigureView characterFigureView;

        protected FigureFormEvent(FigureForm source, CharacterFigureView characterFigureView) {
            super(source, false);
            this.characterFigureView = characterFigureView;
        }
    }

    public static class SaveEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = 7279050229708181166L;

        SaveEvent(FigureForm source, CharacterFigureView characterFigureView) {
            super(source, characterFigureView);
        }
    }

    public static class DeleteEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = 2310452970737431414L;

        DeleteEvent(FigureForm source, CharacterFigureView characterFigureView) {
            super(source, characterFigureView);
        }

    }

    public static class CloseEvent extends FigureFormEvent {
        @Serial
        private static final long serialVersionUID = -8638191020626686710L;

        CloseEvent(FigureForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
