package com.inspur.analysis.util;

/**
 * Created by liutingna on 2017/9/15.
 */
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public final class HttpClientUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private final static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    private HttpClientUtil() {
    }

    public static CloseableHttpClient getHttpClient(String serviceUrl) {
        if (serviceUrl.equals("")) throw new IllegalArgumentException("serviceUrl cannt be \"\" or null");
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        // Increase max connections for localhost:8082 to 50
        HttpHost localhosthost = new HttpHost(serviceUrl);
        cm.setMaxPerRoute(new HttpRoute(localhosthost), 50);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        return httpClient;
    }

    public static void release() {
        cm.shutdown();
    }

    public static Object sendPostRequest(String serviceUrl, String methodName, Map<String, Object> paramMap) {

        CloseableHttpClient httpClient = getHttpClient(serviceUrl);
        String jsonString = "";
        String methodUrl;
        if (serviceUrl.endsWith("/")) {
            methodUrl = serviceUrl + methodName;
        } else {
            methodUrl = serviceUrl + "/" + methodName;
        }
        try {
            HttpPost request = new HttpPost(methodUrl);
            String paramStr = JSON.toJSONString(paramMap);
            StringEntity params = new StringEntity(paramStr, "UTF-8");
            params.setContentType("application/json;charset=UTF-8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                jsonString += temp;
            }
        } catch (Exception ex) {
            logger.debug("post method exception: {}", (Object[]) ex.getStackTrace());
        }
        return jsonString;
    }

}