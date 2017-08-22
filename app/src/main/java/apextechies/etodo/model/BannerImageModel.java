package apextechies.etodo.model;

/**
 * Created by Shankar on 1/10/2017.
 */

public class BannerImageModel {

    public String getSlider_id() {
        return slider_id;
    }

    public void setSlider_id(String slider_id) {
        this.slider_id = slider_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSlider_status() {
        return slider_status;
    }

    public void setSlider_status(String slider_status) {
        this.slider_status = slider_status;
    }

    public String getSlider_coupanid() {
        return slider_coupanid;
    }

    public void setSlider_coupanid(String slider_coupanid) {
        this.slider_coupanid = slider_coupanid;
    }

    String slider_id,image,slider_status,slider_coupanid;

    public BannerImageModel(String slider_id,String image,String slider_status, String slider_coupanid)
    {
        this.slider_id = slider_id;
        this.image = image;
        this.slider_status = slider_status;
        this.slider_coupanid = slider_coupanid;
    }
}
