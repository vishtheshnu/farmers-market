package com.vnator.farmersmarket.recipe;

import com.google.gson.JsonObject;
import com.vnator.farmersmarket.FarmersMarket;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class CrushingTubRecipe implements Recipe<Container> {
    private final ResourceLocation id;

    public final Ingredient input;
    public final Fluid output;
    public final int outputAmount;

    public CrushingTubRecipe(ResourceLocation id, Ingredient input, Fluid output, int outputAmount){
        this.id = id;
        this.input = input;
        this.output = output;
        this.outputAmount = outputAmount;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CrushingTubRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "crushing_tub";
    }

    public static class Serializer implements RecipeSerializer<CrushingTubRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FarmersMarket.MOD_ID, "crushing_tub");
        @Override
        public CrushingTubRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"));
            Fluid output = RecipeUtil.readFluidFromJson(pSerializedRecipe, "output");
            int outputAmount = pSerializedRecipe.get("outputAmount").getAsInt();

            return new CrushingTubRecipe(pRecipeId, input, output, outputAmount);
        }

        @Override
        public @Nullable CrushingTubRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            Fluid output = pBuffer.readRegistryId();
            int outputAmount = pBuffer.readInt();

            return new CrushingTubRecipe(pRecipeId, input, output, outputAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CrushingTubRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeRegistryId(ForgeRegistries.FLUIDS, pRecipe.output);
            pBuffer.writeInt(pRecipe.outputAmount);
        }
    }
}
