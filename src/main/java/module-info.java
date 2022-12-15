module by.bsuir.povs.lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jssc;
    requires java.desktop;

    opens by.bsuir.povs.lab5 to javafx.fxml;
    exports by.bsuir.povs.lab5;
    exports by.bsuir.povs.lab5.service;
    exports by.bsuir.povs.lab5.model;
    opens by.bsuir.povs.lab5.service to javafx.fxml;
    exports by.bsuir.povs.lab5.service.impl;
    opens by.bsuir.povs.lab5.service.impl to javafx.fxml;
    exports by.bsuir.povs.lab5.controller;
    opens by.bsuir.povs.lab5.controller to javafx.fxml;
    exports by.bsuir.povs.lab5.service.listener;
    opens by.bsuir.povs.lab5.service.listener to javafx.fxml;
}