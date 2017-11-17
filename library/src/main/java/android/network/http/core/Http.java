package android.network.http.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Http {

    private static Map<String, String> cookieCache = new HashMap<>();
    private static SSLContext sslcontext;
    private static HostnameVerifier hostnameVerifier;

    private static void addHeaders(HttpURLConnection conn, Map<String, String> headers) {
        if(headers == null || headers.size() == 0){
            return;
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    public static String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            url = URLFormat.getFormatUrl(url, params);
        }
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        int responseCode = conn.getResponseCode();
        StringBuffer body = null;
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            body = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return body != null ? body.toString() : null;
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        URL _url = new URL(URLFormat.getFormatUrl(url, null));
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        int responseCode = conn.getResponseCode();
        StringBuffer body = null;
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            body = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        conn.disconnect();
        return body != null ? body.toString() : null;
    }

    public static String post(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        URL _url = new URL(URLFormat.getFormatUrl(url, null));
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
        addHeaders(conn, headers);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            boolean isAppendAt = false;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (isAppendAt) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(urlEncode(entry.getValue()));
                isAppendAt = true;
            }
        }
        OutputStream out = conn.getOutputStream();
        out.write(sb.toString().getBytes("UTF-8"));
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

    private static void progress(int contentLength, int writeLength) {
        Log.e("ESA", String.valueOf(((float) writeLength) / ((float) contentLength)));
    }

    /**
     * https://developer.android.com/reference/java/net/HttpURLConnection.html
     * To upload data to a web server, configure the connection for output using setDoOutput(true).
     * For best performance, you should call either setFixedLengthStreamingMode(int) when the body length is known in advance,
     * or setChunkedStreamingMode(int) when it is not.
     * Otherwise HttpURLConnection will be forced to buffer the complete request body in memory before it is transmitted,
     * wasting (and possibly exhausting) heap and increasing latency.
     * @param url
     * @param headers
     * @param params
     * @param fileMap
     * @return
     * @throws IOException
     */
    public static String upload(String url, Map<String, String> headers, Map<String, String> params, Map<String, File[]> fileMap) throws IOException {
        String boundary = "---------------------------";
        String endLine = "--" + boundary + "--";
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
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
        addHeaders(conn, headers);

        int paramsBodyLength = 76;
        int fileBodyLength = 129;
        int contentLength = 0;
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                contentLength += (paramsBodyLength + entry.getKey().length() + entry.getValue().length());
            }
        }
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                String fieldName = entry.getKey();
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    contentLength += (fileBodyLength + fieldName.length() + file.getName().length() + file.length());
                }
            }
        }
        contentLength += (endLine.length());
        conn.setFixedLengthStreamingMode(contentLength);

        int writeLength = 0;

        OutputStream out = conn.getOutputStream();
        StringBuilder body = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                body.delete(0, body.length());
                body.append("--").append(boundary).append("\r\n");
                body.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"\r\n\r\n");
                body.append(entry.getValue());
                body.append("\r\n");
                out.write(body.toString().getBytes("UTF-8"));
                out.flush();
                writeLength += body.length();
                progress(contentLength, writeLength);
            }
        }
        byte[] buffer = new byte[10240];
        if (fileMap != null && fileMap.size() > 0) {
            for (Map.Entry<String, File[]> entry : fileMap.entrySet()) {
                String fieldName = entry.getKey();
                File[] files = entry.getValue();
                if (files == null || files.length == 0) {
                    continue;
                }
                for (File file : files) {
                    body.delete(0, body.length());
                    body.append("--").append(boundary).append("\r\n");
                    body.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"\r\n");
                    body.append("Content-Type: application/octet-stream\r\n\r\n");
                    out.write(body.toString().getBytes("UTF-8"));
                    writeLength += body.length();
                    progress(contentLength, writeLength);
                    FileInputStream fis = new FileInputStream(file);
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        writeLength += len;
                        progress(contentLength, writeLength);
                    }
                    out.write("\r\n".getBytes("UTF-8"));
                    out.flush();
                    fis.close();
                    writeLength += 2;
                    progress(contentLength, writeLength);
                }
            }
        }
        out.write(endLine.getBytes("UTF-8"));//发型结束符行
        out.flush();
        writeLength += endLine.length();
        progress(contentLength, writeLength);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            body.delete(0, body.length());
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            reader.close();
        }
        cookie = conn.getHeaderField("Set-Cookie");
        if (cookie != null) {
            cookieCache.put(host, cookie);
        }
        out.close();
        conn.disconnect();
        return body.length() > 0 ? body.toString() : null;
    }

    public static boolean download(String url, Map<String, String> headers, InputStreamCallback callback) throws IOException {
        URL _url = new URL(url);
        String host = _url.getHost();
        HttpURLConnection conn = getHttpURLConnection(_url);
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        String cookie = cookieCache.get(host);
        if (cookie != null) {
            conn.setRequestProperty("Cookie", cookie);
        }
//        断点下载头
//        if (file.exists() && file.length() > 0) {
//            conn.addRequestProperty("Range", "bytes=" + file.length() + "-");
//        } else {
//            conn.addRequestProperty("Range", "bytes=0-");
//        }
        addHeaders(conn, headers);
        int responseCode = conn.getResponseCode();
        boolean result = false;
        if (responseCode == 200) {
            InputStream stream = conn.getInputStream();
            callback.stream(stream);
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

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection conn;
        if (url.toString().startsWith("https")) {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(getSSLContext().getSocketFactory());
            connection.setHostnameVerifier(getHostnameVerifier());
            conn = connection;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        return conn;
    }

    private static SSLContext getSSLContext() {
        if (sslcontext == null) {
            TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            try {
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return sslcontext;
    }

    private static HostnameVerifier getHostnameVerifier() {
        if (hostnameVerifier == null) {
            hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
        }
        return hostnameVerifier;
    }

}
