module com.unimusic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;
    requires javafx.base;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires annotations;


    opens com.unimusic to javafx.fxml;
    exports com.unimusic;
    exports com.unimusic.Controller;
    opens com.unimusic.Controller to javafx.fxml;
    exports com.unimusic.Nav;
    opens com.unimusic.Nav to javafx.fxml;
    exports com.unimusic.Model.Media;
    opens com.unimusic.Model.Media to javafx.fxml;
    exports com.unimusic.Model.Repository;
    opens com.unimusic.Model.Repository to javafx.fxml;
    exports com.unimusic.Model.Authentication;
    opens com.unimusic.Model.Authentication to javafx.fxml;
    exports com.unimusic.Controller.Media;
    opens com.unimusic.Controller.Media to javafx.fxml;
    exports com.unimusic.Controller.Authentication;
    opens com.unimusic.Controller.Authentication to javafx.fxml;
    exports com.unimusic.Controller.Media.Comment;
    opens com.unimusic.Controller.Media.Comment to javafx.fxml;
    exports com.unimusic.Controller.Admin;
    opens com.unimusic.Controller.Admin to javafx.fxml;
    exports com.unimusic.Model.Repository.Implementation;
    opens com.unimusic.Model.Repository.Implementation to javafx.fxml;
    exports com.unimusic.Model.Repository.Interface;
    opens com.unimusic.Model.Repository.Interface to javafx.fxml;
}