package com.likesby.bludoc.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.likesby.bludoc.Adapter.BottomSheetAdapter;
import com.likesby.bludoc.Adapter.Pres_LabTest_adapter;
import com.likesby.bludoc.Adapter.Pres_adapter;
import com.likesby.bludoc.Adapter.SlidingImage_Adapter_GeneratedPrescription;
import com.likesby.bludoc.ModelLayer.Entities.BottomSheetItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.PrescriptionJSON;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.Doctor;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.likesby.bludoc.Fragment.CreatePrescription.certificate_selection;

public class GeneratePres2 extends Fragment {
    ArrayList<MedicinesItem> medicinesItemArrayListO = new ArrayList<>();
    Button generatePDF,back,btn_backbtn_edit_profile;
    Context mContext;
    Dialog dialog;
     Dialog dialog_data;
    View v;
    static RecyclerView rView,rViewlabtest;
    TextView textView_advice;
    static LinearLayoutManager lLayout,lLayout2;
    ArrayList<LabTestItem> labTestItem = new ArrayList<>();
    PrescriptionItem prescriptionItem = new PrescriptionItem();
    ImageView image_layout ,signature;
    TextView sig_text,title,textView_Clinic_name,textView_DocName,textView_degree,textView_res_num,textView_des;
    TextView textView_UID, textView_pat_name,textView_age, textView_days,textView_diagnosis,
                textView_treatment_advice,textView_findings,textView_history,textView_chief_complaint;
    TextView textView_date,textView_add ,textView_time,textView_contact, textView_email;
    SessionManager manager;
    NestedScrollView scrollview_edit_profile;
    String definer,diagnosis_desc="",end_note="";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    private static final String TAG = "generatePres___";
    private static String FILE = "mnt/sdcard/invoice.pdf"; // add permission in your manifest...
    static Image image;
    FrameLayout fl_progress_bar;
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean count;
     BottomSheetBehavior behavior;
    public  static PrescriptionItem patient_item = new PrescriptionItem();
    private static ArrayList<Uri> filesGlobal;
    TextView textView_end_note;
    int currentPage = 0;
    int DELAY_TIME = 2000;
    int DELAY_TIME_MULTIPLIER=5000;
    FrameLayout fl_medicines_symbol;
    TextView textView_medical_cert_desc,textView_medical_cert,page_no;
    LinearLayout ll_patient_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.generate_prescription, container, false);
        initCalls(v);

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

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private  void popupCreatingPrescription()
    {
       dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(false);
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_generating_prescription);
        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp_number_picker);

        dialog_data.show();
    }



    private void initCalls(View view) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null); //RelativeLayout is root view of my UI(xml) file.
        root.setDrawingCacheEnabled(true);

        lLayout = new LinearLayoutManager(mContext);
        lLayout2 = new LinearLayoutManager(mContext);
        rView =  view.findViewById(R.id.pres_recycler);
        rView.setLayoutManager(lLayout);
        ll_patient_name = view.findViewById(R.id.ll_patient_name);
        textView_medical_cert  = view.findViewById(R.id.textView_medical_certificate);
        textView_medical_cert_desc  = view.findViewById(R.id.textView_medical_cert_desc);
        textView_medical_cert.setPaintFlags(textView_medical_cert.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fl_medicines_symbol  = view.findViewById(R.id.fl_medicines_symbol);
        page_no  = view.findViewById(R.id.page_no);
        textView_end_note = view.findViewById(R.id.textView_end_note);
        rViewlabtest = view.findViewById(R.id.pres_recycler_labtest);
        textView_advice = view.findViewById(R.id.textView_advice);
        rViewlabtest.setLayoutManager(lLayout2);

        back = view.findViewById(R.id.btn_back_edit_profile);
        btn_backbtn_edit_profile = view.findViewById(R.id.btn_backbtn_edit_profile);
        image_layout =  view.findViewById(R.id.image_layout);
        signature =  view.findViewById(R.id.iv_signature);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        textView_Clinic_name=  view.findViewById(R.id.textView_Clinic_name);
        textView_DocName=  view.findViewById(R.id.textView_DocName);
        textView_degree=  view.findViewById(R.id.textView_degree);
        textView_res_num=  view.findViewById(R.id.textView_res_num);
        textView_des=  view.findViewById(R.id.textView_des);
        textView_UID=  view.findViewById(R.id.textView_UID);
        textView_pat_name=  view.findViewById(R.id.textView_pat_name);

        textView_age=  view.findViewById(R.id.textView_age);
        textView_diagnosis=  view.findViewById(R.id.textView_diag);
        textView_chief_complaint=  view.findViewById(R.id.textView_chief_complaint);
        textView_history=  view.findViewById(R.id.textView_history);
        textView_findings=  view.findViewById(R.id.textView_findings);
        textView_treatment_advice=  view.findViewById(R.id.textView_treatment_advice);


        textView_days=  view.findViewById(R.id.textView_days);
        textView_date=  view.findViewById(R.id.textView_date);
        textView_add =  view.findViewById(R.id.textView_add);
        textView_time=  view.findViewById(R.id.textView_time);
        textView_contact=  view.findViewById(R.id.textView_contact);
        scrollview_edit_profile = view.findViewById(R.id.scrollview_edit_profile);
      //  textView_closed_day=  view.findViewById(R.id.textView_closed_day);
        textView_email=  view.findViewById(R.id.textView_email);
        title  =  view.findViewById(R.id.title);
        generatePDF  =  view.findViewById(R.id.generatePDF);
        sig_text =  view.findViewById(R.id.sig_text);
        initViews(view);
        initViewHolder();
        onclick();
        BottomSheet(view);

    }



    private  void BottomSheet(View view)
    {
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        RecyclerView recyclerView =  view.findViewById(R.id.recyclerView_bottom_sheet);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.HORIZONTAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView.setLayoutManager(gridLayoutManagerr);
        ArrayList<BottomSheetItem> bottomSheetItemArrayList = new ArrayList<>();
        BottomSheetItem bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("1");
        bottomSheetItem.setMenuName("Email");
        bottomSheetItem.setMenuImage("mail");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("2");
        bottomSheetItem.setMenuName("WhatsApp");
        bottomSheetItem.setMenuImage("whatsapp");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("3");
        bottomSheetItem.setMenuName("Other");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

         /* bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("4");
        bottomSheetItem.setMenuName("Share");
        bottomSheetItem.setMenuImage("ic_share_");
        bottomSheetItemArrayList.add(bottomSheetItem);*/

        BottomSheetAdapter mAdapter = new BottomSheetAdapter(mContext,bottomSheetItemArrayList,fl_progress_bar);
        recyclerView.setAdapter(mAdapter);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN );

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
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

                        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, pName+"IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return  true;
        else
            return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(mContext, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GenratePres();
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
                CreatePrescription.backCheckerFlag = true;

                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        btn_backbtn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePrescription.backCheckerFlag = true;
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
        generatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            GenratePres();
                        } else {
                            requestPermission(); // Code for permission
                        }
                    }else {
                        GenratePres();
                    }
                }
            }
        });
        }

        public void GenratePres(){
            if (("history").equalsIgnoreCase(definer)) {
                genInv();
            } else {
                if(!count) {
                    PrescriptionJSON prescriptionJSON = new PrescriptionJSON();

                    prescriptionJSON.setPatientId(prescriptionItem.getPatientId());
                    prescriptionJSON.setDoctorId(manager.getPreferences(mContext, "doctor_id"));
                    if(prescriptionItem.getMedicines()!=null) {
                        if (prescriptionItem.getMedicines().size() != 0) {
                            medicinesItemArrayListO = new ArrayList<>();
                            for (com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mi : prescriptionItem.getMedicines()) {
                                MedicinesItem mii = new MedicinesItem();
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

                    prescriptionJSON.setMedicines(medicinesItemArrayListO);
                    prescriptionJSON.setDiagnosis(prescriptionItem.getDiagnosis());
                    prescriptionJSON.setEndNote(end_note);
                    if(labTestItem!=null) {
                        if (labTestItem.size() != 0) {
                            prescriptionJSON.setLabTest(labTestItem);
                        }
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(prescriptionJSON);
                    fl_progress_bar.setVisibility(View.VISIBLE);
                    if(prescriptionItem.getMedicines()!=null)
                    if(prescriptionItem.getMedicines().size()>4)
                    {
                        popupCreatingPrescription();
                    }
                    else  if(prescriptionItem.getLabTest()!=null)
                        if(prescriptionItem.getLabTest().size()>4)
                        {
                            popupCreatingPrescription();
                        }

                    apiViewHolder.Prescription(json)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responsePrescription);
                }else {
                    genInv();
                }
            }
        }

        public static ArrayList<Uri>  getFiles()
        {
            return filesGlobal;
        }


        public void genInv(){
            popupCreatingPrescription();
            rViewlabtest.setVisibility(View.GONE);
            textView_advice.setVisibility(View.GONE);
            try {
                final ArrayList<Uri> files = new ArrayList<Uri>();

                final int[] p = {0};
                if (prescriptionItem != null) {
                    fl_progress_bar.setVisibility(View.VISIBLE);
                    if (prescriptionItem.getMedicines().size() > 4) {
                       // Toast.makeText(mContext, ""+DELAY_TIME_MULTIPLIER, Toast.LENGTH_SHORT).show();

                        ArrayList<MedicinesItem> medicineList = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            if (prescriptionItem.getMedicines().size() > p[0]) {
                                medicineList.add(medicinesItemArrayListO.get(p[0]));
                                p[0] = p[0] + 1;
                            }
                        }

                        Pres_adapter templateAdapter = new Pres_adapter(medicineList, mContext);
                       rView.setAdapter(templateAdapter);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 4s = 4000ms
                                Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                                patient_item = prescriptionItem;
                                textView_chief_complaint.setVisibility(View.GONE);
                                textView_history.setVisibility(View.GONE);
                                textView_findings.setVisibility(View.GONE);
                                textView_treatment_advice.setVisibility(View.GONE);
                                textView_diagnosis.setVisibility(View.GONE);
                                temp(p[0], files);


                            }
                        }, DELAY_TIME);

                    } else {
                        Bitmap screen = getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                        files.add(getImageUri(mContext, screen, prescriptionItem.getPName()));
                        patient_item = prescriptionItem;
                        filesGlobal = files;
                        rViewlabtest.setVisibility(View.VISIBLE);
                        textView_advice.setVisibility(View.VISIBLE);
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        fl_progress_bar.setVisibility(View.GONE);
                       if(dialog_data!=null)
                       {
                           dialog_data.dismiss();

                       }

                    }
                }

            } catch (Exception e) {
                fl_progress_bar.setVisibility(View.GONE);
                if(dialog_data!=null)
                {
                    dialog_data.dismiss();

                }
                e.printStackTrace();
            }
        }

        public void temp(int p, final ArrayList<Uri> files){
            if(prescriptionItem.getMedicines().size()> p) {
                if( rViewlabtest.getVisibility()==View.VISIBLE)
                    rViewlabtest.setVisibility(View.GONE);

                if( textView_advice.getVisibility()==View.VISIBLE)
                    textView_advice.setVisibility(View.GONE);

                ArrayList<MedicinesItem> medicineList1 = new ArrayList<>();
                for (int i = 0; i <4; i++) {
                    if (prescriptionItem.getMedicines().size() > p) {
                        medicineList1.add(medicinesItemArrayListO.get(p));
                        p = p + 1;
                    }
                }

                Pres_adapter templateAdapter1 = new Pres_adapter(medicineList1, mContext);
                rView.setAdapter(templateAdapter1);


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
                        temp1(finalP, files);

                        filesGlobal = files;
                    }
                }, DELAY_TIME);
                rViewlabtest.setVisibility(View.VISIBLE);
                textView_advice.setVisibility(View.VISIBLE);

            }
            else if(prescriptionItem.getLabTest().size()>0) {
                rViewlabtest.setVisibility(View.VISIBLE);
                textView_advice.setVisibility(View.VISIBLE);

                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);*/
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                fl_progress_bar.setVisibility(View.GONE);
                if(dialog_data!=null)
                {
                    dialog_data.dismiss();
                    PopupGeneratedImages();
                }
            }
            else {
                rViewlabtest.setVisibility(View.GONE);
                textView_advice.setVisibility(View.GONE);

                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);*/
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                fl_progress_bar.setVisibility(View.GONE);
                if(dialog_data!=null)
                {
                    dialog_data.dismiss();
                    PopupGeneratedImages();
                }
            }
        }

    public void temp1(int p, final ArrayList<Uri> files){
        if(prescriptionItem.getMedicines().size()> p) {
            if( rViewlabtest.getVisibility()==View.VISIBLE)
                rViewlabtest.setVisibility(View.GONE);

            if( textView_advice.getVisibility()==View.VISIBLE)
                textView_advice.setVisibility(View.GONE);
            ArrayList<MedicinesItem> medicineList1 = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (prescriptionItem.getMedicines().size() > p) {
                    medicineList1.add(medicinesItemArrayListO.get(p));
                    p = p + 1;
                }
            }

            Pres_adapter templateAdapter1 = new Pres_adapter(medicineList1, mContext);
            rView.setAdapter(templateAdapter1);

            if(labTestItem!=null)
            {
                if(labTestItem.size()>0)
                {
                    Pres_LabTest_adapter pres_labTest_adapter = new Pres_LabTest_adapter(labTestItem,mContext);
                    rViewlabtest.setAdapter(pres_labTest_adapter);
                    rViewlabtest.setVisibility(View.VISIBLE);
                    textView_advice.setVisibility(View.VISIBLE);
                }
                else {
                    textView_advice.setVisibility(View.GONE);
                }
            }


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
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fl_progress_bar.setVisibility(View.GONE);
                    if(dialog_data!=null)
                    {
                        dialog_data.dismiss();
                        PopupGeneratedImages();
                    }
                }
            }, DELAY_TIME);

        }else {

            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
            intent.putExtra(Intent.EXTRA_TEXT, "Dear "+prescriptionItem.getPName()+ ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
            intent.setType("image/jpeg"); *//* This example is sharing jpeg images. *//*
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);*/

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            fl_progress_bar.setVisibility(View.GONE);
            if(dialog_data!=null)
            {
                dialog_data.dismiss();
                PopupGeneratedImages();
            }
        }
    }

    private static void addImage(Document document,byte[] byteArray)
    {
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(140f, 140f);
        try
        {
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
                   if(dialog_data!=null)
                     dialog_data.dismiss();

                } else if (response.getMessage().equals("Prescription Added")) {
                   // Toast.makeText(mContext, "Prescription Added", Toast.LENGTH_SHORT).show();
                    count = true;
                    generatePDF.setText("Share");
                    back.setVisibility(View.GONE);
                    btn_backbtn_edit_profile.setVisibility(View.VISIBLE);
                    try
                    {
                        final ArrayList<Uri> files = new ArrayList<Uri>();
                        final int[] p = {0};
                        if(prescriptionItem!=null)
                        {
                            generatePDF.setVisibility(View.GONE);
                            if(prescriptionItem.getMedicines()!=null) {
                                if (prescriptionItem.getMedicines().size() != 0) {
                                    if (prescriptionItem.getMedicines().size() > 4) {
                                        rViewlabtest.setVisibility(View.GONE);
                                        textView_advice.setVisibility(View.GONE);

                                        ArrayList<MedicinesItem> medicineList = new ArrayList<>();
                                        for (int i = 0; i < 4; i++) {
                                            if (prescriptionItem.getMedicines().size() > p[0]) {
                                                medicineList.add(medicinesItemArrayListO.get(p[0]));
                                                p[0] = p[0] + 1;
                                            }
                                        }

                                        Pres_adapter templateAdapter = new Pres_adapter(medicineList, mContext);
                                        rView.setAdapter(templateAdapter);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                               /* if(prescriptionItem.getMedicines()!=null)
                                                if (prescriptionItem.getMedicines().size() > 4)
                                                page_no.setText("1/"+(prescriptionItem.getMedicines().size()/4)+1);*/
                                                // Do something after 4s = 4000ms
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
                                                temp(p[0], files);
                                                filesGlobal = files;




                                            }
                                        }, DELAY_TIME);



                                    }
                                    else {
                                        Bitmap screen= getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)

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
                                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                        fl_progress_bar.setVisibility(View.GONE);
                                       if(dialog_data!=null)
                                        dialog_data.dismiss();
                                    }
                                }
                            }
                        else if(prescriptionItem.getLabTest()!=null) {
                                Bitmap screen= getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
                                rViewlabtest.setVisibility(View.VISIBLE);
                                textView_advice.setVisibility(View.VISIBLE);

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
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                fl_progress_bar.setVisibility(View.GONE);
                               if(dialog_data!=null)
                                dialog_data.dismiss();
                            }
                            else {
                                Bitmap screen= getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
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
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                fl_progress_bar.setVisibility(View.GONE);
                               if(dialog_data!=null)
                                dialog_data.dismiss();
                            }


                            }else {
                                Bitmap screen= getBitmapFromView(scrollview_edit_profile); // here give id of our root layout (here its my RelativeLayout's id)
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
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                fl_progress_bar.setVisibility(View.GONE);
                           if(dialog_data!=null)
                            dialog_data.dismiss();
                            }
                       /* else {
                        fl_progress_bar.setVisibility(View.GONE);
                    }*/
                    }
                    catch (Exception e)
                    {
                        fl_progress_bar.setVisibility(View.GONE);
                        if(dialog_data!=null)
                       if(dialog_data!=null)
                        e.printStackTrace();
                    }
                }else {
                    fl_progress_bar.setVisibility(View.GONE);
                   if(dialog_data!=null)
                    dialog_data.dismiss();
                }

            }
            else
            {
                fl_progress_bar.setVisibility(View.GONE);
               if(dialog_data!=null)
                dialog_data.dismiss();
                Toast.makeText(mContext, "Unable to process your request", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_bar.setVisibility(View.GONE);
           if(dialog_data!=null)
            dialog_data.dismiss();
            Log.e(TAG, "onError: responsePrescription >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    private void PopupGeneratedImages()
    {
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
               if(dialog_data!=null)
                dialog_data.dismiss();
            }
        });


        Button btn_proceed = dialog_data.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(dialog_data!=null)
                   dialog_data.dismiss();

            }
        });
       /* for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);*/

       final ViewPager mPager =  dialog_data.findViewById(R.id.pager_images);

        mPager.setAdapter(new SlidingImage_Adapter_GeneratedPrescription(getApplicationContext(),filesGlobal));

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

    private void initViews(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.e("prescriptionItem","________________"+prescriptionItem.toString());
            prescriptionItem = bundle.getParcelable("defaultAttributeId");
            labTestItem = bundle.getParcelableArrayList("defaultAttributeIdLabTest");
            definer = bundle.getString("definer");
            end_note = bundle.getString("end_note");

                if(certificate_selection)
                {
                    fl_medicines_symbol.setVisibility(View.GONE);
                    textView_diagnosis.setVisibility(View.GONE);
                    textView_advice.setVisibility(View.GONE);
                    textView_medical_cert.setVisibility(View.VISIBLE);
                    textView_medical_cert_desc.setVisibility(View.VISIBLE);
                    textView_medical_cert.setText(bundle.getString("certificate_title"));
                    textView_medical_cert_desc.setText(""+bundle.getString("certificate_desc"));
                    textView_medical_cert_desc.setTextColor(getResources().getColor(R.color.colorBlack));
                    textView_pat_name.setTextColor(getResources().getColor(R.color.colorBlack));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    ll_patient_name.setLayoutParams(params);
                }
                else {
                    if(prescriptionItem.getMedicines()!=null){
                        if(prescriptionItem.getMedicines().size()!=0)
                            fl_medicines_symbol.setVisibility(View.VISIBLE);
                    }
                    else {
                        if(prescriptionItem.getLabTest()!=null)
                        {
                            if(prescriptionItem.getLabTest().size()!=0)
                                fl_medicines_symbol.setVisibility(View.VISIBLE);
                        }
                        else
                            fl_medicines_symbol.setVisibility(View.GONE);
                    }

                    textView_medical_cert.setVisibility(View.GONE);
                    textView_medical_cert_desc.setVisibility(View.GONE);
                }

            CreatePrescription.labtestAddFLAG = true;
            CreatePrescription.medicineAddFLAG = true;
            if(end_note==null)
                textView_end_note.setVisibility(View.GONE);
            else
            {
                if(end_note.equalsIgnoreCase(""))
                    textView_end_note.setVisibility(View.GONE);
                else
                 textView_end_note.setText("Note : "+end_note);
            }

            diagnosis_desc  = prescriptionItem.getDiagnosis();
            if(prescriptionItem.getMedicines()!=null)
            {
                if(prescriptionItem.getMedicines().size()!=0)
                {
            medicinesItemArrayListO = new ArrayList<>();
                for (com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem mi: prescriptionItem.getMedicines()   ) {
                    MedicinesItem mii = new MedicinesItem();
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

            if(diagnosis_desc==null)
            {
                textView_diagnosis.setVisibility(View.GONE);
                textView_chief_complaint.setVisibility(View.GONE);
                textView_history.setVisibility(View.GONE);
                textView_treatment_advice.setVisibility(View.GONE);
                textView_findings.setVisibility(View.GONE);
                textView_diagnosis.setVisibility(View.GONE);

            }
            else
            {
                String[] details = diagnosis_desc.split(Pattern.quote("|"));
                if(details.length!=0) {
                    if (details[0] != null)
                        if (details[0].equals(""))
                            textView_chief_complaint.setVisibility(View.GONE);
                        else
                            textView_chief_complaint.setText("Chief Complaint - " + details[0]);

                        if(details.length>=2)
                    if (details[1] != null)
                        if (details[1].equals(""))
                            textView_history.setVisibility(View.GONE);
                        else
                            textView_history.setText("History - " + details[1]);

                    if(details.length>=3)
                    if (details[2] != null)
                        if (details[2].equals(""))
                            textView_findings.setVisibility(View.GONE);
                        else
                            textView_findings.setText("Findings - " + details[2]);

                    if(details.length>=4)
                    if (details[3] != null)
                        if (details[3].equals(""))
                            textView_diagnosis.setVisibility(View.GONE);
                        else
                            textView_diagnosis.setText("Diagnosis - " + details[3]);

                    if(details.length>=5)
                    if (details[4] != null)
                        if (details[4].equals(""))
                            textView_treatment_advice.setVisibility(View.GONE);
                        else
                            textView_treatment_advice.setText("Treatment/Advice - " + details[4]);
                }
                else {
                    textView_chief_complaint.setVisibility(View.GONE);
                    textView_history.setVisibility(View.GONE);
                    textView_treatment_advice.setVisibility(View.GONE);
                    textView_findings.setVisibility(View.GONE);
                    textView_diagnosis.setVisibility(View.GONE);
                }

            }



            if(("history").equalsIgnoreCase(definer)){
                title.setText("Prescription Details");
            }else {
                title.setText("Prescription Preview");
            }

            Doctor doctor = prescriptionItem.getDoctor();
            if(!("").equalsIgnoreCase(doctor.getClinicName())) {
                textView_Clinic_name.setText(doctor.getClinicName());
            }else {
                textView_Clinic_name.setVisibility(View.GONE);
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
            if(!("").equalsIgnoreCase(manager.getPreferences(mContext,"name").trim())) {
                textView_DocName.setText(doctor.getName().trim());
            }
            if(!("").equalsIgnoreCase(doctor.getPgName())) {
                textView_degree.setText(doctor.getUgName() + " " + doctor.getPgName() + " " + doctor.getAddtionalQualification());

            }else {
                textView_degree.setVisibility(View.GONE);
            }
            textView_res_num.setText("Reg. No - " + doctor.getRegistrationNo());
            if(!("").equalsIgnoreCase(doctor.getDesignationName())) {
                textView_des.setText(doctor.getDesignationName());
            }else {
                textView_des.setVisibility(View.GONE);
            }
            String emaill = "";
            if(prescriptionItem.getPEmail()==null)
            {
                emaill = "-";
            }
            else  if(prescriptionItem.getPEmail().equalsIgnoreCase("")) {
                emaill = "-";
            }
            else
                emaill = prescriptionItem.getPEmail();

            String mobl = "-";
            if(prescriptionItem.getPMobile()==null)
            {
                mobl = "-";
            }
            else  if(prescriptionItem.getPMobile().equalsIgnoreCase("")) {
                mobl = "-";
            }
            else
                mobl = prescriptionItem.getPMobile();
            String age___ = "";
            if(prescriptionItem.getAge().contains("yr") || prescriptionItem.getAge().contains("month"))
                age___ = prescriptionItem.getAge();
            else
                age___ = prescriptionItem.getAge() + " yr";
            textView_pat_name.setText("Patient Details : " + prescriptionItem.getPName() + ", "+prescriptionItem.getGender() + " / " + age___);
            if(prescriptionItem.getPEmail().equals(""))

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

                textView_date.setText(DateUtils.outFormatsetMMM(prescriptionItem.getDate()));
            } else {
                textView_date.setText("Date : " +prescriptionItem.getDate());
            }
            if(!("").equalsIgnoreCase(doctor.getWorkingDays())) {
                String work_days = doctor.getWorkingDays();
                if(work_days.contains("MONDAY"))
                    work_days = work_days.replace("MONDAY"," MON");

                if(work_days.contains("TUESDAY"))
                    work_days = work_days.replace("TUESDAY"," TUE");

                if(work_days.contains("WEDNESDAY"))
                    work_days = work_days.replace("WEDNESDAY"," WED");

                if(work_days.contains("THURSDAY"))
                    work_days = work_days.replace("THURSDAY"," THUR");

                if(work_days.contains("FRIDAY"))
                    work_days = work_days.replace("FRIDAY"," FRI");

                if(work_days.contains("SATURDAY"))
                    work_days = work_days.replace("SATURDAY"," SAT");

                if(work_days.contains("SUNDAY"))
                    work_days = work_days.replace("SUNDAY"," SUN");

                textView_days.setText("Working :" + work_days);
            }else {
                textView_days.setVisibility(View.GONE);
            }
            if(!("").equalsIgnoreCase(doctor.getVisitingHrFrom())) {
                String[] visiting_hr_from_details = manager.getPreferences(mContext,"visiting_hr_from").split(Pattern.quote("|"));;
                String[] visiting_hr_to_details = manager.getPreferences(mContext,"visiting_hr_to").split(Pattern.quote("|"));;
                if(visiting_hr_from_details.length>1)
                    textView_time.setText("Time : " +visiting_hr_from_details[0].trim()+" - "+visiting_hr_to_details[0].trim() +",   "+visiting_hr_from_details[1].trim()+" - "+visiting_hr_to_details[1].trim() );
                else
                    textView_time.setText("Time : " +visiting_hr_from_details[0].trim()+" - "+visiting_hr_to_details[0].trim());
              //  textView_time.setText("Time : " + doctor.getVisitingHrFrom() + " to " + doctor.getVisitingHrTo());
            }else {
                textView_time.setVisibility(View.GONE);
            }
            if(!("").equalsIgnoreCase(doctor.getMobileLetterHead())) {
                textView_contact.setText("Contact : " + doctor.getMobileLetterHead());
            }else {
                textView_contact.setVisibility(View.GONE);
            }
            if(!("").equalsIgnoreCase(doctor.getEmailLetterHead())) {
                textView_email.setText("Mail Id : " + doctor.getEmailLetterHead());
            }else {
                textView_email.setVisibility(View.GONE);
            }
            if(!("").equalsIgnoreCase(doctor.getClinicAddress())) {
                textView_add.setText("Add : " + doctor.getClinicAddress());
            }else {
                textView_add.setVisibility(View.GONE);
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
            }else{
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
            }else{
                signature.setVisibility(View.GONE);
                sig_text.setVisibility(View.GONE);
            }
        }

        Log.e(TAG,">>11>> "+medicinesItemArrayListO.toString());

        if(prescriptionItem!=null)
        {
            Pres_adapter templateAdapter = new Pres_adapter(medicinesItemArrayListO,mContext);
            rView.setAdapter(templateAdapter);
        }
        else
            rView.setAdapter(null);

        if(labTestItem!=null)
        {
            if(labTestItem.size()>0)
            {
                Pres_LabTest_adapter pres_labTest_adapter = new Pres_LabTest_adapter(labTestItem,mContext);
                rViewlabtest.setAdapter(pres_labTest_adapter);
                textView_advice.setVisibility(View.VISIBLE);
            }
            else{
                textView_advice.setVisibility(View.GONE);
                rViewlabtest.setAdapter(null);
            }


        }


    }

}