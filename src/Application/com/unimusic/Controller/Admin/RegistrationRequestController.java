package com.unimusic.Controller.Admin;

import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.UniUserRequest;
import com.unimusic.Model.Repository.UserRepository;
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

public class RegistrationRequestController extends UniController {
    @FXML private TableView<UniUserRequest> userTable;
    @FXML private TableColumn<UniUserRequest, String> colUsername;
    @FXML private TableColumn<UniUserRequest, Boolean> colAdmin;
    @FXML private TableColumn<UniUserRequest, Void> colActions;
    private UserRepository userRepo;
    private ObservableList<UniUserRequest> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userRepo = Main.getDataRepository();
        colUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        colAdmin.setCellValueFactory(cellData -> cellData.getValue().adminProperty());
        colAdmin.setCellFactory(CheckBoxTableCell.forTableColumn(colAdmin));
        colAdmin.setEditable(true);
        userTable.setEditable(true);

        setupActionsColumn();

        userList.addAll(userRepo.getRegistrationReqList());
        userTable.setItems(userList);
    }

    private void setupActionsColumn() {
        Callback<TableColumn<UniUserRequest, Void>, TableCell<UniUserRequest, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnApprove = new Button("OK");
            private final Button btnReject = new Button("NO");
            private final HBox pane = new HBox(10, btnApprove, btnReject);

            {
                btnApprove.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                btnReject.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

                btnApprove.setOnAction(event -> {
                    UniUserRequest user = getTableView().getItems().get(getIndex());
                    approvaUtente(user);
                });

                btnReject.setOnAction(event -> {
                    UniUserRequest user = getTableView().getItems().get(getIndex());
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

    private void approvaUtente(UniUserRequest user) {
        System.out.println("Utente approvato: " + user.getUsername() + " (Admin: " + user.isAdmin() + ")");
        userRepo.removeFromRegistrationReqList(user);
        userRepo.updatePermissions(user.getUser(), user.isAdmin(), false);
        userList.remove(user);
    }

    private void rifiutaUtente(UniUserRequest user) {
        System.out.println("Utente rifiutato: " + user.getUsername());
        userRepo.removeFromRegistrationReqList(user);
        userRepo.removeUser(user.getUser());
        userList.remove(user);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
