package awesomespider.usus.Events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Awesome_Spider on 7/13/2015.
 */
public class OpenBookEvent extends Event {
    EntityPlayer viewingPlayer;

    public OpenBookEvent(EntityPlayer player){
        viewingPlayer = player;
    }
}
