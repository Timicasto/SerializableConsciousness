package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID)
final class EventHandler {

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeriConsMod.MODID)
    static class Client {

    }
}
