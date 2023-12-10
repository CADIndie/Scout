package pm.c7.scout.mixin;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.c7.scout.ScoutNetworking;
import pm.c7.scout.ScoutPlayerScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.screen.BagSlot;

@Mixin({ServerPlayerEntity.class})
public class ServerPlayerEntityMixin {
   @Inject(
      method = {"onDeath"},
      at = {@At("HEAD")}
   )
   private void scout$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
      ServerPlayerEntity player = (ServerPlayerEntity)(Object) this;
      ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler)player.playerScreenHandler;
      if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
         ItemStack backStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.SATCHEL, false);
         int slots;
         if (!backStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)backStack.getItem();
            slots = bagItem.getSlotCount();
            DefaultedList<BagSlot> bagSlots = handler.scout$getSatchelSlots();

            for(slots = 0; slots < slots; ++slots) {
               BagSlot slot = (BagSlot)bagSlots.get(slots);
               slot.setInventory((Inventory) null);
               slot.setEnabled(false);
            }

            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            packet.writeBoolean(false);
            packet.writeInt(0);
            packet.writeItemStack(backStack);
            ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
         }

         ItemStack leftPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, false);
         if (!leftPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)leftPouchStack.getItem();
            slots = bagItem.getSlotCount();
            DefaultedList<BagSlot> bagSlots = handler.scout$getLeftPouchSlots();

            for(int i = 0; i < slots; ++i) {
               BagSlot slot = (BagSlot)bagSlots.get(i);
               slot.setInventory((Inventory) null);
               slot.setEnabled(false);
            }

            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            packet.writeBoolean(false);
            packet.writeInt(0);
            packet.writeItemStack(leftPouchStack);
            ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
         }

         ItemStack rightPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, true);
         if (!rightPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)rightPouchStack.getItem();
            slots = bagItem.getSlotCount();
            DefaultedList<BagSlot> bagSlots = handler.scout$getRightPouchSlots();

            for(int i = 0; i < slots; ++i) {
               BagSlot slot = (BagSlot)bagSlots.get(i);
               slot.setInventory((Inventory) null);
               slot.setEnabled(false);
            }

            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            packet.writeBoolean(false);
            packet.writeInt(1);
            packet.writeItemStack(rightPouchStack);
            ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
         }
      }

   }
}
