package com.template.project.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.template.project.MyApplication;
import com.template.project.R;
import com.template.project.http.bean.User;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppUtil {

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("wtf", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    public static String extractYoutubeId(String url) {
        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }


    public static String getThubnailYoutube(String videoId){
        String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg";
        return  img_url;
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void openBrowser(Context context, String url){
        Intent appIntent = new Intent(Intent.ACTION_VIEW);
        appIntent.setData(Uri.parse(url));
        context.startActivity(appIntent);
    }

    /**
     * 同上 但会将空格转化为 %20
     *
     * @param s
     * @return
     */
    public static String uriEncode(String s) {
        return Uri.encode(s);
    }
//
//    /**
//     * 因为在onCreate中我用mBtn.setBackgroundResource(R.drawable.splash)为控件设置背景图，然后在onDestroy中会用((BitmapDrawable)mBtn.getBackground()).setCallback(null)清理背景图。按道理来说图片资源应该已经清理掉了的。百思不得其解，仔细看Bitmap的源代码，它其实起的作用是销毁java对象BitmapDrawable，而android为了提高效率，Bitmap真正的位图数据是在ndk中用c写的，所以用setCallback是不能销毁位图数据的，应该调用Bitmap的recycle()来清理内存。
//     * <p>
//     * 所以想当然的在onDestroy加上((BitmapDrawable)mBtn.getBackground()).getBitmap().recycle()，这样跑下来，内存情况很理想，不管在哪个activity中，使用的资源仅仅是当前activity用到的，就不会象之前到最后一个activity的时候，所有之前使用的资源都累积在内存中。在每个activity资源和class等使用的内存都在10m左右，已经很理想了（当然如果是在android低版本比如1.5,16时还是不行的，这得重新构架应用），可以为显示pdf预留了比较多内存了。
//     * <p>
//     * 但新的问题又出现了，当返回之前的activity时，会出现“try to use a recycled bitmap"的异常。这真是按了葫芦起了瓢啊，内心那个沮丧。。。没办法，继续分析。看来是后加上recycle引起的， 位图肯定在内存中有引用，在返回之前的activity时，因为位图数据其实已经被销毁了，所以才造成目前的情况。在看了setBackgroundResource的源码以后，恍然大悟，android对于直接通过资源id载入的资源其实是做了cache的了，这样下次再需要此资源的时候直接从cache中得到，这也是为效率考虑。但这样做也造成了用过的资源都会在内存中，这样的设计不是很适合使用了很多大图片资源的应用，这样累积下来应用的内存峰值是很高的。看了sdk后，我用：
//     * <p>
//     * Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.splash);
//     * BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
//     * <p>
//     * mBtn.setBackgroundDrawable(bd);
//     * <p>
//     * 来代替mBtn.setBackgroundResource(R.drawable.splash)。
//     * <p>
//     * 销毁的时候使用：
//     * <p>
//     * BitmapDrawable bd ＝ (BitmapDrawable)mBtn.getBackground();
//     * <p>
//     * mBtn.setBackgroundResource(0);//别忘了把背景设为null，避免onDraw刷新背景时候出现used a recycled bitmap错误
//     * <p>
//     * bd.setCallback(null);
//     * bd.getBitmap().recycle();
//     * <p>
//     * 这样调整后，避免了在应用里缓存所有的资源，节省了宝贵的内存，
//     * <p>
//     * <p>
//     * 此方式加载的图片资源只为进行inSampleSize 采样缩放 不会进行inTargetDensity/inDensity缩放处理
//     * 请注意在合适的位置调用  {@link #recycleBackground(View)} 进行资源释放
//     *
//     * @param view
//     * @param resId
//     */
//    public static void setBackgroundResourceForView(View view, @DrawableRes int resId) {
//       LogUtil.logI("setBackgroundResourceForView");
//        Bitmap bm = generateBitmapForView(view, resId);
//        if (bm != null) {
//            BitmapDrawable bd = new BitmapDrawable(view.getResources(), bm);
//            LogUtil.logI("setBackgroundResourceForView bm");
//            view.setBackground(bd);
//        }
//    }
//
//    /**
//     * 此方式加载的图片资源只为进行inSampleSize采样缩放 不会进行inTargetDensity/inDensity缩放处理
//     * 请注意在合适的位置调用 {@link #recycleImageView(ImageView)}  进行资源释放
//     *
//     * @param view
//     * @param resId
//     */
//    public static void setImgResourceForImageView(ImageView view, @DrawableRes int resId) {
//        LogUtil.logI("setBackgroundResourceForImageView");
//        Bitmap bm = generateBitmapForView(view, resId);
//        if (bm != null) {
//            LogUtil.logI("setBackgroundResourceForImageView bm");
//            view.setImageBitmap(bm);
//        }
//    }

//    public static Bitmap generateBitmapForView(View view, @DrawableRes int resId) {
//        LogUtil.logI("generateBitmapForView");
//        if (view == null)
//            return null;
//
//        int height = view.getMeasuredHeight(), width = view.getMeasuredWidth();
//        LogUtil.logI(String.format("generateBitmapForView  width=%s height=%s", width, height));
//        LogUtil.logI(String.format("generateBitmapForView  width=%s height=%s", view.getWidth(), view.getHeight()));
//
//        if (width == 0) {
//            if (height == 0) {
//                view.measure(0, 0);
//                LogUtil.logI("generateBitmapForView   view.measure");
//                width = view.getMeasuredWidth();
//                height = view.getMeasuredHeight();
//            } else {
//                width = height;
//            }
//        } else {
//            if (height == 0) {
//                height = width;
//                LogUtil.logI("generateBitmapForView   height = width");
//            }
//        }
//
//        LogUtil.logI(String.format("generateBitmapForView  width=%s height=%s", width, height));
//
//        return Utils.decodeResource(view.getContext(), resId, width, height);
//    }

    public static boolean isYoutubeUrl(String youTubeURl)
    {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern))
        {
            success = true;
        }
        else
        {
            // Not Valid youtube URL
            success = false;
        }
        return success;
    }

    public static void recycleBackground(View view) {
        if (view != null && view.getBackground() != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            recycleBitmapDrawable(bd);
            view.setBackgroundResource(0);
        }
    }

    public static void recycleImageView(ImageView imageView) {
        if (imageView != null) {
            if (imageView.getDrawable() != null) {//释放图片资源
                recycleBitmapDrawable(imageView.getDrawable());
                imageView.setImageDrawable(null);
            }
            //释放背景资源
            recycleBackground(imageView);
        }
    }

    public static void recycleBitmapDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                recycleBitmap(((BitmapDrawable) drawable).getBitmap());
            }
            drawable.setCallback(null);
        }
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    public static String getAppH5CachePath(Context context) {
        return context.getApplicationContext().getFilesDir().getAbsolutePath() + "H5Cache/";
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static int getAppVersionCode(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            return (int) getPackageInfo(context).getLongVersionCode();
        }
        return getPackageInfo(context).versionCode;
    }

    public static String getAppVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 是否是系统预装app
     * <p>
     * (flags & ApplicationInfo.FLAG_SYSTEM) != 0 表示系统程序
     * (flags & ApplicationInfo.FLAG_SYSTEM) <= 0  表示第三方应用程序
     * (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0  表示系统程序被手动更新后，也成为第三方应用程序
     *
     * @param context
     * @return
     */
    public static Boolean isPreInstalledApp(Context context) {
        int flags = getPackageInfo(context).applicationInfo.flags;
        return (flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static String getAppMetaData(Context context, String key) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);

            return applicationInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static boolean isGooglePlayServiceAvailable(Context context) {
//        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
//        if (status == ConnectionResult.SUCCESS) {
//            com.qschou.pedulisehat.android.utils.LogUtil.logE("GooglePlayService", "GooglePlayServicesUtil service is available.");
//            return true;
//        }
//
//        com.qschou.pedulisehat.android.utils.LogUtil.logE("GooglePlayService", "GooglePlayServicesUtil service is NOT available.");
//        return false;
//    }
//
//
//    /**
//     * Check the device to make sure it has the Google Play Services APK. If
//     * it doesn't, display a dialog that allows users to download the APK from
//     * the Google Play Store or enable it in the device's system settings.
//     */
//    public static boolean checkIsGooglePlayServiceAvailable(AppCompatActivity context) {
////        int statusCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
////        if (statusCode != ConnectionResult.SUCCESS) {
////            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(context);
////        }
//
//        if (BuildConfig.DEBUG) return true;
//
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(context, resultCode, AppConstants.RequestCode.RSC_PLAY_SERVICES_RESOLUTION_REQUEST
//                        , dialog -> {
////                            dialog.dismiss();
//                            context.finish();
//                        }).show();
//            } else {
//                LogUtil.logI("Google play service is not supported on this device .");
//                context.finish();
//            }
//            return false;
//        }
//        return true;
//    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    public static void insertStatusBarHeight2TopPadding(View view) {
        setTopPadding(view, view.getPaddingTop() + getStatusBarHeight(view.getContext()));
    }

    public static void setBottomPadding(View view, int bottomPadding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottomPadding);
    }

    public static void setTopPadding(View view, int topPadding) {
        view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static <T> T getFieldInstance(Object object, String fieldName, Class<T> clz) {
        if (object == null) return null;

        try {
            Field declaredField = object.getClass().getDeclaredField(fieldName);
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }

            Object field = declaredField.get(object);
            if (clz.isInstance(field)) {
                return clz.cast(field);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


//    public static void fixFacebookLeak() {
//        try {
//
//            Field f = ActivityLifecycleTracker.class.getDeclaredField("viewIndexer");
//            if (!f.isAccessible()) {
//                f.setAccessible(true);
//            }
//
//            Object fieldVal = f.get(null);
//            if (fieldVal instanceof ViewIndexer) {
//                ((ViewIndexer) fieldVal).unschedule();
//                Timer timer = getFieldInstance(fieldVal, "indexingTimer", Timer.class);
//                if (timer != null) {
//                    timer.cancel();
//                }
//
//                Handler handler = getFieldInstance(fieldVal, "uiThreadHandler", Handler.class);
//                if (handler != null) {
//                    handler.removeCallbacksAndMessages(null);
//                }
//            }
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }

    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) return;
        InputMethodManager inputMethodManager = null;
        try {
            inputMethodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (inputMethodManager == null) return;
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        for (int i = 0; i < 3; i++) {
            try {
                Field declaredField = inputMethodManager.getClass().getDeclaredField(strArr[i]);
                if (declaredField == null) continue;
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(inputMethodManager);
                if (obj == null || !(obj instanceof View)) continue;
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(inputMethodManager, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private final static String UNREAD_MSG_COUNT = "UnreadNotificationCount";
    private final static String DONOR_VERSION = "donorVersion";

//    public static void removeUser(String userId) {
//        if (userId == null) return;
//        final String key = String.format("user[%s]", userId);
//        SharedPreferencesTool.getInstance(MyApplication.getContext()).putString(key, null);
//    }
//
    public static void saveUser(User user) {
        if (user == null) return;
        final String key = String.format("user[%s]", user.getUserId());
        SharedPreferencesTool.getInstance(MyApplication.getContext()).putObject(key, user);
    }
//
    public static User getUser(String userId) {
        if (userId == null) return null;
        final String key = String.format("user[%s]", userId);
        return SharedPreferencesTool.getInstance(MyApplication.getContext()).getObject(key, User.class, null);
    }
//
    public static int getUnreadNotificationCount(String user_id) {
        if (TextUtils.isEmpty(user_id)) return 0;
        return SharedPreferencesTool.getInstance(MyApplication.getContext()).getInt(UNREAD_MSG_COUNT + user_id, 0);
    }
//
    public static void updateUnreadNotificationCount(String user_id, int count) {
        if (TextUtils.isEmpty(user_id)) return;
        SharedPreferencesTool.getInstance(MyApplication.getContext()).putInt(UNREAD_MSG_COUNT + user_id, count);
    }
//
    public static boolean getDonorVersionVal(String user_id) {
        if (TextUtils.isEmpty(user_id)) return true;
        return SharedPreferencesTool.getInstance(MyApplication.getContext()).getBoolean(DONOR_VERSION + user_id, true);
    }
//
    public static void saveDonorVersionVal(String user_id, boolean donorVersion) {
        if (TextUtils.isEmpty(user_id)) return;
        SharedPreferencesTool.getInstance(MyApplication.getContext()).putBoolean(DONOR_VERSION + user_id, donorVersion);
    }
//
//
    public static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        if (drawable == null) return null;
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
//
    private static SparseArray<Typeface> fontCache = new SparseArray<>(2);

public static Typeface getFont(Context context, @FontRes int id) {
    if (id == -1) { //id=-1 do preload and cache font resource
        getFont(context, R.font.helvetica);
        getFont(context, R.font.helvetica);
        getFont(context, R.font.helvetica);
        return null;
    }

    Typeface typeface = fontCache.get(id);
    if (typeface != null) {
        return typeface;
    }

    try {
        ResourcesCompat.getFont(context.getApplicationContext(), id, new ResourcesCompat.FontCallback() {
            @Override
            public void onFontRetrieved(@NonNull Typeface typeface) {
                fontCache.append(id, typeface);
            }

            @Override
            public void onFontRetrievalFailed(int reason) {

            }
        }, null);
//        } catch (Resources.NotFoundException e) {
    } catch (Exception e) {
        e.printStackTrace();
    }

    return id != R.font.helvetica ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
}
//
//    /**
//     * 根据本应用 页面路由规则 判断a/b属于是否同一个页面路由
//     *
//     * @param a
//     * @param b
//     * @return
//     */
    public static boolean isRouterUriMatch(Uri a, Uri b) {
        if (a == null || b == null) return false;

        if (!a.equals(b)) {
            if (a.getScheme() == null || !a.getScheme().equals(b.getScheme()))
                return false;

            if (a.getAuthority() == null || !a.getAuthority().equals(b.getAuthority()))
                return false;

            if (TextUtils.isEmpty(a.getPath())) {
                return TextUtils.isEmpty(b.getPath());
            }

            return a.getPath().equals(b.getPath());
        }

        return true;
    }
//
//    public static boolean isValidCode(Context context, String verificationCode) {
//        if (TextUtils.isEmpty(verificationCode)) {
//            ToastUtil.show(context.getString(R.string.captcha_is_empty));
//            return false;
//        }
//
//        if (!TextUtils.isDigitsOnly(verificationCode) || context.getResources().getInteger(R.integer.max_verification_code_length) != verificationCode.length()) {
//            ToastUtil.show(context.getString(R.string.code_toast_hint));
//            return false;
//        }
//
//        return true;
//    }
//
//    public static boolean isValidPwd(String pwd, @StringRes int resId) {
//
//        if (TextUtils.isEmpty(pwd)) {
//            ToastUtil.show(R.string.password_is_empty);
//            return false;
//        }
//
////        if (pwd.length() < context.getResources().getInteger(R.integer.min_pwd_length) || pwd.length() > context.getResources().getInteger(R.integer.max_pwd_length)) {
////            ToastUtil.show(context.getString(R.string.password_toast_hint));
////            return false;
////        }
//
//        if (!StringUtil.isPwdValid(pwd)) {
//            ToastUtil.show(resId);
//            return false;
//        }
//
//        return true;
//    }
//
//    public static boolean isValidPhoneNum(Context context, String phoneNum) {
//
//        if (TextUtils.isEmpty(phoneNum)) {
//            ToastUtil.show(context.getString(R.string.mobile_number_is_empty));
//            return false;
//        }
//
//        if (phoneNum.length() < context.getResources().getInteger(R.integer.min_phone_number_length) || phoneNum.length() > context.getResources().getInteger(R.integer.max_phone_number_length)) {
//            ToastUtil.show(context.getString(R.string.illegal_mobile_number));
//            return false;
//        }
//
//        return true;
//    }
//
//
//    public static boolean checkSmsLoginArgs(Context context, String phoneNum, String verificationCode) {
//        return isValidPhoneNum(context, phoneNum) && isValidCode(context, verificationCode);
//    }
//
//    public static boolean checkPwdLoginArgs(Context context, String phoneNum, String pwd) {
//        return isValidPhoneNum(context, phoneNum) && isValidPwd(pwd, R.string.password_rule_hint);
//    }
//
//    /**
//     * 是否组织用户
//     *
//     * @param user_type 用户类型 0普通用户 非0 企业组织用户
//     * @return
//     */
//    public static boolean isOrgOwner(int user_type) {
//        return user_type != 0;
//    }
//
//    public static String getCompatibleThumb(ImageBean imageBean) {
//        return imageBean == null ? null : imageBean.getCompatibleThumb();
//    }
//
//    public static String getImageUrlOrThumb(ImageBean imageBean) {
//        if (imageBean == null) return null;
//
//        final String url = imageBean.getImageUrl();
//        return TextUtils.isEmpty(url) ? imageBean.getImgThumb() : url;
//    }
//
//    public static boolean isInvalidContext(@Nullable Activity activity) {
//        if (activity == null) return true;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return activity.isDestroyed() || activity.isFinishing();
//        }
//
//        return activity.isFinishing();
//    }
//
//    /**
//     * 获取全局性Gson对象 避免日期在序列化/反序列化过程中出现解析异常
//     *
//     * @return
//     */
    public static Gson getGsonInstance() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }


    public static Gson ConverterJsonToObject() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }





}
