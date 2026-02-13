package com.unimusic.Controller;
import com.unimusic.Controller.Media.MediaControlController;
import com.unimusic.Controller.Media.MediaMainController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.Interface.MediaRepository;
import com.unimusic.Model.Repository.RepositoryFactory;
import com.unimusic.Nav.NavigationHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController extends UniController {
    @FXML private BorderPane homeContainer;
    private VBox homePage;
    private MediaRepository mediaRepo;
    private static MediaMainController mediaMainController;
    private static SongFormController addSongController;
    private UniController parentController;
    private static MediaControlController mediaControl;
    @FXML private ScrollPane mainViewArea;
    private int currentIdx;
    private int lastIdx;
    @FXML private HBox adminButton;

    private TextField searchField;
    private ObservableList<UniMedia> currentRawList;
    private VBox contentContainer;
    private String[] genres = {"All", "Pop", "Rock", "Classical", "Jazz"};
    @FXML
    public void initialize() {
        mediaRepo = RepositoryFactory.getInstance().getMediaRepository();
        parentController = Main.mainController;
        NavigationHandler.setHomeController(this);
        if (!AuthManager.getUser().isAdmin()) adminButton.setVisible(false);
        currentIdx = 0;
        lastIdx = -1;
        initSearchControls();
        loadDefaultHomeView(genres, mediaRepo.getSongs());

    }
    private void initSearchControls() {
        searchField = new TextField();
        searchField.setPromptText("Cerca titolo o autore...");
        // e anche quando scrivo rifiltra
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters(null));
    }

    private void applyFilters(String filterText) {
        if (contentContainer == null || currentRawList == null) return;

        contentContainer.getChildren().clear();

        List<UniMedia> filteredList = currentRawList.stream().filter(media -> {
            boolean matchesGenre = true;
            boolean matchesText = true;
            if (filterText != null && filterText.equals("All")) return true;
            // il genere è lo stesso
            if (filterText != null && !filterText.isEmpty()) {
                matchesGenre = media.getGenre().equalsIgnoreCase(filterText);
            }
            // il nome è lo stesso
            String text = searchField.getText().toLowerCase();
            System.out.println(text);
            if (!text.isEmpty()) {
                matchesText = media.getTitle().toLowerCase().contains(text) ||
                        media.getArtist().toLowerCase().contains(text);
            }

            return matchesGenre && matchesText;
        }).collect(Collectors.toList());

        loadMusicSections(contentContainer, 6, new ArrayList<>(filteredList));
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
    private HBox setupFilters(VBox vBox, String[] filterNames) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(0);
        hBox.setPadding(new Insets(0, 0, 10, 0));
        for (String name : filterNames) {
            Button btn = new Button(name);
            btn.getStyleClass().add("filter-btn");
            btn.setOnAction(e -> onFilterClick(name));
            hBox.getChildren().add(btn);
        }
        return hBox;
    }
    private void loadDefaultHomeView(String[] filterNames, ArrayList<UniMedia> media) {
        mainViewArea.setFitToHeight(false);
        mainViewArea.setFitToWidth(false);

        this.currentRawList = FXCollections.observableArrayList(media);

        if(searchField != null) searchField.setText("");

        homePage = new VBox(15);
        homePage.getStyleClass().add("v-box");
        homePage.setPadding(new Insets(10));

        HBox fitlerButtons = setupFilters(homePage, filterNames);

        HBox searchBar = new HBox(10, searchField, fitlerButtons);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 10, 0));
        homePage.getChildren().add(searchBar);

        contentContainer = new VBox(15);
        homePage.getChildren().add(contentContainer);

        applyFilters(null);

        mainViewArea.setContent(homePage);
    }
    private void loadMusicSections(VBox vBox, int sectionLength, ArrayList<UniMedia> media) {
        if (media.isEmpty()) {
            Label noResults = new Label("Nessun risultato trovato.");
            noResults.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            vBox.getChildren().add(noResults);
            return;
        }

        int songsToLoad = media.size();
        int startLength = 0;
        int endLength = sectionLength;

        while (songsToLoad > 0) {
            List<UniMedia> songs;
            if (endLength > media.size()) endLength = media.size();
            songs = media.subList(startLength, endLength);

            ScrollPane section = createSection(songs);
            startLength = endLength;
            endLength += sectionLength;
            vBox.getChildren().add(section);
            songsToLoad -= songs.size();
        }
    }

    private ScrollPane createSection(List<UniMedia> items) {
        HBox cardsContainer = new HBox(15);
        cardsContainer.getStyleClass().add("h-box");
        cardsContainer.setAlignment(Pos.CENTER_LEFT);
        for (UniMedia item : items) {
            VBox card = createMusicCard(item);
            cardsContainer.getChildren().add(card);
        }
        cardsContainer.setPadding(new Insets(0, 0, 0, 10));
        ScrollPane cardsScroll = new ScrollPane(cardsContainer);
        cardsScroll.setFitToHeight(false);
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
        currentIdx = 0;
        loadDefaultHomeView(genres, mediaRepo.getSongs());
        System.out.println("Home clicked");
    }


    private void onFilterClick(String filter) {
        applyFilters(filter);
        System.out.println("Filter clicked: " + filter);
    }
    @FXML
    private void onConcertClick()
    {
        currentIdx = 1;
        mainViewArea.setContent(null);
        loadDefaultHomeView(genres, mediaRepo.getConcerts());
        System.out.println("Concert clicked");
    }
    @FXML
    private void onAdminClick(){
        NavigationHandler.navigateToAdminPanel();
    }
    @FXML
    private void onAddNewMedia()
    {
        boolean guest = AuthManager.getUser().isGuest();
        if (guest) return;
        NavigationHandler.navigateToAddSong();
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