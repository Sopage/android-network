package android.network.protocol;

public class Protocol {

    //{起始标记   -byte     -1}
    //{协议版本   -byte     -1}
    //{包总长度   -int      -4}
    //{包头保留   -byte[24] -24}
    //{包头校验   -byte     -1}
    //{包体内容   -byte[n]  -n}
    //{结束标记   -byte     -1}

    /**
     * 包头长度(算上最后一个结束标记)
     */
    public static final int HEADER_LENGTH = 32;

    /**
     * 协议版本
     */
    public static final byte VERSION = 1;

    /**
     * 起始标记
     */
    public static final byte START_TAG = '<';

    /**
     * 校验字符
     */
    public static final byte VERIFY_TAG = '-';

    /**
     * 结束标记
     */
    public static final byte END_TAG = '>';

    /**
     * 保留位置
     */
    public static final byte[] RETAIN = new byte[24];

    public int version;

}