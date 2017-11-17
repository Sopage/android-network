package android.network.utils;

/**
 * Created by sanders on 15/6/27.
 */
public class NetworkUtils {
//
//    public static boolean isNetworkContented(Context context) {
//        return getNetworkType(context) == NetworkType.NET_UNKNOWN ? false : true;
//    }
//
//    public static NetworkType getNetworkType(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeInfo = cm.getActiveNetworkInfo();
//        NetworkType type = NetworkType.NET_UNKNOWN;
//        if (activeInfo != null && activeInfo.isConnectedOrConnecting()) {
//            switch (activeInfo.getType()) {
//                case ConnectivityManager.TYPE_WIFI:
//                    type = NetworkType.WIFI;
//                    break;
//                case ConnectivityManager.TYPE_MOBILE:
//                    switch (activeInfo.getSubtype()) {
//                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
//                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
//                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
//                        case TelephonyManager.NETWORK_TYPE_1xRTT:
//                        case TelephonyManager.NETWORK_TYPE_IDEN:
//                            type = NetworkType.NET_2G;
//                            break;
//                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
//                        case TelephonyManager.NETWORK_TYPE_UMTS:
//                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                        case TelephonyManager.NETWORK_TYPE_HSDPA:
//                        case TelephonyManager.NETWORK_TYPE_HSUPA:
//                        case TelephonyManager.NETWORK_TYPE_HSPA:
//                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
//                        case TelephonyManager.NETWORK_TYPE_EHRPD:
//                        case TelephonyManager.NETWORK_TYPE_HSPAP:
//                            type = NetworkType.NET_3G;
//                            break;
//                        case TelephonyManager.NETWORK_TYPE_LTE:
//                            type = NetworkType.NET_4G;
//                            break;
//                        default:
//                            type = NetworkType.NET_UNKNOWN;
//                    }
//                    break;
//                default:
//                    type = NetworkType.NET_UNKNOWN;
//            }
//        }
//        return type;
//    }
}
