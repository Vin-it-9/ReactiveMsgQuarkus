package model;

public class Alert {
    private String userId;
    private String metricType;
    private double currentValue;
    private double minValue;
    private double maxValue;
    private String message;
    private long timestamp;

    public Alert() {
        this.timestamp = System.currentTimeMillis();
    }

    public Alert(String userId, String metricType, double currentValue, double minValue, double maxValue) {
        this.userId = userId;
        this.metricType = metricType;
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.message = metricType + " value " + currentValue + " is out of range [" + minValue + " - " + maxValue + "]";
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Alert{userId='" + userId + "', metricType='" + metricType +
                "', currentValue=" + currentValue + ", range=[" + minValue + "-" + maxValue + "]}";
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }
    public double getMinValue() { return minValue; }
    public void setMinValue(double minValue) { this.minValue = minValue; }
    public double getMaxValue() { return maxValue; }
    public void setMaxValue(double maxValue) { this.maxValue = maxValue; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
