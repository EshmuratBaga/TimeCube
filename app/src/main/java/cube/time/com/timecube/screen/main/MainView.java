package cube.time.com.timecube.screen.main;

import java.util.List;

import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.screen.login.LoginView;

/**
 * Created by Andrey on 4/15/2017.
 */

public interface MainView extends LoginView{

    void finishBackPress();
    void showToast();
    void initAdapter(List<CubeSide> cubeSides);
    void openSettingsActivity();
}
