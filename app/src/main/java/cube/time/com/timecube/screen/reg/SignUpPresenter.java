package cube.time.com.timecube.screen.reg;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import cube.time.com.timecube.model.regis.DataReg;
import cube.time.com.timecube.model.regis.RegResponse;
import cube.time.com.timecube.rest.ApiClient;
import cube.time.com.timecube.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public void signUp(String name, String email, String password){
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<RegResponse> call = apiInterface.regUser(name, email, password);
        call.enqueue(new Callback<RegResponse>() {
            @Override
            public void onResponse(Call<RegResponse> call, Response<RegResponse> response) {
                RegResponse regResponse = response.body();
                List<DataReg> dataReg = regResponse.getData();

                boolean success = dataReg.get(0).isSuccess();

                if (success){
                    Toast.makeText(context,"Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                    view.openLoginActivity();
                }else {
                    List<String> error = dataReg.get(0).getErrors();
                    Toast.makeText(context,error.get(0), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegResponse> call, Throwable t) {
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
