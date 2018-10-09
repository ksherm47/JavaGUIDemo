package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromptController {
    String password;
    @FXML private TextField txt_pass;
    private Stage stage = new Stage();

    void showPrompt() {
        // open password dialog
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("save_pass_pane.fxml"));
        try {
            Loader.load();
        }
        catch(Exception e) {
        }
        Parent root = Loader.getRoot();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    String getPassword() {
        return password;
    }

    public void save() {
        password = txt_pass.getText();
        stage.close();
    }
}
