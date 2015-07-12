package awesomespider.usus;

import awesomespider.usus.Blocks.BlockSteamEngine;
import awesomespider.usus.Guis.GuiHandler;
import awesomespider.usus.Items.Ash;
import awesomespider.usus.Items.RedstoneCrystalBattery;
import awesomespider.usus.Proxy.CommonProxy;
import awesomespider.usus.TileEntities.TileEntitySteamEngine;
import awesomespider.usus.Utils.LangUtil;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Usus.MODID, version = Usus.VERSION)
public class Usus
{
    public static final String MODID = "usus";
    public static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "awesomespider.usus.Proxy.ClientProxy", serverSide = "awesomespider.usus.Proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Usus instance;

    public static Logger logger;

    //Items
    public static Item redstoneCrystalBattery;
    public static Item ash;

    //Blocks
    public static Block steamEngine;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(LangUtil.tranlate("Initializing..."));

        redstoneCrystalBattery = new RedstoneCrystalBattery("restoneCrystalBattery", "redstoneCrystalBattery", 4);
        ash = new Ash("ash", "ash", 64);

        steamEngine = new BlockSteamEngine(Material.iron);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info(LangUtil.tranlate("Registering..."));

        GameRegistry.registerItem(redstoneCrystalBattery, "redstoneCrystalBattery", MODID);

        GameRegistry.registerBlock(steamEngine, "steamEngine");

        GameRegistry.registerTileEntity(TileEntitySteamEngine.class, "tileSteamEngine");
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        logger.info(LangUtil.tranlate("Performing other tasks..."));
        GameRegistry.addRecipe(new ItemStack(redstoneCrystalBattery, 4),
                " i ",
                "iri",
                "iri",
                'i', Items.iron_ingot,
                'r', Items.redstone
        );
        GameRegistry.addRecipe(new ItemStack(steamEngine, 1),
                "bib",
                "igi",
                "ifi",
                'b', redstoneCrystalBattery,
                'i', Items.iron_ingot,
                'g', Blocks.glass,
                'f', Blocks.furnace
        );

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        logger.info("All tasks completed without imploding!");
    }
}
