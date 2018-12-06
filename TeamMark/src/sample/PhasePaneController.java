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
import java.sql.*;
import java.util.*;

public class PhasePaneController {

    @FXML private Button btn_execute;
    @FXML private Button btn_stop;
    @FXML private Button btn_saveToDisk;
    @FXML private Button btn_loadFromDisk;
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
    private int userID;

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
    public void setPhaseNumber(int p) {
        phaseNumber = p;
    }

    public void setUserID(int id) { userID = id; }

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

    public void saveToDisk() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Phase Configuration");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON FILES","*.json"));
        File fileToSave = chooser.showSaveDialog(btn_saveToDisk.getScene().getWindow());

        if(fileToSave != null) {
            if (!fileToSave.getName().contains(".json")) {
                fileToSave.renameTo(new File(fileToSave.getName() + ".json"));
            }

            PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);

            try {
                Gson gson = new Gson();
                PrintStream ps = new PrintStream(new FileOutputStream(fileToSave));
                ps.print(gson.toJson(cfg));
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToCloud() {
        String host = "marksgroup.database.windows.net:1433";
        String database = "Test";
        String dbuser = "serveradmin@marksgroup";
        String dbpassword = "MarkTarakaiSucks!";
        String url = String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", host, database, dbuser, dbpassword);
        Connection connection = null;

        //check driver is installed
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC driver detected in library path.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver NOT detected in library path. " + e);
        }

        //initialize connection object
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Failed to create connection to database " + e);
        }

        if (connection != null) {
            System.out.println("Successfully created connection to database.");

            PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
            Gson gson = new Gson();
            //perform SQL queries
            try {

                Statement statement = connection.createStatement();
                statement.executeUpdate(
                        "UPDATE PROFILE " +
                             "SET PHASE_" + Integer.toString(phaseNumber) + "_SETTINGS = \'" + gson.toJson(cfg) + "\' " +
                             "WHERE USER_ID = " + Integer.toString(userID) + ";");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Config saved to cloud.");
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadConfiguration() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        chooser.setTitle("Load Phase Configuration");
        File fileToLoad = chooser.showOpenDialog(btn_loadFromDisk.getScene().getWindow());
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

    public void loadFromCloud() {
        String host = "marksgroup.database.windows.net:1433";
        String database = "Test";
        String dbuser = "serveradmin@marksgroup";
        String dbpassword = "MarkTarakaiSucks!";
        String url = String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", host, database, dbuser, dbpassword);
        Connection connection = null;

        //check driver is installed
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC driver detected in library path.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver NOT detected in library path. " + e);
        }

        //initialize connection object
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Failed to create connection to database " + e);
        }

        if (connection != null) {
            System.out.println("Successfully created connection to database.");

            PhasePaneConfiguration cfg = new PhasePaneConfiguration(pinStatuses);
            Gson gson = new Gson();
            //perform SQL queries
            try {
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(
                        "SELECT PHASE_" + Integer.toString(phaseNumber) +"_SETTINGS " +
                        "FROM AUTH_USER JOIN PROFILE " +
                        "ON ID=USER_ID " +
                        "WHERE USER_ID = " + Integer.toString(userID) + ";");

                results.next();
                String result = results.getString("PHASE_" + phaseNumber + "_SETTINGS");
                if(result != null) {
                    String jsonString = result.replace("\"", "");
                    currentConfig = gson.fromJson(jsonString, PhasePaneConfiguration.class);
                    pinStatuses = currentConfig.getPins();
                    List<Button> pinButtons = getPinButtons();
                    for (int i = 0; i < pinStatuses.length; i++) {
                        if (pinStatuses[i]) {
                            pinButtons.get(i).setStyle("-fx-base: #b6e7c9;");
                        } else {
                            pinButtons.get(i).setStyle("");
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

