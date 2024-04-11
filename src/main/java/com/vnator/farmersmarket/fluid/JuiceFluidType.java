package com.vnator.farmersmarket.fluid;

import com.vnator.farmersmarket.FarmersMarket;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class JuiceFluidType extends FluidType {
    private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(FarmersMarket.MOD_ID, "block/juice_still");
    private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(FarmersMarket.MOD_ID, "block/juice_flowing");
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("block/water_overlay");
    private final int r, g, b;

    public JuiceFluidType(int r, int g, int b) {
        super(FluidType.Properties.create()
                .fallDistanceModifier(0)
                .canExtinguish(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return OVERLAY_TEXTURE;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                float fr = r / 255f;
                float fg = g / 255f;
                float fb = b / 255f;
                return new Vector3f(fr, fg, fb);
            }

            @Override
            public int getTintColor() {
                int color = 255;
                color = (color << 8) + r;
                color = (color << 8) + g;
                color = (color << 8) + b;
                return color;
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return getTintColor();
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return getTintColor();
            }
        });
        super.initializeClient(consumer);
    }
}
