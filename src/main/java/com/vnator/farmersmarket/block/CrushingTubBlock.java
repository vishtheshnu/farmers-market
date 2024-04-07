package com.vnator.farmersmarket.block;

import com.vnator.farmersmarket.blockentity.CrushingTubBE;
import com.vnator.farmersmarket.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CrushingTubBlock extends BaseEntityBlock {
    public CrushingTubBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag){
        pTooltip.add(Component.translatable("tooltip.farmers_market.crushing_tub"));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face){
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face){
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face){
        return 5;
    }

    @Override
    public @NotNull
    VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pos, CollisionContext context){
        return Shapes.or(Block.box(0, 0, 0, 16, 9, 16));
    }

    /*
     * TODO: Implement when BlockEntity is defined.
     *  onRemove- called when block is broken, drop item inventory in world
     *  use- called when player right-clicks block, add/remove from inventory or drain fluid into held container
     *  fallOn- called when player jumps onto block, perform crafting operation and play sound
     * */

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving){
        if(pState.getBlock() != pNewState.getBlock()){
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof CrushingTubBE){
                ((CrushingTubBE) blockEntity).dropInventory();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(), SoundSource.BLOCKS, 1f, (float) Math.random());
        return InteractionResult.SUCCESS;
        //return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity fallEntity, float pFallDistance) {
        if(!pLevel.isClientSide() && !(fallEntity instanceof ItemEntity) && pLevel.getBlockEntity(pPos) instanceof CrushingTubBE entity){
            entity.performCraft();
        }
        super.fallOn(pLevel, pState, pPos, fallEntity, pFallDistance);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrushingTubBE(pPos, pState);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()){
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.CRUSHING_TUB_BE.get(),
                ((pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1)));
    }
}
