package com.mj.vsegamepad.mixin;

import com.mj.vsegamepad.VSKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.entity.ShipMountingEntity;
import org.valkyrienskies.mod.common.networking.PacketPlayerDriving;

@Mixin(ShipMountingEntity.class)
public abstract class MixinShipMountingEntity {


    @Inject(method = "sendDrivingPacket", at = @At("HEAD"), cancellable = true)
    private void sendDrivingPacket(CallbackInfo ci) {
        // Trick to get values from the class we mixining
        ShipMountingEntity self = ((ShipMountingEntity) (Object) this);

        if (!self.level.isClientSide()) return;

        Options opts = Minecraft.getInstance().options;
        boolean forward = opts.keyUp.isDown();
        boolean backward = opts.keyDown.isDown();
        boolean left = opts.keyLeft.isDown();
        boolean right = opts.keyRight.isDown();
        boolean up = opts.keyJump.isDown();
        boolean sprint = self.getControllingPassenger() != null && self.getControllingPassenger().isSprinting();
        boolean down = VSKeyBindings.shipDown.isDown();
        // TODO: replace me with a gamepad keybinding for cruise
        boolean cruise = org.valkyrienskies.mod.common.config.VSKeyBindings.INSTANCE.getShipCruise().get().isDown();

        Vector3f impulse = new Vector3f();
        impulse.z = (forward == backward) ? 0.0f : (forward ? 1.0f : -1.0f);
        impulse.x = (left == right) ? 0.0f : (left ? 1.0f : -1.0f);
        impulse.y = (up == down) ? 0.0f : (up ? 1.0f : -1.0f);

        // TODO: Fix me, we are too deprecated to compile :sob:
        new PacketPlayerDriving(impulse, sprint, cruise).sendToServer();
    }
}
