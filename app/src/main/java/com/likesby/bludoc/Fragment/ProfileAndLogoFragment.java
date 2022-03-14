package com.likesby.bludoc.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.likesby.bludoc.DigitalClinicActivity;
import com.likesby.bludoc.DigitalClinicConfirmationActivity;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentClinicDetailskBinding;
import com.likesby.bludoc.databinding.FragmentProfileAndLogoBinding;
import com.likesby.bludoc.utils.PermissionUtil;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ProfileAndLogoFragment extends Fragment {

    private static final int PROFILE_ACTIVITY_CODE = 150;
    private static final int PROFILE_ACTIVITY_CODE_QR = 220;
    private FragmentProfileAndLogoBinding binding;
    private boolean imageSelectedFlag;
    private SessionManager manager;
    public final int GALLERY_ACTIVITY_CODE = 140;
    public static final int PERMISSIONS_PHOTO = 149;
    private FragmentActivity fragmentActivity;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private Uri mSourceUri;
    private Dialog dialog_data;
    CropImageView mCropView;
    private File imageUp;
    private MultipartBody.Part multipartBodyLogo;
    private MultipartBody.Part multipartBodyProfile;
    private RectF mFrameRect;
    private MultipartBody.Part multipartBody;
    private String Document_img1;
    private String keywords = "";
    private MultipartBody.Part multipartBodyQr;
    private AppointmentModel appointment;
    private boolean isSave;
    private String isFirstTimeDigital;
    private boolean isFirstTime=true;

    public ProfileAndLogoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentProfileAndLogoBinding.inflate(inflater, container, false);

        manager = new SessionManager();

        isFirstTimeDigital = manager.getPreferences(fragmentActivity, "isFirstTimeDigitalLogo");

        if(TextUtils.isEmpty(isFirstTimeDigital)){

            isFirstTime = false;

        }

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if (!(manager.getPreferences(fragmentActivity.getApplicationContext(), "image").equalsIgnoreCase(""))) {

            Picasso.with(fragmentActivity.getApplicationContext()).
                    load(manager.getPreferences(fragmentActivity.getApplicationContext(), "image"))
                    .into(binding.logoImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            binding.imageText.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            binding.imageText.setVisibility(View.VISIBLE);
                        }
                    });

        }

        setAppointmentDataOnProfile();

        binding.logoImage.setOnClickListener(v -> {

            PermissionUtil permissionUtil = new PermissionUtil();
            if (permissionUtil.checkMarshMellowPermission()) {
                if (permissionUtil.verifyPermissions(fragmentActivity, permissionUtil.getGalleryPermissions()))
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_ACTIVITY_CODE);
                else {
                    ActivityCompat.requestPermissions(fragmentActivity, permissionUtil.getGalleryPermissions(), PERMISSIONS_PHOTO);
                }
            }

            /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
        });

        binding.doctorImage.setOnClickListener(v -> {

            PermissionUtil permissionUtil = new PermissionUtil();
            if (permissionUtil.checkMarshMellowPermission()) {
                if (permissionUtil.verifyPermissions(fragmentActivity, permissionUtil.getGalleryPermissions()))
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), PROFILE_ACTIVITY_CODE);
                else {
                    ActivityCompat.requestPermissions(fragmentActivity, permissionUtil.getGalleryPermissions(), PERMISSIONS_PHOTO);
                }
            }

            /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
        });

        binding.qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil permissionUtil = new PermissionUtil();

                if (permissionUtil.checkMarshMellowPermission()) {
                    if (permissionUtil.verifyPermissions(fragmentActivity, permissionUtil.getGalleryPermissions()))
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), PROFILE_ACTIVITY_CODE_QR);
                    else {
                        ActivityCompat.requestPermissions(fragmentActivity, permissionUtil.getGalleryPermissions(), PERMISSIONS_PHOTO);
                    }
                }

                /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
            }
        });

        binding.submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

                if (appointment != null) {
                    uploadProfileAndLogoDetails();
                    manager.setPreferences(fragmentActivity, "isFirstTimeDigitalLogo","true");
                } else {
                    Toast.makeText(fragmentActivity, "First, save the first tab of Doctor Details.", Toast.LENGTH_SHORT).show();
                    ((DigitalClinicActivity) fragmentActivity).moveViewPager(0);
                }
            }
        });

        binding.save.setOnClickListener(v -> {

            isSave=true;

            appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

            if (appointment != null) {
                uploadProfileAndLogoDetails();
            } else {
                Toast.makeText(fragmentActivity, "First, save the first tab of Doctor Details.", Toast.LENGTH_SHORT).show();
                ((DigitalClinicActivity) fragmentActivity).moveViewPager(0);
            }

        });

        binding.removeImageDoctor.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(fragmentActivity, R.style.AlertDialog));
            builder.setMessage("Do you wish to delete the Profile Image?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteImage(appointment.getApp_id().toString().trim(),"profile_image");

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        });
        binding.removeImage.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(fragmentActivity, R.style.AlertDialog));
            builder.setMessage("Do you wish to delete the Clinic / Hospital Logo Image?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteImage(appointment.getApp_id().toString().trim(),"clinic_logo");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
        binding.qrCodeRemove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(fragmentActivity, R.style.AlertDialog));
            builder.setMessage("Do you wish to delete the QR Code Image?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteImage(appointment.getApp_id().toString().trim(),"qr_code");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        return binding.getRoot();
    }

    public void deleteImage(String AptId, String type) {

        if (Utils.isConnectingToInternet(fragmentActivity)) {

            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponseSuccess> call = request.deleteApptImage(AptId,type);

            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {
                    ResponseSuccess jsonResponse = response.body();

                    if (jsonResponse != null && jsonResponse.getSuccess().equalsIgnoreCase("Success")) {
                        Toast.makeText(fragmentActivity, "Image Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                        if(type.equalsIgnoreCase("profile_image")){
                            multipartBodyProfile  = null;
                            appointment.setProfile_image("");
                            binding.doctorImage.setImageResource(0);
                            binding.imageTextProfile.setVisibility(View.VISIBLE);
                        }else if(type.equalsIgnoreCase("clinic_logo")){
                            multipartBodyLogo = null;
                            appointment.setClinic_logo("");
                            binding.logoImage.setImageResource(0);
                            binding.imageText.setVisibility(View.VISIBLE);
                        }else if(type.equalsIgnoreCase("qr_code")){
                            multipartBodyQr = null;
                            appointment.setQr_code("");
                            binding.qrCode.setImageResource(0);
                            binding.imageTextQrCode.setVisibility(View.VISIBLE);
                        }
                      //  setAppointmentDataOnProfile();
                    } else if (jsonResponse != null) {
                        Toast.makeText(fragmentActivity, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {

                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAppointmentDataOnProfile() {

        if (appointment != null) {

            if(isFirstTime){
                binding.save.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(appointment.getProfile_image())) {
                Picasso.with(fragmentActivity.getApplicationContext()).
                    load(appointment.getProfile_image())
                    .into(binding.doctorImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            binding.imageTextProfile.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            binding.imageTextProfile.setVisibility(View.VISIBLE);
                        }
                    });
            }

            if (!TextUtils.isEmpty(appointment.getClinic_logo())) {

                Picasso.with(fragmentActivity.getApplicationContext()).
                    load(appointment.getClinic_logo())
                    .into(binding.logoImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            binding.imageText.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            binding.imageText.setVisibility(View.VISIBLE);
                        }
                    });
            }

            if (!TextUtils.isEmpty(appointment.getQr_code())) {

                Picasso.with(fragmentActivity.getApplicationContext()).
                        load(appointment.getQr_code())
                        .into(binding.qrCode, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                binding.imageTextQrCode.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                binding.imageTextQrCode.setVisibility(View.VISIBLE);
                            }
                        });

            }

        }

    }

    public void uploadProfileAndLogoDetails() {

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if (Utils.isConnectingToInternet(fragmentActivity.getApplicationContext())) {

            binding.progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            List<MultipartBody.Part> multipartList = new ArrayList<>();

            if (multipartBodyProfile != null)
                multipartList.add(multipartBodyProfile);

            if (multipartBodyLogo != null)
                multipartList.add(multipartBodyLogo);

            if (multipartBodyQr != null)
                multipartList.add(multipartBodyQr);

            RequestBody doctor_id = RequestBody.create(MediaType.parse("text/plain"), manager.getPreferences(fragmentActivity, "doctor_id"));

            RequestBody doctor_name_details = RequestBody.create(MediaType.parse("text/plain"), appointment.getDoc_name());
            RequestBody registration_number = RequestBody.create(MediaType.parse("text/plain"),appointment.getReg_no());

            Call<AppointmentModel> call = request.addProfileAndImage(
                    doctor_id,
                    doctor_name_details,
                    registration_number,
                    multipartList
            );

            call.enqueue(new Callback<AppointmentModel>() {
                @Override
                public void onResponse(@NonNull Call<AppointmentModel> call, @NonNull retrofit2.Response<AppointmentModel> response) {
                    AppointmentModel jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                        binding.progressBar.setVisibility(View.GONE);

                        manager.setObjectAppointmentDetails(fragmentActivity.getApplicationContext(),"appointment",jsonResponse);

                        if(isSave){
                            startActivity(new Intent(fragmentActivity, DigitalClinicConfirmationActivity.class));
                            isSave=false;
                            fragmentActivity.finish();

                        }

                        ((DigitalClinicActivity) fragmentActivity).moveViewPager(3);


                    } else {

                        Toast.makeText(fragmentActivity, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AppointmentModel> call, @NonNull Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mSourceUri = data.getData();
                keywords = "logo";
                popup(mSourceUri);
            }

        } else if (requestCode == PROFILE_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mSourceUri = data.getData();
                keywords = "profile";
                popup(mSourceUri);
            }
        } else if (requestCode == PROFILE_ACTIVITY_CODE_QR) {
            if (resultCode == Activity.RESULT_OK) {

                mSourceUri = data.getData();
                keywords = "qr";
                popup(mSourceUri);

            }
        }

    }

    private void popup(Uri mSourceUri) {
        dialog_data = new Dialog(fragmentActivity);
        dialog_data.setCancelable(false);
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_crop_image);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_crop_image = dialog_data.findViewById(R.id.btn_crop_image);
        ImageView btn_cancel = dialog_data.findViewById(R.id.btn_cancel);

        mCropView = dialog_data.findViewById(R.id.cropImageView);
        mCropView.setSaveEnabled(true);
        mCropView.setCompressQuality(30);
        mCropView.setCompressFormat(Bitmap.CompressFormat.JPEG);
        mCropView.setSaveFromParentEnabled(true);
        mCropView.setCropMode(CropImageView.CropMode.CUSTOM);
        mCropView.setBackgroundColor(0xFFFFFFFB);
        mCropView.setOverlayColor(0xAA1C1C1C);
        mCropView.setFrameColor(getResources().getColor(R.color.frame));
        mCropView.setHandleColor(getResources().getColor(R.color.handle));
        mCropView.setGuideColor(getResources().getColor(R.color.guide));
        mCropView.load(mSourceUri)
                .initialFrameRect(mFrameRect)
                .useThumbnail(true)
                .execute(mLoadCallback);

        btn_crop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cropImage();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        dialog_data.show();
    }

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            // Toast.makeText(mContext, "mLoadCallback", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(Throwable e) {
        }
    };

    public void cropImage() {
        mCropView.crop(mSourceUri).execute(mCropCallback);
    }

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            mCropView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    public Uri createSaveUri() {
        return createNewUri(fragmentActivity, mCompressFormat);
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {


        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Logger.i("SaveUri = " + uri);
        return uri;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        Logger.i("getMimeType CompressFormat = " + format);
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/bludoc_cropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {

            Uri selectedImage = outputUri;
            String ss = getFileName(selectedImage);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(fragmentActivity.getContentResolver(), selectedImage);
                bitmap = getResizedBitmap(bitmap, 400);
                //  imgPerson.setImageBitmap(bitmap);

                BitMapToString(bitmap);
                File f = new File(fragmentActivity.getCacheDir(), ss);
                f.createNewFile();

//Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageUp = new File(fragmentActivity.getCacheDir(), ss);
                OutputStream outFile = null;

                try {
                    outFile = new FileOutputStream(imageUp);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                    if (keywords.equals("logo")) {

                        Glide.with(fragmentActivity)
                                .load(bitmap)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(binding.logoImage);

                        binding.imageText.setVisibility(View.GONE);

                        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                        multipartBodyLogo = MultipartBody.Part.createFormData("clinic_logo", imageUp.getName(), fbody);

                    } else if (keywords.equals("profile")) {

                        Glide.with(fragmentActivity)
                                .load(bitmap)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(binding.doctorImage);

                        binding.imageTextProfile.setVisibility(View.GONE);

                        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                        multipartBodyProfile = MultipartBody.Part.createFormData("profile_image", imageUp.getName(), fbody);

                    } else {

                        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                        multipartBodyQr = MultipartBody.Part.createFormData("qr_code", imageUp.getName(), fbody);

                        Glide.with(fragmentActivity)
                                .load(bitmap)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(binding.qrCode);

                    }

                    imageSelectedFlag = true;
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            dialog_data.dismiss();
        }

        @Override
        public void onError(Throwable e) {
            dialog_data.dismiss();
        }
    };


    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = fragmentActivity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}