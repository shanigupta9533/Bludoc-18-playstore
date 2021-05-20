package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.likesby.bludoc.Adapter.UGAdapter;
import com.likesby.bludoc.ModelLayer.Entities.DesignationItem;
import com.likesby.bludoc.ModelLayer.Entities.PGItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseAddUG;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePG;
import com.likesby.bludoc.ModelLayer.Entities.ResponseRegister;
import com.likesby.bludoc.ModelLayer.Entities.ResponseUG;
import com.likesby.bludoc.ModelLayer.Entities.UGItem;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.utils.FileUtil;
import com.likesby.bludoc.utils.PermissionUtil;
import com.likesby.bludoc.utils.ScreenSize;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
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
import java.util.Objects;
import java.util.regex.Pattern;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RegisterDetails extends AppCompatActivity {
    com.lamudi.phonefield.PhoneEditText inputMobileNumber_login;
    //SignaturePad mSignaturePad;
    FrameLayout fl_email;
    int count;
    LinearLayout first,second,third,fourth,fifth;
    Button savee_1,save_2,save_3,save_4,save_5;
    EditText et_name,et_email_id,et_registration_no,et_designation,et_pg_qualificaation,
            et_add_qualification,et_alt_no,et_alt_mail,et_working_days,et_visit_from,et_visit_to,
            et_closed_day,et_Clinic_name,et_Hos_address,et_visit_to1,et_visit_to2,et_visit_from1,et_visit_from2;
    boolean logo_flag= false;
    TextView et_mobile;
    SessionManager manager;
    Context mContext;
    private static final String TAG = "RegisterDetails_____";
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();

    Spinner pg_spinner,ug_spinner,  working_days_spinner,visit_hrs_from_spinner,visit_hrs_to_spinner,closed_day_spinner,visit_hrs_from_spinner2,visit_hrs_to_spinner2;
    ArrayAdapter<String> pg_arrayadp,ug_arrayadp,
                     working_days_arrayadp,visit_hrs_from_arrayadp,visit_hrs_to_arrayadp,closed_day_arrayadp,visit_hrs_from_arrayadp2,visit_hrs_to_arrayadp2;

    ArrayList<String> pg_list,ug_list,  working_days_list,visit_hrs_from_list,visit_hrs_to_list,visit_hrs_from_list2,visit_hrs_to_list2,closed_day_list;

    ArrayList<DesignationItem> designationItemArrayList;
//    ArrayList<String> working_days_Arraylist,visit_hrs_from_Arraylist,
//            visit_hrs_to_Arraylist,closed_day_Arraylist;;
    ArrayList<PGItem> pgItemArrayList;
    ArrayList<UGItem> ugItemArrayList;
    ArrayList<DesignationItem> specialitiesItemArrayList;
    String designation_id="",pg_id="",ug_id= "",specialities_id ="", working_days="",
            visit_hrs_from="",visit_hrs_to="", visit_hrs_from2="",visit_hrs_to2="",closed_day="",vst_frm="",vst_to="";
    private InterstitialAd mInterstitialAd;


    boolean imageSelectedFlag = false;
    private String Document_img1="";
    public static final int GET_FROM_GALLERY = 3;
    public  final int GALLERY_ACTIVITY_CODE = 140;
    public static final int PERMISSIONS_PHOTO = 149;
    //------------------------------------------------------------------------------------------
    public static ImageView image_layout;
    ImageView remove_image;
    ChoosePhoto choosePhoto=null;

    static Bitmap rotatedBitmap=null,bitmap=null;
    Uri global_uri;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_READ_PERMISSION = 123;
    private static final int SELECT_PICTURE = 23;

    final private int CAPTURE_IMAGE = 1;
    final private int CROP_PIC = 15;
    File globalFilePath = null;
    Boolean Camera_Gallery=false, profile_flag = false;
    File imageUp=null;
    public Uri photoURI=null;
    String imageFileName="";
    MultipartBody.Part multipartBody = null;
    //===========================================================================================
    MaterialDayPicker workingDayPicker;
    TextView one_tv,two_tv,three_tv,four_tv,five_tv;
    FrameLayout progressBar_main;
    Button btn_signature_add,btn_signatur_clear,btn_upload_photo;
    ImageView iv_signn,back;
    TextView image_text,tv_sign_will_appear_here;
    ToggleButton Mon,Tue,Wed,Thu,Fri,Sat,Sun;
    //----------------------------------------------------------------------------------------------
    String ug_name=null;
    CropImageView mCropView;
    private Uri mSourceUri = null;
    private RectF mFrameRect = null;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    Dialog dialog_data=null;
    //----------------------------------------------------------------------------------------------
    public static TextView textView__select_ug;
    UGAdapter ugAdapter;
    public static  String ug_name__="",ug_id__="";
    private AdView mAdView,mAdViewNative;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity);
        setContentView(R.layout.register_details);
        //hideSoftKeyboard(getActi);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));

        }
        mContext = RegisterDetails.this;
        manager = new SessionManager();
        initCalls();
        initViewHolder();

                 adRequest = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        mInterstitialAd = new InterstitialAd(mContext);
        //  mInterstitialAd.setAdUnitId("ca-app-pub-9225891557304181/3105393849");//Live
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//Test
        mInterstitialAd.loadAd(adRequest);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.

            }
        });

        if(manager.contains(mContext,"show_banner_ad"))
        BannerAd(adRequest);
        apiViewHolder.getUGs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseUGs);

        apiViewHolder.getPGs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePGs);

        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil permissionUtil = new PermissionUtil();

                if (permissionUtil.checkMarshMellowPermission()) {
                    if ( permissionUtil.verifyPermissions(mContext, permissionUtil.getGalleryPermissions()))
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_ACTIVITY_CODE);
                    else {
                        ActivityCompat.requestPermissions((Activity) mContext, permissionUtil.getGalleryPermissions(), PERMISSIONS_PHOTO);
                    }
                }

                        /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
            }
        });
    }




    private  void InterstitialAd( AdRequest adRequest){


        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            Intent intent1 = new Intent(RegisterDetails.this, HomeActivity.class);
            startActivity(intent1);
            finish();
        }
        }


    private  void BannerAd( AdRequest adRequest){
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }

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

        float bitmapRatio = (float)width / (float) height;
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
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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

    public static Uri createTempUri(Context context) {
        return Uri.fromFile(new File(context.getCacheDir(), "cropped"));
    }
    public Uri createSaveUri() {
        return createNewUri(mContext, mCompressFormat);
    }



    //==============================================================================================
    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override public void onSuccess() {
            // Toast.makeText(mContext, "mLoadCallback", Toast.LENGTH_SHORT).show();

        }

        @Override public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override public void onSuccess(Bitmap cropped) {
            mCropView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback);
        }

        @Override public void onError(Throwable e) {
        }
    };


    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override public void onSuccess(Uri outputUri) {

            Uri selectedImage = outputUri;
            String ss = getFileName(selectedImage);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), selectedImage);
                bitmap=getResizedBitmap(bitmap, 400);
                //  imgPerson.setImageBitmap(bitmap);

                Glide.with(mContext)
                        .load(bitmap)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(image_layout);
                BitMapToString(bitmap);
                image_text.setVisibility(View.GONE);
                File f = new File(mContext.getCacheDir(), ss);
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

                imageUp = new File(mContext.getCacheDir(), ss);
                OutputStream outFile = null;
                try {
                    outFile = new FileOutputStream(imageUp);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                    multipartBody = MultipartBody.Part.createFormData("image", imageUp.getName(),fbody);
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

        @Override public void onError(Throwable e) {
            dialog_data.dismiss();
        }
    };

    public void cropImage() {
        mCropView.crop(mSourceUri).execute(mCropCallback);
    }




    private  void popup(Uri mSourceUri)
    {
        dialog_data = new Dialog(mContext);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mSourceUri = data.getData();
                popup(mSourceUri);
            }
        }


//        //Detects request codes
//        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//            Uri selectedImage = data.getData();
//            String ss = getFileName(selectedImage);
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                bitmap=getResizedBitmap(bitmap, 400);
//                //  imgPerson.setImageBitmap(bitmap);
//
//                Glide.with(mContext)
//                        .load(bitmap)
//                        .apply(new RequestOptions()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                        .into(image_layout);
//                BitMapToString(bitmap);
//                image_text.setVisibility(View.GONE);
//                File f = new File(this.getCacheDir(), ss);
//                f.createNewFile();
//
////Convert bitmap to byte array
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
//                byte[] bitmapdata = bos.toByteArray();
//
////write the bytes in file
//                FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(f);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    fos.write(bitmapdata);
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                imageUp = new File(this.getCacheDir(), ss);
//                OutputStream outFile = null;
//                try {
//                    outFile = new FileOutputStream(imageUp);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//
//                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
//                    multipartBody = MultipartBody.Part.createFormData("image", imageUp.getName(),fbody);
//                    imageSelectedFlag = true;
//                    outFile.flush();
//                    outFile.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
        }

    private  void initCalls(){

        inputMobileNumber_login = findViewById(R.id.login_editTextInputMobile);
        //  inputMobileNumber_login.setHint(R.string.phone_hint);
        inputMobileNumber_login.setDefaultCountry("IN");

       // mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        first = (LinearLayout) findViewById(R.id.first);
        second= (LinearLayout) findViewById(R.id.second);
        third= (LinearLayout) findViewById(R.id.third);
        fourth= (LinearLayout) findViewById(R.id.fourth);
        fifth= (LinearLayout) findViewById(R.id.fifth);

        image_layout = findViewById(R.id.image_layout);
        back = findViewById(R.id.btn_back_edit_profile);
        savee_1 = (Button) findViewById(R.id.savee_1);
        save_2= (Button) findViewById(R.id.save_2);
        save_3= (Button) findViewById(R.id.save_3);
        save_4= (Button) findViewById(R.id.save_4);
        save_5= (Button) findViewById(R.id.save_5);
        progressBar_main = findViewById(R.id.progress_bar_main);
        progressBar_main.setVisibility(View.VISIBLE);
        one_tv  =  findViewById(R.id.one_tv);
        two_tv  =  findViewById(R.id.two_tv);
        three_tv  =  findViewById(R.id.three_tv);
        four_tv  =  findViewById(R.id.four_tv);
        five_tv  =  findViewById(R.id.five_tv);
        btn_signature_add = findViewById(R.id.btn_signature_add);
        iv_signn = findViewById(R.id.iv_signn);

        et_name     = findViewById(R.id.et_name);
        et_mobile     = findViewById(R.id.et_mobile);
        et_email_id     = findViewById(R.id.et_email_id);
        fl_email = findViewById(R.id.fl_email);
        fl_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEmail();
            }
        });
        et_registration_no     = findViewById(R.id.et_registration_no);
        et_designation     = findViewById(R.id.et_designation);
        et_pg_qualificaation     = findViewById(R.id.et_pg_qualificaation);
        et_add_qualification     = findViewById(R.id.et_add_qualification);
        et_alt_no     = findViewById(R.id.et_alt_no);
        et_alt_mail     = findViewById(R.id.et_alt_mail);
        et_working_days     = findViewById(R.id.et_working_days);
        et_visit_from     = findViewById(R.id.et_visit_from);
        et_visit_to     = findViewById(R.id.et_visit_to);
        et_visit_to1 = findViewById(R.id.et_visit_to1);
        et_visit_to2 = findViewById(R.id.et_visit_to21);
        et_visit_from1 = findViewById(R.id.et_visit_from1);
        et_visit_from2 = findViewById(R.id.et_visit_from21);
        et_closed_day     = findViewById(R.id.et_closed_day);
        et_Clinic_name     = findViewById(R.id.et_Clinic_name);
        et_Hos_address     = findViewById(R.id.et_Hos_address);
        remove_image =  findViewById(R.id.remove_image);
        ug_spinner     = findViewById(R.id.ug_spinner);
        pg_spinner     = findViewById(R.id.pg_spinner);

        working_days_spinner     = findViewById(R.id.working_days_spinner);
        visit_hrs_from_spinner     = findViewById(R.id.visiting_hrs_from_spinner);
        visit_hrs_from_spinner2     = findViewById(R.id.visiting_hrs_from_spinner2);
        visit_hrs_to_spinner     = findViewById(R.id.visiting_hrs_to_spinner);
        visit_hrs_to_spinner2     = findViewById(R.id.visiting_hrs_to_spinner2);
        closed_day_spinner     = findViewById(R.id.closed_day_spinner);

        workingDayPicker  = findViewById(R.id.working_days_picker);
        btn_signatur_clear  = findViewById(R.id.btn_signature_remove);
        btn_upload_photo  = findViewById(R.id.btn_upload_logo);
        tv_sign_will_appear_here = findViewById(R.id.tv_sign_will_appear_here);
        image_text= findViewById(R.id.image_text);
        Mon = (ToggleButton) findViewById(R.id.Mon);
        Tue = (ToggleButton) findViewById(R.id.Tue);
        Wed = (ToggleButton) findViewById(R.id.Wed);
        Thu = (ToggleButton) findViewById(R.id.Thu);
        Fri = (ToggleButton) findViewById(R.id.Fri);
        Sat = (ToggleButton) findViewById(R.id.Sat);
        Sun = (ToggleButton) findViewById(R.id.Sun);
        textView__select_ug =  findViewById(R.id.textView__select_ug);

        textView__select_ug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupUG();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                builder.setMessage("Do you wish to delete Logo?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            //manager.getPreferences(Registration_.this, "service_provider");
                            multipartBody = null;
                            imageUp = null;
                            image_layout.setImageResource(0);
                            image_text.setVisibility(View.VISIBLE);

                            apiViewHolder.DeleteLogo( manager.getPreferences(mContext,"doctor_id"))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responsedeleteLogo);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil permissionUtil = new PermissionUtil();

                if (permissionUtil.checkMarshMellowPermission()) {
                    if ( permissionUtil.verifyPermissions(mContext, permissionUtil.getGalleryPermissions()))
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_ACTIVITY_CODE);
                    else {
                        ActivityCompat.requestPermissions((Activity) mContext, permissionUtil.getGalleryPermissions(), PERMISSIONS_PHOTO);
                    }
                }

               /* startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
            }
        });

        btn_signatur_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                builder.setMessage("Do you wish to delete signature?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imageUp = null;
                                tv_sign_will_appear_here.setVisibility(View.VISIBLE);
                                manager.setPreferences(mContext,"signature","");

                                apiViewHolder.DeleteSignature( manager.getPreferences(mContext,"doctor_id"))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(responseDeleteSignature);

                                //manager.getPreferences(Registration_.this, "service_provider");
                                Picasso.with(mContext).
                                    load(R.drawable.blue_faint_btn_gradient2)
                                    .into(iv_signn, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            tv_sign_will_appear_here.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError() {
                                            tv_sign_will_appear_here.setVisibility(View.VISIBLE);
                                        }
                                    });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.GONE);
        third.setVisibility(View.GONE);
        fourth.setVisibility(View.GONE);
        fifth.setVisibility(View.GONE);
       /* String name_ = manager.getPreferences(mContext,"name");
        if(name_.contains("Dr."))
        {
            name_ = name_.replace("Dr.","");
            manager.setPreferences(mContext,"name",name_.trim());
        }*/
       if(manager.getPreferences(mContext,"name").equals(""))
        et_name.setText("Dr. "+manager.getPreferences(mContext,"name").trim());
       else
           et_name.setText(manager.getPreferences(mContext,"name").trim());

        //et_mobile.setText(manager.getPreferences(mContext,"mobile"));
        inputMobileNumber_login.setPhoneNumber(manager.getPreferences(mContext,"mobile"));
        et_email_id.setText(manager.getPreferences(mContext,"email"));



        one_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(manager.getPreferences(mContext,"name").equals(""))
                    et_name.setText("Dr. "+manager.getPreferences(mContext,"name").trim());
                else
                    et_name.setText(manager.getPreferences(mContext,"name").trim());
                //et_mobile.setText(manager.getPreferences(mContext,"mobile"));
                inputMobileNumber_login.setPhoneNumber(manager.getPreferences(mContext,"mobile"));

                et_email_id.setText(manager.getPreferences(mContext,"email"));

                TvSelected(one_tv);
                TvDeSelected(two_tv);
                TvDeSelected(three_tv);
                TvDeSelected(four_tv);
                TvDeSelected(five_tv);

                first.setVisibility(View.VISIBLE);
                second.setVisibility(View.GONE);
                third.setVisibility(View.GONE);
                fourth.setVisibility(View.GONE);
                fifth.setVisibility(View.GONE);
                hideKeyboard(mContext);
            }
        });


        two_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_registration_no.setText(manager.getPreferences(mContext,"registration_no"));
                et_add_qualification.setText(manager.getPreferences(mContext,"addtional_qualification"));
                ug_id = manager.getPreferences(mContext,"ug_id");
                pg_id = manager.getPreferences(mContext,"pg_id");
                et_designation.setText(manager.getPreferences(mContext,"designation_name"));

                if(ugItemArrayList!= null) {
                    for (int i = 0; i < ugItemArrayList.size(); i++) {
                        if (ugItemArrayList.get(i).getUgId().equalsIgnoreCase(ug_id)) {
                             ug_spinner.setSelection(i + 1);
                             ug_id__ = ugItemArrayList.get(i).getUgId();
                            textView__select_ug.setText(ugItemArrayList.get(i).getUgName());
                        }
                    }
                }
                if(pgItemArrayList!= null) {
                    for (int i = 0; i < pgItemArrayList.size(); i++) {
                        if (pgItemArrayList.get(i).getPgId().equalsIgnoreCase(pg_id)) {
                            pg_spinner.setSelection(i + 2);
                        }
                    }

                }
                TvSelected(two_tv);

                TvDeSelected(one_tv);
                TvDeSelected(three_tv);
                TvDeSelected(four_tv);
                TvDeSelected(five_tv);

                first.setVisibility(View.GONE);
                second.setVisibility(View.VISIBLE);
                third.setVisibility(View.GONE);
                fourth.setVisibility(View.GONE);
                fifth.setVisibility(View.GONE);
                hideKeyboard(mContext);
            }
        });

        three_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String a = manager.getPreferences(mContext, "mobile_letter_head");
                String b = manager.getPreferences(mContext, "email_letter_head");
                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "mobile_letter_head"))) {
                    et_alt_no.setText(manager.getPreferences(mContext, "mobile_letter_head"));
                } else {
                    et_alt_no.setText(manager.getPreferences(mContext, "mobile"));
                }
                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "email_letter_head"))) {
                    et_alt_mail.setText(manager.getPreferences(mContext, "email_letter_head"));
                } else {
                    et_alt_mail.setText(manager.getPreferences(mContext, "email"));
                }


                String[] visiting_hr_from_details = manager.getPreferences(mContext,"visiting_hr_from").split(Pattern.quote("|"));;
                String[] visiting_hr_to_details = manager.getPreferences(mContext,"visiting_hr_to").split(Pattern.quote("|"));;
                if(visiting_hr_from_details.length>1)
                {
                    et_visit_to1.setText(visiting_hr_to_details[0].toLowerCase());
                    et_visit_to2.setText(visiting_hr_to_details[1].toLowerCase());
                    et_visit_from1.setText(visiting_hr_from_details[0].toLowerCase());
                    et_visit_from2.setText(visiting_hr_from_details[1].toLowerCase());

                }
                else{
                    et_visit_from1.setText(visiting_hr_from_details[0].toLowerCase());
                    et_visit_to1.setText(visiting_hr_to_details[0].toLowerCase());
                   // visit_hrs_from = visiting_hr_from_details[0].toLowerCase();

                   // visit_hrs_to = visiting_hr_to_details[0].toLowerCase();
                }


                closed_day = manager.getPreferences(mContext, "close_day");
                working_days = manager.getPreferences(mContext, "working_days");


//                if(visiting_hr_from_details.length>1)
//                {
//                    for (int i = 0; i < visit_hrs_from_list.size(); i++) {
//                        if (visit_hrs_from_list.get(i).equalsIgnoreCase(visit_hrs_from)) {
//                            visit_hrs_from_spinner.setSelection(i);
//                        }
//                    }
//
//
//                    for (int i = 0; i < visit_hrs_to_list.size(); i++) {
//                        if (visit_hrs_to_list.get(i).equalsIgnoreCase(visit_hrs_to)) {
//                            visit_hrs_to_spinner.setSelection(i);
//                        }
//                    }
//
//                    for (int i = 0; i < visit_hrs_from_list2.size(); i++) {
//                        if (visit_hrs_from_list2.get(i).equalsIgnoreCase(visit_hrs_from2)) {
//                            visit_hrs_from_spinner2.setSelection(i);
//
//                        }
//                    }
//
//
//                    for (int i = 0; i < visit_hrs_to_list2.size(); i++) {
//                        if (visit_hrs_to_list2.get(i).equalsIgnoreCase(visit_hrs_to2)) {
//                            visit_hrs_to_spinner2.setSelection(i);
//                        }
//                    }
//
//                }
//                else {
//                    for (int i = 0; i < visit_hrs_from_list.size(); i++) {
//                        if (visit_hrs_from_list.get(i).equalsIgnoreCase(visit_hrs_from)) {
//                            visit_hrs_from_spinner.setSelection(i);
//                        }
//                    }
//
//
//                    for (int i = 0; i < visit_hrs_to_list.size(); i++) {
//                        if (visit_hrs_to_list.get(i).equalsIgnoreCase(visit_hrs_to)) {
//                            visit_hrs_to_spinner.setSelection(i);
//                        }
//                    }
//                }
//




                if (working_days != null){
                String[] work = working_days.split(",");

                    for (int k = 0; k< work.length ;k++){
                        if(work[k].equalsIgnoreCase("Mon")){
                            Mon.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Tue")){
                            Tue.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Wed")){
                            Wed.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Thu")){
                            Thu.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Fri")){
                            Fri.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Sat")){
                            Sat.setChecked(true);
                        }
                        if(work[k].equalsIgnoreCase("Sun")){
                            Sun.setChecked(true);
                        }

                    }

            }

//                closed_day.split(",");
//                String[] strArray = closed_day.split(",");
//                ArrayList<String> str = new ArrayList<>();
////                str.add("MONDAY");
////                str.add("TUESDAY");
////                str.add("WEDNESDAY");
////                str.add("THURSDAY");
////                str.add("FRIDAY");
////                str.add("SATURDAY");
////                str.add("SUNDAY");
//                String sss = "";
//                for(int k =0; k<strArray.length ; k++){
//                    str.add(strArray[k]);
//                    sss = sss +",MaterialDayPicker.Weekday."+strArray[k];
//                }
//                if(!sss.equalsIgnoreCase("")) {
//                    sss = sss.substring(1);
//                }
//               // workingDayPicker.setSelectedDays(sss);

                TvSelected(three_tv);
                TvDeSelected(two_tv);
                TvDeSelected(one_tv);
                TvDeSelected(four_tv);
                TvDeSelected(five_tv);

                first.setVisibility(View.GONE);
                second.setVisibility(View.GONE);
                third.setVisibility(View.VISIBLE);
                fourth.setVisibility(View.GONE);
                fifth.setVisibility(View.GONE);
                hideKeyboard(mContext);
//                if(count ==0){
//                    count =1;
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // Do something after 5s = 5000ms
//                            three_tv.performClick();
//                        }
//                    }, 500);
//
//
//                }

            }
        });


        four_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_Clinic_name.setText(manager.getPreferences(mContext,"clinic_name"));
                et_Hos_address.setText(manager.getPreferences(mContext,"clinic_address"));

                if(!(manager.getPreferences(mContext,"image").equalsIgnoreCase("")))
                {
                    Picasso.with(mContext).
                            load(manager.getPreferences(mContext,"image"))
                            //.resize(width_custom-100 , width_custom-100)
                            // .memoryPolicy(MemoryPolicy.)
                            // .centerCrop()

                            .into(image_layout, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    image_text.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    image_text.setVisibility(View.VISIBLE);
                                }
                            });

                }

                TvSelected(four_tv);

                TvDeSelected(two_tv);
                TvDeSelected(three_tv);
                TvDeSelected(one_tv);
                TvDeSelected(five_tv);

                first.setVisibility(View.GONE);
                second.setVisibility(View.GONE);
                third.setVisibility(View.GONE);
                fourth.setVisibility(View.VISIBLE);
                fifth.setVisibility(View.GONE);
                hideKeyboard(mContext);
            }
        });


        five_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(manager.getPreferences(mContext,"signature").equalsIgnoreCase("")))
                {
                    Picasso.with(mContext).
                            load(manager.getPreferences(mContext,"signature"))
                            //.resize(width_custom-100 , width_custom-100)
                            // .memoryPolicy(MemoryPolicy.)
                            // .centerCrop()

                            .into(iv_signn, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    tv_sign_will_appear_here.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    tv_sign_will_appear_here.setVisibility(View.VISIBLE);
                                }
                            });

                }


                TvSelected(five_tv);

                TvDeSelected(two_tv);
                TvDeSelected(three_tv);
                TvDeSelected(four_tv);
                TvDeSelected(one_tv);

                first.setVisibility(View.GONE);
                second.setVisibility(View.GONE);
                third.setVisibility(View.GONE);
                fourth.setVisibility(View.GONE);
                fifth.setVisibility(View.VISIBLE);
                hideKeyboard(mContext);
            }
        });


        visit_hrs_from_list = new ArrayList<>();
        visit_hrs_from_list.add("Select Start Time");
        visit_hrs_from_list.add("6 AM");
        visit_hrs_from_list.add("7 AM");
        visit_hrs_from_list.add("8 AM");
        visit_hrs_from_list.add("9 AM");
        visit_hrs_from_list.add("10 AM");
        visit_hrs_from_list.add("11 AM");
        visit_hrs_from_list.add("12 PM");
        visit_hrs_from_list.add("1 PM");
        visit_hrs_from_list.add("2 PM");
        visit_hrs_from_list.add("3 PM");
        visit_hrs_from_list.add("4 PM");
        visit_hrs_from_list.add("5 PM");
        visit_hrs_from_list.add("6 PM");

        visit_hrs_to_list = new ArrayList<>();
        visit_hrs_to_list.add("Select Start Time");
        visit_hrs_to_list.add("6 AM");
        visit_hrs_to_list.add("7 AM");
        visit_hrs_to_list.add("8 AM");
        visit_hrs_to_list.add("9 AM");
        visit_hrs_to_list.add("10 AM");
        visit_hrs_to_list.add("11 AM");
        visit_hrs_to_list.add("12 PM");
        visit_hrs_to_list.add("1 PM");
        visit_hrs_to_list.add("2 PM");
        visit_hrs_to_list.add("3 PM");
        visit_hrs_to_list.add("4 PM");
        visit_hrs_to_list.add("5 PM");
        visit_hrs_to_list.add("6 PM");
        visit_hrs_to_list.add("7 PM");
        visit_hrs_to_list.add("8 PM");
        visit_hrs_to_list.add("9 PM");
        visit_hrs_to_list.add("10 PM");
        visit_hrs_to_list.add("11 PM");
        visit_hrs_to_list.add("12 AM");

        visit_hrs_from_list2 = new ArrayList<>();
        visit_hrs_from_list2.add("Select Start Time");
        visit_hrs_from_list2.add("12 PM");
        visit_hrs_from_list2.add("1 PM");
        visit_hrs_from_list2.add("2 PM");
        visit_hrs_from_list2.add("3 PM");
        visit_hrs_from_list2.add("4 PM");
        visit_hrs_from_list2.add("5 PM");
        visit_hrs_from_list2.add("6 PM");
        visit_hrs_from_list2.add("7 PM");
        visit_hrs_from_list2.add("8 PM");
        visit_hrs_from_list2.add("9 PM");
        visit_hrs_from_list2.add("10 PM");
        visit_hrs_from_list2.add("11 PM");
        visit_hrs_from_list2.add("12 AM");
        visit_hrs_from_list2.add("1 AM");
        visit_hrs_from_list2.add("2 AM");
        visit_hrs_from_list2.add("3 AM");
        visit_hrs_from_list2.add("4 AM");
        visit_hrs_from_list2.add("5 AM");
        visit_hrs_from_list2.add("6 AM");

        visit_hrs_to_list2 = new ArrayList<>();
        visit_hrs_to_list2.add("Select Start Time");
        visit_hrs_to_list2.add("1 PM");
        visit_hrs_to_list2.add("2 PM");
        visit_hrs_to_list2.add("3 PM");
        visit_hrs_to_list2.add("4 PM");
        visit_hrs_to_list2.add("5 PM");
        visit_hrs_to_list2.add("6 PM");
        visit_hrs_to_list2.add("7 PM");
        visit_hrs_to_list2.add("8 PM");
        visit_hrs_to_list2.add("9 PM");
        visit_hrs_to_list2.add("10 PM");
        visit_hrs_to_list2.add("11 PM");
        visit_hrs_to_list2.add("12 AM");
        visit_hrs_to_list2.add("1 AM");
        visit_hrs_to_list2.add("2 AM");
        visit_hrs_to_list2.add("3 AM");
        visit_hrs_to_list2.add("4 AM");
        visit_hrs_to_list2.add("5 AM");
        visit_hrs_to_list2.add("6 AM");
        visit_hrs_to_list2.add("7 AM");
        visit_hrs_to_list2.add("8 AM");
        visit_hrs_to_list2.add("9 AM");
        visit_hrs_to_list2.add("10 AM");
        visit_hrs_to_list2.add("11 AM");


        visit_hrs_from_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                visit_hrs_from_list);
        visit_hrs_from_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
        visit_hrs_from_spinner     .setAdapter(visit_hrs_from_arrayadp);

        String[] visiting_hr_from_details = manager.getPreferences(mContext,"visiting_hr_from").split(Pattern.quote("|"));;
        String[] visiting_hr_to_details = manager.getPreferences(mContext,"visiting_hr_to").split(Pattern.quote("|"));;
        if(visiting_hr_from_details.length>1)
        {
            visit_hrs_from = visiting_hr_from_details[0].toLowerCase();
            visit_hrs_to = visiting_hr_to_details[0].toLowerCase();
            visit_hrs_from2 = visiting_hr_from_details[1].toLowerCase();
            visit_hrs_to2 = visiting_hr_to_details[1].toLowerCase() ;
        }
        else{
            visit_hrs_from = visiting_hr_from_details[0].toLowerCase();
            visit_hrs_to = visiting_hr_to_details[0].toLowerCase();
        }

        if(visit_hrs_from_list != null) {
            for (int i = 0; i < visit_hrs_from_list.size(); i++) {
                if (visit_hrs_from_list.get(i).equalsIgnoreCase(visit_hrs_from)) {
                    visit_hrs_from_spinner.setSelection(i);
                }
            }
        }

        if(visit_hrs_to_list != null) {
            for (int i = 0; i < visit_hrs_to_list.size(); i++) {
                if (visit_hrs_to_list.get(i).equalsIgnoreCase(visit_hrs_to)) {
                    visit_hrs_to_spinner.setSelection(i);
                }
            }
        }
        if(visit_hrs_from_list2 != null) {
            for (int i = 0; i < visit_hrs_from_list2.size(); i++) {
                if (visit_hrs_from_list2.get(i).equalsIgnoreCase(visit_hrs_from2)) {
                    visit_hrs_from_spinner2.setSelection(i);

                }
            }
        }

        if(visit_hrs_to_list2 != null) {
            for (int i = 0; i < visit_hrs_to_list2.size(); i++) {
                if (visit_hrs_to_list2.get(i).equalsIgnoreCase(visit_hrs_to2)) {
                    visit_hrs_to_spinner2.setSelection(i);
                }
            }
        }
        if(visit_hrs_from_spinner != null) {
            visit_hrs_from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        visit_hrs_from = visit_hrs_from_list.get(position);
                        visit_hrs_to = "";
                        visit_hrs_to_list = new ArrayList<>();
                        visit_hrs_to_list.add("Select End Time");

                        if (visit_hrs_from.equals("12 PM")) {

                            visit_hrs_to_list.add("1 PM");
                            visit_hrs_to_list.add("2 PM");
                            visit_hrs_to_list.add("3 PM");
                            visit_hrs_to_list.add("4 PM");
                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("1 PM")) {
                            visit_hrs_to_list.add("2 PM");
                            visit_hrs_to_list.add("3 PM");
                            visit_hrs_to_list.add("4 PM");
                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("2 PM")) {
                            visit_hrs_to_list.add("3 PM");
                            visit_hrs_to_list.add("4 PM");
                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("3 PM")) {
                            visit_hrs_to_list.add("4 PM");
                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("4 PM")) {

                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("5 PM")) {
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else if (visit_hrs_from.equals("6 PM")) {
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        } else {
                            visit_hrs_to_list.add("12 PM");
                            visit_hrs_to_list.add("1 PM");
                            visit_hrs_to_list.add("2 PM");
                            visit_hrs_to_list.add("3 PM");
                            visit_hrs_to_list.add("4 PM");
                            visit_hrs_to_list.add("5 PM");
                            visit_hrs_to_list.add("6 PM");
                            visit_hrs_to_list.add("7 PM");
                            visit_hrs_to_list.add("8 PM");
                            visit_hrs_to_list.add("9 PM");
                            visit_hrs_to_list.add("10 PM");
                            visit_hrs_to_list.add("11 PM");
                            visit_hrs_to_list.add("12 AM");

                        }


                        visit_hrs_to_arrayadp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                                visit_hrs_to_list);
                        visit_hrs_to_arrayadp.setDropDownViewResource(R.layout.simple_spinner);
                        visit_hrs_to_spinner.setAdapter(visit_hrs_to_arrayadp);


                    } else {
                        hideKeyboard(mContext);
                        visit_hrs_from = "";
                        visit_hrs_to = "";
                        visit_hrs_to_spinner.setAdapter(null);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        //==========================================================================================


        visit_hrs_to_list = new ArrayList<>();
        visit_hrs_to_list.add("12 PM");
        visit_hrs_to_list.add("1 PM");
        visit_hrs_to_list.add("2 PM");
        visit_hrs_to_list.add("3 PM");
        visit_hrs_to_list.add("4 PM");
        visit_hrs_to_list.add("5 PM");
        visit_hrs_to_list.add("6 PM");
        visit_hrs_to_list.add("7 PM");
        visit_hrs_to_list.add("8 PM");
        visit_hrs_to_list.add("9 PM");
        visit_hrs_to_list.add("10 PM");
        visit_hrs_to_list.add("11 PM");
        visit_hrs_to_list.add("12 AM");

        visit_hrs_to_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                visit_hrs_to_list);
        visit_hrs_to_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
        visit_hrs_to_spinner     .setAdapter(visit_hrs_to_arrayadp);
        if(visit_hrs_to_spinner != null) {
            visit_hrs_to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        visit_hrs_to = visit_hrs_to_list.get(position);

                    } else {

                        visit_hrs_to = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        visit_hrs_to_list2 = new ArrayList<>();

        visit_hrs_to_list2.add("1 PM");
        visit_hrs_to_list2.add("2 PM");
        visit_hrs_to_list2.add("3 PM");
        visit_hrs_to_list2.add("4 PM");
        visit_hrs_to_list2.add("5 PM");
        visit_hrs_to_list2.add("6 PM");
        visit_hrs_to_list2.add("7 PM");
        visit_hrs_to_list2.add("8 PM");
        visit_hrs_to_list2.add("9 PM");
        visit_hrs_to_list2.add("10 PM");
        visit_hrs_to_list2.add("11 PM");
        visit_hrs_to_list2.add("12 AM");
        visit_hrs_to_list2.add("1 AM");
        visit_hrs_to_list2.add("2 AM");
        visit_hrs_to_list2.add("3 AM");
        visit_hrs_to_list2.add("4 AM");
        visit_hrs_to_list2.add("5 AM");
        visit_hrs_to_list2.add("6 AM");
        visit_hrs_to_list2.add("7 AM");
        visit_hrs_to_list2.add("8 AM");
        visit_hrs_to_list2.add("9 AM");
        visit_hrs_to_list2.add("10 AM");
        visit_hrs_to_list2.add("11 AM");
        visit_hrs_to_list2.add("12 AM");

        visit_hrs_to_arrayadp2    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                visit_hrs_to_list2);
        visit_hrs_to_arrayadp2    .setDropDownViewResource(R.layout.simple_spinner);
        visit_hrs_to_spinner2     .setAdapter(visit_hrs_to_arrayadp2);
        if(visit_hrs_to_spinner2 != null) {
            visit_hrs_to_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {

                        visit_hrs_to2 = visit_hrs_to_list2.get(position);

                    } else {

                        visit_hrs_to2 = "";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }



        //========================================================================================



        visit_hrs_from_arrayadp2    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                visit_hrs_from_list2);
        visit_hrs_from_arrayadp2.setDropDownViewResource(R.layout.simple_spinner);
        visit_hrs_from_spinner2.setAdapter(visit_hrs_from_arrayadp2);
        if(visit_hrs_from_spinner2 != null) {
            visit_hrs_from_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //  if(!("").equals(visit_hrs_from)) {
                    if (position > 0) {

                        visit_hrs_from2 = visit_hrs_from_list2.get(position);
                        visit_hrs_to2 = "";
                        visit_hrs_to_list2 = new ArrayList<>();
                        visit_hrs_to_list2.add("Select End Time");

                        if (visit_hrs_from2.equals("1 PM")) {
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");

                        } else if (visit_hrs_from2.equals("2 PM")) {
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");

                        } else if (visit_hrs_from2.equals("3 PM")) {
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");

                        } else if (visit_hrs_from2.equals("4 PM")) {
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");

                        } else if (visit_hrs_from2.equals("5 PM")) {
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");

                        } else if (visit_hrs_from2.equals("6 PM")) {
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");

                        } else if (visit_hrs_from2.equals("7 PM")) {
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");


                        } else if (visit_hrs_from2.equals("8 PM")) {
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");


                        } else if (visit_hrs_from2.equals("9 PM")) {

                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");


                        } else if (visit_hrs_from2.equals("10 PM")) {
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");


                        } else if (visit_hrs_from2.equals("11 PM")) {

                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");


                        } else if (visit_hrs_from2.equals("12 AM")) {

                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");


                        } else if (visit_hrs_from2.equals("1 AM")) {
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");

                        } else if (visit_hrs_from2.equals("2 AM")) {
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");

                        } else if (visit_hrs_from2.equals("3 AM")) {
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");

                        } else if (visit_hrs_from2.equals("4 AM")) {

                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");


                        } else if (visit_hrs_from2.equals("5 AM")) {
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");

                        } else if (visit_hrs_from2.equals("6 AM")) {
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");
                            visit_hrs_to_list2.add("12 PM");
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");

                        } else {
                            visit_hrs_to_list2.add("1 PM");
                            visit_hrs_to_list2.add("2 PM");
                            visit_hrs_to_list2.add("3 PM");
                            visit_hrs_to_list2.add("4 PM");
                            visit_hrs_to_list2.add("5 PM");
                            visit_hrs_to_list2.add("6 PM");
                            visit_hrs_to_list2.add("7 PM");
                            visit_hrs_to_list2.add("8 PM");
                            visit_hrs_to_list2.add("9 PM");
                            visit_hrs_to_list2.add("10 PM");
                            visit_hrs_to_list2.add("11 PM");
                            visit_hrs_to_list2.add("12 AM");
                            visit_hrs_to_list2.add("1 AM");
                            visit_hrs_to_list2.add("2 AM");
                            visit_hrs_to_list2.add("3 AM");
                            visit_hrs_to_list2.add("4 AM");
                            visit_hrs_to_list2.add("5 AM");
                            visit_hrs_to_list2.add("6 AM");
                            visit_hrs_to_list2.add("7 AM");
                            visit_hrs_to_list2.add("8 AM");
                            visit_hrs_to_list2.add("9 AM");
                            visit_hrs_to_list2.add("10 AM");
                            visit_hrs_to_list2.add("11 AM");

                        }

                        visit_hrs_to_arrayadp2 = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                                visit_hrs_to_list2);
                        visit_hrs_to_arrayadp2.setDropDownViewResource(R.layout.simple_spinner);
                        visit_hrs_to_spinner2.setAdapter(visit_hrs_to_arrayadp2);


                    } else {
                        hideKeyboard(mContext);
                        visit_hrs_to2 = "";
                        visit_hrs_from2 = "";
                        visit_hrs_to_spinner2.setAdapter(null);
                    }
//                }else {
//                    visit_hrs_to2 = "";
//                    visit_hrs_from2 = "";
//                    Toast.makeText(mContext, "Please Select Timings 1", Toast.LENGTH_SHORT).show();
//                }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }



        savee_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isConnectingToInternet(mContext)) {
                    String et_name_temp = et_name.getText().toString().trim();


                    if(et_name_temp.equalsIgnoreCase(""))
                    {
                        et_name.setFocusable(true);
                        et_name.setError(getResources().getString(R.string.full_name));
                    }
                    else
                    {
                         if(inputMobileNumber_login.getPhoneNumber().trim().equalsIgnoreCase(""))
                        {
                            inputMobileNumber_login.setFocusable(true);
                            inputMobileNumber_login.setError("Enter Mobile");
                        }
                        else
                        {
                        if(et_email_id.getText().toString().trim().equalsIgnoreCase(""))
                        {
                            et_email_id.setFocusable(true);
                            et_email_id.setError(getResources().getString(R.string.email_id));
                        }
                        else {
                            if (Utils.emailValidator(et_email_id.getText().toString().trim())) {

                                progressBar_main.setVisibility(View.VISIBLE);
                                apiViewHolder.Register1(manager.getPreferences(mContext, "doctor_id"),
                                        et_name.getText().toString().trim(),
                                        et_email_id.getText().toString().trim(),
                                        manager.getPreferences(mContext, "App_id"),
                                        inputMobileNumber_login.getPhoneNumber())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(responseRegister1);
                            } else {
                                et_email_id.setFocusable(true);
                                et_email_id.setError("Email Id Not Valid");

                            }
                        }
                        }
                    }

                } else {
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });



        save_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isConnectingToInternet(mContext)) {
                    if(et_registration_no.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        et_registration_no.setFocusable(true);
                        et_registration_no.setError(getResources().getString(R.string.registration_number));
                    }
                    else {
                        if (ug_spinner.getSelectedItemPosition() == 0) {
                            ug_spinner.setFocusable(true);
                            Toast.makeText(mContext, "Please select undergraduate qualification", Toast.LENGTH_SHORT).show();
                        } else {

                            if(!ug_id__.equals("")){
                                progressBar_main.setVisibility(View.VISIBLE);
                                apiViewHolder.Register2(manager.getPreferences(mContext, "doctor_id"),
                                        et_registration_no.getText().toString().trim(), et_designation.getText().toString().trim(), ug_id__, pg_id,
                                        et_add_qualification.getText().toString().trim())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(responseRegister2);
                            }
                            else {
                                Toast.makeText(mContext, "Please select undergraduate qualification", Toast.LENGTH_SHORT).show();

                            }

//                            progressBar_main.setVisibility(View.VISIBLE);
//                            apiViewHolder.Register2(manager.getPreferences(mContext, "doctor_id"),
//                                    et_registration_no.getText().toString().trim(), et_designation.getText().toString().trim(), ug_id, pg_id,
//                                    et_add_qualification.getText().toString().trim())
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(responseRegister2);

                        }
                    }
                } else {
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        save_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isConnectingToInternet(mContext)) {

                    String markedButtons= "";
                    String notmarked= "";

                    //Check individual items.
                    if(Mon.isChecked()){
                        markedButtons +=",Mon";
                    }else {
                        notmarked +=",Mon";
                    }
                    if(Tue.isChecked()){
                        markedButtons +=",Tue";
                    }else {
                        notmarked +=",Tue";
                    }
                    if(Wed.isChecked()){
                        markedButtons +=",Wed";
                    }else {
                        notmarked +=",Wed";
                    }
                    if(Thu.isChecked()){
                        markedButtons +=",Thu";
                    }else {
                        notmarked +=",Thu";
                    }
                    if(Fri.isChecked()){
                        markedButtons +=",Fri";
                    }else {
                        notmarked +=",Fri";
                    }
                    if(Sat.isChecked()){
                        markedButtons +=",Sat";
                    }else {
                        notmarked +=",Sat";
                    }
                    if(Sun.isChecked()){
                        markedButtons +=",Sun";
                    }else {
                        notmarked +=",Sun";
                    }

                    if(!markedButtons.equalsIgnoreCase("")) {
                        markedButtons = markedButtons.substring(1);
                    }

                    if(!notmarked.equalsIgnoreCase("")) {
                        notmarked = notmarked.substring(1);
                    }

                    if(!et_visit_from1.getText().toString().trim().equals("")){
                        if(!et_visit_to1.getText().toString().trim().equals("")){
                            if(!et_visit_from2.getText().toString().trim().equals("")){
                                if(!et_visit_to2.getText().toString().trim().equals("")){
                                    vst_frm = et_visit_from1.getText().toString().trim()+"|"+et_visit_from2.getText().toString().trim();
                                    vst_to = et_visit_to1.getText().toString().trim()+"|"+et_visit_to2.getText().toString().trim();
                                    progressBar_main.setVisibility(View.VISIBLE);
                                    apiViewHolder.Register3(manager.getPreferences(mContext, "doctor_id"),
                                            et_alt_no.getText().toString().trim(),
                                            et_alt_mail.getText().toString().trim(),
                                            markedButtons, vst_frm, vst_to, notmarked )
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(responseRegister3);
                                }else {
                                    Toast.makeText(mContext, "Please select visiting Hours To", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                vst_frm =  et_visit_from1.getText().toString().trim();
                                vst_to = et_visit_to1.getText().toString().trim();
                                progressBar_main.setVisibility(View.VISIBLE);
                                apiViewHolder.Register3(manager.getPreferences(mContext, "doctor_id"),
                                        et_alt_no.getText().toString().trim(),
                                        et_alt_mail.getText().toString().trim(),
                                        markedButtons, vst_frm, vst_to, notmarked )
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(responseRegister3);
                            }
                        }else {
                            Toast.makeText(mContext, "Please select visiting Hours To", Toast.LENGTH_SHORT).show();
                        }
                    }else if(!et_visit_from2.getText().toString().trim().equals("")){
                        if(!et_visit_to2.getText().toString().trim().equals("")){
                            vst_frm =  visit_hrs_from2;
                            vst_to = visit_hrs_to2;
                            progressBar_main.setVisibility(View.VISIBLE);
                            apiViewHolder.Register3(manager.getPreferences(mContext, "doctor_id"),
                                    et_alt_no.getText().toString().trim(),
                                    et_alt_mail.getText().toString().trim(),
                                    markedButtons, vst_frm, vst_to, notmarked )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responseRegister3);
                        }else {
                            Toast.makeText(mContext, "Please select visiting Hours To", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        progressBar_main.setVisibility(View.VISIBLE);
                        apiViewHolder.Register3(manager.getPreferences(mContext, "doctor_id"),
                                et_alt_no.getText().toString().trim(),
                                et_alt_mail.getText().toString().trim(),
                                markedButtons, vst_frm, vst_to, notmarked )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(responseRegister3);
                    }
                } else {
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        save_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext, "save_4", Toast.LENGTH_SHORT).show();
                /*if(logo_flag)
                UploadImage();
                else
                    Toast.makeText(mContext, "Select Logo", Toast.LENGTH_SHORT).show();*/

                UploadImage();

            }
        });
        save_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Your Profile Created Successfully", Toast.LENGTH_SHORT).show();
                if(manager.contains(mContext,"show_banner_ad"))
                    InterstitialAd(adRequest);
                else{
                    Intent intent1 = new Intent(RegisterDetails.this, HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }


            }
        });

        btn_signature_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup();
            }
        });
        /*mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {

                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });*/


    }

    private void TvSelected(TextView one_tv) {
        one_tv.setBackground(getResources().getDrawable(R.drawable.blue_btn_gradient_new));
        one_tv.setTextColor(getResources().getColor(R.color.white));

    }

    private void TvDeSelected(TextView one_tv) {
        one_tv.setBackground(getResources().getDrawable(R.drawable.white_btn_gradient));
        one_tv.setTextColor(getResources().getColor(R.color.colorBlack));

    }


    SingleObserver<ResponseRegister> responseRegister1 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            if (response != null) {

                Log.e(TAG, "responseRegister1: >> " + response.getMessage());
                progressBar_main.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Registration Failed", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getMessage().equals("Profile Updated")) {
                    manager.setPreferences(mContext,"doctor_id",response.getDoctorId());
                    manager.setPreferences(mContext,"email",response.getEmail());
//                    manager.setPreferences(mContext,"name",response.getName());

                    String name_ = response.getName();

                        manager.setPreferences(mContext, "name", name_);

                    /*String name_2 = manager.getPreferences(mContext,"name").trim();
                    if(name_2.contains("Dr."))
                    {
                        name_2 = name_2.replace("Dr.","");
                        manager.setPreferences(mContext,"name",name_2);
                    }*/

                    getUpdatedProfileDetails(response);
                    two_tv.performClick();

                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                progressBar_main.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister1 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    SingleObserver<ResponseRegister> responseDeleteSignature = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            if (response != null) {

                Log.e(TAG, "responseDeleteSignature: >> " + response.getMessage());
                progressBar_main.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Deletion Failed", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getMessage().equals("Profile Updated")) {

                    Toast.makeText(mContext, "Signature Deleted", Toast.LENGTH_SHORT).show();
                    getUpdatedProfileDetails(response);
                    five_tv.performClick();
                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                progressBar_main.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseDeleteSignature >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    SingleObserver<ResponseRegister> responsedeleteLogo = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            if (response != null) {

                Log.e(TAG, "responseDeleteSignature: >> " + response.getMessage());
                progressBar_main.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Deletion Failed", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getMessage().equals("Logo Deleted")) {

                    Toast.makeText(mContext, "Logo Deleted", Toast.LENGTH_SHORT).show();
                    getUpdatedProfileDetails(response);
                 //   four_tv.performClick();
                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                progressBar_main.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseDeleteSignature >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    public  void addUG(){
        popupAddUG();
    }
    public void popupUG(){


        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_data.getWindow().setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.custom_dialog_ug);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        int dim[] = ScreenSize.getDimensions(mContext);

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = (dim[1]/2)+300;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);



        EditText filterText =  dialog_data.findViewById(R.id.alertdialog_edittext);
        RecyclerView alertdialog_Listview =  dialog_data.findViewById(R.id.alertdialog_Listview);

        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        alertdialog_Listview.setLayoutManager(gridLayoutManagerr);

        ugAdapter = new UGAdapter(mContext, ugItemArrayList,ug_name,apiViewHolder,mBag,dialog_data,RegisterDetails.this);
        alertdialog_Listview.setAdapter(ugAdapter);

        filterText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                ugAdapter.getFilter().filter(s);
            }
        });

        dialog_data.show();

    }

    SingleObserver<ResponseAddUG> responseAddUG = new SingleObserver<ResponseAddUG>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseAddUG response) {
            if (response != null) {

                Log.e(TAG, "responseAddUG: >> " + response.getMessage());
                progressBar_main.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();

                } else if (response.getMessage().equals("UG Added")) {
                    Toast.makeText(mContext, "UG Added", Toast.LENGTH_SHORT).show();

                    ug_spinner.setAdapter(null);
                    ug_name = response.getUgName().trim();
                    textView__select_ug.setText(ug_name);
                    //Toast.makeText(mContext, ""+ug_name, Toast.LENGTH_SHORT).show();

                    apiViewHolder.getUGs()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseUGs);

                   /* UGItem ugItem = new UGItem();
                    ugItem.setUgId(""+response.getUgId());
                    ugItem.setUgName(response.getUgName());
                    ugItemArrayList.add(ugItem);

                    ug_list.add(response.getUgName());

                    ug_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, ug_list);
                    ug_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
                    ug_spinner     .setAdapter(ug_arrayadp);

                    ug_id = ""+response.getUgId();*/
                    // ug_spinner.setSelection(ug_list.size()-1);

                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                progressBar_main.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseAddUG >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };



    SingleObserver<ResponseUG> responseUGs = new SingleObserver<ResponseUG>() {
            @Override
            public void onSubscribe(Disposable d) {
                mBag.add(d);
            }

            @Override
            public void onSuccess(ResponseUG response) {
                if (response != null) {

                    Log.e(TAG, "responseUGs: >> " + response.getMessage());

                    if (response.getMessage() == null) {

                    } else if (response.getMessage().equals("All UG")) {

                        ugItemArrayList = new ArrayList<>();
                        ugItemArrayList = response.getUG();
                        UGItem ugItem = new UGItem();
                        ugItem.setUgId("");
                        ugItem.setUgName("Other");
                        ugItemArrayList.add(ugItem);

                        if(ug_name!=null){
                            for (int i=0;i<ugItemArrayList.size();i++){
                                assert ug_name != null;
                                if(ug_name.equals(ugItemArrayList.get(i).getUgName()))
                                {
                                    textView__select_ug.setText(ug_name);
                                    //ug_spinner.setSelection(i);
                                    ug_name = null;
                                    break;
                                }
                            }
                        }

                    /*ug_list = new ArrayList<>();
                    ug_list.add(mContext.getResources().getString(R.string.select_ug));
                    for (UGItem di: response.getUG()) {
                        ug_list.add(di.getUgName());
                    }
                    ug_list.add("Other");
                    ug_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, ug_list);
                    ug_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
                    ug_spinner     .setAdapter(ug_arrayadp);

                    ug_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                // if((ug_list.size()-1)==position){
                                if((ug_list.get(position).equals("Other"))){
                                    popupAddUG();
                                }
                                else {
                                    position= position-1;
                                    ug_id = ugItemArrayList.get(position).getUgId();
                                    hideKeyboard(mContext);
                                }

                            }
                            else {
                                hideKeyboard(mContext);
                                ug_id = "";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    */

                    }
                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: responseUGs >> " + e.toString());
                //intentCall();
                Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
            }
        };



    private  void popupEmail()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_no_email);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }



    private void getUpdatedProfileDetails(ResponseRegister response) {
        manager.setPreferences(mContext, "doctor_id", response.getDoctorId());
        manager.setPreferences(mContext, "name", response.getName());
        manager.setPreferences(mContext, "email", response.getEmail());
        manager.setPreferences(mContext, "mobile", response.getMobile());
        manager.setPreferences(mContext, "registration_no", response.getRegistrationNo());
        manager.setPreferences(mContext, "speciality_id", response.getSpecialityId());
        manager.setPreferences(mContext, "ug_id", response.getUgId());
        manager.setPreferences(mContext, "pg_id", response.getPgId());
        manager.setPreferences(mContext, "designation_id", response.getDesignationId());
        manager.setPreferences(mContext, "addtional_qualification", response.getAddtionalQualification());
        manager.setPreferences(mContext, "mobile_letter_head", response.getMobileLetterHead());
        manager.setPreferences(mContext, "email_letter_head", response.getEmailLetterHead());
        manager.setPreferences(mContext, "working_days", response.getWorkingDays());
        manager.setPreferences(mContext, "visiting_hr_from", response.getVisitingHrFrom());
        manager.setPreferences(mContext, "visiting_hr_to", response.getVisitingHrTo());
        manager.setPreferences(mContext, "close_day", response.getCloseDay());
        manager.setPreferences(mContext, "speciality_name", response.getSpecialityName());
        manager.setPreferences(mContext, "ug_name", response.getUgName());
        manager.setPreferences(mContext, "pg_name", response.getPgName());
        manager.setPreferences(mContext, "designation_name", response.getDesignationName());
        manager.setPreferences(mContext, "signature", response.getSignature());
        manager.setPreferences(mContext, "logo", response.getLogo());
        manager.setPreferences(mContext, "image", response.getImage());
        manager.setPreferences(mContext, "clinic_name", response.getClinicName());
        manager.setPreferences(mContext, "clinic_address", response.getClinicAddress());
    }

    SingleObserver<ResponseRegister> responseRegister2 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            progressBar_main.setVisibility(View.GONE);
            if (response != null) {

                Log.e(TAG, "responseRegister2: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Profile Updated")) {
                    getUpdatedProfileDetails(response);
                    manager.setPreferences(mContext,"second","true");
                    three_tv.performClick();
                    et_alt_mail.setText(manager.getPreferences(mContext,"email"));

                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister2 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    SingleObserver<ResponseRegister> responseRegister3 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            progressBar_main.setVisibility(View.GONE);
            if (response != null) {

                Log.e(TAG, "responseRegister3: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Profile Updated")) {
                    getUpdatedProfileDetails(response);
                    manager.setPreferences(mContext,"third","true");

                   four_tv.performClick();

                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister3 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };




    SingleObserver<ResponseRegister> responseRegister4 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            progressBar_main.setVisibility(View.GONE);
            if (response != null) {

                Log.e(TAG, "responseRegister4: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Profile Updated")) {
                    getUpdatedProfileDetails(response);
                    manager.setPreferences(mContext,"fourth","true");
                    four_tv.performClick();
                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister4 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };



    SingleObserver<ResponseRegister> responseRegister5 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            progressBar_main.setVisibility(View.GONE);
            if (response != null) {

                Log.e(TAG, "responseRegister5: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Profile Updated")) {
                    getUpdatedProfileDetails(response);
                    manager.setPreferences(mContext,"fifth","true");

                    first.setVisibility(View.GONE);
                    second.setVisibility(View.GONE);
                    third.setVisibility(View.GONE);
                    fourth.setVisibility(View.GONE);
                    fifth.setVisibility(View.VISIBLE);

                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            progressBar_main.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister5 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };




    private void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Show
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    SingleObserver<ResponsePG> responsePGs = new SingleObserver<ResponsePG>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponsePG response) {
            if (response != null) {

                Log.e(TAG, "responsePGs: >> " + response.getMessage());

                if (response.getMessage() == null) {

                } else if (response.getMessage().equals("All PG")) {

                    pgItemArrayList = new ArrayList<>();
                    pgItemArrayList = response.getPG();
                    pg_list = new ArrayList<>();
                    pg_list.add(mContext.getResources().getString(R.string.select_pg));
                    pg_list.add("None");
                    for (PGItem di: response.getPG()) {
                        pg_list.add(di.getPgName());
                    }

                    pg_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, pg_list);
                    pg_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
                    pg_spinner     .setAdapter(pg_arrayadp);
                    if(pg_spinner != null ) {
                        pg_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position > 1) {
                                    position = position - 2;
                                    pg_id = pgItemArrayList.get(position).getPgId();
                                    hideKeyboard(mContext);
                                } else {
                                    hideKeyboard(mContext);
                                    pg_id = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    progressBar_main.setVisibility(View.GONE);
                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responsePGs >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private  void popupAddUG()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_add_ug);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp_number_picker);

        Button btn_cancel = dialog_data.findViewById(R.id.btn_cancel);
        Button btn_add_ug= dialog_data.findViewById(R.id.btn_add_ug);
        final EditText et_ug_name= dialog_data.findViewById(R.id.et_ug_name);

        btn_add_ug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                if(et_ug_name.getText().toString().trim().equals("")){
                    et_ug_name.setError("UG qualification required");
                }
                else {
                    apiViewHolder.AddUG(et_ug_name.getText().toString().trim())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseAddUG);
                }
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


   /* SingleObserver<ResponseSpecialities> responseSpecialities = new SingleObserver<ResponseSpecialities>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSpecialities response) {
            if (response != null) {

                Log.e(TAG, "responseSpecialities: >> " + response.getMessage());

                if (response.getMessage() == null) {

                } else if (response.getMessage().equals("All Specialities")) {

                    specialitiesItemArrayList = new ArrayList<>();
                    specialitiesItemArrayList = response.getSpecialities();
                    specialities_list = new ArrayList<>();
                    specialities_list.add(mContext.getResources().getString(R.string.select_specialities));
                    for (SpecialitiesItem di: response.getSpecialities()) {
                        specialities_list.add(di.getSpecialityName());
                    }

                    specialities_arrayadp    = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_dropdown_item, specialities_list);
                    specialities_arrayadp    .setDropDownViewResource(R.layout.simple_spinner);
                    specialities_spinner     .setAdapter(specialities_arrayadp);

                    specialities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {

                                specialities_id = specialitiesItemArrayList.get(position-1).getSpecialityId();
                                hideKeyboard();
                            }
                            else {
                                hideKeyboard();
                                specialities_id = "";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseSpecialities >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };*/


   //-----------------------------------------------------------------------------------------------
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("requestCode   >>>>  ",""+requestCode);
//        Log.e("resultCode   >>>>  ",""+resultCode);
//        //Log.e("data   >>>>  ",""+data.getData());
//        if (resultCode == Activity.RESULT_OK)
//        {
//            if (requestCode == ChoosePhoto.CHOOSE_PHOTO_INTENT)
//            {
//                if (data != null && data.getData() != null)
//                {
//                   /* if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                    {
//
//                    }
//                    else
//                    {
//                        choosePhoto.handleGalleryResult(data);
//                    }*/
//                    choosePhoto.handleGalleryResult(data);
//                    Log.e("<CAMERA_INTENT> >>>>"," photoURI = "+data.getData());
//
//                    photoURI = data.getData();
//                    Log.e("<CAMERA_INTENT> DATA >>","   "+photoURI);
//                    Log.e("<CAMERA_INTENT> DATA >>",
//                            "   getRealPathFromURI = "+ FileUtil.getRealPathFromURI(mContext,photoURI));
//
//                    imageUp = new File(FileUtil.getRealPathFromURI(mContext, photoURI));
//
//                    //getOrientation();
//                    /*if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                    {
//                       // choosePhoto.handleGalleryResult(data);
//                        resizeAndCompressImageBeforeSend(mContext,
//                                FileUtil.getRealPathFromURI(mContext,photoURI),"abc");
//                        profilePic.setImageBitmap(rotatedBitmap);
//                    }
//                    else
//                    {
//                        choosePhoto.handleGalleryResult(data);
//                        resizeAndCompressImageBeforeSend(mContext,
//                                FileUtil.getRealPathFromURI(mContext,photoURI),"abc");
//                        profilePic.setImageBitmap(rotatedBitmap);
//                    }*/
//                    // choosePhoto.handleGalleryResult(data);
//                    resizeAndCompressImageBeforeSend(mContext,
//                            FileUtil.getRealPathFromURI(mContext,photoURI),"abc");
//                    // profilePic.setImageBitmap(rotatedBitmap);
//                    // UploadImage();
//
//
//                } else {
//                    Log.e("<getCameraUri> >>>>", "  " + choosePhoto.getCameraUri());
//
//                    if (choosePhoto.getCameraUri()!=null)
//                    {
//                        choosePhoto.handleCameraResult(choosePhoto.getCameraUri());
//
//                        Log.e("<PHOTO_INTENT> >>>>", "  photoURI = " + choosePhoto.getCameraUri());
//
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(
//                                    getApplicationContext().getContentResolver(), choosePhoto.getCameraUri());
//
//                            photoURI = choosePhoto.getCameraUri();
//                            getOrientation();
//                            //image = fileUtil.createImageTempFile(
//                            //   new File(FileUtil.getRealPathFromURI(context,photoURI)));
//
//                            // profilePic.setImageBitmap(rotatedBitmap);
//                            // UploadImage();
//                            //   resizeAndCompressImageBeforeSend(context,
//                            //           FileUtil.getRealPathFromURI(context, photoURI), "abc");
//                            logo_flag=true;
//                        } catch (IOException e) {
//                            Log.e("<PHOTO_INTENT> EXCP >>", "   " + e);
//
//                        }
//                    }
//                    else
//                    {
//                        //photoURI = FileUtil.getRealPathFromURI(context, data.getData());
//                        /*if(photoURI!=null)
//                        {
//                            Log.e("<PHOTO_INTENT> >>>>", "  Not NULL photoURI  = " + photoURI);
//                            photoURI = data.getData();
//                            choosePhoto.handleCameraResult(photoURI);
//
//                        try {
//                            bitmap = MediaStore.Images.Media.getBitmap(
//                                    getApplicationContext().getContentResolver(), choosePhoto.getCameraUri());
//                            //getOrientation();
//
//                            //photoURI = choosePhoto.getCameraUri();
//
//                            //image = fileUtil.createImageTempFile(
//                            //   new File(FileUtil.getRealPathFromURI(context,photoURI)));
//
//
//                            mImgCamera.setImageBitmap(bitmap);
//                            proceed_.setVisibility(View.VISIBLE);
//                            resizeAndCompressImageBeforeSend(context,
//                                    FileUtil.getRealPathFromURI(context, photoURI), "abc");
//                        } catch (IOException e) {
//                            Log.e("<PHOTO_INTENT> EXCP >>", "   " + e);
//
//                        }*/
//                        Log.e("<PHOTO_INTENT> >>>>", "  NULL  = " );
//
//                        // }
//                    /*else
//                        {
//                        }*/
//                    }
//                }
//            }else if (requestCode == ChoosePhoto.SELECTED_IMG_CROP)
//            {
//                Log.e("CHOOSE PHOTO    >>>>  ",""+choosePhoto.getCropImageUrl());
//                Log.e("getCameraBitmap >>>>  ",""+rotatedBitmap);
//
//                photoURI = data.getData();
//                Log.e("<CAMERA_INTENT> DATA >>","   "+photoURI);
//                Log.e("<CAMERA_INTENT> DATA >>",
//                        "   getRealPathFromURI = "+ FileUtil.getRealPathFromURI(mContext,photoURI));
//
//                imageUp = new File(FileUtil.getRealPathFromURI(mContext, photoURI));
//
//                //getOrientation();
//
//                resizeAndCompressImageBeforeSend(mContext,
//                        FileUtil.getRealPathFromURI(mContext,photoURI),"abc");
//                image_layout.setImageBitmap(rotatedBitmap);
//                logo_flag=true;
//                // profilePic.setImageURI(choosePhoto.getCropImageUrl());
//
//                //  profilePic.setImageBitmap(rotatedBitmap);
//
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_ACTIVITY_CODE);
        }
    }

    public void getOrientation() {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(FileUtil.getRealPathFromURI( getApplicationContext(),photoURI));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Log.e("orientation >>>>  ",""+orientation);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
                //rotatedBitmap = rotateImage(bitmap, 0);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    //====================================================================================
    public void resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName)
    {
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 500, 500);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;
        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);
        int rotationAngle = getCameraPhotoOrientation(context, photoURI, filePath);
        Matrix matrix = new Matrix();

       /* if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
           rotatedBitmap = bmpPic;
        }else {*/
        matrix.postRotate(0, (float) bmpPic.getWidth(), (float) bmpPic.getHeight());
        rotatedBitmap = Bitmap.createBitmap(bmpPic, 0, 0, bmpPic.getWidth(),
                bmpPic.getHeight(),matrix,false);
        // }

        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.e("compressBitmap", "Quality: " + compressQuality);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            //Log.e("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            if(FileUtil.image==null) {
                // imageUp = new File(FileUtil.getRealPathFromURI(context,photoURI));
                Log.e("<PHOTO_INTENT> >>>>", "  NULL  = " );

                String path=""+FileUtil.getRealPathFromURI(context,photoURI);
                // it contains your image path...I'm using a temp string...
                String filename=path.substring(path.lastIndexOf("/")+1);

                imageUp = new File((mContext
                        .getApplicationContext().getFileStreamPath(filename)
                        .getPath()));

                //save the resized and compressed file to disk cache
                Log.e("if = compressBitmap", "cacheDir: " + context.getCacheDir());
                FileOutputStream bmpFile = new FileOutputStream(imageUp);
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);

                Log.e("if = compressBitmap", "Size: " + (imageUp.length() / 1024) + " kb");
                bmpFile.flush();
                bmpFile.close();
            }
            else
            {
                Log.e("else = compressBitmap", "cacheDir: " + context.getCacheDir());
                FileOutputStream bmpFile = new FileOutputStream(FileUtil.image);
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
                imageUp = FileUtil.image;
                Log.e("else = compressBitmap",
                        "Size: " + (FileUtil.image.length() / 1024) + " kb");

                //imageUp = new File(FileUtil.image.getPath());

                bmpFile.flush();
                bmpFile.close();
            }
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file > "+e);
        }
        //return the path of resized and compressed file

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.e(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            Log.e(debugTag,"image height: "+halfHeight+ "---image width: "+ halfWidth);
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }


    //===================================================================================
    public  int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        Log.e("rotate     =    ","getCameraPhotoOrientation");

        try {
            context.getContentResolver().notifyChange(imageUri, null);
            //imageUp = new File(imagePath);
            ExifInterface exif = new ExifInterface(FileUtil.getRealPathFromURI(context,photoURI));
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    Log.e("rotate = 0"," 0");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    Log.e("rotate = 270"," 270");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    Log.e("rotate = 180"," 180");
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    Log.e("rotate = 90"," 90");
                    break;

            }
        } catch (Exception e)
        {
            Log.e("Excp rotate "," "+e);
        }
        return rotate;
    }

    public  void UploadSignature()
    {

          if (Utils.isConnectingToInternet(mContext)) {

                        progressBar_main.setVisibility(View.VISIBLE);
                        Retrofit retrofit = RetrofitClient.getInstance();;
                        final WebServices request = retrofit.create(WebServices.class);;
                        RequestBody CUSTOMER_ID = RequestBody.create(MediaType.parse("text/plain"),
                                manager.getPreferences(mContext,"doctor_id") );
                        RequestBody profile = RequestBody.create(MediaType.parse("text/plain"),
                      "sign" );

                        MultipartBody.Part multipartBody = null;
                        Call<ResponseRegister> call;
                        // File file = new File(file_name_tv.getText().toString().trim());
                        if(imageUp!=null)
                        {
                            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                            multipartBody = MultipartBody.Part.createFormData("signature", imageUp.getName(),fbody);
                        }


                        call = request.UpdateProfileSign(CUSTOMER_ID, profile, multipartBody);

                        call.enqueue(new Callback<ResponseRegister>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseRegister> call, @NonNull retrofit2.Response<ResponseRegister> response) {
                                ResponseRegister jsonResponse = response.body();
                                assert jsonResponse != null;
                                progressBar_main.setVisibility(View.GONE);
                                if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                                        jsonResponse.getMessage().equalsIgnoreCase("Profile Updated")) {
                                    Toast.makeText(mContext, "Signature Updated", Toast.LENGTH_SHORT).show();
                                    getUpdatedProfileDetails(jsonResponse);
                                    five_tv.performClick();
                                } else {
                                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseRegister> call, @NonNull Throwable t) {
                                progressBar_main.setVisibility(View.GONE);
                                Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                            }
                        });



            }
            else
            {
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


    public  void UploadImage()
    {
        if (Utils.isConnectingToInternet(mContext)) {


        progressBar_main.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClient.getInstance();;
        final WebServices request = retrofit.create(WebServices.class);;
            RequestBody CUSTOMER_ID = RequestBody.create(MediaType.parse("text/plain"),
                    manager.getPreferences(mContext,"doctor_id") );


        RequestBody CLINIC_NAME = RequestBody.create(MediaType.parse("text/plain"),
                et_Clinic_name.getText().toString().trim() );

        RequestBody CLINIC_ADD = RequestBody.create(MediaType.parse("text/plain"),
                et_Hos_address.getText().toString().trim() );
        RequestBody Signature = RequestBody.create(MediaType.parse("text/plain"),
                "image");

        Call<ResponseRegister> call;
            if(imageSelectedFlag)
            {
                call = request.UpdateProfileImage(CUSTOMER_ID,CLINIC_NAME,CLINIC_ADD,Signature,multipartBody);
            }
            else {
                call = request.UpdateProfileImage22(CUSTOMER_ID,CLINIC_NAME,CLINIC_ADD,Signature);
            }

        call.enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(@NonNull Call<ResponseRegister> call, @NonNull retrofit2.Response<ResponseRegister> response) {
                ResponseRegister jsonResponse = response.body();
                assert jsonResponse != null;
                progressBar_main.setVisibility(View.GONE);
                if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                        jsonResponse.getMessage().equalsIgnoreCase("Profile Updated")) {
                    imageSelectedFlag = false;
                    Toast.makeText(mContext, "Profile Updated", Toast.LENGTH_SHORT).show();
                    getUpdatedProfileDetails(response.body());
                    five_tv.performClick();

                } else {
                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseRegister> call, @NonNull Throwable t) {
                progressBar_main.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });
            }
        else
            {
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
    }


    private  void popup()
    {
        final SignaturePad mSignaturePad;
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(false);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_no_digital_signature);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_upload = dialog_data.findViewById(R.id.btn_upload);
        Button btn_clear = dialog_data.findViewById(R.id.btn_clear);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);
        final ImageView iv_sign = dialog_data.findViewById(R.id.iv_sign);
        mSignaturePad = dialog_data.findViewById(R.id.signature_pad);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getFileFromBitmapImage(mSignaturePad.getSignatureBitmap(),"Sign"))
                {
                    UploadSignature();
                    dialog_data.dismiss();
                }
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignaturePad.clearView();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                Log.e(TAG, "onSigned: >>>>>>>>>>>>>>>>>>>>>>>>> ");
                //iv_sign.setImageBitmap( mSignaturePad.getSignatureBitmap());
            }

            @Override
            public void onClear() {

            }
        });
        dialog_data.show();

    }



    private  boolean getFileFromBitmapImage(Bitmap bitmap, String name) {
        boolean flag = false;
        File filesDir = mContext.getFilesDir();
        imageUp = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageUp);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            flag = true;
        } catch (Exception e) {
            flag = false;
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return  flag;
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
