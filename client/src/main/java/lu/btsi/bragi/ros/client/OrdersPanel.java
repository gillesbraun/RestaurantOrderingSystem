package lu.btsi.bragi.ros.client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lu.btsi.bragi.ros.client.connection.Client;
import lu.btsi.bragi.ros.models.message.Message;
import lu.btsi.bragi.ros.models.message.MessageException;
import lu.btsi.bragi.ros.models.message.MessageGet;
import lu.btsi.bragi.ros.models.pojos.Order;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by gillesbraun on 08/03/2017.
 */
public class OrdersPanel extends ScrollPane {
    private Client client;

    private Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, this::refreshOrders), new KeyFrame(Duration.seconds(15)));

    private ProgressIndicator progressOrders = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

    private VBox vbox = new VBox(7);

    OrdersPanel(Client client) {
        this.client = client;
        setPadding(new Insets(14));
        setMinWidth(USE_COMPUTED_SIZE);
        vbox.setSpacing(14);

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        setStyle("-fx-background-color:transparent;");
        setFitToWidth(true);
        setContent(progressOrders);
    }

    private void refreshOrders(ActionEvent event) {
        if(client == null)
            return;
        client.sendWithAction(new MessageGet<>(Order.class), m -> {
            try {
                List<Order> payload = new Message<Order>(m).getPayload();
                vbox.getChildren().clear();
                List<Order> ordersToday = payload.stream()
                        .sorted((o1, o2) -> o2.getCreatedAt().toLocalDateTime().compareTo(o1.getCreatedAt().toLocalDateTime()))
                        .filter(o -> o.getCreatedAt().toLocalDateTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
                        .collect(toList());
                ordersToday.forEach(order -> {
                    vbox.getChildren().add(new SingleOrderPanel(client, order));
                    // add separator if not last element
                    if(ordersToday.indexOf(order) != ordersToday.size() - 1)
                        vbox.getChildren().add(new Separator(Orientation.HORIZONTAL));
                });
                if(ordersToday.size() == 0) {
                    getChildren().add(new Label("No orders placed today."));
                }
                setContent(vbox);
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }
}
