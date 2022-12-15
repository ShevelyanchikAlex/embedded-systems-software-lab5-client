package by.bsuir.povs.lab5.service.config;

import by.bsuir.povs.lab5.service.listener.PortEventListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.Closeable;
import java.util.List;

public class SerialPortConfig implements Closeable {
    private final static String COM_PORT = "COM4";
    public static SerialPort serialPort;

    public static void configSerialPort(List<ObservableList<XYChart.Data<String, Double>>> series) {
        serialPort = new SerialPort(COM_PORT);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(new PortEventListener(serialPort, series), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        {
            if (serialPort.isOpened()) {
                try {
                    serialPort.removeEventListener();
                    serialPort.closePort();
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
