package cc.sukazyo.sericons.network;

import cc.sukazyo.sericons.SeriConsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkChannel {
	public static SimpleChannel INSTANCE;
	public static final String VERSION = "1.0.0";
	private static int ID = 0;

	public static int next() {
		return ID++;
	}

	public static void regPacket() {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SeriConsMod.MODID, "networking"), () -> VERSION, (v) -> v.equals(VERSION), (v) -> v.equals(VERSION));
		INSTANCE.messageBuilder(BodyDurabilityUpdatePacket.class, next()).encoder(BodyDurabilityUpdatePacket::toBytes).decoder(BodyDurabilityUpdatePacket::new).consumer(BodyDurabilityUpdatePacket::handler).add();
	}

}
