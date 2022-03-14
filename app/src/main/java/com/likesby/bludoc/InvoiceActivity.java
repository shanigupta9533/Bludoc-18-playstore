package com.likesby.bludoc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.likesby.bludoc.Adapter.InvoicesAdapter;
import com.likesby.bludoc.Fragment.PopUpSubscriptionDialogFragment;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;
import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityInvoiceBinding;
import com.likesby.bludoc.utils.DateUtils;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("SetTextI18n|ClickableViewAccessibility")
public class InvoiceActivity extends AppCompatActivity {

    private ActivityInvoiceBinding activity;
    private final ArrayList<InvoicesDataModel> invoicesDataModels = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private InvoicesAdapter invoicesAdapter;
    private boolean isEdit = false;
    private int positionGlobal;
    private InvoicesDataModel invoicesDataModelGlobal;
    private long totalAmount = 0;
    private long totalPriceGlobal;
    private String patientName;
    private PatientsItem patientsItem;
    private String payStatus = "None";
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    private long calculateFromTotal = 0;
    private long price2 = 0;
    private SessionManager manager = new SessionManager();
    private float price3 = 0f;
    private String currency = "₹";
    private long price1 = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_invoice);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activity.invoicesInRecyclerView.setLayoutManager(linearLayoutManager);
        invoicesAdapter = new InvoicesAdapter(this, invoicesDataModels);
        activity.invoicesInRecyclerView.setAdapter(invoicesAdapter);
        activity.countDown.setVisibility(View.GONE);

        activity.taxTitleSpinner.setSelection(1);

        activity.currency.setText(currency);

        Intent intent = getIntent();
        if (intent != null) {
            patientName = intent.getStringExtra("patientName");
            patientsItem = intent.getParcelableExtra("patientObject");
            activity.patientName.setText(patientName);
        }

        invoicesAdapter.setOnClickListener(new InvoicesAdapter.OnClickListener() {
            @Override
            public void onEdit(InvoicesDataModel invoicesDataModel, int position) {

                isEdit = true;
                activity.addData.setText("Save Edit");
                positionGlobal = position;
                invoicesDataModelGlobal = invoicesDataModel;

                activity.invoiceTitle.setText(invoicesDataModel.getInvoicesTitle());
                activity.amoutInvoiceDetails.setText(invoicesDataModel.getAmount());
                activity.addItemsParticulars.setText(invoicesDataModel.getItem_name());

                activity.invoiceTitle.setSelection(invoicesDataModel.getInvoicesTitle().length());
                activity.amoutInvoiceDetails.setSelection(invoicesDataModel.getAmount().length());
                activity.addItemsParticulars.setSelection(invoicesDataModel.getItem_name().length());
            }

            @Override
            public void onDelete(InvoicesDataModel invoicesDataModel, int position) {

                invoicesDataModels.remove(position);
                invoicesAdapter.notifyDataSetChanged();

                if (invoicesDataModels.isEmpty()) {

                    activity.countDown.setVisibility(View.GONE);
                    activity.totalAmountTextView.setText("Total Amount");

                    // after delete all items then price will we zero
                    totalAmount = 0;
                    totalPriceGlobal = 0;

                } else if (invoicesDataModels.size() == 1) {

                    calculateAmountInRunTime();
                    activity.countDown.setText("1 out of 1");

                } else {
                    activity.invoicesInRecyclerView.scrollToPosition(invoicesDataModels.size() - 1);
                    calculateAmountInRunTime();
                }

            }
        });

        activity.invoiceDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    activity.cancelAction.setVisibility(View.VISIBLE);

                } else {

                    activity.cancelAction.setVisibility(View.GONE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.invoiceDate.setText("");

            }
        });

        activity.chooseFromTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(InvoiceActivity.this, TemplatesInvoicesActivity.class));

            }
        });

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar c2 = Calendar.getInstance();
        String formattedDate = df.format(c2.getTime());
        activity.invoiceDate.setText(formattedDate);

        activity.invoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(InvoiceActivity.this, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(InvoiceActivity.this, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(InvoiceActivity.this, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPausedForChecking();
                                return;

                            }
                        } else {
                            popupPausedForChecking();
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPausedForChecking();
                        } else {

                            changeDateListener();

                        }

                    }
                } else {
                    popup();
                }

            }
        });

        activity.createInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               runTimePermission();

            }
        });

        // todo first amount and percentage
        activity.amountMoreDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s) && Long.parseLong(s.toString()) > 100 && activity.advancePartialAmount.getSelectedItemPosition() == 1) {
                    Toast.makeText(InvoiceActivity.this, "Percentage Invalid", Toast.LENGTH_SHORT).show();
                    activity.amountMoreDetails.setError("Percentage Invalid");
                    return;
                }

                calculateValueAndAssign(s.toString(), activity.advancePartialAmount, activity.amountMoreDetails);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.btnBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        activity.advancePartialAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    setEditTextMaxLength(activity.amountMoreDetails, 6);
                else
                    setEditTextMaxLength(activity.amountMoreDetails, 3);

                activity.amountMoreDetails.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // todo second first amount and percentage
        activity.discountTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s) && Long.parseLong(s.toString()) > 100 && activity.discountTitleSpinner.getSelectedItemPosition() == 1) {

                    Toast.makeText(InvoiceActivity.this, "Percentage Invalid", Toast.LENGTH_SHORT).show();
                    activity.discountTitle.setError("Percentage Invalid");
                    return;
                }

                calculateValueAndAssign(s.toString(), activity.discountTitleSpinner, activity.discountTitle);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.discountTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    setEditTextMaxLength(activity.discountTitle, 6);
                else
                    setEditTextMaxLength(activity.discountTitle, 3);

                activity.discountTitle.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // todo third amount and percentage
        activity.taxTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty() && activity.taxTitleSpinner.getSelectedItemPosition() != 0) { // empty nahi hona chahiye aur spinner amount per set naa ho

                    String str2 = PerfectDecimal(s.toString(), 3, 2);

                    if (!str2.equalsIgnoreCase(s.toString())) {
                        activity.taxTitle.setText(str2);
                        activity.taxTitle.setSelection(str2.length());
                    }

                }

                calculateValueAndAssign(s.toString(), activity.taxTitleSpinner, activity.taxTitle);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.taxTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                activity.taxTitle.setText("");
                calculateValueAndAssign(activity.taxTitle.getText().toString(), activity.taxTitleSpinner, activity.taxTitle);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activity.paid.setOnClickListener(v ->

        {

            activity.paid.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.toPay.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.now.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "Paid";

        });

        activity.toPay.setOnClickListener(v ->

        {

            activity.toPay.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.paid.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.now.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "To Pay";

        });

        activity.now.setOnClickListener(v ->

        {

            activity.now.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.toPay.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.paid.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "None";

        });

        activity.currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {

                        activity.totalAmountTextView.setText("Total Amount " + getDecideCurrency(code) + " " + formatter.format(totalAmount));
                        invoicesAdapter.setCurrency(code);
                        invoicesAdapter.notifyDataSetChanged();
                        currency = code;

                        if(code.equalsIgnoreCase("INR"))
                            currency= "₹";

                        activity.currency.setText(currency);
                        picker.dismiss();

                    }
                });
                picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");

            }
        });


        activity.invoicesInRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {

                int position = linearLayoutManager.findFirstVisibleItemPosition();
                activity.countDown.setText((position + 1) + " out of " + invoicesAdapter.getItemCount());
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        activity.addData.setOnClickListener(v ->
        {

            if (TextUtils.isEmpty(activity.invoiceTitle.getText())) {

                activity.invoiceTitle.requestFocus();
                activity.invoiceTitle.setError("can't be blank");

            } else if (TextUtils.isEmpty(activity.addItemsParticulars.getText())) {

                activity.addItemsParticulars.requestFocus();
                activity.addItemsParticulars.setError("can't be blank");

            } else if (TextUtils.isEmpty(activity.amoutInvoiceDetails.getText())) {

                activity.amoutInvoiceDetails.requestFocus();
                activity.amoutInvoiceDetails.setError("Input Amount");

            } else if (totalAmount + Double.parseDouble(activity.amoutInvoiceDetails.getText().toString()) > 1000000) {

                activity.amoutInvoiceDetails.requestFocus();
                activity.amoutInvoiceDetails.setError("Amount Exceed...");

            } else if (isEdit) {

                invoicesDataModelGlobal.setInvoicesTitle(activity.invoiceTitle.getText().toString());
                invoicesDataModelGlobal.setAmount(activity.amoutInvoiceDetails.getText().toString());
                invoicesDataModelGlobal.setItem_name(activity.addItemsParticulars.getText().toString());

                invoicesAdapter.notifyDataSetChanged();
                isEdit = false;
                activity.addData.setText("ADD");

                activity.amoutInvoiceDetails.setText("");
                activity.addItemsParticulars.setText("");

                activity.invoicesInRecyclerView.post(() -> {

                    activity.invoicesInRecyclerView.smoothScrollToPosition(positionGlobal);

                    calculateAmountInRunTime();

                });

            } else {

                InvoicesDataModel invoicesDataModel = new InvoicesDataModel();
                invoicesDataModel.setInvoicesTitle(activity.invoiceTitle.getText().toString());
                invoicesDataModel.setAmount(activity.amoutInvoiceDetails.getText().toString());
                invoicesDataModel.setItem_name(activity.addItemsParticulars.getText().toString());
                invoicesDataModels.add(invoicesDataModel);
                invoicesAdapter.notifyDataSetChanged();

                activity.amoutInvoiceDetails.setText("");
                activity.addItemsParticulars.setText("");

                activity.countDown.setVisibility(View.VISIBLE);
                activity.invoicesInRecyclerView.post(() -> {

                    activity.invoicesInRecyclerView.smoothScrollToPosition(invoicesDataModels.size() - 1);

                    calculateAmountInRunTime();

                });

            }

        });

        activity.invoiceDetailsTab.setVisibility(View.VISIBLE);
        activity.moreDetailsTab.setVisibility(View.GONE);

        activity.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (activity.tabLayout.getSelectedTabPosition() == 0) {

                    activity.invoiceDetailsTab.setVisibility(View.VISIBLE);
                    activity.moreDetailsTab.setVisibility(View.GONE);

                } else {

                    activity.invoiceDetailsTab.setVisibility(View.GONE);
                    activity.moreDetailsTab.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        activity.parentOfRelative.getViewTreeObserver().
//
//                addOnGlobalLayoutListener(keyboardLayoutListener);

    }

    private void popup() {

        final Dialog dialog_data = new Dialog(this);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.feedback);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_data.dismiss();

            }
        });

        if (!isFinishing())
            dialog_data.show();
    }

    private void popupPausedForChecking() {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment = new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords", "");
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(getSupportFragmentManager(), "");

    }

    private void changeDateListener() {

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd-MM-yyyy"; // your format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            activity.invoiceDate.setText(sdf.format(myCalendar.getTime()));
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(InvoiceActivity.this, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private void generateInvoice() {

        activity.amountMoreDetails.setError(null);
        activity.discountTitle.setError(null);
        activity.taxTitle.setError(null);

        if (TextUtils.isEmpty(activity.invoiceTitle.getText())) {

            activity.invoiceTitle.requestFocus();
            activity.invoiceTitle.setError("can't be blank");

        } else if (activity.discountTitleSpinner.getSelectedItemPosition() == 1 && !TextUtils.isEmpty(activity.discountTitle.getText().toString()) && Long.parseLong(activity.discountTitle.getText().toString()) > 100) {

            activity.discountTitle.requestFocus();
            activity.discountTitle.setError("Percentage Exceeded");

        } else if (activity.advancePartialAmount.getSelectedItemPosition() == 1 && !TextUtils.isEmpty(activity.amountMoreDetails.getText().toString()) && Long.parseLong(activity.amountMoreDetails.getText().toString()) > 100) {

            activity.amountMoreDetails.requestFocus();
            activity.amountMoreDetails.setError("Percentage Exceeded");

        } else if (activity.taxTitleSpinner.getSelectedItemPosition() == 1 && !TextUtils.isEmpty(activity.taxTitle.getText().toString()) && Float.parseFloat(activity.taxTitle.getText().toString()) > 100) {

            activity.taxTitle.requestFocus();
            activity.taxTitle.setError("Percentage Exceeded");

        } else if (calculatedFromTotal() < 0) {

            activity.discountTitle.requestFocus();
            activity.amountMoreDetails.setError("Amount exceeded.");
            activity.discountTitle.setError("Amount exceeded.");
            activity.taxTitle.setError("Amount exceeded.");

        } else {

            InvoicePresModel invoicePresModel = setInvoicesDataInModel();
            Intent intent = new Intent(InvoiceActivity.this, InvoicesPresActivity.class);
            intent.putExtra("patientName", patientName);
            intent.putExtra("patientObject", patientsItem);
            intent.putExtra("invoicePewModel", invoicePresModel);
            startActivity(intent);

        }

    }

    private String getDecideCurrency(String selectedItemPosition) {

        if (selectedItemPosition.equalsIgnoreCase("INR")) {

            return "₹";

        } else {

            return selectedItemPosition;

        }

    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    private InvoicePresModel setInvoicesDataInModel() {

        InvoicePresModel invoicePresModel = new InvoicePresModel();
        invoicePresModel.setInvoice_name(activity.patientName.getText().toString());
        invoicePresModel.setInvoice_title(activity.invoiceTitle.getText().toString());
        invoicePresModel.setCurrency(currency);
        invoicePresModel.setTotal_amount(String.valueOf(totalAmount));
        invoicePresModel.setDiscount_title(activity.discountTitleHeading.getText().toString());
        invoicePresModel.setAdvance_amount_title(activity.advancePartialTitle.getText().toString());
        invoicePresModel.setTax_title(activity.taxTitleHeading.getText().toString());

        if (!TextUtils.isEmpty(activity.invoiceDate.getText().toString()))
            invoicePresModel.setDate(activity.invoiceDate.getText().toString());


        if (calculateFromTotal == 0)
            calculateFromTotal = totalAmount + (long) price3;

        invoicePresModel.setFinal_amount(String.valueOf(calculateFromTotal));
        invoicePresModel.setNote(activity.endNote.getText().toString());
        invoicePresModel.setPay_status(payStatus);

        // todo discount partial amount
        if (activity.discountTitleSpinner.getSelectedItemPosition() == 0) {
            invoicePresModel.setDiscount(activity.discountTitle.getText().toString());
            invoicePresModel.setDiscount_percentage("");
        } else {
            invoicePresModel.setDiscount(calulatedPercentageFromTime(activity.discountTitle.getText().toString()));
            invoicePresModel.setDiscount_percentage(activity.discountTitle.getText().toString());
        }

        // todo calcuate advance partial amount
        if (activity.advancePartialAmount.getSelectedItemPosition() == 0) {
            invoicePresModel.setAdvance_amount(activity.amountMoreDetails.getText().toString());
            invoicePresModel.setAdvance_amount_percentage("");
        } else {
            invoicePresModel.setAdvance_amount(calulatedPercentageFromTime(activity.amountMoreDetails.getText().toString()));
            invoicePresModel.setAdvance_amount_percentage(activity.amountMoreDetails.getText().toString());
        }

        // todo tax title spinner
        if (activity.taxTitleSpinner.getSelectedItemPosition() == 0) {
            invoicePresModel.setTax(activity.taxTitle.getText().toString());
            invoicePresModel.setTax_percentage("");
        } else {

            if (price3 <= 0) {
                invoicePresModel.setTax("");
            } else {
                invoicePresModel.setTax(String.valueOf(price3));
            }

            invoicePresModel.setTax_percentage(activity.taxTitle.getText().toString());
        }

        invoicePresModel.setItems(invoicesDataModels);

        return invoicePresModel;

    }

    private final ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            // navigation bar height
            int navigationBarHeight = 0;
            int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            // status bar height
            int statusBarHeight = 0;
            resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            // display window size for the app layout
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

            // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
            int keyboardHeight = activity.parentOfRelative.getRootView().getHeight() - (statusBarHeight + navigationBarHeight + rect.height());

            if (keyboardHeight <= 0) {
                onHideKeyboard();
            } else {
                onShowKeyboard(keyboardHeight);
            }
        }
    };

    private void onShowKeyboard(int keyboardHeight) {

        activity.parentOfButton.setVisibility(View.GONE);

    }

    private void onHideKeyboard() {

        activity.parentOfButton.setVisibility(View.VISIBLE);

    }

    private String calulatedPercentageFromTime(String enterData) {

        if (TextUtils.isEmpty(enterData)) {
            enterData = "0";
        }

        float price = (totalPriceGlobal * Float.parseFloat(enterData)) / 100;
        return String.valueOf(price);

    }

    private void calculateValueAndAssign(String s, AppCompatSpinner spinner, EditText amountMoreDetails) {

        calculateFromTotal = calculatedFromTotal();

        if (!TextUtils.isEmpty(s) && (totalAmount <= 0 || calculateFromTotal < 0)) {

            amountMoreDetails.setError("Input Amount");
            activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(totalAmount));

        } else if (!TextUtils.isEmpty(s)) {

            activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(calculateFromTotal));

        } else {

            activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(calculateFromTotal));

        }

    }


    private void calculateValueAndAssignWithNoParameter() {

        calculateFromTotal = calculatedFromTotal();

        if (totalAmount <= 0 || calculateFromTotal < 0) {

            activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(totalAmount));

        } else {

            activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(calculateFromTotal));

        }

    }

    private long calculatedFromTotal() {

        price1 = !TextUtils.isEmpty(activity.amountMoreDetails.getText().toString()) ? Long.parseLong(activity.amountMoreDetails.getText().toString()) : 0;
        price2 = !TextUtils.isEmpty(activity.discountTitle.getText().toString()) ? Long.parseLong(activity.discountTitle.getText().toString()) : 0;
        price3 = !TextUtils.isEmpty(activity.taxTitle.getText().toString()) ? Float.parseFloat(activity.taxTitle.getText().toString()) : 0;

        if (!TextUtils.isEmpty(activity.amountMoreDetails.getText().toString()) && activity.advancePartialAmount.getSelectedItemPosition() == 1) {
            price1 = calulatedPercentage(activity.amountMoreDetails.getText().toString());
        }

        if (!TextUtils.isEmpty(activity.discountTitle.getText().toString()) && activity.discountTitleSpinner.getSelectedItemPosition() == 1) {
            price2 = calulatedPercentage(activity.discountTitle.getText().toString());
        }

        if (!TextUtils.isEmpty(activity.taxTitle.getText().toString()) && activity.taxTitleSpinner.getSelectedItemPosition() == 1) {
            price3 = calulatedPercentageForFloat(activity.taxTitle.getText().toString(), totalAmount - price2);
        }

        return (long) (totalAmount - price1 - price2 + price3);

    }

    private long calulatedPercentage(String enterData) {

        return (totalAmount * Long.parseLong(enterData)) / 100;

    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL) {
        if (str.charAt(0) == '.') str = "0" + str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0;
        char t;
        while (i < max) {
            t = str.charAt(i);
            if (t != '.' && after == false) {
                up++;
                if (up > MAX_BEFORE_POINT) return rFinal;
            } else if (t == '.') {
                after = true;
            } else {
                decimal++;
                if (decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }
        return rFinal;
    }

    private float calulatedPercentageForFloat(String enterData, long price) {

        return (price * Float.parseFloat(enterData)) / 100;

    }

    private void calculateAmountInRunTime() {

        long Amount = 0;

        for (InvoicesDataModel invoicesDataModel : invoicesDataModels) {

            Amount = Amount + Long.parseLong(invoicesDataModel.getAmount());

        }

        totalAmount = Amount;
        totalPriceGlobal = Amount;

        activity.totalAmountTextView.setText("Total Amount " + currency + " " + formatter.format(totalAmount));

        calculateValueAndAssignWithNoParameter(); // after add and delete item on recycler then them value change ...... via more details

    }

    public static String formatNumber(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        popupExit();
    }

    private void popupExit() {
        final Dialog dialog_data = new Dialog(this);
        dialog_data.setCancelable(true);
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_exit);
        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        final Button btn_no = dialog_data.findViewById(R.id.btn_no);
        final Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText("Would you like to leave this page?");
        CountDownTimer countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                btn_no.setVisibility(View.VISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                        dialog_data.dismiss();


                    }
                }, 1);


            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });

        dialog_data.show();
    }

    public void runTimePermission(){

        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide storage permission for better reach ......";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                generateInvoice();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });

    }

}