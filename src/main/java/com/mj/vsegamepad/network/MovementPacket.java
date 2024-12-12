package com.mj.vsegamepad.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MovementPacket {
    private final String movement;

    public MovementPacket(String movement) {
        this.movement = movement;
    }

    public MovementPacket(FriendlyByteBuf buf) {
        this.movement = buf.readUtf(16);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(movement);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            System.out.println("Received movement command: " + movement);
        });
        context.setPacketHandled(true);
    }
}
