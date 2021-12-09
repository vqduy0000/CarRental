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

    @FXML
    private TextField zipTextField;

    @FXML
    private TextField areaCodeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailIDTextField;
    @FXML
    private TextField emailDomainTextField;


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
        
        String userName = usernameTextField.getText();
        String password = passwordField.getText(); 
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String birthDate = birthDateTextField.getText();
        String zipCode = zipTextField.getText();
        String areaCode = areaCodeTextField.getText(); 
        String phoneNumber = phoneTextField.getText();
        String emailID = emailIDTextField.getText();
        String emailDomain = emailDomainTextField.getText();

        String insertFields = "INSERT INTO PERSON (PERSON_USERNAME, PERSON_PASSWORD, PERSON_FNAME, PERSON_LNAME, PERSON_DOB, PERSON_ZIPCODE, PERSON_AREACODE, PERSON_PHONE,PERSON_EMAILID,PERSON_EMAILDOMAIN) VALUES ('";
        String insertValues = userName + "','" + password + "','" + firstName + "','" + lastName + "','" + birthDate + "','" + zipCode + "','" + areaCode + "','" + phoneNumber + "','" + emailID + "','" + emailDomain + "')";
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
