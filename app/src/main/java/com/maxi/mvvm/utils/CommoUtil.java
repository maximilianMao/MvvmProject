package com.maxi.mvvm.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * <pre>
 *     author : li
 *     e-mail : xxx@xx
 *     time   : 2020/03/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CommoUtil {

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String noNull(String s) {
        if (null == s || s.isEmpty()) {
            s = "  ";
        }
        return s;
    }

    public static String noEditNull(EditText et) {
        String s;
        if (null == et.getText() || et.getText().toString().isEmpty()) {
            s = "  ";
        } else {
            s = et.getText().toString();
        }
        return s;
    }

    /**
     * 是否包含特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断字符串是否仅含有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex) && !isLetternum(str) && !isLetterzz(str);
    }

    /**
     * 判断字符串是否仅含有数字
     *
     * @param str
     * @return
     */
    public static boolean isLetternum(String str) {
        String regex = "[0-9]+";
        return str.matches(regex);
    }

    /**
     * 判断字符串是否仅含有字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterzz(String str) {
        String regex = "[a-zA-Z]+";
        return str.matches(regex);
    }

    /**
     * 判断字符串是否为中文
     *
     * @param name
     * @return
     */
    public static boolean checkName(String name) {
        int n = 0;
        for (int i = 0; i < name.length(); i++) {
            n = (int) name.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return name.length() >= 2 && name.length() <= 6;
    }

    public static boolean checkChinese(String name) {
        int n = 0;
        for (int i = 0; i < name.length(); i++) {
            n = (int) name.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否是手机号
     *
     * @param mobiles
     * @return
     */
    /*public static boolean isMobile(String mobiles) {
        String regEx = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(mobiles);

        return m.find();
    }*/

    public static boolean isMobile(String mobiles) {
        String regExp = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[1|8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //统一社会信用代码
    public static boolean isSocialCode(String code) {
        //String regExp = "^([1-9A-GY]{1}[1239]{1}[1-5]{1}[0-9]{5}[0-9A-Z]{10})$";
        String regExp = "^([0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10})$";
        //String regExp = "^([0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}|[1-9]\\d{14})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(code);
        return m.matches();
    }

    //密码格式校验
    public static boolean isPassword(String pswd) {
        String regExp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(pswd);
        return m.matches();
    }

    //邮箱格式校验
    public static boolean isEmail(String email) {
        String regExp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isDecimal2(String string) {
        String regExp = "^[0-9]+(.[0-9]{1,2})?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 打开高德导航
     */
    public static void openNavi(Context context, String lat, String lon) {
        if (isAvilible(context, "com.autonavi.minimap")) {
            String uriString = "androidamap://navi?sourceApplication=中交天运司机版&poiname=&lat=" + lat + "&lon=" + lon + "&dev=1&style=2";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.setData(Uri.parse(uriString));
            context.startActivity(intent);
        } else {
            Toast toast = Toast.makeText(context, "请安装高德地图后重试", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static String twoDouble(double ss) {
        return String.format("%.2f", ss);
    }

    /**
     * GPS是否开启
     *
     * @param context
     * @return
     */
    public static Boolean getGpsState(Context context) {
        //GPS是否开启
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 进入GPS设置页面
     *
     * @param context
     */
    public static void openGps(Context context) {
        //进入GPS设置页面
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    public static StringBuffer lpNumber(String lpNumber) {

        if (lpNumber != null && !lpNumber.isEmpty() && lpNumber.length() > 2) {
            StringBuffer sb = new StringBuffer(lpNumber);
            sb.insert(2, ' ');
            return sb;
        } else {
            return null;
        }
    }


    /**
     * 车牌号格式是否正确
     *
     * @param content
     * @return
     */
    public static boolean checkCarNumber(String content) {
        String pattern = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z](([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([A-Z0-9]{5,6}))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$";
        return Pattern.matches(pattern, content);
    }

    /**
     * 车架号
     *
     * @param content
     * @return
     */
    public static boolean checkCarId(String content) {
        String pattern = "^[A-Za-z0-9]{17}$";
        return Pattern.matches(pattern, content);
    }

    //是否为空
    public static boolean isNull(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 营业执照 统一社会信用代码（18位）
     *
     * @param license
     * @return
     */
    public static boolean isLicense18(String license) {
        char[] c = license.toCharArray();
        int daxie = 0;
        int num = 0;
        for (int i = 0; i < license.length(); i++) {
            if (c[i] >= 'A' && c[i] <= 'Z') {
                daxie++;
            } else if (c[i] >= '0' && c[i] <= '9') {
                num++;
            }
        }
        if (daxie + num == 18) {
            return true;
        }

        return false;
    }

    /**
     * 把字符串数字类型的所有数字取出来
     *
     * @param str <li>"1-000我10123我60#0"       》 100010123600</li>
     *            <li>"00010-+123我600"         》 00010123600</li>
     *            <li>"我是2019我600"            》 2019600</li>
     *            <li>"我是20 -19我    600"         》 2019600</li>
     * @return
     */
    public static String getNumberText(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer number = new StringBuffer("");

        String[] strArray = str.split("");
        for (String string : strArray) {
            if (!StringUtils.isEmpty(string) && isLetternum(string)) {
                number.append(string);
            }
        }
        return number.toString();
    }

    /**
     * 获取当前app version code
     */
    public static long getAppVersionCode(Context context) {
        long appVersionCode = -1;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

    /**
     * 银行卡拼接
     *
     * @param content
     * @return
     */
    public static String addSpeaceByCredit(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
//去空格
        content = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
//当为第4位时，并且不是最后一个第4位时
//拼接字符的同时，拼接一个空格
//如果在最后一个第四位也拼接，会产生空格无法删除的问题
//因为一删除，马上触发输入框改变监听，又重新生成了空格
            if (i % 4 == 0 && i != content.length()) {
                newString.append(content.charAt(i - 1) + " ");
            } else {
//如果不是4位的倍数，则直接拼接字符即可
                newString.append(content.charAt(i - 1));

            }
        }
        return newString.toString();
    }

    /**
     * 替换字符串某个位置的字段
     *
     * @param start         替换开始位置
     * @param end           替换结束位置
     * @param replaceString 替换字符串
     * @param oldString     原始字符串
     * @return
     */
    public static String replace(int start, int end, String replaceString, String oldString) {
        StringBuilder stringBuilder = new StringBuilder(oldString);
        stringBuilder.replace(start, end, replaceString);
        return stringBuilder.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*弹出软键盘*/
    public static void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 获取assets文件
     * @param context
     * @return
     */
    public static byte[] getAssetsStyle(Context context,String fileName){
        byte[]  buffer1 = null;
        InputStream is1 = null;
        try {
            is1 = context.getResources().getAssets().open(fileName);
            int lenght1 = is1.available();
            buffer1 = new byte[lenght1];
            is1.read(buffer1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is1!=null) {
                    is1.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer1;
    }

    //地址是否合规
    public static boolean isURL(String str){
        //转换为小写
        if (str != null && !str.isEmpty()) {
            str = str.toLowerCase();
            String regex = "(^[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+$)";
            return str.matches(regex);
        }else {
            return false;
        }
    }
}
