package com.maxi.mvvm.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.gyf.immersionbar.ImmersionBar;
import com.maxi.mvvm.R;
import com.maxi.mvvm.common.LoadState;
import com.maxi.mvvm.databinding.FragmentBaseBinding;
import com.maxi.mvvm.databinding.ViewLoadErrorBinding;
import com.maxi.mvvm.databinding.ViewNoDataBinding;
import com.maxi.mvvm.databinding.ViewNoNetworkBinding;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by maxi on 2021/9/3.
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseVisibilityFragment {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;

    private FragmentBaseBinding mFragmentBaseBinding;

    private ViewLoadErrorBinding mViewLoadErrorBinding;

    private ViewNoNetworkBinding mViewNoNetworkBinding;

    private ViewNoDataBinding mViewNoDataBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentBaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState),
                mFragmentBaseBinding.flContentContainer, true);
        return mFragmentBaseBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentBaseBinding != null) {
            mFragmentBaseBinding.unbind();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        initView();
    }

    @Override
    protected void onVisibilityChanged(boolean visible) {
        super.onVisibilityChanged(visible);
        if (visible) {
            if (isImmersionBarEnabled()) {
                initImmersionBar();
            }
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
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
        binding.setVariable(viewModelId, viewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
        registorCommonLiveDataCallBack();
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    public abstract void initView();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }

    private boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true).init();
    }

    private void registorCommonLiveDataCallBack() {
        if (viewModel != null) {
            viewModel.loadState.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    switchLoadView(loadState);
                }
            });
            viewModel.toastMsg.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            viewModel.finishEvent.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    getActivity().finish();
                }
            });
            viewModel.onBackPressedEvent.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@LoadState Integer loadState) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    private void removeLoadView() {
        int childCount = mFragmentBaseBinding.flContentContainer.getChildCount();
        if (childCount > 1) {
            mFragmentBaseBinding.flContentContainer.removeViews(1, childCount - 1);
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
                            mFragmentBaseBinding.flContentContainer, false);
                    mViewNoNetworkBinding.setViewModel(viewModel);
                } else if (mViewNoNetworkBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewNoNetworkBinding.getRoot().getParent()).removeView(mViewNoNetworkBinding.getRoot());
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewNoNetworkBinding.getRoot());
                break;

            case LoadState.NO_DATA:
                if (mViewNoDataBinding == null) {
                    mViewNoDataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_no_data,
                            mFragmentBaseBinding.flContentContainer, false);
                } else if (mViewNoDataBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewNoDataBinding.getRoot().getParent()).removeView(mViewNoDataBinding.getRoot());
                }
                mFragmentBaseBinding.flContentContainer.addView(mViewNoDataBinding.getRoot());
                break;

            case LoadState.ERROR:
                if (mViewLoadErrorBinding == null) {
                    mViewLoadErrorBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_load_error,
                            mFragmentBaseBinding.flContentContainer, false);
                } else if (mViewLoadErrorBinding.getRoot().getParent() != null) {
                    ((ViewGroup) mViewLoadErrorBinding.getRoot().getParent()).removeView(mViewLoadErrorBinding.getRoot());
                }
                mViewLoadErrorBinding.setViewModel(viewModel);
                mFragmentBaseBinding.flContentContainer.addView(mViewLoadErrorBinding.getRoot());
                break;

            default:
                break;
        }
    }

}
