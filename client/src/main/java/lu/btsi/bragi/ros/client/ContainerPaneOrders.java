package lu.btsi.bragi.ros.client;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lu.btsi.bragi.ros.client.connection.ConnectionManager;
import lu.btsi.bragi.ros.models.message.*;
import lu.btsi.bragi.ros.models.pojos.Order;

import java.util.List;

/**
 * Created by gillesbraun on 08/03/2017.
 */
public class ContainerPaneOrders extends ScrollPane {
    private Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> refreshOrders()), new KeyFrame(Duration.seconds(15)));

    private ProgressIndicator progressOrders = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);

    private VBox vbox = new VBox(7);

    ContainerPaneOrders() {
        setMinWidth(vbox.getPrefWidth());
        setHbarPolicy(ScrollBarPolicy.NEVER);

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        setStyle("-fx-background-color:transparent;");
        setFitToWidth(true);
        setContent(progressOrders);
    }

    void refreshOrders() {
        if(!ConnectionManager.isConnected())
            return;
        ConnectionManager.getInstance().sendWithAction(new MessageGetQuery<>(Order.class, new Query(QueryType.Open_Orders)), m -> {
            try {
                List<Order> ordersToday = new Message<Order>(m).getPayload();
                vbox.getChildren().clear();
                ordersToday.forEach(order -> {
                    vbox.getChildren().add(new SingleOrderPanel(order));
                    // add separator if not last element
                    if(ordersToday.indexOf(order) != ordersToday.size() - 1)
                        vbox.getChildren().add(new Separator(Orientation.HORIZONTAL));
                });
                if(ordersToday.size() == 0) {
                    Label label = new Label("No orders placed today.");
                    label.setPadding(new Insets(14));
                    vbox.getChildren().add(label);
                }
                setContent(vbox);
            } catch (MessageException e) {
                e.printStackTrace();
            }
        });
    }
}
