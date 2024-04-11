package com.vnator.farmersmarket.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.material.Fluid;

public class FluidContainer extends SimpleContainer {
    private Fluid fluid;

    public FluidContainer(Fluid fluid){
        this.fluid = fluid;
    }

    public Fluid getFluid(){
        return fluid;
    }
}
