package cube.time.com.timecube.screen.reg;

import android.support.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class SignUpPresenter {
    private final LifecycleHandler mLifecycleHandler;
    private final SignUpView view;

    public SignUpPresenter(@NonNull LifecycleHandler mLifecycleHandler,@NonNull SignUpView view) {
        this.mLifecycleHandler = mLifecycleHandler;
        this.view = view;
    }
}
