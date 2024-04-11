package com.vnator.farmersmarket.blockentity;

import com.vnator.farmersmarket.recipe.BrewingBarrelRecipe;
import com.vnator.farmersmarket.recipe.FluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class BrewingBarrelBE extends BlockEntity {

    private static final int TANK_CAPACITY = 4000;
    private final FluidTank fluidHandler = new FluidTank(TANK_CAPACITY){
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(getFluidAmount() == 0){
                brewComplete = false;
            }
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<FluidTank> lazyFluidHandler = LazyOptional.empty();
    private int brewTime = 0;
    private boolean brewComplete = false;

    private BrewingBarrelRecipe recipe;

    public BrewingBarrelBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BREWING_BARREL_BE.get(), pPos, pBlockState);
    }

    public String getBarrelStatus(){
        if(brewComplete){
            return "barrel_status.farmersmarket.complete";
        }else if(fluidHandler.isEmpty()){
            return "barrel_status.farmersmarket.empty";
        }else if(recipe == null){ // Existing fluid cannot be brewed
            return "barrel_status.farmersmarket.invalid_input";
        }else if(fluidHandler.getSpace() > 0){
            return "barrel_status.farmersmarket.partial_fill";
        }else if(brewTime < recipe.brewTime/3){
            return "barrel_status.farmersmarket.started";
        }else if(brewTime < 2*recipe.brewTime/3){
            return "barrel_status.farmersmarket.partway";
        }else if(brewTime < recipe.brewTime){
            return "barrel_status.farmersmarket.almost";
        }
        return "barrel_status.farmersmarket.default";
    }

    public FluidTank getFluidTank(){
        return fluidHandler;
    }

    public boolean isBrewComplete(){
        return brewComplete;
    }

    public boolean canExtract(){
        return brewTime == 0;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // Internal Tank only accessible if currently not brewing
        if(brewTime == 0 && cap == ForgeCapabilities.FLUID_HANDLER){
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        refreshRecipe();
        // Needs to be fully filled to work
        if(recipe != null && fluidHandler.getSpace() == 0){
            brewTime++;
            if(brewTime >= recipe.brewTime){
                fluidHandler.setFluid(new FluidStack(recipe.output, TANK_CAPACITY));
                brewTime = 0;
                brewComplete = true;
            }
            setChanged();
        }
//        if(isBrewComplete()){
//            System.out.println("Brew complete, drawing particles");
//            for(int i = 0; i < 10; i++){
//                level.addParticle(ParticleTypes.LARGE_SMOKE,
//                        pos.getX()+0.5, pos.getY()+1.2, pos.getZ()+0.5,
//                        new Random().nextDouble()/2, 0.1, new Random().nextDouble()/2);
//            }
//        }

    }

    private void refreshRecipe(){
        if(recipe == null || !recipe.input.isSame(fluidHandler.getFluid().getFluid())){
            FluidContainer inventory = new FluidContainer(fluidHandler.getFluid().getFluid());
            recipe = level.getRecipeManager().getRecipeFor(BrewingBarrelRecipe.Type.INSTANCE, inventory, level).orElse(null);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("tank", fluidHandler.writeToNBT(new CompoundTag()));
        pTag.putInt("brewTime", brewTime);
        pTag.putBoolean("brewComplete", brewComplete);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        fluidHandler.readFromNBT(pTag.getCompound("tank"));
        brewTime = pTag.getInt("brewTime");
        brewComplete = pTag.getBoolean("brewComplete");
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
