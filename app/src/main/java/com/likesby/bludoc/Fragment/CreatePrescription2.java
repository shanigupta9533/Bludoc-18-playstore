//package com.likesby.bludoc.Fragment;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.ContextThemeWrapper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.PagerSnapHelper;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.SnapHelper;
//
//import com.google.gson.Gson;
//import com.likesby.bludoc.Adapter.AddLabTestAdapter;
//import com.likesby.bludoc.Adapter.AddMedicineAdapter;
//import com.likesby.bludoc.Adapter.AddTemplateAdapter;
//import com.likesby.bludoc.Adapter.PatientsAdapter;
//import com.likesby.bludoc.Adapter.SearchAdapter;
//import com.likesby.bludoc.Adapter.SearchLabTestAdapter;
//import com.likesby.bludoc.HomeActivity;
//import com.likesby.bludoc.ModelLayer.Entities.AddTemplateJSON;
//import com.likesby.bludoc.ModelLayer.Entities.DesignationItem;
//import com.likesby.bludoc.ModelLayer.Entities.PGItem;
//import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
//import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
//import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
//import com.likesby.bludoc.ModelLayer.Entities.SpecialitiesItem;
//import com.likesby.bludoc.ModelLayer.Entities.UGItem;
//import com.likesby.bludoc.ModelLayer.NewEntities.AddTemplateLabTestJSON;
//import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
//import com.likesby.bludoc.ModelLayer.NewEntities3.Doctor;
//import com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem;
//import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
//import com.likesby.bludoc.ModelLayer.NewEntities3.ResponseHistory;
//import com.likesby.bludoc.R;
//import com.likesby.bludoc.SessionManager.SessionManager;
//import com.likesby.bludoc.SplashActivity;
//import com.likesby.bludoc.TemplateSelection;
//import com.likesby.bludoc.constants.ApplicationConstant;
//import com.likesby.bludoc.db.MyDB;
//import com.likesby.bludoc.utils.Utils;
//import com.likesby.bludoc.viewModels.ApiViewHolder;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Objects;
//
//import io.reactivex.SingleObserver;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//import static com.likesby.bludoc.HomeActivity.poss__;
//
//public class CreatePrescription2 extends Fragment {
//    boolean definer_type_flag  = false,delete_flag = false;
//    private static Context mContext;
//    private Dialog dialog;
//    private View v;
//    static RecyclerView rView;
//    static LinearLayoutManager lLayout,  lLayout1;
//    private Button btnChooseFromTemplate,btnChooseFromTemplateLabTest;
//    private EditText  et_additional_comments, et_frequency,
//            et_frequency2, et_route, et_no_of_days, et_instructions,et_additional_comments_labtests;
//    boolean logo_flag = false;
//    private SessionManager manager;
//    TextView HeadertextView;
//    private static final String TAG = "CreatePrescri_____";
//    private ApiViewHolder apiViewHolder;
//    private CompositeDisposable mBag = new CompositeDisposable();
//    SplashActivity splashActivity = new SplashActivity();
//    private Spinner frequency_spinner, frequency2_spinner, route_spinner, no_of_days_spinner, instructions_spinner;
//    private ArrayAdapter<String> frequency_adp, frequency2_adp, route_adp, no_of_days_adp, instructions_adp;
//
//    private ArrayList<String> frequency_list, frequency2_list, route_list, no_of_days_list, instructions_list;
//
//    ArrayList<DesignationItem> designationItemArrayList;
//    //    ArrayList<String> working_days_Arraylist,visit_hrs_from_Arraylist,
//    //            visit_hrs_to_Arraylist,closed_day_Arraylist;;
//    ArrayList<PGItem> pgItemArrayList;
//    ArrayList<UGItem> ugItemArrayList;
//    ArrayList<SpecialitiesItem> specialitiesItemArrayList;
//    public static ArrayList<PatientsItem> patientsItemArrayList = new ArrayList<>();
//    private String frequency = "", medicine_name = "", frequency2 = "", no_of_days = "", route = "",
//            add_instructions = "", instructions = "";
//    private androidx.appcompat.widget.SearchView searchViewPatient,searchViewMedicine;
//    private static RecyclerView mRecyclerViewMedicines, mRecyclerViewPatients,mRecyclerViewAddedMedicines,
//            mRecyclerViewLabTest,mRecyclerViewAddedLabTest;
//    private SearchAdapter mAdapterSearchMedicine;
//
//    private SearchLabTestAdapter mAdapterSearchLabTest;
//    private static PatientsAdapter patientsAdapter;
//    private static LinearLayout  ll_patients_view,ll_prescription_view, ll_info_no_patient;
//    private static  RelativeLayout top_view;
//    private EditText searchBarMaterialPatient;
//    EditText searchBarMaterialMedicine,searchBarMaterialTestLab;
//    FrameLayout fl_progress_bar;
//    private static Button btn_create_patient,btn_clear,btn_prescribe,btn_add,btn_add_labtest;
//    ArrayList<MedicinesItem> addMedicinesArrayList = new ArrayList<>();
//    ArrayList<LabTestItem> addLabTestArrayList = new ArrayList<>();
//    public static ArrayList<com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem> NEWaddMedicinesArrayList = new ArrayList<>();
//    public static ArrayList<com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem> NEWaddLabTestArrayList;
//    AddMedicineAdapter addMedicineAdapter;
//    AddLabTestAdapter addLabTestAdapter;
//    private static TextView patientdetails,textView3_5,header;
//    public static String patient_id="",temp_id="",temp_name="",diagnosis_desc="";
//    public static int pos;
//    GridLayoutManager gridLayoutManager2,gridLayoutManager3,gridLayoutManager,gridLayoutManager4,gridLayoutManager5;
//    AlertDialog alertDialog;
//    String define= "";
//    public  static ImageView back;
//    LinearLayout   ll_medicine_product,ll_medicinal_lab,ll_end_note,ll_certificate;
//    LinearLayout  ll_main_medicine_details,ll_main_lab_test_details,ll_main_end_note_details,ll_main_certificate_details;
//    EditText et_end_note,et_chief_complaint,et_history,et_findings,et_diagnosis,et_treatment_advice,et_cert_title,et_cert_desc;
//    public static boolean backCheckerFlag = false,medicineAddFLAG = false,labtestAddFLAG=false,is_on_lab_test = false;
//    LinearLayout ll_btn_templates;
//    MyDB myDB;
//    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem>  MedicAll;
//    public  static boolean certificate_selection= false;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = getActivity();
//        manager = new SessionManager();
//        myDB  = new MyDB(mContext);
//        splashActivity = new SplashActivity();
//        addMedicinesArrayList = new ArrayList<>();
//        patientsItemArrayList = new ArrayList<>();
//        MedicAll= new ArrayList<>();
//    //    NEWaddMedicinesArrayList = new ArrayList<>();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        //inflater.inflate(R.layout.state_frag, container, false);
//        v = inflater.inflate(R.layout.create_prescription, container, false);
//        MedicAll = myDB.getMedicines();
//        initCalls(v);
//        hideKeyboard(mContext);
//        Bundle args = getArguments();
//
//        patientsItemArrayList =  splashActivity.getpatients();
//
//
//        if(args!=null)
//        {
//            btn_create_patient.setVisibility(View.GONE);
//            patient_id = args.getString("patient_id");
//            ll_info_no_patient.setVisibility(View.GONE);
//            if(("temp").equalsIgnoreCase(args.getString("definer"))) {
//                define = "";
//                header.setText("Create Prescription");
//                btn_prescribe.setText("Prescribe");
//                if (args.getParcelableArrayList("new_list") != null) {
//                    if (addMedicinesArrayList.size() == 0) {
//                        NEWaddMedicinesArrayList = args.getParcelableArrayList("new_list");
//                        assert NEWaddMedicinesArrayList != null;
//
//                        for (com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mi : NEWaddMedicinesArrayList) {
//                            MedicinesItem medicinesItem = new MedicinesItem();
//                            medicinesItem.setMedicineName(mi.getMedicineName());
//                            // medicinesItem.setMedicineId(mi.getTempMedicineId());
//                            medicinesItem.setFrequency(mi.getFrequency());
//                            medicinesItem.setNoOfDays(mi.getNoOfDays());
//                            medicinesItem.setRoute(mi.getRoute());
//                            medicinesItem.setInstruction(mi.getInstruction());
//                            medicinesItem.setAdditionaComment(mi.getAdditionaComment());
//                            addMedicinesArrayList.add(medicinesItem);
//                        }
//
//                    }
//                    fl_progress_bar.setVisibility(View.GONE);
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    String age___ = "";
//                    if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                        age___ = patientsItemArrayList.get(pos).getAge();
//                    else if(patientsItemArrayList.get(pos).getAge().contains("Yr") || patientsItemArrayList.get(pos).getAge().contains("Month"))
//                        age___ = patientsItemArrayList.get(pos).getAge();
//                    else
//                        age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//
//                    patientdetails.setText(patientsItemArrayList.get(pos).getPName() + " - " + patientsItemArrayList.get(pos).getGender() + " / " + age___);
//
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                    frequency_list, frequency2_list, et_no_of_days,
//                                    route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                    route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe, "pres", fl_progress_bar);
//                            mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                        }
//                    });
//
//
//                    if (addMedicinesArrayList.size() > 0) {
//                        textView3_5.setVisibility(View.VISIBLE);
//                        mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size() - 1);
//                        textView3_5.setText("1/" + addMedicinesArrayList.size());
//                    } else {
//
//                    }
//                    mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                                Log.e("position", String.valueOf(position));
//                                textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//                        }
//                    });
//                    if(!backCheckerFlag) {
//                        if (NEWaddLabTestArrayList != null) {
//                            if (NEWaddLabTestArrayList.size() != 0) {
//                            }
//                            assert NEWaddLabTestArrayList != null;
//
//                            for (com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem mi : NEWaddLabTestArrayList) {
//                                LabTestItem labTestItem = new LabTestItem();
//                                labTestItem.setLabTestName(mi.getLabTestName());
//                                labTestItem.setLabTestComment(mi.getLabTestComment());
//                                addLabTestArrayList.add(labTestItem);
//                            }
//                        }
//
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                                        et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab, mRecyclerViewAddedLabTest, btnChooseFromTemplateLabTest, btn_prescribe, "pres");
//                                mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                            }
//                        });
//                    }
//
//                }
//                else {
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                }
//
//                if (args.getParcelableArrayList("new_list_template") != null) {
//
//                    if(addLabTestArrayList.size() == 0) {
//                        NEWaddLabTestArrayList = args.getParcelableArrayList("new_list_template");
//                        assert NEWaddLabTestArrayList != null;
//
//                        for (com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem mi : NEWaddLabTestArrayList) {
//                            LabTestItem labTestItem = new LabTestItem();
//                            labTestItem.setLabTestName(mi.getLabTestName());
//                            labTestItem.setLabTestComment(mi.getLabTestComment());
//
//                            addLabTestArrayList.add(labTestItem);
//                        }
//                    }
//                    fl_progress_bar.setVisibility(View.GONE);
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    String age___ = "";
//                    if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                        age___ = patientsItemArrayList.get(pos).getAge();
//                    else
//                        age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//
//                    patientdetails.setText(patientsItemArrayList.get(pos).getPName() +" - "+patientsItemArrayList.get(pos).getGender()+" / "+age___);
//                    setActiveMedicine_LAB();
//
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                                    et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab,mRecyclerViewAddedLabTest, btnChooseFromTemplateLabTest, btn_prescribe,"pres");
//                            mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                        }
//                    });
//
//
//                    if (addLabTestArrayList.size() > 0) {
//                        textView3_5.setVisibility(View.VISIBLE);
//                        mRecyclerViewAddedLabTest.smoothScrollToPosition(addLabTestArrayList.size()-1);
//                        textView3_5.setText("1/" + addLabTestArrayList.size());
//                    }
//                    else
//                    {
//
//                    }
//                    mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                                Log.e("position", String.valueOf(position));
//                                textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//                        }
//                    });
//
//
//                    if(!backCheckerFlag)
//                    {
//                    if(NEWaddMedicinesArrayList!=null) {
//                        if (NEWaddMedicinesArrayList.size() != 0) {
//                            assert NEWaddMedicinesArrayList != null;
//                            for (com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mi : NEWaddMedicinesArrayList) {
//                                MedicinesItem medicinesItem = new MedicinesItem();
//                                medicinesItem.setMedicineName(mi.getMedicineName());
//                                //medicinesItem.setMedicineId(mi.getTempMedicineId());
//                                medicinesItem.setFrequency(mi.getFrequency());
//                                medicinesItem.setNoOfDays(mi.getNoOfDays());
//                                medicinesItem.setRoute(mi.getRoute());
//                                medicinesItem.setInstruction(mi.getInstruction());
//                                medicinesItem.setAdditionaComment(mi.getAdditionaComment());
//                                addMedicinesArrayList.add(medicinesItem);
//                            }
//
//
//                            //patientdetails.setText(args.getString("temp_name"));
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                            frequency_list, frequency2_list, et_no_of_days,
//                                            route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                            route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe, "temp", fl_progress_bar);
//                                    mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//
//                                }
//                            });
//
//                        }}
//                    }
//                    }
//            }else if(("edit").equalsIgnoreCase(args.getString("definer"))){
//                define = "edit";
//                searchBarMaterialMedicine.clearFocus();
//                hideKeyboard(mContext);
//                btnChooseFromTemplate.setVisibility(View.GONE);
//                btnChooseFromTemplateLabTest.setVisibility(View.GONE);
//                header.setText("View/Edit Template");
//                btn_prescribe.setText("Save Template");
//                if(Objects.requireNonNull(args.getString("definer_type")).equalsIgnoreCase("medicines"))
//                {
//                    definer_type_flag = false;
//                    ll_medicine_product.setVisibility(View.VISIBLE);
//                    ll_medicinal_lab.setVisibility(View.GONE);
//                    ll_end_note.setVisibility(View.GONE);
//                }
//                else if(Objects.requireNonNull(args.getString("definer_type")).equalsIgnoreCase("labtest"))
//                {
//                    definer_type_flag = true;
//
//                    ll_medicine_product.setVisibility(View.GONE);
//                    ll_medicinal_lab.setVisibility(View.VISIBLE);
//                    ll_end_note.setVisibility(View.GONE);
//
//                }
//                temp_id = args.getString("temp_id");
//                temp_name = args.getString("temp_name");
//                if (args.getParcelableArrayList("new_list") != null) {
//                    NEWaddMedicinesArrayList = args.getParcelableArrayList("new_list");
//                    assert NEWaddMedicinesArrayList != null;
//                    for (com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mi : NEWaddMedicinesArrayList) {
//                        MedicinesItem medicinesItem = new MedicinesItem();
//                        medicinesItem.setMedicineName(mi.getMedicineName());
//                        //medicinesItem.setMedicineId(mi.getTempMedicineId());
//                        medicinesItem.setFrequency(mi.getFrequency());
//                        medicinesItem.setNoOfDays(mi.getNoOfDays());
//                        medicinesItem.setRoute(mi.getRoute());
//                        medicinesItem.setInstruction(mi.getInstruction());
//                        medicinesItem.setAdditionaComment(mi.getAdditionaComment());
//                        addMedicinesArrayList.add(medicinesItem);
//                    }
//
//                    fl_progress_bar.setVisibility(View.GONE);
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    patientdetails.setText(args.getString("temp_name"));
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                    frequency_list, frequency2_list, et_no_of_days,
//                                    route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                    route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines,btnChooseFromTemplate,btn_prescribe,"temp", fl_progress_bar);
//                            mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//
//                        }
//                    });
//
//                    if (addMedicinesArrayList.size() > 0) {
//                        textView3_5.setVisibility(View.VISIBLE);
//                        mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size()-1);
//                        textView3_5.setText("1/" + addMedicinesArrayList.size());
//                    }
//
//                    mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                                Log.e("position", String.valueOf(position));
//                                textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//                        }
//                    });
//                } else {
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                }
//
//                if (args.getParcelableArrayList("new_list_template") != null) {
//                    NEWaddLabTestArrayList = args.getParcelableArrayList("new_list_template");
//                    assert NEWaddLabTestArrayList != null;
//                    for (com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem mi : NEWaddLabTestArrayList) {
//                        LabTestItem labTestItem = new LabTestItem();
//                        labTestItem.setLabTestName(mi.getLabTestName());
//                        labTestItem.setLabTestComment(mi.getLabTestComment());
//                        addLabTestArrayList.add(labTestItem);
//                    }
//
//                    fl_progress_bar.setVisibility(View.GONE);
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    patientdetails.setText(args.getString("temp_name"));
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                                   et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab, mRecyclerViewAddedLabTest,btnChooseFromTemplateLabTest,btn_prescribe,"temp");
//                            mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//
//                        }
//                    });
//
//                    if (addLabTestArrayList.size() > 0) {
//                        textView3_5.setVisibility(View.VISIBLE);
//                        mRecyclerViewAddedLabTest.smoothScrollToPosition(addLabTestArrayList.size()-1);
//                        textView3_5.setText("1/" + addLabTestArrayList.size());
//                    }
//
//                    mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                                Log.e("position", String.valueOf(position));
//                                textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//                        }
//                    });
//                    setActiveMedicine_LAB();
//                }
//            } else {
//                String age___ = "";
//                if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                    age___ = patientsItemArrayList.get(pos).getAge();
//                else
//                    age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//
//                patientdetails.setText(patientsItemArrayList.get(pos).getPName() +" - "+patientsItemArrayList.get(pos).getGender()+" / "+age___);
//                if(addMedicinesArrayList.size() == 0){
//                    btn_prescribe.setText("Prescribe");
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    if(patientsItemArrayList != null){
//                        if(patientsItemArrayList.size() > 0 && patient_id != null){
//
//                            for(int  k = 0 ; patientsItemArrayList.size() > k ; k++){
//                                if(patientsItemArrayList.get(k).getPatientId().equalsIgnoreCase(patient_id)){
//                                    pos = k;
//                                }
//                            }
//                        }
//                    }
//                    String age___2 = "";
//                    if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                        age___2 = patientsItemArrayList.get(pos).getAge();
//                    else
//                        age___2 = patientsItemArrayList.get(pos).getAge() + " yr";
//                    patientdetails.setText(patientsItemArrayList.get(pos).getPName() +" - "+ patientsItemArrayList.get(pos).getGender()+" / "+age___2);
//                }else {
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    ll_patients_view.setVisibility(View.GONE);
//                    ll_prescription_view.setVisibility(View.VISIBLE);
//                    top_view.setVisibility(View.VISIBLE);
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                    frequency_list, frequency2_list, et_no_of_days,
//                                    route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                    route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe,"pres", fl_progress_bar);
//                            mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                            mAdapterSearchMedicine.notifyDataSetChanged();
//                        }
//                    });
//
//
//
//                    if (addMedicinesArrayList.size() > 0) {
//                        textView3_5.setVisibility(View.VISIBLE);
//                        mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size()-1);
//                        textView3_5.setText("1/" + addMedicinesArrayList.size());
//                    }
//
//
//
//                    mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                            super.onScrollStateChanged(recyclerView, newState);
//
//                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                               int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                                Log.e("position", String.valueOf(position));
//                                textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//                        }
//                    });
//                }
//            }
//        }
//        else
//        {
//            btn_create_patient.setVisibility(View.VISIBLE);
//            header.setText("Create Prescription");
//            ll_prescription_view.setVisibility(View.GONE);
//            top_view.setVisibility(View.GONE);
//            if(addMedicinesArrayList.size() == 0){
//                fl_progress_bar.setVisibility(View.GONE);
//
//                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        patientsAdapter = new PatientsAdapter(patientsItemArrayList,ll_patients_view,ll_prescription_view,top_view,btn_create_patient,patientdetails,apiViewHolder,mBag,fl_progress_bar,ll_medicinal_lab,ll_medicine_product,ll_end_note,ll_certificate,
//                                ll_main_medicine_details,ll_main_lab_test_details,ll_main_end_note_details,ll_main_certificate_details);
//                        mRecyclerViewPatients.setAdapter(patientsAdapter);
//                    }
//                });
//
//                ll_patients_view.setVisibility(View.VISIBLE);
//                if(patientsItemArrayList.size()>0) {
//                    ll_info_no_patient.setVisibility(View.GONE);
//
//                }else {
//                    ll_info_no_patient.setVisibility(View.VISIBLE);
//                    ll_patients_view.setVisibility(View.GONE);
//                }
//                hideKeyboard(mContext);
//            }else {
//                ll_info_no_patient.setVisibility(View.GONE);
//                btn_create_patient.setVisibility(View.GONE);
//                fl_progress_bar.setVisibility(View.GONE);
//                ll_patients_view.setVisibility(View.GONE);
//                ll_prescription_view.setVisibility(View.VISIBLE);
//                top_view.setVisibility(View.VISIBLE);
//                String age___ = "";
//                if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                    age___ = patientsItemArrayList.get(pos).getAge();
//                else
//                    age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//                patientdetails.setText(patientsItemArrayList.get(pos).getPName() +" - "+patientsItemArrayList.get(pos).getGender()+" / "+age___);
//
//                /*Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        addMedicineAdapter = new AddMedicineAdapter(addMedicinesArrayList,
//                                frequency_list, frequency2_list, et_no_of_days,
//                                route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe,"pres", fl_progress_bar);
//                        mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                        mAdapterSearchMedicine.notifyDataSetChanged();
//                    }
//                });*/
//
//
//               /* if (addMedicinesArrayList.size() > 0) {
//                    textView3_5.setVisibility(View.VISIBLE);
//                    mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size()-1);
//                    textView3_5.setText("1/" + addMedicinesArrayList.size());
//                }
//
//
//
//                mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                            int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                            Log.e("position", String.valueOf(position));
//                            textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//                        }
//                    }
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                    }
//                });*/
//            }
//        }
//        return v;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        hideKeyboard(mContext);
//        //Toast.makeText(mContext, "onResume", Toast.LENGTH_SHORT).show();
//
//        if(backCheckerFlag)
//        {
//            fl_progress_bar.setVisibility(View.GONE);
//            ll_patients_view.setVisibility(View.GONE);
//            ll_prescription_view.setVisibility(View.VISIBLE);
//            top_view.setVisibility(View.VISIBLE);
//            ll_info_no_patient.setVisibility(View.GONE);
//            btn_create_patient.setVisibility(View.GONE);
//        }
//
//        if(labtestAddFLAG)
//        {
//            if(NEWaddLabTestArrayList!=null) {
//                if (NEWaddLabTestArrayList.size() != 0) {
//
//                    assert NEWaddLabTestArrayList != null;
//                    addLabTestArrayList = new ArrayList<>();
//                    for (com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem mi : NEWaddLabTestArrayList) {
//                        LabTestItem labTestItem = new LabTestItem();
//                        labTestItem.setLabTestName(mi.getLabTestName());
//                        labTestItem.setLabTestComment(mi.getLabTestComment());
//
//                        addLabTestArrayList.add(labTestItem);
//                    }
//                }
//            }
//                String age___ = "";
//                if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                    age___ = patientsItemArrayList.get(pos).getAge();
//                else
//                    age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//            patientdetails.setText(patientsItemArrayList.get(pos).getPName() +" - "+patientsItemArrayList.get(pos).getGender()+" / "+age___);
//
//
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//        /*    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                            et_additional_comments_labtests, btn_add_labtest, textView3_5, searchBarMaterialTestLab,mRecyclerViewAddedLabTest, btnChooseFromTemplateLabTest, btn_prescribe,"pres");
//                    mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                    ll_medicinal_lab.performClick();
//                }
//            });*/
//
//
//                addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                        et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab,mRecyclerViewAddedLabTest, btnChooseFromTemplateLabTest, btn_prescribe,"pres");
//                mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                ll_medicinal_lab.performClick();
//
//
//
//        if (addLabTestArrayList.size() > 0) {
//            textView3_5.setVisibility(View.VISIBLE);
//            mRecyclerViewAddedLabTest.smoothScrollToPosition(addLabTestArrayList.size()-1);
//            textView3_5.setText("1/" + addLabTestArrayList.size());
//        }
//        mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                    int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                    Log.e("position", String.valueOf(position));
//                    textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//        }
//        else {
//            if (addLabTestArrayList.size() > 0) {
//                textView3_5.setVisibility(View.VISIBLE);
//                mRecyclerViewAddedLabTest.smoothScrollToPosition(addLabTestArrayList.size() - 1);
//                textView3_5.setText("1/" + addLabTestArrayList.size());
//
//                mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                            int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                            Log.e("position", String.valueOf(position));
//                            textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//                        }
//                    }
//
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                    }
//                });
//            }
//        }
//       // setActiveMedicine_LAB();
//        if(medicineAddFLAG)
//        {
//        if (NEWaddMedicinesArrayList.size() != 0) {
//            addMedicinesArrayList = new ArrayList<>();
//            for (com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mi : NEWaddMedicinesArrayList) {
//                MedicinesItem medicinesItem = new MedicinesItem();
//                medicinesItem.setMedicineName(mi.getMedicineName());
//                // medicinesItem.setMedicineId(mi.getTempMedicineId());
//                medicinesItem.setFrequency(mi.getFrequency());
//                medicinesItem.setNoOfDays(mi.getNoOfDays());
//                medicinesItem.setRoute(mi.getRoute());
//                medicinesItem.setInstruction(mi.getInstruction());
//                medicinesItem.setAdditionaComment(mi.getAdditionaComment());
//                addMedicinesArrayList.add(medicinesItem);
//            }
//
//        }
//            String age___ = "";
//            if(patientsItemArrayList.get(pos).getAge().contains("yr") || patientsItemArrayList.get(pos).getAge().contains("month"))
//                age___ = patientsItemArrayList.get(pos).getAge();
//            else
//                age___ = patientsItemArrayList.get(pos).getAge() + " yr";
//                patientdetails.setText(patientsItemArrayList.get(pos).getPName() + " - " + patientsItemArrayList.get(pos).getGender() + " / " + age___);
//
////                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                        @Override
////                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                            return lhs.getMedicineName().compareTo(rhs.getMedicineName());
////                        }
////                    });
//
//         /*   Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    addMedicineAdapter = new AddMedicineAdapter(addMedicinesArrayList,
//                            frequency_list, frequency2_list, et_no_of_days,
//                            route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                            route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe, "pres", fl_progress_bar);
//                    mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                    ll_medicine_product.performClick();
//                }
//            });*/
//
//
//                addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                        frequency_list, frequency2_list, et_no_of_days,
//                        route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                        route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe, "pres", fl_progress_bar);
//                mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                ll_medicine_product.performClick();
//
//
//        if (addMedicinesArrayList.size() > 0) {
//            textView3_5.setVisibility(View.VISIBLE);
//            mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size() - 1);
//            textView3_5.setText("1/" + addMedicinesArrayList.size());
//        }
//        mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                    Log.e("position", String.valueOf(position));
//                    textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//        }
//        else {
//            if (addMedicinesArrayList.size() > 0) {
//                            addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                frequency_list, frequency2_list, et_no_of_days,
//                                route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe, "pres", fl_progress_bar);
//                        mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                        ll_medicine_product.performClick();
//
//
//
//                textView3_5.setVisibility(View.VISIBLE);
//                mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size() - 1);
//                textView3_5.setText("1/" + addMedicinesArrayList.size());
//
//                mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                            Log.e("position", String.valueOf(position));
//                            textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//                        }
//                    }
//
//                    @Override
//                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        super.onScrolled(recyclerView, dx, dy);
//                    }
//                });
//            }
//        }
//        medicineAddFLAG = false;
//        labtestAddFLAG = false;
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(certificate_selection)
//                {
//                    ll_certificate.setVisibility(View.VISIBLE);
//                    ll_main_certificate_details.setVisibility(View.VISIBLE);
//                    ll_medicinal_lab.setVisibility(View.GONE);
//                    ll_main_lab_test_details.setVisibility(View.GONE);
//                    ll_medicine_product.setVisibility(View.GONE);
//                    ll_main_medicine_details.setVisibility(View.GONE);
//                    ll_end_note.setVisibility(View.GONE);
//                    ll_main_end_note_details.setVisibility(View.GONE);
//                }
//                else  {
//                    ll_certificate.setVisibility(View.GONE);
//                    ll_main_certificate_details.setVisibility(View.GONE);
//                    ll_medicine_product.setVisibility(View.VISIBLE);
//                    ll_main_medicine_details.setVisibility(View.VISIBLE);
//                    if(is_on_lab_test)
//                    {
//                        Toast.makeText(mContext, "is_on_lab_test", Toast.LENGTH_SHORT).show();
//                        setActiveMedicine_LAB();
//                    }
//                    else {
//                        if(definer_type_flag)
//                        {
//                            ll_medicine_product.setVisibility(View.GONE);
//                            ll_medicinal_lab.setVisibility(View.VISIBLE);
//                            ll_end_note.setVisibility(View.GONE);
//                            setActiveMedicine_LAB();
//                            ll_certificate.setVisibility(View.GONE);
//
//                        }
//                        else{
//                            setActiveMedicine_Product();
//                            ll_certificate.setVisibility(View.GONE);
//                        }
//
//                    }
//                }
//            }
//        }, 1);
//      /*  Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(certificate_selection)
//                {
//                    ll_certificate.setVisibility(View.VISIBLE);
//                    ll_main_certificate_details.setVisibility(View.VISIBLE);
//                    ll_medicinal_lab.setVisibility(View.GONE);
//                    ll_main_lab_test_details.setVisibility(View.GONE);
//                    ll_medicine_product.setVisibility(View.GONE);
//                    ll_main_medicine_details.setVisibility(View.GONE);
//                    ll_end_note.setVisibility(View.GONE);
//                    ll_main_end_note_details.setVisibility(View.GONE);
//                }
//                else  {
//                    ll_certificate.setVisibility(View.GONE);
//                    ll_main_certificate_details.setVisibility(View.GONE);
//
//                    if(is_on_lab_test)
//                    {
//                        setActiveMedicine_LAB();
//                    }
//                    else {
//                        setActiveMedicine_Product();
//                    }
//                }
//            }
//        });*/
//
//
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//       // Toast.makeText(mContext, "onPause", Toast.LENGTH_SHORT).show();
//
//    }
//
//    private void initCalls(View view) {
//        patient_id="";
//        et_cert_title = view.findViewById(R.id.et_certificate_title);
//        et_cert_desc = view.findViewById(R.id.et_certificate_desc);
//        ll_patients_view = view.findViewById(R.id.ll_patients_view);
//        ll_prescription_view = view.findViewById(R.id.ll_prescription_view);
//        top_view = view.findViewById(R.id.top_view);
//        ll_info_no_patient = view.findViewById(R.id.info_no_patients);
//        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
//        btn_create_patient = view.findViewById(R.id.btn_create_patient);
//        patientdetails = view.findViewById(R.id.patientdetails);
//      //  btn_clear = view.findViewById(R.id.btn_clear);
//        btn_prescribe = view.findViewById(R.id.btn_prescribe);
//        btn_add = view.findViewById(R.id.btn_add);
//        btn_add_labtest= view.findViewById(R.id.btn_add_labtest);
//        textView3_5 = view.findViewById(R.id.textView3_5);
//        ll_btn_templates = view.findViewById(R.id.ll_btn_templates);
//        ll_medicine_product = view.findViewById(R.id.ll_medicine_product);
//        ll_medicinal_lab = view.findViewById(R.id.ll_medicinal_lab);
//        ll_end_note = view.findViewById(R.id.ll_end_note);
//        ll_certificate = view.findViewById(R.id.ll_certificate);
//        et_chief_complaint = view.findViewById(R.id.et_chief_complaint);
//        et_history = view.findViewById(R.id.et_history);
//        et_findings = view.findViewById(R.id.et_findings);
//        et_end_note = view.findViewById(R.id.et_end_note);
//        et_diagnosis = view.findViewById(R.id.et_diagnosis);
//        et_treatment_advice = view.findViewById(R.id.et_treatment_advice);
//
//        ll_main_lab_test_details = view.findViewById(R.id.ll_main_lab_test_details);
//        ll_main_medicine_details = view.findViewById(R.id.ll_main_medicine_details);
//        ll_main_end_note_details = view.findViewById(R.id.ll_main_end_note_details);
//        ll_main_certificate_details = view.findViewById(R.id.ll_main_certificate);
//
//        ll_medicine_product.setBackground(getResources().getDrawable(R.drawable.blue_round));
//
//        ll_medicine_product.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_medicine_product.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setActiveMedicine_Product();
//                    }
//                });
//            }
//        });
//
//        ll_medicinal_lab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_medicinal_lab.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setActiveMedicine_LAB();
//                    }
//                });
//            }
//        });
//
//        ll_end_note.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_end_note.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setActiveEdit_Note();
//                    }
//                });
//            }
//        });
//        header = view.findViewById(R.id.header);
//        ll_prescription_view.setVisibility(View.GONE);
//        top_view.setVisibility(View.GONE);
//        ll_patients_view.setVisibility(View.GONE);
//        ll_info_no_patient.setVisibility(View.VISIBLE);
//        btn_create_patient.setVisibility(View.GONE);
//        fl_progress_bar.setVisibility(View.VISIBLE);
//
//        lLayout = new LinearLayoutManager(mContext);
//        lLayout1 = new LinearLayoutManager(mContext);
//        rView =  view.findViewById(R.id.patient_recycler);
//        rView.setLayoutManager(lLayout);
//
//        btnChooseFromTemplate = (Button) view.findViewById(R.id.choose);
//        btnChooseFromTemplate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*MyTemplates myFragment = new MyTemplates();
//                Bundle bundle = new Bundle();
//                bundle.putString("patient_id",patient_id)
//                bundle.putString("definer","pres");
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment,
//                        "first").addToBackStack(null).commit();*/
//                backCheckerFlag = false;
//
//                TemplateSelection myFragment = new TemplateSelection();
//                Bundle bundle = new Bundle();
//                bundle.putString("patient_id",patient_id);
//                bundle.putString("definer","pres");
//                bundle.putString("type","medicine");
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment,
//                        "first").addToBackStack("null").commit();
//
//            }
//        });
//
//        btnChooseFromTemplateLabTest = view.findViewById(R.id.choose_labtest);
//        btnChooseFromTemplateLabTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*MyTemplatesLabTest myFragment = new MyTemplatesLabTest();
//                Bundle bundle = new Bundle();
//                bundle.putString("patient_id",patient_id);
//                bundle.putString("definer","pres");
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment,
//                        "first").addToBackStack(null).commit();*/
//                backCheckerFlag = false;
//
//                TemplateSelection myFragment = new TemplateSelection();
//                Bundle bundle = new Bundle();
//                bundle.putString("patient_id",patient_id);
//                bundle.putString("definer","pres");
//                bundle.putString("type","lab");
//
//                myFragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment,
//                        "first").addToBackStack("null").commit();
//            }
//        });
//
//        back = view.findViewById(R.id.btn_back_edit_profile);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
//                builder.setMessage("Would you like to leave this page?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //manager.getPreferences(Registration_.this, "service_provider");
//                                FragmentManager fm = getActivity().getSupportFragmentManager();
//                                fm.popBackStack();
//
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                androidx.appcompat.app.AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });
//        initSpinner(view);
//        initViews(view);
//        initViewHolder();
//        hideKeyboard(mContext);
//        btn_create_patient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PatientRegistration myFragment = new PatientRegistration();
//                assert getFragmentManager() != null;
//                getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
//            }
//        });
//
//        btn_prescribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utils.isConnectingToInternet(mContext)) {
//
//                    if (btn_prescribe.getText().toString().trim().equalsIgnoreCase("Prescribe")) {
//                        if (!("").equalsIgnoreCase(searchBarMaterialMedicine.getText().toString())) {
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
//                            alertDialogBuilder.setCancelable(false);
//                            alertDialogBuilder.setMessage("Kindly save details of current medicine before prescribing!");
//                            alertDialogBuilder.setPositiveButton("Okay",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//                                            alertDialog.dismiss();
//                                        }
//                                    });
//                            alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();
//                        } else {
//                            hideKeyboard(mContext);
//                                GeneratePres myFragment = new GeneratePres();
//                                Bundle bundle = new Bundle();
//                                Date c = Calendar.getInstance().getTime();
//                                System.out.println("Current time => " + c);
//
//                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                                String formattedDate = df.format(c);
//                                patientsItemArrayList = splashActivity.getpatients();
//                                PrescriptionItem prescriptionItem = new PrescriptionItem();
//                                prescriptionItem.setAge(patientsItemArrayList.get(pos).getAge());
//                                prescriptionItem.setDoctorId(manager.getPreferences(mContext, "doctor_id"));
//                                prescriptionItem.setGender(patientsItemArrayList.get(pos).getGender());
//                                prescriptionItem.setPatientId(patientsItemArrayList.get(pos).getPatientId());
//                                prescriptionItem.setPEmail(patientsItemArrayList.get(pos).getPEmail());
//                                prescriptionItem.setPMobile(patientsItemArrayList.get(pos).getPMobile());
//                                prescriptionItem.setPName(patientsItemArrayList.get(pos).getPName());
//                                prescriptionItem.setDate(formattedDate);
//                            if (addMedicinesArrayList.size() != 0) {
//                                prescriptionItem.setMedicines(addMedicinesArrayList);
//                            }
//                            diagnosis_desc = et_chief_complaint.getText().toString().trim()+"|"+
//                                    et_history.getText().toString().trim()+"|"+
//                                    et_findings.getText().toString().trim()+"|"+
//                                    et_diagnosis.getText().toString().trim()+"|"+
//                                    et_treatment_advice.getText().toString().trim()+"|"+
//                                    et_end_note.getText().toString().trim();
//
//                                prescriptionItem.setDiagnosis(diagnosis_desc.trim());
//
//                                Doctor doctor = new Doctor();
//                                doctor.setName(manager.getPreferences(mContext,"name"));
//                                doctor.setEmail(manager.getPreferences(mContext,"email"));
//                                doctor.setAddtionalQualification(manager.getPreferences(mContext,"addtional_qualification"));
//                                doctor.setClinicAddress(manager.getPreferences(mContext,"clinic_address"));
//                                doctor.setClinicName(manager.getPreferences(mContext,"clinic_name"));
//                                doctor.setCloseDay(manager.getPreferences(mContext,"close_day"));
//                                doctor.setDesignationName(manager.getPreferences(mContext,"designation_name"));
//                                doctor.setEmailLetterHead(manager.getPreferences(mContext,"email_letter_head"));
//                                doctor.setImage(manager.getPreferences(mContext,"image"));
//                                doctor.setLogo(manager.getPreferences(mContext,"logo"));
//                                doctor.setMobile(manager.getPreferences(mContext,"mobile"));
//                                doctor.setMobileLetterHead(manager.getPreferences(mContext,"mobile_letter_head"));
//                                doctor.setPgName(manager.getPreferences(mContext,"pg_name"));
//                                doctor.setUgName(manager.getPreferences(mContext,"ug_name"));
//                                doctor.setRegistrationNo(manager.getPreferences(mContext,"registration_no"));
//                                doctor.setSignature(manager.getPreferences(mContext,"signature"));
//                                doctor.setVisitingHrFrom(manager.getPreferences(mContext,"visiting_hr_from"));
//                                doctor.setVisitingHrTo(manager.getPreferences(mContext,"visiting_hr_to"));
//                                doctor.setWorkingDays(manager.getPreferences(mContext,"working_days"));
//                                prescriptionItem.setDoctor(doctor);
//                                bundle.putParcelable("defaultAttributeId", prescriptionItem);
//                                if(addLabTestArrayList.size()!=0)
//                                bundle.putParcelableArrayList("defaultAttributeIdLabTest", addLabTestArrayList);
//                                bundle.putString("definer", "create");
//                                bundle.putString("certificate_selection", ""+certificate_selection);
//                                bundle.putString("certificate_title", ""+et_cert_title.getText().toString().trim());
//                                bundle.putString("certificate_desc", ""+et_cert_desc.getText().toString().trim());
//                                bundle.putString("diagnosis", diagnosis_desc.trim());
//                                if(certificate_selection)
//                                bundle.putString("end_note", "|"+et_cert_title.getText().toString().trim()+"|"+et_cert_desc.getText().toString().trim());
//                                else
//                                    bundle.putString("end_note", et_end_note.getText().toString().trim()+"||");
//
//                            myFragment.setArguments(bundle);
//                                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
//                                fragmentManager.beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
//                             }
//
//                    }else if(btn_prescribe.getText().toString().trim().equalsIgnoreCase("Save Template")) {
//                        if(definer_type_flag) {
//                            if (NEWaddLabTestArrayList.size() != 0) {
//                                apiViewHolder.getDeleteTemplateLabTest(temp_id)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(responseProfile);
//                            } else {
//                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
//                                builder.setMessage("Would you like to delete template?")
//                                        .setCancelable(false)
//                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                delete_flag = true;
//                                                fl_progress_bar.setVisibility(View.VISIBLE);
//                                                apiViewHolder.getDeleteTemplateLabTest(temp_id)
//                                                        .subscribeOn(Schedulers.io())
//                                                        .observeOn(AndroidSchedulers.mainThread())
//                                                        .subscribe(responseProfile);
//
//                                            }
//                                        })
//                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                androidx.appcompat.app.AlertDialog alert = builder.create();
//                                alert.show();
//
//
//
//                            }
//                        }
//                        else
//                        {
//                            if(NEWaddMedicinesArrayList.size()!=0){
//                                apiViewHolder.getDeleteTemplate(temp_id)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(responseProfile);
//                            }else {
//                                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
//                                builder.setMessage("Would you like to delete template?")
//                                        .setCancelable(false)
//                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                delete_flag = true;
//                                                apiViewHolder.getDeleteTemplate(temp_id)
//                                                        .subscribeOn(Schedulers.io())
//                                                        .observeOn(AndroidSchedulers.mainThread())
//                                                        .subscribe(responseProfile);
//
//                                            }
//                                        })
//                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                androidx.appcompat.app.AlertDialog alert = builder.create();
//                                alert.show();
//
//
//                            }
//
//                        }
//                    }
//                } else
//                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//
//        textView3_5.setVisibility(View.GONE);
//       /* btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchBarMaterialMedicine.clearFocus();
//                if (Utils.isConnectingToInternet(mContext)) {
//                    if (et_name_of_medicine.getText().toString().trim().equalsIgnoreCase("")) {
//                        et_name_of_medicine.setFocusable(true);
//                        et_name_of_medicine.setError("Name of the medicine");
//                    } else {
//                        if (frequency.equalsIgnoreCase("")) {
//                            Toast.makeText(mContext, "Select Frequency", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (frequency2.equalsIgnoreCase("")) {
//                                Toast.makeText(mContext, "Select Frequency2", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (et_no_of_days.getText().toString().trim().equalsIgnoreCase("")) {
//                                    et_no_of_days.setFocusable(true);
//                                    et_no_of_days.setError("No of days");
//                                } else {
//                                    if (route.equalsIgnoreCase("")) {
//                                        Toast.makeText(mContext, "Select Route", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        if (instructions_spinner.getSelectedItemPosition()==0) {
//                                            Toast.makeText(mContext, "Select Instruction", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            MedicinesItem medicinesItem = new MedicinesItem();
//                                            medicinesItem.setMedicineName(et_name_of_medicine.getText().toString().trim());
//                                            medicinesItem.setFrequency(frequency.trim());
//                                            medicinesItem.setNoOfDays(et_no_of_days.getText().toString().trim()+" Days");
//                                            medicinesItem.setRoute(route.trim());
//                                            medicinesItem.setInstruction(instructions_spinner.getSelectedItem().toString());
//                                            medicinesItem.setAdditionaComment(et_additional_comments.getText().toString().trim());
//
//                                            addMedicinesArrayList.add(medicinesItem);
//                                            Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
//                                                @Override
//                                                public int compare(MedicinesItem lhs, MedicinesItem rhs) {
//                                                    return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
//                                            });
//                                            addMedicineAdapter = new AddMedicineAdapter(addMedicinesArrayList,
//                                                    et_name_of_medicine, frequency_list,frequency2_list,et_no_of_days,route_list,instructions_list,
//                                                    frequency_spinner,frequency2_spinner,route_spinner,instructions_spinner,et_additional_comments,btn_add);
//                                            mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//
//                                            et_name_of_medicine.setText("");
//                                            et_no_of_days.setText("");
//                                            et_additional_comments.setText("");
//                                            frequency_spinner.setSelection(0);
//                                            frequency2_spinner.setSelection(0);
//                                            route_spinner.setSelection(0);
//                                            instructions_spinner.setSelection(0);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                else
//                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//            }
//        });*/
//
//        btn_add_labtest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideKeyboard(mContext);
//                searchBarMaterialTestLab.clearFocus();
//                if (Utils.isConnectingToInternet(mContext)) {
//
//                    if (searchBarMaterialTestLab.getText().toString().trim().equalsIgnoreCase("")) {
//                        searchBarMaterialTestLab.setFocusable(true);
//                        Toast.makeText(mContext, "Please enter lab test name", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (btn_add_labtest.getText().toString().trim().equalsIgnoreCase("Save and Add next")) {
//
//                            if (!("").equalsIgnoreCase(searchBarMaterialTestLab.getText().toString().trim())) {
//                                LabTestItem labTestItem = new LabTestItem();
//                                labTestItem.setLabTestName(searchBarMaterialTestLab.getText().toString().trim());
//                                labTestItem.setLabTestComment(et_additional_comments_labtests.getText().toString().trim());
//
//                                addLabTestArrayList.add(labTestItem);
//                                com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem labTestItem1 = new com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem();
//                                labTestItem1.setLabTestName(searchBarMaterialTestLab.getText().toString().trim());
//                                labTestItem1.setLabTestComment(et_additional_comments_labtests.getText().toString().trim());
//                                labTestItem1.setLabTemplateId("");
//                                labTestItem1.setLabTemplateTestId("");
//                                NEWaddLabTestArrayList.add(labTestItem1);
////                                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                                        @Override
////                                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                                             return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
////                                    });
//                                String iden = "";
//                                if (("edit").equalsIgnoreCase(define)) {
//                                    iden = "temp";
//                                } else {
//                                    iden = "pres";
//                                }
//
//                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String iden = "";
//                                        if (("edit").equalsIgnoreCase(define)) {
//                                            iden = "temp";
//                                        } else {
//                                            iden = "pres";
//                                        }
//
//                                        addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                                                et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab, mRecyclerViewLabTest, btnChooseFromTemplateLabTest, btn_prescribe, iden);
//                                        mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                                        addLabTestAdapter.notifyDataSetChanged();
//                                    }
//                                });
//
//
//                                if (addLabTestArrayList.size() > 0) {
//                                    textView3_5.setVisibility(View.VISIBLE);
//                                    mRecyclerViewAddedLabTest.smoothScrollToPosition(addLabTestArrayList.size() - 1);
//                                    textView3_5.setText("1/" + addLabTestArrayList.size());
//                                }
//
//
//                                mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                                    @Override
//                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                        super.onScrollStateChanged(recyclerView, newState);
//
//                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                            int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                                            Log.e("position", String.valueOf(position));
//                                            textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                        super.onScrolled(recyclerView, dx, dy);
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(mContext, "Please enter lab test name", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else if (btn_add_labtest.getText().toString().trim().equalsIgnoreCase("Save")) {
//                            if (!("").equalsIgnoreCase(searchBarMaterialTestLab.getText().toString().trim())) {
//                                ArrayList<LabTestItem> newLabTestAL = addLabTestArrayList;
//                                addLabTestArrayList = new ArrayList<>();
//                                for (int i = 0; i < newLabTestAL.size(); i++) {
//                                    if (poss__ == i) {
//                                        LabTestItem labTestItem = new LabTestItem();
//                                        labTestItem.setLabTestName(searchBarMaterialTestLab.getText().toString().trim());
//                                        labTestItem.setLabTestComment(et_additional_comments_labtests.getText().toString().trim());
//
//                                        addLabTestArrayList.add(labTestItem);
//                                    } else {
//                                        addLabTestArrayList.add(newLabTestAL.get(i));
//                                    }
//                                }
////                                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                                        @Override
////                                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                                            return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
////                                    });
//
//                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        btn_add_labtest.setText("Save and Add next");
//                                        String iden= "";
//                                        if(("edit").equalsIgnoreCase(define)) {
//                                            iden = "temp";
//                                        }else {
//                                            iden = "pres";
//                                        }
//
//
//                                        addLabTestAdapter = new AddLabTestAdapter(addLabTestArrayList,
//                                                et_additional_comments_labtests, btn_add_labtest, textView3_5, ll_35, searchBarMaterialTestLab, mRecyclerViewLabTest, btnChooseFromTemplateLabTest, btn_prescribe, iden);
//                                        mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//                                        addLabTestAdapter.notifyDataSetChanged();
//                                    }
//                                });
//
//                                        if (addLabTestArrayList.size() > 0) {
//                                            textView3_5.setVisibility(View.VISIBLE);
//                                            mRecyclerViewAddedLabTest.smoothScrollToPosition(poss__);
//                                            textView3_5.setText("1/" + addLabTestArrayList.size());
//                                        }
//
//                                         poss__ = 0;
//                                        mRecyclerViewAddedLabTest.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                                            @Override
//                                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                                super.onScrollStateChanged(recyclerView, newState);
//
//                                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                                    int position = gridLayoutManager5.findFirstVisibleItemPosition();
//                                                    Log.e("position", String.valueOf(position));
//                                                    textView3_5.setText((position + 1) + "/" + addLabTestArrayList.size());
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                                super.onScrolled(recyclerView, dx, dy);
//                                            }
//                                        });
//
//                                btn_prescribe.setVisibility(View.VISIBLE);
//                                if(("edit").equalsIgnoreCase(define)) {
//                                    btnChooseFromTemplateLabTest.setVisibility(View.GONE);
//                                }else {
//                                    btnChooseFromTemplateLabTest.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                        }
//
//                    searchBarMaterialTestLab.setText("");
//                    et_additional_comments_labtests.setText("");
//                        searchBarMaterialTestLab.setText("");
//                }
//                }
//
//                }
//        });
//
//
//
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideKeyboard(mContext);
//                searchBarMaterialMedicine.clearFocus();
//                if (Utils.isConnectingToInternet(mContext)) {
//
//                    if (searchBarMaterialMedicine.getText().toString().trim().equalsIgnoreCase("")) {
//                        searchBarMaterialMedicine.setFocusable(true);
//                        Toast.makeText(mContext, "Please enter medicine name", Toast.LENGTH_SHORT).show();
//                    } else {
//
//                        if (btn_add.getText().toString().trim().equalsIgnoreCase("Save and Add next")) {
//                            if (!("").equalsIgnoreCase(searchBarMaterialMedicine.getText().toString().trim())) {
//                                MedicinesItem medicinesItem = new MedicinesItem();
//                                medicinesItem.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
//                                medicinesItem.setFrequency(frequency.trim());
//                                medicinesItem.setNoOfDays(et_no_of_days.getText().toString().trim());
//                                medicinesItem.setRoute(route.trim());
//                                medicinesItem.setInstruction(instructions_spinner.getSelectedItem().toString());
//                                medicinesItem.setAdditionaComment(et_additional_comments.getText().toString().trim());
//
//                                addMedicinesArrayList.add(medicinesItem);
//
//                                com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem();
//                                mii.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
//                                mii.setFrequency(frequency.trim());
//                                mii.setNoOfDays(et_no_of_days.getText().toString().trim());
//                                mii.setRoute(route.trim());
//                                mii.setInstruction(instructions_spinner.getSelectedItem().toString());
//                                mii.setAdditionaComment(et_additional_comments.getText().toString().trim());
//                                NEWaddMedicinesArrayList.add(mii);
////                                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                                        @Override
////                                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                                             return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
////                                    });
//                                String iden= "";
//                                if(("edit").equalsIgnoreCase(define)) {
//                                    iden = "temp";
//                                }else {
//                                    iden = "pres";
//                                }
//
//                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        String iden= "";
//                                        if(("edit").equalsIgnoreCase(define)) {
//                                            iden = "temp";
//                                        }else {
//                                            iden = "pres";
//                                        }
//
//                                        addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                                frequency_list, frequency2_list, et_no_of_days,
//                                                route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                                route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe,iden, fl_progress_bar);
//                                        mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                                        mAdapterSearchMedicine.notifyDataSetChanged();
//                                    }
//                                });
//
//
//                                if (addMedicinesArrayList.size() > 0) {
//                                    textView3_5.setVisibility(View.VISIBLE);
//                                    mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size()-1);
//                                    textView3_5.setText("1/" + addMedicinesArrayList.size());
//                                }
//
//
//                                mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                                    @Override
//                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                        super.onScrollStateChanged(recyclerView, newState);
//
//                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                            int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                                            Log.e("position", String.valueOf(position));
//                                            textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                        super.onScrolled(recyclerView, dx, dy);
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(mContext, "Please enter medicine name", Toast.LENGTH_SHORT).show();
//                            }
//                        } else if (btn_add.getText().toString().trim().equalsIgnoreCase("Save")) {
//
//                            if (!("").equalsIgnoreCase(searchBarMaterialMedicine.getText().toString().trim())) {
//                                ArrayList<MedicinesItem> newMedicineAL = addMedicinesArrayList;
//                                addMedicinesArrayList = new ArrayList<>();
//                                NEWaddMedicinesArrayList = new ArrayList<>();
//                                for (int i = 0; i < newMedicineAL.size(); i++) {
//                                    if (poss__ == i) {
//                                        MedicinesItem medicinesItem = new MedicinesItem();
//                                        medicinesItem.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
//                                        medicinesItem.setFrequency(frequency.trim());
//                                        medicinesItem.setNoOfDays(et_no_of_days.getText().toString().trim());
//                                        medicinesItem.setRoute(route.trim());
//                                        medicinesItem.setInstruction(instructions_spinner.getSelectedItem().toString());
//                                        medicinesItem.setAdditionaComment(et_additional_comments.getText().toString().trim());
//
//                                        addMedicinesArrayList.add(medicinesItem);
//
//                                        com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem();
//                                        mii.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
//                                        mii.setFrequency(frequency.trim());
//                                        mii.setNoOfDays(et_no_of_days.getText().toString().trim());
//                                        mii.setRoute(route.trim());
//                                        mii.setInstruction(instructions_spinner.getSelectedItem().toString());
//                                        mii.setAdditionaComment(et_additional_comments.getText().toString().trim());
//                                        NEWaddMedicinesArrayList.add(mii);
//                                    } else {
//                                        com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.NewEntities.MedicinesItem();
//                                        mii.setMedicineName(newMedicineAL.get(i).getMedicineName());
//                                        mii.setFrequency(newMedicineAL.get(i).getFrequency());
//                                        mii.setNoOfDays(newMedicineAL.get(i).getNoOfDays());
//                                        mii.setRoute(newMedicineAL.get(i).getRoute());
//                                        mii.setInstruction(newMedicineAL.get(i).getInstruction());
//                                        mii.setAdditionaComment(newMedicineAL.get(i).getAdditionaComment());
//                                        NEWaddMedicinesArrayList.add(mii);
//
//                                        addMedicinesArrayList.add(newMedicineAL.get(i));
//                                    }
//                                }
////                                    Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
////                                        @Override
////                                        public int compare(MedicinesItem lhs, MedicinesItem rhs) {
////                                            return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
////                                    });
//
//                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        btn_add.setText("Save and Add next");
//                                        String iden= "";
//                                        if(("edit").equalsIgnoreCase(define)) {
//                                            iden = "temp";
//                                        }else {
//                                            iden = "pres";
//                                        }
//                                        addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
//                                                frequency_list, frequency2_list, et_no_of_days,
//                                                route_list, instructions_list, frequency_spinner, frequency2_spinner,
//                                                route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_prescribe,iden, fl_progress_bar);
//                                        mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//                                        mAdapterSearchMedicine.notifyDataSetChanged();
//                                    }
//                                });
//
//                                if (addMedicinesArrayList.size() > 0) {
//                                    textView3_5.setVisibility(View.VISIBLE);
//                                    mRecyclerViewAddedMedicines.smoothScrollToPosition(poss__);
//                                    textView3_5.setText("1/" + addMedicinesArrayList.size());
//                                }
//
//
//                                poss__ = 0;
//                                mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                                    @Override
//                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                        super.onScrollStateChanged(recyclerView, newState);
//
//                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                                            int position = gridLayoutManager3.findFirstVisibleItemPosition();
//                                            Log.e("position", String.valueOf(position));
//                                            textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                        super.onScrolled(recyclerView, dx, dy);
//                                    }
//                                });
//
//                                btn_prescribe.setVisibility(View.VISIBLE);
//                                if(("edit").equalsIgnoreCase(define)) {
//                                    btnChooseFromTemplate.setVisibility(View.GONE);
//                                }else {
//                                    btnChooseFromTemplate.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                        }
//
//                        searchBarMaterialMedicine.setText("");
//                        et_no_of_days.setText("");
//                        et_additional_comments.setText("");
//                        frequency_spinner.setSelection(0);
//                        frequency2_spinner.setSelection(0);
//                        route_spinner.setSelection(0);
//                        instructions_spinner.setSelection(0);
//                        searchBarMaterialMedicine.setText("");
//
//                    }
//                }
//                else
//                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }
//
//    public  static void showPatientList()
//    {
//        HomeActivity.goToPrescription();
//
//    }
//    private void initViewHolder() {
//        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
//
//    }
//
//
//    private void setActiveMedicine_Product()
//    {
//        ll_btn_templates.setVisibility(View.VISIBLE);
//        is_on_lab_test = false;
//        if(addMedicinesArrayList.size()>0)
//        {
//            textView3_5.setVisibility(View.VISIBLE);
//            textView3_5.setText("1/"+addMedicinesArrayList.size());
//
//        }
//        else
//            textView3_5.setVisibility(View.GONE);
//
//        mRecyclerViewAddedMedicines.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerViewAddedLabTest.setVisibility(View.GONE);
//                mRecyclerViewAddedMedicines.setVisibility(View.VISIBLE);
//                mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
//            }
//        });
//
//
//        ll_main_end_note_details.setVisibility(View.GONE);
//        ll_main_medicine_details.setVisibility(View.VISIBLE);
//        ll_main_lab_test_details.setVisibility(View.GONE);
//
//        ll_medicine_product.setBackground(getResources().getDrawable(R.drawable.blue_round));
//        ll_medicinal_lab.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//        ll_end_note.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//    }
//
//    private void setActiveMedicine_LAB()
//    {
//        is_on_lab_test = false;
//        ll_btn_templates.setVisibility(View.GONE);
//        if(addLabTestArrayList.size()>0)
//        {
//            textView3_5.setVisibility(View.VISIBLE);
//            textView3_5.setText("1/"+addLabTestArrayList.size());
//        }
//        else
//            textView3_5.setVisibility(View.GONE);
//
//        mRecyclerViewAddedLabTest.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerViewAddedLabTest.setVisibility(View.VISIBLE);
//                mRecyclerViewAddedMedicines.setVisibility(View.GONE);
//                mRecyclerViewAddedLabTest.setAdapter(addLabTestAdapter);
//            }
//        });
//
//        ll_main_end_note_details.setVisibility(View.GONE);
//        ll_main_medicine_details.setVisibility(View.GONE);
//        ll_main_lab_test_details.setVisibility(View.VISIBLE);
//        ll_medicinal_lab.setBackground(getResources().getDrawable(R.drawable.blue_round));
//        ll_medicine_product.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//        ll_end_note.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//    }
//
//    private void setActiveEdit_Note()
//    {
//        ll_btn_templates.setVisibility(View.GONE);
//        textView3_5.setVisibility(View.GONE);
//        mRecyclerViewAddedLabTest.setVisibility(View.GONE);
//        mRecyclerViewAddedMedicines.setVisibility(View.GONE);
//        ll_main_medicine_details.setVisibility(View.GONE);
//        ll_main_lab_test_details.setVisibility(View.GONE);
//        ll_main_end_note_details.setVisibility(View.VISIBLE);
//        ll_medicinal_lab.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//        ll_medicine_product.setBackground(getResources().getDrawable(R.drawable.faint_white_round));
//        ll_end_note.setBackground(getResources().getDrawable(R.drawable.blue_round));
//    }
//
//
//    SingleObserver<ResponseSuccess> responseProfile = new SingleObserver<ResponseSuccess>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseSuccess response) {
//            if (response != null) {
//
//                Log.e(TAG, "profileDetails: >> " + response.getMessage());
//                if (response.getMessage() == null) {
//                    fl_progress_bar.setVisibility(View.GONE);
//                } else if (response.getMessage().equals("Template Deleted")) {
//                    fl_progress_bar.setVisibility(View.GONE);
//                    if (Utils.isConnectingToInternet(mContext)) {
//
//                        if (!(definer_type_flag)) {
//                            AddTemplateJSON addTemplateJSON = new AddTemplateJSON();
//                            addTemplateJSON.setTemplateName(temp_name);
//                            addTemplateJSON.setCreatedBy(manager.getPreferences(mContext, "doctor_id"));
//                            ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> medicinesItemArrayListO = new ArrayList<>();
//                            Log.e(TAG, "____________ " + addMedicinesArrayList.toString());
//                            for (MedicinesItem mi : addMedicinesArrayList) {
//                                com.likesby.bludoc.ModelLayer.Entities.MedicinesItem mii = new com.likesby.bludoc.ModelLayer.Entities.MedicinesItem();
//                                mii.setMedicineName(mi.getMedicineName());
//                                // mii.setMedicineId(mi.getPresbMedicineId());
//                                mii.setAdditionaComment(mi.getAdditionaComment());
//                                mii.setFrequency(mi.getFrequency());
//                                mii.setInstruction(mi.getInstruction());
//                                mii.setNoOfDays(mi.getNoOfDays());
//                                mii.setRoute(mi.getRoute());
//                                //mii.set(mi.getPresbPatientId());
//                                medicinesItemArrayListO.add(mii);
//                            }
//                            addTemplateJSON.setMedicines(medicinesItemArrayListO);
//
//                            Gson gson = new Gson();
//                            String json = gson.toJson(addTemplateJSON);
//                            if(delete_flag)
//                            {
//                                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//                                fm.popBackStack();
//                            }
//                            else {
//                                apiViewHolder.AddTemplate(json)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(responseAddTemplate);
//                            }
//
//
//                        }
//
//                   /* GeneratePres myFragment = new GeneratePres();
//                    assert getFragmentManager() != null;
//                    getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();*/
//
//                    } else {
//                        Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                    }
//
//                   /* GeneratePres myFragment = new GeneratePres();
//                    assert getFragmentManager() != null;
//                    getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();*/
//
//                }
//                else if(response.getMessage().equals("Lab Test Template Deleted"))
//                {
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    if (definer_type_flag) {
//                        btnChooseFromTemplateLabTest.setVisibility(View.GONE);
//                        AddTemplateLabTestJSON addTemplateLabTestJSON = new AddTemplateLabTestJSON();
//                        addTemplateLabTestJSON.setLabTemplateName(temp_name);
//                        addTemplateLabTestJSON.setCreatedBy(manager.getPreferences(mContext, "doctor_id"));
//                        addTemplateLabTestJSON.setLabTest(addLabTestArrayList);
//
//                        Gson gson = new Gson();
//                        String json = gson.toJson(addTemplateLabTestJSON);
//                        if(delete_flag)
//                        {
//                            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//                            fm.popBackStack();
//                        }
//                        else {
//                            apiViewHolder.AddTemplateLabTest(json)
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(responseAddTemplate);
//                        }
//                    }
//                }
//                else {
//                    fl_progress_bar.setVisibility(View.GONE);
//
//                    Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            fl_progress_bar.setVisibility(View.GONE);
//
//            Log.e(TAG, "onError: profileDetails >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private SingleObserver<ResponseSuccess> responseAddTemplate = new SingleObserver<ResponseSuccess>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseSuccess response) {
//            if (response != null) {
//
//                Log.e(TAG, "responseAddTemplate: >> " + response.getMessage());
//                if (response.getMessage() == null) {
//                    Toast.makeText(mContext, "Unable to process your request", Toast.LENGTH_SHORT).show();
//                } else if (response.getMessage().equals("Template Added")) {
//                    Toast.makeText(mContext, "Template Updated", Toast.LENGTH_SHORT).show();
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    fm.popBackStack();
//                }
//                else
//                {
//
//                }
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
//            Log.e(TAG, "onError: responseAddTemplate >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
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
//                fl_progress_bar.setVisibility(View.GONE);
//                btn_create_patient.setVisibility(View.VISIBLE);
//                if (response.getMessage() == null) {
//                    ll_info_no_patient.setVisibility(View.VISIBLE);
//
//                } else if (response.getMessage().equals("patients")) {
//                    patient_id = "";
//                    patientsItemArrayList = response.getPatients();
//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            patientsAdapter = new PatientsAdapter(patientsItemArrayList,ll_patients_view,ll_prescription_view,top_view,btn_create_patient, patientdetails,apiViewHolder,mBag, fl_progress_bar, ll_medicinal_lab, ll_medicine_product, ll_end_note, ll_certificate, ll_main_medicine_details, ll_main_lab_test_details, ll_main_end_note_details, ll_main_certificate_details);
//                            mRecyclerViewPatients.setAdapter(patientsAdapter);
//                        }
//                    });
//
//
//                    ll_patients_view.setVisibility(View.VISIBLE);
//                    ll_info_no_patient.setVisibility(View.GONE);
//                    hideKeyboard(mContext);
//                }
//                else
//                {
//                    ll_info_no_patient.setVisibility(View.VISIBLE);
//                }
//            }
//            else
//            {
//                fl_progress_bar.setVisibility(View.GONE);
//                ll_info_no_patient.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            btn_create_patient.setVisibility(View.VISIBLE);
//            fl_progress_bar.setVisibility(View.GONE);
//            ll_info_no_patient.setVisibility(View.VISIBLE);
//            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private SingleObserver<ResponseSuccess> responsePrescription = new SingleObserver<ResponseSuccess>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseSuccess response) {
//            if (response != null) {
//
//                Log.e(TAG, "responsePrescription: >> " + response.getMessage());
//                if (response.getMessage() == null) {
//
//                } else if (response.getMessage().equals("Prescription Added")) {
//
//                    Toast.makeText(mContext, "Prescription Added", Toast.LENGTH_SHORT).show();
//
//                    apiViewHolder.getAllPrescription(manager.getPreferences(mContext, "doctor_id"))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(responseHistory);
//                }
//                else
//                {
//
//                }
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
//            Log.e(TAG, "onError: responsePrescription >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//
//    private SingleObserver<ResponseHistory> responseHistory = new SingleObserver<ResponseHistory>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(ResponseHistory response) {
//            if (response != null) {
//
//                Log.e(TAG, "responseTemplates: >> " + response.getMessage());
//
//                if (response.getMessage() == null) {
//
//
//                } else if (response.getMessage().equals("Prescription")) {
//                    for(PrescriptionItem prescriptionItem :response.getPrescription())
//                    {
//                        if(prescriptionItem.getPatientId().equalsIgnoreCase(patient_id))
//                        {
//                            GeneratePres myFragment = new GeneratePres();
//                            Bundle bundle = new Bundle();
//
//                            bundle.putParcelable("defaultAttributeId", prescriptionItem);
//                            bundle.putString("definer", "history");
//                            myFragment.setArguments(bundle);
//                            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
//                            fragmentManager.beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
//                            break;
//                        }
//                    }
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
//
//
//    private void initSpinner(View view) {
//        hideKeyboard(mContext);
//        frequency2_spinner = view.findViewById(R.id.frequency2_spinner);
//        frequency2_list = new ArrayList<>();
//        frequency2_list.add("None");
//        frequency2_list.add("Once A Day");
//        frequency2_list.add("Twice A Day");
//        frequency2_list.add("Thrice A Day");
//        frequency2_list.add("Four Times A Day");
//        frequency2_list.add("Immediately");
//        frequency2_list.add("When Required");
//        frequency2_list.add("Before Sleep");
//
//        frequency2_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
//                frequency2_list);
//        frequency2_adp.setDropDownViewResource(R.layout.simple_spinner);
//        frequency2_spinner.setAdapter(frequency2_adp);
//
//
//        frequency_spinner = view.findViewById(R.id.frequency_spinner);
//        frequency_list = new ArrayList<>();
//        frequency_list.add("None");
//        frequency_list.add("OD (Once A Day)");
//        frequency_list.add("BD (Twice A Day)");
//        frequency_list.add("TDS (Thrice A Day)");
//        frequency_list.add("QID (Four Times A Day)");
//        frequency_list.add("Stat (Immediately)");
//        frequency_list.add("SOS (When Required)");
//        frequency_list.add("HS (Before Sleep)");
//
//        frequency_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
//                frequency_list);
//        frequency_adp.setDropDownViewResource(R.layout.simple_spinner);
//
//        frequency_spinner.setAdapter(frequency_adp);
//
//        frequency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position>0)
//                {
//                    frequency = frequency_list.get(position).toString();
//                    frequency2_spinner.setSelection(position);
//                    hideKeyboard(mContext);
//                    //searchBarMaterialMedicine.findFocus().clearFocus();
//
//                }else {
//                    frequency = "";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//
//
//        /*frequency2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position>0)
//                {
//                    frequency2 = frequency2_list.get(position).toString();
//                    hideKeyboard();
//                    searchBarMaterialMedicine.clearFocus();
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });*/
//
//        route_spinner = view.findViewById(R.id.route_spinner);
//        route_list = new ArrayList<>();
//        route_list.add("None");
//        route_list.add("Oral");
//        route_list.add("Topical");
//        route_list.add("Nasal");
//        route_list.add("Drops");
//        route_list.add("Syrup");
//        route_list.add("Ointment");
//        route_list.add("Injectable");
//        route_list.add("Sub-Lingual");
//        route_list.add("Dermal");
//        route_list.add("Mucosal");
//        route_list.add("Rectal");
//        route_list.add("Intradermal");
//        route_list.add("Subcutaneous");
//        route_list.add("Eye Drops");
//        route_list.add("Ear Drops");
//        route_list.add("Toothpaste");
//
//        route_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
//                route_list);
//        route_adp.setDropDownViewResource(R.layout.simple_spinner);
//        route_spinner.setAdapter(route_adp);
//
//        route_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position>0)
//                {
//                    route = route_list.get(position).toString();
//                    hideKeyboard(mContext);
//                }else {
//                    route = "";
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        instructions_spinner = view.findViewById(R.id.instructions_spinner);
//        instructions_list = new ArrayList<>();
//        instructions_list.add("None");
//        instructions_list.add("After Meal");
//        instructions_list.add("Before Meal");
//        instructions_list.add("Empty Stomach");
//        instructions_list.add("Anytime Of The Day");
//        instructions_list.add("Not Specific");
//        instructions_list.add("After Breakfast");
//        instructions_list.add("After Lunch");
//        instructions_list.add("Before Breakfast");
//        instructions_list.add("Before Lunch");
//        instructions_list.add("Before Dinner");
//        instructions_list.add("After Dinner");
//        instructions_list.add("Early Morning Empty Stomach");
//
//        instructions_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
//                instructions_list);
//        instructions_adp.setDropDownViewResource(R.layout.simple_spinner);
//        instructions_spinner.setAdapter(instructions_adp);
//
//        instructions_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position>0)
//                {
//                    instructions = instructions_list.get(position).toString();
//                    hideKeyboard(mContext);
//                }else {
//                    instructions = "";
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//    }
//
//    private void initViews(View view) {
//       // et_name_of_medicine = view.findViewById(R.id.et_name_of_medicine);
//        et_no_of_days = view.findViewById(R.id.et_days);
//        et_frequency = view.findViewById(R.id.et_frequency);
//        et_frequency2 = view.findViewById(R.id.et_frequency1);
//        et_route = view.findViewById(R.id.et_Route);
//        et_additional_comments = view.findViewById(R.id.et_comments);
//        et_additional_comments_labtests = view.findViewById(R.id.labtest_additional_comments);
//        et_instructions = view.findViewById(R.id.et_instruction);
//
//        mRecyclerViewMedicines = view.findViewById(R.id.medicines_recycler);
//        mRecyclerViewPatients = view.findViewById(R.id.patient_recycler);
//        mRecyclerViewAddedMedicines = view.findViewById(R.id.added_medicines_recycler);
//        mRecyclerViewAddedLabTest = view.findViewById(R.id.added_lab_test_recycler);
//        mRecyclerViewLabTest= view.findViewById(R.id.labtest_recycler);
//
//        searchBarMaterialPatient = view.findViewById(R.id.patient_searchview);
//        searchBarMaterialPatient.setHint("Type patient name here");
//
//        searchBarMaterialPatient.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence newText, int start, int before, int count) {
//
//                if (newText.length() > 1) {
//                    if(patientsAdapter!=null)
//                    patientsAdapter.getFilter().filter(newText);
//                }
//                else  if (newText.length() < 1) {
//
//                    if(patientsAdapter!=null)
//                    patientsAdapter.getFilter().filter("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        searchBarMaterialMedicine = view.findViewById(R.id.medicine_searchview);
//        searchBarMaterialMedicine.setHint("Type here");
//        searchBarMaterialMedicine.post(new Runnable() {
//            @Override
//            public void run() {
//                searchBarMaterialMedicine.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence newText, int start, int before, int count) {
//                        if(!newText.equals("\n")) {
//                            if (newText.length() > 1) {
//                                mRecyclerViewMedicines.setVisibility(View.VISIBLE);
//                                mAdapterSearchMedicine.getFilter().filter(newText);
//                                mAdapterSearchMedicine.notifyDataSetChanged();
//                            } else    if (newText.length()==0)
//                                mRecyclerViewMedicines.setVisibility(View.GONE);
//                        }else    if (newText.length() < 1) {
//
//                            searchBarMaterialMedicine.setText("");
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                    }
//                });
//            }
//        });
//
//
//
//        searchBarMaterialTestLab = view.findViewById(R.id.labtest_searchview);
//        searchBarMaterialTestLab.setHint("Type here");
//        searchBarMaterialTestLab.post(new Runnable() {
//            @Override
//            public void run() {
//                searchBarMaterialTestLab.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence newText, int start, int before, int count) {
//                        if(!newText.equals("\n")) {
//                            if (newText.length() > 1) {
//                                mRecyclerViewLabTest.setVisibility(View.VISIBLE);
//                                mAdapterSearchLabTest.getFilter().filter(newText);
//                                mAdapterSearchLabTest.notifyDataSetChanged();
//                            } else if (newText.length() ==0)
//                                mRecyclerViewLabTest.setVisibility(View.GONE);
//                        }else  if (newText.length() < 1)
//                        {
//                            searchBarMaterialTestLab.setText("");
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                    }
//                });
//            }
//        });
//
//
//        initRecyclerViews();
//
//        AddTemplateAdapter templateAdapter = new AddTemplateAdapter(mContext);
//        rView.setAdapter(templateAdapter);
//    }
//
//    //================     Main  Categories       ==============================================
//    private void initRecyclerViews() {
//        //Create new GridLayoutManager
//        gridLayoutManager = new GridLayoutManager(mContext,
//                1,//span count no of items in single row
//                GridLayoutManager.VERTICAL,//Orientation
//                false);//reverse scrolling of recyclerview
//        //set layout manager as gridLayoutManager
//
//        gridLayoutManager2 = new GridLayoutManager(mContext,
//                1,//span count no of items in single row
//                GridLayoutManager.VERTICAL,//Orientation
//                false);//reverse scrolling of recyclerview
//
//        gridLayoutManager4 = new GridLayoutManager(mContext,
//                1,//span count no of items in single row
//                GridLayoutManager.VERTICAL,//Orientation
//                false);//reverse scrolling of recyclerview
//
//
//        gridLayoutManager3 = new GridLayoutManager(mContext,
//                1,//span count no of items in single row
//                GridLayoutManager.HORIZONTAL,//Orientation
//                false);//reverse scrolling of recyclerview
//
//        gridLayoutManager5 = new GridLayoutManager(mContext,
//                1,//span count no of items in single row
//                GridLayoutManager.HORIZONTAL,//Orientation
//                false);//reverse scrolling of recyclerview
//
//
//        mRecyclerViewPatients.setLayoutManager(gridLayoutManager);
//        mRecyclerViewPatients.setNestedScrollingEnabled(false);
//
//        mRecyclerViewMedicines.setLayoutManager(gridLayoutManager2);
//        mRecyclerViewMedicines.setNestedScrollingEnabled(false);
//
//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(mRecyclerViewMedicines);
//
//        mRecyclerViewLabTest.setLayoutManager(gridLayoutManager4);
//        mRecyclerViewLabTest.setNestedScrollingEnabled(false);
//
//        SnapHelper snapHelper3 = new PagerSnapHelper();
//        snapHelper3.attachToRecyclerView(mRecyclerViewLabTest);
//
//        lLayout1 = new LinearLayoutManager(mContext);
//       // mRecyclerViewMedicines.setLayoutManager(lLayout1);
//
//        mRecyclerViewAddedMedicines.setLayoutManager(gridLayoutManager3);
//        mRecyclerViewAddedMedicines.setNestedScrollingEnabled(false);
//
//        SnapHelper snapHelper2 = new PagerSnapHelper();
//        snapHelper2.attachToRecyclerView(mRecyclerViewAddedMedicines);
//
//        mRecyclerViewAddedLabTest.setLayoutManager(gridLayoutManager5);
//        mRecyclerViewAddedLabTest.setNestedScrollingEnabled(false);
//
//        SnapHelper snapHelper4 = new PagerSnapHelper();
//        snapHelper4.attachToRecyclerView(mRecyclerViewAddedLabTest);
//
//        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mAdapterSearchMedicine = new SearchAdapter(MedicAll,
//                        frequency_list,frequency2_list,et_no_of_days,route_list,instructions_list,
//                        frequency_spinner,frequency2_spinner,route_spinner,instructions_spinner,et_additional_comments,
//                        mRecyclerViewMedicines,searchBarMaterialMedicine);
//                mRecyclerViewMedicines.getRecycledViewPool().clear();
//                mRecyclerViewMedicines.setAdapter(mAdapterSearchMedicine);
//                mAdapterSearchMedicine.notifyDataSetChanged();
//            }
//        });
//
//        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mAdapterSearchLabTest= new SearchLabTestAdapter(myDB.getLabTests(),
//                        et_additional_comments_labtests,
//                        mRecyclerViewLabTest,searchBarMaterialTestLab);
//                mRecyclerViewLabTest.setAdapter(mAdapterSearchLabTest);
//                mAdapterSearchLabTest.notifyDataSetChanged();
//            }
//        });
//
//        mRecyclerViewAddedLabTest.setVisibility(View.GONE);
//        mRecyclerViewAddedMedicines.setVisibility(View.VISIBLE);
//
//    }
//
//
//    private void search(androidx.appcompat.widget.SearchView searchView) {
//        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e(TAG, "onQueryTextSubmit = " + query);
//                //mRecyclerViewMedicines.setVisibility(View.VISIBLE);
//                // mAdapterSearchMedicine.getFilter().filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.e(TAG, "onQueryTextChange = " + newText);
//                return true;
//            }
//        });
//    }
//
//
//
//    public static void hideKeyboard(Context ctx) {
//        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
//        View v = ((Activity) ctx).getCurrentFocus();
//        if (v == null)
//            return;
//        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }
//
//
//
//}
//
//
