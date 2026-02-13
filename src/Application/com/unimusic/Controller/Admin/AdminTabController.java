package com.unimusic.Controller.Admin;

import com.unimusic.Controller.UniController;
import com.unimusic.Model.Authentication.UserProperty;
import com.unimusic.Model.Repository.Interface.UserRepository;
import com.unimusic.Model.Repository.RepositoryFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class AdminTabController extends UniController {
    @FXML private TableView<UserProperty> requestTable;
    @FXML private TableView<UserProperty> userManageTable;

    @FXML private TableColumn<UserProperty, String> colUsername;
    @FXML private TableColumn<UserProperty, Boolean> colAdmin;
    @FXML private TableColumn<UserProperty, Void> colActions;

    @FXML private TableColumn<UserProperty, String> colUsername1;
    @FXML private TableColumn<UserProperty, Boolean> colAdmin1;
    @FXML private TableColumn<UserProperty, Boolean> colEliminate1;
    @FXML private TableColumn<UserProperty, Void> colActions1;

    private UserRepository userRepo;
    private ObservableList<UserProperty> userRequestList = FXCollections.observableArrayList();
    private ObservableList<UserProperty> userManageList = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        userRepo = RepositoryFactory.getInstance().getUserRepository();
        configureRequestTable();
        configureUserTable();
    }
    private void configureUserTable(){
        colUsername1.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        colAdmin1.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        colAdmin1.setCellFactory(CheckBoxTableCell.forTableColumn(colAdmin));
        colAdmin1.setEditable(true);

        colEliminate1.setCellValueFactory(cellData -> cellData.getValue().eliminateProperty());
        colEliminate1.setCellFactory(CheckBoxTableCell.forTableColumn(colAdmin));
        colEliminate1.setEditable(true);

        userManageTable.setEditable(true);

        setupUserManageTableActionsColumn();

        userManageList.addAll(userRepo.getAllRegisteredUsers());
        userManageTable.setItems(userManageList);

    }
    private void configureRequestTable(){
        colUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        colAdmin.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        colAdmin.setCellFactory(CheckBoxTableCell.forTableColumn(colAdmin));
        colAdmin.setEditable(true);

        requestTable.setEditable(true);

        setupRequestTableActionsColumn();

        userRequestList.addAll(userRepo.getRegistrationReqList());
        requestTable.setItems(userRequestList);
    }


    private void setupRequestTableActionsColumn() {
        Callback<TableColumn<UserProperty, Void>, TableCell<UserProperty, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnApprove = new Button("OK");
            private final Button btnReject = new Button("NO");
            private final HBox pane = new HBox(10, btnApprove, btnReject);

            {
                btnApprove.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                btnReject.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

                btnApprove.setOnAction(event -> {
                    UserProperty user = getTableView().getItems().get(getIndex());
                    approvaUtente(user);
                });

                btnReject.setOnAction(event -> {
                    UserProperty user = getTableView().getItems().get(getIndex());
                    rifiutaUtente(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };

        colActions.setCellFactory(cellFactory);
    }

    private void setupUserManageTableActionsColumn() {
        Callback<TableColumn<UserProperty, Void>, TableCell<UserProperty, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnApprove = new Button("Conferma");
            private final HBox pane = new HBox(10, btnApprove);

            {
                btnApprove.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

                btnApprove.setOnAction(event -> {
                    UserProperty user = getTableView().getItems().get(getIndex());
                    confermaScelta(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };

        colActions1.setCellFactory(cellFactory);
    }

    private void confermaScelta(UserProperty user)
    {
        if (user.getEliminate()){
            userRepo.removeUser(user.getUser());
            userManageList.remove(user);
            return;
        }
        user.getUser().setAdmin(user.isAdmin());
        user.getUser().setGuest(false);
        userRepo.replaceUser(user.getUser());
    }
    private void approvaUtente(UserProperty user) {
        System.out.println("Utente approvato: " + user.getUsername() + " (Admin: " + user.isAdmin() + ")");
        userRepo.removeFromRegistrationReqList(user);
        user.getUser().setAdmin(user.isAdmin());
        user.getUser().setGuest(false);
        userRepo.replaceUser(user.getUser());
        userRequestList.remove(user);
    }

    private void rifiutaUtente(UserProperty user) {
        System.out.println("Utente rifiutato: " + user.getUsername());
        userRepo.removeFromRegistrationReqList(user);
        userRepo.removeUser(user.getUser());
        userRequestList.remove(user);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
