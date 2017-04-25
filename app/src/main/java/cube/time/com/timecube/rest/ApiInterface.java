package cube.time.com.timecube.rest;

import cube.time.com.timecube.model.login.LoginResponse;
import cube.time.com.timecube.model.regis.RegResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andrey on 3/20/2017.
 */

public interface ApiInterface {
    @GET("?act=login")
    Call<LoginResponse> checkUser(@Query("email") String login, @Query("password") String password);

    @GET("?act=register")
    Call<RegResponse> regUser(@Query("fullname") String name, @Query("email") String email, @Query("password") String pass);
}
