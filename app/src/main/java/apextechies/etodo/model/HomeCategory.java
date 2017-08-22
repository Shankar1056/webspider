package apextechies.etodo.model;

/**
 * Created by Shankar on 1/5/2017.
 */
public class HomeCategory {

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getCat_status() {
        return cat_status;
    }

    public void setCat_status(String cat_status) {
        this.cat_status = cat_status;
    }

    String cat_id, cat_name, cat_image, cat_status;
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    private boolean selected;
    
    public int getResid() {
        return resid;
    }
    
    int resid;
    
    public int getWhiteid() {
        return whiteid;
    }
    
    int whiteid;
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    int state;

    public HomeCategory(String cat_id, String cat_name,String cat_image, String cat_status,boolean selected,int resid,int
        state,int whiteid) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_image = cat_image;
        this.cat_status = cat_status;
        this.selected = selected;
        this.resid = resid;
        this.state = state;
        this.whiteid = whiteid;
    }

}

