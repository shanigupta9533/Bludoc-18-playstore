package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.TimeSlotAdapter;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.TimeSlotModel;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.databinding.ActivityCreateAppointmentBinding;
import com.likesby.bludoc.utils.GridSpacingItemDecoration;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

@SuppressLint("ClickableViewAccessibility")
public class CreateAppointmentActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ActivityCreateAppointmentBinding activity;
    private ArrayList<TimeSlotModel> morningList = new ArrayList<>();
    private ArrayList<TimeSlotModel> eveningList = new ArrayList<>();
    private ArrayList<TimeSlotModel> afternoonList = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private PatientsItem patientItem;
    private boolean isUpdate;
    CompositeDisposable mBag = new CompositeDisposable();
    private AppointmentListModel appointmentModel;
    private ApiViewHolder apiViewHolder;
    private boolean isToday;
    private boolean isConstant;
    String age_type = "yr";
    private String ymdDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_create_appointment);

        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        Intent intent = getIntent();

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        activity.cancelAction.setVisibility(View.GONE);

        if (intent != null) {

            isUpdate = intent.getBooleanExtra("isUpdate", false);

            if (isUpdate) {

                appointmentModel = intent.getParcelableExtra("appointment");

                patientItem = new PatientsItem();
                patientItem.setPName(appointmentModel.getP_name());
                patientItem.setAge(appointmentModel.getP_age());
                patientItem.setGender(appointmentModel.getP_gender());
                patientItem.setPMobile(appointmentModel.getP_mobile());
                patientItem.setPEmail(appointmentModel.getP_email());

                ymdDate = appointmentModel.getApp_date();

                DateTime dateTime = new DateTime(ymdDate+"T00:00:00");

                activity.dateSlot.setText(dateTime.getDayOfMonth()+"-"+dateTime.getMonthOfYear()+"-"+dateTime.getYear());
                activity.selectTime.setText(appointmentModel.getApp_time());
                activity.saveAppointmentTextview.setText("Update Appointment");
                activity.titleOnToolbar.setText("Edit Appointment");

                activity.purpose.setText(appointmentModel.getPurpose());

                activity.cancel.setVisibility(View.GONE);
                activity.parentOfSelection.setVisibility(View.GONE);

            } else {

                patientItem = intent.getParcelableExtra("patientItem");

            }

            if (patientItem != null) {
                setDataOnEdittext();
                activity.parentOfSelection.setVisibility(View.GONE);
            }

        }

        activity.patientDetailsChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateAppointmentActivity.this, SelectAPatientActivity.class);
                startActivityForResult(intent, 200);

            }
        });

        activity.cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.dateOfBirth.setText("");

            }
        });

        activity.purpose.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (activity.purpose.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            }
        });

        activity.dateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    activity.cancelAction.setVisibility(View.VISIBLE);
                    activity.rbMonth.setEnabled(false);
                    activity.rbYear.setEnabled(false);
                    activity.etAge.setEnabled(false);

                } else {

                    activity.cancelAction.setVisibility(View.GONE);
                    activity.rbMonth.setEnabled(true);
                    activity.etAge.setEnabled(true);
                    activity.rbYear.setEnabled(true);
                    activity.etAge.setText("");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        DateTime dateTime = new DateTime();

                        if (dateTime.getYear() - year > 1) {

                            isConstant = true;
                            String age = getAge(year, monthOfYear, dayOfMonth);

                            if (Integer.parseInt(age) < 0) {
                                age = "0";
                            }

                            activity.etAge.setText("");
                            activity.etAge.setSelection(activity.etAge.getText().toString().length());
                            activity.rbYear.setChecked(true);

                        }
                        if (dateTime.getYear() - year == 1) {
                            activity.etAge.setText("");
                            activity.etAge.setSelection(activity.etAge.getText().toString().length());
                            activity.rbYear.setChecked(true);

                        } else {

                            isConstant = true;

                            activity.etAge.setSelection(activity.etAge.getText().toString().length());
                            activity.rbMonth.setChecked(true);

                        }

                        String myFormat = "dd-MM-yyyy"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        activity.dateOfBirth.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAppointmentActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).getTime());

                datePickerDialog.show();

            }
        });

        activity.rbYear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.rbMonth.setChecked(false);
                    age_type = "yr";

//                    if (TextUtils.isEmpty(et_age.getText().toString())) {
//
//                        Toast.makeText(mContext, "Enter Your Age...", Toast.LENGTH_SHORT).show();
//
//                    } else if (!isConstant) {
//
//                        String age = et_age.getText().toString();
//                        et_age.setText(String.valueOf(Integer.parseInt(age) / 12));
//                        et_age.setSelection(et_age.getText().toString().length());
//                    }
//
//                    isConstant = false;

                }
            }
        });

        activity.rbMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.rbYear.setChecked(false);
                    age_type = "months";

//                    if (TextUtils.isEmpty(et_age.getText().toString())) {
//
//                        Toast.makeText(mContext, "Enter Your Age...", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        String age = et_age.getText().toString();
//                        et_age.setText(String.valueOf(Integer.parseInt(age) * 12));
//                        et_age.setSelection(et_age.getText().toString().length());
//                        isConstant = false;
//                    }

                }
            }
        });

        activity.patientDetailsChoose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0 && !isUpdate) {
                    activity.cancel.setVisibility(View.VISIBLE);
                } else if (s.length() == 0 && !isUpdate) {
                    activity.cancel.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        activity.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.patientDetailsChoose.setText("");
                patientItem = null;

                activity.patientName.setText("");
                activity.patientName.setEnabled(true);

                activity.patientEmail.setText("");
                activity.patientEmail.setEnabled(true);

                activity.patientWhatsNumberEdit.setText("");
                activity.patientWhatsNumberEdit.setEnabled(true);

                activity.patientGender.setSelection(0);
                activity.patientGender.setEnabled(true);

                activity.parentOfSelection.setVisibility(View.VISIBLE);

            }
        });

        activity.patientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    patientItem = null;
                    activity.patientDetailsChoose.setEnabled(false);
                    activity.patientDetailsChoose.setText("");
                } else {
                    activity.patientDetailsChoose.setEnabled(true);
                    activity.patientDetailsChoose.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        activity.dateSlot.setOnClickListener(v -> {

            Calendar myCalendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAppointmentActivity.this, R.style.DialogTheme, CreateAppointmentActivity.this,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            datePickerDialog.show();

        });

        activity.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ymdDate)) {
                    Toast.makeText(CreateAppointmentActivity.this, "Select Date. ", Toast.LENGTH_SHORT).show();
                    return;
                }

                DateTime dateTime = new DateTime();

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        CreateAppointmentActivity.this,
                        dateTime.getHourOfDay(),
                        dateTime.getMinuteOfDay() + 4,
                        false);

                if (isToday)
                    timePickerDialog.setMinTime(dateTime.getHourOfDay(), dateTime.getMinuteOfDay() + 1, 0);

                timePickerDialog.show(getSupportFragmentManager(), "timePickerDialog");

            }
        });

        activity.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (patientItem == null && !isUpdate) {
                    createPatientFirst();
                    return;
                }

                if (TextUtils.isEmpty(ymdDate)) {
                    Toast.makeText(CreateAppointmentActivity.this, "Choose A Date Slot", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(activity.selectTime.getText().toString())) {
                    Toast.makeText(CreateAppointmentActivity.this, "Choose A Time Slot", Toast.LENGTH_SHORT).show();
                } else {

                    DateTime dateTime = new DateTime(ymdDate+"T"+Utils.convertTo24Hour(activity.selectTime.getText().toString())+":00");
                    DateTime today = new DateTime();

                    if (today.getMillis() > dateTime.getMillis()) {

                        Toast.makeText(CreateAppointmentActivity.this, "Invalid time*", Toast.LENGTH_SHORT).show();
                        activity.selectTime.setError("Invalid time*");
                        activity.selectTime.requestFocus();

                        return;
                    }

                    if (isUpdate && TextUtils.isEmpty(patientItem.getPatientId())) {
                        createPatientFromEdit();
                    } else if(isUpdate){
                        editAppointmentDetails(patientItem);
                    } else {
                        UploadAppointmentDetails(patientItem, true);
                    }
                }

            }
        });

        // morning data set
        morningDataOnList();

        GridLayoutManager gridLayoutManagerMorning = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        int spanCount = 4; // 3 columns
        int spacing = 5; // 5px
        boolean includeEdge = false;
        activity.morning.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        activity.morning.setLayoutManager(gridLayoutManagerMorning);
        TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(morningList, this);
        activity.morning.setAdapter(timeSlotAdapter);

        // morning data set
        EveningDataOnList();

        GridLayoutManager gridLayoutManagerAfternoon = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        activity.evening.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        activity.evening.setLayoutManager(gridLayoutManagerAfternoon);
        TimeSlotAdapter timeSlotAdapterAfternoon = new TimeSlotAdapter(eveningList, this);
        activity.evening.setAdapter(timeSlotAdapterAfternoon);

        // morning data set
        AfternoonDataOnList();

        GridLayoutManager gridLayoutManagerEvening = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        activity.afternoon.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        activity.afternoon.setLayoutManager(gridLayoutManagerEvening);
        TimeSlotAdapter timeSlotAdapterEvening = new TimeSlotAdapter(afternoonList, this);
        activity.afternoon.setAdapter(timeSlotAdapterEvening);

    }

    private String getAge(int year, int month, int day) {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;

    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
    }

    private void createPatientFirst() {

        if (TextUtils.isEmpty(activity.patientName.getText().toString())) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Patient name", Toast.LENGTH_SHORT).show();
        } else if (activity.patientGender.getSelectedItemPosition() == 0) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Patient gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ymdDate)) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Date Slot", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(activity.selectTime.getText().toString())) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Time Slot", Toast.LENGTH_SHORT).show();
        } else {

            String strEnteredVal = activity.etAge.getText().toString().trim();
            if (!strEnteredVal.equals("")) {
                float num = Float.parseFloat(strEnteredVal);
                if (age_type.equals("yr") && num < 151) {

                    activity.progressBarInclude.setVisibility(View.VISIBLE);
                    apiViewHolder.PatientRegisterWithPatientItem(
                            activity.patientName.getText().toString().trim(),
                            activity.etAge.getText().toString().trim() + " " + age_type,
                            activity.patientGender.getSelectedItem().toString().trim()
                            , manager.getPreferences(CreateAppointmentActivity.this, "doctor_id"),
                            activity.patientWhatsNumberEdit.getText().toString(),
                            activity.patientEmail.getText().toString()
                            , ""
                            , ""
                            , convertFormat(activity.dateOfBirth.getText().toString()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responsePatientRegister);

                } else if (age_type.equals("month") || age_type.equals("months")) {

                    if (age_type.equals("months"))

                        if (activity.etAge.getText().toString().trim().equals("1"))
                            age_type = "month";

                    activity.progressBarInclude.setVisibility(View.VISIBLE);

                    apiViewHolder.PatientRegisterWithPatientItem(
                            activity.patientName.getText().toString().trim(),
                            activity.etAge.getText().toString().trim() + " " + age_type,
                            activity.patientGender.getSelectedItem().toString().trim(),
                            manager.getPreferences(CreateAppointmentActivity.this, "doctor_id"),
                            activity.patientWhatsNumberEdit.getText().toString(),
                            activity.patientEmail.getText().toString(),
                            "",
                            "",
                            convertFormat(activity.dateOfBirth.getText().toString()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responsePatientRegister);

                } else {
                    activity.etAge.requestFocus();
                    activity.etAge.setError("Age not allowed");
                    Toast.makeText(this, "Age not allowed", Toast.LENGTH_SHORT).show();
                }

            } else {

                activity.progressBarInclude.setVisibility(View.VISIBLE);
                apiViewHolder.PatientRegisterWithPatientItem(
                        activity.patientName.getText().toString().trim(),
                        activity.etAge.getText().toString().trim() + " " + age_type,
                        activity.patientGender.getSelectedItem().toString().trim()
                        , manager.getPreferences(this, "doctor_id")
                        , activity.patientWhatsNumberEdit.getText().toString()
                        , activity.patientEmail.getText().toString().trim()
                        , ""
                        , ""
                        , convertFormat(activity.dateOfBirth.getText().toString()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responsePatientRegister);

            }

        }

    }

    private void createPatientFromEdit() {

        if (TextUtils.isEmpty(patientItem.getPName())) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Patient name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(patientItem.getGender())) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Patient gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ymdDate)) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Date Slot", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(activity.selectTime.getText().toString())) {
            Toast.makeText(CreateAppointmentActivity.this, "Choose A Time Slot", Toast.LENGTH_SHORT).show();
        } else {

            String dob;

            if(TextUtils.isEmpty(patientItem.getpDob()))
                dob = "";
            else {
                dob = convertFormat(patientItem.getpDob().trim());
            }

            activity.progressBarInclude.setVisibility(View.VISIBLE);
                apiViewHolder.PatientRegisterWithPatientItem(
                        patientItem.getPName(),
                        patientItem.getAge(),
                        patientItem.getGender()
                        , manager.getPreferences(this, "doctor_id")
                        , patientItem.getPMobile()
                        , patientItem.getPEmail()
                        , patientItem.getAddress()
                        , patientItem.getpBloodGrp()
                        , dob.trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responsePatientRegister);

        }

    }

    private String convertFormat(String date) {

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateInput = null;
        try {
            dateInput = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateInput == null)
            return "";

        return outputFormat.format(dateInput);

    }


    SingleObserver<PatientsItem> responsePatientRegister = new SingleObserver<PatientsItem>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(PatientsItem response) {

            if (response != null) {

                if (response.getMessage() == null) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                } else if (response.getMessage().equals("Patient Register")) {

                    if(!isUpdate) {
                        UploadAppointmentDetails(response, false);
                    } else {
                        editAppointmentDetails(response);
                    }
                }

            } else {
                activity.progressBarInclude.setVisibility(View.GONE);
                Toast.makeText(CreateAppointmentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            activity.progressBarInclude.setVisibility(View.GONE);
            Log.e("TAG", "onError: responsePatientRegister >> " + e.toString());
            //intentCall();
            Toast.makeText(CreateAppointmentActivity.this, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private void editAppointmentDetails(PatientsItem patientItem) {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            String age = "";

            if (TextUtils.isEmpty(patientItem.getpDob())) // set age by date of birth

                if (TextUtils.isEmpty(patientItem.getAge())) // set age by object of patient item
                    age = "0 yr";
                else
                    age = patientItem.getAge();

            else {

                String[] split = patientItem.getpDob().split("-");
                age = decideMonthOrYear(split);

            }

            Call<ResultOfApi> call = request.editAppointment(
                    manager.getPreferences(CreateAppointmentActivity.this, "doctor_id"),
                    appointmentModel.getApp_list_id()
                    , patientItem.getPatientId()
                    , patientItem.getPName()
                    , patientItem.getPEmail()
                    , patientItem.getPMobile()
                    , age
                    , patientItem.getGender()
                    , ymdDate.toString()
                    , activity.selectTime.getText().toString()
                    , activity.purpose.getText().toString()
                    , "1"
            );

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfApi jsonResponse = response.body();
                        activity.progressBarInclude.setVisibility(View.GONE);
                        if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                                jsonResponse.getMessage().equalsIgnoreCase("New Appointment Added")) {

                            activity.progressBarInclude.setVisibility(View.GONE);

                            Toast.makeText(CreateAppointmentActivity.this, "Appointment updated successfully", Toast.LENGTH_SHORT).show();

                            activity.patientDetailsChoose.setText("");
                            activity.dateSlot.setText("");
                            activity.selectTime.setText("");

                            manager.setPreferences(CreateAppointmentActivity.this, "Appointment", "true");

                            onBackPressed();

                        } else {
                            Toast.makeText(CreateAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        try {
                            Toast.makeText(CreateAppointmentActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(CreateAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setDataOnEdittext() {

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(patientItem.getPName())) // set name
            stringBuilder.append("Name : ").append(patientItem.getPName());

        activity.patientDetailsChoose.setEnabled(false);

        if (TextUtils.isEmpty(patientItem.getpDob())) // set age by date of birth

            if (TextUtils.isEmpty(patientItem.getAge()) || patientItem.getAge().trim().equals("0 yr") ||
                patientItem.getAge().trim().equals("yr")
            ) // set age by object of patient item
                stringBuilder.append("\n").append("Age : ").append("___");
            else
                stringBuilder.append("\n").append("Age : ").append(patientItem.getAge());

        else {

            String[] split = patientItem.getpDob().split("-");
            String return_value = decideMonthOrYear(split);
            stringBuilder.append("\n").append("Age : ").append(return_value);

        }

        if (!TextUtils.isEmpty(patientItem.getGender())) // set gender
            stringBuilder.append("\n").append("Gender : ").append(patientItem.getGender());

        if (!TextUtils.isEmpty(patientItem.getPMobile()) && !patientItem.getPMobile().equalsIgnoreCase("0")) // set mobile number
            stringBuilder.append("\n").append("Mobile : ").append(patientItem.getPMobile());

        if (!TextUtils.isEmpty(patientItem.getPEmail())) // set email
            stringBuilder.append("\n").append("Email : ").append(patientItem.getPEmail());

        activity.patientDetailsChoose.setText(stringBuilder.toString());

    }

    public void UploadAppointmentDetails(PatientsItem patientItem, boolean fromChoosePatient) {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            String age;

            if (TextUtils.isEmpty(patientItem.getpDob())) { // set age by date of birth

                if (TextUtils.isEmpty(patientItem.getAge())) // set age by object of patient item
                    age = "0 yr";
                else
                    age = patientItem.getAge();

            } else {

                String[] split = patientItem.getpDob().split("-");
                String return_value = decideMonthOrYear(split);
                age = return_value;

            }

            Call<ResultOfApi> call = request.addAppointmentList(
                    manager.getPreferences(CreateAppointmentActivity.this, "doctor_id")
                    , patientItem.getPatientId()
                    , patientItem.getPName()
                    , patientItem.getPEmail()
                    , patientItem.getPMobile()
                    , age
                    , patientItem.getGender()
                    , ymdDate
                    , activity.selectTime.getText().toString()
                    , activity.purpose.getText().toString()
                    , "1"
            );

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();

                    activity.progressBarInclude.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("New Appointment Added")) {

                        activity.progressBarInclude.setVisibility(View.GONE);

                        Toast.makeText(CreateAppointmentActivity.this, " Added Successfully", Toast.LENGTH_SHORT).show();

                        activity.patientDetailsChoose.setText("");
                        activity.dateSlot.setText("");
                        activity.selectTime.setText("");

                        manager.setPreferences(CreateAppointmentActivity.this, "Appointment", "true");

                        startActivity(new Intent(CreateAppointmentActivity.this, NewAppointmentActivity.class));

                        onBackPressed();

                    } else {
                        Toast.makeText(CreateAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(CreateAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {

            if (resultCode == RESULT_OK) {

                if (data != null && data.getParcelableExtra("patientItem") != null) {

                    patientItem = data.getParcelableExtra("patientItem");

                    activity.patientName.setText("");
                    activity.patientName.setEnabled(false);

                    activity.dateSlot.setText("");
                    activity.selectTime.setText("");

                    activity.patientEmail.setText("");
                    activity.patientEmail.setEnabled(false);

                    activity.patientWhatsNumberEdit.setText("");
                    activity.patientWhatsNumberEdit.setEnabled(false);

                    activity.patientGender.setSelection(0);
                    activity.patientGender.setEnabled(false);

                    activity.parentOfSelection.setVisibility(View.GONE);

                    StringBuilder stringBuilder = new StringBuilder();

                    if (!TextUtils.isEmpty(patientItem.getPName())) // set name
                        stringBuilder.append("Name : ").append(patientItem.getPName());

                    if (TextUtils.isEmpty(patientItem.getpDob())) // set age by date of birth

                        if (TextUtils.isEmpty(patientItem.getAge()) || patientItem.getAge().trim().equalsIgnoreCase("yr")
                           || patientItem.getAge().trim().equalsIgnoreCase("0 yr")
                        ) // set age by object of patient item
                            stringBuilder.append("\n").append("Age : ").append("___");
                        else if(patientItem.getAge().contains("month"))
                            stringBuilder.append("\n").append("Age : ").append(patientItem.getAge());
                        else
                            stringBuilder.append("\n").append("Age : ").append(patientItem.getAge());

                    else {

                        String[] split = patientItem.getpDob().split("-");
                        String return_value = decideMonthOrYear(split);
                        stringBuilder.append("\n").append("Age : ").append(return_value);

                    }

                    if (!TextUtils.isEmpty(patientItem.getGender())) // set gender
                        stringBuilder.append("\n").append("Gender : ").append(patientItem.getGender());

                    if (!TextUtils.isEmpty(patientItem.getPMobile()) && !patientItem.getPMobile().equalsIgnoreCase("0")) // set mobile number
                        stringBuilder.append("\n").append("Mobile : ").append(patientItem.getPMobile());

                    if (!TextUtils.isEmpty(patientItem.getPEmail())) // set email
                        stringBuilder.append("\n").append("Email : ").append(patientItem.getPEmail());

                    activity.patientDetailsChoose.setText(stringBuilder.toString());

                }
            }
        }

    }

    private String decideMonthOrYear(String[] split) {

        DateTime dateTime = new DateTime();
        int year = Integer.parseInt(split[2]);
        int month = Integer.parseInt(split[1]);

        if (dateTime.getYear() - year > 0) {

            return dateTime.getYear() - year + " yr";

        } else {

            if (dateTime.getMonthOfYear() - (month) == 0)
                return "1 month";
            else
                return dateTime.getMonthOfYear() - (month) + " month";
        }

    }

    private void AfternoonDataOnList() {

        TimeSlotModel timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("06.00 PM");
        afternoonList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("06.30 PM");
        afternoonList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("07.00 PM");
        afternoonList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("07.30 PM");
        afternoonList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("08.00 PM");
        afternoonList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("08.30 PM");
        afternoonList.add(timeSlotModel);

    }

    private void EveningDataOnList() {

        TimeSlotModel timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("02.00 PM");
        eveningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("02.30 PM");
        eveningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("03.30 PM");
        eveningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("03.00 PM");
        eveningList.add(timeSlotModel);

    }

    private void morningDataOnList() {

        TimeSlotModel timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("09.00 AM");
        morningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("09.30 AM");
        morningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("10.30 AM");
        morningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("11.00 AM");
        morningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("11.30 AM");
        morningList.add(timeSlotModel);

        timeSlotModel = new TimeSlotModel();
        timeSlotModel.setTime("12.00 AM");
        morningList.add(timeSlotModel);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        DateTime dateTime = new DateTime();

        final Calendar myCalendar = Calendar.getInstance();

        if (dateTime.getYear() == year && dateTime.getMonthOfYear() == (month + 1) && dateTime.getDayOfMonth() == dayOfMonth) {
            isToday = true;
        } else {
            isToday = false;
        }

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String myFormat = "yyyy-MM-dd"; // your format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        String myFormatInYmd = "dd-MM-yyyy"; // your format
        SimpleDateFormat sdfYmd = new SimpleDateFormat(myFormatInYmd, Locale.getDefault());

        ymdDate = sdf.format(myCalendar.getTime());

        activity.dateSlot.setText(sdfYmd.format(myCalendar.getTime()));

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hours, int mins, int second) {

        DateTime dateTime = new DateTime();
        // 1 >= 1 && 49 >= 50
        if (isToday && dateTime.getHourOfDay() <= hours && dateTime.getMinuteOfDay() <= mins) {

            Toast.makeText(this, "You cannot select previous time.", Toast.LENGTH_SHORT).show();
            return;

        }

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = String.valueOf(hours) + ':' +
                minutes + " " + timeSet;

        activity.selectTime.setText(aTime);

    }
}