package sample;

import com.google.gson.Gson;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    private int phaseNumber;
    private boolean superUser = false;

    private Map<Integer, Process> executeProcesses = new HashMap<Integer, Process>(); //Used to call hardware code

    public List<Button> getPinButtons() {
        return new ArrayList<Button>(
                Arrays.asList(btn_pin1, btn_pin2, btn_pin3, btn_pin4,
                        btn_pin5, btn_pin6, btn_pin7, btn_pin8,
                        btn_pin9, btn_pin10, btn_pin11, btn_pin12,
                        btn_pin13, btn_pin14, btn_pin15, btn_pin16));
    }
    
    private boolean[] pinStatuses = new boolean[16];
    private PhasePaneConfiguration currentConfig;

    //populates phaseNumber String variable
    public void getPhaseNumber(int p) {
        phaseNumber = p;
    }

    public void setSuperUser(boolean superUser) {
        if(!superUser) {
            for (Button b : getPinButtons()) {
                b.setDisable(true);
            }
        }
        this.superUser = superUser;
    }

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
        if(superUser) {
            for (Button b : getPinButtons()) {
                b.setDisable(true);
            }
        }

        int pinNumber = 1;
        for (Boolean b : pinStatuses) {
            if (b) {
                callCScript(1, pinNumber);  // TODO Get what phase we're on
            }
            pinNumber = pinNumber + 1;
        }
    }

    //disables stop button
    //enables pins and execute button
    public void stopPhase() {
        btn_execute.setDisable(false);
        btn_stop.setDisable(true);
        if(superUser) {
            for (Button b : getPinButtons()) {
                b.setDisable(false);
            }
        }
        int phase = 1; //TODO get current phase. Right now just testing with phase 1.
        Process process = executeProcesses.get(phase);
        process.destroy();
        // TODO Tell C script to stop execution
    }

    public void saveConfiguration() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Phase Configuration");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON FILES","*.json"));
        File fileToSave = chooser.showSaveDialog(btn_save.getScene().getWindow());

        if(!fileToSave.getName().contains(".json")) {
            fileToSave.renameTo(new File(fileToSave.getName() + ".json"));
        }

        PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
        String password = getPasswordDialog();

        try {
            cfg.savePassword(password);
            Gson gson = new Gson();
            PrintStream ps =  new PrintStream(new FileOutputStream(fileToSave));
            ps.print(gson.toJson(cfg));
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfiguration() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        chooser.setTitle("Load Phase Configuration");
        File fileToLoad = chooser.showOpenDialog(btn_load.getScene().getWindow());
        try {
            Gson gson = new Gson();
            String jsonString = new String(Files.readAllBytes(Paths.get(fileToLoad.getAbsolutePath())));
            currentConfig = gson.fromJson(jsonString, PhasePaneConfiguration.class);
            // TODO ask user to enter pin for the loaded config, if correct:
            pinStatuses = currentConfig.getPins();
            List<Button> pinButtons = getPinButtons();
            for(int i = 0; i < pinStatuses.length; i++) {
                if(pinStatuses[i]) {
                    pinButtons.get(i).setStyle("-fx-base: #b6e7c9;");
                } else {
                    pinButtons.get(i).setStyle("");
                }
            }
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
        } catch(Exception e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        return ((PromptController)loader.getController()).getPassword();

        // TODO needs to pause execution until a password is returned from the form
    }

    private void callCScript(int phase,  int pin) { // TODO - Reference: https://stackoverflow.com/questions/29609790/using-javas-process-builder-to-call-a-c-program
        String pinHexValue = convertPinValueToHex(phase, pin);

        try {
            String c_file = "script.c";
            String output_exe = "activate_pin";
            // Compile the C code   TODO - This shouldn't be done every time, check if the compiled file exists first
//            Process compile = new ProcessBuilder("gcc", "-o " + output_exe,  c_file).start();
//            if (compile.exitValue() == -1) {
//                // TODO - Compile failed
//            }
//            // Call the compiled code with the computed hex value
            Process execute = new ProcessBuilder("./" + output_exe + " " + pinHexValue).start();
//            if (execute.exitValue() == -1) {
//                // TODO - Call failed
//            }
            executeProcesses.put(phase, execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertPinValueToHex(int phase, int pin) {
        StringBuilder value = new StringBuilder();

        switch (phase) {
            case 1:
                if (pin != 16) {
                    value.append("2");
                } else {
                    return "30";
                }
                break;
            case 2:
                if (pin != 16) {
                    value.append("4");
                } else {
                    return "50";
                }
                break;
            case 3:
                if (pin != 16) {
                    value.append("6");
                } else {
                    return "70";
                }
                break;
        }

        if (pin < 10) {
            value.append(pin);
        } else {
            switch (pin) {
                case 10:
                    value.append("A");
                    break;
                case 11:
                    value.append("B");
                    break;
                case 12:
                    value.append("C");
                    break;
                case 13:
                    value.append("D");
                    break;
                case 14:
                    value.append("E");
                    break;
                case 15:
                    value.append("F");
                    break;
            }
        }

        System.out.println(value.toString());
        return value.toString();
    }
}

