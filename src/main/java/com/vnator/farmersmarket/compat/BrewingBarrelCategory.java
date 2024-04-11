package com.vnator.farmersmarket.compat;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.block.ModBlocks;
import com.vnator.farmersmarket.recipe.BrewingBarrelRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.text.DecimalFormat;

public class BrewingBarrelCategory implements IRecipeCategory<BrewingBarrelRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmersMarket.MOD_ID, "brewing_barrel");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FarmersMarket.MOD_ID, "textures/gui/jei_background.png");
    public static final RecipeType<BrewingBarrelRecipe> BREWING_BARREL_TYPE = new RecipeType<>(UID, BrewingBarrelRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public BrewingBarrelCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 48, 119, 23);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BREWING_BARREL.get()));
    }

    @Override
    public RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return BREWING_BARREL_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.farmers_market.brewing_barrel");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingBarrelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4).addFluidStack(recipe.input, 1000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 4).addFluidStack(recipe.output, 1000);
        builder.addSlot(RecipeIngredientRole.CATALYST, 52, 4).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BREWING_BARREL.get()))
                .addTooltipCallback((view, list) -> list.set(0, getTimeAsComponent(recipe.brewTime)));
    }

    private Component getTimeAsComponent(int ticks){
        DecimalFormat format = new DecimalFormat("##.#");
        if(ticks < 20){ // LT second
            return Component.literal(ticks + " ").append(Component.translatable("tooltip.farmers_market.time_ticks"));
        }else if(ticks < 20 * 60){ // LT minute
            return Component.literal(format.format(ticks/20.0) + " ").append(Component.translatable("tooltip.farmers_market.time_seconds"));
        }else if(ticks < 20 * 60 * 60){ // LT hour
            return Component.literal(format.format(ticks/1200.0) + " ").append(Component.translatable("tooltip.farmers_market.time_minutes"));
        }else{
            return Component.literal(format.format(ticks/72000.0) + " ").append(Component.translatable("tooltip.farmers_market.time_hours"));
        }
    }
}
