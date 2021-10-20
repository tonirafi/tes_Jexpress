package com.template.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class KeyboardHelper extends FrameLayout {

    public static KeyboardHelper inject(Activity activity) {
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        for (int i = 0, c = decorView.getChildCount(); i < c; i++) {
            if (decorView.getChildAt(i) instanceof KeyboardHelper) {
                return (KeyboardHelper) decorView.getChildAt(i);
            }
        }

        final KeyboardHelper keyboardHelper = new KeyboardHelper(activity);
        decorView.addView(keyboardHelper);
        return keyboardHelper;
    }

    static void showKeyboard(final EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }
        });
    }

    static void hideKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private final int statusBarHeight;
    private final int navigationBaHeight;

    private int viewInset = -1;
    private int keyboardHeight;

    //    private List<WeakReference<KeyboardHelper.Listener>> keyboardListener = new ArrayList<>();
    private List<Listener> keyboardListener = new ArrayList<>();
    private SizeListener keyboardSizeListener;

    private EditText inputTrap;

    private KeyboardHelper(@NonNull Activity activity) {
        super(activity);
        this.statusBarHeight = getStatusBarHeight();
        this.navigationBaHeight = getNavigationBarHeight();
        setLayoutParams(new ViewGroup.LayoutParams(0, 0));

        inputTrap = new EditText(activity);
        inputTrap.setFocusable(true);
        inputTrap.setFocusableInTouchMode(true);
        inputTrap.setVisibility(View.VISIBLE);
        inputTrap.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        inputTrap.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES);

        addView(inputTrap);

        final View rootView = activity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardTreeObserver(activity));
    }

    private int getKeyboardHeight(Activity activity) {
        final Rect r = new Rect();
        final View view = activity.getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(r);
        final int keyboardHeight = getViewPortHeight() - (r.bottom - r.top);
        return keyboardHeight < 0 ? 0 : keyboardHeight;
    }

    private int getViewPortHeight() {
        return getRootView().getHeight() - statusBarHeight - getCachedInset();
    }

    /**
     * 顶部状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        final int statusBarRes = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return statusBarRes > 0 ? getResources().getDimensionPixelSize(statusBarRes) : 0;
    }

    /**
     * 底部底部导航栏高度
     *
     * @return
     */
    private int getNavigationBarHeight() {
        int navigationBarRes = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return navigationBarRes > 0 ? getResources().getDimensionPixelSize(navigationBarRes) : 0;
    }

    private int getCachedInset() {
        if (viewInset == -1) {
            viewInset = getViewInset();
        }

        return viewInset;
    }

    private int getViewInset() {
        int height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //当项目targetSdkVersion>28时  代码在Android 10 API29 及以上设备运行时时 无法获取该数据
            //将项目targetSdkVersion设置为28及以下时  可规避该问题
            try {
                Field attachInfoField = View.class.getDeclaredField("mAttachInfo");
                attachInfoField.setAccessible(true);
                Object attachInfo = attachInfoField.get(this);
                if (attachInfo != null) {
                    Field stableInsetsField = attachInfo.getClass().getDeclaredField("mStableInsets");
                    stableInsetsField.setAccessible(true);
                    Rect insets = (Rect) stableInsetsField.get(attachInfo);
                    height = insets.bottom;
//                    return insets.bottom;
                }
            } catch (Exception e) {
                // well .... at least we tried
                height = navigationBaHeight;
            }
        }

        return height;
    }

    public EditText getInputTrap() {
        return inputTrap;
    }

    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    public boolean isKeyboardVisible() {
        return keyboardHeight > 0;
    }

    public void addListener(Listener keyboardListener) {
//        this.keyboardListener.add(new WeakReference<>(keyboardListener));
        this.keyboardListener.add(keyboardListener);
    }

    public void setKeyboardHeightListener(SizeListener sizeListener) {
        this.keyboardSizeListener = sizeListener;
    }

    public void clearAllListeners() {
        this.keyboardListener.clear();
        this.keyboardSizeListener = null;
    }

    private class KeyboardTreeObserver implements ViewTreeObserver.OnGlobalLayoutListener {

        private final Activity activity;

        private KeyboardTreeObserver(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onGlobalLayout() {
            final int keyboardHeight = getKeyboardHeight(activity);

            if (!keyboardListener.isEmpty()) {
                if (keyboardHeight > 0) {
                    if (KeyboardHelper.this.keyboardHeight == 0) {
                        notifyKeyboardVisible();
                    }

                } else {
                    if (KeyboardHelper.this.keyboardHeight > 0) {
                        notifyKeyboardDismissed();
                    }
                }
            }

//            if (keyboardSizeListener != null && KeyboardHelper.this.keyboardHeight != keyboardHeight) {
//                keyboardSizeListener.onSizeChanged(keyboardHeight);
//            }

            if (KeyboardHelper.this.keyboardHeight != keyboardHeight) {
                KeyboardHelper.this.keyboardHeight = keyboardHeight;
                if (keyboardSizeListener != null) {
                    keyboardSizeListener.onSizeChanged(keyboardHeight);
                }
            }
        }
    }

    private void notifyKeyboardVisible() {
//        for (WeakReference<KeyboardHelper.Listener> listeners : keyboardListener) {
//            if (listeners.get() != null) {
//                listeners.get().onKeyboardVisible();
//            }
//        }

        for (Listener li : keyboardListener) {
            if (li != null)
                li.onKeyboardVisible();
        }
    }

    private void notifyKeyboardDismissed() {
//        for (WeakReference<KeyboardHelper.Listener> listeners : keyboardListener) {
//            if (listeners.get() != null) {
//                listeners.get().onKeyboardDismissed();
//            }
//        }

        for (Listener li : keyboardListener) {
            if (li != null)
                li.onKeyboardDismissed();
        }
    }

    public interface Listener {
        void onKeyboardVisible();

        void onKeyboardDismissed();
    }

    public interface SizeListener {
        void onSizeChanged(int keyboardHeight);
    }

}
