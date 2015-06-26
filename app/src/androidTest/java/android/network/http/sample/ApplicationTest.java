package android.network.http.sample;

import android.app.Application;
import android.network.http.HttpUtils;
import android.test.ApplicationTestCase;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testHttp(){
        try {
            String s = HttpUtils.httpGet("http://www.baidu.com");
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}