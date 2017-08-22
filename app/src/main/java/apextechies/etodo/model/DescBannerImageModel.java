package apextechies.etodo.model;

/**
 * Created by Shankar on 5/6/2017.
 */
public class DescBannerImageModel {


    public String getProduct_sliderid() {
        return product_sliderid;
    }

    public void setProduct_sliderid(String product_sliderid) {
        this.product_sliderid = product_sliderid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_sliderimage() {
        return product_sliderimage;
    }

    public void setProduct_sliderimage(String product_sliderimage) {
        this.product_sliderimage = product_sliderimage;
    }

    public String getProduct_slider_visibility_status() {
        return product_slider_visibility_status;
    }

    public void setProduct_slider_visibility_status(String product_slider_visibility_status) {
        this.product_slider_visibility_status = product_slider_visibility_status;
    }

    public String getProduct_slidercoupanid() {
        return product_slidercoupanid;
    }

    public void setProduct_slidercoupanid(String product_slidercoupanid) {
        this.product_slidercoupanid = product_slidercoupanid;
    }

    String product_sliderid,product_id,product_sliderimage,product_slider_visibility_status,product_slidercoupanid;

    public DescBannerImageModel(String product_sliderid,String product_id,String product_sliderimage, String product_slider_visibility_status,
                                String product_slidercoupanid)
    {
        this.product_sliderid = product_sliderid;
        this.product_id = product_id;
        this.product_sliderimage = product_sliderimage;
        this.product_slider_visibility_status = product_slider_visibility_status;
        this.product_slidercoupanid = product_slidercoupanid;
    }
}

