package cube.time.com.timecube.screen.reg;

import android.content.Context;
import android.support.annotation.NonNull;

import cube.time.com.timecube.rest.ApiClient;
import cube.time.com.timecube.rest.ApiInterface;
import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class SignUpPresenter {
    private final SignUpView view;
    private final Context context;

    public SignUpPresenter(@NonNull SignUpView view, @NonNull Context context) {
        this.view = view;
        this.context = context;
    }

    public void signUp(){
        ApiInterface apiInterface = ApiClient.getApiInterface();
    }
}
