package com.mesofi.collection.charactercatalog.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
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

        // addClassName("list-view-class");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolBar(), getContent());
        populateGrid();

    }

    private void populateGrid() {
        List<CharacterFigureView> items = characterFigureService.retrieveAllCharacters().stream()
                .map(mapper::toModelView).toList();

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
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by name ...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button button = new Button("Add figure");
        HorizontalLayout toolBar = new HorizontalLayout(filterText, button);
        // toolBar.addClassName("figure-toolbar-class");
        return toolBar;
    }

    private void configureGrid() {

        final DateTimeFormatter sStyle = DateTimeFormatter.ofPattern("MMMM, yyyy");
        final DateTimeFormatter lStyle = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

        // grid.addClassName("figure-grid-class");
        grid.setSizeFull();

        this.grid.addColumn(CharacterFigureView::getDisplayableName).setHeader("Name").setFlexGrow(0).setSortable(true);

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

        grid.getColumns().stream().forEach($ -> {
            $.setAutoWidth(true);
        });

    }

    private String unknownDate(LocalDate releaseDate) {
        if (Objects.isNull(releaseDate)) {
            return TBD;
        }
        return releaseDate.isBefore(LocalDate.now()) ? "Unknown" : TBD;
    }
}
