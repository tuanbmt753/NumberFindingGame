package com.example.numberfindinggame.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class NetworkHelper {

    /**
     * Kiểm tra thiết bị có kết nối mạng hay không
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(network);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }

    /**
     * Kiểm tra đang dùng WiFi
     */
    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(network);

        return capabilities != null
                && capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
        );
    }

    /**
     * Kiểm tra đang dùng dữ liệu di động
     */
    public static boolean isMobileDataConnected(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();

        if (network == null) {
            return false;
        }

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(network);

        return capabilities != null
                && capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
        );
    }
}