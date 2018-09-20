package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UIController {
    @FXML Button phase1_button;
    public void openPhase() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("phase_pane.fxml"));
            Stage phaseStage = new Stage();
            phaseStage.setTitle("Phase Configuration");
            phaseStage.setScene(new Scene(root, 640, 480));
            phaseStage.setResizable(false);
            phaseStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
