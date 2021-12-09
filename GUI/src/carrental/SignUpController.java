/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

import db.DBconnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Basel, Duy, Jacob, Ismail
 */
public class SignUpController implements Initializable {

    @FXML
    private Button btnSubmit;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField birthDateTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    public void submitButtonOnAction(ActionEvent action) {
        Connection connection = DBconnector.connect(); 
        
        String userName = usernameTextField.getText(); //req
        String password = passwordField.getText(); //req
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String birthDate = birthDateTextField.getText();

        String insertFields = "insert into PERSON (PERSON_USERNAME, PERSON_PASSWORD, PERSON_FNAME, PERSON_LNAME, PERSON_DOB, PERSON_TYPE) VALUES ('";
        String insertValues = userName + "','" + password + "','" + firstName + "','" + lastName + "','" + birthDate + "')";
        String submit = insertFields + insertValues;

        try {
            Statement statement = connection.createStatement();
           statement.executeUpdate(submit);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
