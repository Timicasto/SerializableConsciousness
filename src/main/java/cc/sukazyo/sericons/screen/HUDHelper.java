package cc.sukazyo.sericons.screen;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.capabilities.Capabilities;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HUDHelper {
    public static boolean display = false;

    @SubscribeEvent
    public static void render(RenderGameOverlayEvent e) {
        if (display) {
            if (e.getType() != RenderGameOverlayEvent.ElementType.ALL) {
                return;
            }
            BodyOverlay gui = new BodyOverlay(e.getMatrixStack());
            gui.render();
        }
    }

    public static final KeyMapping DISPLAY_KEY = new KeyMapping("key.display", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.category." + SeriConsMod.MODID);

    @SubscribeEvent
    public static void keyDown(InputEvent.KeyInputEvent e) {
        if (DISPLAY_KEY.isDown()) {
            display = !display;
        }
    }
}
