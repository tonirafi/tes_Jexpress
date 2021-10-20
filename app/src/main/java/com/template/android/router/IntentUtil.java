package com.template.android.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.template.android.AppConstants;
import com.template.android.MyApplication;
import com.template.android.http.bean.ArticlesItem;
import com.template.android.utils.AppUtil;
import com.template.android.utils.LogUtil;
import com.template.android.utils.SharedPreferencesTool;
import com.walkermanx.permission.PermissionActivity;

import cn.campusapp.router.Router;
import cn.campusapp.router.route.ActivityRoute;


public class IntentUtil {


    public static void jumpToMarket(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            intent.setData(Uri.parse(AppConstants.GOOGLE_PLAY_MARKET_URI_PREFIX + url.replaceAll("market://", "")));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
//                Toast.makeText(context, R.string.can_not_find_app_store, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static boolean hasMatchedActivity(Context context, Intent intent) {
        if (context == null) {
            context = MyApplication.getContext();
        }
        try {
            return intent.resolveActivity(context.getPackageManager()) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void intentToUri(Context context, String url, int requestCode) {
        intentToUri(context, url, -1, requestCode);
    }

    public static void intentToUri(Context context, String url, int flag, int requestCode) {
        Uri uri = toUri(context, url);
        if (uri == null)
            return;

        intentToUri(context, uri, flag, requestCode);
    }

    public static Uri toUri(Context context, String url) {


        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Uri uri = Uri.EMPTY;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri == Uri.EMPTY) {
            return null;
        }

        return uri;
    }

    // 做安全处理
    public static void intentToUri(Context context, String url) {
        Uri uri = toUri(context, url);
        if (uri == null)
            return;
        intentToUri(context, uri, Intent.ACTION_VIEW);
    }

    public static void intentToUri(Context context, Uri uri) {
        intentToUri(context, uri, Intent.ACTION_VIEW);
    }

    public static void intentToUri(Context context, Uri uri, String action) {
        intentToUri(context, uri, action, -1);
    }

    public static void intentToUri(Context context, Uri uri, String action, int flag) {
        intentToUri(context, uri, action, flag, -1, null);
    }

    public static void intentToUri(Context context, Uri uri, int requestCode) {
        intentToUri(context, uri, Intent.ACTION_VIEW, -1, requestCode, null);
    }

    public static void intentToUri(Context context, Uri uri, int flag, int requestCode) {
        intentToUri(context, uri, Intent.ACTION_VIEW, flag, requestCode, null);
    }

    public static void intentToUri(Context context, Uri uri, int requestCode, Bundle bundle) {
        intentToUri(context, uri, Intent.ACTION_VIEW, -1, requestCode, bundle);
    }

    public static void intentToUri(Context context, Uri uri, int flag, int requestCode, Bundle bundle) {
        intentToUri(context, uri, Intent.ACTION_VIEW, flag, requestCode, bundle);
    }

    public static void intentToUri(Context context, Uri uri, Bundle bundle) {
        intentToUri(context, uri, Intent.ACTION_VIEW, -1, -1, bundle);
    }


    /**
     * @param context
     * @param uri
     * @param requestCode
     * @param fragment
     */
    public static void intentToUri(Context context, Uri uri, int requestCode, Fragment fragment) {
        intentToUri(context, uri, Intent.ACTION_VIEW, -1, requestCode, null, fragment);
    }

    public static void intentToUri(Context context, Uri uri, Bundle bundle, int requestCode, Fragment fragment) {
        intentToUri(context, uri, Intent.ACTION_VIEW, -1, requestCode, bundle, fragment);
    }

    public static void intentToUri(Context context, Uri uri, String action, int flag, int requestCode, Bundle bundle) {
        intentToUri(context, uri, action, flag, requestCode, bundle, null);
    }

    public static void intentToUri(Context context, Uri uri, String action, int flag, int requestCode, Bundle bundle, Fragment fragment) {
        if (uri == null || context == null) {
            return;
        }

        if (toGallery(uri)) {
            if (context instanceof Activity) {
                int RSC_CODE = 300;
                try {
                    if (uri.getLastPathSegment() != null) {
                        RSC_CODE = Integer.parseInt(uri.getLastPathSegment());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                galleryGo((Activity) context, RSC_CODE);
            } else {
                throw new RuntimeException("toGallery Router has something wrong : illegal context or uri");
            }
            return;
        }

        Boolean user_login = SharedPreferencesTool.getInstance(context).getBoolean(AppConstants.USER_LOGIN, false);

        if ((!user_login && RouterMap.Companion.needLogin(uri) || isNeedJumpLogin(uri))) {
            intentToLogin(context);
            return;
        }

        ActivityRoute route = (ActivityRoute) Router.getRoute(uri.toString());
        if (route != null && routerGo(route, context, uri, flag, requestCode, bundle, fragment)) {
            return;
        }
        Intent intent = new Intent();
        if (isDial(uri)) {
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(uri);
            context.startActivity(intent);
            return;
        }
        LogUtil.logD("0");

        intent.setData(uri);
        intent.setAction(action);
        if (flag != -1) {
            intent.setFlags(flag);
        }

        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (hasMatchedActivity(context, intent)) {
            if (requestCode != -1 && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);

            } else {

                context.startActivity(intent);
            }

        } else {

//            intentToPageInfo(context,"0");
        }

    }


    private static boolean routerGo(ActivityRoute route, Context context, Uri uri, int flag, int requestCode, Bundle bundle, Fragment fragment) {
        //router start
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

//        if (route != null) {
        if (bundle != null) {
            route.addExtras(bundle);
        }
        //for result
        if (requestCode > 0) {
            if (fragment != null) {
                route.withOpenMethodStartForResult(fragment, requestCode);
            } else if (activity != null) {
                route.withOpenMethodStartForResult(activity, requestCode);
            }
        }
        if (flag != -1) {
            route.withFlags(flag);
        }
        if (activity != null) {
//            route.setAnimation(activity, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        }

        return route.open();

        //router end
    }

    private static boolean isDial(Uri uri) {
        return "tel".equals(uri.getScheme());
    }

    private static boolean toGallery(Uri uri) {
        return "photo".equals(uri.getScheme());
    }

    private static void galleryGo(final Activity context, final int RS_CODE) {
        if (context instanceof PermissionActivity) {
//            ((PermissionActivity) context).requestPermissions(RS_CODE, Permission.CAMERA, Permission.STORAGE);
        }
    }

    private static boolean isNeedJumpLogin(Uri uri) {
        return uri.getPath() != null && uri.getPath().contains(RouterConstants.Path.H5_JUMP) && Boolean.parseBoolean(uri.getQueryParameter(RouterConstants.Params.H5_AUTH));
    }

    public static void intentToH5(Context context, String url) {
        intentToH5(context, url, -1);
    }

    public static void intentToH5(Context context, String url, int flag) {
        intentToH5(context, url, null, flag);
    }

    public static void intentToH5(Context context, String url, String title) {
        intentToH5(context, url, title, -1);
    }

    public static void intentToH5(Context context, String url, String title, int flag) {
        intentToH5(context, url, title, false, flag);
    }

    public static void intentToH5(Context context, String url, String title, boolean isFixedTitle) {
        intentToH5(context, url, title, isFixedTitle, -1);
    }

    public static void intentToH5(Context context, String url, String title, boolean isFixedTitle, int flag) {
        intentToH5(context, url, title, isFixedTitle, RouterConstants.Common.DEFAULT_H5_HIDE_TITLE_BAR_VALUE, flag);
    }

    public static void intentToH5(Context context, String url, String title, boolean isFixedTitle, boolean hideTitle) {
        intentToH5(context, url, title, isFixedTitle, hideTitle, -1);
    }

    public static void intentToH5(Context context, String url, String title, boolean isFixedTitle, boolean hideTitle, int flag) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri.Builder builder = RouterConstants.URI.INSTANCE.getJUMP().buildUpon();
        builder.appendQueryParameter(RouterConstants.Params.H5_URL, url);
        if (!TextUtils.isEmpty(title)) {
            builder.appendQueryParameter(RouterConstants.Params.TITLE, title);
        }

        builder.appendQueryParameter(RouterConstants.Params.IS_FIXED_TITLE, String.valueOf(isFixedTitle));
        builder.appendQueryParameter(RouterConstants.Params.HIDE_TITLE, String.valueOf(hideTitle));

        intentToUri(context, builder.build(), "", flag);
    }

    public static void intentToLogin(Context context) {
        intentToLogin(context, AppConstants.RequestCode.RSC_LOGIN);
    }

    //
    public static void intentToLogin(Context context, int requestCode) {
        intentToUri(context, RouterConstants.URI.INSTANCE.getLOGIN(), requestCode);
    }

    public static <T> T getQueryParameter(Intent intent, String key, Class<T> c) {
        if (intent == null || intent.getData() == null) {
            return null;
        }
        String result = intent.getData().getQueryParameter(key);
        if (result == null) {
            return null;
        }
        return (T) result;
    }

    public static void intentToWebView(Context context, @NonNull Object articel) {

        Uri.Builder builder = RouterConstants.URI.INSTANCE.getNEWS_DETAIL().buildUpon();
        if (articel instanceof ArticlesItem) {
            builder.appendQueryParameter(RouterConstants.Params.PRE_LOAD, AppUtil.getGsonInstance().toJson(articel));
        }

        intentToUri(context, builder.build(), AppConstants.RequestCode.DEFAULT_RSC_CODE);
    }

}