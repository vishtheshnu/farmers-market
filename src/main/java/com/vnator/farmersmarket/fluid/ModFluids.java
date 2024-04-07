package com.vnator.farmersmarket.fluid;

import com.vnator.farmersmarket.FarmersMarket;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, FarmersMarket.MOD_ID);

}
