package cc.sukazyo.sericons.screen;

import cc.sukazyo.sericons.SeriConsMod;
import cc.sukazyo.sericons.capabilities.Capabilities;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class BodyOverlay extends GuiComponent {
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final Player player;
    private final PoseStack stack;
    TranslatableComponent c = new TranslatableComponent("gui." + SeriConsMod.MODID + ".durability");

    public BodyOverlay(Player player, PoseStack stack) {
        this.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.player = player;
        this.stack = stack;
    }

    public void render() {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        if (player.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().isPresent()) {
            drawString(stack, minecraft.font, c.getString() + ": " + player.getCapability(Capabilities.BODY_DURATION_CAPABILITY).resolve().get().durability(), 20, 30, 0xFEFEFE);
        }
    }




}
