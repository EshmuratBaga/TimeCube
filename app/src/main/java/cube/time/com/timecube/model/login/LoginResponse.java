package cube.time.com.timecube.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 4/24/2017.
 */

public class LoginResponse {

    @SerializedName("data")
    @Expose
    private List<DataLogin> data = new ArrayList<>();

    public List<DataLogin> getData() {
        return data;
    }

    public void setData(List<DataLogin> data) {
        this.data = data;
    }
}
