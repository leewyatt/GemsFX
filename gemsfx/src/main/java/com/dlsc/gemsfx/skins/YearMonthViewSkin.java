package com.dlsc.gemsfx.skins;

import com.dlsc.gemsfx.YearMonthView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class YearMonthViewSkin extends SkinBase<YearMonthView> {

    private final ObjectProperty<Integer> year = new SimpleObjectProperty<>(this, "year");

    public YearMonthViewSkin(YearMonthView control) {
        super(control);

        year.set(control.getValue().getYear());
        control.valueProperty().addListener(it -> year.set(control.getValue().getYear()));

        Label yearLabel = new Label();
        yearLabel.getStyleClass().add("year-label");
        yearLabel.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(year.get()), year));
        yearLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(yearLabel, Priority.ALWAYS);

        Region leftArrow = new Region();
        leftArrow.getStyleClass().addAll("arrow", "left-arrow");

        Region rightArrow = new Region();
        rightArrow.getStyleClass().addAll("arrow", "right-arrow");

        StackPane leftArrowButton = new StackPane(leftArrow);
        leftArrowButton.getStyleClass().addAll("arrow-button", "left-button");
        leftArrowButton.setOnMouseClicked(evt -> year.set(year.get() - 1));

        StackPane rightArrowButton = new StackPane(rightArrow);
        rightArrowButton.getStyleClass().addAll("arrow-button", "right-button");
        rightArrowButton.setOnMouseClicked(evt -> year.set(year.get() + 1));

        HBox header = new HBox(leftArrowButton, yearLabel, rightArrowButton);
        header.getStyleClass().add("header");

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid-pane");
        gridPane.add(createMonth(Month.JANUARY), 0, 0);
        gridPane.add(createMonth(Month.FEBRUARY), 2, 0);
        gridPane.add(createMonth(Month.MARCH), 0, 1);
        gridPane.add(createMonth(Month.APRIL), 2, 1);
        gridPane.add(createMonth(Month.MAY), 0, 2);
        gridPane.add(createMonth(Month.JUNE), 2, 2);
        gridPane.add(createMonth(Month.JULY), 0, 3);
        gridPane.add(createMonth(Month.AUGUST), 2, 3);
        gridPane.add(createMonth(Month.SEPTEMBER), 0, 4);
        gridPane.add(createMonth(Month.OCTOBER), 2, 4);
        gridPane.add(createMonth(Month.NOVEMBER), 0, 5);
        gridPane.add(createMonth(Month.DECEMBER), 2, 5);

        Region divider = new Region();
        divider.getStyleClass().add("divider");
        gridPane.add(divider, 1, 0);
        GridPane.setRowSpan(divider, 6);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();

        col1.setHalignment(HPos.CENTER);
        col1.setHgrow(Priority.ALWAYS);

        col2.setPrefWidth(1);
        col2.setMinWidth(1);
        col2.setMaxWidth(1);

        col3.setHgrow(Priority.ALWAYS);
        col3.setHalignment(HPos.CENTER);

        gridPane.getColumnConstraints().setAll(col1, col2, col3);

        VBox container = new VBox(header, gridPane);
        container.getStyleClass().add("container");

        header.setViewOrder(Double.NEGATIVE_INFINITY);

        getChildren().add(container);
    }

    private Node createMonth(Month month) {
        Label monthLabel = new Label(month.getDisplayName(TextStyle.FULL, Locale.getDefault()).toUpperCase());
        monthLabel.getStyleClass().add("month-label");
        monthLabel.setMinWidth(Region.USE_PREF_SIZE);
        monthLabel.setMaxWidth(Region.USE_PREF_SIZE);

        YearMonthView view = getSkinnable();

        Region selectionIndicator = new Region();
        selectionIndicator.visibleProperty().bind(Bindings.createBooleanBinding(() -> view.getValue().equals(YearMonth.of(year.get(), month)), view.valueProperty(), year));
        selectionIndicator.getStyleClass().add("selection-indicator");

        VBox box = new VBox(monthLabel, selectionIndicator);
        box.setMaxWidth(Region.USE_PREF_SIZE);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("month-box");
        box.setOnMouseClicked(evt -> view.setValue(YearMonth.of(year.get(), month.getValue())));

        GridPane.setMargin(box, new Insets(10, 30, 10, 30));

        // extra padding for the months at the top of the view
        if (month.equals(Month.JANUARY) || month.equals(Month.FEBRUARY)) {
            GridPane.setMargin(box, new Insets(30, 30, 10, 30));
        }

        // extra padding for the months at the bottom of the view
        if (month.equals(Month.NOVEMBER) || month.equals(Month.DECEMBER)) {
            GridPane.setMargin(box, new Insets(10, 30, 30, 30));
        }

        return box;
    }
}
