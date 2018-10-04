package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PhasePaneController {


    @FXML private Button btn_execute;
    @FXML private Button btn_stop;
    @FXML private Button btn_save;
    @FXML private Button btn_load;
    private boolean[] pinStatuses = new boolean[16];
    private PhasePaneConfiguration currentConfig;

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

    public void saveConfiguration() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Phase Configuration");
        File fileToSave = chooser.showSaveDialog(btn_save.getScene().getWindow());  // TODO error here
        if(!fileToSave.renameTo(new File(fileToSave.getName() + ".pcfg"))) {
            throw new IOException("File could not be successfully saved.");
        }

        PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
        String password = getPasswordDialog();

        if (password == "") {
            // TODO create error message
        }

        try {
            cfg.savePassword(password);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave));
            oos.writeObject(cfg);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfiguration() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PHASE CFG FILES (*.pcfg)", "*.pcfg"));
        chooser.setTitle("Load Phase Configuration");
        File fileToLoad = chooser.showOpenDialog(btn_load.getScene().getWindow());
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToLoad));
            currentConfig = (PhasePaneConfiguration) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPasswordDialog() {
        // open password dialog
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("save_pass_pane.fxml"));
        try {
            Loader.load();
        }
        catch(Exception e) {
        }
        Parent root = Loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        return "";
    }
}
