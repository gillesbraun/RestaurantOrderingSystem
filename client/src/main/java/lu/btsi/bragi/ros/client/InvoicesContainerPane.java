package lu.btsi.bragi.ros.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Order;
import lu.btsi.bragi.ros.models.pojos.Table;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by gillesbraun on 09/03/2017.
 */
class InvoicesContainerPane extends ScrollPane {
    private Client client;
    private VBox content = new VBox();
    private ProgressIndicator progressLoading = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

    InvoicesContainerPane(Client client) {
        this.client = client;
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setStyle("-fx-background-color:transparent;");
        content.setFillWidth(true);
        loadInvoices();
        setFitToWidth(true);
        content.setFillWidth(true);

        setContent(content);
    }

    private void loadInvoices() {
        content.getChildren().add(progressLoading);
        client.sendWithAction(new MessageGet<>(Order.class), message -> {
            try {
                List<Order> payload = new Message<Order>(message).getPayload();
                Map<Table, List<Order>> unpaidOrders = payload.stream()
                        .filter(order -> order.getInvoiceId() == null)
                        .collect(Collectors.groupingBy(
                                Order::getTable, Collectors.toList()
                        ));
                Platform.runLater(() -> {
                    content.getChildren().remove(progressLoading);
                    unpaidOrders.forEach((table, orders) -> {
                        try {
                            content.getChildren().add(new SingleInvoicePane(table, orders));
                            content.getChildren().add(new Separator(Orientation.HORIZONTAL));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    if(content.getChildren().size() > 0) {
                        // remove the last separator
                        content.getChildren().remove(content.getChildren().size() - 1);
                    } else {
                        Label label = new Label("No open invoices available.");
                        label.setPadding(new Insets(14));
                        getChildren().add(label);
                    }
                });
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }


}
