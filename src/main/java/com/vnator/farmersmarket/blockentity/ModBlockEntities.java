package com.vnator.farmersmarket.blockentity;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FarmersMarket.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrushingTubBE>> CRUSHING_TUB_BE =
            BLOCK_ENTITIES.register("crushing_tub_be", () ->
                    BlockEntityType.Builder.of(CrushingTubBE::new, ModBlocks.CRUSHING_TUB.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
