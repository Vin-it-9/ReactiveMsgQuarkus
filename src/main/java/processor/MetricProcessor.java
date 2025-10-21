package processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Alert;
import model.MetricData;
import model.Range;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import service.RangeService;

@ApplicationScoped
public class MetricProcessor {

    @Inject
    RangeService rangeService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Incoming("metrics-in")
    @Outgoing("alerts-out")
    @Broadcast
    public Alert processMetric(String message) {
        System.out.println("\n=== PROCESSING MESSAGE ===");
        System.out.println("RAW: " + message);

        try {
            MetricData metric = mapper.readValue(message, MetricData.class);
            System.out.println("PARSED: " + metric.getMetricType() + " = " + metric.getValue());

            System.out.println("Getting ranges from service...");
            var allRanges = rangeService.getAllRanges();
            System.out.println("RANGES MAP SIZE: " + allRanges.size());
            System.out.println("RANGES MAP CONTENTS: " + allRanges);

            if (allRanges.isEmpty()) {
                System.out.println("❌ NO RANGES IN MAP!");
                return null;
            }

            for (var entry : allRanges.entrySet()) {
                String userId = entry.getKey();
                Range config = entry.getValue();

                System.out.println("Checking range for user: " + userId);
                System.out.println("  Config metric type: " + config.getMetricType());
                System.out.println("  Incoming metric type: " + metric.getMetricType());
                System.out.println("  Min: " + config.getMinValue() + ", Max: " + config.getMaxValue());
                System.out.println("  Current value: " + metric.getValue());

                boolean typeMatches = config.getMetricType().equals(metric.getMetricType());
                boolean outOfRange = config.isOutOfRange(metric.getValue());

                System.out.println("  Type matches: " + typeMatches);
                System.out.println("  Out of range: " + outOfRange);

                if (typeMatches && outOfRange) {
                    System.out.println("🚨 CREATING ALERT!");

                    Alert alert = new Alert(
                            userId,
                            metric.getMetricType(),
                            metric.getValue(),
                            config.getMinValue(),
                            config.getMaxValue()
                    );

                    System.out.println("ALERT OBJECT: " + alert);
                    return alert;
                }
            }

            System.out.println("✅ No alert needed");

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== END PROCESSING ===\n");
        return null;
    }
}
