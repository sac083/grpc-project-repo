package com.inspiron.labs.app.client;


import com.inspiron.notification.NotificationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationServiceClient {
    // This method creates and configures a gRPC blocking stub for communication with the NotificationService.
    // A blocking stub is a synchronous client stub that blocks the caller until the RPC call is complete.
    // It's used when the client needs to wait for the server's response before proceeding.
    @Bean
    public NotificationServiceGrpc.NotificationServiceBlockingStub studentServiceBlockingStub() {
        // Create a managed channel to connect to the gRPC server running on localhost and port 9091.
        // ManagedChannelBuilder is used to configure the channel.
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
                .usePlaintext()// Use plaintext communication (without encryption) for simplicity.
                .build();// Build the channel.
        // Create a new blocking stub for the NotificationService using the configured channel.
        // This stub will be used by the client to make RPC calls to the NotificationService.
        return NotificationServiceGrpc.newBlockingStub(channel);
    }
}
