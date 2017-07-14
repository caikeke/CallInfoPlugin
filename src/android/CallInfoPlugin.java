package com.call.yhck;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CallInfoPlugin extends CordovaPlugin {
  private CallbackContext mCallbackContext;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.mCallbackContext = callbackContext;
    if (!"".equals(action) || action != null) {
      getCallInfo();
      return true;
    }
    mCallbackContext.error("error");
    return false;
  }

  //获取通话记录
  private void getCallInfo() {
    List<CallInfo> infos = new ArrayList<CallInfo>();
    ContentResolver resolver = cordova.getActivity().getContentResolver();
    // uri的写法需要查看源码JB\packages\providers\ContactsProvider\AndroidManifest.xml中内容提供者的授权
    // 从清单文件可知该提供者是CallLogProvider，且通话记录相关操作被封装到了Calls类中
    Uri uri = CallLog.Calls.CONTENT_URI;
    String[] projection = new String[]{
      CallLog.Calls.NUMBER, // 号码
      CallLog.Calls.DATE,   // 日期
      CallLog.Calls.TYPE,   // 类型：来电、去电、未接
      CallLog.Calls.CACHED_NAME, //联系人
      CallLog.Calls.DURATION  //通话时长
    };
    if(isMarshmallow()){
      if (cordova.getActivity().checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        mCallbackContext.success("error");
        return;
      }
    }
    Cursor cursor = resolver.query(uri, projection, null, null, null);
    while (cursor.moveToNext()){
      String number = cursor.getString(0);
      long date = cursor.getLong(1);
      int type = cursor.getInt(2);
      String  name = cursor.getString(3);
      String durion = cursor.getString(4);
      Date currentDate = new Date();
      Calendar calendar = Calendar.getInstance(); //得到日历
      calendar.setTime(currentDate);//把当前时间赋给日历
      calendar.add(calendar.MONTH, -6);  //设置为前6月
      currentDate = calendar.getTime();//获取6个月前的时间
      if(date>=currentDate.getTime()){
        CallInfo item = new CallInfo();
        // 日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = format.format(date);
        item.setNumber(number);
        item.setDate(dateString);
        item.setType(type);
        item.setName(name);
        item.setDurtion(durion);
        infos.add(item);
      }
    }
    cursor.close();
    if(infos.size()>0){
      packagJSONData(infos);
    }else {
      mCallbackContext.success("");
    }
  }

private void packagJSONData(List<CallInfo> infos ){
  JSONArray mJSonArray = new JSONArray();
  for (CallInfo itemCall :infos){
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("number",itemCall.getNumber());
      jsonObject.put("date",itemCall.getDate());
      jsonObject.put("type",itemCall.getType());
      jsonObject.put("name",itemCall.getName());
      jsonObject.put("durtion",itemCall.getDurtion());
      mJSonArray.put(jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  mCallbackContext.success(mJSonArray);
}

  private static boolean isMarshmallow() {
    return Build.VERSION.SDK_INT >= 23;
  }
}
