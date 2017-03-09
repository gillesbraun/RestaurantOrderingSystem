package lu.btsi.bragi.ros.client;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.ProductLocalized;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;
import lu.btsi.bragi.ros.models.pojos.Table;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.jooq.types.UInteger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 09/03/2017.
 */
public class SingleInvoicePane extends VBox {

    @FXML private ListView<ProductLocalized> listViewProducts;
    @FXML private Label labelTable, lableInvoice, labelTotalPrice, labelPaid;

    private ObservableList<ProductLocalized> listProducts = FXCollections.observableArrayList();

    private static final String LANGUAGE = "en";
    private List<ProductPriceForOrder> produtsPriceForOrder;

    SingleInvoicePane(Table table, List<Order> orders) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SingleInvoicePanel.fxml"));
        loader.setController(this);
        VBox root = loader.load();
        root.setFillWidth(true);
        setFillWidth(true);
        getChildren().setAll(root);

        listViewProducts.setItems(listProducts);
        listViewProducts.setCellFactory(param -> new ListCell<ProductLocalized>(){
            @Override
            protected void updateItem(ProductLocalized item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null) {
                    setText(null);
                    return;
                }
                Optional<UInteger> quantity = produtsPriceForOrder.stream()
                        .filter(ppfo -> ppfo.getProductId().equals(item.getProductId()))
                        .map(ProductPriceForOrder::getQuantity)
                        .findFirst();
                Optional<Double> price = produtsPriceForOrder.stream()
                        .filter(ppfo -> ppfo.getProductId().equals(item.getProductId()))
                        .map(ProductPriceForOrder::getPricePerProduct)
                        .findFirst();
                quantity.ifPresent(qty -> setText(qty + " " + item.getLabel() + " " + price.orElse(0.0) + "€"));
            }
        });

        labelTable.setText(table.toString());
        fillList(orders);
        setTotalPrice(orders);
        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        labelPaid.setGraphic(fa.create(FontAwesome.Glyph.CIRCLE_THIN));
        labelPaid.setText("");
    }

    private void setTotalPrice(List<Order> orders) {
        Optional<BigDecimal> sumOptional = orders.stream()
                .flatMap(order -> order.getProductPriceForOrder().stream())
                .map(value -> BigDecimal.valueOf(value.getPricePerProduct()).multiply(new BigDecimal(value.getQuantity().toBigInteger())))
                .reduce(BigDecimal::add);
        sumOptional.ifPresent(sum -> labelTotalPrice.setText(sum + " €"));
    }

    private void fillList(List<Order> orders) {
        produtsPriceForOrder = orders.stream().flatMap(o -> o.getProductPriceForOrder().stream()).collect(toList());
        listProducts.setAll(
                orders.stream()
                        .flatMap(o -> o.getProductPriceForOrder().stream())
                        .map(ProductPriceForOrder::getProduct)
                .flatMap(product ->
                                product.getProductLocalized().stream()
                                .filter(pL -> pL.getLanguageCode().equals(LANGUAGE))
                )
                .collect(toList())
        );

        listViewProducts.prefHeightProperty().bind(Bindings.size(listProducts).multiply(28));
    }


}