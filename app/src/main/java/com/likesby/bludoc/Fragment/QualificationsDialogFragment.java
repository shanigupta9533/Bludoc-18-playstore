package com.likesby.bludoc.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.likesby.bludoc.HomeActivity;
import com.likesby.bludoc.ModelLayer.Entities.ResponseRegister;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import org.jetbrains.annotations.NotNull;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QualificationsDialogFragment extends DialogFragment {

    private onClickListener onClickListener;
    private SessionManager sessionManager;
    private FragmentActivity fragmentActivity;
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    private ProgressDialog progressDialog;
    private EditText qualificationEdit;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    public QualificationsDialogFragment() {
        // Required empty public constructor
    }

    public interface onClickListener {

        void onclick(EditText qualificationEdit);

    }

    public void setOnClickListener(onClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sessionManager = new SessionManager();
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        View inflate = inflater.inflate(R.layout.fragment_qualifications_dialog, container, false);

        qualificationEdit = inflate.findViewById(R.id.bio_edit);

        inflate.findViewById(R.id.save_bio).setOnClickListener(v -> {

            if(TextUtils.isEmpty(qualificationEdit.getText().toString())) {

                qualificationEdit.setError("Enter Your Qualifications");
                qualificationEdit.requestFocus();
                return;
            }

            progressDialog = new ProgressDialog(fragmentActivity);
            progressDialog.setMessage("Please wait.");
            progressDialog.setCancelable(false);
            progressDialog.show();

            apiViewHolder.registerQualifications(qualificationEdit.getText().toString(),sessionManager.getPreferences(fragmentActivity, "doctor_id"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseRegister2);

        });

        return inflate;
    }

    SingleObserver<ResponseRegister> responseRegister2 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {

            if(progressDialog!=null)
                progressDialog.dismiss();

            if (response != null) {

                Log.e("TAG", "responseRegister2: >> " + response.getMessage());

                if (response.getMessage() == null) {

                    Toast.makeText(fragmentActivity, "Not get any response from server", Toast.LENGTH_SHORT).show();

                } else if (response.getMessage().equals("Profile Updated")) {

                    InputMethodManager imm = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(qualificationEdit.getWindowToken(),0);

                    Toast.makeText(fragmentActivity, "Qualification updated successfully", Toast.LENGTH_SHORT).show();
                    sessionManager.setPreferences(fragmentActivity, "addtional_qualification",qualificationEdit.getText().toString());

                    sessionManager.setPreferences(fragmentActivity, "update_qualifications","true");

                    dismiss();

                }
            } else {
                Toast.makeText(fragmentActivity, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            if(progressDialog!=null)
                progressDialog.dismiss();

            Log.e("TAG", "onError: responseRegister2 >> " + e.toString());
            //intentCall();
            Toast.makeText(fragmentActivity, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    public void hideKeyboard() {
        if (fragmentActivity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(fragmentActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}