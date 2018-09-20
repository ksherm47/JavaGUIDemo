package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PhasePaneController {


    @FXML private Button btn_execute;
    @FXML private Button btn_stop;
    private boolean[] pinStatuses = new boolean[16];

    //changes color of button on press
    public void colorChange(Event evt) {

        String buttonID = ((Button)evt.getSource()).getId();
        //This assumes all pin button ids begin with "btn_pin" and is immediately followed by the putton number
        //If the id is modified in phase_pane.fxml, THIS LINE MUST BE MODIFIED ACCORDINGLY
        int buttonNumber = Integer.parseInt(buttonID.substring(7));


        //if button not selected
        if(!pinStatuses[buttonNumber - 1]) {
            pinStatuses[buttonNumber - 1] = true;
            //changes color of button to green
            ((Control) evt.getSource()).setStyle("-fx-base: #b6e7c9;");
        }
        else {
            pinStatuses[buttonNumber - 1] = false;
            //restores default color of button
            ((Control) evt.getSource()).setStyle("");
        }
    }

    public void executePhase() {
        btn_stop.setDisable(false);
        btn_execute.setDisable(true);
    }

    public void stopPhase() {
        btn_stop.setDisable(true);
        btn_execute.setDisable(false);
    }
}
