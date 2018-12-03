package com.mlh.mlh;

import android.graphics.Bitmap;
import android.util.Log;

import com.mlh.mlh.data.ApiManager;
import com.mlh.mlh.data.SharedPreferencesManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRepository {

    private ApiManager manager = new ApiManager();
    private SharedPreferencesManager prefsManager;

    public interface ValidateListener {

        void onSuccess();

        void onError(int error);

    }

    public interface ImageSendingListener {

        void onSuccess();

        void onError(int error);

    }

    public TokenRepository(SharedPreferencesManager prefsManager) {
        this.prefsManager = prefsManager;
    }

    public int getToken() {
        return prefsManager.getToken();
    }

    public void validateToken(int token, final ValidateListener listener) {
        prefsManager.saveToken(token);
        Call<String> call = manager.getService().validateToken();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onSuccess();
                    //listener.onError(Constants.HTTP_BAD_CREDENTIALS);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onSuccess();
                //listener.onError(Constants.HTTP_BAD_CREDENTIALS);
            }
        });
    }

    public void sendImage(int token, Bitmap bitmap, final ImageSendingListener listener) {
        try {
            String path = "data/data/com.mlh.mlh/" + String.valueOf(token);
            File f = new File(path);
            f.createNewFile();


//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            RequestBody requestToken = RequestBody.create(MediaType.parse("form-data"), String.valueOf(token));

            MultipartBody.Part imageBody =
                    MultipartBody.Part.createFormData("file", f.getName(), requestFile);

            // MultipartBody.Part используется, чтобы передать имя файла
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("token", "token", requestToken);


            Call<ResponseBody> call = manager.getService().setImage(body, imageBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    listener.onSuccess();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    listener.onError(123);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
