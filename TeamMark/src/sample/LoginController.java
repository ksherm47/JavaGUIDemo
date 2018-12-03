package sample;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class LoginController {
    @FXML TextField textField_userName;
    @FXML TextField textField_password;
    @FXML Label label_error;
    @FXML Button button_login;

    //client side validation.
    //ensure username and password is not empty
    public void validateLogin() {
        //if the text field userName is empty (null) or contains only spaces then reject
        //if the text field password is empty (null) or contains only spaces then reject
        if((textField_userName.getText() == null || textField_userName.getText().trim().isEmpty()) || (textField_password.getText() == null || textField_password.getText().trim().isEmpty())) {
            //empty text field userName
            textField_userName.setText("");
            //empty text field password
            textField_password.setText("");
            //displays error message
            label_error.setText("Please fill out all fields.");
        }
        else {
            //INSERT CODE TO CONNECT TO DATABASE TO VALIDATE USERNAME AND PASSWORD
            //IF SUCCESSFUL CALL
            connectToDatabase();
            login();
        }
    }

    //clears text fields
    public void clear() {
        //empty text field userw2 SCWANWQ1```````Name
        textField_userName.setText("");
        //empty text field password
        textField_password.setText("");
        //displays error message
    }

    //closes window on successful login and opens main ui window
    public void login() {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("ui.fxml"));
        try {
            Loader.load();
        }
        catch(Exception e) {
        }
        //retrieves the UIController
        UIController UIController = Loader.getController();
        //calls fillUsername method from UIController
        UIController.fillUsername(textField_userName.getText());
        Parent root = Loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Phase Select");
        stage.show();
        //closes current window
        Stage closeStage = (Stage) button_login.getScene().getWindow();
        closeStage.close();
    }

    public void connectToDatabase() {
        //initialize connection variables
        String host = "tcp:marksgroup.database.windows.net:1433";
        String database = "Test";
        String user = "serveradmin@marksgroup";
        String password = "MarkTarakaiSucks!";
        String url = String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", host, database, user, password);
        Connection connection = null;

        //check driver is installed
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC driver detected in library path.");
        }
        catch (ClassNotFoundException e) {
            System.out.println("JDBC driver NOT detected in library path. " + e);
        }

        //initialize connection object
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            System.out.println("Failed to create connection to database " + e);
        }

        if(connection != null) {
            System.out.println("Successfully created connection to database.");

            //perform SQL queries
            try {
                Statement statement = connection.createStatement();

                //INSERT QUERY HERE
            }
            catch (SQLException e) {
                System.out.println("Encountered an error when executing given sql statement. " + e);
            }
        }
        else {
            System.out.println("Failed to create connection to database.");
        }

        System.out.println("Execution finished.");
    }
}
