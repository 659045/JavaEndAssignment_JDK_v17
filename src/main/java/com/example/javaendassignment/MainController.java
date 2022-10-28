package com.example.javaendassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class MainController implements Initializable {
    private final Database db = new Database();

    private Item item;

    @FXML
    private Label lblWelcome, lblLendMsg, lblReceiveMsg, lblAddItemError, lblAddMemberError;

    @FXML
    private TextField txtReceiveID, txtLendID, txtLendMember, txtAddItemTitle, txtAddItemAuthor,
            txtAddMemberUsername, txtAddMemberPassword, txtAddMemberFirstName, txtAddMemberLastName;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Group lendGroup, collectionGroup, addItemGroup, membersGroup, addMemberGroup;

    @FXML
    private TableView<Item> tvItems;

    @FXML
    private TableView<User> tvMembers;

    @FXML
    private TableColumn<Item, Integer> colItemCode;

    @FXML
    private TableColumn<Item, String> colTitle, colAuthor;

    @FXML
    private TableColumn<Item, Boolean> colAvailable;

    @FXML
    private TableColumn<User, Integer> colMemberID;

    @FXML
    private TableColumn<User, String> colFirstName, colLastName;

    @FXML
    private TableColumn<User, LocalDate> colBirthDate;

    //shows the lend tab and loads the table view
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideAllGroups();
        lendGroup.setVisible(true);

        loadTableView();
    }

    //sets welcome user text
    public void welcomeUser(User user){
        lblWelcome.setText(String.format("Welcome %s", user.getFirstName()));
    }

    public void loadTableView(){
        tvItems.getItems().clear();
        tvMembers.getItems().clear();
        setTableViewProperties();
        ObservableList<Item> items = FXCollections.observableArrayList(db.getItems());
        ObservableList<User> users = FXCollections.observableArrayList(db.getUsers());

        tvItems.setItems(items);
        tvMembers.setItems(users);
    }

    public void setTableViewProperties(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

        //make the cells editable tvitems
        colItemCode.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colAvailable.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colAuthor.setCellFactory(TextFieldTableCell.forTableColumn());

        colMemberID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        //make the cells editable in tvmembers
        colMemberID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colBirthDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
    }



    public void onButtonLendClick() {
        try {
            if (txtLendID.getText().isEmpty() || txtLendMember.getText().isEmpty()) {
                lblLendMsg.setText("Please fill the fields");
                return;
            }

            item = db.getItemByID(Integer.parseInt(txtLendID.getText()));
            User user = db.getUserByID(Integer.parseInt(txtLendMember.getText()));

            if (item != null && Objects.equals(item.getStatus(), true) && user != null) {
                item.setStatus(false);
                item.setDate(LocalDate.now());
                lblLendMsg.setText(String.format("Item %s has been lend", item.getId()));
                loadTableView();
                clearAddItemTextFields();
            } else if (item != null && Objects.equals(item.getStatus(), false) && user != null) {
                lblLendMsg.setText(String.format("Item %s is unavailable", item.getId()));
            } else {
                lblLendMsg.setText("Incorrect Item ID or Member ID");
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonReceiveClick() {
        try{
            if (Objects.equals(txtReceiveID.getText(), "")){
                lblReceiveMsg.setText("Please enter a item code");
                return;
            }

            item = db.getItemByID(Integer.parseInt(txtReceiveID.getText()));

            if (Objects.equals(item, null)){
                lblReceiveMsg.setText("Item not found");
            }

            if (Objects.equals(item.getStatus(), false)){

                if (DAYS.between(item.getDate(), LocalDate.now()) > 21){
                    int days = (int) (DAYS.between(item.getDate(), LocalDate.now()) - 21);
                    lblReceiveMsg.setText(String.format("Item is %s days too late", days));
                    return;
                }

                lblReceiveMsg.setText(String.format("Item %s has been received", item.getId()));
                loadTableView();
                clearAddItemTextFields();
                item.setDate(null);
                item.setStatus(true);
                return;
            }
            lblReceiveMsg.setText(String.format("Item %s is available", item.getId()));
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonLendTabClick() {
        hideAllGroups();
        lendGroup.setVisible(true);
    }

    public void onButtonCollectionClick() {
        hideAllGroups();
        collectionGroup.setVisible(true);
    }

    public void onButtonMembersClick() {
        hideAllGroups();
        membersGroup.setVisible(true);
    }

    public void onButtonAddItemClick() {
        hideAllGroups();
        addItemGroup.setVisible(true);
    }

    public void onButtonEditItemClick() {
    }

    //implemented through https://stackoverflow.com/questions/34857007/how-to-delete-row-from-table-column-javafx
    public void onButtonDeleteItemClick() {
        Item selectedItem = tvItems.getSelectionModel().getSelectedItem();
        tvItems.getItems().remove(selectedItem);
        db.getItems().remove(selectedItem);
    }

    public void onButtonAddItemToTableView() {
        try {
            int id = db.generateNextID("item");
            boolean status = true;
            String title = txtAddItemTitle.getText();
            String author = txtAddItemAuthor.getText();

            if (Objects.equals(title, "") || Objects.equals(author, "")) {
                lblAddItemError.setText("Please fill in all fields");
            }

            ArrayList<Item> items = db.getItems();
            Item item = new Item(id, status, title, author);
            items.add(item);

            lblAddItemError.setText("Item Added");
            loadTableView();
            clearAddItemTextFields();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //btnCancel pressed go back to TableView
    public void onButtonCancelItemToTableView() {
        clearAddItemTextFields();
        hideAllGroups();
        collectionGroup.setVisible(true);
    }

    //hides all the groups
    public void hideAllGroups(){
        lendGroup.setVisible(false);
        collectionGroup.setVisible(false);
        addItemGroup.setVisible(false);
        membersGroup.setVisible(false);
        addMemberGroup.setVisible(false);
    }

    //clears the text fields in add item
    public void clearAddItemTextFields(){
        txtAddItemTitle.clear();
        txtAddItemAuthor.clear();
        txtLendID.clear();
        txtLendMember.clear();
        txtReceiveID.clear();
    }

    //clears the text fields in add member
    public void clearAddMemberInputs(){
        txtAddMemberUsername.clear();
        txtAddMemberPassword.clear();
        txtAddMemberFirstName.clear();
        txtAddMemberLastName.clear();
        datePicker.setValue(null);
    }

    //displays the add member components
    public void onButtonAddMemberClick(ActionEvent actionEvent) {
        hideAllGroups();
        addMemberGroup.setVisible(true);
    }

    public void onButtonEditMemberClick(ActionEvent actionEvent) {

    }

    //implemented through https://stackoverflow.com/questions/34857007/how-to-delete-row-from-table-column-javafx
    public void onButtonDeleteMemberClick(ActionEvent actionEvent) {
        User selectedUser = tvMembers.getSelectionModel().getSelectedItem();
        tvMembers.getItems().remove(selectedUser);
        db.getUsers().remove(selectedUser);
    }

    public void onButtonAddMemberToTableViewClick(ActionEvent actionEvent) {
        int id = db.generateNextID("user");
        String username = txtAddMemberUsername.getText();
        String password = txtAddMemberPassword.getText();
        String firstName = txtAddMemberFirstName.getText();
        String lastName = txtAddMemberLastName.getText();
        LocalDate birthday = datePicker.getValue();

        if (Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(birthday, null)){
            lblAddMemberError.setText("Please fill in all fields");
            return;
        }

        User user = new User(id, username, firstName, lastName, password, birthday);
        ArrayList<User> users = db.getUsers();
        users.add(user);

        lblAddMemberError.setText(String.format("user %s has been added", username));
        loadTableView();
        clearAddMemberInputs();
    }

    //btnCancel pressed go back to TableView
    public void onButtonCancelMemberToTableViewClick(ActionEvent actionEvent) {
        clearAddMemberInputs();
        hideAllGroups();
        membersGroup.setVisible(true);
    }
}
