package com.iliasahmed.testpluang.ui.base;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.iliasahmed.testpluang.BR;

@SuppressLint("SetTextI18n")
public abstract class BaseActivity<DATA_BINDING extends ViewDataBinding, VIEW_MODEL extends ViewModel> extends AppCompatActivity {

    protected VIEW_MODEL viewModel;
    protected DATA_BINDING binding;
    protected Intent intent;
    private Class<VIEW_MODEL> viewModelClassType;
    @LayoutRes
    private int layoutId;

    public BaseActivity(Class<VIEW_MODEL> viewModelClassType, int layoutId) {
        this.viewModelClassType = viewModelClassType;
        this.layoutId = layoutId;
    }

    public BaseActivity() {

    }

    public void adjustFontScale(Configuration configuration) {

        try {

            configuration.fontScale = (float) 1.05;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            assert wm != null;
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            try {
                if (!(Build.MANUFACTURER.toLowerCase().contains("meizu")) && Build.MODEL.contains("RNE-L22"))
                    configuration.densityDpi = ((int) getResources().getDisplayMetrics().xdpi);
            } catch (Exception e) {
                configuration.densityDpi = ((int) getResources().getDisplayMetrics().xdpi);
            }
            getBaseContext().getResources().updateConfiguration(configuration, metrics);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(Color.BLACK);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preSuper();
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());

        preBind();

        binding = DataBindingUtil.setContentView(this, layoutId);
        viewModel = new ViewModelProvider(this).get(viewModelClassType);
        binding.setVariable(BR.viewModel, viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
        intent = getIntent();

        initViews();
        clickListeners();
        liveEventsObservers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9187) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                finish();
                startActivity(getIntent());
            }
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    protected void preSuper() {

    }

    protected void preBind() {

    }

    protected abstract void initViews();

    protected abstract void liveEventsObservers();

    protected abstract void clickListeners();

}
