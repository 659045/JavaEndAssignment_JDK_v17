package com.example.javaendassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class MainController implements Initializable {
    private final Database db = new Database();

    private Item item;

    private User user;

    @FXML
    private Label lblWelcome, lblLendReceiveMsg, lblAddItemError, lblTableViewItemsError, lblTableViewMembersError,  lblEditItemError, lblAddMemberError, lblEditMemberError;

    @FXML
    private TextField txtReceiveID, txtLendID, txtLendMember, txtAddItemTitle, txtAddItemAuthor, txtEditItemTitle, txtEditItemAuthor,
            txtEditItemAvailability, txtAddMemberUsername, txtAddMemberPassword, txtAddMemberFirstName, txtAddMemberLastName,
            txtEditMemberUsername, txtEditMemberPassword, txtEditMemberFirstName, txtEditMemberLastName;

    @FXML
    private DatePicker datePicker, datePickerEditMember;

    @FXML
    private Group lendGroup, collectionGroup, addItemGroup, editItemGroup, membersGroup, addMemberGroup, editMemberGroup;

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
        try {
            hideAllGroupsAndSetVisible(lendGroup);
            db.readItemsFromFile();
            db.readUsersFromFile();
            loadTableView();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
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

        colMemberID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthday"));
    }

    //TABS ------------------------------------------
    public void onButtonLendTabClick() {
        try{
            hideAllGroupsAndSetVisible(lendGroup);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonCollectionClick() {
        try {
            hideAllGroupsAndSetVisible(collectionGroup);
            lblTableViewItemsError.setText("");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonMembersClick() {
        try {
            hideAllGroupsAndSetVisible(membersGroup);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //Lend Tab buttons
    public void onButtonLendClick() {
        try {
            if (txtLendID.getText().isEmpty() || txtLendMember.getText().isEmpty()) {
                lblLendReceiveMsg.setText("Please fill the fields");
                return;
            }

            item = db.getItemByID(Integer.parseInt(txtLendID.getText()));
            User user = db.getUserByID(Integer.parseInt(txtLendMember.getText()));

            if (Objects.equals(item, null) || Objects.equals(user, null)){
                lblLendReceiveMsg.setText("Incorrect Item ID and/or Member ID");
                return;
            }

            if (Objects.equals(item.getStatus(), "Yes")) {
                lblLendReceiveMsg.setText(String.format("Item %s has been lend", item.getId()));
                db.LendItem(item);
                clearAllInputs();
                loadTableView();
            } else{
                lblLendReceiveMsg.setText(String.format("Item %s is unavailable", item.getId()));
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonReceiveClick() {
        try{
            if (txtReceiveID.getText().isEmpty()){
                lblLendReceiveMsg.setText("Please enter a item code");
                return;
            }

            item = db.getItemByID(Integer.parseInt(txtReceiveID.getText()));

            if (Objects.equals(item, null)){
                lblLendReceiveMsg.setText("Item not found");
                return;
            }

            if (Objects.equals(item.getStatus(), "No")){

                if (DAYS.between(item.getDate(), LocalDate.now()) > 21){
                    int days = (int) (DAYS.between(item.getDate(), LocalDate.now()) - 21);
                    lblLendReceiveMsg.setText(String.format("Item is %s days too late", days));
                    return;
                }

                lblLendReceiveMsg.setText(String.format("Item %s has been received", item.getId()));
                db.ReceiveItem(item);
                clearAllInputs();
                loadTableView();
                return;
            }
            lblLendReceiveMsg.setText(String.format("Item %s is available", item.getId()));
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //Collections buttons
    public void onButtonAddItemClick() {
        try {
            hideAllGroupsAndSetVisible(addItemGroup);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonEditItemClick() {
        try {
            item = tvItems.getSelectionModel().getSelectedItem();
            if (Objects.equals(item, null)) {
                lblTableViewItemsError.setText("Select ab item to edit");
                return;
            }
            hideAllGroupsAndSetVisible(editItemGroup);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //implemented through https://stackoverflow.com/questions/34857007/how-to-delete-row-from-table-column-javafx
    public void onButtonDeleteItemClick() {
        try {
            int selectedItem = tvItems.getSelectionModel().getSelectedIndex();
            tvItems.getItems().remove(selectedItem);
            db.getItems().remove(selectedItem);
            db.writeItemsToFile();
        }
        catch(Exception e){
            lblTableViewItemsError.setText("Select an item to delete");
        }
    }

    //Collection add buttons
    public void onButtonConfirmAddItemClick() {
        try {
            int id = db.generateNextID("item");
            boolean status = true;
            String title = txtAddItemTitle.getText();
            String author = txtAddItemAuthor.getText();

            if (Objects.equals(title, "") || Objects.equals(author, "")) {
                lblAddItemError.setText("Please fill in all fields");
                return;
            }

            Item item = new Item(id, status, title, author);

            lblAddItemError.setText("Item Added");
            clearAllInputs();
            db.AddItem(item);
            loadTableView();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //btnCancel pressed go back to TableView
    public void onButtonCancelAddItem() {
        try {
            clearAllInputs();
            hideAllGroupsAndSetVisible(collectionGroup);
            lblTableViewItemsError.setText("");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //Collections edit buttons
    public void onButtonConfirmEditItemClick() {
        try {
            if (!txtEditItemTitle.getText().isEmpty() && !txtEditItemAuthor.getText().isEmpty() && !txtEditItemAvailability.getText().isEmpty()) {
                if (Objects.equals(txtEditItemAvailability.getText(), "Yes")) {
                    item.setStatus(true);
                } else if (Objects.equals(txtEditItemAvailability.getText(), "No")) {
                    item.setStatus(false);
                } else {
                    lblEditItemError.setText("Incorrect Availability: Yes/No");
                    return;
                }

                item.setTitle(txtEditItemTitle.getText());
                item.setAuthor(txtEditItemAuthor.getText());

                lblEditItemError.setText("Item has successfully been edited");
                clearAllInputs();
                db.writeItemsToFile();
                loadTableView();
            } else
                lblEditItemError.setText("Please fill in all fields");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonCancelEditItemClick() {
        try{
            clearAllInputs();
            hideAllGroupsAndSetVisible(collectionGroup);
            lblTableViewItemsError.setText("");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //Members buttons
    //displays the add member components
    public void onButtonAddMemberClick() {
        try {
            hideAllGroupsAndSetVisible(addMemberGroup);
        }
       catch (Exception e){
            throw new RuntimeException(e);
       }
    }

    public void onButtonEditMemberClick() {
        try{
            user = tvMembers.getSelectionModel().getSelectedItem();
            if (Objects.equals(user, null)){
                lblTableViewMembersError.setText("Select a member to edit");
                return;
            }
            hideAllGroupsAndSetVisible(editMemberGroup);
            }
            catch(Exception e){
                throw new RuntimeException(e);
        }
    }

    //implemented through https://stackoverflow.com/questions/34857007/how-to-delete-row-from-table-column-javafx
    public void onButtonDeleteMemberClick() {
        try {
            User selectedUser = tvMembers.getSelectionModel().getSelectedItem();
            if (Objects.equals(selectedUser, null)) {
                lblTableViewMembersError.setText("Select a member to delete");
                return;
            }
            tvMembers.getItems().remove(selectedUser);
            db.getUsers().remove(selectedUser);
            db.writeUsersToFile();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //Members add buttons
    public void onButtonConfirmAddMemberClick() {
        try {
            int id = db.generateNextID("user");
            String username = txtAddMemberUsername.getText();
            String password = txtAddMemberPassword.getText();
            String firstName = txtAddMemberFirstName.getText();
            String lastName = txtAddMemberLastName.getText();
            LocalDate birthday = datePicker.getValue();

            if (Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(firstName, "") || Objects.equals(lastName, "") || Objects.equals(birthday, null)) {
                lblAddMemberError.setText("Please fill in all fields");
                return;
            }

            user = new User(id, username, firstName, lastName, password, birthday, User.Type.Member);

            lblAddMemberError.setText(String.format("user %s has been added", username));
            clearAllInputs();
            db.AddUser(user);
            loadTableView();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //btnCancel pressed go back to TableView
    public void onButtonCancelAddMemberClick() {
        try {
            clearAllInputs();
            hideAllGroupsAndSetVisible(membersGroup);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //Member edit buttons
    public void onButtonConfirmEditMemberClick() {
        try {
            if (!Objects.equals(txtEditMemberUsername.getText(), "") && !Objects.equals(txtEditMemberPassword.getText(), "") && !Objects.equals(txtEditMemberFirstName.getText(), "") && !Objects.equals(txtEditMemberLastName.getText(), "") && !Objects.equals(datePickerEditMember.getValue(), null)) {
                user.setUsername(txtEditMemberUsername.getText());
                user.setPassword(txtEditMemberPassword.getText());
                user.setFirstName(txtEditMemberFirstName.getText());
                user.setLastName(txtEditMemberLastName.getText());
                user.setBirthday(datePickerEditMember.getValue());

                lblEditMemberError.setText("Member has successfully been edited");
                clearAllInputs();
                db.writeUsersToFile();
                loadTableView();
            } else {
                lblEditMemberError.setText("Please fill in all fields");
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void onButtonCancelEditMemberClick() {
        try{
            clearAllInputs();
            hideAllGroupsAndSetVisible(membersGroup);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //hides all the groups
    public void hideAllGroupsAndSetVisible(Group group){
        lendGroup.setVisible(false);
        collectionGroup.setVisible(false);
        addItemGroup.setVisible(false);
        editItemGroup.setVisible(false);
        membersGroup.setVisible(false);
        addMemberGroup.setVisible(false);
        editMemberGroup.setVisible(false);
        group.setVisible(true);
    }

    //clears inputs
    public void clearAllInputs(){
        txtLendID.clear();
        txtLendMember.clear();
        txtReceiveID.clear();
        txtAddItemTitle.clear();
        txtAddItemAuthor.clear();
        txtEditItemTitle.clear();
        txtEditItemAuthor.clear();
        txtEditItemAvailability.clear();
        txtAddMemberUsername.clear();
        txtAddMemberPassword.clear();
        txtAddMemberFirstName.clear();
        txtAddMemberLastName.clear();
        datePicker.setValue(null);
        txtEditMemberUsername.clear();
        txtEditMemberPassword.clear();
        txtEditMemberFirstName.clear();
        txtEditMemberLastName.clear();
        datePickerEditMember.setValue(null);
    }
}
