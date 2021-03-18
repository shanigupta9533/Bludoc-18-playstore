package com.likesby.bludoc;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.Fragment.MyTemplates;
import com.likesby.bludoc.Fragment.MyTemplatesLabTest;
import com.likesby.bludoc.SessionManager.SessionManager;

import java.util.Objects;


public class TemplateSelection extends Fragment
{
    static Context mContext;
    SessionManager manager;
    View v;    public  static String patient_id="",definer="";
    FrameLayout fl_medicines,fl_lab_test;
    TextView tv_labtest,tv_medicines;
    ImageView back;
    String type="";
    boolean type_flag = false;
    private AdView mAdView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.template_selection, container, false);
        mContext = getContext();
        manager = new SessionManager();
        fl_medicines = v.findViewById(R.id.fl_medicines);
        fl_lab_test = v.findViewById(R.id.fl_lab_tests);
        tv_medicines = v.findViewById(R.id.tv_medicines);
        tv_labtest = v.findViewById(R.id.tv_labtest);
        mAdView = v.findViewById(R.id.adView);

        tv_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_medicines.setVisibility(View.VISIBLE);
                fl_lab_test.setVisibility(View.GONE);
                tv_medicines.setBackgroundColor(getResources().getColor(R.color.colorDarkestBlue));
                tv_labtest.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            }
        });

        tv_labtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_medicines.setVisibility(View.GONE);
                fl_lab_test.setVisibility(View.VISIBLE);
                tv_labtest.setBackgroundColor(getResources().getColor(R.color.colorDarkestBlue));
                tv_medicines.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            }
        });

        back = v.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePrescription.backCheckerFlag = true;
               FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fm.popBackStackImmediate();

                /* CreatePrescription myFragment = new CreatePrescription();
                Bundle bundle = new Bundle();

                bundle.putString("definer","temp");
                myFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.homePageContainer,
                        myFragment, "first").addToBackStack(null).commit();*/
            }
        });

        Bundle args = getArguments();
        assert args != null;
        {
            patient_id = args.getString("patient_id");
            definer = args.getString("definer");
            if(args.getString("type")!=null)
            {
                type = args.getString("type");
               // Toast.makeText(mContext, ""+type, Toast.LENGTH_SHORT).show();
                assert type != null;
                if(type.equals("medicine"))
                {
                    tv_medicines.performClick();
                    tv_labtest.setVisibility(View.GONE);
                }
                else  if(type.equals("lab"))
                {
                    tv_labtest.setVisibility(View.VISIBLE);
                    tv_labtest.performClick();
                    tv_labtest.setBackgroundColor(getResources().getColor(R.color.colorDarkestBlue));
                    tv_medicines.setVisibility(View.GONE);
                }
            }
        }
        if(manager.contains(mContext,"show_banner_ad"))
        BannerAd();
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    CreatePrescription.backCheckerFlag = true;
                    FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
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
        } );


        return v;
    }

    private  void BannerAd(){
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
               // Toast.makeText(mContext, "ErrorCode = "+errorCode, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        MyTemplates myFragment2 = new MyTemplates();
        Bundle bundle2 = new Bundle();
        bundle2.putString("patient_id",patient_id);
        bundle2.putString("definer","pres");

        myFragment2.setArguments(bundle2);
        getFragmentManager().beginTransaction().replace(R.id.fl_medicines, myFragment2,
                "first").disallowAddToBackStack().commit();

        MyTemplatesLabTest myFragment = new MyTemplatesLabTest();
        Bundle bundle = new Bundle();
        bundle.putString("patient_id",patient_id);
        bundle.putString("definer","pres");

        myFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_lab_tests, myFragment,
                "first").disallowAddToBackStack().commit();
    }

}
