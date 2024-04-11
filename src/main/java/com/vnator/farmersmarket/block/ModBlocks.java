package com.vnator.farmersmarket.block;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.fluid.ModFluids;
import com.vnator.farmersmarket.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersMarket.MOD_ID);

    public static final RegistryObject<Block> CRUSHING_TUB = registerBlock("crushing_tub",
            () -> new CrushingTubBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> BREWING_BARREL = registerBlock("brewing_barrel",
            () -> new BrewingBarrelBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).noOcclusion().sound(SoundType.WOOD)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
