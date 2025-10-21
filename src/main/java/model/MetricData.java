package model;

import java.time.Instant;

public class MetricData {
    private String id;
    private String metricType;
    private double value;
    private String unit;
    private Instant timestamp;

    // Default constructor for Jackson
    public MetricData() {
        this.timestamp = Instant.now();
    }

    // Full constructor
    public MetricData(String id, String metricType, double value, String unit) {
        this.id = id;
        this.metricType = metricType;
        this.value = value;
        this.unit = unit;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "MetricData{metricType='" + metricType + "', value=" + value + ", unit='" + unit + "'}";
    }
}
