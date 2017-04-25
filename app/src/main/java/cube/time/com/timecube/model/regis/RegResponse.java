package cube.time.com.timecube.model.regis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 4/25/2017.
 */

public class RegResponse {
    @SerializedName("data")
    @Expose
    private List<DataReg> data = new ArrayList<>();

    public List<DataReg> getData() {
        return data;
    }

    public void setData(List<DataReg> data) {
        this.data = data;
    }
}
