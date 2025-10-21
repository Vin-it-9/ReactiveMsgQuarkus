package service;


import jakarta.enterprise.context.ApplicationScoped;
import model.Range;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class RangeService {

    private final Map<String, Range> userRanges = new ConcurrentHashMap<>();

    public void setRange(String userId, Range range) {
        userRanges.put(userId, range);
        System.out.println("✅ Range configured: " + userId + " -> " + range.getMetricType() + " [" + range.getMinValue() + ", " + range.getMaxValue() + "]");
    }

    public Range getRange(String userId) {
        return userRanges.get(userId);
    }

    public void removeRange(String userId) {
        userRanges.remove(userId);
    }

    public Map<String, Range> getAllRanges() {
        return new ConcurrentHashMap<>(userRanges);
    }
}
