package com.template.project.http.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.template.project.AppConstants;
import com.template.project.BuildConfig;
import com.template.project.MyApplication;
import com.template.project.http.interceptors.InjectHeaderInterceptor;
import com.template.project.http.interceptors.OfflineCacheInterceptor;
import com.template.project.http.interceptors.RetryInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiEngine {

    private Retrofit retrofit;
    private ApiService apiService;

    public ApiEngine() {
//        initRetrofit();
    }

    private static class Holder {
        private static final ApiEngine INSTANCE = new ApiEngine();
    }

    public static ApiEngine getInstance() {
        return Holder.INSTANCE;
    }

    private void initRetrofit() {
        if (retrofit != null) return;
        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(HomeImageBean.FavoriteInfoBean.class, new HomeFavoriteAdapter())
//                .registerTypeAdapter(HomeImageBean.UserInfoBean.class, new HomeUserInfoAdapter())
//                .registerTypeAdapter(MyLikeBean.ArticleInfoBean.class, new MyLikeAdapter())
//                .registerTypeAdapter(BookmarkQuranBean.QuranInfoBean.class, new BookmarkQuranAdapter())
//                .registerTypeAdapter(BookmarkQuranBean.SurahInfoBean.class, new BookmarkSurahAdapter())
//                .registerTypeAdapter(BookmarkQuranBean.JuzInfoBean.class, new BookmarkJuzAdapter())
//                .registerTypeAdapter(DoaAllBean.AminInfoBean.class, new DoaAminAdapter())
//                .registerTypeAdapter(DoaAllBean.MyAminBean.class, new DoaMyAminAdapter())
//                .registerTypeAdapter(DoaAllBean.UserInfoBean.class, new DoaUserInfoAdapter())
//                .registerTypeAdapter(AllTaskFinishBean.ActivityDetailBean.class, new TaskUserInfoAdapter())
//                .registerTypeAdapter(TaskTemplateBean.UserInfo.class, new TaskUserDetailAdapter())
//                .registerTypeAdapter(HomeCardBean.CardsBean.class, new HomeCardAdapter())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(generateOkHttpClient())
                .build();
    }

    public OkHttpClient generateOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //忽略证书验证
        builder.hostnameVerifier((hostname, session) -> true);
        //设置请求缓存路径和缓存池大小为50M
        builder.cache(new Cache(MyApplication.getContext().getAppCacheDir("url-cache"), 50 * 1024 * 1024));
        builder.connectTimeout(AppConstants.Http.HTTP_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(AppConstants.Http.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(AppConstants.Http.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);

        //默认重试一次，若需要重试N次，则要实现拦截器。
        builder.retryOnConnectionFailure(true);

        // 在线缓存,单位:60秒
//        builder.addNetworkInterceptor(new OnlineCacheInterceptor(AppConstants.Http.DEFAULT_REQ_CACHE_TIME));

        //请求重试
        builder.addInterceptor(new RetryInterceptor(2));
        //7 * 24 * 3600; // 离线时缓存保存7天,单位:秒
        builder.addInterceptor(new OfflineCacheInterceptor());
        //请求 header信息注入
        builder.addInterceptor(new InjectHeaderInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
//            try {
//                builder.addNetworkInterceptor((Interceptor) Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor").newInstance());
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            }
        }
        return builder.build();
    }


    public ApiService getApiService() {
        if (apiService == null) {
            initRetrofit();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public Retrofit getRetrofit() {
        initRetrofit();
        return retrofit;
    }

    public OkHttpClient getDefaultOkHttpClient() {
        return (OkHttpClient) getRetrofit().callFactory();
    }

}
