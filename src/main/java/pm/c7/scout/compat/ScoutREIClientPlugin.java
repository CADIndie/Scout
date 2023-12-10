package pm.c7.scout.compat;

import java.util.ArrayList;
import java.util.List;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.mixin.HandledScreenAccessor;

public class ScoutREIClientPlugin implements REIClientPlugin {
   public void registerScreens(ScreenRegistry registry) {
      ExclusionZones zones = registry.exclusionZones();
      zones.register(InventoryScreen.class, (screen) -> {
         List<Rectangle> bounds = new ArrayList();
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
            bounds.add(new Rectangle(columns, x, slotsx * 18, 68));
         }

         ItemStack rightPouchStack = ScoutUtil.findBagItem(client.player, BaseBagItem.BagType.POUCH, true);
         if (!rightPouchStack.isEmpty()) {
            BaseBagItem bagItem = (BaseBagItem)rightPouchStack.getItem();
            slotsx = bagItem.getSlotCount();
            columns = (int)Math.ceil((double)(slotsx / 3));
            x = ((HandledScreenAccessor)screen).getX() + 176;
            int y = ((HandledScreenAccessor)screen).getY() + 76;
            bounds.add(new Rectangle(x, y, columns * 18, 68));
         }

         return bounds;
      });
   }
}
