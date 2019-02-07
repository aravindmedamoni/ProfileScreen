package com.aravind.profilewithalertdialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {



    private static final int PICK_IMAGE_REQUEST = 0;
    private final String TAG = "Main Activity";
    private ImageView mImage;
    private Uri mImageUri;
    private Button choosePhoto;
  //  private Button reset;
    private Dialog MyDialog;
    LinearLayout chooseImage,ResetDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImage = findViewById(R.id.profile_imageView);

        choosePhoto = findViewById(R.id.profile_choose_button);
        choosePhoto.setOnClickListener(this);
       /* reset = findViewById(R.id.lbl_btn_reset);
        reset.setOnClickListener(this);*/


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mImageUri = preferences.getString("image", null);

        if (mImageUri != null) {
            mImage.setImageURI(Uri.parse(mImageUri));
            // Toast.makeText(this, "saved image set", Toast.LENGTH_SHORT).show();
        } else {
            mImage.setImageResource(R.drawable.profile_icon1);
            // Toast.makeText(this, "default image set", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Select an image
     */
    public void imageSelect() {
        permissionsCheck();

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null) {
                // Make sure the request was successful
                //if (resultCode == RESULT_OK) {
                // The user picked a image.
                // The Intent's data Uri identifies which item was selected.
                //   if (data != null) {

                // This is the key line item, URI specifies the name of the data
                mImageUri = data.getData();

                // Saves image URI as string to Default Shared Preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("image", String.valueOf(mImageUri));
                editor.commit();

                // Sets the ImageView with the Image URI
                mImage.setImageURI(mImageUri);
                mImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImage.setScaleType(ImageView.ScaleType.FIT_XY);
                mImage.invalidate();
                // }
                // }
            }else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * Clear Default Shared Preferences
     */
    public void clearData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();
        startActivity(getIntent());
    }

    /**
     * to handle the buttons
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_choose_button:
                // get image
               // imageSelect();
                myCustomAlertDialog();
                break;
           /* case R.id.lbl_btn_reset:
                // increase by 1
               // clearData();
                break;*/
            default:
                break;
        }
    }

    public void myCustomAlertDialog() {
        MyDialog = new Dialog(ProfileActivity.this);
        MyDialog.setContentView(R.layout.custuomdialog);

        chooseImage=MyDialog.findViewById(R.id.LL_choose_Image);
        ResetDefault=MyDialog.findViewById(R.id.LL_reset_Image);


        //  chooseImage = findViewById(R.id.LL_choose_Image);
      //  ResetDefalut = findViewById(R.id.LL_reset_Image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
                imageSelect();
            }
        });

        ResetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
        MyDialog.show();

    }

}




