package com.unimusic.Controller;
import com.unimusic.Main;
import com.unimusic.Model.*;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class HomeController extends UniController {

    private UniController parentController;
    @FXML private Button playPauseBtn;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    @FXML private ScrollPane mainViewArea;
    private boolean isPlaying = false;

    @FXML
    public void initialize() {
        parentController = Main.mainController;
        NavigationHandler.setHomeController(this);
        loadDefaultHomeView();
    }

    private void setupFilters(VBox vBox) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(0);
        String[] filterNames = {"Podcasts", "Energise", "Feel good", "Relax", "Work out",
                "Party", "Commute", "Focus", "Romance", "Sad", "Sleep"};

        for (String name : filterNames) {
            Button btn = new Button(name);
            btn.getStyleClass().add("filter-btn");
            btn.setOnAction(e -> onFilterClick(name));
            hBox.getChildren().add(btn);
        }
        vBox.getChildren().add(hBox);
    }
    private void loadDefaultHomeView()
    {
        VBox vBox = new VBox();

        // populate the VBOX
        setupFilters(vBox);
        loadMusicSections(vBox);

        mainViewArea.setContent(vBox);
    }
    private void loadMusicSections(VBox vBox) {
        // Sezione "Listen again"
        VBox listenAgainSection = createSection("Listen again", getSampleData1());
        vBox.getChildren().add(listenAgainSection);

        // Sezione "Forgotten favourites"
        VBox forgottenSection = createSection("Forgotten favourites", getSampleData2());
        vBox.getChildren().add(forgottenSection);
    }

    private VBox createSection(String title, List<UniMedia> items) {
        VBox section = new VBox(15);

        // Section header
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        // Cards container
        HBox cardsContainer = new HBox(15);
        cardsContainer.setAlignment(Pos.CENTER_LEFT);

        for (UniMedia item : items) {
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

    private VBox createMusicCard(UniMedia item) {
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
        Label titleLabel = new Label(item.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(180);

        Label artistLabel = new Label(item.type + " • " + item.getArtist());
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

    private void onCardClick(UniMedia item) {
        nowPlayingTitle.setText(item.getTitle());
        nowPlayingArtist.setText(item.getArtist());
        NavigationHandler.navigateToMediaPlay(item);
        System.out.println("Playing: " + item.getTitle());
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
    private List<UniMedia> getSampleData1() {
        List<UniMedia> items = new ArrayList<>();
        items.add(new UniMedia("P-POP CULTURE","", "Karan Aujla & Ikky", null, MediaType.AudioFile));
        items.add(new UniMedia("Convo", "","Chinna, Cheema Y and Gur Sidhu", null, MediaType.AudioFile));
        items.add(new UniMedia("Karan Aujla", "", "4.59M subscribers", null, MediaType.AudioFile));
        items.add(new UniMedia("Street Knowledge","", "Wazir Patar", null, MediaType.AudioFile));
        items.add(new UniMedia("Culture Clicks", "",  "Navaan Sandhu", null, MediaType.AudioFile));
        items.add(new UniMedia("Bachfun*up", "","Karan Aujla", null, MediaType.AudioFile));
        return items;
    }

    private List<UniMedia> getSampleData2() {
        List<UniMedia> items = new ArrayList<>();
        items.add(new UniMedia("Get Rich","", "50 Cent", null, MediaType.AudioFile));
        items.add(new UniMedia("Summer Vibes","", "Various Artists", null, MediaType.AudioFile));
        items.add(new UniMedia("Random Access","", "Daft Punk", null, MediaType.AudioFile));
        items.add(new UniMedia("Nostalgia Mix", "","Various Artists", null, MediaType.AudioFile));
        items.add(new UniMedia("Classic Hip Hop", "", "Various Artists", null, MediaType.AudioFile));
        return items;
    }

    @Override
    public void setContent(Node content, int Idx) {
        mainViewArea.setContent(content);
    }

}