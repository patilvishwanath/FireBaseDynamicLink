package com.vp.firebasedynamiclinks;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * Created by Vishwanath Patil on 2020-06-16.
 */
public class CreateDynamicLinkActivity extends AppCompatActivity {

    private static final String TAG = "CreateDynamicLink";
    Button generateLink,shareLink;
    Uri shortLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic_link);


        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                });

        generateLink = (Button)findViewById(R.id.btn_generate_link);
        shareLink = (Button)findViewById(R.id.btn_share_link);


        generateLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  createDynamicUrl();

                createManualUrl();

            }
        });


        shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(shortLink!=null) {
                    shareDynamicUrl(shortLink.toString());
                }else {
                    Toast.makeText(CreateDynamicLinkActivity.this,"Please generate link first",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void createManualUrl() {

        String code = "1234";

        //create uri to fetch refcode value in SplashActivity.
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("firebasedynamicurl.page.link")
                .appendQueryParameter("channelId", code);

        String myUrl = builder.build().toString();




      String s =  "https://firebasedynamicurl.page.link/?" +
              "link="+myUrl+
              "&apn=" + getPackageName()+
              "&ibi=com.example.ios" +
              "&isi=1234567" +
              "&ius=exampleapp"+
              "&st=FireBaseDynamicLinksApp"+
              "&sd=Hey check this awosme app"+
              "&si=https://pm1.narvii.com/6461/186d8eb32d770687504e10eaa51d3f2d9dade3de_128.jpg";

      shortenDynamicUrl(null,s);
    }


    private void createDynamicUrl() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.google.com/"))
                .setDomainUriPrefix("https://firebasedynamicurl.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.v(TAG,dynamicLinkUri.toString());


        shortenDynamicUrl(dynamicLinkUri,null);
    }



    private void shortenDynamicUrl(Uri longUrl,String s) {

        FirebaseDynamicLinks.getInstance().createDynamicLink()
              //  .setLongLink(longUrl)
                .setLongLink(Uri.parse(s))  // manually
                .buildShortDynamicLink()
                // Set parameters
                // ...
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.v(TAG,"shortLink"+shortLink.toString());
                            Log.v(TAG,"flowchartLink"+flowchartLink.toString());
                            Toast.makeText(CreateDynamicLinkActivity.this,"Link Generated Successfully!!",Toast.LENGTH_LONG).show();

                        } else {
                            // Error
                            // ...
                            Log.e(TAG,task.getException()+"");
                        }
                    }
                });
    }

    private void shareDynamicUrl(String s) {

//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
//
//                //Environment.getExternalStorageDirectory()+"/ERS/4303117840.png";
//        OutputStream out = null;
//        File file=new File(path);
//       // /sdcard/ERS/4303117840.png
//        try {
//            out = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        path=file.getPath();
//        Uri bmpUri = Uri.parse("file://"+path);

        //

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
       // sendIntent.putExtra(Intent.EXTRA_STREAM,bmpUri);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey there please find my link to download the app  "+s);

        //sendIntent.setData(shortLink);
       sendIntent.setType("text/plain");
      //  sendIntent.setType("image/*");
      //  sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        startActivity(Intent.createChooser(sendIntent, "Share both text and image"));
    }
}
