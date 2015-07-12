package awesomespider.usus.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Awesome_Spider on 7/5/2015.
 */
public class TextureUtil {
    public static void bindTexture(String domain, String directory){
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(domain, directory));
    }
}
