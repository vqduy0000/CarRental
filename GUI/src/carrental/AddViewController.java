/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import db.DBconnector;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.sql.*;

/**
 * FXML Controller class
 *
 * @author Basel, Duy, Jacob, Ismail
 */
public class AddViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField brandTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField colorTextField;
    @FXML
    private TextField drivetypeTextField;
    @FXML
    private TextField mpgTextField;
    @FXML
    private TextField bodystyleTextField;
    @FXML
    private TextField transTextField;
    @FXML
    private TextField engineTextField;
    @FXML
    private TextField trimTextField;
    @FXML
    private TextField milesTextField;
    @FXML
    private TextField rentalcostTextField;
    @FXML
    private TextField conditionTextField;
   
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        DBconnector.connect();
    }

    @FXML
    private void btnClick(ActionEvent event) throws IOException{

        /* The code chunk below is creating strings and integers from the 
         * texfields that are displayed on the add screen, this converts strings
         * into ints where needed.
        */
    String brandCar = brandTextField.getText();
    int year = Integer.parseInt(yearTextField.getText());
    String color = colorTextField.getText();
    String drivetype = drivetypeTextField.getText();
    int mpg = Integer.parseInt(mpgTextField.getText());
    String bodystyle = bodystyleTextField.getText();
    String transmission = transTextField.getText();
    String engine = engineTextField.getText();
    String trim = trimTextField.getText();
    int mileage = Integer.parseInt(milesTextField.getText());
    int rentalCost = Integer.parseInt(rentalcostTextField.getText());
    String conditionCar = conditionTextField.getText();
    int office_idd = 01;
    int carAvab = 0;

    try{

        //Creates a connection for the database to be reached and confirms that it was successful.
        Connection addViewConnect = DBconnector.connect();
        System.out.println("Connection Successful");

        //Creates a SQL statement field and adds the connection, this allows the statement to be used when the statement is ready to be sent to the database and executed.
        Statement stmt = addViewConnect.createStatement();
        
        //The string below is used to create a query for mySQL using the variables from above. This allows for editing of the string indepently within this method.
        String queryInsert = "insert into CAR (OFFICE_ID, CAR_BRAND, CAR_YEAR, CAR_COLOR, CAR_FUEL_EFFICIENCY, CAR_BODYSTYLE, CAR_DRIVETYPE, CAR_TRANSMISSION, CAR_ENGINE, CAR_TRIM, CAR_MILEAGE, CAR_RENT, CAR_CONDITION, CAR_AVAILABILITY, PERSON_ID) values " + 
            "(" + "'" + office_idd + "'" + "," + "'" + brandCar + "'" + "," + "'" + year +  "'" + "," + "'" + color + 
                 "'" + "," +  "'" + mpg  + "'" + "," +"'" + bodystyle + "'" + "," + "'"  + drivetype + "'" + "," + "'" + transmission + "'" + "," + "'" + engine + "'" + "," + 
                    "'" + trim + "'" + "," +  "'" + mileage  + "'" + "," + "'" + rentalCost + "'" + "," + "'" + conditionCar + "'" + "," + 
                        "'" + carAvab + "'" + "," + null + ");";

            //This sends the statement that was created earlier and executes in into mySQL with the string above as the parameter. This allows the query to be inserted and updated into the table.
            stmt.executeUpdate(queryInsert);

        System.out.println("Insertion executed sucessfully.....");
        
        //Below will catch the errors and output them onto the terminal for troubleshooting any errors that may occur.
    }catch (SQLException exception){
        exception.printStackTrace();
    }catch(Exception exception){
        exception.printStackTrace();
    }


    }
    
}

