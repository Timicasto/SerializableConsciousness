package cc.sukazyo.sericons.network;

import cc.sukazyo.sericons.screen.BodyOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BodyDurabilityUpdatePacket {
	private final double value;

	public BodyDurabilityUpdatePacket(FriendlyByteBuf buf) {
		value = buf.readDouble();
	}

	public BodyDurabilityUpdatePacket(double v) {
		this.value = v;
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeDouble(value);
	}

	public void handler(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> BodyOverlay.value = this.value);
		ctx.get().setPacketHandled(true);
	}

}
