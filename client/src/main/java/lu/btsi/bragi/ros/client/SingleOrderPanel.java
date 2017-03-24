package lu.btsi.bragi.ros.client;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.time.format.DateTimeFormatter;

/**
 * Created by gillesbraun on 07/03/2017.
 */
public class SingleOrderPanel extends VBox {
    private Order order;

    private TableViewProducts tableViewProducts = new TableViewProducts();
    private Label labelWaiter = new Label("-");

    private ObservableList<ProductPriceForOrder> productsLocalizedForTable;

    private final Background bgYellow = new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null));
    private final Background bgNormal = Background.EMPTY;

    private Animation backgroundFlasher = new Transition() {
        {
            setDelay(Duration.millis(1000));
            setCycleDuration(Duration.millis(1500));
            setInterpolator(Interpolator.EASE_BOTH);
            setAutoReverse(true);
            setCycleCount(6);
        }
        @Override
        protected void interpolate(double frac) {
            Color color = new Color(1, 1, 0.7, frac);
            setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    };

    SingleOrderPanel(Order order) {
        this.order = order;
        setSpacing(7);
        setPadding(new Insets(14));
        if(order.getProcessing().equals((byte)0)) {
            backgroundFlasher.play();
        }

        tableViewProducts.setMinHeight(USE_COMPUTED_SIZE);
        tableViewProducts.setFocusTraversable(false);

        Label labelTable = new Label("Table: "+order.getTableId());
        Label labelTime = new Label("Time created: " + order.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("H:mm")));
        BorderPane info1 = new BorderPane();
        info1.setLeft(labelWaiter);
        info1.setRight(labelTime);

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        Label labelProcessing = new Label("Processing");
        Label labelProcessingDone = new Label("Processing Done");
        if(order.getProcessing().equals((byte)0)) {
            // empty
            labelProcessing.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
        } else {
            labelProcessing.setGraphic(fa.create(FontAwesome.Glyph.CHECK_CIRCLE));
        }
        if(order.getProcessingDone().equals((byte)0)) {
            // empty
            labelProcessingDone.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
        } else {
            labelProcessingDone.setGraphic(fa.create(FontAwesome.Glyph.CHECK_CIRCLE));
        }
        BorderPane info2 = new BorderPane();
        info2.setLeft(labelProcessing);
        info2.setRight(labelProcessingDone);
        getChildren().addAll(info1, info2, labelTable, tableViewProducts);
        setFillWidth(true);
        setPrefHeight(USE_COMPUTED_SIZE);

        labelWaiter.setText("Waiter: " + order.getWaiter().getName());
        loadProducts();
    }

    private void loadProducts() {
        productsLocalizedForTable = FXCollections.observableList(
                order.getProductPriceForOrder()
        );
        tableViewProducts.prefHeightProperty().bind(Bindings.size(productsLocalizedForTable).multiply(28).add(30));

        tableViewProducts.setItems(productsLocalizedForTable);
    }

}
