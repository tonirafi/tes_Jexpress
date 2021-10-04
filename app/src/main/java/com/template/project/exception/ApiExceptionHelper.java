package com.template.project.exception;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.template.project.http.bean.BaseResponse2;
import com.template.project.http.bean.JsonBase;
import com.template.project.utils.LogUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import retrofit2.Response;

public class ApiExceptionHelper {

    public static String getError(HttpException exception) {
        final Response response = exception.response();
        if (response == null) return String.valueOf(exception.code());

        final String msg = response.message();
        int errorCode = response.code();
        if (errorCode != 404 && errorCode >= 400 && errorCode < 600) {
            if (response.errorBody() != null) {
                String errorInfo = null;
                try {
                    errorInfo = response.errorBody().string();
                    if (!TextUtils.isEmpty(errorInfo)) {
                        Gson gson = new Gson();
                        JsonBase jsonBase = gson.fromJson(errorInfo, JsonBase.class);
                        if (jsonBase != null) {
                            return jsonBase.msg;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(errorInfo)) {
                    return errorInfo;
                }

            }

            return TextUtils.isEmpty(msg) ? String.valueOf(errorCode) : msg;
        }

        return TextUtils.isEmpty(msg) ? String.valueOf(errorCode) : String.format("%s:%s", errorCode, msg);
    }

    public static String getDisplayError(Throwable e) {
        return String.format("Oops,%s", handleException(e).msg);
    }

    public static int getNetErrorCode(Throwable throwable) {
        LogUtil.logE("getNetErrorCode: " + throwable.toString());
        return handleException(throwable).getCode();
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof ApiException) {
            ex = (ApiException) e;
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (httpException.code() == ErrorCode.AUTH_ERROR) {
                ex = new ApiException(e, ErrorCode.AUTH_ERROR);
                String errorInfo = getError(httpException);
                ex.msg = String.format("Auth error:%s", errorInfo == null ? "401" : errorInfo);
            } else if (httpException.code() == 504) {
                ex = new ApiException(e, ErrorCode.NETWORK_ERROR);
                String errorInfo = getError(httpException);
                ex.msg = String.format("Network error:%s", errorInfo == null ? "504" : errorInfo);
            } else {
                ex = new ApiException(e, ErrorCode.HTTP_ERROR);
                String errorInfo = getError(httpException);
                ex.msg = errorInfo == null ? "Http request error" : errorInfo;
            }

        } else if (e instanceof ServerException) {
            ServerException serverException = (ServerException) e;
            final String errorInfo = serverException.getMessage();
            if (serverException.getCode() == ErrorCode.AUTH_ERROR) {
                ex = new ApiException(e, ErrorCode.AUTH_ERROR);
                ex.msg = String.format("Auth error:%s", errorInfo);
            } else {
                ex = new ApiException(e, ErrorCode.SERVER_ERROR);
//                ex.msg = "Server error";
                ex.msg = TextUtils.isEmpty(errorInfo) ? "Server error" : errorInfo;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ErrorCode.PARSE_ERROR);
            ex.msg = "Parse error";            //均视为解析错误
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ErrorCode.SERVER_TIME_OUT_ERROR);
            ex.msg = "Server response time out error";  ////服务器响应超时
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ErrorCode.NETWORK_ERROR);
            ex.msg = "Network connection error";  //均视为网络连接错误
        } else {
            ex = new ApiException(e, ErrorCode.UNKNOWN);
            ex.msg = TextUtils.isEmpty(e.getLocalizedMessage()) ? "Unknown error" : e.getLocalizedMessage();
        }

        return ex;
    }

//    protected static void throwServerException(BaseResponse... its) throws ServerException {
//        if (its == null || its.length == 0) return;
//        for (BaseResponse it : its) {
//            if (!it.isSuccess()) {
//                throw new ServerException(it.getMsg(), it.getCode());
//            }
//        }
//
//    }

//    protected static void throwAllServerException(BaseResponse... its) throws ServerException {
//        if (its == null || its.length == 0) return;
//        throwServerException(its);
//        throwServerExceptionIfEmpty(its);
//    }

    public static void throwServerExceptionIfEmpty(BaseResponse2... its) throws ServerException {
        if (its == null || its.length == 0) return;
        for (BaseResponse2 it : its) {
            if (!it.hasData()) {
                throw new ServerException("Data is empty");
            }
        }

    }
}
