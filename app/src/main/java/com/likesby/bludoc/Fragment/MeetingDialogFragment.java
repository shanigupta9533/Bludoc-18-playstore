package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.NewAppointmentActivity;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.TodayAppointmentActivity;
import com.likesby.bludoc.databinding.FragmentMeetingDialogBinding;
import com.likesby.bludoc.databinding.FragmentPopUpSubscriptionBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MeetingDialogFragment extends DialogFragment {

    private FragmentMeetingDialogBinding binding;
    private ArrayList<AppointmentListModel> showModel = new ArrayList<>();
    private String date;
    private FragmentActivity fragmentActivity;
    private NewAppointmentAdapter appointmentAdapter;
    private SessionManager manager = new SessionManager();
    private RelativeLayout no_data_found_id;
    private MeetingDialogFragment.onClickListener onClickListener;
    private String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
    };

    public MeetingDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    public interface onClickListener{

        void onClick();

    }

    public void setOnClickListener(onClickListener onclickListener){

        this.onClickListener = onclickListener;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for fragmentActivity fragment

        binding = FragmentMeetingDialogBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();

        no_data_found_id = binding.getRoot().findViewById(R.id.no_data_found);

        if(arguments!=null) {
            date = arguments.getString("date", "");
        }

        DateTime dateTime = new DateTime(date + "T00:00:00");

        String dateDefined = dateTime.getDayOfMonth() + " " + monthNames[dateTime.getMonthOfYear() - 1] + " " + dateTime.getYear();

        binding.appName.setText("Appointment of "+dateDefined);
        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(gridLayoutManagerMorning);
        appointmentAdapter = new NewAppointmentAdapter(showModel,getContext());
        binding.recyclerview.setAdapter(appointmentAdapter);

        binding.getRoot().findViewById(R.id.refresh_button_no_data).setOnClickListener(v -> AllGetAppointment());

        appointmentAdapter.setOnClickListener(new NewAppointmentAdapter.onClickListener() {
            @Override
            public void onClick(AppointmentListModel appointmentListModel, int position) {

                deleteAppointment(appointmentListModel,position);

            }
        });


        binding.searchPatientView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                appointmentAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_min);
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dismiss();

            }
        });

        AllGetAppointment();

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    public void deleteAppointment(AppointmentListModel appointmentListModel, int position) {

        if (Utils.isConnectingToInternet(fragmentActivity)) {

            binding.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.deleteAppointment(appointmentListModel.getApp_list_id());

            call.enqueue(new Callback<ResultOfApi>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBarInclude.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Appointment Removed")) {

                        binding.progressBarInclude.setVisibility(View.GONE);
                        showModel.remove(position);
                        appointmentAdapter.notifyDataSetChanged();

                        if(onClickListener!=null){

                            onClickListener.onClick();

                        }

                        if (showModel.size() == 0)
                            no_data_found_id.setVisibility(View.VISIBLE);
                        else
                            no_data_found_id.setVisibility(View.GONE);


                        Toast.makeText(fragmentActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    binding.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(fragmentActivity)) {

            binding.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(fragmentActivity, "doctor_id"),"","");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        binding.progressBarInclude.setVisibility(View.GONE);
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();
                        if (appointmentListModels.size() > 0) {

                            for (AppointmentListModel appointmentListModel : appointmentListModels) { // compare SpecificDate date

                                DateTime dateTime;

                                try {
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                                }

                                DateTime SpecificDate;

                                try {
                                    SpecificDate = new DateTime(date+"T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    SpecificDate = new DateTime(date+"T"+appointmentListModel.getApp_time());
                                }


                                if (dateTime.getYear() == SpecificDate.getYear() &&
                                        dateTime.getMonthOfYear() == SpecificDate.getMonthOfYear() &&
                                        dateTime.getDayOfMonth() == SpecificDate.getDayOfMonth()) {

                                    showModel.add(appointmentListModel);

                                }

                            }

                            if (showModel.size() > 0) {

                                appointmentAdapter.notifyItemRangeChanged(0, showModel.size());
                                no_data_found_id.setVisibility(View.GONE);

                            } else {

                                no_data_found_id.setVisibility(View.VISIBLE);

                            }


                        } else {

                            no_data_found_id.setVisibility(View.VISIBLE);

                        }

                    } else {

                        try {
                            Toast.makeText(fragmentActivity, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                    binding.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}