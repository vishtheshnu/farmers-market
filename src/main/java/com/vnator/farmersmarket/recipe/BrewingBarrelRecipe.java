package com.vnator.farmersmarket.recipe;

import com.google.gson.JsonObject;
import com.vnator.farmersmarket.FarmersMarket;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelRecipe implements Recipe<FluidContainer> {
    private final ResourceLocation id;

    public final Fluid input;
    public final Fluid output;
    public final int brewTime;

    public BrewingBarrelRecipe(ResourceLocation id, Fluid input, Fluid output, int brewTime){
        this.id = id;
        this.input = input;
        this.output = output;
        this.brewTime = brewTime;
    }

    @Override
    public boolean matches(FluidContainer pContainer, Level pLevel) {
        return pContainer.getFluid().isSame(input);
    }

    @Override
    public ItemStack assemble(FluidContainer pContainer, RegistryAccess pRegistryAccess) {
        return null;
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

    public static class Type implements RecipeType<BrewingBarrelRecipe> {
        public static final BrewingBarrelRecipe.Type INSTANCE = new BrewingBarrelRecipe.Type();
        public static final String ID = "brewing_barrel";
    }

    public static class Serializer implements RecipeSerializer<BrewingBarrelRecipe> {
        public static final BrewingBarrelRecipe.Serializer INSTANCE = new BrewingBarrelRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FarmersMarket.MOD_ID, "brewing_barrel");

        @Override
        public BrewingBarrelRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Fluid input = RecipeUtil.readFluidFromJson(pSerializedRecipe, "input");
            Fluid output = RecipeUtil.readFluidFromJson(pSerializedRecipe, "output");
            int brewTime = pSerializedRecipe.get("brewTime").getAsInt();

            return new BrewingBarrelRecipe(pRecipeId, input, output, brewTime);
        }

        @Override
        public @Nullable BrewingBarrelRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Fluid input = pBuffer.readRegistryId();
            Fluid output = pBuffer.readRegistryId();
            int brewTime = pBuffer.readInt();

            return new BrewingBarrelRecipe(pRecipeId, input, output, brewTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BrewingBarrelRecipe pRecipe) {
            pBuffer.writeRegistryId(ForgeRegistries.FLUIDS, pRecipe.input);
            pBuffer.writeRegistryId(ForgeRegistries.FLUIDS, pRecipe.output);
            pBuffer.writeInt(pRecipe.brewTime);
        }
    }
}
