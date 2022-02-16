package com.example.tracing;

import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@Liveness
@ApplicationScoped
public class LivenessCheck implements AsyncHealthCheck {

    @Override
    public Uni<HealthCheckResponse> call() {
        return Uni.createFrom().item(HealthCheckResponse.up("Binocla is alive"))
                .onItem().delayIt().by(Duration.ofMillis(10));
    }

}