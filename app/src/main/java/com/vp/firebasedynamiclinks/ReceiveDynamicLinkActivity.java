package com.vp.firebasedynamiclinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * Created by Vishwanath Patil on 2020-06-16.
 */
public class ReceiveDynamicLinkActivity extends AppCompatActivity {
//https://www.blueappsoftware.com/referral-program-using-firebase-dynamic-link-android-tutorial/
    private static final String TAG = "ReceiveDynamicLinkActiv";



    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ReceiveDynamicLinkActivity.this,CreateDynamicLinkActivity.class));
//            }
//        });


        Button b = (Button)findViewById(R.id.click);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiveDynamicLinkActivity.this,CreateDynamicLinkActivity.class));
            }
        });


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.v(TAG,deepLink.toString());
                            Log.v(TAG,deepLink.getQueryParameter("channelId")+" " );
                            textView.setText("channelId " + deepLink.getQueryParameter("channelId"));
                            //Toast.makeText(ReceiveDynamicLinkActivity.this,"channelId"+deepLink.getQueryParameter("channelId"),Toast.LENGTH_LONG).show();


                        }




                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });


    }
}
