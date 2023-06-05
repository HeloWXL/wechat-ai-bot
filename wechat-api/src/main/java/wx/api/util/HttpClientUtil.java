package wx.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 编码格式。发送编码格式统一用UTF-8
     */
    private static final String ENCODING = "UTF-8";

    /**
     * 设置连接超时时间，单位毫秒。
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * 请求获取数据的超时时间(即响应时间)，单位毫秒。
     */
    private static final int SOCKET_TIMEOUT = 5000;

    /**
     * Description: get请求
     *
     * @param url 路径
     * @return 响应结果
     */
    public static String get(String url) {
        logger.info("请求方式：GET ， 请求地址：[{}]", url);
        return get(url, null);
    }

    /**
     * Description: post请求
     *
     * @param url    路径
     * @param params 参数
     * @return 响应结果
     */
    public static String post(String url, Map<String, Object> params) throws Exception {
        logger.info("请求方式：POST ， 请求地址：[{}] ,参数：[{}]", url, params);
        return post(url, params, null);
    }

    /**
     * Description: post请求（用于请求json格式的参数）
     *
     * @param url    路径
     * @param params json参数
     * @return 响应结果
     */
    public static String jsonPost(String url, String params) throws Exception {
        logger.info("请求方式：POST ContextType: JSON ，请求地址：[{}] ,参数：[{}]", url, params);
        return jsonPost(url, params, null);
    }

    /**
     * Description: get请求
     *
     * @param url     路径
     * @param headers 请求头信息
     * @return 响应结果
     */
    public static String get(String url, Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet;
        CloseableHttpResponse httpResponse = null;
        String result;
        try {
            httpClient = HttpClientBuilder.create().build();

            httpGet = new HttpGet(url);
            //设置请求头
            setHeader(headers, httpGet);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpGet.setConfig(config);
            result = getHttpClientResult(httpResponse, httpClient, httpGet);
        } finally {
            try {
                release(httpResponse, httpClient);
            } catch (Exception e) {
                logger.error("HTTP 关闭异常");
            }
        }
        return result;
    }


    /**
     * Description: post请求
     *
     * @param url     路径
     * @param params  参数
     * @param headers 请求头信息
     * @return 响应结果
     */
    public static String post(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost;
        CloseableHttpResponse httpResponse = null;
        String result;
        try {
            httpClient = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            //设置请求头
            setHeader(headers, httpPost);
            //设置参数
            setParam(params, httpPost);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpPost.setConfig(config);
            result = getHttpClientResult(httpResponse, httpClient, httpPost);
        } finally {
            release(httpResponse, httpClient);
        }
        return result;
    }


    /**
     * Description: post请求（用于请求json格式的参数）
     *
     * @param url     路径
     * @param params  json参数
     * @param headers 请求头信息
     * @return 响应结果
     */
    public static String jsonPost(String url, String params, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost;
        CloseableHttpResponse httpResponse = null;
        String result;
        try {
            httpClient = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            //设置请求头
            setHeader(headers, httpPost);
            //设置参数
            httpPost.setEntity(new StringEntity(params, ENCODING));
            RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpPost.setConfig(config);

            result = getHttpClientResult(httpResponse, httpClient, httpPost);
        } finally {
            release(httpResponse, httpClient);
        }
        return result;
    }

    /**
     * Description: 封装请求头
     *
     * @param headers    请求头
     * @param httpMethod httpMethod
     */
    public static void setHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        // 封装请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpMethod.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params     请求参数
     * @param httpMethod httpMethod
     */
    public static void setParam(Map<String, Object> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {

        List<NameValuePair> list = new ArrayList<>();
        if (null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                list.add(new BasicNameValuePair(key, value));
            }
        }
        if (list.size() > 0) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, ENCODING);
            httpMethod.setEntity(entity);
        }
    }

    /**
     * Description: 获取响应结果
     *
     * @param httpResponse httpResponse
     * @param httpClient   httpClient
     * @param httpMethod   httpMethod
     * @return 响应结果
     * @throws Exception Exception
     */
    public static String getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) {
        String result = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpMethod);

            // 获取返回结果
            if (httpResponse != null) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, ENCODING);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        return result;
    }


    /**
     * Description: 释放资源
     *
     * @param httpResponse HttpResponse
     * @param httpClient   HttpClient
     * @throws IOException IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }

    }
}
