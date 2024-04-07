package com.vnator.farmersmarket.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class RecipeUtil {

    public static Fluid readFluidFromJson(JsonObject json, String key){
        String fluidName = GsonHelper.getAsString(json, key);
        ResourceLocation fluidKey = new ResourceLocation(fluidName);
        if (!ForgeRegistries.FLUIDS.containsKey(fluidKey)) {
            throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidKey);
        if (fluid == Fluids.EMPTY) {
            throw new JsonSyntaxException("Invalid Fluid: " + fluidName);
        }
        return Objects.requireNonNull(fluid);
    }
}
