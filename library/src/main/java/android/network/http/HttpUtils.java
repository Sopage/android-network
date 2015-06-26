package android.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static Map<String, String> cookieCache = new HashMap<String, String>();

    public static String httpGet(String url, Map<String, String> params) throws IOException {
        if(params != null){
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String, String> entry : params.entrySet()){
                sb.append(entry.getKey()).append("=").append(urlEncode(entry.getValue())).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            if(url.indexOf("?")>0){
                url += sb.toString();
            }else{
                url = url + "?" + sb.toString();
            }
        }
        return httpGet(url);
    }

    public static String httpGet(String url) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        int responseCode = conn.getResponseCode();
        StringBuffer sb = null;
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return sb != null ? sb.toString() : null;
    }

    public static String httpPost(String url, Map<String, String> params) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(urlEncode(entry.getValue())).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        OutputStream out = conn.getOutputStream();
        out.write(sb.toString().getBytes());
        out.flush();
        out.close();
        sb.delete(0, sb.length());
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } else {
            sb = null;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return sb != null ? sb.toString() : null;
    }

    public static String httpUpload(String url, Map<String, String> params, String fieldName, File... files) throws IOException {
        String boundary = "---------------------------";
        String endLine = "\r\n--" + boundary + "--\r\n";
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        StringBuilder textEntity = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                textEntity.append("--").append(boundary).append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
        }
        StringBuilder sb = new StringBuilder();
        long fileLength = 0;
        for (File file : files) {
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"\r\n");
            sb.append("Content-Type: application/octet-stream\r\n\r\n");
            fileLength += file.length();
            sb.append("\r\n");
        }
        int textLength = sb.length();
        int textEntityLength = textEntity.length();
        conn.setRequestProperty("Content-Length", String.valueOf(textLength + fileLength + endLine.length() + textEntityLength));
        OutputStream out = conn.getOutputStream();
        out.write(textEntity.toString().getBytes());
        byte[] buffer = new byte[1024 * 5];
        for (File file : files) {
            sb.delete(0, sb.length());
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"\r\n");
            sb.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(sb.toString().getBytes());
            FileInputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.write("\r\n".getBytes());
            out.flush();
            fis.close();
        }
        out.write(endLine.getBytes());
        out.flush();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb.delete(0, sb.length());
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        out.close();
        conn.disconnect();
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * @param uploadUrl 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.xxx.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params    请求参数 key为参数名,value为参数值
     * @param files     上传文件
     */
    public static boolean socketUpload(String uploadUrl, Map<String, String> params, String fieldName, File... files) throws IOException {
        final String BOUNDARY = "---------------------------"; //数据分隔线
        final String endLine = "--" + BOUNDARY + "--\r\n";//数据结束标志
        int fileDataLength = 0;
        for (File uploadFile : files) {//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\"" + fieldName + "\";filename=\"" + uploadFile.getName() + "\"\r\n");
            fileExplain.append("Content-Type: application/octet-stream\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            fileDataLength += uploadFile.length();
        }
        StringBuilder textEntity = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endLine.getBytes().length;
        URL url = new URL(uploadUrl);
        String host = url.getHost();
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestMethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestMethod.getBytes());
        String accept = "Accept: */*\r\n";
        outStream.write(accept.getBytes());
        outStream.write("Accept-Charset: UTF-8\r\n".getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            cookie = "Cookie: " + cookie + "\r\n";
            outStream.write(cookie.getBytes());
        }
        String contentType = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
        outStream.write(contentType.getBytes());
        String contentLength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentLength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        for (File uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\"" + fieldName + "\";filename=\"" + uploadFile.getName() + "\"\r\n");
            fileEntity.append("Content-Type: application/octet-stream\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            byte[] buffer = new byte[1024];
            int len;
            FileInputStream inputStream = new FileInputStream(uploadFile);
            while ((len = inputStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, len);
            }
            inputStream.close();
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endLine.getBytes());
        outStream.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (!reader.readLine().startsWith("HTTP/1.1 200 OK")) {//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return false;
        }
        String line;
        while ((line = reader.readLine()) != null) {
        }
        reader.close();
        outStream.close();
        socket.close();
        return true;
    }

    public static boolean download(String url, File saveFile) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        int responseCode = conn.getResponseCode();
        boolean result = false;
        if (responseCode == 200) {
            InputStream stream = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(saveFile);
            byte[] buffer = new byte[1024 * 5];
            int len;
            while ((len = stream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
            stream.close();
            result = true;
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return result;
    }

    private static String urlEncode(String params) {
        try {
            if (params == null) {
                return "";
            }
            return URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return params;
        }
    }
}
