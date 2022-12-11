package by.bsuir.povs.lab5.service.impl;

import by.bsuir.povs.lab5.model.ChartModel;
import by.bsuir.povs.lab5.service.ChartService;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ChartServiceImpl implements ChartService {

    @Override
    public ChartModel initCharts(ChartModel chartModel) {
        List<LineChart<String, Double>> charts = chartModel.getCharts();
        List<ObservableList<XYChart.Data<String, Double>>> series = chartModel.getSeries();
        series.add(registerObservableSeries(charts.get(0), "Light value"));
        series.add(registerObservableSeries(charts.get(1), "Potentiometer value"));
        return chartModel;
    }

    private ObservableList<XYChart.Data<String, Double>> registerObservableSeries(LineChart<String, Double> chart, String seriesName) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(seriesName);
        chart.getData().add(series);
        return series.getData();
    }
}
