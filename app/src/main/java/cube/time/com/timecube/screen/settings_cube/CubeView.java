package cube.time.com.timecube.screen.settings_cube;

import cube.time.com.timecube.screen.login.LoginView;
import cube.time.com.timecube.utils.dialog.LoadingView;

/**
 * Created by Andrey on 4/12/2017.
 */

public interface CubeView extends LoadingView{

    void initWidget();

    void showDialog();

    void populateFeatureList();

    void showAddTaskDialog(int sum);

    void openMainActivity();
}
