package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.MediaRepository;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class SongFormController extends UniController {

    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private TextField instrumentsField;
    @FXML private TextField audioPathField;
    @FXML private TextField videoPathField;
    @FXML private TextField webUrlField;
    @FXML private TextField imagePathField;

    @FXML private CheckBox isLiveContentCheckBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField placeField;
    private MediaRepository mediaRepo;
    @FXML
    public void initialize() {
        mediaRepo = Main.getDataRepository();
    }

    @FXML
    private void onBrowseAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Audio");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files",
                        "*.mp3", "*.wav", "*.flac", "*.ogg", "*.mid", "*.midi")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            String fileName = file.getName();
            audioPathField.setText(fileName);

            // If MIDI file, enable instruments field
            if (fileName.toLowerCase().endsWith(".mid") ||
                    fileName.toLowerCase().endsWith(".midi")) {
                instrumentsField.setPromptText("Inserire strumenti");
            }
        }
    }

    @FXML
    private void onBrowseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Video");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video File",
                        "*.mp4", "*.avi", "*.mkv", "*.mov", "*.wmv", "*.flv")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            videoPathField.setText(file.getName());
        }
    }

    @FXML
    private void onBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Immagine");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagine",
                        "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            imagePathField.setText(file.getName());
        }
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

        // Create song object
        UniMedia uniMedia = createMedia(hasAudio, hasVideo, hasWeb, hasImage);

        // Display the created song (for demonstration)
        showAlert("Success", "Song Created Successfully!");

        // Here you would typically save to database or file
        mediaRepo.saveMedia(uniMedia);
        // Clear form after successful save
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
        if (datePicker.getValue() != null)
        {
            song.setDate(datePicker.getValue().toString());
        }
        song.setPlace(placeField.getText());
        if (isLiveContentCheckBox.isSelected())
        {
            song.setType("Live Concert");
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