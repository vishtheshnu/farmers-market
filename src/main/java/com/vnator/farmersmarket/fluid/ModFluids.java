package com.vnator.farmersmarket.fluid;

import com.vnator.farmersmarket.FarmersMarket;
import com.vnator.farmersmarket.item.ModItems;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*This class, an ugly mess? How DARE you!
* jk you're right, but I'm too lazy to bother moving this to a file when this mod exists for a single modpack.
* If it gets reeeeally popular, then I'll consider cleaning this up.*/
public class ModFluids {
    public static final String PREFIX_JUICE = "juice_";
    public static final String PREFIX_WINE = "wine_";
    public static final String PREFIX_LIQUOR = "liquor_";

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, FarmersMarket.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, FarmersMarket.MOD_ID);

    public static final Map<String, RegistryObject<FluidType>> fruitFluidTypeMap = new HashMap<>();
    public static final Map<String, RegistryObject<ForgeFlowingFluid>> fruitFluidStillMap = new HashMap<>();
    public static final Map<String, RegistryObject<ForgeFlowingFluid>> fruitFluidFlowingMap = new HashMap<>();

    static {
        for(FRUITS fruit : FRUITS.values()){
            if(fruit.hasJuice){
                String fluidName = PREFIX_JUICE+fruit.tagName;

                RegistryObject<FluidType> juiceType = FLUID_TYPES.register(fluidName, () -> new JuiceFluidType(fruit.r, fruit.g, fruit.b));
                RegistryObject<ForgeFlowingFluid> fluidStill = FLUIDS.register(fluidName, () -> new ForgeFlowingFluid.Source(getFluidProperties(fluidName)));
                RegistryObject<ForgeFlowingFluid> fluidFlowing = FLUIDS.register("flowing_"+fluidName, () -> new ForgeFlowingFluid.Flowing(getFluidProperties(fluidName)));

                fruitFluidTypeMap.put(fluidName, juiceType);
                fruitFluidStillMap.put(fluidName, fluidStill);
                fruitFluidFlowingMap.put(fluidName, fluidFlowing);
            }
            if(fruit.hasWine){
                String fluidName = PREFIX_WINE+fruit.tagName;

                RegistryObject<FluidType> juiceType = FLUID_TYPES.register(fluidName, () -> new WineFluidType(fruit.r, fruit.g, fruit.b));
                RegistryObject<ForgeFlowingFluid> fluidStill = FLUIDS.register(fluidName, () -> new ForgeFlowingFluid.Source(getFluidProperties(fluidName)));
                RegistryObject<ForgeFlowingFluid> fluidFlowing = FLUIDS.register("flowing_"+fluidName, () -> new ForgeFlowingFluid.Flowing(getFluidProperties(fluidName)));

                fruitFluidTypeMap.put(fluidName, juiceType);
                fruitFluidStillMap.put(fluidName, fluidStill);
                fruitFluidFlowingMap.put(fluidName, fluidFlowing);
            }
            if(fruit.hasLiquor){
                String fluidName = PREFIX_LIQUOR+fruit.tagName;

                RegistryObject<FluidType> juiceType = FLUID_TYPES.register(fluidName, () -> new LiquorFluidType(fruit.r, fruit.g, fruit.b));
                RegistryObject<ForgeFlowingFluid> fluidStill = FLUIDS.register(fluidName, () -> new ForgeFlowingFluid.Source(getFluidProperties(fluidName)));
                RegistryObject<ForgeFlowingFluid> fluidFlowing = FLUIDS.register("flowing_"+fluidName, () -> new ForgeFlowingFluid.Flowing(getFluidProperties(fluidName)));

                fruitFluidTypeMap.put(fluidName, juiceType);
                fruitFluidStillMap.put(fluidName, fluidStill);
                fruitFluidFlowingMap.put(fluidName, fluidFlowing);
            }

        }
    }

    private static ForgeFlowingFluid.Properties getFluidProperties(String fluidName){
        return new ForgeFlowingFluid.Properties(
                fruitFluidTypeMap.get(fluidName), fruitFluidStillMap.get(fluidName), fruitFluidFlowingMap.get(fluidName))
                .bucket(ModItems.fluidBuckets.get(fluidName));
    }

    public static void register(IEventBus bus){
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }

    public enum FRUITS {
        GLOW_BERRIES("glow_berries", 255, 174, 66, true, true, true),
        SWEET_BERRIES("sweet_berries", 139, 0, 0, true, true, true),
        APPLE("apple",154, 95, 1, true, true, true),
        MELON("melon", 240, 50, 90,true, true, true),
        CHORUS("chorus", 181, 161, 181, true, true, true),
        PINEAPPLE("pineapple",255, 192, 0, true, true, true),
        PRICKLY_PEAR("pricklypear", 227, 51, 110,true, true, true),
        GRAPE_PURPLE("grapes", 104, 41, 97, true, true, true),
        CHERRY("cherry",173, 5, 62, true, true, true),
        ORANGE("orange", 252, 164, 60, true, true, true),
        PEAR("pear",242, 228, 189, true, true, true),
        PEACH("peach",255, 203, 164, true, true, true),
        MANGO("mango",244, 187, 68, true, true, true),
        LEMON("lemon",255, 250, 201, true, true, true),
        PLUM("plum",106, 42, 118, true, true, true),
        COCONUT("coconut",231, 229, 227, true, true, true),
        BANANA("banana", 225, 251, 201,true, true, true),
        MULBERRY("mulberry", 216, 191, 216, true, true, true),
        SALMONBERRY("salmonberries", 249, 191, 141, true, true, true),
        CANTALOUPE("cantaloupe", 244, 174, 114, true, true, true),
        GEAROBERRY("gearo_berry", 255, 105, 180, true, true, true),
        HAWBERRY("hawberry", 196, 16, 17, true, true, true),
        LYCHEE("lychee", 248, 249, 250, true, true, true),
        PERSIMMON("persimmon", 236, 88, 0, true, true, true),
        MANGOSTEEN("mangosteen", 151, 118, 148, true, true, true),
        BAYBERRY("bayberry", 128, 0, 128, true, true, true),
        KIWI("kiwi", 169, 199, 126, true, true, true),
        FIG("fig", 83, 45, 59, true, true, true),
        BLUEBERRY("blueberry", 102, 51, 153, true, true, true),
        CRANBERRY("cranberry", 122, 23, 23, true, true, true),
        DURIAN("durian", 255, 189, 39, true, true, true),
        HAMIMELON("hamimelon", 240, 230, 140, true, true, true);

        public final String tagName;
        public final int r, g, b;
        public final boolean hasJuice;
        public final boolean hasWine;
        public final boolean hasLiquor;

        private FRUITS(String tagName, int r, int g, int b, boolean hasJuice, boolean hasWine, boolean hasLiquor){
            this.tagName = tagName;
            this.r = r;
            this.g = g;
            this.b = b;
            this.hasJuice = hasJuice;
            this.hasWine = hasWine;
            this.hasLiquor = hasLiquor;
        }

        public List<String> getFluidNames(){
            List<String> toret = new ArrayList<>();
            if(hasJuice){toret.add(PREFIX_JUICE+tagName);}
            if(hasWine){toret.add(PREFIX_WINE+tagName);}
            if(hasLiquor){toret.add(PREFIX_LIQUOR+tagName);}
            return toret;
        }
    }

}
