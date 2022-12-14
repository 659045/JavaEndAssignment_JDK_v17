package com.example.javaendassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Database db = new Database();

    ArrayList<User> users = db.getUsers();

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db.readItemsFromFile();
        db.readUsersFromFile();
    }

    public void onButtonLoginClick() {
        try {
            checkLogin(users);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void checkLogin(ArrayList<User> users) throws IOException {
        for (User u: users) {
            System.out.println(u.getType());

            if(Objects.equals(u.getUsername(), txtUsername.getText()) && Objects.equals(u.getPassword(), txtPassword.getText()) && Objects.equals(u.getType(), User.Type.User)){
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 530, 340);
                Stage stage = new Stage();
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();
                MainController controller = fxmlLoader.getController();
                controller.welcomeUser(u);
            }
            else if (Objects.equals("", txtUsername.getText())|| Objects.equals("", txtPassword.getText())){
                lblError.setText("Please fill in all the fields");
            }
            else{
                lblError.setText("Incorrect Login Credentials");
            }
        }
    }
}