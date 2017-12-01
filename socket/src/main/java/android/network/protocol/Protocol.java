package android.network.protocol;

public interface Protocol {

    //{起始标记   -byte     - 1}
    //{包总长度   -int      - 4}
    //{包的类型   -int      - 4}
    //{发送者ID   -int      - 4}
    //{接受者ID   -int      - 4}
    //{消息ID    -byte[32] - 32}
    //{包体内容   -byte[n]  - n}
    //{结束标记   -byte     - 1}

    /**
     * 包头长度(算上头和最后一个结束标记)
     */
    int HEADER_LENGTH = 50;

    int ID_LENGTH = 32;

    /**
     * 起始标记
     */
    byte START_TAG = '<';

    /**
     * 结束标记
     */
    byte END_TAG = '>';

}