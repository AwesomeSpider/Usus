package awesomespider.usus.Items;

import awesomespider.usus.Usus;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Awesome_Spider on 7/2/2015.
 */
public class RedstoneCrystalBattery extends Item implements IEnergyContainerItem {
    public RedstoneCrystalBattery(String name, String texture, int stackSize){
        this.setUnlocalizedName(name);
        this.setTextureName(Usus.MODID + ":" + texture);
        this.setMaxStackSize(stackSize);

        this.setMaxDamage(125);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int remainder = maxReceive;
        int accepted = 0;
        boolean energyTransferComplete = false;

        int stored = container.getTagCompound().getInteger("RF");
        int max = container.getTagCompound().getInteger("MaxRF");

        while (!energyTransferComplete){
            if (stored < max && remainder > 0){
                remainder --;
                if (!simulate) stored ++;
                accepted ++;
            } else {
                energyTransferComplete = true;
            }
        }

        container.getTagCompound().setInteger("RF", stored);

        return accepted;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        int remainder = maxExtract;
        int extracted = 0;
        boolean energyTransferComplete = false;

        int stored = container.getTagCompound().getInteger("RF");

        while (!energyTransferComplete){
            if (stored > remainder && remainder > 0){
                remainder --;
                if (!simulate) stored --;
                extracted ++;
            } else {
                energyTransferComplete = true;
            }
        }

        container.getTagCompound().setInteger("RF", stored);

        return extracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.stackTagCompound != null) return container.stackTagCompound.getInteger("RF");

        return 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return container.getTagCompound().getInteger("MaxRF");
    }

    @Override
    public int getDamage(ItemStack stack){
        return getEnergyStored(stack);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player){
        if (stack.stackTagCompound == null){
            stack.stackTagCompound = new NBTTagCompound();
        }

        stack.stackTagCompound.setInteger("RF", 0);
        stack.stackTagCompound.setInteger("MaxRF", getMaxDamage());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List par3List, boolean par4){
        if (stack.stackTagCompound != null){
            par3List.add(EnumChatFormatting.AQUA + "Not very ideal, but it will have to do...");
            par3List.add(EnumChatFormatting.GREEN + "Max Capacity: " + stack.getTagCompound().getInteger("MaxRF"));

            int rf = stack.getTagCompound().getInteger("RF");

            if (rf > 100){
                par3List.add(EnumChatFormatting.AQUA + "Stored Rf: " + rf);
            } else if (rf > 50 && rf < 100){
                par3List.add(EnumChatFormatting.GREEN + "Stored Rf: " + rf);
            } else if (rf > 25 && rf < 50){
                par3List.add(EnumChatFormatting.YELLOW + "Stored Rf: " + rf);
            } else if (rf > 1 && rf < 25){
                par3List.add(EnumChatFormatting.RED + "Stored Rf: " + rf);
            } else if (rf == 0){
                par3List.add(EnumChatFormatting.DARK_RED + "Stored Rf: " + rf);
            }
        } else {
            par3List.add(EnumChatFormatting.AQUA + "Not very ideal, but it will have to do...");
            par3List.add(EnumChatFormatting.RED + "Item NBT is missing or corrupt...");
            par3List.add(EnumChatFormatting.RED + "Please " + EnumChatFormatting.YELLOW + "<Right Click>" + EnumChatFormatting.RED + " to repair NBT.");
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (stack.stackTagCompound == null){
            stack.stackTagCompound = new NBTTagCompound();
            stack.stackTagCompound.setInteger("MaxRF", getMaxDamage());
            return true;
        }

        return false;
    }
}
