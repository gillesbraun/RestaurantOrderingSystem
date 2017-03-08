package lu.btsi.bragi.ros.client;

import com.google.gson.Gson;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jooq.types.UInteger;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 07/03/2017.
 */
public class SingleOrderPanel extends VBox {

    private static final String LANGUAGE = "en";
    private Client client;
    private Order order;

    private ListView<ProductLocalized> listViewProducts = new ListView<>();
    private Label labelWaiter = new Label("-");

    private ObservableList<ProductLocalized> productsLocalizedForList;
    private List<ProductPriceForOrder> productsPriceForOrder;

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

    SingleOrderPanel(Client client, Order order) {
        this.client = client;
        this.order = order;
        setSpacing(7);
        setPadding(new Insets(14));
        if(order.getProcessing().equals((byte)0)) {
            backgroundFlasher.play();
        }

        listViewProducts.setCellFactory(param -> new ListCell<ProductLocalized>() {
            @Override
            protected void updateItem(ProductLocalized item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setText(null);
                    return;
                }
                Optional<UInteger> quantity = productsPriceForOrder.stream()
                        .filter(ppfo -> ppfo.getProductId().equals(item.getProductId()))
                        .map(ProductPriceForOrder::getQuantity)
                        .findFirst();
                quantity.ifPresent(qty -> setText(qty + " " + item.getLabel()));
            }
        });
        listViewProducts.setMinHeight(USE_COMPUTED_SIZE);
        listViewProducts.setFocusTraversable(false);

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
        getChildren().addAll(info1, info2, labelTable, listViewProducts);
        setFillWidth(true);
        setPrefHeight(USE_COMPUTED_SIZE);

        labelWaiter.setText("Waiter: " + order.getWaiter().getName());
        loadProducts();
    }

    private void loadProducts() {
        System.out.println(new Gson().toJson(order));
        productsPriceForOrder = order.getProductPriceForOrder();
        productsLocalizedForList = FXCollections.observableList(
                order.getProductPriceForOrder().stream()
                        .flatMap(
                                productPriceForOrder ->
                                productPriceForOrder.getProduct().getProductLocalized().stream()
                                .filter(pL -> pL.getLanguageCode().equals(LANGUAGE))
                        )
                .collect(toList())
        );
        listViewProducts.prefHeightProperty().bind(Bindings.size(productsLocalizedForList).multiply(28));

        listViewProducts.setItems(productsLocalizedForList);
    }

}
