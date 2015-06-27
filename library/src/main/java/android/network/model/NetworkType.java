package android.network.model;

/**
 * Created by sanders on 15/6/27.
 */
public enum NetworkType {
    NET_UNKNOWN("未知"),
    NET_2G("2G网络"),
    NET_3G("3G网络"),
    NET_4G("4G网络"),
    WIFI("WIFI网络");

    private String name;

    NetworkType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
