package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 东方购物短链接请求测试
 * @author chenxinjun
 */
public class OcjShortAddressRequest {

    private static Pattern pattern=Pattern.compile("href=\"([\\s\\S]*?)\">");


    public static String post(String urlStr){
        String result="";
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(urlStr.replaceAll("amp;","").split("&fwd_url")[0].replaceAll("\\s*",""));

        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)
        String jsonString = "{}";

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result= EntityUtils.toString(responseEntity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 发起https请求并获取结果
     *
     * @param: requestUrl
     *            请求地址
     * @param : requestMethod
     *            请求方式（GET、POST）
     * @param outputStr
     *            提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpsRequest(String urlStr, String method, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            // 打开restful链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toUpperCase());
           // conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取短链接访问短最终目标
     * @param urlId
     * @return
     */
    public static String getDisinationUrlId(String urlId){
        String resultHtml = post("http://m.ocj.com.cn/d/"+urlId);
        Matcher matcher = pattern.matcher(resultHtml);
        if(matcher.find()){
            return matcher.group(1);
        }else{
            return "";
        }
    }
    public static String getDisinationUrl(String url){
        String resultHtml = post(url);
        Matcher matcher = pattern.matcher(resultHtml);
        if(matcher.find()){
            return matcher.group(1);
        }else{
            return "";
        }
    }
    public static void build(String sourceFile,String targetFile) throws IOException {
        String content=FileUtils.readFileContent(new File(sourceFile));
        String[] list=content.split("\n");
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<list.length;i++){
            String key=list[i];
            String value=getDisinationUrlId(key);
            if(value.contains("urlid")){
                value=getDisinationUrl(value);
            }
            System.out.println("数据内容["+i+"]："+key+"="+value);
            sb.append(key).append(",").append(value).append("\n");
        }
        FileUtils.wiriteFileContent(new File(targetFile),sb.toString());
    }


    public static void main(String[] args) throws IOException {
        String sourceFile="/Users/chenxinjun/Downloads/tcode_web.txt";
        String targetFile="/Users/chenxinjun/Downloads/tcode_web.csv";
        build(sourceFile,targetFile);
    }
}