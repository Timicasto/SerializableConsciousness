package quantumstudio.serializableconsciousness;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import quantumstudio.serializableconsciousness.world.OreGen;

@Mod.EventBusSubscriber(modid = ContentHandler.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {

    @SubscribeEvent
    public static void onCommonStart(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OreGen.initFeatures();
            ContentHandler.logger.info("Registered OreGenFeatures");
        });
    }
}
