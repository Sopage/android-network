package android.network.socket;

import java.nio.ByteBuffer;

public class Processor {

    private Encode encode;
    private Handle handle;
    private Decode decode;

    public Encode getEncode() {
        return encode;
    }

    public void setEncode(Encode encode) {
        this.encode = encode;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }

    public Decode getDecode() {
        return decode;
    }

    public void setDecode(Decode decode) {
        this.decode = decode;
    }

    public void handleStatus(int status) {
        if(this.handle != null){
            handle.onStatus(status);
        }
    }

    public void handle(Object object){
        if(this.handle != null){
            handle.onReceive(object);
        }
    }

    public Object decode(ByteBuffer buffer) {
        if(this.decode != null){
            return this.decode.decode(buffer);
        }
        throw new NullPointerException("没有找到解码器");
    }

    public ByteBuffer decode(Object object) {
        if(this.encode != null){
            return this.encode.encode(object);
        }
        throw new NullPointerException("没有找到编码器");
    }
}
