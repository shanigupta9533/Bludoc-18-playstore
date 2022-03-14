package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.Fragment.MyTemplates;
import com.likesby.bludoc.Fragment.TemplateDetail;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ProfileDetails;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NewEntities.TemplatesItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.likesby.bludoc.Fragment.CreatePrescription.backCheckerFlag;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> implements Filterable {

    private ArrayList<TemplatesItem> mArrayList = new ArrayList<>();
    private ArrayList<TemplatesItem> mFilteredList = new ArrayList<>();
    private Context ctx ;
    LinearLayout ll_patients_view, ll_prescription_view;
    Button btn_create_patient;
    String patient_id,definer;
    Context mContext;
    ApiViewHolder apiViewHolder;
    private static final String TAG = "State_picket";
    CompositeDisposable mBag;
    ViewHolder mViewHolder;
    SessionManager manager;
    FrameLayout fl_progress_bar;

    public TemplateAdapter(ArrayList<TemplatesItem> arrayList, String patient_id, String definer, Context context, CompositeDisposable mBag, ApiViewHolder apiViewHolder, FrameLayout fl_progress_bar)
    {
        mContext = context;
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.patient_id = patient_id;
        this.definer = definer;
        this.mBag = mBag;
        this.apiViewHolder = apiViewHolder;
        this.fl_progress_bar = fl_progress_bar;
    }

    @NonNull
    @Override
    public TemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.template_adapter,
                viewGroup, false);
        return new TemplateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TemplateAdapter.ViewHolder viewHolder, int i) {

        viewHolder.TEMPLATE_NAME.setText(mFilteredList.get(i).getTemplateName());

        if(definer.equalsIgnoreCase("pres")){
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.edit.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.GONE);

        }else {
            viewHolder.add.setVisibility(View.GONE);
            viewHolder.edit.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<TemplatesItem> filteredList = new ArrayList<>();

                    for (TemplatesItem categories : mArrayList) {

                        if (categories.getTemplateName().toLowerCase().contains(charString))
                        {

                            filteredList.add(categories);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<TemplatesItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView TEMPLATE_NAME;
        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        ImageView imageView;
        Button add,edit,delete;;
        public ViewHolder(View view)
        {
            super(view);
            ctx = view.getContext();

            TEMPLATE_NAME        = view.findViewById(R.id.tv_template_name);
            edit = view.findViewById(R.id.template_edit);
            delete = view.findViewById(R.id.template_delete);
            add = view.findViewById(R.id.template_add);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                      ((FragmentActivity) ctx).getSupportFragmentManager().popBackStack();
                    backCheckerFlag = true;
//                    CreatePrescription.medicineAddFLAG = true;
//                    CreatePrescription.NEWaddMedicinesArrayList.addAll(mFilteredList.get(getAdapterPosition()).getMedicines());

                    CreatePrescription createPrescription= (CreatePrescription) ((FragmentActivity) ctx).getSupportFragmentManager().findFragmentByTag("prescription");
                    if(createPrescription!=null){
                        createPrescription.setDataOnMedicines(mFilteredList.get(getAdapterPosition()).getMedicines());
                    }

                    /*CreatePrescription myFragment = new CreatePrescription();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("new_list",mFilteredList.get(getAdapterPosition()).getMedicines());
                    bundle.putString("temp_name",mFilteredList.get(getAdapterPosition()).getTemplateName());
                    bundle.putString("patient_id",patient_id);
                    bundle.putString("definer","temp");
                    bundle.putString("temp_id",mFilteredList.get(getAdapterPosition()).getTemplateId());
                    myFragment.setArguments(bundle);
                    ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer,
                            myFragment, "first").addToBackStack(null).commit();*/
                    //((FragmentActivity)ctx).getSupportFragmentManager().popBackStackImmediate();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreatePrescription myFragment = new CreatePrescription();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("new_list",mFilteredList.get(getAdapterPosition()).getMedicines());
                    bundle.putString("patient_id",patient_id);
                    bundle.putString("definer","edit");
                    CreatePrescription.is_on_lab_test = false;
                    CreatePrescription.is_on_medicines = true;
                    CreatePrescription.is_on_case_history = false;
                    CreatePrescription.certificate_selection = false;
                    bundle.putString("definer_type","medicines");
                    bundle.putString("temp_id",mFilteredList.get(getAdapterPosition()).getTemplateId());
                    bundle.putString("temp_name",mFilteredList.get(getAdapterPosition()).getTemplateName());
                    myFragment.setArguments(bundle);
                    ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer,
                            myFragment, "first").addToBackStack(null).commit();
                    //((FragmentActivity)ctx).getSupportFragmentManager().popBackStackImmediate();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
                    builder.setMessage("Would you like to delete this template?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fl_progress_bar.setVisibility(View.VISIBLE);
                                    //manager.getPreferences(Registration_.this, "service_provider");
                                    apiViewHolder.getDeleteTemplate(mFilteredList.get(getAdapterPosition()).getTemplateId())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(responseProfile);



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
        }
    }
    SingleObserver<ResponseSuccess> responseProfile = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {
            if (response != null) {
                fl_progress_bar.setVisibility(View.GONE);

                Log.e(TAG, "profileDetails: >> " + response.getMessage());
                if (response.getMessage() == null) {
                    fl_progress_bar.setVisibility(View.GONE);
                } else if (response.getMessage().equals("Template Deleted")) {
                    fl_progress_bar.setVisibility(View.GONE);
                    MyTemplates myTemplates = new MyTemplates();
                    myTemplates.initViewHolder();
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

            Log.e(TAG, "onError: profileDetails >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

}
//    Context context;
//
//    // static Typeface face;
//    static String YesFROMTAG = "NO";
//    static String NameofTAG;
//
//
//    public TemplateAdapter(Context context) {
//
//        this.context = context;
//    }
//
//    // Create new views
//    @Override
//    public TemplateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // create a new view
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_adapter, parent, false);
//        return new TemplateAdapter.ViewHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(TemplateAdapter.ViewHolder viewHolder, final int position) {
//
//        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TemplateDetail templateDetail = new TemplateDetail();
//                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.homePageContainer, templateDetail).addToBackStack("Detail").commit();
//
//            }
//        });
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return 6;
//    }
//
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView txtstates;
//        RelativeLayout card_view;
//        public ViewHolder(View itemLayoutView) {
//            super(itemLayoutView);
//            card_view = itemLayoutView.findViewById(R.id.card_view);
//        }
//    }
//}
