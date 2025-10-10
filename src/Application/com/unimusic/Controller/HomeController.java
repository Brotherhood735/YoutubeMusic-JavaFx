package com.unimusic.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML private VBox mainContent;
    @FXML private HBox filtersContainer;
    @FXML private Button playPauseBtn;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;

    private boolean isPlaying = false;

    @FXML
    public void initialize() {
        setupFilters();
        loadMusicSections();
    }

    private void setupFilters() {
        String[] filterNames = {"Podcasts", "Energise", "Feel good", "Relax", "Work out",
                "Party", "Commute", "Focus", "Romance", "Sad", "Sleep"};

        for (String name : filterNames) {
            Button btn = new Button(name);
            btn.getStyleClass().add("filter-btn");
            btn.setOnAction(e -> onFilterClick(name));
            filtersContainer.getChildren().add(btn);
        }
    }

    private void loadMusicSections() {
        // Sezione "Listen again"
        VBox listenAgainSection = createSection("Listen again", getSampleData1());
        mainContent.getChildren().add(listenAgainSection);

        // Sezione "Forgotten favourites"
        VBox forgottenSection = createSection("Forgotten favourites", getSampleData2());
        mainContent.getChildren().add(forgottenSection);
    }

    private VBox createSection(String title, List<MusicItem> items) {
        VBox section = new VBox(15);

        // Section header
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        // Cards container
        HBox cardsContainer = new HBox(15);
        cardsContainer.setAlignment(Pos.CENTER_LEFT);

        for (MusicItem item : items) {
            VBox card = createMusicCard(item);
            cardsContainer.getChildren().add(card);
        }
        ScrollPane cardsScroll = new ScrollPane(cardsContainer);
        cardsContainer.setFillHeight(true);
        cardsScroll.setFitToHeight(true); // match height of content
        cardsScroll.setFitToWidth(false);
        section.getChildren().addAll(titleLabel, cardsScroll);
        return section;
    }

    private VBox createMusicCard(MusicItem item) {
        VBox card = new VBox(12);
        card.setPrefWidth(180);
        card.setMaxWidth(180);
        card.setStyle("-fx-cursor: hand;");

        // Image container
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(180, 180);
        imageContainer.setMaxSize(180, 180);

        // Placeholder image
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(180, 180);
        imagePlaceholder.setStyle("-fx-background-color: linear-gradient(to bottom right, #" +
                getRandomColor() + ", #" + getRandomColor() + "); -fx-background-radius: 8;");


        imageContainer.getChildren().addAll(imagePlaceholder);

        // Hover effects
        imageContainer.setOnMouseEntered(e -> {
            imagePlaceholder.setOpacity(0.7);
        });
        imageContainer.setOnMouseExited(e -> {
            imagePlaceholder.setOpacity(1.0);
        });

        imageContainer.setOnMouseClicked(e -> onCardClick(item));

        // Title and artist
        Label titleLabel = new Label(item.title);
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(180);

        Label artistLabel = new Label(item.type + " • " + item.artist);
        artistLabel.setWrapText(true);
        artistLabel.setMaxWidth(180);

        card.getChildren().addAll(imageContainer, titleLabel, artistLabel);
        return card;
    }

    // Event handlers
    @FXML
    private void onHomeClick() {
        System.out.println("Home clicked");
    }

    @FXML
    private void onExploreClick() {
        System.out.println("Explore clicked");
    }

    @FXML
    private void onLibraryClick() {
        System.out.println("Library clicked");
    }

    private void onFilterClick(String filter) {
        System.out.println("Filter clicked: " + filter);
    }

    private void onCardClick(MusicItem item) {
        nowPlayingTitle.setText(item.title);
        nowPlayingArtist.setText(item.artist);
        System.out.println("Playing: " + item.title);
    }

    @FXML
    private void onPlayPause() {
        isPlaying = !isPlaying;
        playPauseBtn.setText(isPlaying ? "⏸" : "▶");
    }

    @FXML
    private void onPrevious() {
        System.out.println("Previous track");
    }

    @FXML
    private void onNext() {
        System.out.println("Next track");
    }

    // Helper methods
    private String getRandomColor() {
        String[] colors = {"ff0000", "00ff00", "0000ff", "ff00ff", "00ffff",
                "ffff00", "ff6600", "6600ff", "ff0066", "00ff66"};
        return colors[(int)(Math.random() * colors.length)];
    }

    // Data methods - sostituisci con lettura CSV
    private List<MusicItem> getSampleData1() {
        List<MusicItem> items = new ArrayList<>();
        items.add(new MusicItem("P-POP CULTURE", "Karan Aujla & Ikky", null, "Album"));
        items.add(new MusicItem("Convo", "Chinna, Cheema Y and Gur Sidhu", null, "Song"));
        items.add(new MusicItem("Karan Aujla", "4.59M subscribers", null, "Artist"));
        items.add(new MusicItem("Street Knowledge", "Wazir Patar", null, "Album"));
        items.add(new MusicItem("Culture Clicks", "Navaan Sandhu", null, "Song"));
        items.add(new MusicItem("Bachfun*up", "Karan Aujla", null, "Album"));
        return items;
    }

    private List<MusicItem> getSampleData2() {
        List<MusicItem> items = new ArrayList<>();
        items.add(new MusicItem("Get Rich", "50 Cent", null, "Album"));
        items.add(new MusicItem("Summer Vibes", "Various Artists", null, "Playlist"));
        items.add(new MusicItem("Random Access", "Daft Punk", null, "Album"));
        items.add(new MusicItem("Nostalgia Mix", "Various Artists", null, "Playlist"));
        items.add(new MusicItem("Classic Hip Hop", "Various Artists", null, "Playlist"));
        return items;
    }

    // Inner class
    static class MusicItem {
        String title;
        String artist;
        String imageUrl;
        String type;

        public MusicItem(String title, String artist, String imageUrl, String type) {
            this.title = title;
            this.artist = artist;
            this.imageUrl = imageUrl;
            this.type = type;
        }
    }
}