package awesomespider.usus.TileEntities;

import awesomespider.usus.Usus;
import awesomespider.usus.Utils.LangUtil;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyStorage;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Awesome_Spider on 6/30/2015.
 */
public class TileEntitySteamEngine extends TileEntity implements IEnergyProvider, IInventory {
    public ForgeDirection facing;

    int fuelSlot = 0;
    int ashSlot = 1;
    ItemStack[] slots = new ItemStack[2];

    public int waterMb = 0;
    int maxWaterMb = 2500;

    public boolean burningFuel = false;
    public ItemStack currentBurningFuel = null;
    int progress = 0;
    int burningCountdown = 0;
    int rfToGenerate = 0;

    IEnergyStorage energyStorage = new IEnergyStorage() {
        int stored = 0;
        int max = 250;

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int remainder = maxReceive;
            int accepted = 0;
            boolean energyTransferComplete = false;

            while (!energyTransferComplete){
                if (stored < max && remainder > 0){
                    remainder --;
                    if (!simulate) stored ++;
                    accepted ++;
                } else {
                    energyTransferComplete = true;
                }
            }

            return accepted;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {//TODO Clean this up for efficiency. Make a class named "TileEntityEnergyCreator" and have this code in there. Then extend that class in other tile entities and call supers.
            int remainder = maxExtract;
            int extracted = 0;
            boolean energyTransferComplete = false;

            while (!energyTransferComplete){
                if (stored > remainder && remainder > 0){
                    remainder --;
                    if (!simulate) stored --;
                    extracted ++;
                } else {
                    energyTransferComplete = true;
                }
            }

            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return stored;
        }

        @Override
        public int getMaxEnergyStored() {
            return max;
        }
    };

    @Override
    public void updateEntity() {
        burningFuel = burningCountdown > 0;

        if (getStackInSlot(fuelSlot) != null)
           if (isFuel(getStackInSlot(fuelSlot)) && waterMb > 0) {
               burningCountdown = getFuelBurnTime(getStackInSlot(fuelSlot));
               rfToGenerate = getFuelRf(getStackInSlot(fuelSlot));
               currentBurningFuel = getStackInSlot(fuelSlot);
               decrStackSize(fuelSlot, 1);

               markDirty();
           }

        if (burningFuel) {
            burningCountdown--;
            rfToGenerate -= energyStorage.receiveEnergy(1, false);
            waterMb--;

            markDirty();

            if (burningCountdown == 0) {
                if (currentBurningFuel != null) {
                    if (doesFuelMakeAsh(currentBurningFuel)) {
                        if (getStackInSlot(ashSlot).getItem() == Usus.ash) {
                            int quantity = getStackInSlot(ashSlot).stackSize;

                            setInventorySlotContents(ashSlot, new ItemStack(Usus.ash, quantity + 1));
                        }
                    }

                    currentBurningFuel = null;
                }
            }
        }
    }

    public boolean isFuel(ItemStack stack){
        return getFuelBurnTime(stack) > 0;
    }

    public boolean doesFuelMakeAsh(ItemStack stack){
        Item item = stack.getItem();
        return item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD") |
                item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD") |
                item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD") |
                item == Items.stick |
                item == Items.coal |
                item == Item.getItemFromBlock(Blocks.sapling);
    }

    //Taken almost all from tile entity furnace class
    public int getFuelBurnTime(ItemStack stack){
        Item item = stack.getItem();

        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);

            if (block == Blocks.wooden_slab) {
                return 150;
            }

            if (block.getMaterial() == Material.wood) {
                return 300;
            }

            if (block == Blocks.coal_block) {
                return 16000;
            }
        }

        if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
        if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
        if (item == Items.stick) return 100;
        if (item == Items.coal) return 1600;
        if (item == Items.lava_bucket) return 20000;
        if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
        if (item == Items.blaze_rod) return 2400;
        return GameRegistry.getFuelValue(stack);
    }

    public int getFuelRf(ItemStack stack){
        return getFuelBurnTime(stack) / 4;
    }

    public void addWaterBucketToTank(){
        int remainder = 1000;
        int added = 0;
        boolean waterTransferComplete = false;

        while (!waterTransferComplete){
            if (waterMb < maxWaterMb && remainder > 0){
                remainder --;
                waterMb --;
                added ++;
            } else {
                waterTransferComplete = true;
            }
        }
    }

    public int scaleRfForDisplay(int maxNumber){
        return energyStorage.getEnergyStored() * maxNumber / energyStorage.getMaxEnergyStored();
    }

    public int scaleWaterForDisplay(int maxNumber){
        return waterMb * maxNumber / maxWaterMb;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int maxNumber) {
        return progress * maxNumber / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int maxNumber) {
        if (this.burningCountdown == 0) {
            this.burningCountdown = 200;
        }

        return this.progress * maxNumber / this.burningCountdown;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList listOfItems = nbt.getTagList("Items", 10);
        slots = new ItemStack[getSizeInventory()];

        for (int i = 0; i < listOfItems.tagCount(); i++)
        {
            NBTTagCompound slotNbt = listOfItems.getCompoundTagAt(i);
            byte b0 = slotNbt.getByte("Slot");

            if (b0 >= 0 && b0 < slots.length)
            {
                slots[b0] = ItemStack.loadItemStackFromNBT(slotNbt);
            }
        }

        waterMb = nbt.getInteger("WaterMb");
        progress = nbt.getInteger("BurnProgress");
        burningCountdown = nbt.getInteger("BurningTimer");
        currentBurningFuel = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("CurrentBurningFuel")); //TODO Confirm that this works.
        rfToGenerate = nbt.getInteger("RfToGenerate");

        energyStorage.receiveEnergy(nbt.getInteger("RF"), false);

        facing = ForgeDirection.getOrientation(nbt.getInteger("Facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        NBTTagList listOfItems = new NBTTagList();

        for (int i = 0; i < slots.length; i++){
            if (slots[i] != null) {
                NBTTagCompound slotNbt = new NBTTagCompound();
                slotNbt.setByte("Slot", (byte) i);
                this.slots[i].writeToNBT(slotNbt);
                listOfItems.appendTag(slotNbt);
            }
        }

        nbt.setTag("Items", listOfItems);

        nbt.setInteger("WaterMb", waterMb);
        nbt.setInteger("BurnProgress", progress);
        nbt.setInteger("BurningTimer", burningCountdown);
        NBTTagCompound currentBurningFuelTag = new NBTTagCompound();
        currentBurningFuel.writeToNBT(currentBurningFuelTag);
        nbt.setTag("CurrentBurningFuel", currentBurningFuelTag);
        nbt.setInteger("RfToGenerate", rfToGenerate);

        nbt.setInteger("RF", energyStorage.getEnergyStored());

        if (facing.equals(ForgeDirection.NORTH)){
            nbt.setInteger("Facing", 3);
        } else if (facing.equals(ForgeDirection.SOUTH)){
            nbt.setInteger("Facing", 4);
        } else if (facing.equals(ForgeDirection.WEST)){
            nbt.setInteger("Facing", 5);
        } else if (facing.equals(ForgeDirection.EAST)){
            nbt.setInteger("Facing", 6);
        }
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (from != ForgeDirection.UP && from != ForgeDirection.DOWN){
            return energyStorage.extractEnergy(maxExtract, simulate);
        } else {
            return 0;
        }
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return from != ForgeDirection.UP && from != ForgeDirection.DOWN;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slots[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.slots[slot] != null) {
            ItemStack itemstack;

            if (this.slots[slot].stackSize <= slot) {
                itemstack = this.slots[slot];
                this.slots[slot] = null;
                this.markDirty();
                return itemstack;
            }
            else {
                itemstack = this.slots[slot].splitStack(slot);

                if (this.slots[slot].stackSize == 0)
                {
                    this.slots[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.slots[slot] != null) {
            ItemStack itemstack = this.slots[slot];
            this.slots[slot] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        slots[slot] = stack;

        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "inv.steamEngine.name"; //TODO Confirm that I'm doing this right. Also, if I am, add this to the lang file.
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == fuelSlot){
            return isFuel(stack);
        } else if (slot == ashSlot){
           return false;
        } else {
            return true;
        }
    }
}
