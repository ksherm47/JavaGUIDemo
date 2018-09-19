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


    //changes color of button on press
    public void colorChange(Event evt) {
        //changes color of button to green if not already green
        if(((Control)evt.getSource()).getStyle() != "-fx-base: #b6e7c9;") {
            ((Control) evt.getSource()).setStyle("-fx-base: #b6e7c9;");
        }
        //restores default color of button
        else {
            ((Control) evt.getSource()).setStyle("");
        }
    }
}
