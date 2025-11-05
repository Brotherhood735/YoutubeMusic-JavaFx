package com.unimusic.Controller;
import com.almasb.fxgl.ui.FontFactory;
import com.unimusic.Config.AppConfig;
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
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeController extends UniController {
    @FXML private BorderPane homeContainer;
    private VBox homePage;
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
        ScrollPane listenAgainSection = createSection("", Main.getDataManager().loadMediaWithInterval(0, 6));
        vBox.getChildren().add(listenAgainSection);

        // Sezione "Forgotten favourites"
        ScrollPane forgottenSection = createSection("", Main.getDataManager().loadMediaWithInterval(6, 12));
        vBox.getChildren().add(forgottenSection);
        ScrollPane forgottenSection2 = createSection("", Main.getDataManager().loadMediaWithInterval(12, 18));
        vBox.getChildren().add(forgottenSection2);
        ScrollPane forgottenSection3 = createSection("", Main.getDataManager().loadMediaWithInterval(18, 24));
        vBox.getChildren().add(forgottenSection3);
        ScrollPane forgottenSection4 = createSection("", Main.getDataManager().loadMediaWithInterval(24, 30));
        vBox.getChildren().add(forgottenSection4);
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
        System.out.println("Filter clicked: " + filter);
    }

    private void onCardClick(UniMedia item) {
        mainViewArea.setFitToHeight(true);
        mainViewArea.setFitToWidth(true);
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