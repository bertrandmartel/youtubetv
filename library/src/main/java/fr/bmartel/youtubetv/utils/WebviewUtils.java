package fr.bmartel.youtubetv.utils;

import android.view.Display;
import android.webkit.WebView;

/**
 * Created by akinaru on 14/10/16.
 */
public class WebviewUtils {

    public static int getWebviewScale(final Display display) {
        int width = display.getWidth();
        Double val = new Double(width) / new Double(1920);
        val = val * 100d;
        return val.intValue();
    }

    public static void callJavaScript(final WebView webView, String methodName, Object... params) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:try{");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        String separator = "";
        for (Object param : params) {
            stringBuilder.append(separator);
            separator = ",";
            if (param instanceof String) {
                stringBuilder.append("'");
            }
            stringBuilder.append(param);
            if (param instanceof String) {
                stringBuilder.append("'");
            }

        }
        stringBuilder.append(")}catch(error){console.error(error.message);}");
        final String call = stringBuilder.toString();

        webView.loadUrl(call);
    }

}
