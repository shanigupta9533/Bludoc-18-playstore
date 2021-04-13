package com.likesby.bludoc.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.likesby.bludoc.Adapter.AddMedicineAdapter;
import com.likesby.bludoc.Adapter.PatientsAdapter;
import com.likesby.bludoc.Adapter.SearchAdapter;
import com.likesby.bludoc.ModelLayer.Entities.AddTemplateJSON;
import com.likesby.bludoc.ModelLayer.Entities.DesignationItem;
import com.likesby.bludoc.ModelLayer.Entities.PGItem;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.Entities.SpecialitiesItem;
import com.likesby.bludoc.ModelLayer.Entities.UGItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem;
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

import static com.likesby.bludoc.HomeActivity.poss__;

public class AddTemplate  extends Fragment {
    private static Context mContext;
    private Dialog dialog;
    private View v;
    static RecyclerView rView;
    static LinearLayoutManager lLayout;
    private Button btnChooseFromTemplate;

    private EditText et_additional_comments, et_frequency,
            et_frequency2, et_route, et_no_of_days, et_instructions;
    boolean logo_flag = false;
    private SessionManager manager;
    TextView HeadertextView;
    private static final String TAG = "CreatePrescri_____";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();

    private Spinner frequency_spinner, frequency2_spinner, route_spinner, no_of_days_spinner, instructions_spinner;
    private ArrayAdapter<String> frequency_adp, frequency2_adp, route_adp, no_of_days_adp, instructions_adp;

    private ArrayList<String> frequency_list, frequency2_list, route_list, no_of_days_list, instructions_list;
    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem>  MedicAll;
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
    private SearchAdapter mAdapterSearchMedicine;

    private PatientsAdapter patientsAdapter;
  //  private MaterialSearchBar searchBarMaterialPatient;
    EditText searchBarMaterialMedicine;
    FrameLayout fl_progress_bar;
    Button btn_save_template,btn_add;
    ArrayList<MedicinesItem> addMedicinesArrayList = new ArrayList<>();
    AddMedicineAdapter addMedicineAdapter;
    TextView template_name;
    String template_name__="";
    TextView textView3_5;
    LinearLayout ll_35;
    GridLayoutManager gridLayoutManager2,gridLayoutManager3;
    ImageView back;
    MyDB myDB;
    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> SearchMedicine;
    Boolean searchFlag;
    private EditText medicine_qty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
        MedicAll= new ArrayList<>();
        myDB = new MyDB(mContext);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.add_template, container, false);
        Bundle args = getArguments();
        MedicAll = myDB.getMedicines();
        template_name = v.findViewById(R.id.textView_template_name);
        assert args != null;
        template_name__ = args.getString("name");
        template_name.setText(template_name__);
        initCalls(v);

        return v;
    }

    private void initCalls(View view) {

        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        btn_save_template = view.findViewById(R.id.btn_save_template);
        textView3_5 = view.findViewById(R.id.textView3_5);
        ll_35= view.findViewById(R.id.ll_35);
        btn_add = view.findViewById(R.id.btn_add);

        initSpinner(view);
        initViews(view);
        initViewHolder();


        btn_save_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mContext);
                searchBarMaterialMedicine.clearFocus();
                if (Utils.isConnectingToInternet(mContext)) {
                    fl_progress_bar.setVisibility(View.VISIBLE);
                    AddTemplateJSON addTemplateJSON = new AddTemplateJSON();
                    addTemplateJSON.setTemplateName(template_name__);
                    addTemplateJSON.setCreatedBy(manager.getPreferences(mContext, "doctor_id"));
                    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> medicinesItemArrayListO = new ArrayList<>();
                    for (MedicinesItem mi: addMedicinesArrayList   ) {
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


                    addTemplateJSON.setMedicines(medicinesItemArrayListO);

                    Gson gson = new Gson();
                    String json = gson.toJson(addTemplateJSON);

                    apiViewHolder.AddTemplate(json)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseAddTemplate);

                   /* GeneratePres myFragment = new GeneratePres();
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();*/

                } else
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        addMedicinesArrayList = new ArrayList<>();
        textView3_5.setVisibility(View.GONE);
        ll_35.setVisibility(View.GONE);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mContext);
                searchBarMaterialMedicine.clearFocus();
                if (Utils.isConnectingToInternet(mContext)) {

                    if (searchBarMaterialMedicine.getText().toString().trim().equalsIgnoreCase("")) {
                        searchBarMaterialMedicine.setFocusable(true);
                        Toast.makeText(mContext, "Please enter medicine name", Toast.LENGTH_SHORT).show();

                    } else {

                        if(!("").equalsIgnoreCase(searchBarMaterialMedicine.getText().toString().trim())) {
                            if(btn_add.getText().toString().trim().equalsIgnoreCase("Save and Add next"))
                            {
                                btn_save_template.setVisibility(View.VISIBLE);
                                    MedicinesItem medicinesItem = new MedicinesItem();
                                    medicinesItem.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
                                    medicinesItem.setFrequency(frequency.trim());
                                    medicinesItem.setNoOfDays(et_no_of_days.getText().toString().trim());
                                    medicinesItem.setRoute(route.trim());
                                    medicinesItem.setQty(medicine_qty.getText().toString());
                                    medicinesItem.setInstruction(instructions_spinner.getSelectedItem().toString());
                                    medicinesItem.setAdditionaComment(et_additional_comments.getText().toString().trim());

                                    addMedicinesArrayList.add(medicinesItem);
//                                                Collections.sort(addMedicinesArrayList, new Comparator<MedicinesItem>() {
//                                                    @Override
//                                                    public int compare(MedicinesItem lhs, MedicinesItem rhs) {
//                                                        return lhs.getMedicineName().compareTo(rhs.getMedicineName()); }
//                                                });

                                    addMedicineAdapter = new AddMedicineAdapter(mContext,addMedicinesArrayList,
                                            frequency_list, frequency2_list, et_no_of_days,
                                            medicine_qty, route_list, instructions_list, frequency_spinner, frequency2_spinner,
                                            route_spinner, instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine,mRecyclerViewMedicines, btnChooseFromTemplate, btn_save_template,"template",fl_progress_bar);
                                    mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);
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
                                ArrayList<MedicinesItem> newMedicineAL = addMedicinesArrayList;
                                addMedicinesArrayList = new ArrayList<>();
                                btn_save_template.setVisibility(View.VISIBLE);
                                for(int i=0;i<newMedicineAL.size();i++)
                                {
                                    if(poss__ ==i )
                                    {
                                        MedicinesItem medicinesItem = new MedicinesItem();
                                        medicinesItem.setMedicineName(searchBarMaterialMedicine.getText().toString().trim());
                                        medicinesItem.setFrequency(frequency.trim());
                                        medicinesItem.setNoOfDays(et_no_of_days.getText().toString().trim()+" Days");
                                        medicinesItem.setRoute(route.trim());
                                        medicinesItem.setQty(medicine_qty.getText().toString());
                                        medicinesItem.setInstruction(instructions_spinner.getSelectedItem().toString());
                                        medicinesItem.setAdditionaComment(et_additional_comments.getText().toString().trim());

                                        addMedicinesArrayList.add(medicinesItem);
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
                                addMedicineAdapter = new AddMedicineAdapter(mContext, addMedicinesArrayList,
                                         frequency_list,frequency2_list,et_no_of_days,
                                        medicine_qty, route_list,instructions_list, frequency_spinner,frequency2_spinner,
                                        route_spinner,instructions_spinner, et_additional_comments, btn_add, textView3_5, ll_35, searchBarMaterialMedicine, mRecyclerViewMedicines, btnChooseFromTemplate, btn_save_template,"template", fl_progress_bar);
                                mRecyclerViewAddedMedicines.setAdapter(addMedicineAdapter);

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
                            et_no_of_days.setText("");
                            et_additional_comments.setText("");
                            frequency_spinner.setSelection(0);
                            frequency2_spinner.setSelection(0);
                            route_spinner.setSelection(0);
                            medicine_qty.setText("");
                            instructions_spinner.setSelection(0);


                        }else {
                            Toast.makeText(mContext, "Please enter medicine name", Toast.LENGTH_SHORT).show();
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

                Log.e(TAG, "responseAddTemplate: >> " + response.getMessage());
                if (response.getMessage() == null) {

                    fl_progress_bar.setVisibility(View.GONE);

                } else if (response.getMessage().equals("Template Added")) {
                    fl_progress_bar.setVisibility(View.GONE);

                    Toast.makeText(mContext, "Template Added", Toast.LENGTH_SHORT).show();
                    MyTemplates myTemplates = new MyTemplates();
                    myTemplates.initViewHolder();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                }
                else
                {
                    fl_progress_bar.setVisibility(View.GONE);

                }
            }
            else
            {
                fl_progress_bar.setVisibility(View.GONE);

            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_bar.setVisibility(View.GONE);

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


    private void initSpinner(View view) {
        hideKeyboard(mContext);
        frequency2_spinner = view.findViewById(R.id.frequency2_spinner);
        frequency2_list = new ArrayList<>();
        frequency2_list.add("None");
        frequency2_list.add("Once A Day");
        frequency2_list.add("Twice A Day");
        frequency2_list.add("Thrice A Day");
        frequency2_list.add("Four Times A Day");
        frequency2_list.add("Immediately");
        frequency2_list.add("When Required");
        frequency2_list.add("Before Sleep");

        frequency2_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                frequency2_list);
        frequency2_adp.setDropDownViewResource(R.layout.simple_spinner);
        frequency2_spinner.setAdapter(frequency2_adp);


        frequency_spinner = view.findViewById(R.id.frequency_spinner);
        frequency_list = new ArrayList<>();
        frequency_list.add("None");
        frequency_list.add("OD (Once A Day)");
        frequency_list.add("BD (Twice A Day)");
        frequency_list.add("TDS (Thrice A Day)");
        frequency_list.add("QID (Four Times A Day)");
        frequency_list.add("Stat (Immediately)");
        frequency_list.add("SOS (When Required)");
        frequency_list.add("HS (Before Sleep)");

        frequency_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                frequency_list);
        frequency_adp.setDropDownViewResource(R.layout.simple_spinner);
        frequency_spinner.setAdapter(frequency_adp);
        if(frequency_spinner != null) {
            frequency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        frequency = frequency_list.get(position).toString();
                        frequency2_spinner.setSelection(position);

                        //searchBarMaterialMedicine.findFocus().clearFocus();

                    } else {
                        frequency = "";
                    }
                    hideKeyboard(mContext);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }



        /*frequency2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                {
                    frequency2 = frequency2_list.get(position).toString();
                    hideKeyboard();
                    searchBarMaterialMedicine.clearFocus();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        route_spinner = view.findViewById(R.id.route_spinner);
        route_list = new ArrayList<>();
        route_list.add("None");
        route_list.add("Oral");
        route_list.add("Topical");
        route_list.add("Nasal");
        route_list.add("Drops");
        route_list.add("Syrup");
        route_list.add("Ointment");
        route_list.add("Injectable");
        route_list.add("Sub-Lingual");
        route_list.add("Dermal");
        route_list.add("Mucosal");
        route_list.add("Rectal");
        route_list.add("Intradermal");
        route_list.add("Subcutaneous");
        route_list.add("Eye Drops");
        route_list.add("Ear Drops");
        route_list.add("Toothpaste");

        route_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                route_list);
        route_adp.setDropDownViewResource(R.layout.simple_spinner);
        route_spinner.setAdapter(route_adp);
        if(route_spinner != null) {
            route_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        route = route_list.get(position).toString();

                    } else {
                        route = "";
                    }
                    hideKeyboard(mContext);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        instructions_spinner = view.findViewById(R.id.instructions_spinner);
        instructions_list = new ArrayList<>();
        instructions_list.add("None");
        instructions_list.add("After Meal");
        instructions_list.add("Before Meal");
        instructions_list.add("Empty Stomach");
        instructions_list.add("Anytime Of The Day");
        instructions_list.add("Not Specific");
        instructions_list.add("After Breakfast");
        instructions_list.add("After Lunch");
        instructions_list.add("Before Breakfast");
        instructions_list.add("Before Lunch");
        instructions_list.add("Before Dinner");
        instructions_list.add("After Dinner");
        instructions_list.add("Early Morning Empty Stomach");

        instructions_adp = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_visiting_hrs,
                instructions_list);
        instructions_adp.setDropDownViewResource(R.layout.simple_spinner);
        instructions_spinner.setAdapter(instructions_adp);
        if(instructions_spinner != null) {
            instructions_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        instructions = instructions_list.get(position).toString();

                    } else {
                        instructions = "";
                    }
                    hideKeyboard(mContext);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }



    private void initViews(View view) {
      //  et_name_of_medicine = view.findViewById(R.id.et_name_of_medicine);
        et_no_of_days = view.findViewById(R.id.et_days);
        et_frequency = view.findViewById(R.id.et_frequency);
        et_frequency2 = view.findViewById(R.id.et_frequency1);
        et_route = view.findViewById(R.id.et_Route);
        medicine_qty=view.findViewById(R.id.medicine_qty);
        et_additional_comments = view.findViewById(R.id.et_comments);
        et_instructions = view.findViewById(R.id.et_instruction);
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
                ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> favorites1 = searchtext(String.valueOf(newText));
                if(searchFlag) {
                    if (favorites1.size() == 0) {
                        mRecyclerViewMedicines.setVisibility(View.GONE);
                    } else {
                        mRecyclerViewMedicines.setVisibility(View.VISIBLE);

                        mAdapterSearchMedicine = new SearchAdapter(favorites1,
                                frequency_list,frequency2_list,et_no_of_days,route_list,instructions_list,
                                frequency_spinner,frequency2_spinner,route_spinner,instructions_spinner,et_additional_comments,
                                mRecyclerViewMedicines,searchBarMaterialMedicine);
                        mRecyclerViewMedicines.setAdapter(mAdapterSearchMedicine);
                        mAdapterSearchMedicine.notifyDataSetChanged();

                    }
                }else {
                    mRecyclerViewMedicines.setVisibility(View.GONE);
                }

//
//                if (newText.length() > 1) {
//                    mRecyclerViewMedicines.setVisibility(View.VISIBLE);
//                    mAdapterSearchMedicine.getFilter().filter(newText);
//                } else  if (newText.length() == 0)
//                    mRecyclerViewMedicines.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        initRecyclerViews();
    }

    private  ArrayList<com.likesby.bludoc.ModelLayer.Entities.MedicinesItem> searchtext(String query) {
        query = query.toLowerCase();
        SearchMedicine = new ArrayList<>();
        searchFlag =false;
        if(query.length() >= 3) {
            query= query.trim().replaceAll("\\s+", "%%") + "%%";
            //  query = query.replace(" ", "%%");
            query= query.trim().replaceAll("'", "%%");
            SearchMedicine = myDB.getSearchData(query);
            searchFlag = true;
        }else {
            searchFlag = false;
            SearchMedicine = new ArrayList<>();
        }
        return SearchMedicine;
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


        mAdapterSearchMedicine = new SearchAdapter(MedicAll,
                 frequency_list,frequency2_list,et_no_of_days,route_list,instructions_list,
                frequency_spinner,frequency2_spinner,route_spinner,instructions_spinner,et_additional_comments,
                mRecyclerViewMedicines,searchBarMaterialMedicine);
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