package com.github.hellocrossy.wondersoftheworld.item;

import com.github.hellocrossy.wondersoftheworld.WondersOfTheWorld;
import com.github.hellocrossy.wondersoftheworld.entity.WOTWEntities;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.zawamod.zawa.Zawa;
import org.zawamod.zawa.world.item.ZawaFishBucketItem;

public class WOTWItems {
    public static final DeferredRegister<Item> REGISTRAR = DeferredRegister.create(ForgeRegistries.ITEMS, WondersOfTheWorld.MOD_ID);

public  static final RegistryObject<Item>
OSCAR = REGISTRAR.register("oscar", () -> new Item(new Item.Properties().tab(Zawa.ITEMS_GROUP)));
public static final RegistryObject<Item>
OSCAR_BUCKET = REGISTRAR.register("oscar_bucket", () -> new ZawaFishBucketItem(WOTWEntities.OSCAR, () -> Fluids.WATER,
        new Item.Properties().stacksTo(1).tab(Zawa.ITEMS_GROUP)));

    public  static final RegistryObject<Item>
            DISCUS_FISH = REGISTRAR.register("discus_fish", () -> new Item(new Item.Properties().tab(Zawa.ITEMS_GROUP)));
    public static final RegistryObject<Item>
            DISCUS_FISH_BUCKET = REGISTRAR.register("discus_fish_bucket", () -> new ZawaFishBucketItem(WOTWEntities.DISCUS_FISH, () -> Fluids.WATER,
            new Item.Properties().stacksTo(1).tab(Zawa.ITEMS_GROUP)));

}
