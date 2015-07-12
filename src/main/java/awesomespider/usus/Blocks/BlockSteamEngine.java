package awesomespider.usus.Blocks;

import awesomespider.usus.TileEntities.TileEntitySteamEngine;
import awesomespider.usus.Usus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Awesome_Spider on 7/3/2015.
 */
public class BlockSteamEngine extends BlockContainer {
    IIcon[] icons;

    public BlockSteamEngine(Material material) {
        super(material);

        setBlockName("steamEngine");
        setBlockTextureName(Usus.MODID + ":steamEngine");
        setHardness(5F);
        setHarvestLevel("pickaxe", 0);
        setStepSound(Block.soundTypeMetal);
        setResistance(30F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileEntitySteamEngine();
    }

    @Override
    public void registerBlockIcons(IIconRegister reg){
        icons = new IIcon[11];

        icons[0] = reg.registerIcon(getTextureName() + "Front");
        icons[1] = reg.registerIcon(getTextureName() + "FrontWater1");
        icons[2] = reg.registerIcon(getTextureName() + "FrontWater2");
        icons[3] = reg.registerIcon(getTextureName() + "FrontWater3");
        icons[4] = reg.registerIcon(getTextureName() + "FrontWater4");
        icons[5] = reg.registerIcon(getTextureName() + "FrontActiveWater1");
        icons[6] = reg.registerIcon(getTextureName() + "FrontActiveWater2");
        icons[7] = reg.registerIcon(getTextureName() + "FrontActiveWater3");
        icons[8] = reg.registerIcon(getTextureName() + "FrontActiveWater4");
        icons[9] = reg.registerIcon(getTextureName() + "Side");
        icons[10] = reg.registerIcon(getTextureName() + "WaterPort");
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int posX, int posY, int posZ, int side){
        ForgeDirection side2 = ForgeDirection.getOrientation(side);
        ForgeDirection facing = ForgeDirection.getOrientation(world.getBlockMetadata(posX, posY, posZ));
        TileEntitySteamEngine te = (TileEntitySteamEngine) world.getTileEntity(posX, posY, posZ);
        int waterLevel = te.waterMb;
        int waterLevelScaled = te.scaleWaterForDisplay(4);
        boolean active = te.burningFuel;

        if (facing.equals(side2)) {
            if (waterLevelScaled == 0 && !active) {
                return icons[0];
            } else if (waterLevelScaled == 1 && !active) {
                return icons[1];
            } else if (waterLevelScaled == 2 && !active) {
                return icons[2];
            } else if (waterLevelScaled == 3 && !active) {
                return icons[3];
            } else if (waterLevelScaled == 4 && !active) {
                return icons[4];
            } else if (waterLevelScaled == 1 && active) {
                return icons[5];
            } else if (waterLevelScaled == 2 && active) {
                return icons[6];
            } else if (waterLevelScaled == 3 && active) {
                return icons[7];
            } else if (waterLevelScaled == 4 && active) {
                return icons[8];
            }
        } else if (side2.equals(ForgeDirection.NORTH)){
            return icons[9];
        } else if (side2.equals(ForgeDirection.SOUTH)){
            return icons[9];
        } else if (side2.equals(ForgeDirection.EAST)){
            return icons[9];
        } else if (side2.equals(ForgeDirection.WEST)){
            return icons[9];
        } else if (side2.equals(ForgeDirection.UP)){
            return icons[10];
        } else if (side2.equals(ForgeDirection.DOWN)){
            return icons[10];
        }

        return null;
    }

    @Override
    public IIcon getIcon(int side, int meta){
        if (side == 0){
            return icons[10];
        } else if (side == 1){
            return icons[10];
        } else if (side == 2){
            return icons[9];
        } else if (side == 3){
            return icons[0];
        } else if (side == 4){
            return icons[9];
        } else if (side == 5){
            return icons[9];
        }

        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entity, ItemStack stack) {
        int lookDirection = MathHelper.floor_double((double) (entity.rotationYawHead * 4.0F / 360.0F) + 0.5D) & 3;

        TileEntitySteamEngine te = (TileEntitySteamEngine) world.getTileEntity(posX, posY, posZ);

        if (lookDirection == 0) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 2, 2);
            te.facing = ForgeDirection.getOrientation(2);
        }

        if (lookDirection == 1) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 5, 2);
            te.facing = ForgeDirection.getOrientation(5);
        }

        if (lookDirection == 2) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 3, 2);
            te.facing = ForgeDirection.getOrientation(3);
        }

        if (lookDirection == 3) {
            world.setBlockMetadataWithNotify(posX, posY, posZ, 4, 2);
            te.facing = ForgeDirection.getOrientation(4);
        }
    }

    public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == Items.water_bucket){
            TileEntitySteamEngine te = (TileEntitySteamEngine) world.getTileEntity(posX, posY, posZ);
            te.addWaterBucketToTank();
            player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
        } else {
            if (!world.isRemote) player.openGui(Usus.instance, 1, world, posX, posY, posZ);
        }

        return true;
    }
}
