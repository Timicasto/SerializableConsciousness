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
        if (Minecraft.getInstance().player == null || !Minecraft.getInstance().player.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().isPresent() || Minecraft.getInstance().player.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability() == 0) {
            return;
        }
        BodyOverlay gui = new BodyOverlay(Minecraft.getInstance().player, e.getMatrixStack());
        gui.render();
    }

}
