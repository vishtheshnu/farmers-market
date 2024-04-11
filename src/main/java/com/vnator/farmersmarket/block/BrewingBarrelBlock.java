package com.vnator.farmersmarket.block;

import com.vnator.farmersmarket.blockentity.BrewingBarrelBE;
import com.vnator.farmersmarket.blockentity.CrushingTubBE;
import com.vnator.farmersmarket.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected BrewingBarrelBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
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
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide()){
            FluidActionResult fluidResult;
            ItemStack stackInHand = player.getItemInHand(hand);
            Item itemInHand = stackInHand.getItem();
            if(level.getBlockEntity(pos) instanceof BrewingBarrelBE entity){
                FluidTank fluidTank = entity.getFluidTank();

                if(entity.canExtract()){
                    // Drain into bucket in hand
                    fluidResult = FluidUtil.tryEmptyContainerAndStow(stackInHand, fluidTank,
                            new InvWrapper(player.getInventory()), 1000, player, true);
                    if(fluidResult.isSuccess())
                        player.setItemInHand(hand, fluidResult.getResult());

                    // Fill with bucket in hand
                    fluidResult = FluidUtil.tryFillContainerAndStow(stackInHand, fluidTank,
                            new InvWrapper(player.getInventory()), 1000, player, true);
                    if(fluidResult.isSuccess())
                        player.setItemInHand(hand, fluidResult.getResult());
                }

                if(stackInHand.isEmpty()){
                    player.sendSystemMessage(Component.translatable(entity.getBarrelStatus()));
                    float fillRate = 1.0f * entity.getFluidTank().getFluidAmount() / entity.getFluidTank().getCapacity();
                    level.playSound(null, player.blockPosition(), SoundEvents.BAMBOO_WOOD_HIT, SoundSource.PLAYERS, 1f, 0.5f + fillRate*1.5f);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        super.animateTick(pState, pLevel, pPos, pRandom);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrewingBarrelBE(pPos, pState);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()){
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.BREWING_BARREL_BE.get(),
                ((pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1)));
    }
}
