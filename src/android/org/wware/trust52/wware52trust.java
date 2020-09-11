package org.wware.trust52;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.provider.Settings;

import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.EnvType;
import cn.org.bjca.sdk.core.kit.YWXListener;
import cn.org.bjca.sdk.core.values.EnvCheck;


public class wware52trust extends CordovaPlugin {
        public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
          if(action.equals("coolMethod")) {
                  String messag = args.getString(0);
                  this.coolMethod(messag, callbackContext);
                  return true;
          }
          if(action.equals("testing")) {
                  String message = args.getString(0);
                  this.testing(message, callbackContext);
                  return true;
          }
          if(action.equals("getCertinfo")) {
                  JSONObject message = args.getJSONObject(0);
                  this.getCertinfo(message, callbackContext);
                  return true;
          }
          if(action.equals("setServerEnv")) {
                  JSONObject message = args.getJSONObject(0);
                  this.setServerEnv(message, callbackContext);
                  return true;
          }
          if(action.equals("certDownload")){
            JSONObject message = args.getJSONObject(0);
            this.certDownload(message, callbackContext);
            return true;
          }
                return false;
        }
        private final static String FILE_DEMO = "trust51settingFile";
//this.cordova.getActivity().getBaseContext()  use getBaseContext or getApplicationContext is not sure
        // blows cause an error
        //private Context currentContext = this.cordova.getActivity().getApplicationContext();
        //private Activity currentActivity = this.cordova.getActivity();

        private SharedPreferences getSharedPreferences() {
                Context mContext = this.cordova.getActivity().getApplicationContext();;
                SharedPreferences sp = mContext.getSharedPreferences(FILE_DEMO, Context.MODE_PRIVATE);
                return sp;
        }
        private SharedPreferences.Editor getEditor() {
                SharedPreferences sp = getSharedPreferences();
                SharedPreferences.Editor editor = sp.edit();
                return editor;

        }
        public void set(String key, String value) {
                SharedPreferences.Editor editor = getEditor();
                editor.putString(key, value);
                editor.commit();
        }
        private void coolMethod(String message, CallbackContext callbackContext){
          if(message != null && message.length()>0){
              callbackContext.success("调用成功："+message);
          }else{
              callbackContext.error("调用失败：无有效参数");
          }

        }
        private void testing(String message, CallbackContext callbackContext){
                String version= BJCASDK.getInstance().getVersion();
                if(message != null && message.length()>0){
                      callbackContext.success("调用成功："+message+version);
                  }else{
                      callbackContext.error("调用失败：无有效参数");
                  }
        }
        private void getCertinfo(JSONObject message, CallbackContext callbackContext) throws JSONException {

                boolean existsCert = BJCASDK.getInstance().existsCert(this.cordova.getActivity().getBaseContext());
                EnvType envType = BJCASDK.getInstance().getServerEnvType();
                JSONObject value = new JSONObject();
                if(!existsCert) {
                        value.put("success",true);
                        value.put("existsCert",false);
                        value.put("message","without cert");
                        value.put("envType",envType);

                        callbackContext.success(value);
                }else{
                        String clientId = message.getString("clientId");
                        YWXListener listener = new YWXListener(){
                                public void callback(String message){
                                        //用户信息返回
                                        callbackContext.success(message);
                                }
                        };
                        BJCASDK.getInstance().getUserInfo(this.cordova.getActivity().getBaseContext(),clientId,listener);

                }
        }
        private void setServerEnv(JSONObject message, CallbackContext callbackContext){
                EnvType setType = EnvType.PUBLIC;
                EnvType envType = BJCASDK.getInstance().getServerEnvType();
                Context context = this.cordova.getActivity().getApplicationContext();;
                if(message.has("isDebug")) {
                        setType = EnvType.INTEGRATE;
                        if(EnvCheck.getUrlByEnvType(envType) !=  EnvCheck.getUrlByEnvType(setType) ) {
                                BJCASDK.getInstance().setServerUrl(EnvType.INTEGRATE);
                                this.set("envType",setType.toString());
                                //doColdRestart(context);
                                callbackContext.success(message);
                        }
                }else{
                        if(EnvCheck.getUrlByEnvType(envType) !=  EnvCheck.getUrlByEnvType(setType) ) {
                                BJCASDK.getInstance().setServerUrl(EnvType.PUBLIC);
                                this.set("envType",setType.toString());
                                //doColdRestart(context);
                                callbackContext.success(message);
                        }
                }

        }
        private void certDownload(JSONObject message, CallbackContext callbackContext)throws JSONException {
                String clientId = message.getString("clientId");
                String phone = message.getString("phone");
                //Activity activity

                BJCASDK.getInstance().certDown(this.cordova.getActivity(),clientId,phone,new YWXListener(){
                        @Override
                        public void callback(String msg){
                                callbackContext.success(msg);
                        }
                });
        }

}
