package com.likesby.bludoc.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment;
import com.commit451.modalbottomsheetdialogfragment.Option;
import com.commit451.modalbottomsheetdialogfragment.OptionRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.likesby.bludoc.Adapter.BottomSheetAdapter;
import com.likesby.bludoc.Adapter.Pres_LabTest_adapter_mobile;
import com.likesby.bludoc.Adapter.Pres_adapter_mobile;
import com.likesby.bludoc.Adapter.SlidingImage_Adapter_GeneratedPrescription;
import com.likesby.bludoc.AllPharmacistActivity;
import com.likesby.bludoc.BuildConfig;
import com.likesby.bludoc.CreateAppointmentActivity;
import com.likesby.bludoc.ModelLayer.Entities.BottomSheetItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.PrescriptionJSON;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItems;
import com.likesby.bludoc.ModelLayer.NewEntities.MedicineItem;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.Doctor;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.MultiplePharmacistActivity;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.TemplateSelection;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.databinding.GeneratePrescriptionBinding;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.utils.ScreenSize;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.likesby.bludoc.Fragment.CreatePrescription.backCheckerFlag;
import static com.likesby.bludoc.Fragment.CreatePrescription.certificate_selection;

public class GeneratePres extends Fragment implements ModalBottomSheetDialogFragment.Listener, GeneratedPresBottomFragment.onClickListener {
    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> medicinesItemArrayListO = new ArrayList<>();
    Button generatePDF, back, btn_backbtn_edit_profile;
    Context mContext;
    Dialog dialog;
    Dialog dialog_data;
    CoordinatorLayout coordinator_layout;
    LinearLayout ll_menu;
    View v;
    LinearLayout ll_invite_member;
    static RecyclerView rView, rViewlabtest;
    TextView textView_advice;
    static LinearLayoutManager lLayout, lLayout2;
    ArrayList<LabTestItem> labTestItem = new ArrayList<>();
    ArrayList<LabTestItem> labTestItemTEMP = new ArrayList<>();
    PrescriptionItem prescriptionItem = new PrescriptionItem();
    PrescriptionItem prescriptionItemTEMP = new PrescriptionItem();
    ImageView image_layout, signature;
    TextView sig_text, title, textView_Clinic_name, textView_DocName, textView_degree, textView_res_num, textView_des;
    TextView textView_UID, textView_pat_name, textView_age, textView_days, textView_diagnosis,
            textView_treatment_advice, textView_findings, textView_history, textView_chief_complaint;
    TextView textView_date, textView_add, textView_time, textView_contact, textView_email;
    SessionManager manager;
    LinearLayout scrollview_edit_profile;
    NestedScrollView nestedScrollView;
    String definer, diagnosis_desc = "", end_note = "", definerTEMP, end_noteTEMP = "";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    private static final String TAG = "generatePres___";
    private static String FILE = "mnt/sdcard/invoice.pdf"; // add permission in your manifest...
    static Image image;
    FrameLayout fl_progress_bar;
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean count;
    BottomSheetBehavior behavior;
    public static PrescriptionItem patient_item = new PrescriptionItem();
    private static ArrayList<Uri> filesGlobal;
    TextView textView_end_note;
    int currentPage = 0;
    int DELAY_TIME = 2000;
    int DELAY_TIME_MULTIPLIER = 5000;
    ImageView fl_medicines_symbol;
    TextView textView_medical_cert_desc, page_no;
    TextView textView_medical_cert;
    int page_no_count = 1;
    Bundle bundle;
    LinearLayout ll_patient_name;
    ArrayList<String> stringArrayDESC;
    int width = 480;
    boolean value;
    int i = 0;
    Dialog dialog_dataShareMenu;

    private GeneratePrescriptionBinding binding;
    private String yesOrNo;
    private BottomSheetAdapter mAdapter;
    private ArrayList<AbstractViewRenderer> pages = new ArrayList();
    FrameLayout fl_main;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    private boolean isVisible = true;
    private boolean isFooterVisible = true;
    private boolean isClinicNameFound = false;
    private FragmentActivity fragmentActivity;
    private boolean isClinicVisible = true;
    private String prescriptionId;
    private String dateValueString;
    private String dateWithName;
    private ArrayList<com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem> medicinesInEnglish;
    private ArrayList<LabTestItem> labTestItemInEnglish;
    private boolean isSendPharmacy = true;
    private String message;
    private String isCertificate;
    private boolean isCertificateAvail;
    private boolean isBackChecked = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
        if (manager.contains(mContext, "show_banner_ad")) {
            adRequest = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
            initInterstitialAd(adRequest);
            if (manager.contains(mContext, "ad_count") && manager.getPreferences(mContext, "ad_count").equals("0")) {
                rewardedAd = createAndLoadRewardedAd();
            }
        }

        fragmentActivity = (FragmentActivity) mContext;

    }

    public RewardedAd createAndLoadRewardedAd() {
           rewardedAd = new RewardedAd(mContext,"ca-app-pub-9225891557304181/7053195953");// Live ad credentials
        //rewardedAd = new RewardedAd(mContext, "ca-app-pub-3940256099942544/5224354917");// Test ad credentials

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.e("Rewarded", "onRewardedAdLoaded");


            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Log.e("Rewarded", "onRewardedAdFailedToLoad");
                // Toast.makeText(mContext, "RewardedAd FailedToLoad", Toast.LENGTH_SHORT).show();


            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        // rewardedAd.loadAd(new PublisherAdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public void runTimePermission() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide storage permission for better reach ......";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(fragmentActivity/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });

    }


    private void initInterstitialAd(AdRequest adRequest) {

        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-9225891557304181/3105393849");//Live
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//Test
        mInterstitialAd.loadAd(this.adRequest);

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

    }

    public void setViewInInches(float width, float height, View v) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthInches = Math.round(width * 300);
        int heightInches = Math.round(height * 300);
        Log.e("WH", "________________ " + widthInches + " : " + heightInches);

        v.setLayoutParams(new CoordinatorLayout.LayoutParams(widthInches, heightInches));
        v.requestLayout();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.generate_prescription, container, false);
        binding = GeneratePrescriptionBinding.inflate(inflater, container, false);

        v = binding.getRoot();
        //

        width = ScreenSize.getDimensions(mContext)[0];
        initCalls(v);

        if (manager.getPreferences(mContext, "first_time").equals("")) {

            binding.newText.setVisibility(View.VISIBLE);
            manager.setPreferences(mContext, "first_time", "false");

        } else {

            binding.newText.setVisibility(View.GONE);

        }

       /* v.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //do something
                    if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                    {
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                }
                return true;
            }
        });*/

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    CreatePrescription.backCheckerFlag = isBackChecked;
                    FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                    fm.popBackStackImmediate();

                     /*CreatePrescription myFragment = new CreatePrescription();
                    Bundle bundle = new Bundle();
                    bundle.putString("definer","temp");
                    myFragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.homePageContainer,
                            myFragment, "first").addToBackStack(null).commit();*/
                    return true;
                }
                return false;
            }
        });

        runTimePermission();

        return binding.getRoot();
    }

    private void openBusinessWhatsUpAndShare(File pdfWithMultipleImage) {

        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp.w4b");
        if (isWhatsappInstalled) {
            //======================================================================
            PackageManager packageManager = mContext.getPackageManager();
            // Intent sendIntent = new Intent(Intent.ACTION_VIEW);

            try {
                ArrayList<Uri> files = GeneratePres.getFiles();

                String smsNumber = "9399104906";
                smsNumber = smsNumber.replace("+", "").trim(); // E164 format without '+' sign
                if (!(smsNumber.contains("91")))
                    smsNumber = "91" + smsNumber;
                Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                sendIntent.setType("application/pdf");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Dear " + GeneratePres.patient_item.getPName() + ", " + manager.getPreferences(mContext, "name") + " has sent you an E-prescription / Certificate via BluDoc");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pdfWithMultipleImage.getPath()));
                sendIntent.putExtra("jid", "" + smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                sendIntent.setPackage("com.whatsapp.w4b");
                if (sendIntent.resolveActivity(mContext.getPackageManager()) == null) {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sendIntent.resolveActivity(packageManager) != null) {
                    mContext.startActivity(sendIntent);
                } else
                    Toast.makeText(mContext, "Resolve activity Null", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else
            Toast.makeText(mContext, "WhatsApp Not installed", Toast.LENGTH_SHORT).show();

    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return app_installed;
    }

    public void sendIdAndPresIdOnServer(String keywords, String keywordsMultiple) {

        if (Utils.isConnectingToInternet(mContext)) {

            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Sending to " + keywordsMultiple);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.sendPresciption(manager.getPreferences(mContext, "doctor_id"), prescriptionId, keywords);

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    progressDialog.dismiss();
                    if (jsonResponse != null && jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                        Toast.makeText(mContext, "Sent Successfully", Toast.LENGTH_SHORT).show();

                    } else if (jsonResponse != null && jsonResponse.getMessage().toLowerCase().equalsIgnoreCase("no data available")) {
                        Toast.makeText(mContext, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        message = jsonResponse.getMessage();
                    } else if (jsonResponse != null) {
                        Toast.makeText(mContext, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        message = jsonResponse.getMessage();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupBottomMenu() {

        if (dialog_data != null)
            dialog_data.dismiss();

        dialog_dataShareMenu = new Dialog(mContext);
        dialog_dataShareMenu.setCancelable(false);
        dialog_dataShareMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_dataShareMenu.getWindow()).setGravity(Gravity.CENTER);
        dialog_dataShareMenu.setContentView(R.layout.popup_share_menu);
        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_dataShareMenu.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp_number_picker);

        TextView __bottom_sheet_name = dialog_dataShareMenu.findViewById(R.id.__bottom_sheet_name);
        TextView open_via_page_pdf = dialog_dataShareMenu.findViewById(R.id.open_via_page_pdf);
        TextView send_to = dialog_dataShareMenu.findViewById(R.id.send_to);

        open_via_page_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdapter != null) {

                    Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
                    File fileWithinMyDir = new File(mAdapter.getPdfViaApps());
                    Uri bmpUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setDataAndType(bmpUri, "application/pdf");
                        intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(Intent.createChooser(intentShareFile, "Open File Using..."));

                    }
                }

            }
        });

        RecyclerView recyclerView = dialog_dataShareMenu.findViewById(R.id.recyclerView_bottom_sheet);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                3,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView.setLayoutManager(gridLayoutManagerr);
        ArrayList<BottomSheetItem> bottomSheetItemArrayList = new ArrayList<>();
        BottomSheetItem bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("1");
        bottomSheetItem.setMenuName("View Pdf");
        bottomSheetItem.setMenuImage("mail");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("2");
        bottomSheetItem.setMenuName("Share");
        bottomSheetItem.setMenuImage("ic_download_");

        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("3");
        bottomSheetItem.setMenuName("Download");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("4");
        bottomSheetItem.setMenuName("Email");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("5");
        bottomSheetItem.setMenuName("WhatsApp");
        bottomSheetItem.setMenuImage("whatsapp");
        bottomSheetItemArrayList.add(bottomSheetItem);

        mAdapter = new BottomSheetAdapter(mContext, bottomSheetItemArrayList, fl_progress_bar, GeneratePres.this, null, prescriptionItem);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setPatientName(prescriptionItem.getPName());

        mAdapter.setOnClickListener(new BottomSheetAdapter.onClickListener() {
            @Override
            public void onClick(int i) {

                if (i == 0) {

                    if (mAdapter != null) {

                        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
                        File fileWithinMyDir = new File(mAdapter.getPdfViaApps());
                        Uri bmpUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
                        if (fileWithinMyDir.exists()) {
                            intentShareFile.setDataAndType(bmpUri, "application/pdf");
                            intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(Intent.createChooser(intentShareFile, "Open File Using..."));

                        }
                    }

                }

            }
        });

        RecyclerView recyclerView_bottom_sheet_send_to = dialog_dataShareMenu.findViewById(R.id.recyclerView_bottom_sheet_send_to);

        if (isCertificateAvail) {
            recyclerView_bottom_sheet_send_to.setVisibility(View.GONE);
            send_to.setVisibility(View.GONE);
        }

        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerSendTo = new GridLayoutManager(mContext,
                3,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView_bottom_sheet_send_to.setLayoutManager(gridLayoutManagerSendTo);

        recyclerView.setLayoutManager(gridLayoutManagerr);
        ArrayList<BottomSheetItem> bottomSheetItemArrayListSendTo = new ArrayList<>();
        BottomSheetItem bottomSheetItemSendTo = new BottomSheetItem();
        bottomSheetItemSendTo.setMenuId("1");
        bottomSheetItemSendTo.setMenuName("Pharmacy");
        bottomSheetItemSendTo.setMenuImage("pharmacy");
        bottomSheetItemArrayListSendTo.add(bottomSheetItemSendTo);

        bottomSheetItemSendTo = new BottomSheetItem();
        bottomSheetItemSendTo.setMenuId("2");
        bottomSheetItemSendTo.setMenuName("Path Lab");
        bottomSheetItemSendTo.setMenuImage("laboratory");
        bottomSheetItemArrayListSendTo.add(bottomSheetItemSendTo);

        bottomSheetItemSendTo = new BottomSheetItem();
        bottomSheetItemSendTo.setMenuId("3");
        bottomSheetItemSendTo.setMenuName("Imaging Centre");
        bottomSheetItemSendTo.setMenuImage("imaging_center");
        bottomSheetItemArrayListSendTo.add(bottomSheetItemSendTo);

        bottomSheetItemSendTo = new BottomSheetItem();
        bottomSheetItemSendTo.setMenuId("4");
        bottomSheetItemSendTo.setMenuName("Hospital");
        bottomSheetItemSendTo.setMenuImage("hospital");
        bottomSheetItemArrayListSendTo.add(bottomSheetItemSendTo);

        bottomSheetItemSendTo = new BottomSheetItem();
        bottomSheetItemSendTo.setMenuId("5");
        bottomSheetItemSendTo.setMenuName("Doctor");
        bottomSheetItemSendTo.setMenuImage("doctor");
        bottomSheetItemArrayListSendTo.add(bottomSheetItemSendTo);

        BottomSheetAdapter mAdapterSendTo = new BottomSheetAdapter(mContext, bottomSheetItemArrayListSendTo, fl_progress_bar, GeneratePres.this, null, prescriptionItem);
        recyclerView_bottom_sheet_send_to.setAdapter(mAdapterSendTo);

        mAdapterSendTo.setPatientName(prescriptionItem.getPName());
        mAdapterSendTo.setSendTo(true);

        mAdapterSendTo.setOnClickListener(new BottomSheetAdapter.onClickListener() {
            @Override
            public void onClick(int i) {

                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                    if (!("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        if (responseProfileDetails.getAccess().equals("Approve")) {

                            String sub_valid = "", premium_valid = "";
                            boolean flag_reset_paid = false;
                            Date date1 = null, date2 = null;
                            int days_left_free = 0, days_left_paid = 0;
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
                            if (profileDetails.getSubcriptions() != null) {
                                if (profileDetails.getSubcriptions().size() != 0) {
                                    for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                        if (!si.getHospital_code().equals("")) {
                                            try {
                                                Calendar c2 = Calendar.getInstance();
                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                                Date dateEnd = dateFormat.parse(si.getEnd());
                                                Date c = Calendar.getInstance().getTime();
                                                assert dateEnd != null;

                                                //premium_valid = si.getDays();
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                                // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                                // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                                try {
                                                    date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                    date2 = dateFormat.parse(si.getEnd());
                                                    Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");

                                                    String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                    days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                    // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                    // break;
                                                    sub_valid = si.getEnd();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                            } catch (ParseException pe) {
                                                // handle the failure
                                            }

                                        }
                                    }

                                } else {
                                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                    return;
                                }
                            } else {
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                return;
                            }

                            if (days_left_free < 0) {
                                popupPaused("Your subscription has expired. Please contact to hospital  admin");
                                return;
                            } else {

                                handleClickListener(i);

                            }
//Now run it
                        } else if (responseProfileDetails.getAccess().equals("Pending")) {
                            popupPaused("Your account is pending for approval. Please contact " + responseProfileDetails.getHospital_name());

                        } else if (responseProfileDetails.getAccess().equals("Remove")) {
                            popupPaused("You have been removed from " + responseProfileDetails.getHospital_name() + ". You now have accesses like BluDoc standard user");
                            //popupHospitalCode();
                        } else if (responseProfileDetails.getAccess().equals("Pause")) {

                            popupPaused("Your subscription is paused. Please contact to " + responseProfileDetails.getHospital_name());

                        } else {
                            popupAccess();
                        }

                    } else {

                        checkSubsciptionForLabTest(i);

                    }
                } else {
                    popup();
                }

            }
        });

        ImageView iv_close = dialog_dataShareMenu.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_dataShareMenu.dismiss();

                isVisible = true;
                isFooterVisible = true;
                isClinicNameFound = false;
                isClinicVisible = true;

                Bundle bundle = new Bundle();
                bundle.putParcelable("defaultAttributeId", prescriptionItem);
                bundle.putParcelableArrayList("defaultAttributeIdLabTest", labTestItem);
                bundle.putString("definer", definer);
                bundle.putString("end_note", end_note);
                bundle.putString("yesOrNo", yesOrNo);

                setArguments(bundle);

                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .detach(GeneratePres.this)
                        .attach(GeneratePres.this)
                        .commit();

                /*assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();*/
            }
        });

        dialog_dataShareMenu.show();
    }

    private void popupPaused(String keywords) {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment= new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        popUpSubscriptionDialogFragment.show(fragmentActivity.getSupportFragmentManager(),"");

    }

    private void popupAccess() {

        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_access);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_add = dialog_data.findViewById(R.id.btn_register);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }


    private void checkSubsciptionForLabTest(int i) {

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
        if (profileDetails.getSubcriptions() != null) {
            if (profileDetails.getSubcriptions().size() != 0) {
                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                    if (!si.getHospital_code().equals("")) {
                        boolean flag_reset_free;
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;

                            //premium_valid = si.getDays();
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                            try {
                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                date2 = dateFormat.parse(si.getEnd());
                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                flag_reset_free = true;

                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                // break;
                                sub_valid = si.getEnd();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                        } catch (ParseException pe) {
                            // handle the failure
                            flag_reset_free = false;
                        }

                    }
                }

            } else {
                popupPausedForChecking();
                return;

            }
        } else {
            popupPausedForChecking();
            return;
        }

        if (days_left_free < 0) {
            popupPausedForChecking();
        } else {

            handleClickListener(i);

        }

    }

    private void handleClickListener(int i) {

        Intent intent = new Intent(mContext, MultiplePharmacistActivity.class);

        if (i == 0) {

            intent.putExtra("keywords", "Pharmacy");

        } else if (i == 1) {

            intent.putExtra("keywords", "Path Lab");

        } else if (i == 2) {

            intent.putExtra("keywords", "Imaging Centre");

        } else if (i == 3) {

            intent.putExtra("keywords", "Hospital");

        } else if (i == 4) {

            intent.putExtra("keywords", "Doctor");

        }

        startActivityForResult(intent, 510);

    }

    public Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view

        view.setDrawingCacheEnabled(true);

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        return returnedBitmap;
    }

    private void popupCreatingPrescription() {
        if (dialog_data != null && dialog_data.isShowing())
            dialog_data.dismiss();

        dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(false);
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_generating_prescription);
        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp_number_picker);
        dialog_data.show();
    }


    private void initCalls(View view) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null); //RelativeLayout is root view of my UI(xml) file.
        root.setDrawingCacheEnabled(true);

        //lLayout = new LinearLayoutManager(mContext);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        lLayout2 = new LinearLayoutManager(mContext);

        ll_invite_member = view.findViewById(R.id.ll_invite_member);
        fl_main = view.findViewById(R.id.fl_main);
        //  fl_main.setLayoutParams(new FrameLayout.LayoutParams(1800,2300));
        coordinator_layout = view.findViewById(R.id.coordinator_layout);

        rView = view.findViewById(R.id.pres_recycler);
        rView.setLayoutManager(gridLayoutManagerr);
        ll_patient_name = view.findViewById(R.id.ll_patient_name);
        textView_medical_cert = view.findViewById(R.id.textView_medical_certificate);
        textView_medical_cert_desc = view.findViewById(R.id.textView_medical_cert_desc);
        textView_medical_cert.setPaintFlags(textView_medical_cert.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fl_medicines_symbol = view.findViewById(R.id.fl_medicines_symbol);
        page_no = view.findViewById(R.id.page_no);
        textView_end_note = view.findViewById(R.id.textView_end_note);
        rViewlabtest = view.findViewById(R.id.pres_recycler_labtest);
        textView_advice = view.findViewById(R.id.textView_advice);
        rViewlabtest.setLayoutManager(lLayout2);

        back = view.findViewById(R.id.btn_back_edit_profile);
        btn_backbtn_edit_profile = view.findViewById(R.id.btn_backbtn_edit_profile);
        image_layout = view.findViewById(R.id.image_layout);
        signature = view.findViewById(R.id.iv_signature);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        textView_Clinic_name = view.findViewById(R.id.textView_Clinic_name);
        textView_DocName = view.findViewById(R.id.textView_DocName);
        textView_degree = view.findViewById(R.id.textView_degree);
        textView_res_num = view.findViewById(R.id.textView_res_num);
        textView_des = view.findViewById(R.id.textView_des);
        textView_UID = view.findViewById(R.id.textView_UID);
        textView_pat_name = view.findViewById(R.id.textView_pat_name);

        textView_age = view.findViewById(R.id.textView_age);
        textView_diagnosis = view.findViewById(R.id.textView_diag);
        textView_chief_complaint = view.findViewById(R.id.textView_chief_complaint);
        textView_history = view.findViewById(R.id.textView_history);
        textView_findings = view.findViewById(R.id.textView_findings);
        textView_treatment_advice = view.findViewById(R.id.textView_treatment_advice);


        textView_days = view.findViewById(R.id.textView_days);
        textView_date = view.findViewById(R.id.textView_date);
        textView_add = view.findViewById(R.id.textView_add);
        textView_time = view.findViewById(R.id.textView_time);
        textView_contact = view.findViewById(R.id.textView_contact);
        scrollview_edit_profile = view.findViewById(R.id.saveBitmapFromImage);
        nestedScrollView = view.findViewById(R.id.scrollview_edit_profile);
        //  textView_closed_day=  view.findViewById(R.id.textView_closed_day);
        textView_email = view.findViewById(R.id.textView_email);
        title = view.findViewById(R.id.title);
        generatePDF = view.findViewById(R.id.generatePDF);
        sig_text = view.findViewById(R.id.sig_text);

        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneratedPresBottomFragment generatedPresBottomFragment = new GeneratedPresBottomFragment();

                binding.newText.setVisibility(View.GONE);

                Bundle bundle = new Bundle();
                bundle.putBoolean("isDoctorDetails", isVisible);
                bundle.putBoolean("isClinic", isClinicVisible);
                bundle.putBoolean("isFooter", isFooterVisible);
                bundle.putBoolean("isClinicFound", isClinicNameFound);

                generatedPresBottomFragment.setArguments(bundle);

                generatedPresBottomFragment.show(fragmentActivity.getSupportFragmentManager(), "generatedBottomFragment");
                generatedPresBottomFragment.SetOnClickListener(GeneratePres.this);


            }
        });

        bundle = getArguments();
        if (bundle != null) {
            Log.e("prescriptionItem", "________________" + prescriptionItem.toString());
            prescriptionItem = bundle.getParcelable("defaultAttributeId");
            labTestItem = bundle.getParcelableArrayList("defaultAttributeIdLabTest");
            definer = bundle.getString("definer");
            end_note = bundle.getString("end_note");
            isCertificate = bundle.getString("isCertificate", "");
            prescriptionId = bundle.getString("pres_id", "");
            isBackChecked = bundle.getBoolean("isBackChecked", true);
            if (bundle.getString(yesOrNo) != null) {
                yesOrNo = bundle.getString("yesOrNo");

            }

            prescriptionItemTEMP = prescriptionItem;
            labTestItemTEMP = labTestItem;
            definerTEMP = definer;
            end_noteTEMP = end_note;

            if (!value) {

                medicinesInEnglish = prescriptionItem.getMedicines();
                labTestItemInEnglish = labTestItem;

            }

            initViews();

            generatePDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkPermission()) {
                                // GenratePres();
                                popupSelection();
                            } else {
                                requestPermission(); // Code for permission
                            }
                        } else {
                            // GenratePres();
                            popupSelection();
                        }
                    }

                }
            });

           /* rView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2000));*/
           /* int line = 0;
            String[] lines = null;
            String[] details = diagnosis_desc.split(Pattern.quote("|"));

            for (String strr : details
            ) {
                line = line + strr.length();
            }
            Toast.makeText(mContext, ""+lines, Toast.LENGTH_SHORT).show();
            if(line>300){
                rView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2000));
            }
            else {
                rView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2300));
            }*/

        }

       /* ll_invite_member.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2300));*/

        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2500);


        fl_main.setLayoutParams(params);*/

      /*  rView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2500));*/
        initViewHolder();
        onclick();
        BottomSheet(view);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 510 && data != null) {

                String result = data.getStringExtra("arraylist_of_details");
                String keywordsMultiple = data.getStringExtra("keywordsMultiple");
                sendIdAndPresIdOnServer(result, keywordsMultiple);

            }

        }

    }

    private void popupSelection() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_selection);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.MATCH_PARENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_mobile = dialog_data.findViewById(R.id.btn_mobile);
        Button btn_a4_size = dialog_data.findViewById(R.id.btn_a_size);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                if (manager.contains(mContext, "show_banner_ad")) {
                    if (manager.contains(mContext, "ad_show_time")) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
                        Log.e("DIFF", "ad_show_time ____________________ " + DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "ad_show_time"), DateUtils.currentDateTime()));

                        if (DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "ad_show_time"), DateUtils.currentDateTime()) > 10) {

                            if (manager.contains(mContext, "ad_count")) {
                                if (manager.getPreferences(mContext, "ad_count").equals("0")) {
                                    if (rewardedAd != null && rewardedAd.isLoaded()) {
                                        manager.setPreferences(mContext, "ad_count", "5");


                                        rewardedAd.show(getActivity(), new RewardedAdCallback() {
                                            @Override
                                            public void onRewardedAdOpened() {
                                                super.onRewardedAdOpened();
                                            }

                                            @Override
                                            public void onRewardedAdClosed() {
                                                super.onRewardedAdClosed();
                                                // rewardedAd = createAndLoadRewardedAd();
                                            }

                                            @Override
                                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                                // rewardedAd = createAndLoadRewardedAd();
                                                //   Toast.makeText(mContext, "Congratulations!! You won "+rewardItem.getAmount()+" "+rewardItem.getType(), Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onRewardedAdFailedToShow(int i) {
                                                super.onRewardedAdFailedToShow(i);
                                            }
                                        });
                                    } else {
                                        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                            mInterstitialAd.show();
                                    }
                                } else {
                                    int count = Integer.parseInt(manager.getPreferences(mContext, "ad_count"));
                                    manager.setPreferences(mContext, "ad_count", "" + (count - 1));
                                    if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                        mInterstitialAd.show();
                                }
                            } else {
                                manager.setPreferences(mContext, "ad_count", "5");
                                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                            }
                        } else {
                            if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                mInterstitialAd.show();
                        }
                    } else {
                        manager.setPreferences(mContext, "ad_count", "5");
                        manager.setPreferences(mContext, "ad_show_time", DateUtils.currentDateTime());
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                    }
                }

                GenratePres();
            }
        });

        btn_a4_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPausedForChecking();
                                return;

                            }
                        } else {
                            popupPausedForChecking();
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPausedForChecking();
                        } else {

                            aFourSizeManaged();

                        }

                    }
                } else {
                    popup();
                }

            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }

    private void aFourSizeManaged() {

        if(dialog_data!=null)
        dialog_data.dismiss();

        if (manager.contains(mContext, "show_banner_ad")) {
            if (manager.contains(mContext, "ad_show_time")) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
                Log.e("DIFF", "ad_show_time ____________________ " + DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "ad_show_time"), DateUtils.currentDateTime()));

                if (DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "ad_show_time"), DateUtils.currentDateTime()) > 10) {

                    if (manager.contains(mContext, "ad_count")) {
                        if (manager.getPreferences(mContext, "ad_count").equals("0")) {
                            if (rewardedAd != null && rewardedAd.isLoaded()) {
                                manager.setPreferences(mContext, "ad_count", "5");


                                rewardedAd.show(getActivity(), new RewardedAdCallback() {
                                    @Override
                                    public void onRewardedAdOpened() {
                                        super.onRewardedAdOpened();
                                    }

                                    @Override
                                    public void onRewardedAdClosed() {
                                        super.onRewardedAdClosed();
                                        // rewardedAd = createAndLoadRewardedAd();
                                    }

                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        // rewardedAd = createAndLoadRewardedAd();
                                        //   Toast.makeText(mContext, "Congratulations!! You won "+rewardItem.getAmount()+" "+rewardItem.getType(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onRewardedAdFailedToShow(int i) {
                                        super.onRewardedAdFailedToShow(i);
                                    }
                                });
                            } else {
                                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                    mInterstitialAd.show();
                            }
                        } else {
                            int count = Integer.parseInt(manager.getPreferences(mContext, "ad_count"));
                            manager.setPreferences(mContext, "ad_count", "" + (count - 1));
                            if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                                mInterstitialAd.show();
                        }
                    } else {
                        manager.setPreferences(mContext, "ad_count", "5");
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                            mInterstitialAd.show();
                    }
                } else {
                    if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                        mInterstitialAd.show();
                }
            } else {
                manager.setPreferences(mContext, "ad_count", "5");
                manager.setPreferences(mContext, "ad_show_time", DateUtils.currentDateTime());
                if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
            }
        }

        generatePDF.setVisibility(View.GONE);
        GeneratePresNew myFragment = new GeneratePresNew();

        if (binding.topDoctorDetails.getVisibility() == View.VISIBLE)
            bundle.putString("top_doctor_details", "true");
        else
            bundle.putString("top_doctor_details", "false");

        if (binding.footerDocterDetails.getVisibility() == View.VISIBLE)
            bundle.putString("footer_doctor_details", "true");
        else
            bundle.putString("footer_doctor_details", "false");

        if (binding.textViewClinicName.getVisibility() == View.VISIBLE)
            bundle.putString("clinic_name", "true");
        else
            bundle.putString("clinic_name", "false");

        bundle.putBoolean("isBackChecked", isBackChecked);
        bundle.putString("dateString", dateWithName);
        bundle.putString("dateStringWithNum", dateValueString);
        bundle.putString("definer", definer);
        bundle.putString("presId", prescriptionId);
        bundle.putBoolean("certificate_selection", certificate_selection);

        myFragment.setArguments(bundle);

        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();

        myFragment.setOnClickListener(new GeneratePresNew.onClickListener() {
            @Override
            public void onClick() {

                fragmentActivity.getSupportFragmentManager().popBackStack();
                CreatePrescription.backCheckerFlag = isBackChecked;
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                fm.popBackStackImmediate();
            }
        });

    }

    private void popup() {

        final Dialog dialog_data = new Dialog(fragmentActivity);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.feedback);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_data.dismiss();

            }
        });

        if (!fragmentActivity.isFinishing())
            dialog_data.show();
    }

    private void popupPausedForChecking() {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment = new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords", "");
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(fragmentActivity.getSupportFragmentManager(), "");

    }

    private void BottomSheet(View view) {
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_bottom_sheet);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                3,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView.setLayoutManager(gridLayoutManagerr);
        ArrayList<BottomSheetItem> bottomSheetItemArrayList = new ArrayList<>();
        BottomSheetItem bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("1");
        bottomSheetItem.setMenuName("View Pdf");
        bottomSheetItem.setMenuImage("mail");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("2");
        bottomSheetItem.setMenuName("Share");
        bottomSheetItem.setMenuImage("ic_download_");

        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("3");
        bottomSheetItem.setMenuName("Download");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("4");
        bottomSheetItem.setMenuName("Send to pharmacy");
        bottomSheetItem.setMenuImage("whatsapp");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("3");
        bottomSheetItem.setMenuName("Email");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("4");
        bottomSheetItem.setMenuName("WhatsApp");
        bottomSheetItem.setMenuImage("whatsapp");
        bottomSheetItemArrayList.add(bottomSheetItem);

        mAdapter = new BottomSheetAdapter(mContext, bottomSheetItemArrayList, fl_progress_bar, GeneratePres.this, null, prescriptionItem);
        recyclerView.setAdapter(mAdapter);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //   behavior.setPeekHeight(200);
        //behavior.setPeekHeight(72);
        //Load animation
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        // Start animation

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                /*if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else if(newState==BottomSheetBehavior.PEEK_HEIGHT_AUTO)
                {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }*/
                /*else
                {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }*/

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // iv_switch.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));

                        // iv_switch.startAnimation(slide_down);

                        // popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            //  iv_switch.setVisibility(View.GONE);
                        } else {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            //  iv_switch.setVisibility(View.VISIBLE);
                        }
                    case BottomSheetBehavior.STATE_SETTLING:
                        //behavior.setHideable(false);
                        break;

                    case BottomSheetBehavior.STATE_HALF_EXPANDED:

                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //  Log.e(TAG,"slideOffset = "+slideOffset);
            }
        });


    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String pName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, pName + "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    private void requestPermission() {
        /*if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(mContext, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {*/
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // GenratePres();
                    popupSelection();
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void onclick() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePrescription.backCheckerFlag = isBackChecked;
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        btn_backbtn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePrescription.backCheckerFlag = isBackChecked;
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fm.popBackStack();

            }
        });

        if (("history").equalsIgnoreCase(definer)) {
            generatePDF.setText("Share");
            back.setText("Back");
        } else {
            generatePDF.setText("Approve & Share");
            back.setText("Edit");
        }
    }

    public void GenratePres() {
        // setViewInInches(9f, 11.7f, fl_main);

        if (("history").equalsIgnoreCase(definer)) {
//                textView_medical_cert.setVisibility(View.GONE);
//                textView_medical_cert_desc.setVisibility(View.GONE);

            genInv();


        } else {
            if (!count) {

                ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                PrescriptionJSON prescriptionJSON = new PrescriptionJSON();
                prescriptionJSON.setPatientId(prescriptionItem.getPatientId());
                prescriptionJSON.setHospitalCode(responseProfileDetails.getHospitalCode());
                prescriptionJSON.setDoctorId(manager.getPreferences(mContext, "doctor_id"));
                if (prescriptionItem.getMedicines() != null) {
                    if (prescriptionItem.getMedicines().size() != 0) {
                        medicinesItemArrayListO = new ArrayList<>();
                        for (com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mi : prescriptionItem.getMedicines()) {
                            com.likesby.bludoc.ModelLayer.Entities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.Entities.MedicinesItem();
                            mii.setMedicineName(mi.getMedicineName());
                            mii.setMedicineId(mi.getPresbMedicineId());
                            mii.setAdditionaComment(mi.getAdditionaComment());
                            mii.setQty(mi.getQty());
                            mii.setFrequency(mi.getFrequency());
                            mii.setInstruction(mi.getInstruction());
                            mii.setNoOfDays(mi.getNoOfDays());
                            mii.setRoute(mi.getRoute());
                            medicinesItemArrayListO.add(mii);
                        }
                    }
                }

                prescriptionJSON.setMedicines(medicinesItemArrayListO);
                prescriptionJSON.setDiagnosis(prescriptionItem.getDiagnosis());

                if (certificate_selection)
                    prescriptionJSON.setIsCertificate("yes");
                else
                    prescriptionJSON.setIsCertificate("no");


                if (TextUtils.isEmpty(dateValueString)) {

                    Calendar myCalendar = Calendar.getInstance();
                    int year = myCalendar.get(Calendar.YEAR);
                    int month = myCalendar.get(Calendar.MONTH);
                    int DAY_OF_MONTH = myCalendar.get(Calendar.DAY_OF_MONTH);

                    prescriptionJSON.setDate(year + "-" + (month + 1) + "-" + DAY_OF_MONTH);

                } else
                    prescriptionJSON.setDate(dateValueString);

                prescriptionJSON.setEndNote(end_note);
                if (labTestItem != null) {
                    if (labTestItem.size() != 0) {
                        prescriptionJSON.setLabTest(labTestItem);
                    }
                }
                Gson gson = new Gson();
                String json = gson.toJson(prescriptionJSON);
                fl_progress_bar.setVisibility(View.VISIBLE);
               /* if (prescriptionItem.getMedicines() != null)
                    if (prescriptionItem.getMedicines().size() > 3) {
                        popupCreatingPrescription();
                    } else if (labTestItem != null)
                        if (labTestItem.size() > 3) {

                        }*/

                popupCreatingPrescription();
                apiViewHolder.Prescription(json)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responsePrescription);

            } else {
                genInv();
            }
        }
    }

    public static ArrayList<Uri> getFiles() {
        return filesGlobal;
    }


    public void genInv() {

        popupCreatingPrescription();
        rViewlabtest.setVisibility(View.GONE);
        textView_advice.setVisibility(View.GONE);
        try {
            count = true;
            boolean checker = false;
            generatePDF.setText("Share");
            back.setVisibility(View.GONE);
            btn_backbtn_edit_profile.setVisibility(View.VISIBLE);
            try {
                final ArrayList<Uri> files = new ArrayList<Uri>();
                final int[] p = {0};
                int line = 0;
                String[] lines = null;
                String[] details = diagnosis_desc.split(Pattern.quote("|"));

                for (String strr : details
                ) {
                    line = line + strr.length();
                }
                // Toast.makeText(mContext, "Lines = "+line, Toast.LENGTH_SHORT).show();
                int field_active = 0;
                for (String str : details) {
                    if (!str.trim().equals(""))
                        field_active = field_active + 1;
                }
                textView_end_note.setVisibility(View.GONE);

                Log.e(TAG, "------------------------------------   LINE   === " + line);


                if (line > 3000) {
                    checker = true;
//                    rView.setVisibility(View.GONE);
                    rViewlabtest.setVisibility(View.GONE);
                    fl_medicines_symbol.setVisibility(View.GONE);
                    textView_advice.setVisibility(View.GONE);
                    textView_end_note.setVisibility(View.GONE);

                    if (prescriptionItem.getMedicines() == null) {
                        rViewlabtest.setVisibility(View.VISIBLE);
                        textView_advice.setVisibility(View.VISIBLE);
                        textView_end_note.setVisibility(View.VISIBLE);
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                            // Do something after 4s = 4000ms

                            page_no.setText("Page No. " + page_no_count);
                            Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                            files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                            filesGlobal = files;
                            page_no_count = page_no_count + 1;
                            textView_chief_complaint.setVisibility(View.GONE);
                            textView_history.setVisibility(View.GONE);
                            textView_findings.setVisibility(View.GONE);
                            textView_treatment_advice.setVisibility(View.GONE);
                            textView_diagnosis.setVisibility(View.GONE);
                            rView.setVisibility(View.VISIBLE);
                            binding.textviewTemprature.setVisibility(View.GONE);
                            binding.textviewHeight.setVisibility(View.GONE);
                            binding.textviewWeight.setVisibility(View.GONE);
                            binding.textviewPulse.setVisibility(View.GONE);
                            binding.textviewBloodPressure.setVisibility(View.GONE);
                            binding.textviewBloodSugar.setVisibility(View.GONE);
                            binding.textviewHemoglobing.setVisibility(View.GONE);
                            binding.textviewSpo2.setVisibility(View.GONE);
                            binding.textviewBloodGroup.setVisibility(View.GONE);
                            binding.textViewHosAddress.setVisibility(View.GONE);
                            binding.textviewRespirationRate.setVisibility(View.GONE);
                            binding.textviewAllergy.setVisibility(View.GONE);
                            fl_medicines_symbol.setVisibility(View.VISIBLE);
                            textView_end_note.setVisibility(View.GONE);
                        }
                    }, 1);
                }
                //==========================================================================
                if (prescriptionItem != null) {

                    generatePDF.setVisibility(View.GONE);
                    if (prescriptionItem.getMedicines() != null && prescriptionItem.getMedicines().size() != 0) {
                        int sizee = 8;
                                   /* if (width < 520)
                                        sizee = 5;
                                    else if (width < 820)
                                        sizee = 6;

                                    if (line == 0) {

                                        sizee = 6;
                                    }*/

                        if (prescriptionItem.getMedicines().size() >= sizee) {
                            rViewlabtest.setVisibility(View.GONE);
                            textView_advice.setVisibility(View.GONE);

                            ArrayList<MedicinesItem> medicineList = new ArrayList<>();
                            for (int i = 0; i < sizee; i++) {
                                if (prescriptionItem.getMedicines().size() > p[0]) {
                                    medicineList.add(medicinesItemArrayListO.get(p[0]));
                                    p[0] = p[0] + 1;
                                }
                            }

                            Pres_adapter_mobile templateAdapter = new Pres_adapter_mobile(medicineList, mContext);
                            rView.setAdapter(templateAdapter);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                                    // Do something after 4s = 4000ms
                                    page_no.setText("Page No. " + page_no_count);
                                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));

                                               /* if(prescriptionItem.getMedicines()!=null)
                                                    if (prescriptionItem.getMedicines().size() > 4)
                                                        page_no.setText("2/"+(prescriptionItem.getMedicines().size()/4)+1);*/

                                    patient_item = prescriptionItem;
                                    textView_chief_complaint.setVisibility(View.GONE);
                                    textView_history.setVisibility(View.GONE);
                                    textView_findings.setVisibility(View.GONE);
                                    textView_treatment_advice.setVisibility(View.GONE);
                                    textView_diagnosis.setVisibility(View.GONE);
                                    binding.textviewTemprature.setVisibility(View.GONE);
                                    binding.textviewHeight.setVisibility(View.GONE);
                                    binding.textviewWeight.setVisibility(View.GONE);
                                    binding.textviewPulse.setVisibility(View.GONE);
                                    binding.textviewBloodPressure.setVisibility(View.GONE);
                                    binding.textviewBloodSugar.setVisibility(View.GONE);
                                    binding.textviewHemoglobing.setVisibility(View.GONE);
                                    binding.textviewSpo2.setVisibility(View.GONE);
                                    binding.textviewBloodGroup.setVisibility(View.GONE);
                                    binding.textviewRespirationRate.setVisibility(View.GONE);
                                    binding.textViewHosAddress.setVisibility(View.GONE);
                                    binding.textviewAllergy.setVisibility(View.GONE);
                                    Log.e(TAG, "--------------------------------------------p[0] - 1199 = " + p[0]);

                                    temp(p[0], files);
                                    filesGlobal = files;

                                }
                            }, DELAY_TIME);

                        } else {
                                       /* if(!checker){
                                            fl_medicines_symbol.setVisibility(View.VISIBLE);
                                            if(prescriptionItem.getMedicines().size()!=0)
                                                rView.setVisibility(View.VISIBLE);
                                            page_no_count = page_no_count + 1;
                                            textView_chief_complaint.setVisibility(View.GONE);
                                            textView_history.setVisibility(View.GONE);
                                            textView_findings.setVisibility(View.GONE);
                                            textView_treatment_advice.setVisibility(View.GONE);
                                            textView_diagnosis.setVisibility(View.GONE);
                                            binding.textviewTemprature.setVisibility(View.GONE);
                                            binding.textviewHeight.setVisibility(View.GONE);
                                            binding.textviewWeight.setVisibility(View.GONE);
                                            binding.textviewPulse.setVisibility(View.GONE);
                                            binding.textviewBloodPressure.setVisibility(View.GONE);
                                            binding.textviewBloodSugar.setVisibility(View.GONE);
                                            binding.textviewHemoglobing.setVisibility(View.GONE);
                                            binding.textviewSpo2.setVisibility(View.GONE);
                                            binding.textviewBloodGroup.setVisibility(View.GONE);
                                            binding.textViewHosAddress.setVisibility(View.GONE);
                                            binding.textviewRespirationRate.setVisibility(View.GONE);
                                            binding.textviewAllergy.setVisibility(View.GONE);
                                           top_header_parent.setVisibility(View.GONE);
                                        }*/


                            textView_end_note.setVisibility(View.VISIBLE);
                            page_no.setText("Page No. " + page_no_count);

                            Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)

                            files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                            patient_item = prescriptionItem;
                            filesGlobal = files;
                                        /*Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                        intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                        intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                        Intent shareIntent = Intent.createChooser(intent, null);
                                        startActivity(shareIntent);*/
                            popupBottomMenu();
                            fl_progress_bar.setVisibility(View.GONE);
                            if (dialog_data != null)
                                dialog_data.dismiss();
                        }

                    } else if (labTestItem != null) {
                        fl_medicines_symbol.setVisibility(View.GONE);
                        if (checker && prescriptionItem.getMedicines() == null) {
                            patient_item = prescriptionItem;
                        } else {
                            textView_end_note.setVisibility(View.VISIBLE);
                            rViewlabtest.setVisibility(View.VISIBLE);
                            if (labTestItem.size() != 0)
                                textView_advice.setVisibility(View.VISIBLE);
                            else
                                textView_advice.setVisibility(View.GONE);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)

                                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                    patient_item = prescriptionItem;
                                    filesGlobal = files;
                                }
                            }, 1);
                        }

                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                        popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        fl_progress_bar.setVisibility(View.GONE);
                        if (dialog_data != null)
                            dialog_data.dismiss();
                    } else {
                        fl_medicines_symbol.setVisibility(View.GONE);
                        if (checker) {
                            patient_item = prescriptionItem;
                        } else {
                            textView_end_note.setVisibility(View.VISIBLE);
                            Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                            rViewlabtest.setVisibility(View.GONE);
                            textView_advice.setVisibility(View.GONE);

                            files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                            patient_item = prescriptionItem;
                            filesGlobal = files;
                        }


                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                        popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        fl_progress_bar.setVisibility(View.GONE);
                        if (dialog_data != null)
                            dialog_data.dismiss();

//                                String myString = textView_medical_cert_desc.getText().toString().trim();
//                                String [] arr = myString.split("\\s+");
//                                //Splits words & assign to the arr[]  ex : arr[0] -> Copying ,arr[1] -> first
//                                if (arr != null) {
//
//
//                                    double length_desc = Math.ceil(arr.length/250);
//                                    int N=250; // NUMBER OF WORDS THAT YOU NEED
//                                    int z=0;
//                                    String nWords="";
//
//                                    // concatenating number of words that you required
//                                    stringArrayDESC = new ArrayList<>();
//                                    for(int k=0; k<length_desc ; k++) {
//                                        for (int i = z; i < N+z; i++) {
//                                            nWords = nWords + " " + arr[i];
//
//                                        }
//                                        z= z+250;
//                                        stringArrayDESC.add(nWords);
//                                        nWords="";
//                                    }
//                                    //String upToNCharacters = textView_medical_cert_desc.getText().toString().trim().substring(0, Math.min(length_desc, 1000));
//
//                                }
//
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        textView_medical_cert_desc.setText(stringArrayDESC.get(0).trim());
//                                               /* if(prescriptionItem.getMedicines()!=null)
//                                                if (prescriptionItem.getMedicines().size() > 4)
//                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
//                                        // Do something after 4s = 4000ms
//                                        page_no.setText("Page No. "+page_no_count);
//                                        Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
//                                        files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
//
//                                               /* if(prescriptionItem.getMedicines()!=null)
//                                                    if (prescriptionItem.getMedicines().size() > 4)
//                                                        page_no.setText("2/"+(prescriptionItem.getMedicines().size()/4)+1);*/
//
//                                        patient_item = prescriptionItem;
//                                        textView_chief_complaint.setVisibility(View.GONE);
//                                        textView_history.setVisibility(View.GONE);
//                                        textView_findings.setVisibility(View.GONE);
//                                        textView_treatment_advice.setVisibility(View.GONE);
//                                        textView_diagnosis.setVisibility(View.GONE);
//                                        Log.e(TAG,"MEDICAL CERTIFICATE WITH DESC --------------------------------------------p[0] - 1310 = ");
//                                        if(stringArrayDESC.size() >1) {
//                                            tempMEDICAL_CERT(1, files);
//
//                                        }
//
//
//
//                                    }
//                                }, DELAY_TIME);
                    }


                } else {
                    textView_end_note.setVisibility(View.VISIBLE);
                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                    rViewlabtest.setVisibility(View.GONE);
                    textView_advice.setVisibility(View.GONE);
                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                    patient_item = prescriptionItem;
                    filesGlobal = files;
                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                    popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fl_progress_bar.setVisibility(View.GONE);
                    if (dialog_data != null)
                        dialog_data.dismiss();
                }
                       /* else {
                        fl_progress_bar.setVisibility(View.GONE);
                    }*/
            } catch (Exception e) {
                fl_progress_bar.setVisibility(View.GONE);
                if (dialog_data != null)
                    dialog_data.dismiss();
                e.printStackTrace();
            }
        } catch (Exception e) {
            fl_progress_bar.setVisibility(View.GONE);
            if (dialog_data != null) {
                dialog_data.dismiss();

            }
            e.printStackTrace();
        }
    }

    public void temp(int p, final ArrayList<Uri> files) {
        if (prescriptionItem.getMedicines() != null)
            Log.e(TAG, "--------------------------------------------prescriptionItem.getMedicines().size()   = " + prescriptionItem.getMedicines().size());

        if (prescriptionItem.getMedicines().size() > p) {
            int diff_size = prescriptionItem.getMedicines().size() - p;
            Log.e(TAG, "--------------------------------------------diff_size - 701 = " + diff_size);

            if (diff_size < 5) {
                textView_end_note.setVisibility(View.VISIBLE);
                if (rViewlabtest.getVisibility() == View.VISIBLE)
                    rViewlabtest.setVisibility(View.GONE);

                if (textView_advice.getVisibility() == View.VISIBLE)
                    textView_advice.setVisibility(View.GONE);

                ArrayList<MedicinesItem> medicineList1 = new ArrayList<>();
                for (int i = 0; i < diff_size; i++) {
                    if (prescriptionItem.getMedicines().size() > p) {
                        medicineList1.add(medicinesItemArrayListO.get(p));
                        p = p + 1;
                    }
                }

                Pres_adapter_mobile templateAdapter1 = new Pres_adapter_mobile(medicineList1, mContext);
                rView.setAdapter(templateAdapter1);

                if (labTestItem != null) {
                    if (labTestItem.size() > 0) {
                        Pres_LabTest_adapter_mobile pres_labTest_adapter = new Pres_LabTest_adapter_mobile(labTestItem, mContext);
                        rViewlabtest.setAdapter(pres_labTest_adapter);
                        rViewlabtest.setVisibility(View.VISIBLE);
                        textView_advice.setVisibility(View.VISIBLE);
                    } else {
                        textView_advice.setVisibility(View.GONE);
                    }
                }

                final Handler handler = new Handler();
                final int finalP = p;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 4s = 4000ms
                      /*  if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("3/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        page_no_count = page_no_count + 1;
                        page_no.setText("Page No. " + page_no_count);
                        Bitmap screen1 = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                        files.add(getImageUri(mContext, screen1, prescriptionItem.getPName() + "1"));

                     /*   if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("4/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        patient_item = prescriptionItem;
                        textView_chief_complaint.setVisibility(View.GONE);
                        textView_history.setVisibility(View.GONE);
                        textView_findings.setVisibility(View.GONE);
                        textView_treatment_advice.setVisibility(View.GONE);
                        textView_diagnosis.setVisibility(View.GONE);
                        binding.textviewTemprature.setVisibility(View.GONE);
                        binding.textviewHeight.setVisibility(View.GONE);
                        binding.textviewWeight.setVisibility(View.GONE);
                        binding.textviewPulse.setVisibility(View.GONE);
                        binding.textviewBloodPressure.setVisibility(View.GONE);
                        binding.textviewBloodSugar.setVisibility(View.GONE);
                        binding.textviewHemoglobing.setVisibility(View.GONE);
                        binding.textviewSpo2.setVisibility(View.GONE);
                        binding.textviewRespirationRate.setVisibility(View.GONE);
                        binding.textviewAllergy.setVisibility(View.GONE);
                        Log.e(TAG, "--------------------------------------------finalP 760 = " + finalP);
                        // temp1(finalP, files);
                        filesGlobal = files;
                        fl_progress_bar.setVisibility(View.GONE);
                        popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        if (dialog_data != null) {
                            dialog_data.dismiss();
                            // PopupGeneratedImages();
                        }


                    }
                }, DELAY_TIME);
            } else {

                if (rViewlabtest.getVisibility() == View.VISIBLE)
                    rViewlabtest.setVisibility(View.GONE);

                if (textView_advice.getVisibility() == View.VISIBLE)
                    textView_advice.setVisibility(View.GONE);

                ArrayList<MedicinesItem> medicineList1 = new ArrayList<>();
                int sizee = 8;
                /*if (width < 520)
                    sizee = 5;
                else if (width < 820)
                    sizee = 6;*/

                for (int i = 0; i < sizee; i++) {
                    if (prescriptionItem.getMedicines().size() > p) {
                        medicineList1.add(medicinesItemArrayListO.get(p));
                        p = p + 1;
                    }
                }

                Pres_adapter_mobile templateAdapter1 = new Pres_adapter_mobile(medicineList1, mContext);
                rView.setAdapter(templateAdapter1);


                final Handler handler = new Handler();
                final int finalP = p;
                Log.e(TAG, "--------------------------------------------finalP  = " + finalP);

                /*if (finalP == prescriptionItem.getMedicines().size())
                    textView_end_note.setVisibility(View.VISIBLE);
                else
                    textView_end_note.setVisibility(View.GONE);*/

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 4s = 4000ms
                      /*  if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("3/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        page_no_count = page_no_count + 1;
                        page_no.setText("Page No. " + page_no_count);
                        Bitmap screen1 = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                        files.add(getImageUri(mContext, screen1, prescriptionItem.getPName() + "1"));

                     /*   if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("4/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        patient_item = prescriptionItem;
                        textView_chief_complaint.setVisibility(View.GONE);
                        textView_history.setVisibility(View.GONE);
                        textView_findings.setVisibility(View.GONE);
                        textView_treatment_advice.setVisibility(View.GONE);
                        textView_diagnosis.setVisibility(View.GONE);
                        binding.textviewTemprature.setVisibility(View.GONE);
                        binding.textviewHeight.setVisibility(View.GONE);
                        binding.textviewWeight.setVisibility(View.GONE);
                        binding.textviewPulse.setVisibility(View.GONE);
                        binding.textviewBloodPressure.setVisibility(View.GONE);
                        binding.textviewBloodSugar.setVisibility(View.GONE);
                        binding.textviewHemoglobing.setVisibility(View.GONE);
                        binding.textviewSpo2.setVisibility(View.GONE);
                        binding.textviewRespirationRate.setVisibility(View.GONE);
                        binding.textviewAllergy.setVisibility(View.GONE);
                        Log.e(TAG, "--------------------------------------------finalP - 817 = " + finalP);
                        temp(finalP, files);

                        filesGlobal = files;
                    }
                }, DELAY_TIME);
            }

            // rViewlabtest.setVisibility(View.VISIBLE);//*****************
            //textView_advice.setVisibility(View.VISIBLE);//*****************

        } else if (labTestItem != null) {
            page_no_count = page_no_count + 1;
            page_no.setText("Page No. " + page_no_count);
//            rView.setVisibility(View.GONE);
            fl_medicines_symbol.setVisibility(View.GONE);
            textView_end_note.setVisibility(View.VISIBLE);
            if (labTestItem.size() > 0) {
//                rView.setVisibility(View.GONE);

                Pres_LabTest_adapter_mobile pres_labTest_adapter = new Pres_LabTest_adapter_mobile(labTestItem, mContext);
                rViewlabtest.setAdapter(pres_labTest_adapter);
                rViewlabtest.setVisibility(View.VISIBLE);
                textView_advice.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                final int finalP = p;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Do something after 4s = 4000ms
                      /*  if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("3/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        Bitmap screen1 = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                        files.add(getImageUri(mContext, screen1, prescriptionItem.getPName() + "1"));

                     /*   if(prescriptionItem.getMedicines()!=null)
                            if (prescriptionItem.getMedicines().size() > 4)
                                page_no.setText("4/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                        patient_item = prescriptionItem;
                        textView_chief_complaint.setVisibility(View.GONE);
                        textView_history.setVisibility(View.GONE);
                        textView_findings.setVisibility(View.GONE);
                        textView_treatment_advice.setVisibility(View.GONE);
                        textView_diagnosis.setVisibility(View.GONE);
                        binding.textviewTemprature.setVisibility(View.GONE);
                        binding.textviewHeight.setVisibility(View.GONE);
                        binding.textviewWeight.setVisibility(View.GONE);
                        binding.textviewPulse.setVisibility(View.GONE);
                        binding.textviewBloodPressure.setVisibility(View.GONE);
                        binding.textviewBloodSugar.setVisibility(View.GONE);
                        binding.textviewHemoglobing.setVisibility(View.GONE);
                        binding.textviewSpo2.setVisibility(View.GONE);
                        binding.textviewRespirationRate.setVisibility(View.GONE);
                        binding.textviewAllergy.setVisibility(View.GONE);
                        Log.e(TAG, "--------------------------------------------finalP - 866 = " + finalP);

                        filesGlobal = files;

                        popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        fl_progress_bar.setVisibility(View.GONE);
                        if (dialog_data != null) {
                            dialog_data.dismiss();
                            // PopupGeneratedImages();
                        }
                    }
                }, DELAY_TIME);

            } else {
//                rView.setVisibility(View.GONE);
                textView_advice.setVisibility(View.GONE);
                rViewlabtest.setVisibility(View.GONE);
                rViewlabtest.setVisibility(View.GONE);
                popupBottomMenu();

                if (dialog_data != null) {
                    dialog_data.dismiss();
                    // PopupGeneratedImages();
                }
            }


            Log.e(TAG, "--------------------------------------------(labTestItem>0)");



                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);*/

        } else {
            rViewlabtest.setVisibility(View.GONE);
            textView_advice.setVisibility(View.GONE);
            textView_end_note.setVisibility(View.VISIBLE);

                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);*/
            popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            fl_progress_bar.setVisibility(View.GONE);
            if (dialog_data != null) {
                dialog_data.dismiss();
                //  PopupGeneratedImages();
            }
        }
    }

    public void temp1(int p, final ArrayList<Uri> files) {
        if (prescriptionItem.getMedicines().size() > p) {
            if (rViewlabtest.getVisibility() == View.VISIBLE)
                rViewlabtest.setVisibility(View.GONE);

            if (textView_advice.getVisibility() == View.VISIBLE)
                textView_advice.setVisibility(View.GONE);
            ArrayList<MedicinesItem> medicineList1 = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                if (prescriptionItem.getMedicines().size() > p) {
                    medicineList1.add(medicinesItemArrayListO.get(p));
                    p = p + 1;
                }
            }
            Log.e(TAG, "--------------------------------------------p - 940 = " + p);
            Pres_adapter_mobile templateAdapter1 = new Pres_adapter_mobile(medicineList1, mContext);
            rView.setAdapter(templateAdapter1);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    page_no_count = page_no_count + 1;
                    page_no.setText("Page No. " + page_no_count);

                    // Do something after 4s = 4000ms
                    Bitmap screen1 = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                    files.add(getImageUri(mContext, screen1, prescriptionItem.getPName() + "2"));
                    patient_item = prescriptionItem;
                    filesGlobal = files;
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                    intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                    intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    Intent shareIntent = Intent.createChooser(intent, null);
                    startActivity(shareIntent);*/
                    popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fl_progress_bar.setVisibility(View.GONE);
                    if (dialog_data != null) {
                        dialog_data.dismiss();
                        // PopupGeneratedImages();
                    }
                }
            }, DELAY_TIME);

        } else {
            rView.setAdapter(null);
//            rView.setVisibility(View.GONE);

            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
            intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
            intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);*/

            popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            if (labTestItem != null) {
                if (labTestItem.size() > 0) {
                    page_no_count = page_no_count + 1;
                    page_no.setText("Page No. " + page_no_count);

                    Pres_LabTest_adapter_mobile pres_labTest_adapter = new Pres_LabTest_adapter_mobile(labTestItem, mContext);
                    rViewlabtest.setAdapter(pres_labTest_adapter);
                    rViewlabtest.setVisibility(View.VISIBLE);
                    textView_advice.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 4s = 4000ms
                            Bitmap screen1 = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                            files.add(getImageUri(mContext, screen1, prescriptionItem.getPName() + "2"));
                            patient_item = prescriptionItem;
                            filesGlobal = files;
                    /*Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                    intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                    intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                    Intent shareIntent = Intent.createChooser(intent, null);
                    startActivity(shareIntent);*/
                            popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            fl_progress_bar.setVisibility(View.GONE);
                            if (dialog_data != null) {
                                dialog_data.dismiss();
                                //  PopupGeneratedImages();
                            }
                        }
                    }, DELAY_TIME);

                } else {
                    textView_advice.setVisibility(View.GONE);
                    rViewlabtest.setVisibility(View.GONE);

                    popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fl_progress_bar.setVisibility(View.GONE);
                    if (dialog_data != null) {
                        dialog_data.dismiss();
                        // PopupGeneratedImages();
                    }
                }
            } else {
                textView_advice.setVisibility(View.GONE);
                rViewlabtest.setVisibility(View.GONE);
                popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                fl_progress_bar.setVisibility(View.GONE);
                if (dialog_data != null) {
                    dialog_data.dismiss();
                    //  PopupGeneratedImages();
                }
            }

        }
    }


    private static void addImage(Document document, byte[] byteArray) {
        try {
            image = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(140f, 140f);
        try {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private SingleObserver<ResponseSuccess> responsePrescription = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {
            if (response != null) {
                Log.e(TAG, "responsePrescription: >> " + response.getMessage());
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Unable to process your request", Toast.LENGTH_SHORT).show();
                    fl_progress_bar.setVisibility(View.GONE);
                    if (dialog_data != null)
                        dialog_data.dismiss();

                } else if (response.getMessage().equals("Prescription Added")) {

                    prescriptionId = response.getPrescriptionId();

                    // Toast.makeText(mContext, "Prescription Added", Toast.LENGTH_SHORT).show();
                    count = true;
                    boolean checker = false;
                    generatePDF.setText("Share");
                    back.setVisibility(View.GONE);
                    btn_backbtn_edit_profile.setVisibility(View.VISIBLE);
                    try {
                        final ArrayList<Uri> files = new ArrayList<Uri>();
                        final int[] p = {0};
                        int line = 0;
                        String[] lines = null;
                        String[] details = diagnosis_desc.split(Pattern.quote("|"));

                        for (String strr : details
                        ) {
                            line = line + strr.length();
                        }
                        // Toast.makeText(mContext, "Lines = "+line, Toast.LENGTH_SHORT).show();
                        int field_active = 0;
                        for (String str : details) {
                            if (!str.trim().equals(""))
                                field_active = field_active + 1;
                        }
                        textView_end_note.setVisibility(View.GONE);

                        Log.e(TAG, "------------------------------------   LINE   === " + line);


                        if (line > 3000) {
                            checker = true;
//                            rView.setVisibility(View.GONE);
                            rViewlabtest.setVisibility(View.GONE);
                            fl_medicines_symbol.setVisibility(View.GONE);
                            textView_advice.setVisibility(View.GONE);
                            textView_end_note.setVisibility(View.GONE);

                            if (prescriptionItem.getMedicines() == null) {
                                rViewlabtest.setVisibility(View.VISIBLE);
                                textView_advice.setVisibility(View.VISIBLE);
                                textView_end_note.setVisibility(View.VISIBLE);
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                                    // Do something after 4s = 4000ms

                                    page_no.setText("Page No. " + page_no_count);
                                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                    filesGlobal = files;
                                    page_no_count = page_no_count + 1;
                                    textView_chief_complaint.setVisibility(View.GONE);
                                    textView_history.setVisibility(View.GONE);
                                    textView_findings.setVisibility(View.GONE);
                                    textView_treatment_advice.setVisibility(View.GONE);
                                    textView_diagnosis.setVisibility(View.GONE);
                                    rView.setVisibility(View.VISIBLE);
                                    binding.textviewTemprature.setVisibility(View.GONE);
                                    binding.textviewHeight.setVisibility(View.GONE);
                                    binding.textviewWeight.setVisibility(View.GONE);
                                    binding.textviewPulse.setVisibility(View.GONE);
                                    binding.textviewBloodPressure.setVisibility(View.GONE);
                                    binding.textviewBloodSugar.setVisibility(View.GONE);
                                    binding.textviewHemoglobing.setVisibility(View.GONE);
                                    binding.textviewSpo2.setVisibility(View.GONE);
                                    binding.textviewBloodGroup.setVisibility(View.GONE);
                                    binding.textViewHosAddress.setVisibility(View.GONE);
                                    binding.textviewRespirationRate.setVisibility(View.GONE);
                                    binding.textviewAllergy.setVisibility(View.GONE);
                                    fl_medicines_symbol.setVisibility(View.VISIBLE);
                                    textView_end_note.setVisibility(View.GONE);
                                }
                            }, 1);
                        }
                        //==========================================================================
                        if (prescriptionItem != null) {

                            generatePDF.setVisibility(View.GONE);
                            if (prescriptionItem.getMedicines() != null) {
                                if (prescriptionItem.getMedicines().size() != 0) {
                                    int sizee = 8;
                                   /* if (width < 520)
                                        sizee = 5;
                                    else if (width < 820)
                                        sizee = 6;

                                    if (line == 0) {

                                        sizee = 6;
                                    }*/

                                    if (prescriptionItem.getMedicines().size() >= sizee) {
                                        rViewlabtest.setVisibility(View.GONE);
                                        textView_advice.setVisibility(View.GONE);

                                        ArrayList<MedicinesItem> medicineList = new ArrayList<>();
                                        for (int i = 0; i < sizee; i++) {
                                            if (prescriptionItem.getMedicines().size() > p[0]) {
                                                medicineList.add(medicinesItemArrayListO.get(p[0]));
                                                p[0] = p[0] + 1;
                                            }
                                        }

                                        Pres_adapter_mobile templateAdapter = new Pres_adapter_mobile(medicineList, mContext);
                                        rView.setAdapter(templateAdapter);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                                                // Do something after 4s = 4000ms
                                                page_no.setText("Page No. " + page_no_count);
                                                Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                                files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));

                                               /* if(prescriptionItem.getMedicines()!=null)
                                                    if (prescriptionItem.getMedicines().size() > 4)
                                                        page_no.setText("2/"+(prescriptionItem.getMedicines().size()/4)+1);*/

                                                patient_item = prescriptionItem;
                                                textView_chief_complaint.setVisibility(View.GONE);
                                                textView_history.setVisibility(View.GONE);
                                                textView_findings.setVisibility(View.GONE);
                                                textView_treatment_advice.setVisibility(View.GONE);
                                                textView_diagnosis.setVisibility(View.GONE);
                                                binding.textviewTemprature.setVisibility(View.GONE);
                                                binding.textviewHeight.setVisibility(View.GONE);
                                                binding.textviewWeight.setVisibility(View.GONE);
                                                binding.textviewPulse.setVisibility(View.GONE);
                                                binding.textviewBloodPressure.setVisibility(View.GONE);
                                                binding.textviewBloodSugar.setVisibility(View.GONE);
                                                binding.textviewHemoglobing.setVisibility(View.GONE);
                                                binding.textviewSpo2.setVisibility(View.GONE);
                                                binding.textviewBloodGroup.setVisibility(View.GONE);
                                                binding.textviewRespirationRate.setVisibility(View.GONE);
                                                binding.textViewHosAddress.setVisibility(View.GONE);
                                                binding.textviewAllergy.setVisibility(View.GONE);
                                                Log.e(TAG, "--------------------------------------------p[0] - 1199 = " + p[0]);

                                                temp(p[0], files);
                                                filesGlobal = files;

                                            }
                                        }, DELAY_TIME);

                                    } else {
                                       /* if(!checker){
                                            fl_medicines_symbol.setVisibility(View.VISIBLE);
                                            if(prescriptionItem.getMedicines().size()!=0)
                                                rView.setVisibility(View.VISIBLE);
                                            page_no_count = page_no_count + 1;
                                            textView_chief_complaint.setVisibility(View.GONE);
                                            textView_history.setVisibility(View.GONE);
                                            textView_findings.setVisibility(View.GONE);
                                            textView_treatment_advice.setVisibility(View.GONE);
                                            textView_diagnosis.setVisibility(View.GONE);
                                            binding.textviewTemprature.setVisibility(View.GONE);
                                            binding.textviewHeight.setVisibility(View.GONE);
                                            binding.textviewWeight.setVisibility(View.GONE);
                                            binding.textviewPulse.setVisibility(View.GONE);
                                            binding.textviewBloodPressure.setVisibility(View.GONE);
                                            binding.textviewBloodSugar.setVisibility(View.GONE);
                                            binding.textviewHemoglobing.setVisibility(View.GONE);
                                            binding.textviewSpo2.setVisibility(View.GONE);
                                            binding.textviewBloodGroup.setVisibility(View.GONE);
                                            binding.textViewHosAddress.setVisibility(View.GONE);
                                            binding.textviewRespirationRate.setVisibility(View.GONE);
                                            binding.textviewAllergy.setVisibility(View.GONE);
                                           top_header_parent.setVisibility(View.GONE);
                                        }*/


                                        textView_end_note.setVisibility(View.VISIBLE);
                                        page_no.setText("Page No. " + page_no_count);

                                        Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)

                                        files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                        patient_item = prescriptionItem;
                                        filesGlobal = files;
                                        /*Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                        intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                        intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                        Intent shareIntent = Intent.createChooser(intent, null);
                                        startActivity(shareIntent);*/
                                        popupBottomMenu();
                                        fl_progress_bar.setVisibility(View.GONE);
                                        if (dialog_data != null)
                                            dialog_data.dismiss();
                                    }
                                }
                            } else if (labTestItem != null) {
                                fl_medicines_symbol.setVisibility(View.GONE);
                                if (checker && prescriptionItem.getMedicines() == null) {
                                    patient_item = prescriptionItem;
                                } else {
                                    textView_end_note.setVisibility(View.VISIBLE);
                                    rViewlabtest.setVisibility(View.VISIBLE);
                                    textView_advice.setVisibility(View.VISIBLE);

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)

                                            files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                            patient_item = prescriptionItem;
                                            filesGlobal = files;
                                        }
                                    }, 1);
                                }

                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                                popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                fl_progress_bar.setVisibility(View.GONE);
                                if (dialog_data != null)
                                    dialog_data.dismiss();
                            } else {
                                fl_medicines_symbol.setVisibility(View.GONE);
                                if (checker) {
                                    patient_item = prescriptionItem;
                                } else {
                                    textView_end_note.setVisibility(View.VISIBLE);
                                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                    rViewlabtest.setVisibility(View.GONE);
                                    textView_advice.setVisibility(View.GONE);

                                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                    patient_item = prescriptionItem;
                                    filesGlobal = files;
                                }


                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                                popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                fl_progress_bar.setVisibility(View.GONE);
                                if (dialog_data != null)
                                    dialog_data.dismiss();

//                                String myString = textView_medical_cert_desc.getText().toString().trim();
//                                String [] arr = myString.split("\\s+");
//                                //Splits words & assign to the arr[]  ex : arr[0] -> Copying ,arr[1] -> first
//                                if (arr != null) {
//
//
//                                    double length_desc = Math.ceil(arr.length/250);
//                                    int N=250; // NUMBER OF WORDS THAT YOU NEED
//                                    int z=0;
//                                    String nWords="";
//
//                                    // concatenating number of words that you required
//                                    stringArrayDESC = new ArrayList<>();
//                                    for(int k=0; k<length_desc ; k++) {
//                                        for (int i = z; i < N+z; i++) {
//                                            nWords = nWords + " " + arr[i];
//
//                                        }
//                                        z= z+250;
//                                        stringArrayDESC.add(nWords);
//                                        nWords="";
//                                    }
//                                    //String upToNCharacters = textView_medical_cert_desc.getText().toString().trim().substring(0, Math.min(length_desc, 1000));
//
//                                }
//
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        textView_medical_cert_desc.setText(stringArrayDESC.get(0).trim());
//                                               /* if(prescriptionItem.getMedicines()!=null)
//                                                if (prescriptionItem.getMedicines().size() > 4)
//                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
//                                        // Do something after 4s = 4000ms
//                                        page_no.setText("Page No. "+page_no_count);
//                                        Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
//                                        files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
//
//                                               /* if(prescriptionItem.getMedicines()!=null)
//                                                    if (prescriptionItem.getMedicines().size() > 4)
//                                                        page_no.setText("2/"+(prescriptionItem.getMedicines().size()/4)+1);*/
//
//                                        patient_item = prescriptionItem;
//                                        textView_chief_complaint.setVisibility(View.GONE);
//                                        textView_history.setVisibility(View.GONE);
//                                        textView_findings.setVisibility(View.GONE);
//                                        textView_treatment_advice.setVisibility(View.GONE);
//                                        textView_diagnosis.setVisibility(View.GONE);
//                                        Log.e(TAG,"MEDICAL CERTIFICATE WITH DESC --------------------------------------------p[0] - 1310 = ");
//                                        if(stringArrayDESC.size() >1) {
//                                            tempMEDICAL_CERT(1, files);
//
//                                        }
//
//
//
//                                    }
//                                }, DELAY_TIME);
                            }


                        } else {
                            textView_end_note.setVisibility(View.VISIBLE);
                            Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                            rViewlabtest.setVisibility(View.GONE);
                            textView_advice.setVisibility(View.GONE);
                            files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                            patient_item = prescriptionItem;
                            filesGlobal = files;
                                /*Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                Intent shareIntent = Intent.createChooser(intent, null);
                                startActivity(shareIntent);*/
                            popupBottomMenu();
// behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            fl_progress_bar.setVisibility(View.GONE);
                            if (dialog_data != null)
                                dialog_data.dismiss();
                        }
                       /* else {
                        fl_progress_bar.setVisibility(View.GONE);
                    }*/
                    } catch (Exception e) {
                        fl_progress_bar.setVisibility(View.GONE);
                        if (dialog_data != null)
                            dialog_data.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    fl_progress_bar.setVisibility(View.GONE);
                    if (dialog_data != null)
                        dialog_data.dismiss();

                }

            } else {
                fl_progress_bar.setVisibility(View.GONE);
                if (dialog_data != null)
                    dialog_data.dismiss();
                Toast.makeText(mContext, "Unable to process your request", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_bar.setVisibility(View.GONE);
            if (dialog_data != null)
                dialog_data.dismiss();
            Log.e(TAG, "onError: responsePrescription >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    public void tempMEDICAL_CERT(final int position, final ArrayList<Uri> files) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (stringArrayDESC.get(position) != null) {
                    textView_medical_cert_desc.setText(stringArrayDESC.get(position).trim());
                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                    // Do something after 4s = 4000ms
                    page_no.setText("Page No. " + position + 1);
                    Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                    files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));

                                               /* if(prescriptionItem.getMedicines()!=null)
                                                    if (prescriptionItem.getMedicines().size() > 4)
                                                        page_no.setText("2/"+(prescriptionItem.getMedicines().size()/4)+1);*/

                    patient_item = prescriptionItem;
                    textView_chief_complaint.setVisibility(View.GONE);
                    textView_history.setVisibility(View.GONE);
                    textView_findings.setVisibility(View.GONE);
                    textView_treatment_advice.setVisibility(View.GONE);
                    textView_diagnosis.setVisibility(View.GONE);
                    binding.textviewTemprature.setVisibility(View.GONE);
                    binding.textviewHeight.setVisibility(View.GONE);
                    binding.textviewWeight.setVisibility(View.GONE);
                    binding.textviewPulse.setVisibility(View.GONE);
                    binding.textviewBloodPressure.setVisibility(View.GONE);
                    binding.textviewBloodSugar.setVisibility(View.GONE);
                    binding.textviewHemoglobing.setVisibility(View.GONE);
                    binding.textviewSpo2.setVisibility(View.GONE);
                    binding.textviewRespirationRate.setVisibility(View.GONE);
                    binding.textviewAllergy.setVisibility(View.GONE);
                    Log.e(TAG, "MEDICAL CERTIFICATE WITH DESC --------------------------------------------p[0] - 1310 = ");

                    if (position < stringArrayDESC.size() - 1) {
                        tempMEDICAL_CERT(position + 1, files);
                    } else {
                        filesGlobal = files;
                    }
                }
            }
        }, DELAY_TIME);
    }

    private void PopupGeneratedImages() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(false);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        }

        dialog_data.setContentView(R.layout.popup_generated_prescription_images);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.MATCH_PARENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView iv_cancel = dialog_data.findViewById(R.id.remove_image);

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_data != null)
                    dialog_data.dismiss();
            }
        });


        Button btn_proceed = dialog_data.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog_data != null)
                    dialog_data.dismiss();

                prescriptionItem = prescriptionItemTEMP;
                labTestItem = labTestItemTEMP;
                definer = definerTEMP;
                end_note = end_noteTEMP;

                initViews();

            }
        });
       /* for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);*/

        final ViewPager mPager = dialog_data.findViewById(R.id.pager_images);

        mPager.setAdapter(new SlidingImage_Adapter_GeneratedPrescription(getApplicationContext(), filesGlobal));

        CirclePageIndicator indicator = (CirclePageIndicator)
                dialog_data.findViewById(R.id.indicato_imagesr);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        /*NUM_PAGES =filesGlobal.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);*/

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        dialog_data.show();
    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    private void initViews() {
        int counter = 0;

        if (certificate_selection && !TextUtils.isEmpty(definer) && !definer.equalsIgnoreCase("history")) {
            page_no.setVisibility(View.VISIBLE);
            fl_medicines_symbol.setVisibility(View.GONE);
            textView_diagnosis.setVisibility(View.GONE);
            textView_advice.setVisibility(View.GONE);
            textView_medical_cert.setVisibility(View.VISIBLE);
            textView_medical_cert_desc.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(bundle.getString("certificate_title"))) {
                textView_medical_cert.setText(bundle.getString("certificate_title"));
                textView_medical_cert.setVisibility(View.VISIBLE);
            }

            if (prescriptionItem.getMedicines() != null)
                prescriptionItem.getMedicines().clear();

            if (labTestItem != null)
                labTestItem.clear();

            if (!TextUtils.isEmpty(bundle.getString("certificate_desc")) && !bundle.getString("certificate_desc").equalsIgnoreCase("null")) {
                textView_medical_cert_desc.setText("" + bundle.getString("certificate_desc"));
                textView_medical_cert_desc.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            textView_pat_name.setTextColor(getResources().getColor(R.color.colorBlack));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            ll_patient_name.setLayoutParams(params);
        } else {

            if (prescriptionItem.getMedicines() != null) {
                if (prescriptionItem.getMedicines().size() != 0)
                    fl_medicines_symbol.setVisibility(View.VISIBLE);
                else
                    fl_medicines_symbol.setVisibility(View.GONE);
            } else {
                if (labTestItem != null) {
                    if (labTestItem.size() != 0)
                        fl_medicines_symbol.setVisibility(View.VISIBLE);
                } else
                    fl_medicines_symbol.setVisibility(View.GONE);
            }

            textView_medical_cert.setVisibility(View.GONE);
            textView_medical_cert_desc.setVisibility(View.GONE);
        }

        CreatePrescription.labtestAddFLAG = true;
        CreatePrescription.medicineAddFLAG = true;
        if (end_note == null)
            textView_end_note.setVisibility(View.GONE);
        else {
            if (end_note.contains("|")) {
                String[] details = end_note.split(Pattern.quote("|"));

                textView_medical_cert.setVisibility(View.GONE);
                if (details.length >= 2)
                    if (!details[1].equals("")) {
                        textView_medical_cert.setText(details[1]);
                        textView_medical_cert.setVisibility(View.VISIBLE);
                        isCertificateAvail = true;

                    }

                textView_medical_cert_desc.setVisibility(View.GONE);
                if (details.length >= 3)
                    if (!details[2].equals("")) {
                        textView_medical_cert_desc.setText(details[2]);
                        textView_medical_cert_desc.setVisibility(View.VISIBLE);
                    }

                if (details.length != 0) {
                    if (details[0] != null)
                        if (details[0].equals("")) {
                            page_no.setVisibility(View.VISIBLE);
                            fl_medicines_symbol.setVisibility(View.GONE);
                            textView_diagnosis.setVisibility(View.GONE);
                            textView_advice.setVisibility(View.GONE);
                            textView_medical_cert_desc.setVisibility(View.VISIBLE);
                            textView_medical_cert_desc.setTextColor(getResources().getColor(R.color.colorBlack));
                            textView_pat_name.setTextColor(getResources().getColor(R.color.colorBlack));
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.CENTER;
                            ll_patient_name.setLayoutParams(params);
                            textView_chief_complaint.setVisibility(View.GONE);
                            textView_medical_cert_desc.setVisibility(View.VISIBLE);

                        } else {
                            textView_chief_complaint.setVisibility(View.VISIBLE);
                            textView_end_note.setText("Note : " + details[0]);
                        }

                }
            } else {
                if (end_note.trim().equals(""))
                    textView_end_note.setVisibility(View.GONE);
                else
                    textView_chief_complaint.setVisibility(View.VISIBLE);
                textView_end_note.setText("Note : " + end_note);
            }
        }

        diagnosis_desc = prescriptionItem.getDiagnosis();
        if (prescriptionItem.getMedicines() != null) {
            if (prescriptionItem.getMedicines().size() != 0) {
                medicinesItemArrayListO = new ArrayList<>();
                for (com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mi : prescriptionItem.getMedicines()) {
                    com.likesby.bludoc.ModelLayer.Entities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.Entities.MedicinesItem();
                    mii.setMedicineName(mi.getMedicineName());
                    mii.setMedicineId(mi.getPresbMedicineId());
                    mii.setAdditionaComment(mi.getAdditionaComment());
                    mii.setFrequency(mi.getFrequency());
                    mii.setQty(mi.getQty());
                    mii.setInstruction(mi.getInstruction());
                    mii.setNoOfDays(mi.getNoOfDays());
                    mii.setRoute(mi.getRoute());
                    medicinesItemArrayListO.add(mii);
                }
            }
        }

        if (diagnosis_desc == null) {
            textView_diagnosis.setVisibility(View.GONE);
            textView_chief_complaint.setVisibility(View.GONE);
            textView_history.setVisibility(View.GONE);
            textView_treatment_advice.setVisibility(View.GONE);
            textView_findings.setVisibility(View.GONE);
            textView_diagnosis.setVisibility(View.GONE);
            binding.textviewTemprature.setVisibility(View.GONE);
            binding.textviewHeight.setVisibility(View.GONE);
            binding.textviewWeight.setVisibility(View.GONE);
            binding.textviewPulse.setVisibility(View.GONE);
            binding.textviewBloodPressure.setVisibility(View.GONE);
            binding.textviewBloodSugar.setVisibility(View.GONE);
            binding.textviewHemoglobing.setVisibility(View.GONE);
            binding.textviewSpo2.setVisibility(View.GONE);
            binding.textviewRespirationRate.setVisibility(View.GONE);
            binding.textviewAllergy.setVisibility(View.GONE);
        } else {
            textView_diagnosis.setVisibility(View.GONE);
            textView_chief_complaint.setVisibility(View.GONE);
            textView_history.setVisibility(View.GONE);
            textView_treatment_advice.setVisibility(View.GONE);
            textView_findings.setVisibility(View.GONE);
            textView_diagnosis.setVisibility(View.GONE);
            binding.textviewTemprature.setVisibility(View.GONE);
            binding.textviewHeight.setVisibility(View.GONE);
            binding.textviewWeight.setVisibility(View.GONE);
            binding.textviewPulse.setVisibility(View.GONE);
            binding.textviewBloodPressure.setVisibility(View.GONE);
            binding.textviewBloodSugar.setVisibility(View.GONE);
            binding.textviewHemoglobing.setVisibility(View.GONE);
            binding.textviewSpo2.setVisibility(View.GONE);
            binding.textviewRespirationRate.setVisibility(View.GONE);
            binding.textviewAllergy.setVisibility(View.GONE);
            String[] details = diagnosis_desc.split(Pattern.quote("|"));
            // Toast.makeText(mContext, "details = "+details.length, Toast.LENGTH_SHORT).show();
            int lineCounter = 0;
            for (String strr : details
            ) {
                if (!strr.equals("")) {
                    int multiplier = 1;
                    if (strr.length() > 90) {
                        multiplier = (strr.length() / 90);
                    }
                    Log.e("multiplier", " ====== " + multiplier);

                    lineCounter = lineCounter + multiplier;
                }
            }
            Log.e("details", " ======-------------------------------------------------- ");
            Log.e("details", " ====== " + diagnosis_desc);
            Log.e("lineCounter", " ====== " + lineCounter);
            Log.e("details", " ======-------------------------------------------------- ");

            if (details.length != 0) {
                int line = 0;
                String[] lines = null;
                for (String strr : details
                ) {
                    line = line + strr.length();
                }
                if (details[0] != null)
                    if (details[0].equals(""))
                        textView_chief_complaint.setVisibility(View.GONE);
                    else {
                        textView_chief_complaint.setVisibility(View.VISIBLE);
                        textView_chief_complaint.setText("Chief Complaint - " + details[0]);
                    }

                if (details.length >= 2)
                    if (details[1] != null)
                        if (details[1].equals(""))
                            textView_history.setVisibility(View.GONE);
                        else {
                            textView_history.setVisibility(View.VISIBLE);
                            textView_history.setText("History - " + details[1]);
                        }

                if (details.length >= 3)
                    if (details[2] != null)
                        if (details[2].equals(""))
                            textView_findings.setVisibility(View.GONE);
                        else {
                            textView_findings.setVisibility(View.VISIBLE);
                            textView_findings.setText("Findings - " + details[2]);
                        }

                if (details.length > 3 && !TextUtils.isEmpty(details[3])) {

                    textView_diagnosis.setVisibility(View.VISIBLE);
                    textView_diagnosis.setText("Diagnosis - " + details[3]);

                }

                if (details.length > 4 && !TextUtils.isEmpty(details[4])) {

                    textView_treatment_advice.setVisibility(View.VISIBLE);
                    textView_treatment_advice.setText("Treatment/Advice - " + details[4]);

                }

                if (details.length >= 5)
                    if (details[3] != null)
                        if (details[3].equals(""))
                            textView_diagnosis.setVisibility(View.GONE);
                        else {
                            textView_diagnosis.setVisibility(View.VISIBLE);
                            textView_diagnosis.setText("Diagnosis - " + details[3]);
                        }


                if (details.length > 6 && !TextUtils.isEmpty(details[6])) {

                    binding.textviewTemprature.setVisibility(View.VISIBLE);
                    binding.textviewTemprature.setText("Temperature - " + details[6]);

                }

                if (details.length > 7 && !TextUtils.isEmpty(details[7])) {

                    binding.textviewHeight.setVisibility(View.VISIBLE);
                    binding.textviewHeight.setText("Height - " + details[7]);

                }

                if (details.length > 8 && !TextUtils.isEmpty(details[8])) {

                    binding.textviewWeight.setVisibility(View.VISIBLE);
                    binding.textviewWeight.setText("Weight - " + details[8]);

                }

                if (details.length > 9 && !TextUtils.isEmpty(details[9])) {

                    binding.textviewPulse.setVisibility(View.VISIBLE);
                    binding.textviewPulse.setText("Pulse - " + details[9]);

                }

                if (details.length > 10 && !TextUtils.isEmpty(details[10])) {

                    binding.textviewBloodPressure.setVisibility(View.VISIBLE);
                    binding.textviewBloodPressure.setText("Blood Pressure - " + details[10]);

                }

                if (details.length > 11 && !TextUtils.isEmpty(details[11])) {

                    binding.textviewBloodSugar.setVisibility(View.VISIBLE);
                    binding.textviewBloodSugar.setText("Blood Sugar - " + details[11]);

                }

                if (details.length > 12 && !TextUtils.isEmpty(details[12])) {

                    binding.textviewHemoglobing.setVisibility(View.VISIBLE);
                    binding.textviewHemoglobing.setText("Hemoglobin - " + details[12]);

                }

                if (details.length > 13 && !TextUtils.isEmpty(details[13])) {

                    binding.textviewSpo2.setVisibility(View.VISIBLE);
                    binding.textviewSpo2.setText("Spo2 - " + details[13]);

                }

                if (details.length > 14 && !TextUtils.isEmpty(details[14])) {

                    binding.textviewRespirationRate.setVisibility(View.VISIBLE);
                    binding.textviewRespirationRate.setText("Respiration Rate - " + details[14]);

                }

                if (details.length > 15 && !TextUtils.isEmpty(details[15])) {

                    binding.textviewAllergy.setVisibility(View.VISIBLE);
                    binding.textviewAllergy.setText("Allergy - " + details[15]);

                }

            } else {
                textView_chief_complaint.setVisibility(View.GONE);
                textView_history.setVisibility(View.GONE);
                textView_treatment_advice.setVisibility(View.GONE);
                textView_findings.setVisibility(View.GONE);
                textView_diagnosis.setVisibility(View.GONE);
                binding.textviewTemprature.setVisibility(View.GONE);
                binding.textviewHeight.setVisibility(View.GONE);
                binding.textviewWeight.setVisibility(View.GONE);
                binding.textviewPulse.setVisibility(View.GONE);
                binding.textviewBloodPressure.setVisibility(View.GONE);
                binding.textviewBloodSugar.setVisibility(View.GONE);
                binding.textviewHemoglobing.setVisibility(View.GONE);
                binding.textviewSpo2.setVisibility(View.GONE);
                binding.textviewRespirationRate.setVisibility(View.GONE);
                binding.textviewAllergy.setVisibility(View.GONE);
            }

        }


        if (("history").equalsIgnoreCase(definer)) {
            title.setText("Prescription Details");
        } else {
            title.setText("Prescription Preview");
        }

        Doctor doctor = prescriptionItem.getDoctor();
        if (!("").equalsIgnoreCase(doctor.getClinicName())) {
            textView_Clinic_name.setText(doctor.getClinicName());
            isClinicNameFound = true;
        } else {
            textView_Clinic_name.setVisibility(View.GONE);
            isClinicNameFound = false;
        }

            /*String name_ =doctor.getName().trim();
            if(name_.contains("Dr.")) {
                name_ = name_.replace("Dr.", "");
                //manager.setPreferences(mContext, "name", name_.trim());
            }
            String name_2 = doctor.getName().trim();
            if(name_2.contains("Dr."))
            {
                name_2 = name_2.replace("Dr.","");
              //  manager.setPreferences(mContext,"name",name_2.trim());
            }*/
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "name").trim())) {
            textView_DocName.setText(doctor.getName().trim());
        }

        String ugName = "", pgName = "";

        if (!TextUtils.isEmpty(doctor.getUgName())) {

            ugName = doctor.getUgName() + " ";
            pgName = doctor.getPgName() + " ";

        }

        if (!("").equalsIgnoreCase(doctor.getPgName()) && doctor.getPgName() != null) {
            if (!("").equalsIgnoreCase(doctor.getAddtionalQualification()) && (doctor.getAddtionalQualification() != null)) {
                textView_degree.setText(ugName + pgName + doctor.getAddtionalQualification());
            } else {
                textView_degree.setText(ugName + doctor.getPgName());
            }

            // todo doctor additional qualification =================>
        } else if (!("").equalsIgnoreCase(doctor.getAddtionalQualification()) && (doctor.getAddtionalQualification() != null)) {
            textView_degree.setText(ugName + doctor.getAddtionalQualification());

        } else {
            textView_degree.setText(ugName);
        }


        textView_res_num.setText("Reg. No - " + doctor.getRegistrationNo());
        if (!("").equalsIgnoreCase(doctor.getDesignationName())) {
            textView_des.setText(doctor.getDesignationName());
        } else {
            textView_des.setVisibility(View.GONE);
        }
        String emaill = "";
        if (prescriptionItem.getPEmail() == null) {
            emaill = "-";
        } else if (prescriptionItem.getPEmail().equalsIgnoreCase("")) {
            emaill = "-";
        } else
            emaill = prescriptionItem.getPEmail();

        String mobl = "-";
        if (prescriptionItem.getPMobile() == null) {
            mobl = "-";
        } else if (prescriptionItem.getPMobile().equalsIgnoreCase("")) {
            mobl = "-";
        } else
            mobl = prescriptionItem.getPMobile();
        String age___ = "";
        if (prescriptionItem.getAge().contains("yr") || prescriptionItem.getAge().contains("month"))
            age___ = prescriptionItem.getAge();
        else
            age___ = prescriptionItem.getAge() + " yr";

        if (!TextUtils.isEmpty(prescriptionItem.getAddress())) {
            binding.textViewHosAddress.setText("Address - " + prescriptionItem.getAddress());
        } else {

            binding.textViewHosAddress.setVisibility(View.GONE);

        }

        if (!TextUtils.isEmpty(prescriptionItem.getpBloodGrp())) {
            binding.textviewBloodGroup.setText("Blood Group - " + prescriptionItem.getpBloodGrp());
        } else {

            binding.textviewBloodGroup.setVisibility(View.GONE);

        }

        String patient_age = "";
        if(!TextUtils.isEmpty(prescriptionItem.getAge())){
            patient_age = " / " + age___;
        }

        textView_pat_name.setText("Patient : " + prescriptionItem.getPName() + ", " + prescriptionItem.getGender() + patient_age);
        if (prescriptionItem.getPEmail().equals(""))

            patient_item = prescriptionItem;
        if (("history").equalsIgnoreCase(definer)) {
               /* String sourceDate = prescriptionItem.getDate();
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat targetFormat = new SimpleDateFormat("dd-MMM-YYYY");
                Date date = null;
                try {
                    date = originalFormat.parse(sourceDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = targetFormat.format(date);
                System.out.println(formattedDate);*/

            textView_date.setText("Date : " + DateUtils.outFormatsetMMM(prescriptionItem.getDate()));
        } else {
            textView_date.setText("Date : " + prescriptionItem.getDate());
        }
        if (!("").equalsIgnoreCase(doctor.getWorkingDays())) {
            String work_days = doctor.getWorkingDays();
            if (work_days.contains("MONDAY"))
                work_days = work_days.replace("MONDAY", " MON");

            if (work_days.contains("TUESDAY"))
                work_days = work_days.replace("TUESDAY", " TUE");

            if (work_days.contains("WEDNESDAY"))
                work_days = work_days.replace("WEDNESDAY", " WED");

            if (work_days.contains("THURSDAY"))
                work_days = work_days.replace("THURSDAY", " THUR");

            if (work_days.contains("FRIDAY"))
                work_days = work_days.replace("FRIDAY", " FRI");

            if (work_days.contains("SATURDAY"))
                work_days = work_days.replace("SATURDAY", " SAT");

            if (work_days.contains("SUNDAY"))
                work_days = work_days.replace("SUNDAY", " SUN");

            textView_days.setText("Working Days : " + work_days);
        } else {
            textView_days.setVisibility(View.GONE);
            counter = counter + 1;
        }

        if (TextUtils.isEmpty(binding.textViewDays.getText().toString()) &&
                TextUtils.isEmpty(binding.textViewTime.getText().toString()) &&
                TextUtils.isEmpty(binding.textViewContact.getText().toString()) &&
                TextUtils.isEmpty(binding.textViewEmail.getText().toString()) &&
                TextUtils.isEmpty(binding.textViewAdd.getText().toString()))
            binding.lineOfSingle.setVisibility(View.GONE); // hide line when data is empty todo

        if (("history").equalsIgnoreCase(definer)) {

            String[] visiting_hr_from_details = prescriptionItem.getDoctor().getVisitingHrFrom().split(Pattern.quote("|"));
            String[] visiting_hr_to_details = prescriptionItem.getDoctor().getVisitingHrTo().split(Pattern.quote("|"));
            if (visiting_hr_from_details.length > 1)
                textView_time.setText("Visiting Hours : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim() + ", " + visiting_hr_from_details[1].toLowerCase().trim() + " - " + visiting_hr_to_details[1].toLowerCase().trim());
            else
                textView_time.setText("Visiting Hours : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim());

            if (visiting_hr_from_details.length == 1 && visiting_hr_from_details[0].equals(""))
                if (visiting_hr_to_details.length == 1 && visiting_hr_to_details[0].equals(""))
                    textView_time.setVisibility(View.GONE);
        } else if (!("").equalsIgnoreCase(doctor.getVisitingHrFrom())) {
            String[] visiting_hr_from_details = manager.getPreferences(mContext, "visiting_hr_from").split(Pattern.quote("|"));
            ;
            String[] visiting_hr_to_details = manager.getPreferences(mContext, "visiting_hr_to").split(Pattern.quote("|"));
            ;
            if (visiting_hr_from_details.length > 1)
                textView_time.setText("Visiting Hours : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim() + ", " + visiting_hr_from_details[1].toLowerCase().trim() + " - " + visiting_hr_to_details[1].toLowerCase().trim());
            else
                textView_time.setText("Visiting Hours : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim());
            //  textView_time.setText("Time : " + doctor.getVisitingHrFrom() + " to " + doctor.getVisitingHrTo());

            if (visiting_hr_from_details.length == 1 && visiting_hr_from_details[0].equals(""))
                if (visiting_hr_to_details.length == 1 && visiting_hr_to_details[0].equals(""))
                    textView_time.setVisibility(View.GONE);
        } else {
            textView_time.setVisibility(View.GONE);
            counter = counter + 1;
        }
        if (!("").equalsIgnoreCase(doctor.getMobileLetterHead())) {
            textView_contact.setText("Contact : " + doctor.getMobileLetterHead());
        } else {
            textView_contact.setVisibility(View.GONE);
            counter = counter + 1;
        }
        if (!("").equalsIgnoreCase(doctor.getEmailLetterHead())) {
            textView_email.setText("Mail Id : " + doctor.getEmailLetterHead());
        } else {
            textView_email.setVisibility(View.GONE);
            counter = counter + 1;
        }
        if (!("").equalsIgnoreCase(doctor.getClinicAddress())) {
            textView_add.setText("Address : " + doctor.getClinicAddress());
        } else {
            textView_add.setVisibility(View.GONE);
            counter = counter + 1;
        }


        if (!("").equalsIgnoreCase(doctor.getImage())) {
            Picasso.with(mContext).
                    load(doctor.getImage())
                    .into(image_layout, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            image_layout.setVisibility(View.INVISIBLE);
        }


        if (!("").equalsIgnoreCase(doctor.getSignature())) {
            Picasso.with(mContext).
                    load(doctor.getSignature())
                    .into(signature, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            signature.setVisibility(View.GONE);
            sig_text.setVisibility(View.GONE);
        }


        Log.e(TAG, ">>11>> " + medicinesItemArrayListO.toString());

        if (prescriptionItem != null) {
            Pres_adapter_mobile templateAdapter = new Pres_adapter_mobile(medicinesItemArrayListO, mContext);
            rView.setAdapter(templateAdapter);
        } else
            rView.setAdapter(null);

        if (labTestItem != null) {
            if (labTestItem.size() > 0) {
                Pres_LabTest_adapter_mobile pres_labTest_adapter = new Pres_LabTest_adapter_mobile(labTestItem, mContext);
                rViewlabtest.setAdapter(pres_labTest_adapter);
                textView_advice.setVisibility(View.VISIBLE);
            } else {
                textView_advice.setVisibility(View.GONE);
                rViewlabtest.setAdapter(null);
            }
        }

        if (counter == 5)
            binding.lineOfSingle.setVisibility(View.GONE);

    }

    public void setOnDataViaTranslation(String selectedLanguage) {

        Log.i(TAG, "setOnDataViaTranslation1: " + medicinesInEnglish);
        Log.i(TAG, "setOnDataViaTranslation2: " + labTestItemInEnglish);

        ArrayList<MedicineItem> medicineItems = setDataOnSecondModules(medicinesInEnglish);
        ArrayList<LabTestItems> labTestItems = setLabtestDataModules(labTestItemInEnglish);

        String translatedString = toJSON(medicineItems);
        String translatedStringOfLabTest = toJSONLabTest(labTestItems);

        String translatedStringJsonOfMedicines = getTranslatedJson(translatedString);
        String translatedStringOfLabTestOfOffline = getTranslatedJson(translatedStringOfLabTest);

        TranslateAPI translateAPI = new TranslateAPI(
                Language.AUTO_DETECT,   //Source Language
                selectedLanguage,        //Target Language
                translatedStringJsonOfMedicines);         //Query Text

        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                String replace = translatedText.trim().replace(" ,", ",").replace("??? ", "\"").replace("???", "\"").replace("???", "\"").replace(" ??", ",").replace(", \"", ",");
                getTranslateOnDiffrentLanguage(replace, selectedLanguage);
                Log.i(TAG, "onSuccess: " + replace);
            }

            @Override
            public void onFailure(String ErrorText) {
                Log.d(TAG, "onFailure: " + ErrorText);
            }
        });

        // lab_test

        TranslateAPI labTestTranslatedApi = new TranslateAPI(
                Language.AUTO_DETECT,   //Source Language
                selectedLanguage,        //Target Language
                translatedStringOfLabTestOfOffline);         //Query Text

        labTestTranslatedApi.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                String replace = translatedText.trim().replace(" ,", ",").replace("??? ", "\"").replace("???", "\"").replace("???", "\"").replace(" ??", ",").replace(", \"", ",");
                getTranslateOnDiffrentLanguageLabTest(replace, selectedLanguage);
                Log.i(TAG, "onSuccess: " + replace);
            }


            @Override
            public void onFailure(String ErrorText) {
                Log.d(TAG, "onFailure: " + ErrorText);
            }
        });

    }

    private ArrayList<LabTestItems> setLabtestDataModules(ArrayList<LabTestItem> labTestItem) {

        ArrayList<LabTestItems> labTestItemArrayList = new ArrayList<>();

        if (labTestItem != null) {

            for (LabTestItem mi : labTestItem) {
                LabTestItems mii = new LabTestItems();
                mii.setLabTestComment(mi.getLabTestComment());
                mii.setLabTestName(mi.getLabTestName());
                labTestItemArrayList.add(mii);

            }
        }

        return labTestItemArrayList;

    }

    private ArrayList<MedicineItem> setDataOnSecondModules(ArrayList<com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem> medicines) {

        ArrayList<MedicineItem> medicineItems = new ArrayList<>();

        if (medicines != null) {

            for (com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mi : medicines) {
                MedicineItem mii = new MedicineItem();
                mii.setMedicineName(mi.getMedicineName());
                mii.setPresbMedicineId(mi.getPresbMedicineId());
                mii.setAdditionaComment(mi.getAdditionaComment());
                mii.setQty(mi.getQty());

                if (mi.getInstruction().equalsIgnoreCase("None") || mi.getInstruction().equalsIgnoreCase(""))
                    mii.setInstruction("");
                else
                    mii.setInstruction(mi.getInstruction());

                if (mi.getFrequency().equalsIgnoreCase("None") || mi.getFrequency().equalsIgnoreCase(""))
                    mii.setFrequency("");
                else
                    mii.setFrequency(mi.getFrequency());

                if (mi.getRoute().equalsIgnoreCase("None") || mi.getRoute().equalsIgnoreCase(""))
                    mii.setRoute("");
                else
                    mii.setRoute(mi.getRoute());

                mii.setNoOfDays(mi.getNoOfDays());
                medicineItems.add(mii);

            }
        }

        return medicineItems;

    }

    private void getTranslateOnDiffrentLanguageLabTest(String labTestJoin, String selectedLanguage) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<LabTestItems>>() {
        }.getType();
        ArrayList<LabTestItems> contactList = null;
        try {
            contactList = gson.fromJson(labTestJoin, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        ArrayList<LabTestItem> reverseFromLabTest = getReverseFromLabTest(contactList);

        if (labTestItem != null && contactList != null) {
            labTestItem.clear();
            labTestItem.addAll(reverseFromLabTest);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable("defaultAttributeId", prescriptionItem);
        bundle.putParcelableArrayList("defaultAttributeIdLabTest", labTestItem);
        bundle.putString("definer", definer);
        bundle.putString("end_note", end_note);
        bundle.putString("yesOrNo", yesOrNo);
        bundle.putString("selectedLanguage", selectedLanguage);

        this.setArguments(bundle);

        value = true;

        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();


    }

    private ArrayList<LabTestItem> getReverseFromLabTest(ArrayList<LabTestItems> contactList) {

        ArrayList<LabTestItem> labTestItemArrayList = new ArrayList<>();

        if (contactList != null) {

            for (LabTestItems mi : contactList) {
                LabTestItem mii = new LabTestItem();
                mii.setLabTestComment(mi.getLabTestComment());
                mii.setLabTestName(mi.getLabTestName());
                labTestItemArrayList.add(mii);

            }
        }

        return labTestItemArrayList;

    }

    private String getTranslatedJson(String translatedString) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        return gson.toJson(jp.parse(translatedString));

    }

    String toJSON(ArrayList<MedicineItem> list) {

        Gson gson = new Gson();
        return gson.toJson(list, new TypeToken<ArrayList<MedicineItem>>() {
        }.getType());
    }

    String toJSONLabTest(ArrayList<LabTestItems> labTestItem) {

        Gson gson = new Gson();
        return gson.toJson(labTestItem, new TypeToken<ArrayList<LabTestItems>>() {
        }.getType());
    }

    private void getTranslateOnDiffrentLanguage(final String medicinesJoin, String selectedLanguage) {

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MedicineItem>>() {
        }.getType();
        ArrayList<MedicineItem> contactList = null;
        try {
            contactList = gson.fromJson(medicinesJoin, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        ArrayList<com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem> reverseFromMedicineItem = getReverseFromMedicineItem(contactList);

        prescriptionItem.setMedicines(reverseFromMedicineItem);

        Bundle bundle = new Bundle();
        bundle.putParcelable("defaultAttributeId", prescriptionItem);
        bundle.putParcelableArrayList("defaultAttributeIdLabTest", labTestItem);
        bundle.putString("definer", definer);
        bundle.putString("end_note", end_note);
        bundle.putString("yesOrNo", yesOrNo);
        bundle.putString("selectedLanguage", selectedLanguage);

        this.setArguments(bundle);

        value = true;

        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

    }

    private ArrayList<com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem> getReverseFromMedicineItem(ArrayList<MedicineItem> contactList) {

        ArrayList<com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem> medicineItems = new ArrayList<>();

        if (contactList != null) {

            for (MedicineItem mi : contactList) {
                com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem();
                mii.setMedicineName(mi.getMedicineName());
                mii.setPresbMedicineId(mi.getPresbMedicineId());
                mii.setAdditionaComment(mi.getAdditionaComment());
                mii.setFrequency(mi.getFrequency());
                mii.setQty(mi.getQty());
                mii.setInstruction(mi.getInstruction());
                mii.setNoOfDays(mi.getNoOfDays());
                mii.setRoute(mi.getRoute());
                medicineItems.add(mii);

            }
        }

        return medicineItems;

    }

    private String checkNull(@Nullable String s) {

        if (TextUtils.isEmpty(s))
            return "";
        else if (!TextUtils.isEmpty(s) && s != null)
            return s.replace("0000000", "");

        return "";

    }

    @Override
    public void onModalOptionSelected(@org.jetbrains.annotations.Nullable String s, @NotNull Option option) {

        switch (option.getId()) {

            case 6:
                setOnDataViaTranslation(Language.HINDI);
                break;

            case 7:
                setOnDataViaTranslation(Language.ENGLISH);
                break;

            case 8:
                setOnDataViaTranslation(Language.CHINESE);
                break;

            case 9:
                setOnDataViaTranslation(Language.SPANISH);
                break;

            case 10:
                setOnDataViaTranslation(Language.FRENCH);
                break;

            case 11:
                setOnDataViaTranslation(Language.BENGALI);
                break;

            case 12:
                setOnDataViaTranslation(Language.GERMAN);
                break;

            case 13:
                setOnDataViaTranslation(Language.ITALIAN);
                break;

            case 14:
                setOnDataViaTranslation(Language.DUTCH);
                break;

            case 15:
                setOnDataViaTranslation(Language.POLISH);
                break;

            case 16:
                setOnDataViaTranslation(Language.INDONESIAN);
                break;

            case 17:
                setOnDataViaTranslation(Language.DANISH);
                break;

            case 18:
                setOnDataViaTranslation(Language.SWEDISH);
                break;

            case 19:
                setOnDataViaTranslation(Language.TURKISH);
                break;

            case 20:
                setOnDataViaTranslation("mr");
                break;

            case 21:
                setOnDataViaTranslation("ur");
                break;
        }

    }

    private void setLanguageOnBlueDoc() {

        ModalBottomSheetDialogFragment.Builder builder =
                new ModalBottomSheetDialogFragment.Builder(); //custom option, without needing menu XML

        builder.add(new OptionRequest(6, "Hindi", R.drawable.ic_action_language));
        builder.add(new OptionRequest(7, "English", R.drawable.ic_action_language));
        builder.add(new OptionRequest(8, "Chinese", R.drawable.ic_action_language));
        builder.add(new OptionRequest(9, "Spanish", R.drawable.ic_action_language));
        builder.add(new OptionRequest(10, "French", R.drawable.ic_action_language));
        builder.add(new OptionRequest(11, "Bengali", R.drawable.ic_action_language));
        builder.add(new OptionRequest(12, "German", R.drawable.ic_action_language));
        builder.add(new OptionRequest(13, "italian", R.drawable.ic_action_language));
        builder.add(new OptionRequest(14, "Dutch", R.drawable.ic_action_language));
        builder.add(new OptionRequest(15, "Polish", R.drawable.ic_action_language));
        builder.add(new OptionRequest(16, "Indonesian", R.drawable.ic_action_language));
        builder.add(new OptionRequest(17, "Danish", R.drawable.ic_action_language));
        builder.add(new OptionRequest(18, "Swedish", R.drawable.ic_action_language));
        builder.add(new OptionRequest(19, "Turkish", R.drawable.ic_action_language));
        builder.add(new OptionRequest(20, "Marathi", R.drawable.ic_action_language));
        builder.add(new OptionRequest(21, "Urdu", R.drawable.ic_action_language));

        builder.layout(R.layout.item_customs_languages);
        builder.show(getChildFragmentManager(), "Custom_languages");

    }

    private void hideAndShowDoctarDetails(boolean value) {

        if (!value) {
            binding.topDoctorDetails.setVisibility(View.GONE);
        } else {
            binding.topDoctorDetails.setVisibility(View.VISIBLE);
        }

        isVisible = value;

    }

    private void hideAndShowClinicName(boolean value) {

        if (!value) {
            binding.textViewClinicName.setVisibility(View.GONE);
        } else {
            binding.textViewClinicName.setVisibility(View.VISIBLE);
        }

        isClinicVisible = value;

    }

    private void hideAndShowFooterDetails(boolean value) {

        if (!value) {
            binding.footerDocterDetails.setVisibility(View.GONE);
            binding.pageOfParent.setVisibility(View.GONE);
        } else {
            binding.footerDocterDetails.setVisibility(View.VISIBLE);
            binding.pageOfParent.setVisibility(View.VISIBLE);
        }

        isFooterVisible = value;

    }

    @Override
    public void onChangeResult(int ids, boolean value) {

        switch (ids) {

            case 1:

                hideAndShowDoctarDetails(value);

                break;

            case 2:

                hideAndShowClinicName(value);

                break;

            case 3:

                hideAndShowFooterDetails(value);

                break;

            case 4:
                startActivity(new Intent(mContext, AllPharmacistActivity.class));
                break;

            case 5:
                startActivityForResult(new Intent(mContext, MultiplePharmacistActivity.class), 510);
                break;

            case 6:
                setLanguageOnBlueDoc();
                break;

        }

    }

    @Override
    public void onChooseCalender(String dataValue, String date_with_name) {

        textView_date.setText("Date: " + date_with_name);
        dateValueString = dataValue;
        dateWithName = date_with_name;

    }
}