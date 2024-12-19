package com.mj.vsegamepad;

import net.minecraft.client.Minecraft;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MemoryStack;
import org.valkyrienskies.mod.common.entity.ShipMountingEntity;

import java.lang.reflect.Method;

public class GamepadManager {
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void update() {
        if (minecraft.level == null || minecraft.player == null) return;

        for (int i = GLFW.GLFW_JOYSTICK_1; i <= GLFW.GLFW_JOYSTICK_LAST; i++) {
            if (GLFW.glfwJoystickPresent(i) && GLFW.glfwJoystickIsGamepad(i)) {
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    GLFWGamepadState state = GLFWGamepadState.mallocStack(stack);
                    if (GLFW.glfwGetGamepadState(i, state)) {
                        handleGamepadInput(state);
                    }
                }
            }
        }
    }

    private static void handleGamepadInput(GLFWGamepadState state) {
        float xAxis = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X);
        float yAxis = state.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y);
        boolean ascend = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A) == GLFW.GLFW_PRESS;
        boolean descend = state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B) == GLFW.GLFW_PRESS;

        Vector3f impulse = new Vector3f();
        impulse.x = (xAxis < -0.5f) ? 1.0f : (xAxis > 0.5f ? -1.0f : 0.0f);
        impulse.z = (yAxis < -0.5f) ? 1.0f : (yAxis > 0.5f ? -1.0f : 0.0f);
        impulse.y = ascend ? 1.0f : (descend ? -1.0f : 0.0f);

        sendToShip(impulse);
    }

    private static void sendToShip(Vector3f impulse) {
        if (minecraft.player.getVehicle() instanceof ShipMountingEntity ship) {
            invokeHandleShipInput(ship, impulse);
        }
    }

    private static void invokeHandleShipInput(ShipMountingEntity ship, Vector3f impulse) {
        try {
            // Reflectively invoke the method injected by the mixin
            Method handleShipInput = ship.getClass().getMethod("handleShipInput", Vector3f.class);
            handleShipInput.invoke(ship, impulse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
