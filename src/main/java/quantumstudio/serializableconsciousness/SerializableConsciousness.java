package quantumstudio.serializableconsciousness;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ContentHandler.MODID)
public class SerializableConsciousness {
    public SerializableConsciousness() {
        ContentHandler.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ContentHandler.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
