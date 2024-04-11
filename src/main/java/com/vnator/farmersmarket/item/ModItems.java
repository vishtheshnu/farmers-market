package com.vnator.farmersmarket.item;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.fluid.ModFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FarmersMarket.MOD_ID);

    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            () -> new Item(new Item.Properties()));

    public static final Map<String, RegistryObject<Item>> fluidBuckets = new HashMap<>();
    static {
        for(ModFluids.FRUITS fruit : ModFluids.FRUITS.values()){
            for(String fluidName : fruit.getFluidNames()) {
                fluidBuckets.put(fluidName, ITEMS.register(fluidName + "_bucket",
                        () -> new BucketItem(ModFluids.fruitFluidStillMap.get(fluidName),
                                new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET))
                ));
            }

        }
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
