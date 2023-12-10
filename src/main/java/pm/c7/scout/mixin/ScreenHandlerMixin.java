package pm.c7.scout.mixin;

import com.google.common.collect.Table;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ScreenHandler.class})
public class ScreenHandlerMixin {
   @Redirect(
      method = {"copySharedSlots"},
      at = @At(
   value = "INVOKE",
   target = "Lcom/google/common/collect/Table;put(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
   remap = false
)
   )
   private Object scout$skipNullInventories1(Table<Inventory, Integer, Integer> self, Object inventory, Object index, Object size) {
      return inventory == null ? null : self.put((Inventory) inventory, (Integer)index, (Integer)size);
   }

   @Redirect(
      method = {"copySharedSlots"},
      at = @At(
   value = "INVOKE",
   target = "Lcom/google/common/collect/Table;get(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
   remap = false
)
   )
   private Object scout$skipNullInventories2(Table<Inventory, Integer, Integer> self, Object inventory, Object index) {
      return inventory == null ? null : self.get(inventory, (Integer)index);
   }
}
