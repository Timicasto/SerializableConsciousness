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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BodyOverlay extends GuiComponent {
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final PoseStack stack;
    public static double value;
    TranslatableComponent c = new TranslatableComponent("gui." + SeriConsMod.MODID + ".durability");

    public BodyOverlay(PoseStack stack) {
        this.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.stack = stack;
    }

    public void render() {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        BigDecimal i = BigDecimal.valueOf(value);
        i = i.setScale(2, RoundingMode.HALF_EVEN);
        drawString(stack, minecraft.font, c.getString() + ": " + i.toString(), 20, 30, 0xFEFEFE);
    }




}
