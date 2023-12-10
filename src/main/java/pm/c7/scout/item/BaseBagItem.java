package pm.c7.scout.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.Optional;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Formatting;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.World;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.client.item.TooltipData;
import org.jetbrains.annotations.Nullable;
import pm.c7.scout.ScoutNetworking;
import pm.c7.scout.ScoutPlayerScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.screen.BagSlot;

public class BaseBagItem extends TrinketItem {
   private static final String ITEMS_KEY = "Items";
   private final int slots;
   private final BaseBagItem.BagType type;

   public BaseBagItem(Settings settings, int slots, BaseBagItem.BagType type) {
      super(settings);
      if (type == BaseBagItem.BagType.SATCHEL && slots > 18) {
         throw new IllegalArgumentException("Satchel has too many slots.");
      } else if (type == BaseBagItem.BagType.POUCH && slots > 6) {
         throw new IllegalArgumentException("Pouch has too many slots.");
      } else {
         this.slots = slots;
         this.type = type;
      }
   }

   public int getSlotCount() {
      return this.slots;
   }

   public BaseBagItem.BagType getType() {
      return this.type;
   }

   public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
      super.appendTooltip(stack, world, tooltip, context);
      tooltip.add(Text.translatable("tooltip.scout.slots", new Object[]{Text.literal(String.valueOf(this.slots)).formatted(Formatting.BLUE)}).formatted(Formatting.GRAY));
   }

   public Inventory getInventory(ItemStack stack) {
      SimpleInventory inventory = new SimpleInventory(this.slots) {
         public void markDirty() {
            stack.getOrCreateNbt().put("Items", ScoutUtil.inventoryToTag(this));
            super.markDirty();
         }
      };
      NbtCompound compound = stack.getOrCreateNbt();
      if (!compound.contains("Items")) {
         compound.put("Items", new NbtList());
      }

      NbtList items = compound.getList("Items", 10);
      ScoutUtil.inventoryFromTag(items, inventory);
      return inventory;
   }

   public Optional<TooltipData> getTooltipData(ItemStack stack) {
      DefaultedList<ItemStack> stacks = DefaultedList.of();
      Inventory inventory = this.getInventory(stack);

      for(int i = 0; i < this.slots; ++i) {
         stacks.add(inventory.getStack(i));
      }

      return stacks.stream().allMatch(ItemStack::isEmpty) ? Optional.empty() : Optional.of(new BagTooltipData(stacks, this.slots));
   }

   public void onEquip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
      PlayerEntity player = (PlayerEntity)entity;
      ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler)player.playerScreenHandler;
      ItemStack satchelStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.SATCHEL, false);
      DefaultedList<BagSlot> satchelSlots = handler.scout$getSatchelSlots();

      for(int i = 0; i < 18; ++i) {
         BagSlot slot = (BagSlot)satchelSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }

      int i;
      BagSlot slot;
      if (!satchelStack.isEmpty()) {
         BaseBagItem satchelItem = (BaseBagItem)satchelStack.getItem();
         Inventory satchelInv = satchelItem.getInventory(satchelStack);

         for(i = 0; i < satchelItem.getSlotCount(); ++i) {
            slot = (BagSlot)satchelSlots.get(i);
            slot.setInventory(satchelInv);
            slot.setEnabled(true);
         }
      }

      ItemStack leftPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, false);
      DefaultedList<BagSlot> leftPouchSlots = handler.scout$getLeftPouchSlots();

      for(i = 0; i < 6; ++i) {
         slot = (BagSlot)leftPouchSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }


      if (!leftPouchStack.isEmpty()) {
         BaseBagItem leftPouchItem = (BaseBagItem)leftPouchStack.getItem();
         Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack);

         for(i = 0; i < leftPouchItem.getSlotCount(); ++i) {
            slot = (BagSlot)leftPouchSlots.get(i);
            slot.setInventory(leftPouchInv);
            slot.setEnabled(true);
         }
      }

      ItemStack rightPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, true);
      DefaultedList<BagSlot> rightPouchSlots = handler.scout$getRightPouchSlots();

      for(i = 0; i < 6; ++i) {
         slot = (BagSlot)rightPouchSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }

      if (!rightPouchStack.isEmpty()) {
         BaseBagItem rightPouchItem = (BaseBagItem)rightPouchStack.getItem();
         Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack);

         for(i = 0; i < rightPouchItem.getSlotCount(); ++i) {
            slot = (BagSlot)rightPouchSlots.get(i);
            slot.setInventory(rightPouchInv);
            slot.setEnabled(true);
         }
      }

      PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
      if (entity instanceof ServerPlayerEntity) {
         ServerPlayerEntity serverPlayer = (ServerPlayerEntity)entity;
         ServerPlayNetworking.send(serverPlayer, ScoutNetworking.ENABLE_SLOTS, packet);
      }

   }

   public void onUnequip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
      PlayerEntity player = (PlayerEntity)entity;
      ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler)player.playerScreenHandler;
      ItemStack satchelStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.SATCHEL, false);
      DefaultedList<BagSlot> satchelSlots = handler.scout$getSatchelSlots();

      for(int i = 0; i < 18; ++i) {
         BagSlot slot = (BagSlot)satchelSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }

      int i;
      BagSlot slot;
      if (!satchelStack.isEmpty()) {
         BaseBagItem satchelItem = (BaseBagItem)satchelStack.getItem();
         Inventory satchelInv = satchelItem.getInventory(satchelStack);

         for(i = 0; i < satchelItem.getSlotCount(); ++i) {
            slot = (BagSlot)satchelSlots.get(i);
            slot.setInventory(satchelInv);
            slot.setEnabled(true);
         }
      }

      ItemStack leftPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, false);
      DefaultedList<BagSlot> leftPouchSlots = handler.scout$getLeftPouchSlots();

      for(i = 0; i < 6; ++i) {
         slot = (BagSlot)leftPouchSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }

      if (!leftPouchStack.isEmpty()) {
         BaseBagItem leftPouchItem = (BaseBagItem)leftPouchStack.getItem();
         Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack);

         for(i = 0; i < leftPouchItem.getSlotCount(); ++i) {
            slot = (BagSlot)leftPouchSlots.get(i);
            slot.setInventory(leftPouchInv);
            slot.setEnabled(true);
         }
      }

      ItemStack rightPouchStack = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, true);
      DefaultedList<BagSlot> rightPouchSlots = handler.scout$getRightPouchSlots();

      for(i = 0; i < 6; ++i) {
         slot = (BagSlot)rightPouchSlots.get(i);
         slot.setInventory((Inventory)null);
         slot.setEnabled(false);
      }

      if (!rightPouchStack.isEmpty()) {
         BaseBagItem rightPouchItem = (BaseBagItem)rightPouchStack.getItem();
         Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack);

         for(i = 0; i < rightPouchItem.getSlotCount(); ++i) {
            slot = (BagSlot)rightPouchSlots.get(i);
            slot.setInventory(rightPouchInv);
            slot.setEnabled(true);
         }
      }

      PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
      if (entity instanceof ServerPlayerEntity) {
         ServerPlayerEntity serverPlayer = (ServerPlayerEntity)entity;
         ServerPlayNetworking.send(serverPlayer, ScoutNetworking.ENABLE_SLOTS, packet);
      }

   }

   public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
      Item item = stack.getItem();
      ItemStack slotStack = slot.inventory().getStack(slot.index());
      Item slotItem = slotStack.getItem();
      if (slotItem instanceof BaseBagItem) {
         if (((BaseBagItem)item).getType() == BaseBagItem.BagType.SATCHEL) {
            if (((BaseBagItem)slotItem).getType() == BaseBagItem.BagType.SATCHEL) {
               return true;
            }

            return ScoutUtil.findBagItem((PlayerEntity)entity, BaseBagItem.BagType.SATCHEL, false).isEmpty();
         }

         if (((BaseBagItem)item).getType() == BaseBagItem.BagType.POUCH) {
            if (((BaseBagItem)slotItem).getType() == BaseBagItem.BagType.POUCH) {
               return true;
            }

            return ScoutUtil.findBagItem((PlayerEntity)entity, BaseBagItem.BagType.POUCH, true).isEmpty();
         }
      } else {
         if (((BaseBagItem)item).getType() == BaseBagItem.BagType.SATCHEL) {
            return ScoutUtil.findBagItem((PlayerEntity)entity, BaseBagItem.BagType.SATCHEL, false).isEmpty();
         }

         if (((BaseBagItem)item).getType() == BaseBagItem.BagType.POUCH) {
            return ScoutUtil.findBagItem((PlayerEntity)entity, BaseBagItem.BagType.POUCH, true).isEmpty();
         }
      }

      return false;
   }

   public static enum BagType {
      SATCHEL,
      POUCH;

      // $FF: synthetic method
      private static BaseBagItem.BagType[] $values() {
         return new BaseBagItem.BagType[]{SATCHEL, POUCH};
      }
   }
}
