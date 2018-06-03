package net.fenv.smsfwd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;




public class SmsReceiver extends BroadcastReceiver {
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    static final String TAG = "SmsReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)){
            //Log.i(TAG, "收到短信");

            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            Object[] smsObj = (Object[]) bundle.get("pdus");

            String format = intent.getStringExtra("format");
            String message = "";
            String number = "";
            for (Object sms : smsObj) {
                if(Build.VERSION.SDK_INT>=23){
                    msg = SmsMessage.createFromPdu((byte[]) sms, format);
                }else{
                    msg = SmsMessage.createFromPdu((byte[]) sms);
                }
                number = msg.getOriginatingAddress();
                message += msg.getDisplayMessageBody();

            }
            Deal(context, message, number);
        }
    }

    public void transmitMessageTo(String phoneNumber,String message){//转发短信
        SmsManager manager = SmsManager.getDefault();
        /** 切分短信，每七十个汉字切一个，短信长度限制不足七十就只有一个：返回的是字符串的List集合*/
        ArrayList<String> texts =manager.divideMessage(message);//这个必须有
        manager.sendMultipartTextMessage(phoneNumber,null, texts, null,null);
    }
    public void Deal(Context context, String message, String number){
        String confstring = MainActivity.getSettingNote(context,"configstring");
        String[] lines = confstring.split("\n");
        for (String line:lines) {
            String[] parts = line.split("\\|");
            if( isMatch(number, message, parts[0])){
                String[] mobiles = parts[1].split(",");
                for (String mobile: mobiles ) {
                    transmitMessageTo(mobile, message);
                }
            }
        }
    }
    private boolean isMatch(String number, String message, String keyword){
        if(keyword.startsWith("-")){
            keyword = keyword.substring(1);
            return message.indexOf(keyword)>=0;
        }else{
            return number.equals(keyword);
        }
    }
}
