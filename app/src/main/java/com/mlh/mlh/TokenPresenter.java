package com.mlh.mlh;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.mlh.mlh.data.SharedPreferencesManager;

public class TokenPresenter<T extends TokenContract.View> implements TokenContract.Presenter<T>{

    private final TokenRepository repository;
    private T view;

    public TokenPresenter(Context context) {
        repository = new TokenRepository(SharedPreferencesManager
                .getInstance(context));
    }

    @Override
    public void start(T view) {
        this.view = view;
    }

    @Override
    public void stop() {
        view = null;
    }

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void validateToken(int token) {
        getView().showProgress();
        repository.validateToken(token, new TokenRepository.ValidateListener() {
            @Override
            public void onSuccess() {
                getView().hideProgress();
                getView().beginDrawing();
            }

            @Override
            public void onError(int error) {
                getView().hideProgress();
                getView().showError();
                Log.d("token error", "not validated");
            }
        });
    }

    @Override
    public int getToken() {
        return repository.getToken();
    }

    @Override
    public void sendImage(int token, Bitmap image) {
        getView().showProgress();
        repository.sendImage(token, image, new TokenRepository.ImageSendingListener() {
            @Override
            public void onSuccess() {
                getView().hideProgress();
                getView().showSuccess();
            }

            @Override
            public void onError(int error) {
                getView().showError();
                Log.d("image error", "not sent");
            }
        });
    }
}
