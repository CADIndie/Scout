package pm.c7.scout.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;

@Mixin({InventoryScreen.class})
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
   private InventoryScreenMixin() {
      super((PlayerScreenHandler) null, (PlayerInventory)null, (Text)null);
   }

   @Inject(
      method = {"drawBackground"},
      at = {@At("HEAD")}
   )
   private void scout$drawSatchelRow(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
      if (this.client != null && this.client.player != null) {
         ItemStack backStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.SATCHEL, false);
         if (!backStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)backStack.getItem();
            int slots = bagItem.getSlotCount();
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int x = this.x;
            int y = this.y + this.backgroundHeight - 3;
            this.drawTexture(matrices, x, y, 0, 32, this.backgroundWidth, 4);
            y += 4;
            int u = 0;
            int v = 36;

            for(int slot = 0; slot < slots; ++slot) {
               if (slot % 9 == 0) {
                  x = this.x;
                  u = 0;
                  this.drawTexture(matrices, x, y, u, v, 7, 18);
                  x += 7;
                  u = u + 7;
               }

               this.drawTexture(matrices, x, y, u, v, 18, 18);
               x += 18;
               u += 18;
               if ((slot + 1) % 9 == 0) {
                  this.drawTexture(matrices, x, y, u, v, 7, 18);
                  y += 18;
               }
            }

            x = this.x;
            this.drawTexture(matrices, x, y, 0, 54, this.backgroundWidth, 7);
         }
      }

   }

   @Inject(
      method = {"drawBackground"},
      at = {@At("RETURN")}
   )
   private void scout$drawPouchSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
      if (this.client != null && this.client.player != null) {
         ItemStack leftPouchStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.POUCH, false);
         int columns;
         int x;
         int y;
         if (!leftPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)leftPouchStack.getItem();
            int slots = bagItem.getSlotCount();
            columns = (int)Math.ceil((double)(slots / 3));
            columns = this.x;
            x = this.y;
            x += 137;
            RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexture(matrices, columns, x, 18, 25, 7, 7);

            for(y = 0; y < columns; ++y) {
               columns -= 11;
               this.drawTexture(matrices, columns, x, 7, 25, 11, 7);
            }

            if (columns > 1) {
               for(y = 0; y < columns - 1; ++y) {
                  columns -= 7;
                  this.drawTexture(matrices, columns, x, 7, 25, 7, 7);
               }
            }

            columns -= 7;
            this.drawTexture(matrices, columns, x, 0, 25, 7, 7);
            columns = this.x + 7;
            x -= 54;

            for(y = 0; y < slots; ++y) {
               if (y % 3 == 0) {
                  columns -= 18;
                  x += 54;
               }

               x -= 18;
               this.drawTexture(matrices, columns, x, 7, 7, 18, 18);
            }

            columns -= 7;
            x += 54;

            for(y = 0; y < 3; ++y) {
               x -= 18;
               this.drawTexture(matrices, columns, x, 0, 7, 7, 18);
            }

            columns = this.x;
            x -= 7;
            this.drawTexture(matrices, columns, x, 18, 0, 7, 7);

            for(y = 0; y < columns; ++y) {
               columns -= 11;
               this.drawTexture(matrices, columns, x, 7, 0, 11, 7);
            }

            if (columns > 1) {
               for(y = 0; y < columns - 1; ++y) {
                  columns -= 7;
                  this.drawTexture(matrices, columns, x, 7, 0, 7, 7);
               }
            }

            columns -= 7;
            this.drawTexture(matrices, columns, x, 0, 0, 7, 7);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         }

         ItemStack rightPouchStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.POUCH, true);
         if (!rightPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)rightPouchStack.getItem();
            columns = bagItem.getSlotCount();
            columns = (int)Math.ceil((double)(columns / 3));
            x = this.x;
            y = this.y;
            x += this.backgroundWidth - 7;
            y += 137;
            RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexture(matrices, x, y, 25, 25, 7, 7);
            x += 7;

            int i;
            for(i = 0; i < columns; ++i) {
               this.drawTexture(matrices, x, y, 7, 25, 11, 7);
               x += 11;
            }

            if (columns > 1) {
               for(i = 0; i < columns - 1; ++i) {
                  this.drawTexture(matrices, x, y, 7, 25, 7, 7);
                  x += 7;
               }
            }

            this.drawTexture(matrices, x, y, 32, 25, 7, 7);
            x = this.x + this.backgroundWidth - 25;
            y -= 54;

            for(i = 0; i < columns; ++i) {
               if (i % 3 == 0) {
                  x += 18;
                  y += 54;
               }

               y -= 18;
               this.drawTexture(matrices, x, y, 7, 7, 18, 18);
            }

            x += 18;
            y += 54;

            for(i = 0; i < 3; ++i) {
               y -= 18;
               this.drawTexture(matrices, x, y, 32, 7, 7, 18);
            }

            x = this.x + this.backgroundWidth - 7;
            y -= 7;
            this.drawTexture(matrices, x, y, 25, 0, 7, 7);
            x += 7;

            for(i = 0; i < columns; ++i) {
               this.drawTexture(matrices, x, y, 7, 0, 11, 7);
               x += 11;
            }

            if (columns > 1) {
               for(i = 0; i < columns - 1; ++i) {
                  this.drawTexture(matrices, x, y, 7, 0, 7, 7);
                  x += 7;
               }
            }

            this.drawTexture(matrices, x, y, 32, 0, 7, 7);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }

   }

   @Inject(
      method = {"isClickOutsideBounds"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void scout$adjustOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
      if (this.client != null && this.client.player != null) {
         ItemStack backStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.SATCHEL, false);
         int slots;
         if (!backStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)backStack.getItem();
            slots = bagItem.getSlotCount();
            slots = (int)Math.ceil((double)(slots / 9));
            if (mouseY < (double)(top + this.backgroundHeight + 8 + 18 * slots) && mouseY >= (double)(top + this.backgroundHeight) && mouseX >= (double)left && mouseY < (double)(left + this.backgroundWidth)) {
               callbackInfo.setReturnValue(false);
            }
         }

         ItemStack leftPouchStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.POUCH, false);
         if (!leftPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)leftPouchStack.getItem();
            slots = bagItem.getSlotCount();
            slots = (int)Math.ceil((double)(slots / 3));
            if (mouseX >= (double)(left - slots * 18) && mouseX < (double)left && mouseY >= (double)(top + this.backgroundHeight - 90) && mouseY < (double)(top + this.backgroundHeight - 22)) {
               callbackInfo.setReturnValue(false);
            }
         }

         ItemStack rightPouchStack = ScoutUtil.findBagItem(this.client.player, BaseBagItem.BagType.POUCH, true);
         if (!rightPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)rightPouchStack.getItem();
            slots = bagItem.getSlotCount();
            int columns = (int)Math.ceil((double)(slots / 3));
            if (mouseX >= (double)(left + this.backgroundWidth) && mouseX < (double)(left + this.backgroundWidth + columns * 18) && mouseY >= (double)(top + this.backgroundHeight - 90) && mouseY < (double)(top + this.backgroundHeight - 22)) {
               callbackInfo.setReturnValue(false);
            }
         }
      }

   }
}
