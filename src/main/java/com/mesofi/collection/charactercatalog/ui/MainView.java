package com.mesofi.collection.charactercatalog.ui;

import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.mesofi.collection.charactercatalog.mappers.CharacterFigureModelMapper;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.service.CharacterFigureService;
import com.mesofi.collection.charactercatalog.views.CharacterFigureView;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Route("ui")
public class MainView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = -4228406830903202529L;
    private final Grid<CharacterFigureView> grid;

    private final String TBD = "Not published yet";
    private final DateTimeFormatter sStyle = DateTimeFormatter.ofPattern("MMMM, yyyy");
    private final DateTimeFormatter lStyle = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    public MainView(CharacterFigureService characterFigureService, CharacterFigureModelMapper mapper) {
        log.debug("Displaying all characters ...");

        List<CharacterFigureView> items = characterFigureService.retrieveAllCharacters().stream().map(mapper::toView)
                .toList();

        this.grid = new Grid<>(CharacterFigureView.class, false);
        this.grid.setAllRowsVisible(true);
        this.grid.addColumn(CharacterFigureView::getDisplayableName).setHeader("Name").setAutoWidth(true).setFlexGrow(0)
                .setFrozen(true).setSortable(true)
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

        this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        this.grid.setItems(items);
        this.grid.addSelectionListener(selection -> {
            Optional<CharacterFigureView> optional = selection.getFirstSelectedItem();
            // Selected
            optional.ifPresent($ -> log.debug("Selected: {}", $.getDisplayableName()));
        });
        add(grid);
    }

    private String unknownDate(LocalDate releaseDate) {
        if (Objects.isNull(releaseDate)) {
            return TBD;
        }
        return releaseDate.isBefore(LocalDate.now()) ? "Unknown" : TBD;
    }

    private static ComponentRenderer<CharacterDetailsFormLayout, CharacterFigureView> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(CharacterDetailsFormLayout::new, CharacterDetailsFormLayout::setPerson);
    }

    private static class CharacterDetailsFormLayout extends FormLayout {
        private final NativeLabel label = new NativeLabel();
        private final NativeLabel label1 = new NativeLabel();
        // private final TextField emailField = new TextField("Email address");
        // private final TextField phoneField = new TextField("Phone number");
        // private final TextField streetField = new TextField("Street address");
        // private final TextField zipField = new TextField("ZIP code");
        // private final TextField cityField = new TextField("City");
        // private final TextField stateField = new TextField("State");

        public CharacterDetailsFormLayout() {
            add(label);
            add(label1);
            // Stream.of(emailField, phoneField, streetField, zipField, cityField,
            // stateField).forEach(field -> {
            // field.setReadOnly(true);
            /// add(field);
            // });

            // setResponsiveSteps(new ResponsiveStep("0", 3));
            // setColspan(emailField, 3);
            // setColspan(phoneField, 3);
            // setColspan(streetField, 3);
        }

        public void setPerson(CharacterFigureView characterFigureView) {
            label.setText(characterFigureView.getDisplayableName());
            label.setTitle("dd");

            label1.setText("dddd");
            // emailField.setValue(characterFigureView.getDisplayableName());
        }
    }
}
