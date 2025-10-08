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

    opens com.unimusic to javafx.fxml;
    exports com.unimusic;
    exports com.unimusic.Controller;
    opens com.unimusic.Controller to javafx.fxml;
    exports com.unimusic.Model;
    opens com.unimusic.Model to javafx.fxml;
    exports com.unimusic.Nav;
    opens com.unimusic.Nav to javafx.fxml;
}