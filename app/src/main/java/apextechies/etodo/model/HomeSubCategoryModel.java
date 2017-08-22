package apextechies.etodo.model;

/**
 * Created by Shankar on 7/16/2017.
 */
public class HomeSubCategoryModel {
    String id,cat_id,heading,images,status;

    public String getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getHeading() {
        return heading;
    }

    public String getImages() {
        return images;
    }

    public String getStatus() {
        return status;
    }

    public HomeSubCategoryModel(String id, String cat_id, String heading, String images, String status) {

        this.id = id;
        this.cat_id = cat_id;
        this.heading = heading;
        this.images = images;
        this.status = status;
    }
}
