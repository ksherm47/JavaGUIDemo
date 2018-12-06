package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PromptController {
    private String password;
    @FXML private TextField txt_pass;

    public void save() {
        password = txt_pass.getText();
        Stage closeStage = (Stage)txt_pass.getScene().getWindow();
        closeStage.close();
    }

    public String getPassword() {
        return password;
    }
}
