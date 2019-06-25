package com.example.eventos_denuncia.secciones;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eventos_denuncia.BuildConfig;
import com.example.eventos_denuncia.R;
import com.example.eventos_denuncia.SharedPrefManager;
import com.example.eventos_denuncia.Usuario;
import com.example.eventos_denuncia.api.RetrofitClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class RegistrarDenuncia extends Fragment {
    private GoogleMap mMap;
    private double latitud, longitud;
    private EditText titulo;
    private EditText desc;
    private static final int PICK_IMAGE = 12;
    private static final int CAPTURA_FOTO = 13;

    private ImageView mPhotoImageView;
    Uri uriImagen = null;
    String imageFilePath;


    Marker marker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_denuncia, container, false);
        return view;
    }
public void setLatitudLong (double lat, double lonng){

        this.latitud= lat;
        this.longitud=lonng;
}
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        final Button denuncia = view.findViewById(R.id.btn_denunciar);
        denuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nuevaDenuncia(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        final Button cancelar = view.findViewById(R.id.btn_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }   });

        final Button btn_foto = view.findViewById(R.id.btn_foto);
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        final Button btn_camara = view.findViewById(R.id.btn_camara);
        btn_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarCamara();
            }
        });

        titulo = view.findViewById(R.id.txt_titulo);
        desc = view.findViewById(R.id.txt_descripcion);
        mPhotoImageView = view.findViewById(R.id.imageden);

    }

    private void nuevaDenuncia(View v) throws IOException {


        String nombre = titulo.getText().toString();
        String descripcion= desc.getText().toString();

        double latitud1 = latitud;
        double longitud1 = longitud;


        //String foto = uriImagen.getPath();


        //validarPermisosEscritura();
        File fil = FileUtil.from(getActivity(), this.uriImagen);
        //File file = new Compressor(getActivity()).compressToFile(fil);
       File file = new Compressor(getActivity())
                .setMaxWidth(500)
                .setMaxHeight(500)
                .setQuality(75)

                .compressToFile(fil);

        System.out.print("Sin Comprimir:" + fil.length());
        System.out.print("Comprimida: " + file.length());
        RequestBody rqBody = RequestBody.create(MediaType.parse("multipart/form-data*"), file);
        MultipartBody.Part imagen = MultipartBody.Part.createFormData("foto", file.getName(), rqBody);
        RequestBody tituloRB = RequestBody.create(MediaType.parse("multipart/form-data*"), nombre);
        RequestBody descripcionRB = RequestBody.create(MediaType.parse("multipart/form-data*"), descripcion);
        RequestBody latitudRB = RequestBody.create(MediaType.parse("multipart/form-data*"), Double.toString(latitud1));
        RequestBody longitudRB = RequestBody.create(MediaType.parse("multipart/form-data*"), Double.toString(longitud1));
        final RequestBody idEstadoRB = RequestBody.create(MediaType.parse("multipart/form-data*"), String.valueOf(1));
        RequestBody activoRB = RequestBody.create(MediaType.parse("multipart/form-data*"), String.valueOf(1));

        Usuario usuario = SharedPrefManager.getInstance(getActivity()).getUsuario();
        RequestBody idusu = RequestBody.create(MediaType.parse("multipart/form-data*"), String.valueOf(usuario.getCedula()));

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .crearDenuncia(imagen,tituloRB,descripcionRB,latitudRB,longitudRB,idEstadoRB,activoRB,idusu);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String s = null;

                try {
                    if (response.code() == 201){
                        s = response.body().string();
                    }
                    else{
                        s = response.errorBody().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (s != null){
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Inicio inicio = new Inicio();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, inicio)
                        .addToBackStack(null)
                        .commit();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void llamarCamara() {
        if (validaPermisosCamara()) {

            Intent pictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                //Create a file to store the image
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "file_provider_authority", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(pictureIntent,
                            CAPTURA_FOTO);
                }
            }
        }
        }



    private void openGallery() {//funcion que abre la galeria
        if (validaPermisosGaleria()) {//valido que se tengan los permisos
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);//cargo el intento con el codigo 12
        }
    }

    private boolean validaPermisosGaleria() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);//los pido aca si no los tengo
            return true;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {//si es de la galeria
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).fit().centerCrop().into(mPhotoImageView);
           // mPhotoImageView.setImageURI(imageUri);
            this.uriImagen = imageUri;


        }
        if (resultCode == RESULT_OK && requestCode == CAPTURA_FOTO) {

            Uri imageUri = Uri.fromFile(new File(imageFilePath));
            Picasso.get().load(imageUri).fit().centerCrop().into(mPhotoImageView);
            //mPhotoImageView.setImageURI(imageUri);
            this.uriImagen = Uri.fromFile(new File(imageFilePath));

        }
    }

    private boolean validaPermisosCamara() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 2000);

        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
            } else {
                return true;
            }

        }
        return true;
    }

    private boolean validarPermisosEscritura() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
        }
        return true;
    }


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

}
