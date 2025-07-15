module com.localride.demo{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.localride.controller to javafx.fxml;
    opens com.localride.model to javafx.fxml;
    opens com.localride.demo to javafx.fxml;
    exports com.localride.controller;
    exports com.localride.model;
    exports com.localride.demo;
}