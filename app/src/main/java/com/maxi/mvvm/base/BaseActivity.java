package com.maxi.mvvm.base;

/**
 * Created by maxi on 2021/9/3.
 */

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.R;
import com.maxi.mvvm.common.LoadState;
import com.maxi.mvvm.databinding.ActivityBaseBinding;
import com.maxi.mvvm.databinding.ViewLoadErrorBinding;
import com.maxi.mvvm.databinding.ViewNoDataBinding;
import com.maxi.mvvm.databinding.ViewNoNetworkBinding;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private ActivityBaseBinding mActivityBaseBinding;
    private ViewLoadErrorBinding mViewLoadErrorBinding;
    private ViewNoNetworkBinding mViewNoNetworkBinding;
    private ViewNoDataBinding mViewNoDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        if (needInitImmersionBar()) {
            initImmersionBar();
        }
    }

    /**
     * 默认返回true
     *
     * @return
     */
    protected boolean needInitImmersionBar() {
        return true;
    }

    protected boolean noToolBar() {
        return false;
    }

    protected void initImmersionBar() {
        View top = topViewForImmersionBar();
        if (top == null) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.white)
                    .fitsSystemWindows(true)
                    .init();
        } else {
            ImmersionBar.with(this)
                    .transparentStatusBar()
                    .titleBar(top)
                    .init();
        }
    }

    protected View topViewForImmersionBar() {
        return mActivityBaseBinding.ltvTop.rlToolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivityBaseBinding != null) {
            mActivityBaseBinding.unbind();
        }
        if (binding != null) {
            binding.unbind();
        }
        if (mViewLoadErrorBinding != null) {
            mViewLoadErrorBinding.unbind();
        }
        if (mViewNoNetworkBinding != null) {
            mViewNoNetworkBinding.unbind();
        }
        if (mViewNoDataBinding != null) {
            mViewNoDataBinding.unbind();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        mActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        binding = DataBindingUtil.inflate(getLayoutInflater(), initContentView(savedInstanceState),
                mActivityBaseBinding.flContentContainer, true);
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        // ViewModel订阅生命周期事件
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
        registorCommonLiveDataCallBack();
        if (noToolBar() && topViewForImmersionBar() != null) {
            topViewForImmersionBar().setVisibility(View.GONE);
        }
        init();
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    private void registorCommonLiveDataCallBack() {
        if (viewModel != null) {
            if (viewModel instanceof ToolbarViewModel) {
                mActivityBaseBinding.ltvTop.setToolbarViewModel((ToolbarViewModel) viewModel);
            }
            viewModel.loadState.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    switchLoadView(loadState);
                }
            });
            viewModel.toastMsg.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String msg) {
                    Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.finishEvent.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    finish();
                }
            });
            viewModel.onBackPressedEvent.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    public abstract void init();

    private void removeLoadView() {
        int childCount = mActivityBaseBinding.flContentContainer.getChildCount();
        if (childCount > 1) {
            mActivityBaseBinding.flContentContainer.removeViews(1, childCount - 1);
        }
    }

    private void switchLoadView(@LoadState int loadState) {
        removeLoadView();

        switch (loadState) {
            case LoadState.LOADING:
                break;

            case LoadState.NO_NETWORK:
                if (mViewNoNetworkBinding == null) {
                    mViewNoNetworkBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_network,
                            mActivityBaseBinding.flContentContainer, false);
                    mViewNoNetworkBinding.setViewModel(viewModel);
                } else if (mViewNoNetworkBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewNoNetworkBinding.getRoot().getParent()).removeView(mViewNoNetworkBinding.getRoot());
                }
                mActivityBaseBinding.flContentContainer.addView(mViewNoNetworkBinding.getRoot());
                break;

            case LoadState.NO_DATA:
                if (mViewNoDataBinding == null) {
                    mViewNoDataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_data,
                            mActivityBaseBinding.flContentContainer, false);
                } else if (mViewNoDataBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewNoDataBinding.getRoot().getParent()).removeView(mViewNoDataBinding.getRoot());
                }
                mActivityBaseBinding.flContentContainer.addView(mViewNoDataBinding.getRoot());
                break;

            case LoadState.ERROR:
                if (mViewLoadErrorBinding == null) {
                    mViewLoadErrorBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_load_error,
                            mActivityBaseBinding.flContentContainer, false);
                } else if (mViewLoadErrorBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewLoadErrorBinding.getRoot().getParent()).removeView(mViewLoadErrorBinding.getRoot());
                }
                mViewLoadErrorBinding.setViewModel(viewModel);
                mActivityBaseBinding.flContentContainer.addView(mViewLoadErrorBinding.getRoot());
                break;

            default:
                break;
        }
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
}