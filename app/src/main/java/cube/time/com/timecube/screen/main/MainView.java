package cube.time.com.timecube.screen.main;

import java.util.List;

import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.screen.login.LoginView;
import cube.time.com.timecube.utils.dialog.LoadingView;

/**
 * Created by Andrey on 4/15/2017.
 */

public interface MainView extends LoadingView{

    void initWidget();
    void finishBackPress();
    void showToast();
    void initAdapter(List<CubeSide> cubeSides);
    void openSettingsActivity();
    void openStatisticActivity();
}
