package lu.btsi.bragi.ros.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by gillesbraun on 21/03/2017.
 */
public class AboutStage extends Stage {

    public AboutStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AboutStage.fxml"));
        loader.setController(this);
        Parent root =  loader.load();
        setTitle("About ROS");

        setScene(new Scene(root));

    }
}
