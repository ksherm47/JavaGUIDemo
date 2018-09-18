package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

public class UIController {

    //@FXML Button phase1_button;
    @FXML
    public void openPhase(ActionEvent event) {
        try {
            String buttonId = ((Button)event.getSource()).getId();
            String phaseNumber = buttonId.substring(5,6);
            Parent root = FXMLLoader.load(getClass().getResource("phase_pane.fxml"));
            Stage phaseStage = new Stage();
            phaseStage.setTitle("Phase Configuration - " + phaseNumber);
            phaseStage.setScene(new Scene(root, 640, 480));
            phaseStage.setResizable(false);
            phaseStage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
