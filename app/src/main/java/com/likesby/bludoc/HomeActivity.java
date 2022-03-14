package com.likesby.bludoc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.likesby.bludoc.Adapter.SlidingImage_Adapter;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.Fragment.GeneratedPresBottomFragment;
import com.likesby.bludoc.Fragment.PatientRegistration;
import com.likesby.bludoc.Fragment.PopUpSubscriptionDialogFragment;
import com.likesby.bludoc.Fragment.QualificationsDialogFragment;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.Entities.BannersItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.viewpagerindicator.CirclePageIndicator;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-6756023122563497/5728747901";
    //Context mContext;
    Dialog dialog;

    static ApiViewHolder apiViewHolder;
    BottomSheetBehavior behavior;
    CoordinatorLayout coordinatorLayout;
    CompositeDisposable mBag = new CompositeDisposable();
    private static final String TAG = "State_picket";
    RecyclerView rView;
    boolean flag_reset_free = false;
    GridLayoutManager lLayout;
    View v;
    public static ArrayList<PatientsItem> patientsItemArrayList = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    TextView login;
    static String Streamid, SubjectName;
    //   FrameLayout frame;
    //private BottomNavigationView bottomNavigationView;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    CardView History, prescribe, my_template, buy_subscription;
    ImageView refer_app, close_pop;
    static SessionManager manager;
    Boolean addflag = false;
    Context mContext;
    public static ArrayList<MedicinesItem> medicinesItemArrayList = new ArrayList<>();
    private ArrayList<BannersItem> bannerArrayList = new ArrayList<>();
    public static int poss__ = 0;
    FrameLayout fl_progress_layout;
    private static Context ctx;
    Button wahtsappTest;
    private static FragmentManager fragmanager = null;
    MyDB myDB;
    private RewardedAd rewardedAd;
    TextView tv_days_left, tv_premium_top;
    LinearLayout ll_premium;
    private AdView mAdView, mAdViewNative;
    AdLoader adLoader;
    private UnifiedNativeAd nativeAd;
    boolean showNativeAdFlag = false;
    Dialog dialog_data;
    private OnBackClickListener onBackClickListener;
    AdRequest adRequest, adRequestInterstitial;
    private InterstitialAd mInterstitialAd;
    private OnBackClickListener2 onBackClickListener2;
    private AppointmentModel appointment;
    private TextView head_dot;
    private CardView dot_parent;

    public void methodName() {

        CreatePrescription.patientsItemArrayList = new ArrayList<PatientsItem>();
        CreatePrescription.pos = -1;
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }

    }

    public interface OnBackClickListener {
        boolean onBackClick();
    }

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public interface OnBackClickListener2 {
        boolean onBackClick();
    }

    public void setOnBackClickListener2(OnBackClickListener2 onBackClickListener2) {
        this.onBackClickListener2 = onBackClickListener2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mContext = HomeActivity.this;
        MobileAds.initialize(this, "ca-app-pub-9225891557304181~7586066501");
        // rewardedAd = createAndLoadRewardedAd();

        adRequest = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        adRequestInterstitial = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        ctx = mContext;
        myDB = new MyDB(mContext);
        fragmanager = getSupportFragmentManager();
        manager = new SessionManager();
        manager.setPreferences(mContext, "home", "true");

        initCalls();

        /* adLoader = new AdLoader.Builder(mContext, "ca-app-pub-6756023122563497/5728747901")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                      //  Toast.makeText(mContext, "NATIVE onUnifiedNativeAdLoaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                      //  Toast.makeText(mContext, "NATIVE onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
*/

     /*   if(manager.contains(mContext,"purchased_new")){
            try {
                Calendar c2 = Calendar .getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                Date dateEnd = dateFormat.parse(manager.getPreferences(mContext,"purchased_date"));
                Date c = Calendar.getInstance().getTime();
                assert dateEnd != null;
                Log.e("DATE_____S = ","DATE_____S ="+DateUtils.printDifference(c,dateEnd));
                // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                Date date1 = null,date2=null;

                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                date2 = dateFormat.parse(manager.getPreferences(mContext,"purchased_date"));

                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");
                Toast.makeText(mContext, ""+splited[0], Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Log.e("DATE_____S = ","Exception ="+e);

            }
        }*/

        ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
        String[] s = responseProfileDetails.getCreated().split(" ");
        DateTime dateTime = new DateTime(s[0]);

        String updateQualification = manager.getPreferences(this, "update_qualifications");

        if (TextUtils.isEmpty(responseProfileDetails.getAddtionalQualification()) && !isToday(dateTime) && !updateQualification.equals("true")) {

            QualificationsDialogFragment qualificationsDialogFragment = new QualificationsDialogFragment();
            qualificationsDialogFragment.setCancelable(false);
            qualificationsDialogFragment.show(getSupportFragmentManager(), "qualifications");

            manager.setPreferences(this, "update_qualifications", "false");

        }


    }

    private boolean isToday(DateTime dateTime) {

        DateTime today = new DateTime();

        return (today.getMonthOfYear() == dateTime.getMonthOfYear()) && (today.getYear() == dateTime.getYear()) && (today.getDayOfMonth() == dateTime.getDayOfMonth());

    }


    private void initInterstitialAd(AdRequest adRequest) {
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-9225891557304181/3105393849");//Live
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//Test
        mInterstitialAd.loadAd(adRequest);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
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
                mInterstitialAd = null;
                //adRequestInterstitial = null;
            }
        });

    }

    private void displayUnifiedNativeAd(ViewGroup parent, UnifiedNativeAd ad) {

        // Inflate a layout and add it to the parent ViewGroup.
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater
                .inflate(R.layout.my_ad_layout, parent);

        // Locate the view that will hold the headline, set its text, and call the
        // UnifiedNativeAdView's setHeadlineView method to register it.
        TextView headlineView = adView.findViewById(R.id.ad_headline);
        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);

        // Repeat the above process for the other assets in the UnifiedNativeAd
        // using additional view objects (Buttons, ImageViews, etc).


        // If the app is using a MediaView, it should be
        // instantiated and passed to setMediaView. This view is a little different
        // in that the asset is populated automatically, so there's one less step.
        /*MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);*/

        // Call the UnifiedNativeAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(ad);

        // Ensure that the parent view doesn't already contain an ad view.
        parent.removeAllViews();

        // Place the AdView into the parent.
        parent.addView(adView);
    }


//    private void NativeAd(Dialog v, ProgressBar pb, Button btn_yes, Button btn_no) {
//                refreshAd(v,pb,btn_yes,btn_no);
//        //  "ca-app-pub-3940256099942544/1044960115"
//    }
//
//        /**
//         * Populates a {@link UnifiedNativeAdView} object with data from a given
//         * {@link UnifiedNativeAd}.
//         *
//         * @param nativeAd the object containing the ad's assets
//         * @param adView          the view to be populated
//         */
//        private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
//            // Set the media view.
//            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
//
//            // Set other ad assets.
//            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//            adView.setBodyView(adView.findViewById(R.id.ad_body));
//            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//            adView.setPriceView(adView.findViewById(R.id.ad_price));
//            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//            adView.setStoreView(adView.findViewById(R.id.ad_store));
//            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//            // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
//            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//            // check before trying to display them.
//            if (nativeAd.getBody() == null) {
//                adView.getBodyView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getBodyView().setVisibility(View.VISIBLE);
//                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//            }
//
//            if (nativeAd.getCallToAction() == null) {
//                adView.getCallToActionView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getCallToActionView().setVisibility(View.VISIBLE);
//                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//            }
//
//            if (nativeAd.getIcon() == null) {
//                adView.getIconView().setVisibility(View.GONE);
//            } else {
//                ((ImageView) adView.getIconView()).setImageDrawable(
//                        nativeAd.getIcon().getDrawable());
//                adView.getIconView().setVisibility(View.VISIBLE);
//            }
//
//            if (nativeAd.getPrice() == null) {
//                adView.getPriceView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getPriceView().setVisibility(View.VISIBLE);
//                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//            }
//
//            if (nativeAd.getStore() == null) {
//                adView.getStoreView().setVisibility(View.INVISIBLE);
//            } else {
//                adView.getStoreView().setVisibility(View.VISIBLE);
//                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//            }
//
//            if (nativeAd.getStarRating() == null) {
//                adView.getStarRatingView().setVisibility(View.INVISIBLE);
//            } else {
//
//                ((RatingBar) adView.getStarRatingView())
//                        .setRating(nativeAd.getStarRating().floatValue());
//                adView.getStarRatingView().setVisibility(View.VISIBLE);
//            }
//
//            if (nativeAd.getAdvertiser() == null) {
//                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//            } else {
//                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//                adView.getAdvertiserView().setVisibility(View.VISIBLE);
//            }
//
//            // This method tells the Google Mobile Ads SDK that you have finished populating your
//            // native ad view with this native ad.
//            adView.setNativeAd(nativeAd);
//
//            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//            // have a video asset.
//            VideoController vc = nativeAd.getVideoController();
//
//            // Updates the UI to say whether or not this ad has a video asset.
//            if (vc.hasVideoContent()) {
//
//
//                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
//                // VideoController will call methods on this object when events occur in the video
//                // lifecycle.
//                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
//                    @Override
//                    public void onVideoEnd() {
//                        // Publishers should allow native ads to complete video playback before
//                        // refreshing or replacing them with another ad in the same UI location.
//
//                        super.onVideoEnd();
//                    }
//                });
//            } else {
//
//            }
//        }
//
//        /**
//         * Creates a request for a new native ad based on the boolean parameters and calls the
//         * corresponding "populate" method when one is successfully returned.
//         *
//         * @param
//         * @param btn_yes
//         * @param btn_no
//         */
//        private void refreshAd(final Dialog dialog_view, final ProgressBar pb, final Button btn_yes, final Button btn_no) {
//
//            AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);
//
//            builder.forUnifiedNativeAd(
//                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                        // OnUnifiedNativeAdLoadedListener implementation.
//                        @Override
//                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                            // If this callback occurs after the activity is destroyed, you must call
//                            // destroy and return or you may get a memory leak.
//                            boolean isDestroyed = false;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                isDestroyed = isDestroyed();
//                            }
//                            if (isDestroyed || isFinishing() || isChangingConfigurations()) {
//                                unifiedNativeAd.destroy();
//                                return;
//                            }
//                            // You must call destroy on old ads when you are done with them,
//                            // otherwise you will have a memory leak.
//                            if (nativeAd != null) {
//                                nativeAd.destroy();
//                            }
//                            nativeAd = unifiedNativeAd;
//                            FrameLayout frameLayout = dialog_view.findViewById(R.id.fl_adplaceholder);
//                            UnifiedNativeAdView adView =
//                                    (UnifiedNativeAdView) getLayoutInflater()
//                                            .inflate(R.layout.my_ad_layout, null);
//                            populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                            frameLayout.removeAllViews();
//                            frameLayout.addView(adView);
//
//                            pb.setVisibility(View.GONE);
//                            btn_yes.setVisibility(View.VISIBLE);
//                            btn_no.setVisibility(View.VISIBLE);
//                        }
//                    });
//
//           /* VideoOptions videoOptions =
//                    new VideoOptions.Builder().build();*/
//
//            NativeAdOptions adOptions =
//                    new NativeAdOptions.Builder().build();
//
//            builder.withNativeAdOptions(adOptions);
//
//            AdLoader adLoader =
//                    builder
//                            .withAdListener(
//                                    new AdListener() {
//                                        @Override
//                                        public void onAdFailedToLoad(LoadAdError loadAdError) {
//                                            String error =
//                                                    String.format(
//                                                            "domain: %s, code: %d, message: %s",
//                                                            loadAdError.getDomain(),
//                                                            loadAdError.getCode(),
//                                                            loadAdError.getMessage());
////                                            Toast.makeText(
////                                                    mContext,
////                                                    "Failed to load native ad with error " + error,
////                                                    Toast.LENGTH_SHORT)
////                                                    .show();
//                                        }
//                                    })
//                            .build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
//    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    private void BannerAd(AdRequest adRequest) {
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        //Toast.makeText(mContext, "BANNER Test Device = "+adRequest.isTestDevice(mContext), Toast.LENGTH_LONG).show();


        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //  Toast.makeText(mContext, "BANNER ErrorCode = "+errorCode, Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
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
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    public RewardedAd createAndLoadRewardedAd() {
        rewardedAd = new RewardedAd(this,
                //    "ca-app-pub-3940256099942544/5224354917");// Test ad credentials
                "ca-app-pub-6756023122563497/6395539305");// Live ad credentials

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


            }
        };
        rewardedAd.loadAd(new PublisherAdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }


    private void initCalls() {
        init();
        initViews();
        initViewHolder();
        CreatePrescription.certificate_selection = false;

        findViewById(R.id.statics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this,StaticsBludocActivity.class));

            }
        });

        findViewById(R.id.appointment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                startActivity(new Intent(HomeActivity.this, AppointmentActivity.class));
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

                        checkSubsciptionForAppointment();

                    }
                } else {
                    popup();
                }

            }
        });


        findViewById(R.id.digital_clinic).setOnClickListener(v -> {

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
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                return;

                            }
                        } else {
                            popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());

                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            return;
                        } else {

                            appointment = manager.getObjectAppointmentDetails(this, "appointment");

                            if (appointment != null &&
                                    !TextUtils.isEmpty(appointment.getDoc_name()) &&
                                    !TextUtils.isEmpty(appointment.getReg_no()) &&
                                    !TextUtils.isEmpty(appointment.getMail_id()) &&
                                    !TextUtils.isEmpty(appointment.getCont_no())) {

                                startActivity(new Intent(HomeActivity.this, DigitalClinicConfirmationActivity.class));

                            } else {

                                startActivity(new Intent(HomeActivity.this, DigitalClinicActivity.class));

                            }
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

                    checkSubsciptionForDigitalClinic();
                }

            } else {
                popup();
            }

        });

        findViewById(R.id.add_pharmacist).setOnClickListener(v -> {

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
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                return;
                            }
                        } else {
                            popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                        } else {
                            startActivity(new Intent(HomeActivity.this, AllPharmacistActivity.class));
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

                    checkSubsciptionForReferralCenter();

                }
            } else {
                popup();
            }

        });
        //transparentStatusAndNavigation();
        //  setState();
    }

    private void checkSubsciptionForAppointment() {

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(this, "profile");
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
                popupPausedForChecking("Doctor Appointment");
                return;

            }
        } else {
            popupPausedForChecking("Doctor Appointment");
            return;
        }

        if (days_left_free < 0) {
            popupPausedForChecking("Doctor Appointment");
        } else {
            startActivity(new Intent(HomeActivity.this, AppointmentActivity.class));
        }

    }

    private void checkSubsciptionForReferralCenter() {

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(this, "profile");
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
                popupPausedForChecking("Referral Center");
                return;

            }
        } else {
            popupPausedForChecking("Referral Center");
            return;
        }

        if (days_left_free < 0) {
            popupPausedForChecking("Referral Center");
        } else {
            startActivity(new Intent(HomeActivity.this, AllPharmacistActivity.class));
        }

    }

    private void checkSubsciptionForDigitalClinic() {

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(this, "profile");
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
                popupPausedForChecking("My Digital Clinic");
                return;

            }
        } else {
            popupPausedForChecking("My Digital Clinic");
            return;
        }

        if (days_left_free < 0) {
            popupPausedForChecking("My Digital Clinic");
        } else {

            appointment = manager.getObjectAppointmentDetails(this, "appointment");

            if (appointment != null &&
                    !TextUtils.isEmpty(appointment.getDoc_name()) &&
                    !TextUtils.isEmpty(appointment.getReg_no()) &&
                    !TextUtils.isEmpty(appointment.getMail_id()) &&
                    !TextUtils.isEmpty(appointment.getCont_no())) {

                startActivity(new Intent(HomeActivity.this, DigitalClinicConfirmationActivity.class));

            } else {

                startActivity(new Intent(HomeActivity.this, DigitalClinicActivity.class));

            }
        }

    }

    private void popupPausedForChecking(String keywords) {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment= new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords", keywords);
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(getSupportFragmentManager(),"");

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

    private void popupPaused(String s) {
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
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText(s);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

                getSupportFragmentManager().popBackStackImmediate();

            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

                getSupportFragmentManager().popBackStackImmediate();

            }
        });
        dialog_data.show();
    }

    private void initViews() {
        lLayout = new GridLayoutManager(this, 2);


        tv_premium_top = findViewById(R.id.tv_premium_top);
        ll_premium = findViewById(R.id.ll_premium);
        ll_premium.setPadding(20, 30, 20, 30);
        tv_days_left = findViewById(R.id.tv_days_left);
        History = findViewById(R.id.History);
        wahtsappTest = findViewById(R.id.wahtsappTest);
        prescribe = findViewById(R.id.prescribe);
        my_template = findViewById(R.id.my_template);
        //  buy_subscription= findViewById(R.id.buy_subscription);
        refer_app = findViewById(R.id.refer_app);
        close_pop = findViewById(R.id.close_pop);

        fl_progress_layout = findViewById(R.id.fl_progress_layout);
        head_dot = findViewById(R.id.head_dot);
        dot_parent = findViewById(R.id.dot_parent);
        dot_parent.setVisibility(View.GONE);

        fl_progress_layout.setVisibility(View.GONE);
        // fl_progress_layout.setVisibility(View.VISIBLE);
        // setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.add_pharmacist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, AllPharmacistActivity.class));

            }
        });

        findViewById(R.id.statics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, InvoiceActivity.class));

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);

        TextView Log_name = headerView.findViewById(R.id.Log_name);

        ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");

        if (!TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.Subscription_packages).setVisible(false);

        }

        /*String name_ = manager.getPreferences(mContext,"name").trim();
        if(name_.contains("Dr.")) {
            name_ = name_.replace("Dr.", "");
            manager.setPreferences(mContext, "name", name_.trim());
        }
        String name_2 = manager.getPreferences(mContext,"name").trim();
        if(name_2.contains("Dr."))
        {
            name_2 = name_2.replace("Dr.","");
            manager.setPreferences(mContext,"name",name_2.trim());
        }*/
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "name").trim())) {
            Log_name.setText(manager.getPreferences(mContext, "name").trim().trim());
        }
        //handling floating action menu
        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.btn_desktop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!("").equalsIgnoreCase(manager.getPreferences(HomeActivity.this, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(HomeActivity.this, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(HomeActivity.this, "profile");
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
                                popupPausedForChecking("");
                                return;

                            }
                        } else {
                            popupPausedForChecking("");
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPausedForChecking("");
                        } else {

                            popupDesktopVersion();

                        }

                    }
                } else {
                    popup();
                }
            }
        });

        wahtsappTest.setVisibility(View.GONE);
        wahtsappTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* if (isWhatsappInstalled) {
                 *//*Uri uri = Uri.parse("smsto:" + "+918668931702");
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    sendIntent.putExtra("sms_body", "Hii this is a test message for Whatsapp");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);*//*

                        PackageManager pm = mContext.getPackageManager();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    R.drawable.history);;
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);
                            Uri imageUri = Uri.parse(path);

                            @SuppressWarnings("unused")
                            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                            Intent waIntent = new Intent(Intent.ACTION_SENDTO);
                           // waIntent.setDataAndType(imageUri,"image/*");
                            waIntent.setPackage("com.whatsapp");
                           // waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
                            String url = "https://api.whatsapp.com/send?phone="+ " 918668931702" +"&text=" + URLEncoder.encode("Hii this is a test message for Whatsapp", "UTF-8")+"&image="+imageUri;
                            waIntent.setData(Uri.parse(url));

                            waIntent.putExtra(Intent.EXTRA_TEXT, "Image URL");
                            mContext.startActivity(Intent.createChooser(waIntent, "Share with"));
                        } catch (Exception e) {
                            Log.e("Error on sharing", e + " ");
                            Toast.makeText(mContext, "App not Installed", Toast.LENGTH_SHORT).show();
                        }


                } else {
                    Toast.makeText(mContext, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);

                }*/
                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                if (isWhatsappInstalled) {
                    PackageManager packageManager = mContext.getPackageManager();
                    // Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                    try {
                        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                R.drawable.history);
                        ;
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", "null");
                        Uri imageUri = Uri.parse(path);


                        String smsNumber = "917768055620"; // E164 format without '+' sign
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                        sendIntent.setPackage("com.whatsapp");
                        if (sendIntent.resolveActivity(mContext.getPackageManager()) == null) {
                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (sendIntent.resolveActivity(packageManager) != null) {
                            mContext.startActivity(sendIntent);
                        } else
                            Toast.makeText(HomeActivity.this, "Resolve activity Null", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(HomeActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();

            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
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

                        startActivity(new Intent(HomeActivity.this, HistoryActivity.class));

                    }
                } else {
                    popup();
                }
            }
        });


        prescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                                } else
                                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            } else
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());

                            if (days_left_free < 0) {
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            } else {
                                CreatePrescription.backCheckerFlag = false;
                                CreatePrescription.NEWaddMedicinesArrayList = new ArrayList<>();
                                CreatePrescription.NEWaddLabTestArrayList = new ArrayList<>();
                                CreatePrescription myFragment = new CreatePrescription();

                                getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();
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

                        CreatePrescription.backCheckerFlag = false;
                        CreatePrescription.NEWaddMedicinesArrayList = new ArrayList<>();
                        CreatePrescription.NEWaddLabTestArrayList = new ArrayList<>();
                        CreatePrescription myFragment = new CreatePrescription();
                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();

                    }
                } else {
                    popup();
                }
            }
        });


//        new_patient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
//                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
//                    if (!("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {
//
//                        if (responseProfileDetails.getAccess().equals("Approve")) {
//
//                            String sub_valid = "", premium_valid = "";
//                            boolean flag_reset_paid = false;
//                            Date date1 = null, date2 = null;
//                            int days_left_free = 0, days_left_paid = 0;
//                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//                            ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
//                            if (profileDetails.getSubcriptions() != null) {
//                                if (profileDetails.getSubcriptions().size() != 0) {
//                                    for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
//                                        if (!si.getHospital_code().equals("")) {
//                                            try {
//                                                Calendar c2 = Calendar.getInstance();
//                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//                                                Date dateEnd = dateFormat.parse(si.getEnd());
//                                                Date c = Calendar.getInstance().getTime();
//                                                assert dateEnd != null;
//
//                                                //premium_valid = si.getDays();
//                                                Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
//                                                // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
//                                                // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));
//
//
//                                                try {
//                                                    date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
//                                                    date2 = dateFormat.parse(si.getEnd());
//                                                    Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
//                                                    flag_reset_free = true;
//
//                                                    String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");
//
//                                                    days_left_free = days_left_free + Integer.parseInt(splited[0]);
//                                                    // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
//                                                    // break;
//                                                    sub_valid = si.getEnd();
//                                                } catch (ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");
//
//                                            } catch (ParseException pe) {
//                                                // handle the failure
//                                                flag_reset_free = false;
//                                            }
//
//                                        }
//                                    }
//
//                                } else
//                                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
//                            } else
//                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
//
//                            if (days_left_free < 0) {
//                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
//                            } else {
//                                PatientRegistration myFragment = new PatientRegistration();
//                                Bundle bundle = new Bundle();
//                                bundle.putBoolean("isFromHome", true);
//                                myFragment.setArguments(bundle);
//                                getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
//                            }
////Now run it
//                        } else if (responseProfileDetails.getAccess().equals("Pending")) {
//                            popupPaused("Your account is pending for approval. Please contact " + responseProfileDetails.getHospital_name());
//
//                        } else if (responseProfileDetails.getAccess().equals("Remove")) {
//                            popupPaused("You have been removed from " + responseProfileDetails.getHospital_name() + ". You now have accesses like BluDoc standard user");
//                            //popupHospitalCode();
//                        } else if (responseProfileDetails.getAccess().equals("Pause")) {
//
//                            popupPaused("Your subscription is paused. Please contact to " + responseProfileDetails.getHospital_name());
//
//                        } else {
//                            popupAccess();
//                        }
//
//                    } else {
//
//                        PatientRegistration myFragment = new PatientRegistration();
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("isFromHome", true);
//                        myFragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
//
//                    }
//                } else {
//                    popup();
//                }
//
//            }
//        });

        my_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                                } else
                                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            } else
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());

                            if (days_left_free < 0) {
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                            } else {
                                TemplateSelection myFragment = new TemplateSelection();
                                Bundle bundle = new Bundle();
                                bundle.putString("patient_id", "");
                                bundle.putString("definer", "home");

                                myFragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
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

                        TemplateSelection myFragment = new TemplateSelection();
                        Bundle bundle = new Bundle();
                        bundle.putString("patient_id", "");
                        bundle.putString("definer", "home");

                        myFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();

                    }
                } else {
                    popup();
                }


            }
        });


        refer_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bludoc : E-Prescription for doctors");
                    String shareMessage = "\nHey Doctor! Try this new application BluDoc : E-prescription app for Doctors. Make your prescriptions easy, quick, smart, paper free and send them to your patients across the world on a single click. I have started using it. Download BluDoc now!!\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.likesby.bludoc";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

    }

    public static void goToPrescription() {

       /* apiViewHolder.getPatients(manager.getPreferences(ctx, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePatients);*/

        CreatePrescription myFragment = new CreatePrescription();

        fragmanager.beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return app_installed;
    }


    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);


//        apiViewHolder.getBanners()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseBanners);
//
//        apiViewHolder.getMedicines(manager.getPreferences(mContext,"doctor_id"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseMedicines);
//
//        apiViewHolder.getProfileDetails(manager.getPreferences(mContext,"doctor_id"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseProfile);
//
//        updatecustomer();
    }

//    public void getProfile(){
//        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
//
//        apiViewHolder.getAllPrescription(manager.getPreferences(mContext, "doctor_id"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responseHistory);
//    }
//
//    private SingleObserver<com.likesby.bludoc.ModelLayer.Entities.History> responseHistory = new SingleObserver<com.likesby.bludoc.ModelLayer.Entities.History>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(com.likesby.bludoc.ModelLayer.Entities.History response) {
//            if (response != null) {
//
//                Log.e(TAG, "responseTemplates: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//
//
//                } else if (response.getMessage().equals("Prescription")) {
//
//                }
//
//            }
//            else
//            {
//
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//
//            Log.e(TAG, "onError: responseTemplates >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    public ArrayList<PatientsItem> getpatients(){
//        return patientsItemArrayList;
//    }
//    public void setPatients(ArrayList<PatientsItem> patients){
//        Collections.reverse(patients);
//        patientsItemArrayList = new ArrayList<>();
//         patientsItemArrayList= patients;
//    }
//
//    public void updatecustomer(){
//        apiViewHolder.getPatients(manager.getPreferences(mContext, "doctor_id"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(responsePatients);
//    }
//
//    private SingleObserver<ResponsePatients> responsePatients = new SingleObserver<ResponsePatients>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponsePatients response) {
//            if (response != null) {
//
//                Log.e(TAG, "ResponseMedicines: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//
//
//                } else if (response.getMessage().equals("patients")) {
//                    patientsItemArrayList = new ArrayList<>();
//                    patientsItemArrayList = response.getPatients();
//                    Collections.reverse(patientsItemArrayList);
//                }
//
//            }
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    SingleObserver<ProfileDetails> responseProfile = new SingleObserver<ProfileDetails>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ProfileDetails response) {
//            if (response != null) {
//
//                Log.e(TAG, "profileDetails: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//                } else if (response.getMessage().equals("Profile Details")) {
//                    manager.setPreferences(mContext,"doctor_id",response.getDoctorId());
//                    manager.setPreferences(mContext,"name",response.getName());
//                    manager.setPreferences(mContext,"email",response.getEmail());
//                    manager.setPreferences(mContext,"mobile",response.getMobile());
//                    manager.setPreferences(mContext,"registration_no",response.getRegistrationNo());
//                    manager.setPreferences(mContext,"speciality_id",response.getSpecialityId());
//                    manager.setPreferences(mContext,"ug_id",response.getUgId());
//                    manager.setPreferences(mContext,"pg_id",response.getPgId());
//                    manager.setPreferences(mContext,"designation_id",response.getDesignationId());
//                    manager.setPreferences(mContext,"addtional_qualification",response.getAddtionalQualification());
//                    manager.setPreferences(mContext,"mobile_letter_head",response.getMobileLetterHead());
//                    manager.setPreferences(mContext,"email_letter_head",response.getEmailLetterHead());
//                    manager.setPreferences(mContext,"working_days",response.getWorkingDays());
//                    manager.setPreferences(mContext,"visiting_hr_from",response.getVisitingHrFrom());
//                    manager.setPreferences(mContext,"visiting_hr_to",response.getVisitingHrTo());
//                    manager.setPreferences(mContext,"close_day",response.getCloseDay());
//                    manager.setPreferences(mContext,"speciality_name",response.getSpecialityName());
//                    manager.setPreferences(mContext,"ug_name",response.getUgName());
//                    manager.setPreferences(mContext,"pg_name",response.getPgName());
//                    manager.setPreferences(mContext,"designation_name",response.getDesignationName());
//                    manager.setPreferences(mContext,"signature",response.getSignature());
//                    manager.setPreferences(mContext,"logo",response.getLogo());
//                    manager.setPreferences(mContext,"image",response.getImage());
//                    manager.setPreferences(mContext, "clinic_name", response.getClinicName());
//                    manager.setPreferences(mContext, "clinic_address", response.getClinicAddress());
//                }
//            }
//            else
//            {
//                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Log.e(TAG, "onError: profileDetails >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//
//
//    SingleObserver<ResponseMedicines> responseMedicines = new SingleObserver<ResponseMedicines>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseMedicines response) {
//            if (response != null) {
//
//                Log.e(TAG, "ResponseMedicines: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//                } else if (response.getMessage().equals("medicines")) {
//                    medicinesItemArrayList = new ArrayList<>();
//                    medicinesItemArrayList = response.getMedicines();
//                    fl_progress_layout.setVisibility(View.GONE);
//                }
//            }
//            else
//            {
//                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            fl_progress_layout.setVisibility(View.GONE);
//            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    SingleObserver<ResponseBanners> responseBanners = new SingleObserver<ResponseBanners>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseBanners response) {
//            if (response != null) {
//
//                Log.e(TAG, "responseBanners: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//                } else if (response.getMessage().equals("Banners")) {
//                    bannerArrayList = new ArrayList<>();
//                    bannerArrayList = response.getBanners();
//                    init();
//                }
//            }
//            else
//            {
//                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Log.e(TAG, "onError: responseBanners >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//
////    private void getCourse(){
////        //if(subscriptionList.size()!=0) {
////        if (Utils.isConnectingToInternet(this)) {
////
////            apiViewHolder.getSubject(stateId,cityId,courseId,Streamid,userId)
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(responseSubject);
////        } else {
////            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    SingleObserver<CategoriesResponse> responseSubject = new SingleObserver<CategoriesResponse>() {
////        @Override
////        public void onSubscribe(Disposable d) {
////            mBag.add(d);
////        }
////
////        @Override
////        public void onSuccess(CategoriesResponse response) {
////            if (response != null) {
////                Log.e(TAG, "responseState: >> " + response.getMessage());
////                if (response.getMessage() == null) {
////                    Toast.makeText(HomeActivity.this, "No States", Toast.LENGTH_SHORT).show();
////
////                }
////                else if  (response.getCategories()!=null) {
////                    //  frame.setBackgroundResource(R.mipmap.banner4);
////                    rView.setVisibility(View.VISIBLE);
////                    login.setText("Select Category");
////                    Log.e("responseState","get states");
////                    categoryArrayList = new ArrayList<>();
////                    categoryArrayList = response.getCategories();
////
////                    //  fillAutoSpacingLayout();
////
////                    CategoryAdapter stateAdapter= new CategoryAdapter(HomeActivity.this,categoryArrayList);
////                    rView.setAdapter(stateAdapter);
////
////
////                } else {
////                    Toast.makeText(HomeActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
////
////                }
////            }
////        }
////        @Override
////        public void onError(Throwable e) {
////            Log.e(TAG, "onError: responseUserRegistration >> " + e.toString());
////            //intentCall();
////            Toast.makeText(HomeActivity.this, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
////        }
////    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Profile) {

            Intent intent1 = new Intent(HomeActivity.this, Profile.class);
            startActivity(intent1);
            // Handle the camera action
        } else if (id == R.id.My_Subscription) {

            Intent intent1 = new Intent(HomeActivity.this, MySubscription.class);
            startActivity(intent1);

            //popupsubscriptiom();
//        } else if (id == R.id.Assign_a_task) {
//           // popupRefer();
//
//            popupAssign();


        } else if (id == R.id.letter_head_preview) {

//            Sample myFragment = new Sample();
//            getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();

            Intent intent = new Intent(this, SampleActivity.class);
            startActivity(intent);

        } else if (id == R.id.Rate_and_review) {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
            }
            return true;
        } else if (id == R.id.Feedback) {
            Intent intent1 = new Intent(HomeActivity.this, Feedback.class);
            startActivity(intent1);
        } else if (id == R.id.Contact_Us) {
            Intent intent1 = new Intent(HomeActivity.this, Contact.class);
            startActivity(intent1);
        } else if (id == R.id.About_Us) {
            Intent intent1 = new Intent(HomeActivity.this, PrivacyPolicy.class);
            startActivity(intent1);
        } else if (id == R.id.FAQ) {
            Intent intent1 = new Intent(HomeActivity.this, FAQ.class);
            startActivity(intent1);
        } else if (id == R.id.logout) {
            popuplogout();
        } else if (id == R.id.Subscription_packages) {
            Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
            startActivity(intent1);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void init() {
       /* for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);*/

        mPager = (ViewPager) findViewById(R.id.pager);
        bannerArrayList = SplashActivity.bannerArrayList;
        mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this, SplashActivity.bannerArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = bannerArrayList.size();

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
        }, 5000, 5000);

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
    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(this)) {

            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(HomeActivity.this, "doctor_id"),"","");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    int count = 0;

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();
                        if (appointmentListModels.size() > 0) {

                            for (AppointmentListModel appointmentListModel : appointmentListModels) { // compare today date

                                DateTime dateTime;

                                try {
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                                }

                                DateTime today = new DateTime();

                                if (dateTime.getYear() == today.getYear() &&
                                        dateTime.getMonthOfYear() == today.getMonthOfYear() &&
                                        dateTime.getDayOfMonth() == today.getDayOfMonth() && today.getMillis()  < dateTime.getMillis() + 60000) {

                                    count++;

                                }

                                dot_parent.setVisibility(View.VISIBLE);

                                if(count == 0)
                                    dot_parent.setVisibility(View.GONE);

                            }

                        } else {

                            dot_parent.setVisibility(View.GONE);

                        }

                    } else {

                        dot_parent.setVisibility(View.GONE);

                        try {
                            Toast.makeText(HomeActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {

                    dot_parent.setVisibility(View.GONE);

                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(HomeActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AllGetAppointment();

        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");

        if ((profileDetails.getHospitalCode() != null && !profileDetails.getHospitalCode().equals(""))) {
            if (profileDetails.getAccess().equals("Pause")) {
                popupPaused("Your subscription is paused. Please contact to " + profileDetails.getHospital_name());

                tv_days_left.setText("Your subscription is paused");
                showNativeAdFlag = false;
                ll_premium.setVisibility(View.VISIBLE);
                if (manager.contains(mContext, "show_banner_ad"))
                    manager.deletePreferences(mContext, "show_banner_ad");

            } else if (profileDetails.getAccess().equals("Approve")) {

                String sub_valid = "", premium_valid = "";
                boolean flag_reset_paid = false;
                Date date1 = null, date2 = null;
                int days_left_free = 0, days_left_paid = 0;
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                ResponseProfileDetails profileDetailsSubmit = manager.getObjectProfileDetails(mContext, "profile");
                if (profileDetailsSubmit.getSubcriptions() != null) {

//                    ArrayList<SubcriptionsItem> subcriptions = profileDetailsSubmit.getSubcriptions();
//                    Comparator<SubcriptionsItem> comparator = new Comparator<SubcriptionsItem>() {
//                        @Override
//                        public int compare(SubcriptionsItem movie, SubcriptionsItem t1) {
//                            return movie.genre.compareTo(t1.genre);
//                        }
//                    };


                    if (profileDetailsSubmit.getSubcriptions().size() != 0) {
                        for (SubcriptionsItem si : profileDetailsSubmit.getSubcriptions()) {
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
                                        flag_reset_free = true;

                                        String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                        days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                        // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                        // break;
                                        sub_valid = si.getEnd();
                                        tv_days_left.setText("Your subscription is valid till " + sub_valid + ".");

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
                        popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                        tv_days_left.setText("Your subscription has expired. Please contact " + profileDetails.getHospital_name());
                    }
                } else {
                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                    tv_days_left.setText("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                }

                if (days_left_free < 0) {
                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                    tv_days_left.setText("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                }
//Now run it
            } else if (profileDetails.getAccess().equals("Pending")) {
                tv_days_left.setText("Pending for Approval");
            }

        } else {

            String sub_valid = "", premium_valid = "";
            boolean flag_reset_paid = false;
            Date date1 = null, date2 = null;
            int days_left_free = 0, days_left_paid = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            if (profileDetails.getSubcriptions() != null) {
                if (profileDetails.getSubcriptions().size() != 0) {
                    for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                        if (si.getType().equals("Free")) {
                            try {

                                Calendar c2 = Calendar.getInstance();
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                Date dateEnd = dateFormat.parse(si.getEnd());
                                Date c = Calendar.getInstance().getTime();
                                assert dateEnd != null;
                                premium_valid = si.getDays();
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

                        } else if (si.getType().equals("Paid")) {
                            try {
                                Calendar c2 = Calendar.getInstance();
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                Date dateEnd = dateFormat.parse(si.getEnd());
                                Date c = Calendar.getInstance().getTime();
                                assert dateEnd != null;
                                premium_valid = si.getDays();
                                Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                date2 = dateFormat.parse(si.getEnd());
                                // date2 = dateFormat.parse("12-12-2020");
                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                flag_reset_paid = true;

                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                days_left_paid = days_left_paid + Integer.parseInt(splited[0]);
                                sub_valid = si.getEnd();
                                // break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (flag_reset_paid) {   //Premium Subscription
                        if (days_left_paid < 1) {
                            tv_premium_top.setVisibility(View.GONE);
                            showNativeAdFlag = false;
                            // popupFreeSubscription("",false);
                            manager.setPreferences(mContext, "show_banner_ad", "true");
                            BannerAd(adRequest);
                            addflag = true;
                            ll_premium.setVisibility(View.VISIBLE);
                            ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_btn_gradient));
                            ll_premium.setPadding(20, 30, 20, 30);
                            tv_days_left.setText("Expired!! Upgrade to premium to use all features.");
                            ll_premium.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                                    if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                        Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                        startActivity(intent1);

                                    } else {

//                                        Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        } else {
                            showNativeAdFlag = false;
                            //  popupFreeSubscription("",false);
                            //   Toast.makeText(mContext, ""+days_left_paid, Toast.LENGTH_SHORT).show();
                            if (days_left_paid < 11) {
                                ll_premium.setVisibility(View.VISIBLE);
                                if (manager.contains(mContext, "show_banner_ad"))
                                    manager.deletePreferences(mContext, "show_banner_ad");
                                tv_days_left.setText("Your subscription will end on " + sub_valid + ".\n Click here to renew");
                                ll_premium.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                                        if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                            Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                            startActivity(intent1);

                                        } else {

//                                            Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                showNativeAdFlag = false;
                                ll_premium.setVisibility(View.VISIBLE);

                                if (manager.contains(mContext, "show_banner_ad"))
                                    manager.deletePreferences(mContext, "show_banner_ad");
                                tv_days_left.setText("Your subscription is valid till " + sub_valid + ".");
                                ll_premium.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                                        if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                            Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                            startActivity(intent1);

                                        } else {

//                                            Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            /*ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_gradient));
                            ll_premium.setPadding(20,30,20,30);

                            tv_days_left.setTextColor(getResources().getColor(R.color.colorWhite));*/
                            }

                            if (manager.contains(mContext, "purchased_new")) {
                                if (manager.contains(mContext, "show_banner_ad"))
                                    manager.deletePreferences(mContext, "show_banner_ad");
                                if (manager.getPreferences(mContext, "purchased_new").equalsIgnoreCase("true")) {
                                    ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_gradient));
                                    ll_premium.setPadding(20, 30, 20, 30);
                                    tv_days_left.setText("Congratulations!! \n You are now a premium member. \n Your subscription is valid till " + sub_valid);
                                    tv_days_left.setTextColor(mContext.getResources().getColor(R.color.white));
                                    showNativeAdFlag = false;
                                } else {
                                    ll_premium.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else   //Free Subscription
                    {
                        if (flag_reset_free) {
                            if (manager.contains(mContext, "show_banner_ad"))
                                manager.deletePreferences(mContext, "show_banner_ad");
                            manager.setPreferences(mContext, "show_banner_ad", "true");
                            BannerAd(adRequest);
                            //  popupFreeSubscription(""+days_left_free,true);
                            tv_days_left.setText("Congratulations!! \nYou have been offered a " + premium_valid + " days free trial.\nValid till - " + sub_valid + " \nUpgrade to premium to use all features.\nClick here to upgrade");
                            tv_premium_top.setVisibility(View.VISIBLE);
                            showNativeAdFlag = false;
                            ll_premium.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");

                                    if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                        Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                        startActivity(intent1);

                                    } else {

//                                        Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        } else { // Expired
//                        if (profileDetails.getHospitalCode()!= null && !profileDetails.getHospitalCode().equals("") ) {
//                            if(profileDetails.getAccess().equals("Paused"))
//                            popupPaused("Your subscription is paused. Please contact hospital");
//
//                        }

                            if (profileDetails.getHospitalCode() != null && !profileDetails.getHospitalCode().equals("")) {

                                popupPaused("Your subscription has expired. Please contact " + profileDetails.getHospital_name());

                            }


                            manager.setPreferences(mContext, "show_banner_ad", "true");
                            BannerAd(adRequest);
                            addflag = true;
                            ll_premium.setVisibility(View.GONE);
                            ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_btn_gradient));
                            ll_premium.setPadding(20, 30, 20, 30);
                            tv_days_left.setText("Upgrade to premium to use all features.\nClick here to upgrade");
                            showNativeAdFlag = true;
                            ll_premium.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");

                                    if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                        Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                        startActivity(intent1);

                                    } else {

//                                        Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }

                    }
                } else if (!flag_reset_free) {
//                if (profileDetails.getHospitalCode() != null && !profileDetails.getHospitalCode().equals("")) {
//                    if (profileDetails.getAccess().equals("Paused"))
//                        popupPaused("Your subscription is paused. Please contact hospital");
//
//                }

                    if (profileDetails.getHospitalCode() != null && !profileDetails.getHospitalCode().equals("")) {

                        popupPaused("Your subscription has expired. Please contact hospital admin");
                    }

                    BannerAd(adRequest);
                    addflag = true;
                    manager.setPreferences(mContext, "show_banner_ad", "true");
                    tv_premium_top.setVisibility(View.GONE);
                    ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_round_btn_gradient_mojito));
                    showNativeAdFlag = true;
                    // popupFreeSubscription("", false);
                    tv_days_left.setTextColor(getResources().getColor(R.color.colorBlue));
                    tv_days_left.setText("Upgrade to Premium");
                    ll_premium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");

                            if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                                Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                                startActivity(intent1);

                            } else {

//                                Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            } else {
                manager.setPreferences(mContext, "show_banner_ad", "true");
                BannerAd(adRequest);
                addflag = true;
                showNativeAdFlag = true;
            }

            if (manager.contains(mContext, "show_banner_ad")) {
                if (manager.contains(mContext, "show_time")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
                    Log.e("DIFF", "____________________ " + DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "show_time"), DateUtils.currentDateTime()));

                    if (DateUtils.getDateDiff(dateFormat, manager.getPreferences(mContext, "show_time"), DateUtils.currentDateTime()) > 0) {
                        manager.setPreferences(mContext, "show_time", DateUtils.currentDateTime());
                        initInterstitialAd(adRequestInterstitial);
                    }


                } else {
                    manager.setPreferences(mContext, "show_time", DateUtils.currentDateTime());

                    initInterstitialAd(adRequestInterstitial);
                }

            }
        }
        //  BannerAd();
        // popupFreeSubscription();
        // Toast.makeText(mContext, ""+CreatePrescription.backCheckerFlag, Toast.LENGTH_SHORT).show();
    }

    private void popuplogout() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_logout);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_no = dialog_data.findViewById(R.id.btn_no);
        Button btn_yes = dialog_data.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("status", 0).edit().clear().commit();
                myDB.deleteAllMedicines();
                myDB.deleteAllLabTests();
                SplashActivity splashActivity = new SplashActivity();
                splashActivity.setPatientsnull();
                Intent i = new Intent(mContext, SplashActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });
        if (dialog_data != null && !dialog_data.isShowing())
            dialog_data.show();
    }

    private void popupExit() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_exit);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);
        FrameLayout fl_layout = dialog_data.findViewById(R.id.fl_layout);

        ProgressBar pb = dialog_data.findViewById(R.id.pb);

        final Button btn_no = dialog_data.findViewById(R.id.btn_no);
        final Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        btn_no.setVisibility(View.VISIBLE);
        btn_yes.setVisibility(View.VISIBLE);
        fl_layout.setVisibility(View.GONE);
       /* if(showNativeAdFlag){
            NativeAd(dialog_data,pb,btn_yes,btn_no);

        }
        else {
            btn_no.setVisibility(View.VISIBLE);
            btn_yes.setVisibility(View.VISIBLE);
            fl_layout.setVisibility(View.GONE);
        }
        CountDownTimer countDownTimer  = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                btn_no.setVisibility(View.VISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();*/
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText("Are you sure you want to exit?");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.finish();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        if (dialog_data != null)
            dialog_data.show();
    }


    private void popupAssign() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_assign);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_add = dialog_data.findViewById(R.id.btn_register);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        dialog_data.show();
    }


    private void popup() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_no_registration);

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
                Intent intent = new Intent(mContext, RegisterDetails.class);
                startActivity(intent);

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


    private void popupDesktopVersion() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_desktop_version);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_email = dialog_data.findViewById(R.id.btn_email);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                Intent intentEmail = new Intent();
                intentEmail.setType("message/rfc822");
                intentEmail.setAction(Intent.ACTION_SEND);
                intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Get a BluDoc Desktop Version");
                intentEmail.putExtra(Intent.EXTRA_TEXT, "Dear User,\nKindly click on the link below to get BluDoc desktop version. \n https://bludoc.in/web");
                intentEmail.setPackage("com.google.android.gm");
                //intent.setDataAndType(Uri.parse("mailto:"+GeneratePres.patient_item.getPEmail()),"message/rfc822"); // or just "mailto:" for blank

                Intent shareIntent = Intent.createChooser(intentEmail, "Send mail...");
                mContext.startActivity(shareIntent);

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


    private void popupsubscriptiom() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_refer_app);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);
        TextView tv_congratulations = dialog_data.findViewById(R.id.tv_congratulations);
        TextView tv_msg = dialog_data.findViewById(R.id.tv_msg);


        String sub_valid = "", premium_valid = "";
        boolean flag_reset_free = false, flag_reset_paid = false;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
        if (profileDetails.getSubcriptions() != null) {
            if (profileDetails.getSubcriptions().size() != 0) {
                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                    if (si.getType().equals("Free")) {
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                            Date date1 = null, date2 = null;
                            try {
                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                date2 = dateFormat.parse(si.getEnd());
                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                flag_reset_free = true;

                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                // break;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                        } catch (ParseException pe) {
                            // handle the failure
                            flag_reset_free = false;
                        }

                    } else if (si.getType().equals("Paid")) {
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));

                            Date date1 = null, date2 = null;

                            date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                            date2 = dateFormat.parse(si.getEnd());
                            // date2 = dateFormat.parse("12-12-2020");
                            Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                            flag_reset_paid = true;

                            String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                            days_left_paid = days_left_paid + Integer.parseInt(splited[0]);
                            sub_valid = si.getEnd();
                            // break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (flag_reset_paid) {   //Premium Subscription
                    if (days_left_paid < 1) {

                        tv_msg.setText("Expired!! Upgrade to premium to use all features.");

                    } else {
                        //  popupFreeSubscription("",false);
                        //   Toast.makeText(mContext, ""+days_left_paid, Toast.LENGTH_SHORT).show();
                        if (days_left_paid < 11) {
                            tv_congratulations.setVisibility(View.VISIBLE);
                            tv_congratulations.setText("Premium");
                            tv_msg.setText("Your subscription will end on " + sub_valid + ".");

                        } else {
                            tv_congratulations.setVisibility(View.VISIBLE);
                            tv_congratulations.setText("Premium");

                            tv_msg.setText("Your subscription will end on " + sub_valid + ".");


                            /*ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_gradient));
                            ll_premium.setPadding(20,30,20,30);

                            tv_days_left.setTextColor(getResources().getColor(R.color.colorWhite));*/
                        }


                    }
                } else   //Free Subscription
                {
                    if (flag_reset_free) {

                        tv_msg.setText("Congratulations!! You have been offered a 5 days free trial.\nDays left - " + days_left_free + "\nUpgrade to premium to use all features.");

                    } else {

                        tv_msg.setText("Upgrade to premium to use all features.");


                    }

                }


                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_data.dismiss();

                    }
                });
                dialog_data.show();
            }
        }
    }

    private void popupFreeSubscription(String days_left, boolean flagger) {
        if (dialog_data != null && dialog_data.isShowing())
            dialog_data.dismiss();

        dialog_data = new Dialog(mContext);

        if (flagger)
            dialog_data.setCancelable(true);
        else
            dialog_data.setCancelable(false);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_subscriptions);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);
        ProgressBar pb = dialog_data.findViewById(R.id.pb);
        ImageView iv_no_template = dialog_data.findViewById(R.id.iv_no_template);
        iv_no_template.setVisibility(View.GONE);
        if (!flagger)
            btn_close.setVisibility(View.GONE);

        final Button btn_upgrade_premium = dialog_data.findViewById(R.id.btn_upgrade_premium);
        final Button btn_watch_ad = dialog_data.findViewById(R.id.btn_watch_ad);
        FrameLayout fl_layout = dialog_data.findViewById(R.id.fl_layout);


        if (showNativeAdFlag) {

            btn_upgrade_premium.setVisibility(View.GONE);
            btn_watch_ad.setVisibility(View.GONE);
            //  NativeAd(dialog_data,pb,btn_upgrade_premium,btn_watch_ad);
        } else {
            btn_upgrade_premium.setVisibility(View.VISIBLE);
            btn_watch_ad.setVisibility(View.VISIBLE);
            fl_layout.setVisibility(View.GONE);
        }
        CountDownTimer countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                btn_upgrade_premium.setVisibility(View.VISIBLE);
                btn_watch_ad.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
        LinearLayout ll_congratulations = dialog_data.findViewById(R.id.ll_congratulations);
        LinearLayout ll_expired = dialog_data.findViewById(R.id.ll_expired);
        TextView tv_free_trial_days_left = dialog_data.findViewById(R.id.tv_free_trial_days_left);

        tv_free_trial_days_left.setText("Your free trial will end after " + days_left + " days.");
        if (flagger) {
            ll_congratulations.setVisibility(View.VISIBLE);
            ll_expired.setVisibility(View.GONE);
        } else {
            ll_congratulations.setVisibility(View.GONE);
            ll_expired.setVisibility(View.VISIBLE);
        }

        btn_upgrade_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

                ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");

                if (TextUtils.isEmpty(responseProfileDetails.getHospitalCode())) {

                    Intent intent1 = new Intent(HomeActivity.this, SubscriptionPackages.class);
                    startActivity(intent1);

                } else {

                    Toast.makeText(mContext, "You cannot access this page...", Toast.LENGTH_SHORT).show();

                }

            }
        });


        btn_watch_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                if (rewardedAd.isLoaded()) {

                    rewardedAd.show(HomeActivity.this, new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            super.onRewardedAdOpened();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            super.onRewardedAdClosed();
                            //  rewardedAd = createAndLoadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            flag_reset_free = true;
                            // rewardedAd = createAndLoadRewardedAd();
                            //   Toast.makeText(mContext, "Congratulations!! You won "+rewardItem.getAmount()+" "+rewardItem.getType(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int i) {
                            super.onRewardedAdFailedToShow(i);
                            flag_reset_free = true;
                        }
                    });
                } else {
                    //  Toast.makeText(HomeActivity.this, "Ad is not loaded", Toast.LENGTH_SHORT).show();
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

    public void transactFragment(boolean reload) {

        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (reload) {
            getSupportFragmentManager().popBackStack();
        }
        transaction.replace(R.id.homePageContainer, new CreatePrescription(), "prescription");
        transaction.addToBackStack(null);
        transaction.commit();

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialog.dismiss();

            }
        }, 1000);
    }

    private void popupExit2() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_exit);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);
        ProgressBar pb = dialog_data.findViewById(R.id.pb);

        final Button btn_no = dialog_data.findViewById(R.id.btn_no);
        final Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        FrameLayout fl_layout = dialog_data.findViewById(R.id.fl_layout);
        /*CountDownTimer countDownTimer  = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                btn_no.setVisibility(View.VISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();*/
        /*if(showNativeAdFlag){
            NativeAd(dialog_data,pb,btn_yes,btn_no);

        }
        else {
            btn_no.setVisibility(View.VISIBLE);
            btn_yes.setVisibility(View.VISIBLE);
            fl_layout.setVisibility(View.GONE);

        }*/
        btn_no.setVisibility(View.VISIBLE);
        btn_yes.setVisibility(View.VISIBLE);
        fl_layout.setVisibility(View.GONE);

        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText("Would you like to leave this page?");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                CreatePrescription.is_on_lab_test = false;
                CreatePrescription.is_on_case_history = true;
                CreatePrescription.is_on_medicines = false;
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStackImmediate();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        dialog_data.show();
    }

    private void popUpExitPage() {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_exit);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);
        ProgressBar pb = dialog_data.findViewById(R.id.pb);

        final Button btn_no = dialog_data.findViewById(R.id.btn_no);
        final Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        FrameLayout fl_layout = dialog_data.findViewById(R.id.fl_layout);
        /*CountDownTimer countDownTimer  = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                btn_no.setVisibility(View.VISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();*/
        /*if(showNativeAdFlag){
            NativeAd(dialog_data,pb,btn_yes,btn_no);

        }
        else {
            btn_no.setVisibility(View.VISIBLE);
            btn_yes.setVisibility(View.VISIBLE);
            fl_layout.setVisibility(View.GONE);

        }*/
        btn_no.setVisibility(View.VISIBLE);
        btn_yes.setVisibility(View.VISIBLE);
        fl_layout.setVisibility(View.GONE);

        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText("Would you like to Exit?");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        dialog_data.show();
    }

    @Override
    public void onBackPressed() {

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("prescription");
        if (myFragment != null && myFragment.isVisible()) {

            if (onBackClickListener2 != null && onBackClickListener2.onBackClick()) {
                return;
            }

//            popupExit2();
           /* androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
            builder.setMessage("Would you like leave this page?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //manager.getPreferences(Registration_.this, "service_provider");
                            CreatePrescription.is_on_lab_test = false;
                            CreatePrescription.is_on_case_history = true;
                            CreatePrescription.is_on_medicines = false;
                            FragmentManager fm = getSupportFragmentManager();
                            fm.popBackStackImmediate();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            androidx.appcompat.app.AlertDialog alert = builder.create();
            alert.show();*/

        } else {

            Fragment myFragment1 = (Fragment) getSupportFragmentManager().findFragmentByTag("first");
            if (myFragment1 != null && myFragment1.isVisible()) {

                super.onBackPressed();

            } else {

                if (onBackClickListener != null && onBackClickListener.onBackClick()) {
                    onBackClickListener = null;
                    return;
                } else {
                    popUpExitPage();
                }

            }

        }

    }
}
