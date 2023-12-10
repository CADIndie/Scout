package pm.c7.scout.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pm.c7.scout.item.BaseBagItem;

@Mixin({ItemStack.class})
public class ItemStackMixin {
   @Inject(
      method = {"isItemEqual"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void scout$grossTrinketsEquipFix(ItemStack newStack, CallbackInfoReturnable<Boolean> callbackInfo) {
      ItemStack self = (ItemStack);
      if (self.getItem() instanceof BaseBagItem && newStack.getItem() instanceof BaseBagItem) {
         callbackInfo.setReturnValue(false);
      }

   }
}
