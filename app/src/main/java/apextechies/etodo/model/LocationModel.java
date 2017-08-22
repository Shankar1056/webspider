package apextechies.etodo.model;

/**
 * Created by Admin on 24-01-2017.
 */

public class LocationModel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String id,city_id,location;

    public LocationModel(String id, String city_id, String location)
    {
        this.id = id;
        this.city_id = city_id;
        this.location = location;
    }
    public LocationModel(String location)
    {
        this.location = location;
    }
}
