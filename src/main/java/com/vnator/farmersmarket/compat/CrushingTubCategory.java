package com.vnator.farmersmarket.compat;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.block.ModBlocks;
import com.vnator.farmersmarket.recipe.CrushingTubRecipe;
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

public class CrushingTubCategory implements IRecipeCategory<CrushingTubRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmersMarket.MOD_ID, "crushing_tub");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FarmersMarket.MOD_ID, "textures/gui/jei_background.png");
    public static final RecipeType<CrushingTubRecipe> CRUSHING_TUB_TYPE = new RecipeType<>(UID, CrushingTubRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CrushingTubCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 48, 119, 23);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRUSHING_TUB.get()));
    }

    @Override
    public RecipeType<CrushingTubRecipe> getRecipeType() {
        return CRUSHING_TUB_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.farmers_market.crushing_tub");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrushingTubRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4).addIngredients(recipe.input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 4).addFluidStack(recipe.output, recipe.outputAmount)
                .addTooltipCallback((view, list) -> {list.add(Component.literal(recipe.outputAmount+" mb"));});
        builder.addSlot(RecipeIngredientRole.CATALYST, 52, 4)
                .addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRUSHING_TUB.get()));
    }
}
