package com.example.android.todolistgh;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ed on 27/11/15.
 */
public class Connectivity {

    Context context;

    public Connectivity(Context context){
        this.context = context;
    }

    public void sendToEmail(String memo) {
        Intent sendEmailSummary = new Intent(Intent.ACTION_SENDTO);

        sendEmailSummary.setData(Uri.parse("mailto:")); // only email apps should handle this
        sendEmailSummary.putExtra(Intent.EXTRA_EMAIL, new String[] { "example@gmail.com" });
        sendEmailSummary.putExtra(Intent.EXTRA_SUBJECT, ("MEMO- To-Do List App"));
        sendEmailSummary.putExtra(Intent.EXTRA_TEXT, memo);
        if (sendEmailSummary.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(sendEmailSummary);
        }
    }
}