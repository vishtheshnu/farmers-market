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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CrushingTubBlock extends BaseEntityBlock {
    private static final int CRAFT_SLOT = 0;
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

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide){
            FluidActionResult fluidResult;
            ItemStack stackInHand = player.getItemInHand(hand);
            Item itemInHand = stackInHand.getItem();
            if(level.getBlockEntity(pos) instanceof CrushingTubBE entity){
                FluidTank fluidTank = entity.getFluidTank();
                IItemHandler itemHandler = entity.getItemHandler();
                ItemStack internalStack = itemHandler.getStackInSlot(CRAFT_SLOT);
                Item internalItem = internalStack.getItem();


                //try to bucket in to/out of
                fluidResult = FluidUtil.tryEmptyContainerAndStow(stackInHand, fluidTank, new InvWrapper(player.getInventory()), 1000, player, true);
                if(fluidResult.isSuccess())
                    player.setItemInHand(hand, fluidResult.getResult());

                fluidResult = FluidUtil.tryFillContainerAndStow(stackInHand, fluidTank, new InvWrapper(player.getInventory()), 1000, player, true);
                if(fluidResult.isSuccess())
                    player.setItemInHand(hand, fluidResult.getResult());

                if(!stackInHand.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()){
                    if(internalStack.isEmpty() || (internalItem == itemInHand && (internalStack.getCount() < internalStack.getMaxStackSize()))){
                        level.playSound(null, player.blockPosition(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);
                        int amountToInsert = Math.min(stackInHand.getCount(), internalStack.getMaxStackSize() - internalStack.getCount());
                        itemHandler.insertItem(CRAFT_SLOT, new ItemStack(itemInHand, amountToInsert), false);
                        entity.setChanged();
                        entity.getLevel().sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), 3);
                        if(!player.isCreative())
                            stackInHand.shrink(amountToInsert);
                    }
                    else{
                        level.playSound(null, player.blockPosition(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 1.0F, 1.0F);
                        if(!player.isCreative())
                            popResourceFromFace(level, entity.getBlockPos(), player.getDirection().getOpposite(), internalStack);
                        itemHandler.extractItem(CRAFT_SLOT, internalStack.getCount(), false);
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
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
