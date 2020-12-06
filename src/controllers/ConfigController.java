package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import models.Action;
import objects.ActionList;
import objects.ActionStyle;
import objects.ActionTier;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConfigController  implements Initializable {
    @FXML
    private TableView<Action> table;
    @FXML
    private TableColumn<Action, String> actionName;
    @FXML
    private TableColumn<Action, Integer> pressedKey;
    @FXML
    private TableColumn<Action, Boolean> ctrlPressed;
    @FXML
    private TableColumn<Action, Boolean> shiftPressed;
    @FXML
    private TableColumn<Action, Boolean> altPressed;
    @FXML
    private TableColumn<Action, ActionTier> actionTier;
    @FXML
    private TableColumn<Action, ImageView> actionImage;
    @FXML
    private TableColumn<Action, ActionStyle> actionStyle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actionName.setCellValueFactory(
                new PropertyValueFactory<>("actionName"));
        pressedKey.setCellValueFactory(
                new PropertyValueFactory<>("pressedKey"));
        ctrlPressed.setCellValueFactory(
                new PropertyValueFactory<>("ctrlPressed"));
        shiftPressed.setCellValueFactory(
                new PropertyValueFactory<>("shiftPressed"));
        altPressed.setCellValueFactory(
                new PropertyValueFactory<>("altPressed"));
        actionTier.setCellValueFactory(
                new PropertyValueFactory<>("actionTier"));
        actionImage.setCellValueFactory(
                new PropertyValueFactory<>("actionImage"));
        actionStyle.setCellValueFactory(
                new PropertyValueFactory<>("actionStyle"));

        actionName.setCellFactory(
                TextFieldTableCell.forTableColumn());

        ctrlPressed.setCellFactory(
                CheckBoxTableCell.forTableColumn(ctrlPressed));
        shiftPressed.setCellFactory(
                CheckBoxTableCell.forTableColumn(shiftPressed));
        altPressed.setCellFactory(
                CheckBoxTableCell.forTableColumn(altPressed));

        ObservableList<models.Action> data = FXCollections.observableArrayList();

        table.setItems(data);
    }
}