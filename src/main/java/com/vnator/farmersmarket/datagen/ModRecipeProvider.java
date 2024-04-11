package com.vnator.farmersmarket.datagen;

import com.vnator.farmersmarket.datagen.recipe_builder.BrewingBarrelRecipeBuilder;
import com.vnator.farmersmarket.datagen.recipe_builder.CrushingTubRecipeBuilder;
import com.vnator.farmersmarket.fluid.ModFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        for(ModFluids.FRUITS fruit : ModFluids.FRUITS.values()){
            if(fruit.hasJuice){
                new CrushingTubRecipeBuilder()
                        .setInput(new ResourceLocation("forge", "fruits/"+fruit.tagName))
                        .setOutput(new ResourceLocation("farmers_market", "juice_"+fruit.tagName))
                        .setOutputAmount(250)
                        .save(pWriter, new ResourceLocation("farmers_market_juicing_"+fruit.tagName));
            }
            if(fruit.hasJuice && fruit.hasWine){
                new BrewingBarrelRecipeBuilder()
                        .setInput(new ResourceLocation("farmers_market", "juice_"+fruit.tagName))
                        .setOutput(new ResourceLocation("farmers_market", "wine_"+fruit.tagName))
                        .setBrewTime(6000) //5 minutes
                        .save(pWriter, new ResourceLocation("farmers_market_brewing_"+fruit.tagName));
            }


        }
    }

}
