package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UIController {
    @FXML Label label_username;
    @FXML Button btn_phase1;

    public void openPhase(Event evt) {
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
        //determine title of opened stage
        if(((Button)evt.getSource()).getId().equals("btn_phase1")) {
            stage.setTitle("Phase 1");
        }
        else if (((Button)evt.getSource()).getId().equals("btn_phase2")) {
            stage.setTitle("Phase 2");
        }
        else if (((Button)evt.getSource()).getId().equals("btn_phase3")) {
            stage.setTitle("Phase 3");
        }
        stage.show();
        //closes current window
        Stage closeStage = (Stage) btn_phase1.getScene().getWindow();
        closeStage.close();
    }

    //fills in username label
    public void fillUsername (String username) {
        label_username.setText("Welcome: " + username);
    }
}
