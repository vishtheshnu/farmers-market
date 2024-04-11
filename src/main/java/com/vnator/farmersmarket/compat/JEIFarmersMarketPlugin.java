package com.vnator.farmersmarket.compat;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.recipe.BrewingBarrelRecipe;
import com.vnator.farmersmarket.recipe.CrushingTubRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIFarmersMarketPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FarmersMarket.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CrushingTubCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BrewingBarrelCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        System.out.println("Crushing Tub recipe item inputs:");
        List<CrushingTubRecipe> crushingTubRecipes = recipeManager.getAllRecipesFor(CrushingTubRecipe.Type.INSTANCE)
                .stream()
                .filter(recipe -> !recipe.input.getItems()[0].getItem().equals(Items.BARRIER))
                .collect(Collectors.toList());
        registration.addRecipes(CrushingTubCategory.CRUSHING_TUB_TYPE, crushingTubRecipes);

        List<BrewingBarrelRecipe> brewingBarrelRecipes = recipeManager.getAllRecipesFor(BrewingBarrelRecipe.Type.INSTANCE);
        registration.addRecipes(BrewingBarrelCategory.BREWING_BARREL_TYPE, brewingBarrelRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Mod doesn't add GUIs, so can't add areas to them that lead to JEI
        //registration.addRecipeClickArea();
    }
}
