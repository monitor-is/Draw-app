package com.mlh.mlh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.divyanshu.draw.activity.DrawingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TokenContract.View {

    private TokenPresenter<MainActivity> presenter;
    @BindView(R.id.et_order) EditText etOrderToken;
    @BindView(R.id.btn_connect) Button btnConnect;
    @BindView(R.id.spinner) ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new TokenPresenter<>(this);
        presenter.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.stop();
        }
    }

    @Override
    public void showError() {
        Snackbar.make(btnConnect, "Произошла ошибка, попробуйте еще раз", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void beginDrawing() {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_DRAW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int token = presenter.getToken();
        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_DRAW) {
                byte[] result = data.getByteArrayExtra("bitmap");
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                presenter.sendImage(token, bitmap);
            }
        }
    }

    @Override
    public void showSuccess() {
        Snackbar.make(btnConnect, "Изображение отправлено", Snackbar.LENGTH_LONG)
                .show();
    }

    @OnClick(R.id.btn_connect)
    public void connect(View view) {
        hideKeyboard();
        if (etOrderToken.getText().toString().isEmpty()) {
            showError();
        } else {
            int token = Integer.valueOf(etOrderToken.getText().toString());
            presenter.validateToken(token);
        }
    }

    private void hideKeyboard() {
        etOrderToken.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void showProgress() {
        spinner.setVisibility(View.VISIBLE);
        btnConnect.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        spinner.setVisibility(View.GONE);
        btnConnect.setEnabled(true);
    }
}
