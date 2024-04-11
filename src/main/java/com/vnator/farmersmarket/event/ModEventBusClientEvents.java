package com.vnator.farmersmarket.event;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.blockentity.ModBlockEntities;
import com.vnator.farmersmarket.fluid.ModFluids;
import com.vnator.farmersmarket.item.ModItems;
import com.vnator.farmersmarket.renderer.CrushingTubBER;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmersMarket.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        // TODO for registering any mob models
        //event.registerLayerDefinition();
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModBlockEntities.CRUSHING_TUB_BE.get(), CrushingTubBER::new);
    }

    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        for(ModFluids.FRUITS fruit : ModFluids.FRUITS.values()){
            for(String fluidName : fruit.getFluidNames()){
                int tintcolor = IClientFluidTypeExtensions.of(ModFluids.fruitFluidStillMap.get(fluidName).get()).getTintColor();
                event.register((stack, index) -> index == 1 ? tintcolor : 0xFFFFFF, ModItems.fluidBuckets.get(fluidName).get());
            }
        }
    }
}
