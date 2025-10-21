package kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import model.MetricData;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;
import java.util.Random;

@ApplicationScoped
public class MetricProducer {

    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    @Outgoing("metrics-out")
    public Multi<String> generateMetrics() {
        System.out.println("🚀 Metric producer starting...");

        return Multi.createFrom().ticks().every(Duration.ofSeconds(2))
                .map(tick -> {
                    String[] types = {"cpu", "memory", "disk", "network"};
                    String type = types[random.nextInt(types.length)];
                    double value = 60 + (random.nextDouble() * 40);

                    MetricData metric = new MetricData(
                            "metric-" + tick,
                            type,
                            value,
                            "%"
                    );

                    System.out.printf("📤 Producing: %s = %.2f%n", type, value);

                    try {
                        return mapper.writeValueAsString(metric);
                    } catch (Exception e) {
                        return "{}";
                    }
                });
    }
}
