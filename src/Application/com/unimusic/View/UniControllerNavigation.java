package com.unimusic.View;

import com.unimusic.Main;
import com.unimusic.Model.AuthManager;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class UniControllerNavigation
{
    public static void loadView(String fxml, int idx, UniController controller, UniData data)
    {
        try {
            URL fxmlUrl = Main.class.getResource("Fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node view = loader.load();
            controller.setContent(view, idx);
            UniController nextController =  loader.getController();
            nextController.setData(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    public void navigateTo(String fxml, UniController controller, int idx, UniData data)
    {
        if (AuthManager.isAuthenticated())
        {
            loadView(fxml, idx, controller, data);
        }
    }
}
