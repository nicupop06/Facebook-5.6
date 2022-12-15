module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens socialnetwork to javafx.fxml;
    opens socialnetwork.controllers to javafx.fxml;

    exports socialnetwork;
    exports socialnetwork.domain;
    exports socialnetwork.controllers;
}