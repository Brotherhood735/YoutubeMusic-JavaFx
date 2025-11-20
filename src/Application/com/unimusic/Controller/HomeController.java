package com.unimusic.Controller;
import com.unimusic.Controller.Media.MediaControlController;
import com.unimusic.Controller.Media.MediaMainController;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.MediaRepository;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
        homePage.setStyle("-fx-box-border: transparent;");
        // populate the VBOX
        setupFilters(homePage);
        loadMusicSections(homePage);
        mainViewArea.setContent(homePage);
    }
    private void loadMusicSections(VBox vBox) {
        // Sezione "Listen again"
        ScrollPane listenAgainSection = createSection("", mediaRepo.loadMediaWithInterval(0, 2));
        vBox.getChildren().add(listenAgainSection);

        // Sezione "Forgotten favourites"
        ScrollPane forgottenSection = createSection("", mediaRepo.loadMediaWithInterval(2, 4));
        vBox.getChildren().add(forgottenSection);
    }

    private ScrollPane createSection(String title, List<UniMedia> items) {
        // Cards container
        HBox cardsContainer = new HBox(15);
        cardsContainer.setAlignment(Pos.CENTER_LEFT);
        for (UniMedia item : items) {
            VBox card = createMusicCard(item);
            cardsContainer.getChildren().add(card);
        }
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
        Image image = new Image(item.getThumbnailUrl());
        ImageView imagePlaceHolder;
        System.out.println(item.getTitle() + image.heightProperty().intValue() + image.errorProperty().getValue());
        if (!image.errorProperty().getValue())
        {
            imagePlaceHolder = new ImageView(image);
        }
        else
        {
            URL fxmlUrl = Main.class.getResource("Images/default_music_icon.png");
            imagePlaceHolder = new ImageView(fxmlUrl.toString());
        }
        imagePlaceHolder.setFitWidth(180);
        imagePlaceHolder.setFitHeight(100);
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
        titleLabel.setStyle("font-weight: bold;");
        titleLabel.setMaxWidth(180);

        Label artistLabel = new Label(item.getType() + " • " + item.getArtist());
        artistLabel.setFont(Font.font("Roboto", 5));
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
                loadMediaControlView("MediaControl.fxml", false);
                setContent(mediaMainController.getView(), 0);
                return;
            }
            // we stop whatever is playing before playing a new media
            mediaMainController.stopPlayer();
        }
        loadMediaControlView("MediaControl.fxml", true);
        mainViewArea.setContent(null);
        NavigationHandler.navigateToMediaPlay(item);
        System.out.println("Playing: " + item.getTitle());
    }


    @Override
    public void setContent(Node content, int Idx) {
        mainViewArea.setContent(content);
    }

}