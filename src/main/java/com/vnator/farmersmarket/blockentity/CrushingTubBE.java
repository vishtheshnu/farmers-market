package com.vnator.farmersmarket.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CrushingTubBE extends BlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private final FluidTank fluidHandler = new FluidTank(4000);

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<FluidTank> lazyFluidHandler = LazyOptional.empty();

    public CrushingTubBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRUSHING_TUB_BE.get(), pPos, pBlockState);

    }

    public void performCraft(){
        level.playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS,
                0.5f, new Random().nextFloat() * 0.1f + 0.9f);
        //TODO- call when crafting operation succeeds: setChanged();
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        //Does nothing here
    }

    public void dropInventory(){
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++){
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.put("tank", fluidHandler.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        fluidHandler.readFromNBT(pTag.getCompound("tank"));
    }
}
