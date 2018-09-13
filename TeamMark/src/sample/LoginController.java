package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginController {
    //client side validation.
    //ensure username and password is not empty
    @FXML TextField textField_userName;
    @FXML TextField textField_password;
    @FXML Label label_error;
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

    //closes window on successful login and opens main ui window
    @FXML Button button_login;
    private void login() {
        //opens main ui window
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ui.fxml"));
            Stage stage = new Stage();
            stage.setTitle("UI");
            stage.setScene(new Scene(root, 250, 250));
            stage.show();
            //closes current window
            Stage closeStage = (Stage) button_login.getScene().getWindow();
            closeStage.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
