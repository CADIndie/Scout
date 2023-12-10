package pm.c7.scout;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import pm.c7.scout.item.BaseBagItem;

public class ScoutUtil {
   public static final Identifier SLOT_TEXTURE = new Identifier("scout", "textures/gui/slots.png");

   public static ItemStack findBagItem(PlayerEntity player, BaseBagItem.BagType type, boolean right) {
      ItemStack targetStack = ItemStack.EMPTY;
      boolean hasFirstPouch = false;
      Optional<TrinketComponent> _component = TrinketsApi.getTrinketComponent(player);
      if (_component.isPresent()) {
         TrinketComponent component = (TrinketComponent)_component.get();
         Iterator var7 = component.getAllEquipped().iterator();

         while(true) {
            ItemStack slotStack;
            BaseBagItem bagItem;
            do {
               do {
                  if (!var7.hasNext()) {
                     return targetStack;
                  }

                  Pair<SlotReference, ItemStack> pair = (Pair)var7.next();
                  slotStack = (ItemStack)pair.getRight();
               } while(!(slotStack.getItem() instanceof BaseBagItem));

               bagItem = (BaseBagItem)slotStack.getItem();
            } while(bagItem.getType() != type);

            if (type != BaseBagItem.BagType.POUCH) {
               targetStack = slotStack;
               break;
            }

            if (!right || hasFirstPouch) {
               targetStack = slotStack;
               break;
            }

            hasFirstPouch = true;
         }
      }

      return targetStack;
   }

   public static NbtList inventoryToTag(SimpleInventory inventory) {
      NbtList tag = new NbtList();

      for(int i = 0; i < inventory.size(); ++i) {
         NbtCompound stackTag = new NbtCompound();
         stackTag.putInt("Slot", i);
         stackTag.put("Stack", inventory.getStack(i).writeNbt(new NbtCompound()));
         tag.add(stackTag);
      }

      return tag;
   }

   public static void inventoryFromTag(NbtList tag, SimpleInventory inventory) {
      inventory.clear();
      tag.forEach((element) -> {
         NbtCompound stackTag = (NbtCompound)element;
         int slot = stackTag.getInt("Slot");
         ItemStack stack = ItemStack.fromNbt(stackTag.getCompound("Stack"));
         inventory.setStack(slot, stack);
      });
   }
}
