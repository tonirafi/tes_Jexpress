package com.tes.frezzmart.utils;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtil {


    private StringUtil() {
    }


    public static boolean isMobileNO(String mobiles) {
        //手机
        boolean IS_MOBEILPHONE = false;
//        String phoneExp= "(^(0\\d{2,3})?-?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^((\\(\\d{3}\\))|(\\d{0}))?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)";
        String phoneExp = "(^((0\\d{2,3})-)?([2-9]\\d{6,7})(-\\d{1,5})?$)|(^(086)?(13|14|15|17|18)\\d{9}$)|(^(400|800)\\d{7}(-\\d{1,6})?$)|(^(95013)\\d{6,8}$)";
        try {
            Pattern p = Pattern.compile(phoneExp);
            Matcher m = p.matcher(mobiles);
            IS_MOBEILPHONE = m.matches();
            if (IS_MOBEILPHONE) {
                return true;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    //生成随机数字和字母,
    public static CharSequence getRandomStr(int length) {

        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母，ASCII中 65～90号为26个大写英文字母，97～122号为26个小写英文字母，其余为一些标点符号、运算符号a
//                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  //
                val.append((char) (random.nextInt(26) + 97));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val;
    }

    public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }

    /**
     * Returns a string whose value is this string, with any leading and trailing
     * LF removed.
     *
     * @param str
     * @return
     */
    public static String trimLF(String str) {
        if (TextUtils.isEmpty(str))
            return str;

        str = str.trim();

        while (str.startsWith("\n")) {
            str = str.substring(1).trim();
        }

        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1).trim();
        }

        return str;
    }

    public static String stripLF(String str) {

        if (!TextUtils.isEmpty(str)) {
//            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//            Matcher m = p.matcher(str);
            // // 去除字符串中的空格、回车、换行符、制表符
//            Matcher m = Pattern.compile("\\s*|\t|\r|\n").matcher(str);
            Matcher m = Pattern.compile("\t|\r|\n").matcher(str);
            return m.replaceAll("");
        }

        return str;
    }

    private static String replaceEach(
            final String text, final String[] searchList, final String[] replacementList, final boolean repeat, final int timeToLive) {

        // mchyzer Performance note: This creates very few new objects (one major goal)
        // let me know if there are performance requests, we can create a harness to measure

        if (text == null || text.isEmpty() || searchList == null ||
                searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }

        // if recursing, this shouldn't be less than 0
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - " +
                    "output of one loop is the input of another");
        }

        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;

        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength);
        }

        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;

        // index of replace array that will replace the search string found
        // NOTE: logic duplicated below START
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                    searchList[i].isEmpty() || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else {
                if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        // NOTE: logic mostly below END

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;

        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;

        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            final int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);

        final StringBuilder buf = new StringBuilder(text.length() + increase);

        while (textIndex != -1) {

            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);

            start = textIndex + searchList[replaceIndex].length();

            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            // find the next earliest match
            // NOTE: logic mostly duplicated above START
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null ||
                        searchList[i].isEmpty() || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else {
                    if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
            // NOTE: logic duplicated above END

        }
        final int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        final String result = buf.toString();
        if (!repeat) {
            return result;
        }

        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }

    public static boolean isDouble(String value) {
        return value != null && value.matches("^-?\\d+\\.?\\d*$");
    }

    public static boolean isInteger(String value) {
        return value != null && value.matches("^-?\\d+$");
    }

    public static boolean isEmail(String value) {
        return value != null && value.matches("^[a-z0-9A-Z]+([\\-_\\.][a-z0-9A-Z]+)*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)*?\\.)+[a-zA-Z]{2,4}$");
    }

    /**
     * @param pwd
     * @return
     */
    public static boolean isPwdValid(String pwd) {
        return pwd != null && pwd.matches("^[A-Za-z0-9\\x21-\\x7e]{6,16}$");
    }


    public static String removeHeadZero(String text) {

        if (!TextUtils.isEmpty(text) && text.startsWith("0"))
            return text.substring(1);

        return text;
    }

    public static String formatCurrency(String text) {
        return formatCurrency(null, text);
    }

    public static String formatCurrency(boolean showCurrencyPrefix, String text) {
        return formatCurrency(showCurrencyPrefix ? "Rp " : null, text);
    }

    public static String formatCurrency(String currencyPrefix, String text) {

        if (text == null) return "0";

        double num;
        try {
            num = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return "0";
        }

        NumberFormat format = NumberFormat.getNumberInstance(new Locale("in", "ID"));
        format.setMaximumFractionDigits(2);
        if (text.contains(".")) { //如果格式化的数字字符含小数点 则保留2位小数
            format.setMinimumFractionDigits(2);
        }

        return TextUtils.isEmpty(currencyPrefix) ? format.format(num) : currencyPrefix + format.format(num);
    }

    public static synchronized String formatDate(Context context, Date date) {
        if (date == null)
            return null;

        int delta = (int) ((System.currentTimeMillis() - date.getTime()) / 1000); //secs
//        if (delta < 60)
//            return context.getResources().getQuantityString(R.plurals.x_minutes_ago, delta, delta);

        if (delta > 0 && delta < 24 * 60 * 60) { //小于24小时
            Calendar calendar = Calendar.getInstance();
            int newDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (newDay == day) {
                return formatDate(context, "HH:mm", date);
            }
        }

        return formatDate(context, "HH:mm dd MMM yyyy", date);
    }

    public static String formatDate(Context context, String dateFormat, Date date) {
        return new SimpleDateFormat(dateFormat, LocaleUtil.getSystemLocale(context)).format(date);
    }

    public static String formatRemainWaitTime(Context context, long secs) {

        DecimalFormat df = new DecimalFormat("00");

        int scale = 60;
        if (secs < scale) {
            return String.format("00 : 00 : %s", df.format(secs));
        }

        if (secs < scale * 60) {
            return String.format("00 : %s : %s", df.format(secs / scale), df.format(secs % scale));
        }
        scale *= 60;
        if (secs <= scale * 24) {
            long leftSec = secs % scale;
            return String.format("%s : %s : %s", df.format(secs / scale), df.format(leftSec / 60), df.format(leftSec % 60));
        }

        int days = (int) (secs / (scale * 24));
        long leftSecs = secs % (scale * 24);

//        return String.format("%s %s", context.getResources().getQuantityString(R.plurals.x_days, days, days), formatRemainWaitTime(context, leftSecs));

        return null;
    }

    public static Spanned trans2Spanned(String source) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(source);
    }

    public static SpannableString generateCounterSpan(String count, int max) {
        SpannableString temp = new SpannableString(String.format("%s/%s", count, max));
        temp.setSpan(new ForegroundColorSpan(0xff333333), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return temp;
    }

    public static boolean isAuthenticatedImgUrl(String value) {
        return !TextUtils.isEmpty(value) && Pattern.compile("/image/authenticated/s--[\\w\\-]{8}--/").matcher(value).find();
    }

    public static String stripFetchUrlPrefix(String value) {
        if (TextUtils.isEmpty(value) || !value.startsWith("https://res.cloudinary.com/dqgl4hkkx/image/fetch/c_fill,g_face,w_72,h_72/http"))
            return value;
        return value.replace("https://res.cloudinary.com/dqgl4hkkx/image/fetch/c_fill,g_face,w_72,h_72/", "");
    }

    public static String formatUrlWthWatermark(String sourceUrl) {
        if (!TextUtils.isEmpty(sourceUrl) && !sourceUrl.startsWith(watermark_fetch_domain)) {
            return watermark_fetch_domain + sourceUrl;
        }

        return sourceUrl;
    }

    public static final String watermark_fetch_domain = "https://res.cloudinary.com/dqgl4hkkx/image/fetch/a_ignore,f_auto,q_auto/c_scale,f_auto,fl_relative,g_center,l_overlay_njgppi,w_0.7/";

    public static boolean isBase64Image(String value) {
        return !TextUtils.isEmpty(value) && value.matches("^(data:image/(png|jpeg|jpg|webp|gif|bmp);base64,).+");
    }

    public static String getNonNullVal(String val) {
        return val == null ? "" : val;
    }

    public static String generateFileNameForBase64Image(String base64String, String imageStoragePath) {
        if (!isBase64Image(base64String)) return null;

        String[] sp = base64String.split(",");

        String imageFormat = "jpg";
        if (sp[0].contains("/") && sp[0].contains(";")) {
            imageFormat = sp[0].substring(sp[0].indexOf("/") + 1, sp[0].indexOf(";"));
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return imageStoragePath + "GudangView_Base64_" + timeStamp + "." + imageFormat;
    }

    public static boolean base64ToImage(String base64String, String fileName) {
        if (!isBase64Image(base64String) || TextUtils.isEmpty(fileName)) return false;


        File file = new File(fileName);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            boolean mk = file.getParentFile().mkdirs();
            if (!mk) return false;
        }

        OutputStream out = null;
        try {
            // Base64解码
            byte[] bytes = Base64.decode(base64String.split(",")[1], Base64.DEFAULT);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }

            out = new FileOutputStream(fileName);
            out.write(bytes);
            out.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static String getNAIfEmpty(String text) {
        return TextUtils.isEmpty(text) ? "N/A" : text;
    }

    /**
     * This is a text
     *
     * @param text
     * @return 将字符转为首字母大写 其余均为小写
     */
    public static String getCapitalizeString(String text) {
        if (TextUtils.isEmpty(text)) return text;
        String lowText = text.toLowerCase();
        return lowText.substring(0, 1).toUpperCase() + lowText.substring(1);
    }

    public static String getDisplayNoticeCount(int noticeCount) {
        return noticeCount > 99 ? "···" : String.valueOf(noticeCount);
    }

    public static JsonObject getJsonObject(String json) {
        if (TextUtils.isEmpty(json)) return null;
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        return jsonObject.isJsonObject() ? jsonObject : null;
    }

    @Nullable
    public static String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 7)
            return null;

        int len = mobile.length();
        if (len > 7)
            return String.format("%s **** %s", mobile.substring(0, 4), mobile.substring(len - 4, len));

        return String.format("%s **** %s", mobile.substring(0, 3), mobile.substring(len - 4, len));
    }

    public static String trimStringByMaxLength(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }

        return String.format("%s...", str.substring(0, maxLength));
    }
}
