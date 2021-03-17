package com.example.broadcastreceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SMSReceiver extends BroadcastReceiver {
    StringBuffer sb = new StringBuffer();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if(intentExtras!=null)
        {
            Object[] smsData = (Object[])intentExtras.get("pdus");

            for(int i = 0;i<smsData.length;i++)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])smsData[i]);
                sb.append("SENDER::"+smsMessage.getOriginatingAddress()+"\n");
                sb.append("MESSAGE::"+smsMessage.getMessageBody()+"\n");
            }
            Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG).show();
        }

        /*Channel for New Android above Oreo*/
        createNewChannel(context);

        /*Creating channel for older androids*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"SMSChannel");
        builder.setSmallIcon(R.drawable.ic_message_icon);
        builder.setContentText(sb.toString());

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(007,builder.build());
    }

    private void  createNewChannel(Context c)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("SMSChannel","TEST",NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager notificationManager = (NotificationManager)c.getSystemService(c.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

        }

    }
}
