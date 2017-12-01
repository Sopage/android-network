package android.network.sdk.listener;

import android.network.protocol.Body;

/**
 * @author Mr.Huang
 * @date 2017/8/23
 */
public interface OnReceiverMessage {

    void onMessage(Body body);

    void onStatus(int status);

}
