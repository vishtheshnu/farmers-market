package com.vnator.farmersmarket.datagen;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.fluid.ModFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class ModItemModelProvider extends ModelProvider<BucketItemModelBuilder> {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FarmersMarket.MOD_ID, ITEM_FOLDER, BucketItemModelBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerFluids();
    }

    private void registerFluids(){
        for(ModFluids.FRUITS fruit : ModFluids.FRUITS.values()){
            for(String fluidName : fruit.getFluidNames()){
                getBuilder(fluidName+"_bucket").setFluid(new ResourceLocation(modid, fluidName));

            }
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "Item Models: " + modid;
    }
}
