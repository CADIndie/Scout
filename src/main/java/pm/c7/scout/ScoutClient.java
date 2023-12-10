package pm.c7.scout;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import pm.c7.scout.gui.BagTooltipComponent;
import pm.c7.scout.item.BagTooltipData;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.screen.BagSlot;

public class ScoutClient implements ClientModInitializer {
   public void onInitializeClient() {
      ClientPlayNetworking.registerGlobalReceiver(ScoutNetworking.ENABLE_SLOTS, (client, handler, packet, sender) -> {
         client.execute(() -> {
            ScoutPlayerScreenHandler screenHandler = (ScoutPlayerScreenHandler)client.player.playerScreenHandler;
            ItemStack satchelStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.SATCHEL, false);
            DefaultedList<BagSlot> satchelSlots = screenHandler.scout$getSatchelSlots();

            for(int i = 0; i < 18; ++i) {
               BagSlot slotx = (BagSlot)satchelSlots.get(i);
               slotx.setInventory((Inventory)null);
               slotx.setEnabled(false);
            }

            int ix;
            BagSlot slotxx;
            if (!satchelStack.isEmpty()) {
               BaseBagItem satchelItem = (BaseBagItem)satchelStack.getItem();
               Inventory satchelInv = satchelItem.getInventory(satchelStack);

               for(ix = 0; ix < satchelItem.getSlotCount(); ++ix) {
                  slotxx = (BagSlot)satchelSlots.get(ix);
                  slotxx.setInventory(satchelInv);
                  slotxx.setEnabled(true);
               }
            }

            ItemStack leftPouchStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.POUCH, false);
            DefaultedList<BagSlot> leftPouchSlots = screenHandler.scout$getLeftPouchSlots();

            for(ix = 0; ix < 6; ++ix) {
               slotxx = (BagSlot)leftPouchSlots.get(ix);
               slotxx.setInventory((Inventory)null);
               slotxx.setEnabled(false);
            }

            int ixx;
            BagSlot slotxxx;
            if (!leftPouchStack.isEmpty()) {
               BaseBagItem leftPouchItem = (BaseBagItem)leftPouchStack.getItem();
               Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack);

               for(ixx = 0; ixx < leftPouchItem.getSlotCount(); ++ixx) {
                  slotxxx = (BagSlot)leftPouchSlots.get(ixx);
                  slotxxx.setInventory(leftPouchInv);
                  slotxxx.setEnabled(true);
               }
            }

            ItemStack rightPouchStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.POUCH, true);
            DefaultedList<BagSlot> rightPouchSlots = screenHandler.scout$getRightPouchSlots();

            for(ixx = 0; ixx < 6; ++ixx) {
               slotxxx = (BagSlot)rightPouchSlots.get(ixx);
               slotxxx.setInventory((Inventory)null);
               slotxxx.setEnabled(false);
            }

            if (!rightPouchStack.isEmpty()) {
               BaseBagItem rightPouchItem = (BaseBagItem)rightPouchStack.getItem();
               Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack);

               for(int ixxx = 0; ixxx < rightPouchItem.getSlotCount(); ++ixxx) {
                  BagSlot slot = (BagSlot)rightPouchSlots.get(ixxx);
                  slot.setInventory(rightPouchInv);
                  slot.setEnabled(true);
               }
            }

         });
      });
      TooltipComponentCallback.EVENT.register((data) -> {
         if (data instanceof BagTooltipData) {
            BagTooltipData d = (BagTooltipData)data;
            return new BagTooltipComponent(d);
         } else {
            return null;
         }
      });
   }
}
