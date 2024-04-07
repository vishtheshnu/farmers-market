package com.vnator.farmersmarket.recipe;

import com.vnator.farmersmarket.FarmersMarket;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersMarket.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CrushingTubRecipe>> CRUSHING_TUB_SERIALIZER =
            SERIALIZERS.register("crushing_tub", () -> CrushingTubRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
