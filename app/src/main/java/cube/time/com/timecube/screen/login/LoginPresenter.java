package cube.time.com.timecube.screen.login;

import android.support.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class LoginPresenter {
    private final LifecycleHandler mLifecycleHandler;
    private final LoginView view;

    public LoginPresenter(@NonNull LifecycleHandler mLifecycleHandler,@NonNull LoginView view) {
        this.mLifecycleHandler = mLifecycleHandler;
        this.view = view;
    }
}
