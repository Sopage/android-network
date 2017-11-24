package android.network.protocol;

public interface Protocol {

    //{起始标记   -byte     - 1}
    //{包的类型   -int      - 4}
    //{包总长度   -int      - 4}
    //{包总长度   -byte[22] - 22}
    //{包体内容   -byte[n]  - n}
    //{结束标记   -byte     - 1}

    /**
     * 包头长度(算上头和最后一个结束标记)
     */
    int HEADER_LENGTH = 32;

    /**
     * 起始标记
     */
    byte START_TAG = '<';

    /**
     * 保留头
     */
    byte[] RETAIN = new byte[22];

    /**
     * 结束标记
     */
    byte END_TAG = '>';

}