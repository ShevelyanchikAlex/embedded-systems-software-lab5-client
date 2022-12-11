package by.bsuir.povs.lab5.service;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class EventListener implements SerialPortEventListener {
    private static final Pattern MSG_PATTERN = Pattern.compile("(empty|[0-9]*\\.?[0-9]+)\\|(empty|[0-9]*\\.?[0-9]+)");
    private static final String EMPTY_VAL = "empty";
    private static final String MSG_SEPARATOR = "\n";
    private static final String VALUES_SEPARATOR = "\\|";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final StringBuffer DATA_BUFFER = new StringBuffer();
    private final SerialPort serialPort;

    private final List<ObservableList<XYChart.Data<String, Double>>> series;

    public EventListener(SerialPort serialPort, List<ObservableList<XYChart.Data<String, Double>>> series) {
        this.serialPort = serialPort;
        this.series = series;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (!serialPortEvent.isRXCHAR() || serialPortEvent.getEventValue() <= 0)
            return;

        String rxData = "";
        try {
            rxData = serialPort.readString(serialPortEvent.getEventValue());

            if (!MSG_SEPARATOR.equals(rxData)) return;

            int lastNewLineIndex = DATA_BUFFER.lastIndexOf(MSG_SEPARATOR);
            if (lastNewLineIndex == -1) return;

            String message = DATA_BUFFER.substring(lastNewLineIndex + 1);
            if (!MSG_PATTERN.matcher(message).matches()) return;

            String[] msgFields = message.split(VALUES_SEPARATOR);
            String light = msgFields[0].trim();
            String potentiometer = msgFields[1].trim();

            System.out.println(potentiometer + "  " + light);

            Date currentDate = new Date();
            Platform.runLater(() -> {
                addValueToSeries(light, currentDate, 0);
                addValueToSeries(potentiometer, currentDate, 1);
            });
        } catch (SerialPortException e) {
            e.printStackTrace();
        } finally {
            DATA_BUFFER.append(rxData);
        }
    }

    private void addValueToSeries(String value, Date date, int seriesIndex) {
        if (!EMPTY_VAL.equals(value)) {
            series.get(seriesIndex).add(new XYChart.Data<>(DATE_FORMAT.format(date), Double.parseDouble(value)));
        }
    }
}
