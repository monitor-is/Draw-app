package com.mlh.mlh;

import android.graphics.Bitmap;

public class TokenContract {

    interface View {
        void showError();
        void beginDrawing();
        void showProgress();
        void hideProgress();
        void showSuccess();
    }

    interface Presenter<T extends View> {
        void start(T view);
        void stop();
        T getView();
        void validateToken(int token);
        void sendImage(int token, Bitmap image);
        int getToken();
    }
}
