package pm.c7.scout;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import pm.c7.scout.item.BaseBagItem;

public class Scout implements ModInitializer {
   public static final String MOD_ID = "scout";
   public static final int MAX_SATCHEL_SLOTS = 18;
   public static final int MAX_POUCH_SLOTS = 6;

   public static final Item TANNED_LEATHER;
   public static final Item SATCHEL_STRAP;
   public static final BaseBagItem SATCHEL;
   public static final BaseBagItem UPGRADED_SATCHEL;
   public static final BaseBagItem POUCH;
   public static final BaseBagItem UPGRADED_POUCH;

   public void onInitialize() {
      Registry.register(Registry.ITEM, new Identifier("scout", "tanned_leather"), TANNED_LEATHER);
      Registry.register(Registry.ITEM, new Identifier("scout", "satchel_strap"), SATCHEL_STRAP);
      Registry.register(Registry.ITEM, new Identifier("scout", "satchel"), SATCHEL);
      Registry.register(Registry.ITEM, new Identifier("scout", "upgraded_satchel"), UPGRADED_SATCHEL);
      Registry.register(Registry.ITEM, new Identifier("scout", "pouch"), POUCH);
      Registry.register(Registry.ITEM, new Identifier("scout", "upgraded_pouch"), UPGRADED_POUCH);
   }

   public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("scout", "itemgroup"), () -> {
      return new ItemStack(SATCHEL);
   });

   static {
      TANNED_LEATHER = new Item((new FabricItemSettings()).group(ITEM_GROUP));
      SATCHEL_STRAP = new Item((new FabricItemSettings()).group(ITEM_GROUP));
      SATCHEL = new BaseBagItem((new FabricItemSettings()).group(ITEM_GROUP).maxCount(1), 9, BaseBagItem.BagType.SATCHEL);
      UPGRADED_SATCHEL = new BaseBagItem((new FabricItemSettings()).group(ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 18, BaseBagItem.BagType.SATCHEL);
      POUCH = new BaseBagItem((new FabricItemSettings()).group(ITEM_GROUP).maxCount(1), 3, BaseBagItem.BagType.POUCH);
      UPGRADED_POUCH = new BaseBagItem((new FabricItemSettings()).group(ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 6, BaseBagItem.BagType.POUCH);
   }
}
