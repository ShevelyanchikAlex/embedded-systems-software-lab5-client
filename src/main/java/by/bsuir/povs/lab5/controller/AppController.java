package by.bsuir.povs.lab5.controller;

import by.bsuir.povs.lab5.model.ChartModel;
import by.bsuir.povs.lab5.service.ChartService;
import by.bsuir.povs.lab5.service.config.SerialPortConfig;
import by.bsuir.povs.lab5.service.impl.ChartServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleButton;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.ArrayList;
import java.util.List;

public class AppController {
    private static final ObservableList<Integer> DELAYS = FXCollections.observableArrayList(1000, 3000);

    @FXML
    private LineChart<String, Double> potentiometerChart;

    @FXML
    private LineChart<String, Double> lightChart;

    @FXML
    private ChoiceBox<Integer> delayChoiceBox;

    @FXML
    private CheckBox lightCheckBox;

    @FXML
    private CheckBox potentiometerCheckBox;

    @FXML
    private ToggleButton startStopToggle;

    private SerialPort serialPort;

    @FXML
    void initialize() {
        ChartService chartService = new ChartServiceImpl();
        ChartModel chartModel = new ChartModel(List.of(lightChart, potentiometerChart), new ArrayList<>());
        chartModel = chartService.initCharts(chartModel);
        delayChoiceBox.setItems(DELAYS);
        delayChoiceBox.setValue(DELAYS.get(0));

        SerialPortConfig.configSerialPort(chartModel.getSeries());
        serialPort = SerialPortConfig.serialPort;
        startStopToggle.selectedProperty().addListener((__, ___, selected) -> onStartChanged(selected));
        lightCheckBox.selectedProperty().addListener((__, ___, selected) -> onStartChanged(startStopToggle.isSelected()));
        potentiometerCheckBox.selectedProperty().addListener((__, ___, selected) -> onStartChanged(startStopToggle.isSelected()));
        delayChoiceBox.setOnAction(selected -> onStartChanged(startStopToggle.isSelected()));
    }

    private void onStartChanged(boolean selected) {
        try {
            if (selected) {
                serialPort.writeBytes(new byte[]{(byte) 1, convertCheckBoxValueToByte(lightCheckBox), convertCheckBoxValueToByte(potentiometerCheckBox), convertChoiceBoxToByte(delayChoiceBox)});
            } else {
                serialPort.writeBytes(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0});
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private byte convertCheckBoxValueToByte(CheckBox checkBox) {
        return checkBox.isSelected() ? (byte) 1 : (byte) 0;
    }

    private byte convertChoiceBoxToByte(ChoiceBox<Integer> choiceBox) {
        if (choiceBox.getValue() == 3000) {
            return (byte) 1;
        }
        return (byte) 0;
    }
}
