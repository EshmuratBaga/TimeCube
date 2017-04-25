package cube.time.com.timecube.screen.reg;

import cube.time.com.timecube.screen.login.LoginView;
import cube.time.com.timecube.utils.dialog.LoadingView;

/**
 * Created by Andrey on 4/12/2017.
 */

public interface SignUpView extends LoadingView{

    void initWidget();
    void openLoginActivity();
}
