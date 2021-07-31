package cc.sukazyo.sericons.register;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.capabilities.Capabilities;
import cc.sukazyo.sericons.crafting.MetalSmelterRecipe;
import cc.sukazyo.sericons.network.BodyDurabilityUpdatePacket;
import cc.sukazyo.sericons.network.NetworkChannel;
import cc.sukazyo.sericons.screen.MetalSmelterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = SeriConsMod.MODID)
public final class EventHandler {

    @SubscribeEvent
    public static void hurt(LivingHurtEvent e) {
        if (e.getEntityLiving() instanceof Player) {
            if (e.getSource() != DamageSource.OUT_OF_WORLD) {
                Player p = (Player) e.getEntityLiving();
                if (p.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().isPresent()) {
                    double d = p.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability();
                    p.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().setDurability(d - (e.getAmount() / 100));
                    NetworkChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) p), new BodyDurabilityUpdatePacket(p.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability()));
                    p.setHealth(p.getHealth() + e.getAmount());
                    if (p.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability() <= 0) {
                        if (!p.isDeadOrDying()) {
                            p.kill();
                        }
                    }
                }
            }
        }
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

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getPlayer() instanceof ServerPlayer) {
            NetworkChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) e.getPlayer()), new BodyDurabilityUpdatePacket(e.getPlayer().getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability()));
        } else {
            Component c = new TextComponent(I18n.get("message.serializableconsciousness.login"));
            e.getPlayer().sendMessage(c, UUID.randomUUID());
        }
    }
}
