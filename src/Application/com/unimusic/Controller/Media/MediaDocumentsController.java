package com.unimusic.Controller.Media;

import com.unimusic.Config.AppConfig;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.DocumentType;
import com.unimusic.Model.Media.UniDocument;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.Implementation.DocumentRepositoryImpl;
import com.unimusic.Model.Repository.Interface.DocumentRepository;
import com.unimusic.Model.Repository.RepositoryFactory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MediaDocumentsController {

    @FXML private VBox documentsContainer;

    private UniMedia currentMedia;
    private DocumentRepository docRepo;

    public MediaDocumentsController(UniMedia media) {
        this.currentMedia = media;
    }

    @FXML
    public void initialize() {
        docRepo = RepositoryFactory.getInstance().getDocumentRepository();

        loadDocuments();
    }

    private void loadDocuments() {
        documentsContainer.getChildren().clear();

        List<UniDocument> allDocs = docRepo.getDocumentsByMediaId(currentMedia.getId());

        for (DocumentType type : DocumentType.values()) {

            List<UniDocument> docsOfThisType = allDocs.stream()
                    .filter(d -> d.getType() == type)
                    .collect(Collectors.toList());

            VBox typeSection = createTypeSection(type, docsOfThisType);

            documentsContainer.getChildren().add(typeSection);

            documentsContainer.getChildren().add(new Separator());
        }
    }

    private VBox createTypeSection(DocumentType type, List<UniDocument> docs) {
        VBox section = new VBox(5);
        section.setPadding(new Insets(5, 0, 10, 0));

        Label header = new Label(type.getLabel());
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        section.getChildren().add(header);

        if (docs.isEmpty()) {
            Label emptyLabel = new Label("Nessun " + type.getLabel().toLowerCase() + " presente.");
            emptyLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            section.getChildren().add(emptyLabel);
        } else {
            for (UniDocument doc : docs) {
                HBox docRow = createDocumentRow(doc);
                section.getChildren().add(docRow);
            }
        }

        if (AuthManager.getUser().isGuest()) return section;

        Button addBtn = new Button("Aggiungi " + type.getLabel());
        addBtn.setOnAction(e -> uploadDocument(type));

        VBox.setMargin(addBtn, new Insets(5, 0, 0, 0));
        section.getChildren().add(addBtn);

        return section;
    }

    private HBox createDocumentRow(UniDocument doc) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 5; -fx-background-radius: 5;");
        HBox.setHgrow(row, Priority.ALWAYS);
        // Estrai il nome del file dal percorso completo per mostrarlo
        String fileName = doc.getFileName();

        Label nameLabel = new Label(fileName);
        nameLabel.setMaxWidth(200);
        nameLabel.setWrapText(false);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Button openBtn = new Button("Apri");
        openBtn.setOnAction(e -> openFile(doc.getFileName()));
        openBtn.setAlignment(Pos.CENTER_RIGHT);

        Button deleteBtn = new Button("Elimina");
        deleteBtn.setOnAction(e-> removeFile(doc));
        deleteBtn.setAlignment(Pos.CENTER_RIGHT);

        if (AuthManager.getUser().isAdmin() || currentMedia.getUser().getUsername().equals(AuthManager.getUser().getUsername())) {
            row.getChildren().addAll(nameLabel, openBtn, deleteBtn);
        }
        else{
            row.getChildren().addAll(nameLabel, openBtn);
        }
        return row;
    }

    private void removeFile(UniDocument doc)
    {
        System.out.println("Cancellazione documento");
        docRepo.removeDocument(doc);
        loadDocuments();
    }
    private void openFile(String fileName) {
        new Thread(() -> {
            try {
                File file = new File(AppConfig.RESOURCES_DIR + "/Documents/" + fileName);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.err.println("File non trovato: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }

    private void uploadDocument(DocumentType type) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Aggiungi " + type.getLabel());

        if(type == DocumentType.SPARTITO) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        } else {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tutti i file", "*.*"));
        }

        File file = fileChooser.showOpenDialog(documentsContainer.getScene().getWindow());
        if (file != null) {
            UniDocument newDoc = new UniDocument(
                    docRepo.generateId(),
                    currentMedia.getId(),
                    type,
                    file.getName()
            );
            docRepo.saveDocument(newDoc);

            loadDocuments();
        }
    }
}