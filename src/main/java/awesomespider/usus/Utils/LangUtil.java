package awesomespider.usus.Utils;

import net.minecraft.util.StatCollector;

/**
 * Created by Awesome_Spider on 6/28/2015.
 */
public class LangUtil {//TODO Fix the lang util.
    public static String tranlate(String unlocalizedString){
        return StatCollector.translateToLocal(unlocalizedString);
    }
}
