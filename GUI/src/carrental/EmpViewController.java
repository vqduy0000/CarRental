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
import java.sql.SQLException;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;
import db.DBconnector;

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
    private Label lblChosen;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txtMileage;
    @FXML
    private TextField txtCondition;

    @FXML
    private TableView<ObservableList> tableEmp;
    
    Connection c = DBconnector.connect();

    private String selectedItem;
    private String currentCon;
    private String currentMile;
    private String sql = "select CAR_ID as ID, CAR_BRAND as Brand, CAR_YEAR as Year, " +
                                "CAR_MILEAGE as Mileage, CAR_CONDITION as 'Condition', " + 
                                "CAR_AVAILABILITY as Availability, " +
                                "concat(PERSON.PERSON_LNAME, \" \", PERSON.PERSON_FNAME) as 'Rented-to', " +
                                "OFFICE.OFFICE_STATE as '  Location  ' " +
                            "from CAR " +
                            "left join PERSON " +
                            "on CAR.PERSON_ID = PERSON.PERSON_ID " +
                            "join OFFICE " +
                            "on OFFICE.OFFICE_ID = CAR.OFFICE_ID " +
                            "order by ID asc";

                            @FXML
                            private void openAddscreenas(ActionEvent event) throws IOException{
                                Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddView.fxml"));
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.show(); 
                            }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumn();
        refreshTable();

        //set up listener to get id associated with select car so further operation can be perform
        //this id is store in selectedItem and update whenever a new input is detected
        //also enable button related to data manipulation
        tableEmp.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = (String) tableEmp.getSelectionModel().getSelectedItem().get(0);
                currentCon = (String) tableEmp.getSelectionModel().getSelectedItem().get(4);
                currentMile = (String) tableEmp.getSelectionModel().getSelectedItem().get(3);
                lblChosen.setText(selectedItem);
                btnRemove.setDisable(false);
                btnUpdate.setDisable(false);
                System.out.println(tableEmp.getSelectionModel().getSelectedItem().get(0));
            }
        });
    } 
    
    //open addView screen
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
    @FXML
    private void btnRemoveAction(ActionEvent event)throws IOException{
        try {
            Connection c = DBconnector.connect();
            System.out.println("Connection Successfull");
            String query = "DELETE FROM CAR WHERE CAR_ID = ?";
            PreparedStatement stmt = c.prepareStatement(query);
           stmt.setString(1, selectedItem);
           int rowCount = stmt.executeUpdate();
           System.out.println("Deletion sucessful");
           refreshTable();
            } catch (SQLException  e) {
            e.printStackTrace();
            }catch(Exception exception){
            exception.printStackTrace();
                   }
    }

    @FXML
    private void btnUpdateAction(ActionEvent event)throws IOException{
        
        String mileage = txtMileage.getText();
        try{
        Connection c = DBconnector.connect();
        System.out.println("Connection Successfull");

        PreparedStatement stmtMile = c.prepareStatement("update CAR set CAR_MILEAGE = ? where CAR_ID = ?");
        PreparedStatement stmtCon = c.prepareStatement("update CAR set CAR_CONDITION = ?, CAR_AVAILABILITY = ? where CAR_ID = ?");
        if (mileage != "" && Integer.valueOf(mileage) > Integer.valueOf(currentMile)){
            stmtMile.setString(1, mileage);
            stmtMile.setString(2, selectedItem);
            stmtMile.executeUpdate();
         }
        if (!(txtCondition.getText().equals(currentCon)) && !(txtCondition.getText().equals(""))){
            stmtCon.setString(1, txtCondition.getText());
            stmtCon.setString(3, selectedItem);
            if(txtCondition.getText().equals("G"))
                stmtCon.setString(2, "1");
            else
                stmtCon.setString(2, "0");
            stmtCon.executeUpdate();
        }
        //c.createStatement().executeUpdate(query);
        refreshTable();
       
     }catch (SQLException  e) {
        e.printStackTrace();
        }catch(Exception exception){
        exception.printStackTrace();

    }


}
}
