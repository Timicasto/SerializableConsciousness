package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import com.mojang.blaze3d.platform.ScreenManager;
import net.minecraft.client.gui.screens.MenuScreens;
import cc.sukazyo.sericons.screen.MetalSmelterScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID)
final class EventHandler {

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeriConsMod.MODID)
    static class Client {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(RegistryBlocks.METAL_SMELTER_MENU, MetalSmelterScreen::new));
        }
    }
}
