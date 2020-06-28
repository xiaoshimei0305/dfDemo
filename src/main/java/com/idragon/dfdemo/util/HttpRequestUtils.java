package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author chenxinjun
 */
@Slf4j
public class HttpRequestUtils {

    public static String getHttpEntityContent(HttpResponse response) throws UnsupportedOperationException, IOException {
        String result = "";
        HttpEntity entity = response.getEntity();
        if(entity != null){
            InputStream in = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber= new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                strber.append(line+'\n');
            }
            br.close();
            in.close();
            result = strber.toString();
        }

        return result;

    }
    public static String getRequest(String url){
        String result = "";
        HttpGet get = new HttpGet(url);
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpResponse response = httpClient.execute(get);
            result = getHttpEntityContent(response);

            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                result = "服务器异常";
            }
        } catch (Exception e){
            log.error("getRequest error {}",e);
            throw new RuntimeException(e);
        } finally{
            get.abort();
        }
        return result;
    }

    /**
     * GET 获取商品信息
     * @param url
     * @return
     */
    public static JSONObject get(String url){
        String result=getRequest(url);
        if(StringUtils.isBlank(result)){
            return null;
        }
        try {
            return JSONObject.parseObject(result);
        }catch (Exception e){
            log.error("url 请求失败：{}",url);
        }
        return null;
    }
}
