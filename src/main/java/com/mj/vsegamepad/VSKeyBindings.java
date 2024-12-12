package com.mj.vsegamepad;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class VSKeyBindings {
    public static final KeyMapping shipUp = new KeyMapping(
            "key.vsegamepad.ship_up", // Forward
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "key.categories.vsegamepad"
    );

    public static final KeyMapping shipDown = new KeyMapping(
            "key.vsegamepad.ship_down", // Backward
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.vsegamepad"
    );

    public static final KeyMapping shipLeft = new KeyMapping(
            "key.vsegamepad.ship_left", // Left
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "key.categories.vsegamepad"
    );

    public static final KeyMapping shipRight = new KeyMapping(
            "key.vsegamepad.ship_right", // Right
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "key.categories.vsegamepad"
    );

    public static void register() {
        ClientRegistry.registerKeyBinding(shipUp);
        ClientRegistry.registerKeyBinding(shipDown);
        ClientRegistry.registerKeyBinding(shipLeft);
        ClientRegistry.registerKeyBinding(shipRight);
    }
}
