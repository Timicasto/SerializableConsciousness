package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.screen.MetalSmelterScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID)
public final class EventHandler {

    @SubscribeEvent
    public static void onLoad(FMLLoadCompleteEvent event) {
        MetalSmelterRecipe.register(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Items.COAL, 1), 600);
    }

    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent event) {
        MetalSmelterRecipe.register(new ItemStack(Items.IRON_INGOT, 8), new ItemStack(Blocks.IRON_ORE, 1), new ItemStack(Items.GLOWSTONE_DUST, 1), new ItemStack(Items.COAL, 1), 600);
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeriConsMod.MODID)
    static class Client {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(RegistryBlocks.METAL_SMELTER_MENU, MetalSmelterScreen::new);

            });
        }
    }
}
