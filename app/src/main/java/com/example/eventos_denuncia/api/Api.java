package com.example.eventos_denuncia.api;

import com.example.eventos_denuncia.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Api {

    @FormUrlEncoded
    @POST("createuser")
    Call<ResponseBody> createUser(

            @Field("cedula") String cedula,
            @Field("name") String name,
            @Field("apellido") String apellido,
            @Field("email") String email,
            @Field("password") String password,
            @Field("telefono") String telefono,
            @Field("fechan") String fechan

    );

    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userLogin(

            @Field("email") String email,
            @Field("password") String password

    );

    @FormUrlEncoded
    @PUT("updatepassword")
    Call<ResponseBody> updatePassword(

            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("email") String email

    );

}
