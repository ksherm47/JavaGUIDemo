package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UIController {

    //@FXML Button phase1_button;
    public void openPhase() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("phase_pane.fxml"));
            Stage phaseStage = new Stage();
            phaseStage.setTitle("Phase Configuration");
            phaseStage.setScene(new Scene(root, 550, 400));
            phaseStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
