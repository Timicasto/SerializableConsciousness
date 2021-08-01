package cc.sukazyo.sericons.render;

import cc.sukazyo.sericons.register.RegistryFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderHelper {

	@SubscribeEvent
	public static void renderType(FMLClientSetupEvent e) {
		e.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(RegistryFluids.STEAM_FLUID, RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(RegistryFluids.STEAM_FLUID_FLOWING, RenderType.translucent());
		});
	}
}
