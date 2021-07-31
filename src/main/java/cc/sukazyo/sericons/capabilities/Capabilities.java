package cc.sukazyo.sericons.capabilities;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.network.BodyDurabilityUpdatePacket;
import cc.sukazyo.sericons.network.NetworkChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber()
public class Capabilities {
    @CapabilityInject(IBodyDurabilityCapability.class)
    public static Capability<IBodyDurabilityCapability> BODY_DURATION_CAPABILITY;

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent e) {
        e.enqueueWork(() -> {
            CapabilityManager.INSTANCE.register(IBodyDurabilityCapability.class, new Capability.IStorage<IBodyDurabilityCapability>() {
                @Nullable
                @Override
                public Tag writeNBT(Capability<IBodyDurabilityCapability> capability, IBodyDurabilityCapability object, Direction arg) {
                    return null;
                }

                @Override
                public void readNBT(Capability<IBodyDurabilityCapability> capability, IBodyDurabilityCapability object, Direction arg, Tag arg2) {

                }
            }, () -> null);
        });
    }

    @SubscribeEvent
    public static void attachBody(AttachCapabilitiesEvent<Entity> e) {
        Entity entity = e.getObject();
        if (entity instanceof Player) {
            e.addCapability(new ResourceLocation(SeriConsMod.MODID, "body_durability"), new BodyDurabilityCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone e) {
        if (!e.isWasDeath() && e.getPlayer().getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().isPresent()) {
            e.getPlayer().getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().setDurability(1D);
            NetworkChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)e.getPlayer()), new BodyDurabilityUpdatePacket(e.getPlayer().getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability()));
        }
    }
}
