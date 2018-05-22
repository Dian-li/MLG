package com.bupt.client;

import com.alibaba.fastjson.JSONObject;
import com.bupt.util.FileUtil;
import com.sun.net.httpserver.Headers;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.portable.InputStream;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Component
public class Httpclient {

    public void post(String url, String filepath, Map<String,String> params)throws Exception {
        HttpClient httpclient = HttpClients.createDefault();

        URL url1 = new URL(url);
        URI uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
        HttpPost httpPost = new HttpPost(uri);
        //FileEntity fileEntity = new FileEntity(new File(filepath), ContentType.APPLICATION_OCTET_STREAM);

        JSONObject jsonObject = new JSONObject();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for(Map.Entry<String,String> param:params.entrySet()){
            jsonObject.put(param.getKey(),param.getValue());
        }


        EntityBuilder entityBuilder = EntityBuilder.create();
        entityBuilder.setFile(new File(filepath));
        entityBuilder.setParameters(pairs);

        httpPost.setEntity(entityBuilder.build());

        HttpResponse httpResponse = httpclient.execute(httpPost);

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if(statusCode== HttpStatus.SC_OK){
            System.out.println("got the back.");
        }
    }

    public HttpResponse postParam(String url, Map<String,String> params)throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpHost proxy = new HttpHost("127.0.0.1",8888,null);
        //httpClientBuilder.setProxy(proxy);
        //httpClientBuilder.setUserAgent("Mozilla/5.0 (X11; U; Linux i686; en; rv:1.9.1.2) Gecko/20090803 Fedora/3.5");
        HttpClient httpclient = httpClientBuilder.build();
        url = addProtocolForURL(url);
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if(params!=null && params.size()!=0){
            JSONObject jsonObject = new JSONObject();
            for(Map.Entry<String,String> param:params.entrySet()){
                jsonObject.put(param.getKey(),param.getValue());
            }

            EntityBuilder entityBuilder = EntityBuilder.create();
            entityBuilder.setText(JSONObject.toJSONString(jsonObject));
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding("UTF-8");
            httpPost.setEntity(entityBuilder.build());
        }
        return httpclient.execute(httpPost);
    }

    public HttpResponse postRequest(String url,String filename, Map<String,Object> params)throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpHost proxy = new HttpHost("127.0.0.1",8888,null);
        //httpClientBuilder.setProxy(proxy);
        //httpClientBuilder.setUserAgent("Mozilla/5.0 (X11; U; Linux i686; en; rv:1.9.1.2) Gecko/20090803 Fedora/3.5");
        HttpClient httpclient = httpClientBuilder.build();
        url = addProtocolForURL(url);
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if(params!=null && params.size()!=0){
            JSONObject jsonObject = new JSONObject();
            for(Map.Entry<String,Object> param:params.entrySet()){
                jsonObject.put(param.getKey(),param.getValue());
            }
            String fileContext = FileUtil.getFileContext(filename);
            jsonObject.put("file",fileContext);

            EntityBuilder entityBuilder = EntityBuilder.create();
            entityBuilder.setText(JSONObject.toJSONString(jsonObject));
            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
            entityBuilder.setContentEncoding("UTF-8");
            httpPost.setEntity(entityBuilder.build());
        }
        return httpclient.execute(httpPost);
    }

    public int getParam(String url, Map<String,String> params)throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //HttpHost proxy = new HttpHost("127.0.0.1",8888,null);
        //httpClientBuilder.setProxy(proxy);
        //httpClientBuilder.setUserAgent("Mozilla/5.0 (X11; U; Linux i686; en; rv:1.9.1.2) Gecko/20090803 Fedora/3.5");
        HttpClient httpclient = httpClientBuilder.build();
        url = addProtocolForURL(url);
        if(params!=null && params.size()!=0){
            //JSONObject jsonObject = new JSONObject();
            url += "?";
            for(Map.Entry<String,String> param:params.entrySet()){
                url += param.getKey()+"="+param.getValue();
            }
        }
        System.out.println("url:"+url);
        URLEncoder.encode(url,"utf-8");
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpclient.execute(httpGet);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if(statusCode== HttpStatus.SC_OK){
            HttpEntity response =  httpResponse.getEntity();
            String result = EntityUtils.toString(response,"UTF-8");
            System.out.println(result);
        }
        return statusCode;
    }

    public boolean postFile(String url,String filepath)throws Exception{
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //proxy for fiddler cach package
        //HttpHost proxy = new HttpHost("127.0.0.1",8888,null);
        //httpClientBuilder.setProxy(proxy);
        //httpClientBuilder.setUserAgent("Mozilla/5.0 (X11; U; Linux i686; en; rv:1.9.1.2) Gecko/20090803 Fedora/3.5");
        HttpClient httpclient = httpClientBuilder.build();
        url = addProtocolForURL(url);
        HttpPost httpPost = new HttpPost(url);
        EntityBuilder entityBuilder = EntityBuilder.create();
        String fileContext;
        try {
            fileContext = FileUtil.getFileContext(filepath);
        }catch (Exception e){
            fileContext = "";
        }
        if(fileContext=="")
            return false;
        entityBuilder.setText(fileContext);
        entityBuilder.setContentEncoding("UTF-8");
        httpPost.setEntity(entityBuilder.build());
        HttpResponse httpResponse = httpclient.execute(httpPost);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if(statusCode== HttpStatus.SC_OK){
            HttpEntity response =  httpResponse.getEntity();
            String result = EntityUtils.toString(response,"UTF-8");
            System.out.println(result);
            return true;
        }else{
            return false;
        }
    }



    String addProtocolForURL(String url){
        if (url.indexOf("http://")==-1 && url.indexOf("https://")==-1){
            return "http://"+url;
        }else{
            return url;
        }
    }
}
