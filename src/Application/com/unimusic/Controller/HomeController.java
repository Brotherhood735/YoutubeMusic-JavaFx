package com.unimusic.Controller;
import com.unimusic.Controller.Media.MediaControlController;
import com.unimusic.Controller.Media.MediaMainController;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.MediaRepository;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HomeController extends UniController {
    @FXML private BorderPane homeContainer;
    private VBox homePage;
    private MediaRepository mediaRepo;
    private static MediaMainController mediaMainController;
    private static SongFormController addSongController;
    private UniController parentController;
    private static MediaControlController mediaControl;
    @FXML private ScrollPane mainViewArea;

    @FXML
    public void initialize() {
        mediaRepo = Main.getDataRepository();
        parentController = Main.mainController;
        NavigationHandler.setHomeController(this);
        loadDefaultHomeView();

    }

    private void loadMediaControlView(String fxml, boolean restartPanel)
    {
        if (!restartPanel) return;

        homeContainer.setBottom(null);
        try {
            FXMLLoader loader = NavigationHandler.getLoader(fxml);
            Node view = loader.load();
            mediaControl = loader.getController();
            homeContainer.setBottom(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }

    public static void setMediaMainController(MediaMainController controller) {
        mediaMainController = controller;
    }
    public static void setAddSongController(SongFormController controller) {addSongController = controller;}
    private void setupFilters(VBox vBox) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(0);
        String[] filterNames = {"Songs", "Concerts"};

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
        mainViewArea.setFitToHeight(false);
        mainViewArea.setFitToWidth(false);
        if (homePage != null)
        {
            mainViewArea.setContent(homePage);
            return;
        }
        homePage = new VBox(15);
        homePage.getStyleClass().add("v-box");
        // populate the VBOX
        setupFilters(homePage);
        loadMusicSections(homePage, 3);
        mainViewArea.setContent(homePage);
    }
    private void loadMusicSections(VBox vBox, int sectionLength) {
        int songsToLoad = mediaRepo.getMediaLength();
        int startLength = 0;
        int endLength = sectionLength;
        while (songsToLoad > 0)
        {
            ScrollPane section = createSection(mediaRepo.loadMediaWithInterval(startLength, endLength));
            startLength = endLength;
            endLength += sectionLength;
            vBox.getChildren().add(section);
            songsToLoad -= sectionLength;
        }
    }

    private ScrollPane createSection(List<UniMedia> items) {
        // Cards container
        HBox cardsContainer = new HBox(15);
        cardsContainer.getStyleClass().add("h-box");
        cardsContainer.setAlignment(Pos.CENTER_LEFT);
        for (UniMedia item : items) {
            VBox card = createMusicCard(item);
            cardsContainer.getChildren().add(card);
        }
        cardsContainer.setPadding(new Insets(0, 0, 0, 10));
        ScrollPane cardsScroll = new ScrollPane(cardsContainer);
        cardsScroll.setFitToHeight(false); // match height of content
        cardsScroll.setFitToWidth(false);
        return cardsScroll;
    }

    private VBox createMusicCard(UniMedia item) {
        VBox card = new VBox();
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(180);
        card.setPrefHeight(100);
        card.setMaxWidth(180);
        card.setStyle("-fx-cursor: hand;");

        // Image container
        // Placeholder image
        URL imageUrl = Main.class.getResource("Images/"+ item.getImageName());
        ImageView imagePlaceHolder;
        imagePlaceHolder = new ImageView(imageUrl.toString());
        imagePlaceHolder.setFitWidth(180);
        imagePlaceHolder.setFitHeight(180);
        // Hover effects
        imagePlaceHolder.setOnMouseEntered(e -> {
            imagePlaceHolder.setOpacity(0.7);
        });
        imagePlaceHolder.setOnMouseExited(e -> {
            imagePlaceHolder.setOpacity(1.0);
        });

        imagePlaceHolder.setOnMouseClicked(e -> onCardClick(item));

        // Title and artist
        Label titleLabel = new Label(item.getTitle());
        titleLabel.setFont(Font.font("Roboto", 16));
        titleLabel.getStyleClass().add("label-title");
        titleLabel.setStyle("font-weight: bold;");
        titleLabel.setMaxWidth(180);

        Label artistLabel = new Label(item.getType() + " • " + item.getArtist());
        artistLabel.getStyleClass().add("label-artist");
        artistLabel.setMaxWidth(180);

        card.getChildren().addAll(imagePlaceHolder, titleLabel, artistLabel);
        return card;
    }

    // Event handlers
    @FXML
    private void onHomeClick()
    {
        mainViewArea.setContent(null);
        loadDefaultHomeView();
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

        NavigationHandler.navigateToAddSong();
        System.out.println("Filter clicked: " + filter);
    }

    private void onCardClick(UniMedia item) {
        mainViewArea.setFitToHeight(true);
        mainViewArea.setFitToWidth(true);
        if (mediaMainController != null) {
            // we check if the clicked media is the current media
            // if it is, we don't play it again
            if(item.getId() == mediaMainController.getCurrentMedia().getId())
            {
                loadMediaControlView("Main/MediaControl.fxml", false);
                setContent(mediaMainController.getView(), 0);
                return;
            }
            // we stop whatever is playing before playing a new media
            mediaMainController.stopPlayer();
        }
        loadMediaControlView("Main/MediaControl.fxml", true);
        mainViewArea.setContent(null);
        NavigationHandler.navigateToMediaPlay(item);
        System.out.println("Playing: " + item.getTitle());
    }


    @Override
    public void setContent(Node content, int Idx) {
        mainViewArea.setContent(content);
    }

}