package com.lorabit.util;

import com.google.common.base.Charsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lorabit
 * @since 16-2-24
 */
public class HttpHelper {

  private static HttpClient httpClient;
  private static final ConnManageThread demonConnManageThread;

  static {
    HttpParams params = new BasicHttpParams();
    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
    PoolingClientConnectionManager connManager = new PoolingClientConnectionManager();
    connManager.setMaxTotal(200);
    httpClient = new DefaultHttpClient(connManager, params);

    demonConnManageThread = new ConnManageThread(connManager);
    demonConnManageThread.start();
  }


  public T getReq(HttpUriRequest req, ResponseHandler<T> handler) {
    try {
      return httpClient.execute(req, handler);
    } catch (IOException e) {
      e.printStackTrace();
      //todo log
      return null;
    }
  }

  public T postReq(HttpPost req, Map<String, String> params, ResponseHandler<T> handler) {
    List<NameValuePair> nvPairList = new ArrayList<>();
    for (Map.Entry<String, String> param : params.entrySet()) {
      NameValuePair nvPair = new BasicNameValuePair(param.getKey(), param.getValue());
      nvPairList.add(nvPair);
    }
    req.setEntity(new UrlEncodedFormEntity(nvPairList, Charsets.UTF_8));
    try {
      return httpClient.execute(req, handler);
    } catch (IOException e) {
      e.printStackTrace();
      //todo log
      return null;
    }
  }

  public <T> T postReq(HttpPost req, String params, ResponseHandler<T> handler) {
    req.setEntity(new StringEntity(params, ContentType.APPLICATION_FORM_URLENCODED));
    try {
      return httpClient.execute(req, handler);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String postReq(HttpPost req, String params) {
    return (String) postReq(req, params, new DemoHandler<String>());
  }

  abstract class basicHandler<T> implements ResponseHandler<T>{
    private final int UNSUCCESS_CODE = 300;

    @Override
    public T handleResponse(HttpResponse response) throws HttpResponseException {
      if(response.getStatusLine().getStatusCode() >= UNSUCCESS_CODE){
        throw new HttpResponseException(response.getStatusLine().getStatusCode(), "proccess error");
      }
      return handler(response);
    }

    abstract T handler(HttpResponse response);
  }

  public class DemoHandler<String> extends basicHandler{

    @Override
    java.lang.String handler(HttpResponse response) {
      HttpEntity entity = response.getEntity();
      try {
        if(entity == null) return "";
        return EntityUtils.toString(entity);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }


  private static class ConnManageThread extends Thread{
    private PoolingClientConnectionManager connManager;
    private AtomicBoolean stop = new AtomicBoolean(false);

    public ConnManageThread(PoolingClientConnectionManager connManager){
      this.connManager = connManager;
    }

    public  void shutdown(){
      this.stop.set(true);
    }

    @Override
    public void run() {
      while(!stop.get()){
        connManager.closeExpiredConnections();
        connManager.closeIdleConnections(30, TimeUnit.SECONDS);
        try {
          Thread.sleep(20000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

  public static HttpClient getHttpClient() {
    return httpClient;
  }

  public static void closeDemon(){
    demonConnManageThread.shutdown();
  }
}
