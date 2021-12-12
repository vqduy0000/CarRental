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
        //btnDebug.setVisible(false);
    }

    //When the user presses signup, this method will take them to the signup screen
    //where they can create their username and password and enter their personal information
    //to register and create an account in the Car Rental Management System.
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

    //After a customer logs in, this method will take them into the user screen where they can check and rent
    //from the availble cars for rent.
    public void openUserView (ActionEvent event, String customerID) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserView.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        UserViewController controller = loader.getController();
        controller.initData(customerID); //send data to the next controller
        stage.show();
    }

    //After an employee logs in, this method will take them into the employee screen where they can check and manage
    //the car inventory and operations.
    public void openEmployeeView (ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EmpView.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //This method is linked to the login button in the start screen, and it performs user authentication as
    //it is used for user validation through log-in operations.
    //It will check if the username and password combination entered matches the combination that is stores in the database
    //and if it matches, it will show a the user "Success!"" message and it will direct the user into their respective screen
    //it will take the customer to the user screen through openUserView(), and it will take the employee to the employee screen through openEmployeeView
    //and it does that through checking if the person type for the entered combination of username and password in the database macthes C for customer or E fro employee.
    //Meanwhile, if the entered username and password combination does not match a combination in the DB, a "Login Failed" message will show.
    //On the other hand, if the user leaves the username and password fields empty and presses login, a "Please Enter" message will show next to each of the username and
    //password fields.
    @FXML
    public void loginButtonOnAction(ActionEvent action){
        Connection connection = DBconnector.connect(); 
        String customerID = "0"; //a String value to store the customer ID.
        //This will check if the username and password fields are empty or not after pressing login.
        if(fieldUsername.getText().isBlank() == false && fieldPassword.getText().isBlank() == false) {
            //If we come here, It means the username and password fields were both not empty.
            //Which means that now we got to check if the entered username and password combination match a username and password combination that is stored in the database.
            //verofyUser will store the SQL query that will check if the entered combination matches a combination in the DB
            //as it will return the number of rows that have the same PERSON_USERNAME and PERSON_PASSWORD as the entered in the fieldUsername and fieldPassword.

            //Note: The SELECT command is a READ operation as it allows you to retrieve specific records and READ their values.

        String verifyUser = "SELECT COUNT(1) FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'";
        //userDetail will store the SQL query that gets the PERSON_ID of the entered username and password combination.
        String userDetail = "SELECT PERSON_ID FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'";
        //check for execptions with try...
        try {
            Statement statement = connection.createStatement();
            //Execute the SQL query that is stored in userDetail which will return the PERSON_ID and store the result in the ResultSet customerDetails.
            ResultSet customerDetails = statement.executeQuery(userDetail);
            
            //check while customerDetails include more values...
            while(customerDetails.next())
            {
                //get the value of what is in the ResultSet object (the ID) and get it as a string using getString(1) using the first column index 1.
                customerID = customerDetails.getString(1);
            }
            //now, execute the SQL query in verifyUser which can get a number that means there is a user in the DB that has what macthes the entered username and password combination.
            //and store the result in the ResultSet object result. 
            ResultSet result = statement.executeQuery(verifyUser);
            //now, check while result include more values do the following...
            while(result.next()) {
                //and here finally, we check if the result that we got from the SQL query in verifyUser is what we want.
                //we use getInt(1) to get the value in the ResultSet object result as an integer and check if it equals 1.
                //which if it equals 1, it means that we found a macting record in the DB that has the same username and password combination as the entered username
                //and password combination when logging in.
                if (result.getInt(1) == 1) {
                    //Now, show a "Sucess!" message as we successfully logged in.
                    lblUsername.setText("Success!");
                    lblPassword.setText("Success!");
                    //Now, after guaranteeing we have a valid user, we need to determine if they are a customer or an employee to be able to direct them
                    //to their own specific screens.

                    //Now, personTypeSQL will store the SQL query that will find out the type of the person that logged in, it will find out whether
                    //they are a customer 'C' or an employee 'E' by checking the PERSON_TYPE attribute/column for the valid entered username and passowrd combination in the DB. 
                    String personTypeSQL = "SELECT PERSON_TYPE FROM PERSON WHERE PERSON_USERNAME = '" + fieldUsername.getText() + "' AND PERSON_PASSWORD='" + fieldPassword.getText() + "'";
                    Statement statement2 = connection.createStatement();
                    //Execute the SQL query in personTypeSQL and store the result in personTypeResult.
                    ResultSet personTypeResult = statement2.executeQuery(personTypeSQL);
                    //check while there are values in personTypeResult and do...
                    while(personTypeResult.next()){
                        //get the result in personTypeResult and store it in personType as a string by using getString(1).
                    String personType = personTypeResult.getString(1);
                    //check if the result value in personType is a "C" which means it is a customer, so we would call the openUserView() method which takes us to the user (customer) screen.
                    if("C".equalsIgnoreCase(personType)) {
                        //explained with the method above.
                        openUserView(action, customerID);
                        //else, we will check if the result is "E" which means it is an employee, so we would call the openEmployeeView() method which takes us to the employee screen.
                    } else if("E".equalsIgnoreCase(personType)) {
                        //explained with the method above.
                        openEmployeeView(action);
                    }}
                } else {
                    //if result.getInt(1) != 1, we will get here which means we did not find a matching usnername and password combination in the DB that matches what was entered
                    //so we show the message "Login Failed" and the user can try again.
                    lblUsername.setText("Login Failed");
                    lblPassword.setText("Login Failed");
                }
            }
            //catch execptions, and show them if catched.
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    } else {
        //we will get here if we press the login button and we have empty username and password fields, which means isBlank() returned true which != false which makes
        //the whole condition false and we get here, and as a result we show a message that says "Please enter" referring to the username and password fields, and the user can try again.
        lblUsername.setText("Please enter");
        lblPassword.setText("Please enter");
    }
    }
}
