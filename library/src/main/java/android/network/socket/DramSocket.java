package android.network.socket;

import android.network.socket.codec.Handle;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DramSocket {

    private Processor processor;
    private InetSocketAddress address;
    private ExecutorService pool;
    private Socket socket;
    private WriteRunnable writeRunnable;
    private ReadRunnable readRunnable;

    public DramSocket() {
        pool = Executors.newFixedThreadPool(3);
    }

    public void connect(String host, int port) {
        this.address = new InetSocketAddress(host, port);
    }

    public void connect(InetSocketAddress address) {
        this.address = address;
    }

    public void processor(Processor processor) {
        this.processor = processor;
    }

    public void start() {
        if (processor == null) {
            throw new NullPointerException("请设置消息处理器");
        }
        if (address == null) {
            throw new NullPointerException("请设置链接地址");
        }
        try {
            socket = new Socket();
            socket.connect(address);
            if (socket.isConnected()) {
                processor.handleStatus(Handle.STATUS_CONNECTED);
                writeRunnable = new WriteRunnable(socket.getOutputStream(), processor);
                readRunnable = new ReadRunnable(socket.getInputStream(), processor);
                pool.execute(writeRunnable);
                pool.execute(readRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            processor.handleStatus(Handle.STATUS_FAIL);
        }
    }

    public void stop() {
        if (writeRunnable != null) {
            writeRunnable.stop();
            writeRunnable = null;
        }
        if (readRunnable != null) {
            readRunnable.stop();
            readRunnable = null;
        }
        if (socket != null) {
            shutdownInput(socket);
            shutdownOutput(socket);
            close(socket);
            socket = null;
            if (processor != null) {
                processor.handleStatus(Handle.STATUS_DISCONNECT);
            }
        }

    }

    private static final class ReadRunnable implements Runnable {

        private InputStream stream;
        private Processor processor;

        private ReadRunnable(InputStream stream, Processor processor) {
            this.stream = stream;
            this.processor = processor;
        }

        @Override
        public void run() {
            try {
                final ByteBuffer buffer = ByteBuffer.allocate(102400);//解码需要操作的buffer
                byte[] bytes = new byte[10240];//读取的缓冲区
                byte[] cache = new byte[buffer.capacity()];//缓存没有被解码的缓冲区
                byte[] swap = new byte[buffer.capacity()];//用于和缓存缓冲区交换用的缓冲区
                int cacheLength = 0;//缓存的长度
                int readLength;//读取到的数据长度
                while ((readLength = stream.read(bytes)) > 0) {
                    System.arraycopy(bytes, 0, cache, cacheLength, readLength);//把读取到的数据拷贝到上次缓存缓冲区的后面
                    buffer.clear();//重置解码的ByteBuffer
                    cacheLength += readLength;//缓存长度=上次的缓存长度+读取的数据长度
                    buffer.put(cache, 0, cacheLength);
                    buffer.flip();//切换到读模式
                    buffer.mark();//先标记当前开始读取的点，用于后面不够解码后reset操作
                    Object o;
                    while (buffer.hasRemaining() && ((o = processor.decode(buffer)) != null)) {//判断如果ByteBuffer后面有可读数据并且解码一次
                        processor.handle(o);//把解码的数据回调给Handler
                        if (buffer.hasRemaining()) {//再次判断ByteBuffer后面是否还有可读数据
                            int position = buffer.position();//ByteBuffer当前读取到的位置
                            int limit = buffer.limit();//ByteBuffer数据总长度
                            int length = limit - position;//ByteBuffer剩余数据长度
                            System.arraycopy(cache, position, swap, 0, length);//拷贝缓存剩余长度的数据到交换缓冲区
                            System.arraycopy(swap, 0, cache, 0, length);//在把交换缓冲区的数据拷贝的缓存缓冲区用于下次解码
                            cacheLength = length;//重置缓存缓冲区长度为剩余数据长度
                            buffer.clear();//再次重置解码的ByteBuffer
                            buffer.put(cache, 0, cacheLength);
                            buffer.flip();//切换到读模式
                        }
                        buffer.mark();//再次标记当前开始读取点
                    }
                    buffer.reset();//上面解码完成后重置到make读取点
                    if (buffer.hasRemaining()) {//判断是否还有数据可读
                        int remaining = buffer.remaining();//剩余可读长度
                        buffer.get(cache, 0, remaining);//将剩余数据拷贝到缓存缓冲区
                        cacheLength = remaining;//缓存数据长度为当前剩余数据长度
                    } else {
                        cacheLength = 0;//如果没有可读的数据 缓存数据长度为0
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void stop() {
            close(stream);
        }
    }

    private static final class WriteRunnable implements Runnable {
        private OutputStream stream;
        private Processor processor;

        private WriteRunnable(OutputStream stream, Processor processor) {
            this.stream = stream;
            this.processor = processor;
        }

        @Override
        public void run() {

        }

        private void stop() {

        }
    }

    private static void shutdownInput(Socket socket) {
        if (socket != null && !socket.isInputShutdown()) {
            try {
                socket.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void shutdownOutput(Socket socket) {
        if (socket != null && !socket.isOutputShutdown()) {
            try {
                socket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
