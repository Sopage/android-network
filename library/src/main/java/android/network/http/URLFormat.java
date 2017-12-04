package android.network.http;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Mr.Huang
 * @date 2017/11/17
 */
public class URLFormat {

    public static String getFormatUrl(String urlString, Map<String, String> params) {
        try {
            URL url = new URL(urlString);
            StringBuilder sb = new StringBuilder();
            sb.append(url.getProtocol()).append("://").append(url.getHost());
            int port = url.getPort();
            if (port > 0) {
                sb.append(":").append(port);
            }
            String path = fixPathStart(url.getPath());
            if (path != null) {
                sb.append("/").append(path);
            }
            fixPathEnd(sb);
            String query = url.getQuery();
            boolean isQuery = (query != null && query.trim().length() > 0);
            if (isQuery) {
                sb.append("?");
                String[] ps = query.split("&");
                int len = ps.length;
                for (int i = 0; i < len; i++) {
                    String p = ps[i];
                    String[] kv = p.split("=");
                    if (kv.length == 2) {
                        if ((i + 1) != len) {
                            sb.append("&");
                        }
                        sb.append(encode(kv[0])).append("=").append(encode(kv[1]));
                    }
                }
            }
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    String value = param.getValue();
                    if (value == null || value.trim().length() == 0) {
                        continue;
                    }
                    String key = param.getKey();
                    if (isQuery) {
                        sb.append("&");
                    } else {
                        sb.append("?");
                        isQuery = true;
                    }
                    sb.append(encode(key)).append("=").append(encode(value));
                }
            }
            return sb.append(isQuery ? "&" : "?").append("timestamp=").append(System.currentTimeMillis()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return urlString;
        }
    }

    private static void fixPathEnd(StringBuilder sb) {
        int len = sb.length();
        if (sb.charAt(len - 1) == '/') {
            sb.deleteCharAt(len - 1);
            fixPathEnd(sb);
        }
    }

    private static String fixPathStart(String path) {
        if (path == null) {
            return null;
        }
        path = path.trim();
        if (path.length() == 0 || "/".equals(path)) {
            return null;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        } else {
            return path;
        }
        return fixPathStart(path);
    }

    public static String encode(String params) {
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
