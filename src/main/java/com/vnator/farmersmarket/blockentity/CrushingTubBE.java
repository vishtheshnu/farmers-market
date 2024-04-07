package com.vnator.farmersmarket.blockentity;

import com.vnator.farmersmarket.recipe.CrushingTubRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class CrushingTubBE extends BlockEntity {

    private static final int TANK_CAPACITY = 4000;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private final FluidTank fluidHandler = new FluidTank(TANK_CAPACITY){
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<FluidTank> lazyFluidHandler = LazyOptional.empty();

    public CrushingTubBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRUSHING_TUB_BE.get(), pPos, pBlockState);

    }

    public void performCraft(){
        if(hasRecipe()){
            CrushingTubRecipe recipe = getCurrentRecipe().get();
            ItemStack inv = itemHandler.getStackInSlot(0);
            if(inv.getCount() == 1){
                inv = ItemStack.EMPTY;
            }else{
                inv = inv.copyWithCount(inv.getCount()-1);
            }
            itemHandler.setStackInSlot(0, inv);
            fluidHandler.fill(new FluidStack(recipe.output, recipe.outputAmount), IFluidHandler.FluidAction.EXECUTE);
            setChanged();

            level.playSound(null, getBlockPos(), SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS,
                    0.5f, new Random().nextFloat() * 0.1f + 0.9f);
        }else{
            level.playSound(null, getBlockPos(), SoundEvents.WOOD_FALL, SoundSource.BLOCKS,
                    0.5f, new Random().nextFloat() * 0.1f + 0.9f);
        }
        //TODO- call when crafting operation succeeds: setChanged();
    }

    private boolean hasRecipe(){
        Optional<CrushingTubRecipe> recipeOpt = getCurrentRecipe();
        if(recipeOpt.isEmpty()){
            return false;
        }
        CrushingTubRecipe recipe = recipeOpt.get();

        boolean isInputValid = recipe.input.test(itemHandler.getStackInSlot(0));
        boolean isOuputSpace = fluidHandler.getSpace() >= recipe.outputAmount &&
                (fluidHandler.isEmpty() || fluidHandler.getFluid().getFluid().isSame(recipe.output));

        return isInputValid && isOuputSpace;
    }

    private Optional<CrushingTubRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, itemHandler.getStackInSlot(0));
        return this.level.getRecipeManager().getRecipeFor(CrushingTubRecipe.Type.INSTANCE, inventory, level);
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

    public FluidTank getFluidTank(){
        return fluidHandler;
    }

    public IItemHandler getItemHandler(){
        return itemHandler;
    }

    public static int getCapacity(){
        return TANK_CAPACITY;
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
