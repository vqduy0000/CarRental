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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import db.DBconnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Basel, Duy, Jacob, Ismail
 */
public class EmpViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ObservableList<ObservableList> data;
    
    @FXML
    private TableColumn col_ID;
    @FXML
    private TableColumn col_Brand;
    @FXML
    private TableColumn col_Year;
    @FXML
    private TableColumn col_Mileage;
    @FXML
    private TableColumn col_Condition;
    @FXML
    private TableColumn col_Avail;
    @FXML
    private TableColumn col_Renter;

    @FXML
    private Label lblChosen;
    
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;

    @FXML
    private TableView<ObservableList> tableEmp;
    
    Connection c = DBconnector.connect();

    private String selectedItem;
    private String sql = "select CAR_ID as Car, CAR_BRAND as Brand, CAR_YEAR as Year, " +
                                "CAR_MILEAGE as Mileage, CAR_CONDITION as 'Condition', " + 
                                "CAR_AVAILABILITY as Availability, " +
                                "concat(PERSON.PERSON_LNAME, \" \", PERSON.PERSON_FNAME) as 'Rented-to' " +
                            "from CAR " +
                            "left join PERSON " +
                            "on CAR.PERSON_ID = PERSON.PERSON_ID";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumn();
        refreshTable();
        tableEmp.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = (String) tableEmp.getSelectionModel().getSelectedItem().get(0);
                lblChosen.setText(selectedItem);
                btnRemove.setDisable(false);
                btnUpdate.setDisable(false);
                System.out.println(tableEmp.getSelectionModel().getSelectedItem().get(0));
            }
        });
    }    
    @FXML
    private void empopenaddView(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show(); }
        
    public void refreshTable(){
        data = FXCollections.observableArrayList();
        try{
            ResultSet rs = c.createStatement().executeQuery(sql);
            
            while (rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i<=rs.getMetaData().getColumnCount(); i++){
                    row.add(rs.getString(i));
                }

                System.out.println("Row added"+ row);
                data.add(row);
            }

            tableEmp.setItems(data);
            System.out.println("insertion completed");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setColumn (){
        try{
            ResultSet rs = c.createStatement().executeQuery(sql);
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {    
                        if (param.getValue().get(j) == null)
                            return new SimpleObjectProperty("");
                        else                                                                     
                            return new SimpleStringProperty(param.getValue().get(j).toString());                      
                    }                    
                });  
                tableEmp.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
    }
}
