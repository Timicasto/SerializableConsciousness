package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriCons;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SeriCons.MODID)
final class EventHandler {

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeriCons.MODID)
    static class Client {

    }
}
