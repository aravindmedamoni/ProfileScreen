package com.aravind.profilewithalertdialog;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    public ImageView profile_img;
    public Button profile_choosebtn;
    private static final int PICK_IMAGE=50;


    Uri image_URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_img=findViewById(R.id.profile_imageView);
        profile_choosebtn=findViewById(R.id.profile_choose_button);


       /* final Matrix matrix = profile_img.getImageMatrix();
        final float imageWidth = profile_img.getDrawable().getIntrinsicWidth();
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final float scaleRatio = screenWidth / imageWidth;
        matrix.postScale(scaleRatio, scaleRatio);
        profile_img.setImageMatrix(matrix);*/

        profile_choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            image_URI = data.getData();
            profile_img.setImageURI(image_URI);
            profile_img.setScaleType(ImageView.ScaleType.FIT_XY);

        }

    }
}
