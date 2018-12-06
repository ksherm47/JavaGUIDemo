package sample;

import javafx.event.Event; //Event
import javafx.fxml.FXML; //FXML
import javafx.fxml.FXMLLoader; //Loader
import javafx.scene.control.Button; //Button
import javafx.scene.control.Label; //Label
import javafx.scene.layout.Pane; //Pane
import javafx.scene.layout.AnchorPane; //AnchorPane

public class UIController {
    @FXML Label label_username;
    @FXML Button btn_phase1;
    @FXML Button btn_phase2;
    @FXML Button btn_phase3;
    @FXML AnchorPane phasePane;


    //array to hold panes that hold phase_pane.fxml
    Pane[] phaseArray = new Pane[3];

    //calls initialize when FXML is loaded
    public void initialize(boolean superUser) {
        //fills array up to array length with panes
        for(int i = 0; i < phaseArray.length; i++) {
            //fills array position i with pane from makePane method
            phaseArray[i] = makePane(i+1, superUser);
        }
        //fills phasePane with phase 1 by default
        phasePane.getChildren().add(phaseArray[0]);
        //disables button phase 1
        btn_phase1.setDisable(true);
    }

    //makes and returns a pane
    public Pane makePane(int phaseNumber, boolean superUser) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("phase_pane.fxml"));
        try {
            loader.load();
        } catch(Exception e) {
            e.printStackTrace();
        }
        //retrieves PhasePaneController
        PhasePaneController PhasePaneController = loader.getController();
        //calls getPhaseNumber method to determine phase number
        PhasePaneController.getPhaseNumber(phaseNumber);
        PhasePaneController.setSuperUser(superUser);
        Pane root = loader.getRoot();
        return root;
    }

    //opens a phase depending on button press
    public void openPhase(Event evt) {
        //if button phase1 is pressed, open phase 1 pane
        if(((Button)evt.getSource()).getId().equals("btn_phase1")) {
            //clear pane
            phasePane.getChildren().clear();
            //add phase 1 pane to phasePane
            phasePane.getChildren().add(phaseArray[Integer.parseInt(((Button)evt.getSource()).getId().substring(9)) - 1]);
            //disables phase 1 button
            btn_phase1.setDisable(true);
            //enables phase 2 and 3 button
            btn_phase2.setDisable(false);
            btn_phase3.setDisable(false);
        }
        //if button phase 2 is pressed, open phase 2 pane
        else if (((Button)evt.getSource()).getId().equals("btn_phase2")) {
            //clear pane
            phasePane.getChildren().clear();
            //add phase 2 pane to phasePane
            phasePane.getChildren().add(phaseArray[Integer.parseInt(((Button)evt.getSource()).getId().substring(9)) - 1]);
            //disables phase 2 button
            btn_phase2.setDisable(true);
            //enables phase 1 and 3 button
            btn_phase1.setDisable(false);
            btn_phase3.setDisable(false);
        }
        //if button phase 3 is pressed, open phase 3 pane
        else if (((Button)evt.getSource()).getId().equals("btn_phase3")) {
            //clear pane
            phasePane.getChildren().clear();
            //add phase 3 pane to phasePane
            phasePane.getChildren().add(phaseArray[Integer.parseInt(((Button)evt.getSource()).getId().substring(9)) - 1]);
            //disables phase 3 button
            btn_phase3.setDisable(true);
            //enables phase 1 and 2 button
            btn_phase1.setDisable(false);
            btn_phase2.setDisable(false);
        }
    }

    //fills in username label
    public void fillUsername (String username) {
        label_username.setText("Welcome: " + username);
    }
}
