package by.bsuir.povs.lab5.controller;

import by.bsuir.povs.lab5.model.ChartModel;
import by.bsuir.povs.lab5.service.ChartService;
import by.bsuir.povs.lab5.service.config.SerialPortConfig;
import by.bsuir.povs.lab5.service.impl.ChartServiceImpl;
import by.bsuir.povs.lab5.service.impl.SoundServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppController {
    private static final String MUSIC_FILE_PATH = "src/main/resources/music/music.wav";
    private static final String ALARM_FILE_PATH = "src/main/resources/music/alarm.wav";
    private static final ObservableList<Integer> DELAYS = FXCollections.observableArrayList(1000, 3000);

    @FXML
    private LineChart<String, Double> potentiometerChart;

    @FXML
    private LineChart<String, Double> lightChart;

    @FXML
    private LineChart<String, Double> temperatureChart;

    @FXML
    private ChoiceBox<Integer> delayChoiceBox;

    @FXML
    private CheckBox lightCheckBox;

    @FXML
    private CheckBox potentiometerCheckBox;

    @FXML
    private CheckBox temperatureCheckBox;

    @FXML
    private ToggleButton startStopToggle;

    @FXML
    private Button playButton;

    @FXML
    private Button repeatButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button muteButton;

    @FXML
    private Button volumeUpButton;

    @FXML
    private Button volumeDownButton;

    private SerialPort serialPort;
    private static boolean isLowTemperatureAlertShow;

    @FXML
    void initialize() {
        File file = new File(MUSIC_FILE_PATH);
        SoundServiceImpl soundService = new SoundServiceImpl();
        soundService.setFile(file);
        playButton.setOnAction(actionEvent -> soundService.play());
        repeatButton.setOnAction(actionEvent -> soundService.loop());
        stopButton.setOnAction(actionEvent -> soundService.stop());
        muteButton.setOnAction(actionEvent -> soundService.volumeMute());
        volumeUpButton.setOnAction(actionEvent -> soundService.volumeUp());
        volumeDownButton.setOnAction(actionEvent -> {
            soundService.volumeDown();
            showLowTemperatureAlert(14.5);
        });

        ChartService chartService = new ChartServiceImpl();
        ChartModel chartModel = new ChartModel(List.of(lightChart, potentiometerChart, temperatureChart), new ArrayList<>(), List.of("Light value", "Potentiometer value", "Temperature value"));
        chartModel = chartService.initCharts(chartModel);
        delayChoiceBox.setItems(DELAYS);
        delayChoiceBox.setValue(DELAYS.get(0));

        SerialPortConfig.configSerialPort(chartModel.getSeries(), soundService);
        serialPort = SerialPortConfig.serialPort;
        startStopToggle.selectedProperty().addListener((__, ___, selected) -> onStartChanged(selected));
        lightCheckBox.selectedProperty().addListener((__, ___, selected) -> onStartChanged(startStopToggle.isSelected()));
        potentiometerCheckBox.selectedProperty().addListener((__, ___, selected) -> onStartChanged(startStopToggle.isSelected()));
        temperatureCheckBox.selectedProperty().addListener((__, ___, selected) -> onStartChanged(startStopToggle.isSelected()));
        delayChoiceBox.setOnAction(selected -> onStartChanged(startStopToggle.isSelected()));
    }

    public static void showLowTemperatureAlert(double temperature) {
        if (!isLowTemperatureAlertShow) {
            isLowTemperatureAlertShow = true;
            File file = new File(ALARM_FILE_PATH);
            SoundServiceImpl soundService = new SoundServiceImpl();
            soundService.setFile(file);
            soundService.play();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Low temperature");
            alert.setHeaderText("Temperature value: " + temperature);
            alert.setContentText("It's cold in the office, find a warmer place for yourself!");
            alert.showAndWait();
            soundService.stop();
            isLowTemperatureAlertShow = false;
        }
    }

    private void onStartChanged(boolean selected) {
        try {
            if (selected) {
                serialPort.writeBytes(new byte[]{(byte) 1, convertCheckBoxValueToByte(lightCheckBox), convertCheckBoxValueToByte(potentiometerCheckBox),
                        convertCheckBoxValueToByte(temperatureCheckBox), convertChoiceBoxToByte(delayChoiceBox)});
            } else {
                serialPort.writeBytes(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0});
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private byte convertCheckBoxValueToByte(CheckBox checkBox) {
        return checkBox.isSelected() ? (byte) 1 : (byte) 0;
    }

    private byte convertChoiceBoxToByte(ChoiceBox<Integer> choiceBox) {
        if (Objects.equals(choiceBox.getValue(), DELAYS.get(1))) {
            return (byte) 1;
        }
        return (byte) 0;
    }
}
