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
import javafx.scene.control.*;

import javax.swing.*;
import java.awt.desktop.SystemSleepEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;



public class Controller implements Initializable
{
    @FXML
    private ListView<RandomNums> generatedNums;
    @FXML
    private JFXTextField minTextField;
    @FXML
    private JFXTextField maxTextField;
    @FXML
    Label randNumLabel;

    @FXML
    JFXButton runButton;
    @FXML
    JFXButton loadButton;
    @FXML
    JFXButton delButton;
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
                        "RandomNum VARCHAR(25))");

                System.out.println("TABLE CREATED");
            }
            catch (Exception ex)
            {
                System.out.println("TABLE ALREADY EXISTS, NOT CREATED");
            }

            RandomNums rand = new RandomNums();
            String lo = minTextField.getText();
            String hi = maxTextField.getText();
            int loInt = Integer.parseInt(lo);
            int hiInt = Integer.parseInt(hi);
            rand.generateRandomNum(loInt, hiInt);
            randNumLabel.setText(rand.toString());

            String randomNum = rand.toString();
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
    private void deleteTable(String url)
    {
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE RandomNums");
            stmt.close();
            conn.close();
            System.out.println("TABLE DROPPED");
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println("TABLE NOT DROPPED");
            System.out.println(msg);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                createDatabase(AWS_URL);
            }
        });
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                loadData(AWS_URL);
            }
        });
        delButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                deleteTable(AWS_URL);
            }
        });
    }
}
