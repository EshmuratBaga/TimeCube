package cube.time.com.timecube.screen.statistic;

import cube.time.com.timecube.screen.login.LoginView;
import cube.time.com.timecube.utils.dialog.LoadingView;

/**
 * Created by Andrey on 4/18/2017.
 */

public interface StatisticView extends LoadingView{
    void initActionBar();
    void initWidget();
}
