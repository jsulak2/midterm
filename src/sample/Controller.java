package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;



public class Controller
{
    @FXML
    private ListView<RandomNums> generatedNums;
    @FXML
    private TextField firstNameTextField;

    @FXML
    JFXButton runButton;
    @FXML
    JFXButton loadButton;
    @FXML
    JFXTextField minTextField;
    @FXML
    JFXTextField maxTextField;
    @FXML
    JFXListView materialListView;


    final String hostname = "classdb.cchu0uapco1s.us-east-1.rds.amazonaws.com";
    final String dbName = "ClassDB";
    final String port = "3306";
    final String username = "admin";
    final String password = "password";
    final String AWS_URL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password;

    private void createDatabase(String url)
    {
        try{

            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try
            {
                stmt.execute("CREATE TABLE RandomNums (" +
                        "RandomNum INT(25)");

                System.out.println("TABLE CREATED");
            }
            catch (Exception ex)
            {
                System.out.println("TABLE ALREADY EXISTS, NOT CREATED");
            }
//  CHANGE THESE LINES TO USE THE RANDOM NUMBER
            String randomNum = "Bat";
            String sql = "INSERT INTO RandomNums VALUES" +
                    "('" + randomNum + "')";

            stmt.executeUpdate(sql);

            System.out.println("TABLE FILLED");

            stmt.close();
            conn.close();

        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println(msg);
        }
    }
    private void loadData(String url)
    {
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT RandomNum FROM RandomNums";
            ResultSet result = stmt.executeQuery(sqlStatement);
            ObservableList<RandomNums> dbRandomNumsList = FXCollections.observableArrayList();
            while (result.next())
            {
                RandomNums randomNum = new RandomNums();
                randomNum.randomNum = result.getString("RandomNum");

                dbRandomNumsList.add(randomNum);
            }

            materialListView.setItems(dbRandomNumsList);


            System.out.println("DATA LOADED");
            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println("DATA NOT LOADED");
            System.out.println(msg);
        }
    }
/*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        createawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                createDatabase(AWS_URL);
            }
        });
        createdbbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                createDatabase(DB_URL);
            }
        });
        deleteawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                deleteTable(AWS_URL);
            }
        });
        deletetablebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                deleteTable(DB_URL);
            }
        });

        loadawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                loadData(AWS_URL);
            }
        });
        loaddatabutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                loadData(DB_URL);
            }
        });

        employeeListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Worker> ov, Worker old_val, Worker new_val) -> {
            Worker selectedItem = employeeListView.getSelectionModel().getSelectedItem();
            firstNameTextField.setText(((Employee)selectedItem).firstName);
            isActiveCheckBox.setSelected(((Employee) selectedItem).isActive);

        });


        ObservableList<Worker> items = employeeListView.getItems();
        Employee employee1 = new Employee();
        employee1.firstName = "Alyssa";
        employee1.lastName = "Anderson";
        Employee employee2 = new Employee();
        employee2.firstName = "Robert";
        employee2.lastName = "Smith";

        items.add(employee1);
        items.add(employee2);

        for(int i = 0; i < 10; i++)
        {
            Employee employee = new Employee();
            employee.firstName = "EMPLOYEE" + i;
            employee.lastName = "Incognito";
            employee.isActive = true;
            employee.id = UUID.randomUUID();
            items.add(employee);
        }

        Staff staff1 = new Staff();
        staff1.firstName = "Some Staff";
        staff1.lastName = "Lee";
        items.add(staff1);

        Faculty faculty1 = new Faculty();
        faculty1.firstName = "Some Faculty";
        faculty1.lastName = "Diaz";
        items.add(faculty1);



    }
*/

}
