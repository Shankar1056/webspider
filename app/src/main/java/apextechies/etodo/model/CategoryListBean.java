package apextechies.etodo.model;

import java.util.ArrayList;

/**
 * Created by Shankar on 1/8/2017.
 */
public class CategoryListBean {

    public String getSuccess() {
        return success;
    }

    public String getSuccess_message() {
        return success_message;
    }

    public ArrayList<ProductListModel> getData() {
        return data;
    }

    String success,success_message;
    ArrayList<ProductListModel> data;
    }