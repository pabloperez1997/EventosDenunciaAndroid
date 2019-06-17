package com.example.eventos_denuncia.api;

import com.example.eventos_denuncia.EventoResponse;
import com.example.eventos_denuncia.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @FormUrlEncoded
    @PUT("updateuser/{ci}")
    Call<LoginResponse> updateUser(
            @Path("ci") String cedula,
            @Field("nombre") String name,
            @Field("apellido") String apellido,
            @Field("email") String email,
            @Field("telefono") String telefono,
            @Field("fechan") String fechan

    );

    @FormUrlEncoded
    @PUT("desactivarusuario/{ci}")
    Call<LoginResponse> desactivarUsuario(
            @Path("ci") String cedula,
            @Field("nombre") String name

    );

    @GET("obtenereventos")
    Call<EventoResponse> obtenerEventos();


    @GET("obtenereventosdelusuario/{idusu}")
    Call<EventoResponse> obtenerEventosdelusuario(@Path("idusu") String idusu);

    @Multipart
    @POST("nuevoevento")
    Call<ResponseBody> crearDenuncia(@Part MultipartBody.Part file,
                                  @Part("nombre") RequestBody titulo,
                                  @Part("descripcion") RequestBody descripcion,
                                  @Part("latitud") RequestBody latitud,
                                  @Part("longitud") RequestBody longitud,
                                  @Part("idEstado") RequestBody idStado,
                                  @Part("activo") RequestBody activo,
                                  @Part("idusu") RequestBody idusu);

}
