package pm.c7.scout.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.c7.scout.ScoutPlayerScreenHandler;
import pm.c7.scout.screen.BagSlot;

@Mixin(
   value = {PlayerScreenHandler.class},
   priority = 950
)
public abstract class PlayerScreenHandlerMixin extends ScreenHandler implements ScoutPlayerScreenHandler {
   @Shadow
   @Final
   private PlayerEntity owner;
   @Unique
   public final DefaultedList<BagSlot> satchelSlots = DefaultedList.ofSize(18);
   @Unique
   public final DefaultedList<BagSlot> leftPouchSlots = DefaultedList.ofSize(6);
   @Unique
   public final DefaultedList<BagSlot> rightPouchSlots = DefaultedList.ofSize(6);

   protected PlayerScreenHandlerMixin() {
      super((ScreenHandlerType<?>) null, 0);
   }

   @Inject(
      method = {"<init>"},
      at = {@At("RETURN")}
   )
   private void scout$addSlots(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo callbackInfo) {
      int x = 8;
      int y = 168;

      int i;
      BagSlot slot;
      for(i = 0; i < 18; ++i) {
         if (i % 9 == 0) {
            x = 8;
         }

         slot = new BagSlot(i, x, y);
         this.satchelSlots.add(slot);
         this.addSlot(slot);
         x += 18;
         if ((i + 1) % 9 == 0) {
            y += 18;
         }
      }

      x = 8;
      y = 66;

      for(i = 0; i < 6; ++i) {
         if (i % 3 == 0) {
            x -= 18;
            y += 54;
         }

         slot = new BagSlot(i, x, y);
         this.leftPouchSlots.add(slot);
         this.addSlot(slot);
         y -= 18;
      }

      x = 152;
      y = 66;

      for(i = 0; i < 6; ++i) {
         if (i % 3 == 0) {
            x += 18;
            y += 54;
         }

         slot = new BagSlot(i, x, y);
         this.rightPouchSlots.add(slot);
         this.addSlot(slot);
         y -= 18;
      }

   }

   public final DefaultedList<BagSlot> scout$getSatchelSlots() {
      return this.satchelSlots;
   }

   public final DefaultedList<BagSlot> scout$getLeftPouchSlots() {
      return this.leftPouchSlots;
   }

   public final DefaultedList<BagSlot> scout$getRightPouchSlots() {
      return this.rightPouchSlots;
   }
}
