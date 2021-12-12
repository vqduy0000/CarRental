/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import db.DBconnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Basel, Duy, Jacob, Ismail
 */
public class StartScreenController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignup;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private TextField fieldUsername;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private Button btnDebug;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBconnector.connect();
    }

    //adding sign-up button functionality from start screen cause same problem with sign-up button.
    @FXML
    private void openSignUpScreen(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show(); 
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/GuiManager.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void openUserView (ActionEvent event, String customerID) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserView.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        UserViewController controller = loader.getController();
        controller.initData(customerID);    
        stage.show();
    }

    public void openEmployeeView (ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/UserView.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void loginButtonOnAction(ActionEvent action){
        Connection connection = DBconnector.connect(); 
        String customerID = "0";
        if(fieldUsername.getText().isBlank() == false && fieldPassword.getText().isBlank() == false) {
        String verifyUser = "SELECT COUNT(1) FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'";
        String userDetail = "SELECT PERSON_ID FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'"; 
        try {
            Statement statement = connection.createStatement();
            ResultSet customerDetails = statement.executeQuery(userDetail);
            
            while(customerDetails.next())
            {
                customerID = customerDetails.getString(1);
            }

            ResultSet result = statement.executeQuery(verifyUser);
            while(result.next()) {
                if (result.getInt(1) == 1) {
                    lblUsername.setText("Success!");
                    lblPassword.setText("Success!");
                    String personTypeSQL = "SELECT PERSON_TYPE FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'";
                    Statement statement2 = connection.createStatement();
                    ResultSet personTypeResult = statement2.executeQuery(personTypeSQL);
                    while(personTypeResult.next()){
                    String personType = personTypeResult.getString(1);
                    if("C".equalsIgnoreCase(personType)) {
                        openUserView(action, customerID);
                    } else if("E".equalsIgnoreCase(personType)) {
                        openEmployeeView(action);
                    }}
                } else {
                    lblUsername.setText("Login Failed");
                    lblPassword.setText("Login Failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    } else {
        lblUsername.setText("Please enter");
        lblPassword.setText("Please enter");
    }
    }
}
