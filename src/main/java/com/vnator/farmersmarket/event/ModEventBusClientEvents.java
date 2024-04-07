package com.vnator.farmersmarket.event;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.blockentity.ModBlockEntities;
import com.vnator.farmersmarket.renderer.CrushingTubBER;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
}
