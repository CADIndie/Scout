package pm.c7.scout.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import pm.c7.scout.item.BaseBagItem;

public class BagSlot extends Slot {
   private final int index;
   public Inventory inventory;
   private boolean enabled = false;

   public BagSlot(int index, int x, int y) {
      super((Inventory) null, index, x, y);
      this.index = index;
   }

   public void setInventory(Inventory inventory) {
      this.inventory = inventory;
   }

   public void setEnabled(boolean state) {
      this.enabled = state;
   }

   public boolean method_7680(ItemStack stack) {
      if (stack.getItem() instanceof BaseBagItem) {
         return false;
      } else {
         return this.enabled && this.inventory != null;
      }
   }

   public boolean method_7674(PlayerEntity playerEntity) {
      return this.enabled && this.inventory != null;
   }

   public boolean method_7682() {
      return this.enabled && this.inventory != null;
   }

   public ItemStack method_7677() {
      return this.enabled && this.inventory != null ? this.inventory.getStack(this.index) : ItemStack.EMPTY;
   }

   public void method_7673(ItemStack stack) {
      if (this.enabled && this.inventory != null) {
         this.inventory.setStack(this.index, stack);
         this.method_7668();
      }

   }

   public void method_44206(ItemStack stack) {
      if (this.enabled && this.inventory != null) {
         this.inventory.setStack(this.index, stack);
         this.method_7668();
      }

   }

   public void method_7668() {
      if (this.enabled && this.inventory != null) {
         this.inventory.markDirty();
      }

   }

   public ItemStack method_7671(int amount) {
      return this.enabled && this.inventory != null ? this.inventory.removeStack(this.index, amount) : ItemStack.EMPTY;
   }

   public int method_7675() {
      return this.enabled && this.inventory != null ? this.inventory.getMaxCountPerStack() : 0;
   }
}
