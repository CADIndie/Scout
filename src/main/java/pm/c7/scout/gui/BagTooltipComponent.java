package pm.c7.scout.gui;

import com.google.common.math.IntMath;
import com.mojang.blaze3d.systems.RenderSystem;
import java.math.RoundingMode;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BagTooltipData;

public class BagTooltipComponent implements TooltipComponent {
   private final DefaultedList<ItemStack> inventory;
   private final int slotCount;

   public BagTooltipComponent(BagTooltipData data) {
      this.inventory = data.getInventory();
      this.slotCount = data.getSlotCount();
   }

   public int getHeight() {
      return 18 * IntMath.divide(this.slotCount, 6, RoundingMode.UP) + 2;
   }

   public int getWidth(TextRenderer textRenderer) {
      return 18 * (this.slotCount < 6 ? this.slotCount : 6);
   }

   public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
      int originalX = x;

      for(int i = 0; i < this.slotCount; ++i) {
         ItemStack itemStack = (ItemStack)this.inventory.get(i);
         this.drawSlot(matrices, x, y, z);
         //itemRenderer.method_32797(itemStack, x + 1, y + 1, i);
         //itemRenderer.method_4025(textRenderer, itemStack, x + 1, y + 1);
         x += 18;
         if ((i + 1) % 6 == 0) {
            y += 18;
            x = originalX;
         }
      }

   }

   private void drawSlot(MatrixStack matrices, int x, int y, int z) {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
      //class_332.method_25291(matrices, x, y, z, 7.0F, 7.0F, 18, 18, 256, 256);
   }
}
