package awesomespider.usus.Guis;

import awesomespider.usus.Containers.ContainerSteamEngine;
import awesomespider.usus.TileEntities.TileEntitySteamEngine;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Awesome_Spider on 7/8/2015.
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1){
            return new ContainerSteamEngine(player.inventory, (TileEntitySteamEngine) world.getTileEntity(x, y, z));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1){
            return new GuiSteamEngine(player.inventory, (TileEntitySteamEngine) world.getTileEntity(x, y, z));
        }

        return null;
    }
}
