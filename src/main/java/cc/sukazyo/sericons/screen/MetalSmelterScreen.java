package cc.sukazyo.sericons.screen;

import cc.sukazyo.sericons.inventory.MetalSmelterMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MetalSmelterScreen extends AbstractContainerScreen<MetalSmelterMenu> {

    public MetalSmelterScreen(MetalSmelterMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(PoseStack camera, float deltaTicks, int mouseX, int mouseY) {
        Canvas canvas = Canvas.getInstance();
        Paint paint = Paint.take();
        paint.reset();
        paint.setFeatherRadius(0.5f);
        paint.setStyle(Paint.Style.FILL);
        paint.setRGBA(16, 16, 16, 192);
        canvas.drawRoundRect(width / 2f - 86, height / 2f - 90, width / 2f + 86, height / 2f + 90, 5, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setRGBA(255, 255, 255, 255);
        canvas.drawRoundRect(width / 2f - 86, height / 2f - 90, width / 2f + 86, height / 2f + 90, 5, paint);
    }
}
