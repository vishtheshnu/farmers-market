package com.vnator.farmersmarket.datagen.recipe_builder;

import com.google.gson.JsonObject;
import com.vnator.farmersmarket.datagen.ModRecipeProvider;
import com.vnator.farmersmarket.recipe.ModRecipes;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CrushingTubRecipeBuilder {
    private ResourceLocation input;
    private ResourceLocation output;
    private int outputAmount;

    public CrushingTubRecipeBuilder setInput(ResourceLocation itemTag){
        this.input = itemTag;
        return this;
    }

    public CrushingTubRecipeBuilder setOutput(ResourceLocation fluid){
        this.output = fluid;
        return this;
    }

    public CrushingTubRecipeBuilder setOutputAmount(int amount){
        this.outputAmount = amount;
        return this;
    }



    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new CrushingTubFinishedRecipe(pRecipeId, input, output, outputAmount));
    }

    private class CrushingTubFinishedRecipe implements FinishedRecipe{

        private ResourceLocation id;
        private ResourceLocation input;
        private ResourceLocation output;
        private int outputAmount;

        public CrushingTubFinishedRecipe(ResourceLocation id, ResourceLocation input, ResourceLocation output, int outputAmount){
            this.id = id;
            this.input = input;
            this.output = output;
            this.outputAmount = outputAmount;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", "farmers_market:crushing_tub");

            JsonObject inputObj = new JsonObject();
            inputObj.addProperty("tag", input.toString());
            json.add("input", inputObj);

            json.addProperty("output", output.toString());
            json.addProperty("outputAmount", outputAmount);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.CRUSHING_TUB_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return new JsonObject();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return new ResourceLocation("");
        }
    }
}
