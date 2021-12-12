/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.lang.model.util.ElementScanner14;

import javafx.collections.FXCollections;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import db.DBconnector;

/**
 * FXML Controller class
 *
 * @author Basel, Duy, Jacob, Ismail
 */
public class UserViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ObservableList<ObservableList> data;
    @FXML
    private Label lblID;
    @FXML
    private Label lblWarning;
    @FXML
    private Button btnReserve;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogout;
    @FXML
    private TableView<ObservableList> tableUser;
    @FXML  
    private ChoiceBox cb;
    
    Connection c = DBconnector.connect();

    private String customerID;
    private String officeID;
    private String carID;
    //main sql query which would generate table for customer
    private String sql = "select CAR_ID as ID, CAR_BRAND as Brand, CAR_YEAR as Year, CAR_COLOR as Color, " +
                                "CAR_FUEL_EFFICIENCY as \"Fuel Eff\", CAR_BODYSTYLE as Bodystyle, CAR_TRANSMISSION as Transmission, "+
                                "CAR_ENGINE as Engine, CAR_TRIM as Trim, CAR_RENT as Cost " +
                            "from CAR " +
                            "where CAR_AVAILABILITY = 1 and OFFICE_ID = ?";
    private String officeQuery = "select OFFICE_STREET, OFFICE_CITY, OFFICE_STATE from OFFICE";
    private PreparedStatement mainSelect;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createChoiceBox();
        //create a listener to when user pick any office location
        cb.setOnAction((event) -> {
            int selectedIndex = cb.getSelectionModel().getSelectedIndex(); //get the index of the office
            officeID = "" + (selectedIndex + 1);                           //since index start at 0, +1 would result in the office id
            try{
                mainSelect = c.prepareStatement(sql);                      //create prep statement with sql
                mainSelect.setString(1, "" + (selectedIndex + 1));         //insert the office id into the sql string
            }catch (Exception e){
                e.printStackTrace();
            }
            setColumn();
            refreshTable();
            insertUser();
            System.out.println("   ChoiceBox.getValue(): " + cb.getValue());
        });
        //this check for user interaction with the table
        tableUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                carID = (String)(tableUser.getSelectionModel().getSelectedItem().get(0));
                btnReserve.setDisable(false); //enable reservation option when a row is selected
            }
        });
    }

    //take customer initData is called whenever a customer login in and transfer their ID to this controller
    public void initData(String customerID) {
        this.customerID = customerID;
        lblID.setText("Customer # " + (Integer.valueOf(customerID) + 10000)); //make the number more fancy
        checkUser();
    }
    
    //log out button implementation
    public void logout (ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /*
        implementation of reservation
            when button is pressed, create a sql statement which update car table
                set the person_id (renter) as customerID
                set car availability to 0 so no one else can reserve the car
    */
    @FXML
    public void reservation (ActionEvent event) throws IOException{
        try{
            PreparedStatement stmt = c.prepareStatement("update CAR set PERSON_ID = ?, CAR_AVAILABILITY = 0 where CAR_ID = ?");
            stmt.setString(1, customerID);
            stmt.setString(2, carID);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        checkUser();
        refreshTable();
    }  

    /*
        implementation of cancellation
            when button is pressed, create a sql statement which update car table
                set the person_id (renter) to null
                set car availability to 1 so the car can be reserve by other or the same customer
    */
    @FXML
    public void cancellation (ActionEvent event) throws IOException{
        try{
            PreparedStatement stmt = c.prepareStatement("update CAR set PERSON_ID = null, CAR_AVAILABILITY = 1 where PERSON_ID = ?");
            stmt.setString(1, customerID);
            stmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        checkUser();
    }  
    
    //refresh data inside the table
    public void refreshTable(){
        data = FXCollections.observableArrayList();
        try{
            ResultSet rs = mainSelect.executeQuery();
            
            while (rs.next()){  //go through the result set
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i<=rs.getMetaData().getColumnCount(); i++){
                    row.add(rs.getString(i));
                }

                System.out.println("Row added "+ row); //for debugging purpose
                data.add(row);
            }

            tableUser.setItems(data);
            System.out.println("insertion completed");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //set columns base on query
    public void setColumn(){
        try{
            tableUser.getColumns().clear();
            ResultSet rs = mainSelect.executeQuery();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;  
                //create column and get name from query result set 
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                //this if statement check whether the column is ID and dont display it
                if (rs.getMetaData().getColumnName(i + 1).equalsIgnoreCase("ID"))             
                    col.setVisible(false);


                //set of cells inside the tableview
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {    
                        if (param.getValue().get(j) == null) //check whether a value is null --- else it would cause an error
                            return new SimpleObjectProperty("");
                        else                                                                     
                            return new SimpleStringProperty(param.getValue().get(j).toString());    //return the actual value                  
                    }                    
                });  
                tableUser.getColumns().addAll(col);
                System.out.println("Column ["+i+"] "); //print statement for debugging purpose
                }
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    /*
        create a new choice box that contains the office address
        this method run the officeQuery to get the street city and state in this order
        these value is then extracted from the result set and place into a string called choice
            which get added to the ChoiceBox 
    */
    public void createChoiceBox(){
        try{
            ResultSet rs = c.createStatement().executeQuery(officeQuery);
            String choice;
            while(rs.next()){
                choice = rs.getString(1) +", "+ rs.getString(2) + ", " + rs.getString(3);
                cb.getItems().add(choice);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        insertUser() should be call whenever the user choose an office location
            this method check if the customer is already in the customer table
                if not
                    insert a new entry with the customerID and the chosen office
                else do nothing
    */
    public void insertUser(){
        try{
            PreparedStatement stmt = c.prepareStatement("insert into CUSTOMER (PERSON_ID, OFFICE_ID) values (?, ?)");
            String checkInCustomer = "select count(PERSON_ID) from CUSTOMER where PERSON_ID = " + customerID + " and OFFICE_ID = " + officeID;
            ResultSet rs = c.createStatement().executeQuery(checkInCustomer);
            while (rs.next()){
                if (rs.getInt(1) == 1)
                    break;
                else{
                    stmt.setString(1, customerID);
                    stmt.setString(2, officeID);
                    stmt.executeUpdate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        checkUser() must be called whenever the state of user is alter either through cancellation or reservation or when first login
            check if user has rented a car by query to see if the customerID exist in CAR table
            then disable/enable functionality accordingly
    */
    public void checkUser(){
        //query to check where customer ID is in the CAR table. Which indicate where they have already reserve a car
        String checkInCar = "select count(PERSON_ID) from CAR where PERSON_ID  = " + customerID;

        //disable/enable difference functionality base on whether the customer already reserve a car or not
        try{
            ResultSet rs = c.createStatement().executeQuery(checkInCar);

            while(rs.next()){
                if ((rs.getInt(1)) == 1){   //if already reserved a car
                    tableUser.setDisable(true);     //disable the table so no car can be selected which prevent table listener form enabling reserve button
                    btnReserve.setDisable(true);    //disable reserve button
                    btnCancel.setDisable(false);    //enable cancel button
                    cb.setDisable(true);            //disable ebility to choose office to prevent unnecessay operations
                    lblWarning.setText("You have already reserve a car"); //warning message
                }
                else{    //if not
                    btnReserve.setDisable(true);    //disable reservation button by default unless user choose a car
                    btnCancel.setDisable(true);     //disable cancellation button
                    tableUser.setDisable(false);    //enable the table so user can choose car
                    cb.setDisable(false);           //allow for office selection
                    lblWarning.setText("");         //remove waring message if exist
                }    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
