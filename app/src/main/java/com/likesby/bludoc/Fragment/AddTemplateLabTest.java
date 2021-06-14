package com.likesby.bludoc.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.likesby.bludoc.Adapter.AddLabTestTemplateAdapter;
import com.likesby.bludoc.Adapter.PatientsAdapter;
import com.likesby.bludoc.Adapter.SearchLabTestAdapter;
import com.likesby.bludoc.ModelLayer.Entities.DesignationItem;
import com.likesby.bludoc.ModelLayer.Entities.PGItem;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.Entities.SpecialitiesItem;
import com.likesby.bludoc.ModelLayer.Entities.UGItem;
import com.likesby.bludoc.ModelLayer.NewEntities.AddTemplateLabTestJSON;

import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.likesby.bludoc.HomeActivity.poss__;

public class AddTemplateLabTest extends Fragment {
    private static Context mContext;
    private Dialog dialog;
    private View v;
    static RecyclerView rView;
    static LinearLayoutManager lLayout;
    private Button btnChooseFromTemplate;

    private EditText et_additional_comments;
    boolean logo_flag = false;
    private SessionManager manager;
    TextView HeadertextView;
    private static final String TAG = "CreatePrescri_____";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();



    ArrayList<DesignationItem> designationItemArrayList;
    //    ArrayList<String> working_days_Arraylist,visit_hrs_from_Arraylist,
    //            visit_hrs_to_Arraylist,closed_day_Arraylist;;
    ArrayList<PGItem> pgItemArrayList;
    ArrayList<UGItem> ugItemArrayList;
    ArrayList<SpecialitiesItem> specialitiesItemArrayList;
    ArrayList<PatientsItem> patientsItemArrayList;
    private String frequency = "", medicine_name = "", frequency2 = "", no_of_days = "", route = "",
            add_instructions = "", instructions = "";
    private androidx.appcompat.widget.SearchView searchViewPatient,searchViewMedicine;
    private RecyclerView mRecyclerViewMedicines,mRecyclerViewAddedMedicines;
    private SearchLabTestAdapter mAdapterSearchMedicine;
    private PatientsAdapter patientsAdapter;
  //  private MaterialSearchBar searchBarMaterialPatient;
    EditText searchBarMaterialMedicine;
    FrameLayout fl_progress_bar;
    Button btn_save_template,btn_add;
    ArrayList<LabTestItem> addMedicinesArrayList = new ArrayList<>();
    AddLabTestTemplateAdapter addLabTestAdapter;
    TextView template_name;
    String template_name__="";
    TextView textView3_5;
    LinearLayout ll_35;
    GridLayoutManager gridLayoutManager2,gridLayoutManager3;
    ImageView back;
    FrameLayout fl_progress_layout;
    MyDB myDB;

    ArrayList<com.likesby.bludoc.ModelLayer.Entities.LabTestItem> SearchLab;
    Boolean searchFlagLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
        myDB = new MyDB(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.add_template_lab_test, container, false);
        Bundle args = getArguments();

        template_name = v.findViewById(R.id.textView_template_name);
        assert args != null;
        template_name__ = args.getString("name");
        template_name.setText(template_name__);

        v.findViewById(R.id.speech_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(intentSpeech, 500);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(
                            mContext,
                            "Oops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }
        });

        initCalls(v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 500 && data != null) {

                ArrayList<String> stringArrayListExtra =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBarMaterialMedicine.setText(stringArrayListExtra.get(0));

                searchBarMaterialMedicine.post(new Runnable() {
                    @Override
                    public void run() {
                        searchBarMaterialMedicine.setSelection(searchBarMaterialMedicine.getText().length());
                    }
                });

            }
        }

    }

    private void initCalls(View view) {

    //    btn_clear = view.findViewById(R.id.btn_clear);
        btn_save_template = view.findViewById(R.id.btn_save_template);
        textView3_5 = view.findViewById(R.id.textView3_5);
        ll_35= view.findViewById(R.id.ll_35);
        btn_add = view.findViewById(R.id.btn_add);
        fl_progress_layout = view.findViewById(R.id.fl_progress_layout);
        initViews(view);
        initViewHolder();


        btn_save_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(mContext);
                searchBarMaterialMedicine.clearFocus();
                if(addMedicinesArrayList.size()!=0)
                {


                if (Utils.isConnectingToInternet(mContext)) {
                    fl_progress_layout.setVisibility(View.VISIBLE);
                    AddTemplateLabTestJSON addTemplateJSON = new AddTemplateLabTestJSON();
                    addTemplateJSON.setLabTemplateName(template_name__);
                    addTemplateJSON.setCreatedBy(manager.getPreferences(mContext, "doctor_id"));
                    addTemplateJSON.setLabTest(addMedicinesArrayList);

                    Gson gson = new Gson();
                    String json = gson.toJson(addTemplateJSON);

                    apiViewHolder.AddTemplateLabTest(json)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseAddTemplate);

                   /* GeneratePres myFragment = new GeneratePres();
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();*/

                } else
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(mContext, "Add at least one lab test", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addMedicinesArrayList = new ArrayList<>();
        ll_35.setVisibility(View.GONE);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mContext);
                searchBarMaterialMedicine.clearFocus();
                if (Utils.isConnectingToInternet(mContext)) {

                    if (searchBarMaterialMedicine.getText().toString().trim().equalsIgnoreCase("")) {
                        searchBarMaterialMedicine.setFocusable(true);
                        Toast.makeText(mContext, "Please enter lab test / imaging name", Toast.LENGTH_SHORT).show();

                    } else {

                        if(!("").equalsIgnoreCase(searchBarMaterialMedicine.getText().toString().trim())) {
                            if(btn_add.getText().toString().trim().equalsIgnoreCase("Save and Add next"))
                            {
                                btn_save_template.setVisibility(View.VISIBLE);
                                LabTestItem labTestItem = new LabTestItem();
                                    labTestItem.setLabTestName(searchBarMaterialMedicine.getText().toString().trim());
                                    labTestItem.setLabTestComment(et_additional_comments.getText().toString().trim());


                                    addMedicinesArrayList.add(labTestItem);
//                                                Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
//                                                    @Override
//                                                    public int compare(MedicinesItem lhs, MedicinesItem rhs) {
//                                                        return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
//                                                });

                                addLabTestAdapter = new AddLabTestTemplateAdapter(addMedicinesArrayList,
                                             et_additional_comments, btn_add, textView3_5,ll_35, searchBarMaterialMedicine,mRecyclerViewMedicines, btnChooseFromTemplate, btn_save_template,"template");

                                    mRecyclerViewAddedMedicines.setAdapter(addLabTestAdapter);
                                    if (addMedicinesArrayList.size() > 0) {
                                        textView3_5.setVisibility(View.VISIBLE);
                                        ll_35.setVisibility(View.VISIBLE);
                                        mRecyclerViewAddedMedicines.smoothScrollToPosition(addMedicinesArrayList.size()-1);
                                }


                                    textView3_5.setText("1/" + addMedicinesArrayList.size());
                                    mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                                                int position = gridLayoutManager3.findFirstVisibleItemPosition();
                                                Log.e("position", String.valueOf(position));
                                                textView3_5.setText((position + 1) + "/" + addMedicinesArrayList.size());

                                                addLabTestAdapter.notifyItemChanged(position);

                                            }
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                        }
                                    });

                            }
                            else if(btn_add.getText().toString().trim().equalsIgnoreCase("Save"))
                            {
                                ArrayList<LabTestItem> newMedicineAL = addMedicinesArrayList;
                                addMedicinesArrayList = new ArrayList<>();
                                btn_save_template.setVisibility(View.VISIBLE);
                                for(int i=0;i<newMedicineAL.size();i++)
                                {
                                    if(poss__ ==i )
                                    {
                                        LabTestItem labTestItem = new LabTestItem();
                                        labTestItem.setLabTestName(searchBarMaterialMedicine.getText().toString().trim());

                                        labTestItem.setLabTestComment(et_additional_comments.getText().toString().trim());

                                        addMedicinesArrayList.add(labTestItem);
                                    }
                                    else
                                    {
                                        addMedicinesArrayList.add(newMedicineAL.get(i));
                                    }
                                }

//                                                Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
//                                                    @Override
//                                                    public int compare(MedicinesItem lhs, MedicinesItem rhs) {
//                                                        return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
//                                                });
                                btn_add.setText("Save and Add next");

                                addLabTestAdapter = new AddLabTestTemplateAdapter(addMedicinesArrayList,
                                        et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine,mRecyclerViewMedicines, btnChooseFromTemplate, btn_save_template,"template");

                                mRecyclerViewAddedMedicines.setAdapter(addLabTestAdapter);

                                if (addMedicinesArrayList.size() > 0) {
                                    textView3_5.setVisibility(View.VISIBLE);
                                    ll_35.setVisibility(View.VISIBLE);
                                    mRecyclerViewAddedMedicines.smoothScrollToPosition(poss__);
                                }
                                textView3_5.setText("1/"+addMedicinesArrayList.size());

                                poss__ = 0;
                                mRecyclerViewAddedMedicines.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);

                                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                                            int position = gridLayoutManager3.findFirstVisibleItemPosition();
                                            Log.e("position", String.valueOf(position));
                                            textView3_5.setText((position+1)+"/"+addMedicinesArrayList.size());

                                        }
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                    }
                                });
                            }
                            searchBarMaterialMedicine.setText("");
                            searchBarMaterialMedicine.setVisibility(View.VISIBLE);
                            et_additional_comments.setText("");

                            searchBarMaterialMedicine.requestFocus();
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        }else {
                            Toast.makeText(mContext, "Please enter lab test / imaging name", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    private SingleObserver<ResponseSuccess> responseAddTemplate = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {
            if (response != null) {
                fl_progress_layout.setVisibility(View.GONE);

                Log.e(TAG, "responseAddTemplate: >> " + response.getMessage());
                if (response.getMessage() == null) {


                } else if (response.getMessage().equals("Lab Template Added")) {
                    Toast.makeText(mContext, "Lab Template Added", Toast.LENGTH_SHORT).show();
                    MyTemplatesLabTest myTemplates = new MyTemplatesLabTest();
                    myTemplates.initViewHolder();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
                else
                {

                }
            }
            else
            {
                fl_progress_layout.setVisibility(View.GONE);

            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_layout.setVisibility(View.GONE);

            Log.e(TAG, "onError: responseAddTemplate >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    public  String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // get current date time with Date()
        Date date = new Date();
        // System.out.println(dateFormat.format(date));
        // don't print it, but save it!
        return dateFormat.format(date);
    }






    private void initViews(View view) {
      //  et_name_of_medicine = view.findViewById(R.id.et_name_of_medicine);

        et_additional_comments = view.findViewById(R.id.et_comments);
        back = view.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        mRecyclerViewMedicines = view.findViewById(R.id.medicines_recycler);
        mRecyclerViewAddedMedicines = view.findViewById(R.id.templates_added_medicine_recycler);


        searchBarMaterialMedicine = view.findViewById(R.id.templates_searchview);
        searchBarMaterialMedicine.setHint("Type here");
        searchBarMaterialMedicine.clearFocus();
        searchBarMaterialMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {


                ArrayList<com.likesby.bludoc.ModelLayer.Entities.LabTestItem> favorites1 = searchtextlab(String.valueOf(newText));
                if (searchFlagLab) {
                    if (favorites1.size() == 0) {
                        mRecyclerViewMedicines.setVisibility(View.GONE);
                    } else {
                        mRecyclerViewMedicines.setVisibility(View.VISIBLE);

                        mAdapterSearchMedicine = new SearchLabTestAdapter(favorites1,
                                et_additional_comments,
                                mRecyclerViewMedicines, searchBarMaterialMedicine);
                        mRecyclerViewMedicines.setAdapter(mAdapterSearchMedicine);
                        mAdapterSearchMedicine.notifyDataSetChanged();

                    }
                } else {
                    mRecyclerViewMedicines.setVisibility(View.GONE);
                }
//
//                if (newText.length() > 1) {
//                    mRecyclerViewMedicines.setVisibility(View.VISIBLE);
//                    mAdapterSearchMedicine.getFilter().filter(newText);
//                } else  if (newText.length() ==0)
//                    mRecyclerViewMedicines.setVisibility(View.GONE);
//            }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        initRecyclerViews();
    }

    private  ArrayList<com.likesby.bludoc.ModelLayer.Entities.LabTestItem> searchtextlab(String query) {
        query = query.toLowerCase();
        SearchLab = new ArrayList<>();
        searchFlagLab =false;
        if(query.length() >= 3) {
            query= query.trim().replaceAll("\\s+", "%%") + "%%";
            //  query = query.replace(" ", "%%");
            query= query.trim().replaceAll("'", "%%");
            SearchLab = myDB.getSearchDataLab(query);
            searchFlagLab = true;
        }else {
            searchFlagLab = false;
            SearchLab = new ArrayList<>();
        }
        return SearchLab;
    }


    //================     Main  Categories       ==============================================
    private void initRecyclerViews() {
        //Create new GridLayoutManager


      gridLayoutManager2 = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview


         gridLayoutManager3 = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.HORIZONTAL,//Orientation
                false);//reverse scrolling of recyclerview



        mRecyclerViewMedicines.setLayoutManager(gridLayoutManager2);
        mRecyclerViewMedicines.setNestedScrollingEnabled(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerViewMedicines);

        mRecyclerViewAddedMedicines.setLayoutManager(gridLayoutManager3);
        mRecyclerViewAddedMedicines.setNestedScrollingEnabled(false);

        SnapHelper snapHelper2 = new PagerSnapHelper();
        snapHelper2.attachToRecyclerView(mRecyclerViewAddedMedicines);


        mAdapterSearchMedicine = new SearchLabTestAdapter(myDB.getLabTests(),
                 et_additional_comments,                mRecyclerViewMedicines,searchBarMaterialMedicine);
        mRecyclerViewMedicines.setAdapter(mAdapterSearchMedicine);
    }


    private void search(androidx.appcompat.widget.SearchView searchView) {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit = " + query);
                //mRecyclerViewMedicines.setVisibility(View.VISIBLE);
                // mAdapterSearchMedicine.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange = " + newText);
                return true;
            }
        });
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}