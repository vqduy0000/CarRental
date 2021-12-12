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
    
    //this submitButtonOnAction() method is triggered when the user presses on the submit button in the signup screen
    //where the user (customer) ceates their username and password and fills in their personal information to create an account
    //in our car rental management system, and as this method is connected to the submit button, when it is pressed, all the filled in data in the forms
    //will be inserted in the database in the PERSON table in their respective columns, so they can be used in logging in and further operations.
    @FXML
    public void submitButtonOnAction(ActionEvent action) {
        Connection connection = DBconnector.connect(); 
        //get the data in the signup form fields and store it in variables.
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

        //insertFields and insertValues will store the SQL query that will be used to insert data into the database (populate the PERSON table) which was entered
        //in the signup form fields.

        //Note: INSERT INTO command is a CREATE operation which allows us to add new rows to the table.

        //insertFields will store the part of the query that has the INSERT INTO command and the table and columns that we are going to insert into.
        //insertValues will store the part of the query that has the values that will be inserted into the specified database table and columns.
        String insertFields = "INSERT INTO PERSON (PERSON_USERNAME, PERSON_PASSWORD, PERSON_FNAME, PERSON_LNAME, PERSON_DOB, PERSON_ZIPCODE, PERSON_AREACODE, PERSON_PHONE,PERSON_EMAILID,PERSON_EMAILDOMAIN) VALUES ('";
        String insertValues = userName + "','" + password + "','" + firstName + "','" + lastName + "','" + birthDate + "','" + zipCode + "','" + areaCode + "','" + phoneNumber + "','" + emailID + "','" + emailDomain + "')";
        //submit will store the full SQL query by concatenating the parts from insertFields and insertValues.
        String submit = insertFields + insertValues;

        try {
            //check for exception with try.
            //create the statement and then execute it.
            Statement statement = connection.createStatement();
            //Execute the SQL query in submit using executeUpdate(), to insert the data into the database.
           statement.executeUpdate(submit);
           //catch execptions, and show them if catched.
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
