package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ServerConnect.ServerConnect;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityConsentBinding;
import com.likesby.bludoc.utils.Utils;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ConsentActivity extends AppCompatActivity {

    private ActivityConsentBinding activity;
    private MultipartBody.Part multipartSignaturePart;
    private SessionManager manager = new SessionManager();
    private String patient_id;
    private ConsentTemplateModel consentTemplateModel;
    private boolean isEdit;
    private PatientsItem patientItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_consent);

        consentTemplateModel = getIntent().getParcelableExtra("consentTemplateModel");
        patient_id = getIntent().getStringExtra("patient_id");
        String patient_name = getIntent().getStringExtra("patient_name");
        patientItem = getIntent().getParcelableExtra("patientDetails");

        activity.parentsName.setText(patient_name);

        if(consentTemplateModel != null){
            activity.nameOfConsent.setText(consentTemplateModel.getTitle());
            activity.consentDescription.setText(consentTemplateModel.getDescription());
        }

        activity.aSignaturePad.setOnClickListener(view -> {

            Intent intent = new Intent(ConsentActivity.this, SignaturePadActivity.class);
            startActivityForResult(intent, 1001);

        });

        activity.headingVoice.setOnClickListener(v -> {

            Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            try {
                startActivityForResult(intentSpeech, 500);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        this,
                        "Oops! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        activity.descriptionVoice.setOnClickListener(v -> {

            Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            try {
                startActivityForResult(intentSpeech, 1000);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        this,
                        "Oops! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        activity.btnBackEditProfile.setOnClickListener(view -> onBackPressed());

        activity.submit.setOnClickListener(view -> {

            activity.nameOfConsent.setError(null);
            activity.consentDescription.setError(null);
            activity.parentsName.setError(null);

            if (TextUtils.isEmpty(activity.nameOfConsent.getText().toString())) {

                activity.nameOfConsent.setError("Name of consent is required*");
                activity.nameOfConsent.requestFocus();

            } else if (TextUtils.isEmpty(activity.consentDescription.getText().toString())) {

                activity.consentDescription.setError("Consent description is required*");
                activity.consentDescription.requestFocus();

            } else if (TextUtils.isEmpty(activity.parentsName.getText().toString())) {

                activity.parentsName.setError("Parent name is required*");
                activity.parentsName.requestFocus();

            } else if (activity.parentsName.getText().toString().length() < 3) {

                activity.parentsName.setError("Parent name is minimum 3 digit*");
                activity.parentsName.requestFocus();

            } else if (activity.dateOfConsent.getText().toString().length() < 3) {

                Toast.makeText(this, "Date is required*", Toast.LENGTH_SHORT).show();

            } else {

                uploadProfileAndLogoDetails();

            }
        });

        activity.dateOfConsent.setOnClickListener(view -> changeDateListener());

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        activity.dateOfConsent.setText(currentDate);

        setDataOnEdittext();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {

            String signedUrl = data.getStringExtra("data");
            activity.aSignaturePad.setText("Signature is added");

            RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(signedUrl));
            multipartSignaturePart = MultipartBody.Part.createFormData("signature", signedUrl, fbody);

        }

        if (requestCode == 500 && data != null) {

            ArrayList<String> stringArrayListExtra =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            activity.nameOfConsent.setText(stringArrayListExtra.get(0));

            activity.nameOfConsent.post(() -> activity.nameOfConsent.setSelection(activity.nameOfConsent.getText().length()));

        } else if (requestCode == 1000 && data != null) {

            ArrayList<String> stringArrayListExtra =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            activity.consentDescription.setText(stringArrayListExtra.get(0));

            activity.consentDescription.post(() -> activity.consentDescription.setSelection(activity.consentDescription.getText().length()));

        }


    }

    public void uploadProfileAndLogoDetails() {

        if (Utils.isConnectingToInternet(getApplicationContext())) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            List<MultipartBody.Part> multipartList = new ArrayList<>();

            if (multipartSignaturePart != null)
                multipartList.add(multipartSignaturePart);

            RequestBody doctor_id = RequestBody.create(MediaType.parse("text/plain"), manager.getPreferences(ConsentActivity.this, "doctor_id"));
            RequestBody headingConsent = RequestBody.create(MediaType.parse("text/plain"), activity.nameOfConsent.getText().toString());
            RequestBody descriptionConsent = RequestBody.create(MediaType.parse("text/plain"), activity.consentDescription.getText().toString());
            RequestBody parentsName = RequestBody.create(MediaType.parse("text/plain"), activity.parentsName.getText().toString());
            RequestBody patientName = RequestBody.create(MediaType.parse("text/plain"), patientItem.getPName());
            RequestBody patientId = RequestBody.create(MediaType.parse("text/plain"), patient_id);
            RequestBody date = RequestBody.create(MediaType.parse("text/plain"), activity.dateOfConsent.getText().toString());

            Call<ResponseSuccess> call = request.addConsentImage(
                    doctor_id,
                    headingConsent,
                    descriptionConsent,
                    patientId,
                    patientName,
                    parentsName,
                    date,
                    multipartList
            );

            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                    ResponseSuccess body = response.body();

                    activity.progressBarInclude.setVisibility(View.GONE);
                    if (response.isSuccessful() && body != null && body.getSuccess().equals("success")) {

                        if(response.body() != null) {

                            String doctorConsentId = response.body().getDoctorConsentId();

                            try {

                                byte[] data = doctorConsentId.getBytes("UTF-8");
                                String base64Ids = Base64.encodeToString(data, Base64.DEFAULT);

                                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                String shareBody = ServerConnect.ShareSignature+base64Ids;
                                intent.setType("text/plain");
                                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(intent, "Share using"));

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        activity.progressBarInclude.setVisibility(View.GONE);
                        assert response.body() != null;
                        onBackPressed();
                        Toast.makeText(ConsentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else if (response.body() != null) {

                        Toast.makeText(ConsentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else if (response.errorBody() != null) {

                        try {
                            Toast.makeText(ConsentActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(ConsentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(ConsentActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeDateListener() {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd-MM-yyyy"; // your format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            activity.dateOfConsent.setText(sdf.format(myCalendar.getTime()));

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(ConsentActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void setDataOnEdittext() {

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(patientItem.getPName())) // set name
            stringBuilder.append("Name : ").append(patientItem.getPName());

        activity.patientDetailsChoose.setEnabled(false);

        if (TextUtils.isEmpty(patientItem.getpDob())) // set age by date of birth

            if (TextUtils.isEmpty(patientItem.getAge()) || patientItem.getAge().trim().equals("0 yr") ||
                    patientItem.getAge().trim().equals("yr")
               )
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

}