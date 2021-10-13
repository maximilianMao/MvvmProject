package com.maxi.mvvm.ui.widget;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.maxi.mvvm.R;
import com.maxi.mvvm.base.BaseDialogFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 通用dialog
 * <p>
 * Created by maxi on 2020-01-17.
 */
public class CommonDialog extends BaseDialogFragment implements View.OnClickListener {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            CommonDialogType.TYPE_NORMAL, CommonDialogType.TYPE_WITH_EDITTEXT
    })
    public @interface CommonDialogType {
        int TYPE_NORMAL = 0;//标题带详情的弹框
        int TYPE_WITH_EDITTEXT = 1;//标题带输入框的弹框
    }

    private TextView tvTitle;
    private TextView tvDes;
    private EditText etDes;
    private TextView tvDisagree;
    private TextView tvAgree;
    private View vLine;

    private @CommonDialogType
    int dialogType;
    private String title;
    private String des;
    private String cancel;
    private String enter;
    private int maxInputSize;
    private boolean cancelAble;
    private int delayTime;

    private CommonCallback commonCallback;
    private Disposable countdownDisposable;

    private CommonDialog(Builder builder) {
        this.dialogType = builder.dialogType;
        this.title = builder.title;
        this.des = builder.des;
        this.cancel = builder.cancel;
        this.enter = builder.enter;
        this.cancelAble = builder.cancelAble;
        this.commonCallback = builder.commonCallback;
        this.maxInputSize = builder.maxInputSize;
        this.delayTime = builder.delayTime;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(cancelAble);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismissAllowingStateLoss();
        if (countdownDisposable != null && !countdownDisposable.isDisposed()) {
            countdownDisposable.dispose();
        }
        countdownDisposable = null;
    }

    private void setDialogParams(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.dimAmount = 0.0f;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setStatusBarColor(Color.parseColor("#8c000000"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.dialog_common, container);
        //设置全屏
        Window window = this.getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            setDialogParams(window);
        }
        initView(parentView);
        return parentView;
    }

    private void initView(View parentView) {
        tvTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvDes = (TextView) parentView.findViewById(R.id.tv_desc);
        etDes = (EditText) parentView.findViewById(R.id.et_desc);
        tvDisagree = (TextView) parentView.findViewById(R.id.tv_disagree);
        tvAgree = (TextView) parentView.findViewById(R.id.tv_agree);
        vLine = parentView.findViewById(R.id.view_line);
        if (TextUtils.isEmpty(cancel)) {
            tvDisagree.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        } else {
            tvDisagree.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
            tvDisagree.setText(cancel);
        }
        if (TextUtils.isEmpty(des)) {
            tvDes.setVisibility(View.GONE);
        } else {
            tvDes.setVisibility(View.VISIBLE);
        }
        switch (dialogType) {
            case CommonDialogType.TYPE_NORMAL:
                etDes.setVisibility(View.GONE);
                tvDes.setText(des);
                break;
            case CommonDialogType.TYPE_WITH_EDITTEXT:
                tvDes.setVisibility(View.GONE);
                etDes.setHint(des);
                break;
        }
        tvTitle.setText(title);
        tvTitle.setText(title);
        tvAgree.setText(enter);

        tvDes.setMovementMethod(ScrollingMovementMethod.getInstance());

        tvAgree.setOnClickListener(this);
        tvDisagree.setOnClickListener(this);

        etDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                int remainCount = maxInputSize - s.length();
                if (remainCount >= 0 && remainCount < maxInputSize) {
                } else {
                    String str = s.toString();
                    if (str.length() >= maxInputSize) {
                        str = str.substring(0, maxInputSize);
                        etDes.setText(str);
                        etDes.setSelection(str.length());
                        Toast.makeText(getActivity(), "字数超过限制：" + maxInputSize + "字", Toast.LENGTH_SHORT).show();
//                        MyToast.showToast(getActivity(), "字数超过限制：" + maxInputSize + "字", 1, MySequent.RED);
                    }
                }
            }
        });
        initDelay();
    }

    private void initDelay() {
        if (delayTime <= 0 || dialogType == CommonDialogType.TYPE_WITH_EDITTEXT) {
            return;
        }
        countdownDisposable = Flowable.intervalRange(0, delayTime + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvAgree.setEnabled(false);
                        tvAgree.setTextColor(Color.parseColor("#40000000"));
                        tvAgree.setText(enter + "(" + (delayTime - aLong) + "s)");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕事件处理
                        tvAgree.setText(enter);
                        tvAgree.setTextColor(Color.parseColor("#ff1677ff"));
                        tvAgree.setEnabled(true);
                    }
                })
                .subscribe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_agree:
                if (commonCallback != null) {
                    String editContent = dialogType == CommonDialogType.TYPE_WITH_EDITTEXT ? etDes.getText().toString() : "";
                    if (dialogType == CommonDialogType.TYPE_WITH_EDITTEXT && TextUtils.isEmpty(editContent)) {
                        Toast.makeText(getActivity(), TextUtils.isEmpty(des) ? "输入不能为空" : des, Toast.LENGTH_SHORT).show();
//                        MyToast.showToast(getActivity(), TextUtils.isEmpty(des) ? "输入不能为空" : des, 1, MySequent.RED);
                        return;
                    }
                    commonCallback.dialogEnter(editContent);
                }
                dismiss();
                break;
            case R.id.tv_disagree:
                if (commonCallback != null) {
                    commonCallback.dialogCancel();
                }
                dismiss();
                break;
        }
    }

    public void setCommonCallback(CommonCallback commonCallback) {
        this.commonCallback = commonCallback;
    }

    public interface CommonCallback {
        //标题带输入框的弹框 editTextDes才有值 否则为""
        void dialogEnter(String editTextDes);

        void dialogCancel();
    }

    public static final class Builder {
        private @CommonDialogType
        int dialogType = CommonDialogType.TYPE_NORMAL;
        private String title;
        private String des;
        private String cancel;
        private String enter;
        private int maxInputSize = 30;
        private boolean cancelAble = false;
        private int delayTime = -1;
        private CommonCallback commonCallback;

        public Builder() {
        }

        public Builder(String title, String des, String enter) {
            this.title = title;
            this.des = des;
            this.enter = enter;
        }

        public Builder dialogType(@CommonDialogType int dialogType) {
            this.dialogType = dialogType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder des(String des) {
            this.des = des;
            return this;
        }

        public Builder cancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder enter(String enter) {
            this.enter = enter;
            return this;
        }

        public Builder cancelAble(boolean cancelAble) {
            this.cancelAble = cancelAble;
            return this;
        }

        public Builder commonCallback(CommonCallback commonCallback) {
            this.commonCallback = commonCallback;
            return this;
        }

        public Builder maxInputSize(int maxInputSize) {
            this.maxInputSize = maxInputSize;
            return this;
        }

        public Builder delayTime(int delayTime) {
            this.delayTime = delayTime;
            return this;
        }

        public CommonDialog build() {
            CommonDialog commonDialog = new CommonDialog(this);
            return commonDialog;
        }
    }
}
