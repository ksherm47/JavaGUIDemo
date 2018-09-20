package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UIController {
    @FXML Label label_username;
    public void openPhase() {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("phase_pane.fxml"));
        try {
            Loader.load();
        }
        catch(Exception e) {
        }
        Parent root = Loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //fills in username label
    public void fillUsername (String username) {
        label_username.setText(username);
    }
}
