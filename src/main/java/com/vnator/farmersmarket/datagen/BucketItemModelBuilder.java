package com.vnator.farmersmarket.datagen;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BucketItemModelBuilder extends ModelBuilder<BucketItemModelBuilder> {
    private ResourceLocation fluid;

    protected BucketItemModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    public void setFluid(ResourceLocation fluid){
        this.fluid = fluid;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "forge:item/bucket");
        root.addProperty("fluid", fluid.toString());
        root.addProperty("loader", "forge:fluid_container");
        return root;
    }
}
