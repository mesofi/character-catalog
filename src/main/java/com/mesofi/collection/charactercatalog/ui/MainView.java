package com.mesofi.collection.charactercatalog.ui;

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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route("ui")
public class MainView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -4228406830903202529L;
    private final Grid<CharacterFigureView> grid;

    private final String TBD = "Not published yet";
    private final DateTimeFormatter sStyle = DateTimeFormatter.ofPattern("MMMM, yyyy");
    private final DateTimeFormatter lStyle = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    public MainView(CharacterFigureService characterFigureService, CharacterFigureModelMapper mapper) {
        log.debug("Displaying all characters ...");

        List<CharacterFigureView> items = characterFigureService.retrieveAllCharacters().stream()
                .map(mapper::toModelView).toList();

        this.grid = new Grid<>(CharacterFigureView.class, false);
        this.grid.addColumn(CharacterFigureView::getDisplayableName).setHeader("Name").setAutoWidth(true).setFlexGrow(0)
                .setSortable(true)
                .setFooter(String.format("%d total, %d MC EX, %d MC, %d DD, %d App, %d Crown, %d LOS, %d Figuarts",
                        items.size(), items.stream().filter($ -> $.getLineUp() == LineUp.MYTH_CLOTH_EX).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.MYTH_CLOTH).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.APPENDIX).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.DDP).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.CROWN).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.LEGEND).count(),
                        items.stream().filter($ -> $.getLineUp() == LineUp.FIGUARTS).count()));

        Locale locale = new Locale("jp", "JP");
        this.grid.addColumn(new NumberRenderer<>(CharacterFigureView::getReleasePrice, "¥ %,.0f", locale, "¥ " + TBD))
                .setHeader("Price").setSortable(true).setComparator(CharacterFigureView::getReleasePrice);

        grid.addColumn($ -> Objects.nonNull($.getPreorderDate())
                ? $.getPreorderDate().format($.isPreorderConfirmationDay() ? lStyle : sStyle)
                : unknownDate($.getReleaseDate())).setHeader("Preorder Date").setSortable(true)
                .setComparator(CharacterFigureView::getPreorderDate);

        grid.addColumn($ -> Objects.nonNull($.getReleaseDate())
                ? $.getReleaseDate().format($.isReleaseConfirmationDay() ? lStyle : sStyle)
                : TBD).setHeader("Release Date").setSortable(true).setComparator(CharacterFigureView::getReleaseDate);

        this.grid.setItems(items);
        add(grid);
    }

    private String unknownDate(LocalDate releaseDate) {
        if (Objects.isNull(releaseDate)) {
            return TBD;
        }
        return releaseDate.isBefore(LocalDate.now()) ? "Unknown" : TBD;
    }
}