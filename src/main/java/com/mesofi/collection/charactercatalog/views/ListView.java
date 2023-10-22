package com.mesofi.collection.charactercatalog.views;

import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Myth Clothllection")
@Route(value = "ui")
public class ListView extends VerticalLayout {
    @Serial
    private static final long serialVersionUID = 8725171465467462841L;

    private final String TBD = "Not published yet";

    private final Grid<CharacterFigureView> grid = new Grid<>();
    private final TextField filterText = new TextField();
    private FigureForm form;

    private final CharacterFigureService characterFigureService;
    private final CharacterFigureModelMapper mapper;

    public ListView(CharacterFigureService characterFigureService, CharacterFigureModelMapper mapper) {
        this.characterFigureService = characterFigureService;
        this.mapper = mapper;

        List<CharacterFigureView> items = characterFigureService.retrieveAllCharacters().stream().map(mapper::toView)
                .toList();

        // addClassName("list-view-class");
        setSizeFull();

        configureGrid(items);
        configureForm();

        add(getToolBar(), getContent());
        populateGrid(items);
        closeEditor();
    }

    private void closeEditor() {
        form.setCharacterFigureView(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void populateGrid(List<CharacterFigureView> items) {

        grid.setItems(items);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        // content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new FigureForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveFigure);
        form.addDeleteListener(this::deleteFigure);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveFigure(FigureForm.SaveEvent event) {
        CharacterFigureView newCharacterFigureView = event.getCharacterFigureView();
        characterFigureService.createNewCharacter(mapper.toModel(newCharacterFigureView));
        // updateList ?? TODO How to update the list?
        closeEditor();
    }

    private void deleteFigure(FigureForm.DeleteEvent deleteEvent) {
        System.out.println("DELETED");
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name ...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button button = new Button("Add figure");
        button.addClickListener(e -> addNewFigure());
        HorizontalLayout toolBar = new HorizontalLayout(filterText, button);
        toolBar.addClassName("figure-toolbar-class");
        return toolBar;
    }

    private void addNewFigure() {
        grid.asSingleSelect().clear();
        editCharacter(new CharacterFigureView());
    }

    private void configureGrid(List<CharacterFigureView> items) {

        final DateTimeFormatter sStyle = DateTimeFormatter.ofPattern("MMMM, yyyy");
        final DateTimeFormatter lStyle = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

        // grid.addClassName("figure-grid-class");
        grid.setSizeFull();

        this.grid.addColumn(CharacterFigureView::getDisplayableName).setHeader("Name").setFlexGrow(0).setSortable(true)
                .setFooter(String.format("%d total, %d MC EX, %d MC, %d DD, %d App, %d Crown, %d LOS, %d Figuarts",
                        items.size(), items.stream().filter($ -> $.getLineUp() == LineUp.MYTH_CLOTH_EX).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.MYTH_CLOTH).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.APPENDIX).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.DDP).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.CROWN).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.LEGEND).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.FIGUARTS).count()));

        Locale jpyLocale = new Locale("jp", "JP");
        Locale cnyLocale = new Locale("cn", "CN");
        this.grid.addColumn($ -> {
            if ($.isHongKongVersion()) {
                return Objects.isNull($.getReleasePrice()) ? "HK ¥ " + TBD
                        : String.format(cnyLocale, "HK ¥ %.0f", $.getReleasePrice());
            } else {

                return Objects.isNull($.getReleasePrice()) ? "¥ " + TBD
                        : String.format(jpyLocale, "¥ %,.0f", $.getReleasePrice());

            }
        }).setHeader("Price").setSortable(true).setComparator(CharacterFigureView::getReleasePrice);

        grid.addColumn($ -> Objects.nonNull($.getPreorderDate())
                ? $.getPreorderDate().format($.isPreorderConfirmationDay() ? lStyle : sStyle)
                : unknownDate($.getReleaseDate())).setHeader("Preorder Date").setSortable(true)
                .setComparator(CharacterFigureView::getPreorderDate);

        grid.addColumn($ -> Objects.nonNull($.getReleaseDate())
                ? $.getReleaseDate().format($.isReleaseConfirmationDay() ? lStyle : sStyle)
                : TBD).setHeader("Release Date").setSortable(true).setComparator(CharacterFigureView::getReleaseDate);

        grid.addColumn(CharacterFigureView::getLineUp).setHeader("Line Up").setFlexGrow(0).setSortable(true);

        grid.getColumns().forEach($ -> $.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editCharacter(e.getValue()));
    }

    private void editCharacter(CharacterFigureView value) {
        if (Objects.isNull(value)) {
            closeEditor();
        } else {
            form.setCharacterFigureView(value);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private String unknownDate(LocalDate releaseDate) {
        if (Objects.isNull(releaseDate)) {
            return TBD;
        }
        return releaseDate.isBefore(LocalDate.now()) ? "Unknown" : TBD;
    }
}
