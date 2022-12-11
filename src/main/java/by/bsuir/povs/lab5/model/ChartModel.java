package by.bsuir.povs.lab5.model;

import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChartModel {
    private List<LineChart<String, Double>> charts;
    private List<ObservableList<XYChart.Data<String, Double>>> series;
}