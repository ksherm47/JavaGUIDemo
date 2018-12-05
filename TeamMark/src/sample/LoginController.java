package sample;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class LoginController {
    @FXML TextField textField_userName;
    @FXML TextField textField_password;
    @FXML Label label_error;
    @FXML Button button_login;

    private enum ReturnCode {USER_DOES_NOT_EXIST,
        USER_NOT_ACTIVATED,
        INCORRECT_PASSWORD,
        PASSWORD_NOT_SET,
        NO_DB_CONNECTION,
        INTERNAL_ERROR,
        GOOD}

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
        } else {
            //INSERT CODE TO CONNECT TO DATABASE TO VALIDATE USERNAME AND PASSWORD
            //IF SUCCESSFUL CALL
            ReturnCode rc = validateLogin(textField_userName.getText(), textField_password.getText());
            if(rc == ReturnCode.GOOD) {
                login();
            } else if(rc == ReturnCode.USER_DOES_NOT_EXIST) {
                label_error.setText("Username not recognized");
            } else if(rc == ReturnCode.USER_NOT_ACTIVATED) {
                label_error.setText("User is not activated");
            } else if(rc == ReturnCode.INCORRECT_PASSWORD) {
                label_error.setText("Incorrect password");
            } else if(rc == ReturnCode.PASSWORD_NOT_SET) {
                label_error.setText("Password not set");
            } else if(rc == ReturnCode.NO_DB_CONNECTION) {
                label_error.setText("Could not connect to database.");
            } else if(rc == ReturnCode.INTERNAL_ERROR) {
                label_error.setText("Internal error occured.");
            }
        }
    }

    //clears text fields
    public void clear() {
        //empty text field userName
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

    private ReturnCode validateLogin(String username, String attemptedPassword) {
        //initialize connection variables
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
            return ReturnCode.NO_DB_CONNECTION;
        }

        if(connection != null) {
            System.out.println("Successfully created connection to database.");

            //perform SQL queries
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT UI_PASSWORD, IS_ACTIVATED " +
                                "FROM AUTH_USER JOIN PROFILE " +
                                "ON ID=USER_ID " +
                                "WHERE USERNAME = ?;");
                preparedStatement.setString(1, username);
                ResultSet results = preparedStatement.executeQuery();

                //YOU CAN USE THE LOOP BELOW TO SHOW THE FULL CONTENTS OF ANY ARBITRARY QUERY
//                ResultSet results = statement.executeQuery("SELECT * FROM PROFILE JOIN AUTH_USER ON ID=USER_ID;");
//                ResultSetMetaData rsmd = results.getMetaData();
//                int columnsNumber = rsmd.getColumnCount();
//                while (results.next()) {
//                    for (int i = 1; i <= columnsNumber; i++) {
//                        if (i > 1) System.out.print(",  ");
//                        String columnValue = results.getString(i);
//                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
//                    }
//                    System.out.println("");
//                }
                if(!results.next()) {
                    return ReturnCode.USER_DOES_NOT_EXIST;
                }

                if(!results.getString("IS_ACTIVATED").equals("1")) {
                    return ReturnCode.USER_NOT_ACTIVATED;
                }

                if(results.getString("UI_PASSWORD").equals("")) {
                    return ReturnCode.PASSWORD_NOT_SET;
                }

                String[] passwordFields = results.getString("UI_PASSWORD").split("\\$");
                int num_iterations = Integer.parseInt(passwordFields[1]);
                byte[] salt = passwordFields[2].getBytes(Charset.forName("UTF-8")),
                        passwordHash = passwordFields[3].getBytes(Charset.forName("UTF-8"));

                KeySpec ks = new PBEKeySpec(attemptedPassword.toCharArray(), salt, num_iterations, 256);
                byte[] encryptedAttempt = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(ks).getEncoded();
                byte[] encryptedAttemptBase64 = Base64.getEncoder().encode(encryptedAttempt);
                String s1 = new String(encryptedAttemptBase64);
                return s1.equals(new String(passwordHash)) ? ReturnCode.GOOD : ReturnCode.INCORRECT_PASSWORD;
            }
            catch (SQLException ex)  {
                System.out.println("Encountered an error when executing given sql statement. " + ex);
                return ReturnCode.INTERNAL_ERROR;
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                ex.printStackTrace();
                return ReturnCode.INTERNAL_ERROR;
            }
        }
        else {
            System.out.println("Failed to create connection to database.");
            return ReturnCode.NO_DB_CONNECTION;
        }
    }
}
