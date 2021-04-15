package io.github.robit.agones_spigot;

import agones.dev.sdk.Agones;
import agones.dev.sdk.SDKGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    ManagedChannel channel;
    SDKGrpc.SDKBlockingStub blockingStub;
    SDKGrpc.SDKStub stub;
    Agones.Empty empty = Agones.Empty.newBuilder().build();

    @Override
    public void onLoad() {
        String port = "";
        port = System.getenv("AGONES_SDK_GRPC_PORT");
        channel = ManagedChannelBuilder.forAddress("http://localhost", Integer.parseInt(port)).build();
        blockingStub = SDKGrpc.newBlockingStub(channel);
        stub = SDKGrpc.newStub(channel);
    }

    @Override
    public void onEnable() {
        blockingStub.ready(empty);
        new BukkitRunnable() {
            @Override
            public void run() {
                stub.health(null);
            }
        }.runTaskTimer(this, 0, 100);
    }

    @Override
    public void onDisable() {
        if(channel != null) {
            blockingStub.shutdown(empty);
        }
    }
}
