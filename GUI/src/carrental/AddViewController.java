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
   
    /**
     * Initializes the controller class.
     */
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        DBconnector.connect();
    }

    @FXML
    private void btnClick(ActionEvent event) throws IOException{
         
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
        Connection addViewConnect = DBconnector.connect();
        System.out.println("Connection Successful");
        Statement stmt = addViewConnect.createStatement();
        
        String queryInsert = "insert into CAR (OFFICE_ID, CAR_BRAND, CAR_YEAR, CAR_COLOR, CAR_FUEL_EFFICIENCY, CAR_BODYSTYLE, CAR_DRIVETYPE, CAR_TRANSMISSION, CAR_ENGINE, CAR_TRIM, CAR_MILEAGE, CAR_RENT, CAR_CONDITION, CAR_AVAILABILITY, PERSON_ID) values " + 
            "(" + "'" + office_idd + "'" + "," + "'" + brandCar + "'" + "," + "'" + year +  "'" + "," + "'" + color + 
                 "'" + "," +  "'" + mpg  + "'" + "," +"'" + bodystyle + "'" + "," + "'"  + drivetype + "'" + "," + "'" + transmission + "'" + "," + "'" + engine + "'" + "," + 
                    "'" + trim + "'" + "," +  "'" + mileage  + "'" + "," + "'" + rentalCost + "'" + "," + "'" + conditionCar + "'" + "," + 
                        "'" + carAvab + "'" + "," + null + ");";


                System.out.println(queryInsert);
            stmt.executeUpdate(queryInsert);

        //Keeping the below portion as this is the first way that it worked.     Adding addential comment so it notices a change 
        //String queryInsert = "insert into CAR ( CAR_ID, OFFICE_ID, CAR_BRAND, CAR_YEAR, CAR_COLOR, CAR_FUEL_EFFICIENCY, CAR_BODYSTYLE, CAR_DRIVETYPE, CAR_TRANSMISSION, CAR_ENGINE, CAR_TRIM, CAR_MILEAGE, CAR_RENT, CAR_CONDITION, CAR_AVAILABILITY, PERSON_ID) values " + 
        //  "(" + "'" + car_IDD + "'" + "," + "'" + office_idd + "'" + "," + "'" + brandCar + "'" + "," + "'" + year +  "'" + "," + "'" + color + 
        //     "'" + "," +  "'" + mpg  + "'" + "," +"'" + bodystyle + "'" + "," + "'"  + drivetype + "'" + "," + "'" + transmission + "'" + "," + "'" + engine + "'" + "," + 
        //     "'" + trim + "'" + "," +  "'" + mileage  + "'" + "," + "'" + rentalCost + "'" + "," + "'" + conditionCar + "'" + "," + 
        //     "'" + carAvab + "'" + "," + null + ");";

        System.out.println("Insertion executed sucessfully.....");

    }catch (SQLException exception){
        exception.printStackTrace();
    }catch(Exception exception){
        exception.printStackTrace();
    }


    }
    
}

