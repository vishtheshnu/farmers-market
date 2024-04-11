package com.vnator.farmersmarket.item;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersMarket.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("farmers_market_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CRUSHING_TUB.get()))
                    .title(Component.translatable("creativetab.farmers_market.tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.CRUSHING_TUB.get());
                        output.accept(ModBlocks.BREWING_BARREL.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TAB_JUICE = CREATIVE_MODE_TABS.register("farmers_market_tab_juice",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.fluidBuckets.get("juice_apple").get()))
                    .title(Component.translatable("creativetab.farmers_market.juice"))
                    .displayItems((parameters, output) -> {
                        ModItems.fluidBuckets.keySet().stream()
                                .filter(i -> i.startsWith("juice_"))
                                .sorted()
                                .map(i -> ModItems.fluidBuckets.get(i))
                                .forEach(bucket -> output.accept(bucket.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TAB_WINE = CREATIVE_MODE_TABS.register("farmers_market_tab_wine",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.fluidBuckets.get("wine_apple").get()))
                    .title(Component.translatable("creativetab.farmers_market.wine"))
                    .displayItems((parameters, output) -> {
                        ModItems.fluidBuckets.keySet().stream()
                                .filter(i -> i.startsWith("wine_"))
                                .sorted()
                                .map(i -> ModItems.fluidBuckets.get(i))
                                .forEach(bucket -> output.accept(bucket.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TAB_LIQUOR = CREATIVE_MODE_TABS.register("farmers_market_tab_liquor",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.fluidBuckets.get("liquor_apple").get()))
                    .title(Component.translatable("creativetab.farmers_market.liquor"))
                    .displayItems((parameters, output) -> {
                        ModItems.fluidBuckets.keySet().stream()
                                .filter(i -> i.startsWith("liquor_"))
                                .sorted()
                                .map(i -> ModItems.fluidBuckets.get(i))
                                .forEach(bucket -> output.accept(bucket.get()));
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
