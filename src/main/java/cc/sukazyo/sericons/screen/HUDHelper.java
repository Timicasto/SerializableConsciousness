package cc.sukazyo.sericons.screen;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HUDHelper {
    @SubscribeEvent
    public static void render(RenderGameOverlayEvent e) {
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        BodyOverlay gui = new BodyOverlay(e.getMatrixStack());
        gui.render();
    }


}
