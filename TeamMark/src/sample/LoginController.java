package sample;

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
            login();
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
        Parent root = Loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        //closes current window
        Stage closeStage = (Stage) button_login.getScene().getWindow();
        closeStage.close();
    }
}
