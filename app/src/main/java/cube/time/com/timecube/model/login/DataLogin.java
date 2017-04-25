package cube.time.com.timecube.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 4/24/2017.
 */

public class DataLogin {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("object")
    @Expose
    private List<LoginInfo> object = new ArrayList<>();
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<LoginInfo> getObject() {
        return object;
    }

    public void setObject(List<LoginInfo> object) {
        this.object = object;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
