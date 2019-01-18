package com.githubv3api.meesn.githubv3api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.NetworkOnMainThreadException;

import java.io.IOException;
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
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                mIpAddrProcess.destroy();
                return true;
            }else{
                mIpAddrProcess.destroy();
                return false;
            }
        }
        catch (InterruptedException | IOException | NetworkOnMainThreadException | NullPointerException ex)
        {
            return false;
        }
    }
    private boolean networkCheck()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
