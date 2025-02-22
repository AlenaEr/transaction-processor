package org.example.transactionprocessor;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionProcessorApplication {

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    public static void main(String[] args) {
        SpringApplication.run(TransactionProcessorApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Metrics Endpoint: " + metricsEndpoint);
    }
}
