package com.maxi.mvvm.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.maxi.mvvm.R;
import com.maxi.mvvm.utils.CommoUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by maxi on 2021/9/23.
 */
public class TextInfoView extends ConstraintLayout {
    private TextView tvLabel, tvValue;
    private View vLine;

    public TextInfoView(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public TextInfoView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInfoView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        obtainAttributes(context, attrs);
    }

    private void initView(Context context) {
        View baseView = LayoutInflater.from(context).inflate(R.layout.view_text_info, this);
        tvLabel = baseView.findViewById(R.id.tv_label);
        tvValue = baseView.findViewById(R.id.tv_value);
        vLine = baseView.findViewById(R.id.v_line1);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextInfoView);

        int count = ta.getIndexCount();

        String textValue = "";
        int maxLines = 1;

        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.TextInfoView_textValue:
                    textValue = ta.getString(attr);
                    break;
                case R.styleable.TextInfoView_maxLines:
                    maxLines = ta.getInt(attr, 1);
                    break;
            }
        }
        tvLabel.setText(textValue);
        if (maxLines > 1) {
            tvValue.setSingleLine(false);
            tvValue.setMaxLines(maxLines);
        }
        ta.recycle();
    }

    public void setTextValue(String text) {
        text = CommoUtil.isNull(text) ? "无" : text;
        tvValue.setText(text);
    }
}