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
import java.util.ResourceBundle;

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
    
    Connection c = DBconnector.connect();

    private String customerID;
    private String selectedItem;
    //main sql query which would generate table for customer
    private String sql = "select CAR_BRAND as Brand, CAR_YEAR as Year, CAR_COLOR as Color, " +
                                "CAR_FUEL_EFFICIENCY as \"Fuel Eff\", CAR_BODYSTYLE as Bodystyle, CAR_TRANSMISSION as Transmission, "+
                                "CAR_ENGINE as Engine, CAR_TRIM as Trim, CAR_RENT as Cost " +
                            "from CAR " +
                            "where CAR_AVAILABILITY = 1";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumn();
        refreshTable();
        //this check for user interaction with the table
        tableUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(tableUser.getSelectionModel().getSelectedItem().get(0));
            }
        });
    }

    //take customer ID from StartScreen
    public void initData(String customerID) {
        this.customerID = customerID;
        lblID.setText("Customer # " + (Integer.valueOf(customerID) + 10000));
        //query to check where customer ID is in the CAR table. Which indicate where they have already reserve a car
        String check = "select count(PERSON_ID) from CAR where PERSON_ID  = " + customerID;

        //disable/enable difference functionality base on whether the customer already reserve a car or not
        try{
            ResultSet rs = c.createStatement().executeQuery(check);

            while(rs.next()){
                if ((rs.getInt(1)) == 1){   //if already reserved a car
                    tableUser.setDisable(true);     //disable the table
                    btnReserve.setDisable(true);    //disable reserve button
                    lblWarning.setText("You have already reserve a car"); //warning message
                }
                else    //if not
                    btnCancel.setDisable(true);     //disable cancellation button
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //log out button implementation
    public void logout (ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }     
    
    //refresh data inside the table
    public void refreshTable(){
        data = FXCollections.observableArrayList();
        try{
            ResultSet rs = c.createStatement().executeQuery(sql);
            
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
            ResultSet rs = c.createStatement().executeQuery(sql);
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;  
                //create column and get name from query result set              
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

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
}
