package awesomespider.usus.Book;

import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.List;

/**
 * Created by Awesome_Spider on 7/11/2015.
 */
public class Pages {
    public static List<Page> pages;

    private void addPage(String id, String title, String[] lines, String imageTextureName){
        pages.add(new Page(id, title, lines, imageTextureName) {});
    }

    public static void registerPages(){
        
    }

    public static Page getPageById(String id){
        for (Page page : pages){
            if (page.getId().equals(id)){
                return page;
            }
        }

        return null;
    }
}
