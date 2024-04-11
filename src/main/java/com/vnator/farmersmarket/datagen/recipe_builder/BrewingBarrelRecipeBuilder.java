package com.vnator.farmersmarket.datagen.recipe_builder;

import com.google.gson.JsonObject;
import com.vnator.farmersmarket.recipe.ModRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BrewingBarrelRecipeBuilder {
    private ResourceLocation input;
    private ResourceLocation output;
    private int time;

    public BrewingBarrelRecipeBuilder setInput(ResourceLocation fluitInput){
        this.input = fluitInput;
        return this;
    }

    public BrewingBarrelRecipeBuilder setOutput(ResourceLocation fluitOutput){
        this.output = fluitOutput;
        return this;
    }

    public BrewingBarrelRecipeBuilder setBrewTime(int time){
        this.time = time;
        return this;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new BrewingBarrelFinishedRecipe(pRecipeId, input, output, time));
    }

    private class BrewingBarrelFinishedRecipe implements FinishedRecipe{

        private ResourceLocation id;
        private ResourceLocation input;
        private ResourceLocation output;
        private int brewTime;

        public BrewingBarrelFinishedRecipe(ResourceLocation id, ResourceLocation input, ResourceLocation output, int brewTime){
            this.id = id;
            this.input = input;
            this.output = output;
            this.brewTime = brewTime;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", "farmers_market:brewing_barrel");
            json.addProperty("input", input.toString());
            json.addProperty("output", output.toString());
            json.addProperty("brewTime", brewTime);
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
