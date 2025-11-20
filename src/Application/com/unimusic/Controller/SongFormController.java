package com.unimusic.Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongFormController extends UniController {

    @FXML private TextField titleField;
    @FXML private TextField artistField;
    @FXML private TextField instrumentsField;
    @FXML private TextField audioPathField;
    @FXML private TextField videoPathField;
    @FXML private TextField youtubeUrlField;
    @FXML private TextField imagePathField;

    @FXML private CheckBox hasAudioCheck;
    @FXML private CheckBox hasVideoCheck;
    @FXML private CheckBox hasYoutubeCheck;
    @FXML private CheckBox hasImageCheck;

    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    @FXML
    public void initialize() {
        // Add listeners to update checkboxes automatically
        audioPathField.textProperty().addListener((obs, old, newVal) ->
                hasAudioCheck.setSelected(!newVal.isEmpty() && !newVal.equals("No file selected")));

        videoPathField.textProperty().addListener((obs, old, newVal) ->
                hasVideoCheck.setSelected(!newVal.isEmpty() && !newVal.equals("No file selected")));

        youtubeUrlField.textProperty().addListener((obs, old, newVal) ->
                hasYoutubeCheck.setSelected(!newVal.isEmpty()));

        imagePathField.textProperty().addListener((obs, old, newVal) ->
                hasImageCheck.setSelected(!newVal.isEmpty() && !newVal.equals("No image selected")));
    }

    @FXML
    private void onBrowseAudio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Audio File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files",
                        "*.mp3", "*.wav", "*.flac", "*.ogg", "*.mid", "*.midi"),
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV Files", "*.wav"),
                new FileChooser.ExtensionFilter("FLAC Files", "*.flac"),
                new FileChooser.ExtensionFilter("OGG Files", "*.ogg"),
                new FileChooser.ExtensionFilter("MIDI Files", "*.mid", "*.midi"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            audioPathField.setText(file.getAbsolutePath());

            // If MIDI file, enable instruments field
            if (file.getName().toLowerCase().endsWith(".mid") ||
                    file.getName().toLowerCase().endsWith(".midi")) {
                instrumentsField.setPromptText("Enter instruments used in this MIDI file");
            }
        }
    }

    @FXML
    private void onBrowseVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files",
                        "*.mp4", "*.avi", "*.mkv", "*.mov", "*.wmv", "*.flv"),
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"),
                new FileChooser.ExtensionFilter("AVI Files", "*.avi"),
                new FileChooser.ExtensionFilter("MKV Files", "*.mkv"),
                new FileChooser.ExtensionFilter("MOV Files", "*.mov"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            videoPathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void onBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Cover Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            imagePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void onClearAudio() {
        audioPathField.clear();
        audioPathField.setPromptText("No file selected");
    }

    @FXML
    private void onClearVideo() {
        videoPathField.clear();
        videoPathField.setPromptText("No file selected");
    }

    @FXML
    private void onClearImage() {
        imagePathField.clear();
        imagePathField.setPromptText("No image selected");
    }

    @FXML
    private void onSave() {
        // Validate required fields
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a song title.");
            return;
        }

        if (artistField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter an artist name.");
            return;
        }

        // Check if at least one media source is provided
        boolean hasAudio = !audioPathField.getText().isEmpty() &&
                !audioPathField.getText().equals("No file selected");
        boolean hasVideo = !videoPathField.getText().isEmpty() &&
                !videoPathField.getText().equals("No file selected");
        boolean hasYoutube = !youtubeUrlField.getText().trim().isEmpty();

        if (!hasAudio && !hasVideo && !hasYoutube) {
            showAlert("Validation Error",
                    "Please provide at least one media source (Audio, Video, or YouTube URL).");
            return;
        }

        // Create song object
        Song song = createSongFromFields();

        // Display the created song (for demonstration)
        showSongDetails(song);

        // Here you would typically save to database or file
        // saveSongToDatabase(song);

        // Clear form after successful save
        clearForm();
    }

    @FXML
    private void onCancel() {
        clearForm();
        // Optionally close the window
        // getStage().close();
    }

    private Song createSongFromFields() {
        Song song = new Song();
        song.setTitle(titleField.getText().trim());
        song.setArtist(artistField.getText().trim());
        song.setInstruments(instrumentsField.getText().trim());

        List<String> mediaSources = new ArrayList<>();

        if (!audioPathField.getText().isEmpty() &&
                !audioPathField.getText().equals("No file selected")) {
            song.setAudioPath(audioPathField.getText());
            mediaSources.add("Audio: " + new File(audioPathField.getText()).getName());
        }

        if (!videoPathField.getText().isEmpty() &&
                !videoPathField.getText().equals("No file selected")) {
            song.setVideoPath(videoPathField.getText());
            mediaSources.add("Video: " + new File(videoPathField.getText()).getName());
        }

        if (!youtubeUrlField.getText().trim().isEmpty()) {
            song.setYoutubeUrl(youtubeUrlField.getText().trim());
            mediaSources.add("YouTube: " + youtubeUrlField.getText().trim());
        }

        if (!imagePathField.getText().isEmpty() &&
                !imagePathField.getText().equals("No image selected")) {
            song.setImagePath(imagePathField.getText());
        }

        return song;
    }

    private void showSongDetails(Song song) {
        StringBuilder details = new StringBuilder();
        details.append("Song Created Successfully!\n\n");
        details.append("Title: ").append(song.getTitle()).append("\n");
        details.append("Artist: ").append(song.getArtist()).append("\n");

        if (!song.getInstruments().isEmpty()) {
            details.append("Instruments: ").append(song.getInstruments()).append("\n");
        }

        details.append("\nMedia Sources:\n");
        if (song.getAudioPath() != null) {
            details.append("  Audio: ").append(song.getAudioPath()).append("\n");
        }
        if (song.getVideoPath() != null) {
            details.append("  Video: ").append(song.getVideoPath()).append("\n");
        }
        if (song.getYoutubeUrl() != null) {
            details.append("  YouTube: ").append(song.getYoutubeUrl()).append("\n");
        }
        if (song.getImagePath() != null) {
            details.append("  Cover: ").append(song.getImagePath()).append("\n");
        }

        showAlert("Success", details.toString());
    }

    private void clearForm() {
        titleField.clear();
        artistField.clear();
        instrumentsField.clear();
        audioPathField.clear();
        audioPathField.setPromptText("No file selected");
        videoPathField.clear();
        videoPathField.setPromptText("No file selected");
        youtubeUrlField.clear();
        imagePathField.clear();
        imagePathField.setPromptText("No image selected");
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

// Song Model Class
class Song {
    private String title;
    private String artist;
    private String instruments;
    private String audioPath;
    private String videoPath;
    private String youtubeUrl;
    private String imagePath;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getInstruments() { return instruments; }
    public void setInstruments(String instruments) { this.instruments = instruments; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public String getVideoPath() { return videoPath; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}