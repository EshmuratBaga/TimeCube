package cube.time.com.timecube.screen.settings_cube;

import android.support.annotation.NonNull;

import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class CubeSettingsPresenter {
    private final LifecycleHandler mLifecycleHandler;
    private final CubeView view;

    public CubeSettingsPresenter(@NonNull LifecycleHandler mLifecycleHandler,@NonNull CubeView view) {
        this.mLifecycleHandler = mLifecycleHandler;
        this.view = view;
    }

    public void chooseType(){
        view.showDialog();
    }
}
