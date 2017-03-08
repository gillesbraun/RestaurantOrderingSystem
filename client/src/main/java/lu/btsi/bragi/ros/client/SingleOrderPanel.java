package lu.btsi.bragi.ros.client;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.models.pojos.Waiter;
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

    private static final String LANGUAGE = "de";
    private Client client;
    private Order order;

    private ListView<ProductLocalized> listViewProducts = new ListView<>();
    private Label labelWaiter = new Label("-");

    private ObservableList<ProductLocalized> productsLocalizedForList;
    private List<ProductPriceForOrder> productsPriceForOrder;

    public SingleOrderPanel(Client client, Order order) {
        this.client = client;
        this.order = order;
        setSpacing(7);


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

        loadWaiter();
        loadProducts();
    }

    private void loadProducts() {
        client.sendWithAction(new MessageGet<ProductPriceForOrder>(ProductPriceForOrder.class), message -> {
            try {
                List<ProductPriceForOrder> payload = new Message<ProductPriceForOrder>(message).getPayload();
                productsPriceForOrder = payload.stream()
                        .filter(ppfo -> ppfo.getOrderId().equals(order.getId()))
                        .collect(toList());


                List<UInteger> productIDs = productsPriceForOrder.stream()
                        .map(ProductPriceForOrder::getProductId)
                        .collect(toList());

                client.sendWithAction(new MessageGet<>(ProductLocalized.class), message1 -> {
                    try {
                        List<ProductLocalized> productLocalizedList = new Message<ProductLocalized>(message1).getPayload();
                        List<ProductLocalized> productsInOrderLocalized = productLocalizedList.stream()
                                .filter(pL -> productIDs.contains(pL.getProductId()))
                                .filter(pL -> pL.getLanguageCode().equals(LANGUAGE))
                                .collect(toList());
                        productsLocalizedForList = FXCollections.observableList(productsInOrderLocalized);

                        listViewProducts.prefHeightProperty().bind(Bindings.size(productsLocalizedForList).multiply(28));

                        listViewProducts.setItems(productsLocalizedForList);
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                });

            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadWaiter() {
        client.sendWithAction(new MessageGet<>(Waiter.class), m -> {
            try {
                List<Waiter> payload = new Message<Waiter>(m).getPayload();
                Optional<Waiter> waiterFound = payload.stream()
                        .filter(w -> w.getId().equals(order.getWaiterId()))
                        .findFirst();
                waiterFound.ifPresent(w -> labelWaiter.setText("Waiter: "+w.getName()));
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }

}
