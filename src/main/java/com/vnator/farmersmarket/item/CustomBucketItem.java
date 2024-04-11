package com.vnator.farmersmarket.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class CustomBucketItem extends BucketItem {
    public CustomBucketItem(RegistryObject<ForgeFlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}
