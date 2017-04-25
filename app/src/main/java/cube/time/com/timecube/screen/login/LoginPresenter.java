package cube.time.com.timecube.screen.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import cube.time.com.timecube.model.login.DataLogin;
import cube.time.com.timecube.model.login.LoginInfo;
import cube.time.com.timecube.model.login.LoginResponse;
import cube.time.com.timecube.rest.ApiClient;
import cube.time.com.timecube.rest.ApiInterface;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class LoginPresenter {
    private final LoginView view;
    private final Context context;
    private Realm realm;

    public LoginPresenter(@NonNull LoginView view, @NonNull Context context) {
        this.view = view;
        this.context = context;
    }

    public void checkUser(String email, String password){
        view.showLoading();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<LoginResponse> call = apiInterface.checkUser(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                List<DataLogin> dataLogin = loginResponse.getData();
                List<LoginInfo> loginInfo = dataLogin.get(0).getObject();

                boolean success = dataLogin.get(0).isSuccess();

                if (success){
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(loginInfo);
                    realm.commitTransaction();
                    view.hideLoading();
                    view.openScanActivity();
                }else {
                    List<String> error = dataLogin.get(0).getErrors();
                    Toast.makeText(context,error.get(0),Toast.LENGTH_SHORT).show();
                    view.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                view.hideLoading();
            }
        });
    }
}
