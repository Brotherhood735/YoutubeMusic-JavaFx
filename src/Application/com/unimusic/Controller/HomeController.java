package com.unimusic.Controller;
import com.unimusic.Main;
import com.unimusic.Model.*;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends UniController {
    @FXML private BorderPane homeContainer;
    private static MediaMainController mediaMainController;
    private UniController parentController;
    private static MediaControlController mediaControl;
    @FXML private ScrollPane mainViewArea;

    @FXML
    public void initialize() {
        parentController = Main.mainController;
        NavigationHandler.setHomeController(this);
        loadDefaultHomeView();

    }

    private void loadMediaControlView(String fxml)
    {
        //if the media control is already initilized we don't need to restart it
        if(mediaControl != null) return;
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
        VBox vBox = new VBox();
        // populate the VBOX
        setupFilters(vBox);
        loadMusicSections(vBox);
        mainViewArea.setContent(vBox);
    }
    private void loadMusicSections(VBox vBox) {
        // Sezione "Listen again"
        VBox listenAgainSection = createSection("", Main.getDataManager().loadMediaWithInterval(0, 6));
        vBox.getChildren().add(listenAgainSection);

        // Sezione "Forgotten favourites"
        VBox forgottenSection = createSection("", Main.getDataManager().loadMediaWithInterval(6, 12));
        vBox.getChildren().add(forgottenSection);
        VBox forgottenSection2 = createSection("", Main.getDataManager().loadMediaWithInterval(12, 18));
        vBox.getChildren().add(forgottenSection2);
        VBox forgottenSection3 = createSection("", Main.getDataManager().loadMediaWithInterval(18, 24));
        vBox.getChildren().add(forgottenSection3);
        VBox forgottenSection4 = createSection("", Main.getDataManager().loadMediaWithInterval(24, 30));
        vBox.getChildren().add(forgottenSection4);
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
        ImageView imagePlaceHolder = new ImageView(new Image(item.getThumbnailUrl()));
        imagePlaceHolder.setFitWidth(130);
        imagePlaceHolder.setFitHeight(130);
        imagePlaceHolder.setPreserveRatio(true);
        imageContainer.getChildren().addAll(imagePlaceHolder);

        // Hover effects
        imageContainer.setOnMouseEntered(e -> {
            imagePlaceHolder.setOpacity(0.7);
        });
        imageContainer.setOnMouseExited(e -> {
            imagePlaceHolder.setOpacity(1.0);
        });

        imageContainer.setOnMouseClicked(e -> onCardClick(item));

        // Title and artist
        Label titleLabel = new Label(item.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(180);

        Label artistLabel = new Label(item.getType() + " • " + item.getArtist());
        artistLabel.setWrapText(true);
        artistLabel.setMaxWidth(180);

        card.getChildren().addAll(imageContainer, titleLabel, artistLabel);
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
        System.out.println("Filter clicked: " + filter);
    }

    private void onCardClick(UniMedia item) {
        loadMediaControlView("MediaControl.fxml");
        if (mediaMainController != null) {
            if(item.getId() == mediaMainController.getCurrentMedia().getId())
            {
                setContent(mediaMainController.getView(), 0);
                return;
            }
        }
        mainViewArea.setContent(null);
        NavigationHandler.navigateToMediaPlay(item);
        System.out.println("Playing: " + item.getTitle());
    }


    // Helper methods
    private String getRandomColor() {
        String[] colors = {"ff0000", "00ff00", "0000ff", "ff00ff", "00ffff",
                "ffff00", "ff6600", "6600ff", "ff0066", "00ff66"};
        return colors[(int)(Math.random() * colors.length)];
    }


    @Override
    public void setContent(Node content, int Idx) {
        mainViewArea.setContent(content);
    }

}