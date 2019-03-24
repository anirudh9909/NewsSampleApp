package com.example.anirudhbharti.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    static Toast mToast;
    public static String baseURL = "https://newsapi.org/";
    public static String Api_KEY="ae45eb444b584b91812590bf5edb1ef5";

    public static void showToast(Context context, String statusMsg){
        if(mToast != null) mToast.cancel();
        mToast = Toast.makeText(context,statusMsg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static boolean checkIfInternetAvialable(Context ctx) {
        if (ctx != null) {
            boolean isConnected;
            ConnectivityManager cm =
                    (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            isConnected = ni != null && ni.isConnected();
            return isConnected;
        } else {
            return false;
        }

    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}