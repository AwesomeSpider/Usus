package awesomespider.usus.Book;

/**
 * Created by Awesome_Spider on 7/11/2015.
 */
public abstract class Page {
    private String id;
    private String title;
    private String[] lines;
    private String imageTextureName;

    public Page (String title, String[] lines, String imageTextureName){
        setTitle(title);
        setLines(lines);
        setImageTextureName(imageTextureName);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setLines(String[] lines){
        this.lines = lines;
    }

    public String[] getLines(){
        return lines;
    }

    public void setImageTextureName(String imageTextureName){
        this.imageTextureName = imageTextureName;
    }

    public String getImageTextureName(){
        return imageTextureName;
    }
}
