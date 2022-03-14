package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class SignaturePadActivity extends AppCompatActivity {

    private SignaturePad signaturePad;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);

        signaturePad = findViewById(R.id.signature_pad);
        Button clear_text = findViewById(R.id.clear_text);
        Button save = findViewById(R.id.save);

        findViewById(R.id.back).setOnClickListener(view -> {

            onBackPressed();

        });

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                i++; // called when users signied
            }

            @Override
            public void onClear() {

                i=0;

            }
        });

        save.setOnClickListener(view -> {

            if (i > 5) {
                runTimePermission();
            } else {
                Toast.makeText(this, "Please do a valid signed.", Toast.LENGTH_SHORT).show();
            }

        });

        clear_text.setOnClickListener(view -> {

            signaturePad.clear();

        });

    }

    public void runTimePermission() {

        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide storage permission for better reach ......";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {

                Uri uri = bitmapToFile(signaturePad.getSignatureBitmap());
                String url = uri.toString();
                File file = new File(url);
                Intent intent = getIntent().putExtra("data", file.getAbsolutePath());
                setResult(RESULT_OK,intent);
                finish();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });

    }

    // Method to save an bitmap to a file

    private Uri bitmapToFile(Bitmap bitmap){

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Images",Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID()+".jpg");

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (IOException e){

           e.printStackTrace();

        }

        return Uri.parse(file.getAbsolutePath());

    }

}