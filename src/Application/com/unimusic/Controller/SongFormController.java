package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.DocumentType;
import com.unimusic.Model.Media.UniDocument;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.Interface.DocumentRepository;
import com.unimusic.Model.Repository.Interface.MediaRepository;
import com.unimusic.Model.Repository.RepositoryFactory;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class SongFormController extends UniController {

    private MediaRepository mediaRepo;
    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private TextField instrumentsField;
    @FXML private TextField genreField;
    @FXML private CheckBox isLiveContentCheckBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField placeField;

    @FXML private TextField audioPathField;
    @FXML private TextField videoPathField;
    @FXML private TextField webUrlField;
    @FXML private TextField imagePathField;

    @FXML private TextField spartitoPathField;
    @FXML private TextField testoPathField;
    @FXML private TextField accordiPathField;
    @FXML
    public void initialize() {
        mediaRepo = RepositoryFactory.getInstance().getMediaRepository();
    }

    @FXML
    private void onBrowseAudio() {
        File file = browseFile("Audio Files", "*.mp3", "*.wav", "*.flac", "*.ogg", "*.mid", "*.midi");
        if (file != null) {
            String fileName = file.getName();
            audioPathField.setText(fileName);

            if (fileName.toLowerCase().endsWith(".mid") ||
                    fileName.toLowerCase().endsWith(".midi")) {
                instrumentsField.setPromptText("Inserire strumenti");
            }
        }
    }

    @FXML
    private void onBrowseVideo() {
        File file = browseFile("Video Files", "*.mp4", "*.avi", "*.mkv", "*.mov", "*.wmv", "*.flv");
        if (file != null) {
            videoPathField.setText(file.getName());
        }
    }

    @FXML
    private void onBrowseImage() {
        File file = browseFile("Immagine", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        if (file != null) {
            imagePathField.setText(file.getName());
        }
    }

    @FXML
    private void onBrowseSpartito() {
        File file = browseFile("Seleziona Spartito", "*.pdf");
        if (file != null) spartitoPathField.setText(file.getName());
    }

    @FXML
    private void onBrowseTesto() {
        File file = browseFile("Seleziona Testo", "*.txt", "*.pdf", "*.doc");
        if (file != null) testoPathField.setText(file.getName());
    }

    @FXML
    private void onBrowseAccordi() {
        File file = browseFile("Seleziona Accordi", "*.txt", "*.pdf");
        if (file != null) accordiPathField.setText(file.getName());
    }

    private File browseFile(String title, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File Documenti", extensions));
        return fileChooser.showOpenDialog(getStage());
    }
    @FXML
    private void onClearAudio() {
        audioPathField.clear();
        audioPathField.setPromptText("Nessun file selezionato");
    }

    @FXML
    private void onClearVideo() {
        videoPathField.clear();
        videoPathField.setPromptText("Nessun file selezionato");
    }

    @FXML
    private void onClearImage() {
        imagePathField.clear();
        imagePathField.setPromptText("Nessuna immagine selezionata");
    }
    @FXML private void onClearSpartito() {
        spartitoPathField.clear();
        spartitoPathField.setPromptText("Nessun file selezionato");
    }
    @FXML private void onClearTesto() {
        testoPathField.clear();
        testoPathField.setPromptText("Nessun file selezionato");
    }
    @FXML private void onClearAccordi() {
        accordiPathField.clear();
        accordiPathField.setPromptText("Nessun file selezionato");
    }
    @FXML
    private void onSave() {

        // Validate required fields
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Errore", "inserire un titolo");
            return;
        }

        if (artistField.getText().trim().isEmpty()) {
            showAlert("Errore", "inserire un artista");
            return;
        }

        // Check if at least one media source is provided
        boolean hasAudio = !audioPathField.getText().isEmpty();
        boolean hasVideo = !videoPathField.getText().isEmpty();
        boolean hasWeb = !webUrlField.getText().trim().isEmpty();
        boolean hasImage = !imagePathField.getText().isEmpty();

        // Check if all the data is given for a live concert
        boolean isLive = isLiveContentCheckBox.isSelected();
        boolean hasDate = datePicker.getValue() != null;
        boolean hasPlace = !placeField.getText().trim().isEmpty();
        boolean hasInstruments = !instrumentsField.getText().trim().isEmpty();

        if (!hasAudio && !hasVideo && !hasWeb) {
            showAlert("Errore",
                    "serve almeno un file audio, video o web");
            return;
        }
        if (!hasImage)
        {
            showAlert("Errore",
                    "è obbligatorio l'inserimento di una immagine.");
            return;
        }
        if (isLive && (!hasInstruments || !hasDate || !hasPlace))
        {
            showAlert("Errore",
                    "Essendo una registrazione dal vivo necessità di specificare gli strumenti, la data e il luogo.");
            return;
        }

        UniMedia uniMedia = createMedia(hasAudio, hasVideo, hasWeb, hasImage);

        DocumentRepository docRepo = RepositoryFactory.getInstance().getDocumentRepository();
        if (!spartitoPathField.getText().trim().isEmpty()) {
            UniDocument doc = new UniDocument(
                    docRepo.generateId(),
                    uniMedia.getId(),
                    DocumentType.SPARTITO,
                    spartitoPathField.getText()
            );
            docRepo.saveDocument(doc);
        }
        if (!testoPathField.getText().trim().isEmpty()) {
            UniDocument doc = new UniDocument(
                    docRepo.generateId(),
                    uniMedia.getId(),
                    DocumentType.TESTO,
                    testoPathField.getText()
            );
            docRepo.saveDocument(doc);
        }
        if (!accordiPathField.getText().trim().isEmpty()) {
            UniDocument doc = new UniDocument(
                    docRepo.generateId(),
                    uniMedia.getId(),
                    DocumentType.ACCORDI,
                    accordiPathField.getText()
            );
            docRepo.saveDocument(doc);
        }
        showAlert("Successo", "Canzone creata.");

        mediaRepo.saveMedia(uniMedia);
        NavigationHandler.navigateToHome();
    }

    @FXML
    private void onCancel() {
        NavigationHandler.navigateToHome();
    }

    private UniMedia createMedia(boolean hasAudio, boolean hasVideo, boolean hasWeb, boolean hasImage) {
        UniMedia song = new UniMedia(mediaRepo.generateMediaId());
        song.setTitle(titleField.getText());
        song.setArtist(artistField.getText());
        song.setInstruments(instrumentsField.getText());
        song.setGenre(genreField.getText());
        if (datePicker.getValue() != null)
        {
            song.setDate(datePicker.getValue().toString());
        }
        song.setPlace(placeField.getText());
        song.setUser(AuthManager.getUser());
        if (isLiveContentCheckBox.isSelected())
        {
            song.setType("Concert");
        }
        else
        {
            song.setType("Song");
        }
        if (hasAudio) {
            song.setAudioName(audioPathField.getText());
        }

        if (hasVideo) {
            song.setVideoName(videoPathField.getText());
        }

        if (hasWeb) {
            song.setWebUrl(webUrlField.getText().trim());
        }
        if (hasImage) {
            song.setImageName(imagePathField.getText());
        }
        return song;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Stage getStage() {
        return (Stage) titleField.getScene().getWindow();
    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}