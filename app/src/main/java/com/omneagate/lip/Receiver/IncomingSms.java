package com.omneagate.lip.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(final Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        Log.d("sms get", "sms Get receiver");

        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String result_ = message.substring(message.lastIndexOf("otp:") + 4);
                    String[] separeted = result_.split(" ,");
                    String result = separeted[0];
                    result = result.replace(". Valid upto 30 mins after receiving", "");
                    Log.d("otp_code ", "" + result);
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    if (senderNum.equalsIgnoreCase("DZ-NOTIFY") || senderNum.equalsIgnoreCase("DZ-JOYCHT") || senderNum.equalsIgnoreCase("BW-JOYCHT")) {
                        Intent in = new Intent("SmsMessage.intent.MAIN").putExtra("otp_pass", result);
                        context.sendBroadcast(in);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}