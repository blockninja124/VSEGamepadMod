package com.mj.vsegamepad.mixin;

import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.entity.ShipMountingEntity;
import org.valkyrienskies.mod.common.networking.PacketPlayerDriving;

import static org.valkyrienskies.core.impl.networking.simple.SimplePackets.sendToServer;

@Mixin(ShipMountingEntity.class)
public abstract class MixinShipMountingEntity {

    @Inject(method = "sendDrivingPacket", at = @At("HEAD"), cancellable = true, remap = false)
    private void sendDrivingPacket(CallbackInfo ci) {
        // Cancel the default packet sending logic
        ci.cancel();
    }

    // Add a method to handle custom ship movement input
    public void handleShipInput(Vector3f impulse) {
        sendToServer(new PacketPlayerDriving(impulse, false, false));
    }
}
