package sample;

import javafx.event.Event;
import javafx.scene.control.Control;

public class PhasePaneController {
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
