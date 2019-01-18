package com.githubv3api.meesn.githubv3api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InternetCheck {
    private Context context;
    public InternetCheck(Context context)
    {
        this.context = context;
    }
    public boolean netCheck()
    {
        try
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");
            //return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (UnknownHostException | NetworkOnMainThreadException | NullPointerException ex)
        {
            return false;
        }
    }
}
