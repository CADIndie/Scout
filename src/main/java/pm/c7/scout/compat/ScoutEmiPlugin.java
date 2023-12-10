package pm.c7.scout.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;

import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.mixin.HandledScreenAccessor;

public class ScoutEmiPlugin implements EmiPlugin {
   public void register(EmiRegistry registry) {
      registry.addExclusionArea(InventoryScreen.class, (screen, consumer) -> {
         MinecraftClient client = MinecraftClient.getInstance();
         ItemStack leftPouchStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.POUCH, false);
         int slotsx;
         int columns;
         int x;
         if (!leftPouchStack.isEmpty()) {
            BaseBagItem bagItemx = (BaseBagItem)leftPouchStack.getItem();
            int slots = bagItemx.getSlotCount();
            slotsx = (int)Math.ceil((double)(slots / 3));
            columns = ((HandledScreenAccessor)screen).getX() - slotsx * 18;
            x = ((HandledScreenAccessor)screen).getY() + 76;
            consumer.accept(new Bounds(columns, x, slotsx * 18, 68));
         }

         ItemStack rightPouchStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.POUCH, true);
         if (!rightPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)rightPouchStack.getItem();
            slotsx = bagItem.getSlotCount();
            columns = (int)Math.ceil((double)(slotsx / 3));
            x = ((HandledScreenAccessor)screen).getX() + 176;
            int y = ((HandledScreenAccessor)screen).getY() + 76;
            consumer.accept(new Bounds(x, y, columns * 18, 68));
         }

      });
   }
}
