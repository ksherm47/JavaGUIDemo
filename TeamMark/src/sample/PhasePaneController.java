package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.*;

public class PhasePaneController {

    @FXML private Button btn_execute;
    @FXML private Button btn_stop;
    @FXML private Button btn_save;
    @FXML private Button btn_load;
    @FXML private Button btn_pin1;
    @FXML private Button btn_pin2;
    @FXML private Button btn_pin3;
    @FXML private Button btn_pin4;
    @FXML private Button btn_pin5;
    @FXML private Button btn_pin6;
    @FXML private Button btn_pin7;
    @FXML private Button btn_pin8;
    @FXML private Button btn_pin9;
    @FXML private Button btn_pin10;
    @FXML private Button btn_pin11;
    @FXML private Button btn_pin12;
    @FXML private Button btn_pin13;
    @FXML private Button btn_pin14;
    @FXML private Button btn_pin15;
    @FXML private Button btn_pin16;

    private Button[] pinButtons = { btn_pin1, btn_pin2, btn_pin3, btn_pin4, btn_pin5, btn_pin6, btn_pin7, btn_pin8,
                                    btn_pin9, btn_pin10, btn_pin11, btn_pin12, btn_pin13, btn_pin14, btn_pin15, btn_pin16};
    
    private boolean[] pinStatuses = new boolean[16];
    private PhasePaneConfiguration currentConfig;

    //changes color of button on press
    public void colorChange(Event evt) {

        String buttonID = ((Button)evt.getSource()).getId();
        //This assumes all pin button ids begin with "btn_pin" and is immediately followed by the button number
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

    //disables pins and execute buttons
    //enables stop button
    public void executePhase() {
        btn_stop.setDisable(false);
        btn_execute.setDisable(true);
        for(Button b : pinButtons) {
            b.setDisable(true);
        }

        // TODO call C script with activated pins here

    }

    //disables stop button
    //enables pins and execute button
    public void stopPhase() {
        btn_stop.setDisable(true);
        btn_execute.setDisable(false);
        for(Button b : pinButtons) {
            b.setDisable(false);
        }
        // TODO Tell C script to stop execution
    }

    public void saveConfiguration() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Phase Configuration");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PCFG","*.pcfg"));
        File fileToSave = chooser.showSaveDialog(btn_save.getScene().getWindow());

        if(!fileToSave.getName().contains(".pcgf")) {
            fileToSave.renameTo(new File(fileToSave.getName() + ".pcgf"));
        }

        PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
        String password = getPasswordDialog();

        if (password == "") {
            // TODO Do we want to allow empty passwords? I think we should.
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
            // TODO ask user to enter pin for the loaded config, if correct:
            currentConfig = (PhasePaneConfiguration) ois.readObject();
            // TODO if not correct, show error message and let them try again

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPasswordDialog() {
        // open password dialog
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("save_pass_pane.fxml"));
        try {
            loader.load();
        }
        catch(Exception e) {
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        return ((PromptController)loader.getController()).getPassword();

        // TODO needs to pause execution until a password is returned from the form
    }
}

