package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    
    private boolean[] pinStatuses = new boolean[16];
    private PhasePaneConfiguration currentConfig;
    private PromptController promptController = new PromptController();

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

    //disables pins and execute buttons
    //enables stop button
    public void executePhase() {
        btn_stop.setDisable(false);
        btn_execute.setDisable(true);
        btn_pin1.setDisable(true);
        btn_pin2.setDisable(true);
        btn_pin3.setDisable(true);
        btn_pin4.setDisable(true);
        btn_pin5.setDisable(true);
        btn_pin6.setDisable(true);
        btn_pin7.setDisable(true);
        btn_pin8.setDisable(true);
        btn_pin9.setDisable(true);
        btn_pin10.setDisable(true);
        btn_pin11.setDisable(true);
        btn_pin12.setDisable(true);
        btn_pin13.setDisable(true);
        btn_pin14.setDisable(true);
        btn_pin15.setDisable(true);
        btn_pin16.setDisable(true);
    }

    //disables stop button
    //enables pins and execute butotn
    public void stopPhase() {
        btn_stop.setDisable(true);
        btn_execute.setDisable(false);
        btn_pin1.setDisable(false);
        btn_pin2.setDisable(false);
        btn_pin3.setDisable(false);
        btn_pin4.setDisable(false);
        btn_pin5.setDisable(false);
        btn_pin6.setDisable(false);
        btn_pin7.setDisable(false);
        btn_pin8.setDisable(false);
        btn_pin9.setDisable(false);
        btn_pin10.setDisable(false);
        btn_pin11.setDisable(false);
        btn_pin12.setDisable(false);
        btn_pin13.setDisable(false);
        btn_pin14.setDisable(false);
        btn_pin15.setDisable(false);
        btn_pin16.setDisable(false);
    }

    public void saveConfiguration() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Phase Configuration");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PCFG","*.pcfg"));
        chooser.setInitialFileName(".pcfg");
        File fileToSave = chooser.showSaveDialog(btn_save.getScene().getWindow());

        if(!fileToSave.getName().contains(".pcgf")) {
            fileToSave.renameTo(new File(fileToSave.getName() + ".pcgf"));
        }

        PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
        promptController.showPrompt();
        String password = promptController.getPassword();

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

        // TODO needs to pause execution until a password is returned from the form

        return "";
    }
}

