package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.Fragment.MeetingDialogFragment;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityCalenderViewBinding;
import com.likesby.bludoc.utils.Utils;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import ru.cleverpumpkin.calendar.CalendarDate;
import ru.cleverpumpkin.calendar.CalendarView;

public class CalenderViewActivity extends AppCompatActivity {

    private ActivityCalenderViewBinding activity;
    private ArrayList<AppointmentListModel> arrayList = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private ArrayList<AppointmentListModel> showModel = new ArrayList<>();
    private RelativeLayout no_data_found_id;
    private MeetingDialogFragment meetingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_calender_view);

        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activity.recyclerView.setLayoutManager(gridLayoutManagerMorning);
        NewAppointmentAdapter appointmentAdapter = new NewAppointmentAdapter(arrayList, this);
        activity.recyclerView.setAdapter(appointmentAdapter);

        Calendar calendar = Calendar.getInstance();

        no_data_found_id = findViewById(R.id.no_data_found);

        findViewById(R.id.refresh_button_no_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllGetAppointment();

            }
        });

        // Initial date
        CalendarDate initialDate = new CalendarDate(calendar.getTime());

        // Minimum available date
        calendar.set(2016, Calendar.MAY, 15);
        CalendarDate minDate = new CalendarDate(calendar.getTime());

        DateTime today = new DateTime();
        DateTime dateTime = today.plusYears(5);

        // Maximum available date
        calendar.set(dateTime.getYear(), Calendar.JULY, 15);
        CalendarDate maxDate = new CalendarDate(calendar.getTime());

        List<CalendarDate> calendarDateList = new ArrayList<>();

        // The first day of week
        int firstDayOfWeek = java.util.Calendar.MONDAY;

        // Set up calendar with all available parameters

        activity.simpleCalendarView.setupCalendar(
                initialDate,
                minDate,
                maxDate,
                CalendarView.SelectionMode.NONE,
                calendarDateList,
                firstDayOfWeek,
                true
        );

        // Set date click callback
        activity.simpleCalendarView.setOnDateClickListener(date -> {

            meetingDialogFragment = new MeetingDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("date",date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth());
            meetingDialogFragment.setArguments(bundle);
            meetingDialogFragment.show(getSupportFragmentManager(), "meetingDialogFragment");

            meetingDialogFragment.setOnClickListener(new MeetingDialogFragment.onClickListener() {
                @Override
                public void onClick() {

                    AllGetAppointment();

                }
            });

            return null;

        });

        AllGetAppointment();

    }

    private CalendarView.DateIndicator returnDateSelector(int year, int monthOfYear, int dayOfMonth) {

        CalendarView.DateIndicator dateIndicator = new CalendarView.DateIndicator() {
            @NonNull
            @Override
            public CalendarDate getDate() {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                return new CalendarDate(calendar.getTime());
            }

            @Override
            public int getColor() {
                return Color.parseColor("#ff00ff");
            }
        };

        return dateIndicator;

    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            no_data_found_id.setVisibility(View.GONE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(CalenderViewActivity.this, "doctor_id"),"");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        activity.progressBarInclude.setVisibility(View.GONE);
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();

                        if (appointmentListModels.size() > 0) {

                            List<CalendarView.DateIndicator> dateIndicatorList = new ArrayList<>();
                            activity.simpleCalendarView.setDatesIndicators(dateIndicatorList);

                            for (AppointmentListModel appointmentListModel : appointmentListModels) {

                                DateTime dateTime;

                                try {
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                                }

                                CalendarView.DateIndicator dateIndicators = returnDateSelector(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());

                                dateIndicatorList.add(dateIndicators);

                            }

                            activity.simpleCalendarView.setDatesIndicators(dateIndicatorList);

                        } else {

                            List<CalendarView.DateIndicator> dateIndicatorList = new ArrayList<>();
                            activity.simpleCalendarView.setDatesIndicators(dateIndicatorList);

                        }

                    } else {

                        try {
                            Toast.makeText(CalenderViewActivity.this, ""+response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(CalenderViewActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(meetingDialogFragment!=null)
            meetingDialogFragment.dismiss();

        if (manager.getPreferences(this, "Appointment").equals("true")) {

            AllGetAppointment();
            manager.setPreferences(this, "Appointment", "false");

        }

    }
}