package lu.btsi.bragi.ros.client;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lu.btsi.bragi.ros.client.settings.Config;
import lu.btsi.bragi.ros.models.pojos.Language;
import lu.btsi.bragi.ros.models.pojos.ProductPriceForOrder;

import java.util.Arrays;

/**
 * Created by gillesbraun on 22/03/2017.
 */
public class TableViewProducts extends TableView<ProductPriceForOrder> {
    private TableColumn<ProductPriceForOrder, String>
            cellQuantity = new TableColumn<>("Qty"),
            cellName = new TableColumn<>("Label"),
            cellPricePer = new TableColumn<>("Single Price"),
            cellPriceTotal = new TableColumn<>("Total Price");

    public TableViewProducts() {
        getColumns().setAll(Arrays.asList(cellQuantity, cellName, cellPricePer, cellPriceTotal));
        Language language = Config.getInstance().generalSettings.getLanguage();

        cellQuantity.setPrefWidth(50);
        cellQuantity.setCellValueFactory(ppfo ->
                new ReadOnlyObjectWrapper<>(ppfo.getValue().getQuantity().toString())
        );

        cellName.setPrefWidth(150);
        cellName.setCellValueFactory(ppfo -> {
            try {
                return new ReadOnlyObjectWrapper<>(ppfo.getValue().getProductInLanguage(language).getLabel());
            } catch(Exception ignore) {
                return new ReadOnlyObjectWrapper<>("No Translation (ID: "+ppfo.getValue().getProductId().toString()+")");
            }
        });

        cellPricePer.setPrefWidth(60);
        cellPricePer.setCellValueFactory(ppfo ->
                new ReadOnlyObjectWrapper<>(Config.getInstance().formatCurrency(ppfo.getValue().getPricePerProduct().doubleValue()))
        );

        cellPriceTotal.setPrefWidth(60);
        cellPriceTotal.setCellValueFactory(ppfo ->
                new ReadOnlyObjectWrapper<>(Config.getInstance().formatCurrency(ppfo.getValue().getTotalPriceOfProduct().doubleValue()))
        );
    }
}
