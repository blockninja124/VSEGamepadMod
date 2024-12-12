package com.mj.vsegamepad.mixin;

import com.mj.vsegamepad.VSKeyBindings;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.entity.ShipMountingEntity;
import org.valkyrienskies.mod.common.networking.PacketPlayerDriving;
import org.valkyrienskies.mod.common.vsCore;

@Mixin(ShipMountingEntity.class)
public abstract class MixinShipMountingEntity {

    @Inject(method = "sendDrivingPacket", at = @At("HEAD"), cancellable = true)
    private void sendDrivingPacket(CallbackInfo ci) {
        if (!Minecraft.getInstance().level.isClientSide) {
            ci.cancel();
            return;
        }

        Options opts = Minecraft.getInstance().options;
        boolean forward = VSKeyBindings.shipUp.isDown();
        boolean backward = VSKeyBindings.shipDown.isDown();
        boolean left = VSKeyBindings.shipLeft.isDown();
        boolean right = VSKeyBindings.shipRight.isDown();

        Vector3f impulse = new Vector3f();
        impulse.set(
                left == right ? 0.0f : (left ? -1.0f : 1.0f),
                0.0f,
                forward == backward ? 0.0f : (forward ? 1.0f : -1.0f)
        );

        with(vsCore.simplePacketNetworking) {
            PacketPlayerDriving packet = new PacketPlayerDriving(impulse, false, false);
            packet.sendToServer();
        }

        ci.cancel();
    }
}
