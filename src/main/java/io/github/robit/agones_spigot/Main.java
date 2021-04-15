package io.github.robit.agones_spigot;

import agones.dev.sdk.Agones;
import agones.dev.sdk.SDKGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    ManagedChannel channel;
    SDKGrpc.SDKBlockingStub blockingStub;
    SDKGrpc.SDKStub stub;
    Agones.Empty empty = Agones.Empty.newBuilder().build();
    io.grpc.stub.StreamObserver<Agones.Empty> healthCheck;


    @Override
    public void onLoad() {
        String port = "";
        port = System.getenv("AGONES_SDK_GRPC_PORT");
        channel = ManagedChannelBuilder.forTarget("localhost:" + port).usePlaintext().build();
        blockingStub = SDKGrpc.newBlockingStub(channel);
        stub = SDKGrpc.newStub(channel);
    }

    @Override
    public void onEnable() {
        blockingStub.ready(empty);
        healthCheck = stub.health(new ServerCallStreamObserver<Agones.Empty>() {
            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public void setOnCancelHandler(Runnable runnable) {

            }

            @Override
            public void setCompression(String s) {

            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setOnReadyHandler(Runnable runnable) {

            }

            @Override
            public void request(int i) {

            }

            @Override
            public void setMessageCompression(boolean b) {

            }

            @Override
            public void disableAutoInboundFlowControl() {

            }

            @Override
            public void onNext(Agones.Empty empty) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                healthCheck.onNext(empty);
            }
        }.runTaskTimer(this, 0, 100);
    }

    @Override
    public void onDisable() {
        if(channel != null) {
            blockingStub.shutdown(empty);
            channel.shutdown();
        }
    }
}
