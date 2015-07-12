package awesomespider.usus.Items;

import awesomespider.usus.Usus;
import net.minecraft.item.Item;

/**
 * Created by Awesome_Spider on 7/4/2015.
 */
public class Ash extends Item {
    public Ash(String name, String texture, int stackSize){
        this.setUnlocalizedName(name);
        this.setTextureName(Usus.MODID + ":" + texture);
        this.setMaxStackSize(stackSize);
    }
}
