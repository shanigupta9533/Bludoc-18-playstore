package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.likesby.bludoc.Adapter.InvoicesAdapter;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;
import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;
import com.likesby.bludoc.databinding.ActivityInvoiceBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("SetTextI18n")
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
    private String payStatus = "Paid";
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    private float calculateFromTotal = 0;
    private long price2=0;
    private float price3=0f;

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
                     totalAmount=0;
                     totalPriceGlobal=0;

                } else if (invoicesDataModels.size() == 1) {

                    calculateAmountInRunTime();
                    activity.countDown.setText("1 out of 1");

                } else {
                    activity.invoicesInRecyclerView.scrollToPosition(invoicesDataModels.size() - 1);
                    calculateAmountInRunTime();
                }

            }
        });

        activity.chooseFromTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(InvoiceActivity.this, TemplatesInvoicesActivity.class));

            }
        });

        activity.createInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activity.invoiceTitle.getText())) {

                    activity.invoiceTitle.requestFocus();
                    activity.invoiceTitle.setError("can't be blank");

                } else {

                    InvoicePresModel invoicePresModel = setInvoicesDataInModel();
                    Intent intent = new Intent(InvoiceActivity.this, InvoicesPresActivity.class);
                    intent.putExtra("patientName", patientName);
                    intent.putExtra("patientObject", patientsItem);
                    intent.putExtra("invoicePewModel", invoicePresModel);
                    startActivity(intent);

                }

            }
        });

        // todo first amount and percentage
        activity.amountMoreDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                calculateValueAndAssign(s.toString(), activity.advancePartialAmount, activity.amountMoreDetails);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.advancePartialAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateValueAndAssign(activity.amountMoreDetails.getText().toString(), activity.advancePartialAmount, activity.amountMoreDetails);

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

                calculateValueAndAssign(s.toString(), activity.discountTitleSpinner, activity.discountTitle);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity.discountTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                calculateValueAndAssign(activity.discountTitle.getText().toString(), activity.discountTitleSpinner, activity.discountTitle);

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

                if (!s.toString().isEmpty() && activity.taxTitleSpinner.getSelectedItemPosition()!=0){ // empty nahi hona chahiye aur spinner amount per set naa ho

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

        activity.paid.setOnClickListener(v -> {

            activity.paid.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.toPay.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.now.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "Paid";

        });

        activity.toPay.setOnClickListener(v -> {

            activity.toPay.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.paid.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.now.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "To Pay";

        });

        activity.now.setOnClickListener(v -> {

            activity.now.setBackgroundResource(R.drawable.blue_faint_btn_gradient_changes);
            activity.toPay.setBackgroundColor(Color.parseColor("#ffffff"));
            activity.paid.setBackgroundColor(Color.parseColor("#ffffff"));

            payStatus = "None";

        });

        activity.currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (activity.currency.getSelectedItemPosition() == 0)
                    activity.totalAmountTextView.setText("Total Amount  ₹ " + formatter.format(totalAmount));
                else
                    activity.totalAmountTextView.setText("Total Amount " + activity.currency.getSelectedItem().toString() + " " + formatter.format(totalAmount));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        activity.addData.setOnClickListener(v -> {

            if (TextUtils.isEmpty(activity.invoiceTitle.getText())) {

                activity.invoiceTitle.requestFocus();
                activity.invoiceTitle.setError("can't be blank");

            } else if (TextUtils.isEmpty(activity.addItemsParticulars.getText())) {

                activity.addItemsParticulars.requestFocus();
                activity.addItemsParticulars.setError("can't be blank");

            } else if (TextUtils.isEmpty(activity.amoutInvoiceDetails.getText())) {

                activity.amoutInvoiceDetails.requestFocus();
                activity.amoutInvoiceDetails.setError("Invalid Amount");

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

        activity.parentOfRelative.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

    }

    private InvoicePresModel setInvoicesDataInModel() {

        InvoicePresModel invoicePresModel = new InvoicePresModel();
        invoicePresModel.setInvoice_name(activity.patientName.getText().toString());
        invoicePresModel.setInvoice_title(activity.invoiceTitle.getText().toString());
        invoicePresModel.setCurrency(activity.currency.getSelectedItem().toString());
        invoicePresModel.setTotal_amount(String.valueOf(totalAmount));
        invoicePresModel.setDiscount_title(activity.discountTitleHeading.getText().toString());
        invoicePresModel.setAdvance_amount_title(activity.advancePartialTitle.getText().toString());
        invoicePresModel.setTax_title(activity.taxTitleHeading.getText().toString());

        if (calculateFromTotal == 0)
            calculateFromTotal = totalAmount;

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

            if(price3<=0) {
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

            amountMoreDetails.setError("Invalid Amount");

            if (activity.currency.getSelectedItemPosition() == 0)
                activity.totalAmountTextView.setText("Total Amount  ₹ " + formatter.format(totalAmount));
            else
                activity.totalAmountTextView.setText("Total Amount " + activity.currency.getSelectedItem().toString() + " " + formatter.format(totalAmount));

        } else if (!TextUtils.isEmpty(s)) {

            activity.totalAmountTextView.setText("Total Amount " + (calculateFromTotal));

        } else {

            activity.totalAmountTextView.setText("Total Amount " + calculateFromTotal);

        }

    }


    private void calculateValueAndAssignWithNoParameter() {

        calculateFromTotal = calculatedFromTotal();

        if (totalAmount <= 0 || calculateFromTotal < 0) {

            if (activity.currency.getSelectedItemPosition() == 0)
                activity.totalAmountTextView.setText("Total Amount  ₹ " + formatter.format(totalAmount));
            else
                activity.totalAmountTextView.setText("Total Amount " + activity.currency.getSelectedItem().toString() + " " + formatter.format(totalAmount));

        } else {

            activity.totalAmountTextView.setText("Total Amount " + calculateFromTotal);

        }

    }

    private float calculatedFromTotal() {

        long price1 = !TextUtils.isEmpty(activity.amountMoreDetails.getText().toString()) ? Long.parseLong(activity.amountMoreDetails.getText().toString()) : 0;
        price2 = !TextUtils.isEmpty(activity.discountTitle.getText().toString()) ? Long.parseLong(activity.discountTitle.getText().toString()) : 0;
        price3 = !TextUtils.isEmpty(activity.taxTitle.getText().toString()) ? Float.parseFloat(activity.taxTitle.getText().toString()) : 0;

        if (!TextUtils.isEmpty(activity.amountMoreDetails.getText().toString()) && activity.advancePartialAmount.getSelectedItemPosition() == 1) {
            price1 = calulatedPercentage(activity.amountMoreDetails.getText().toString());
        }

        if (!TextUtils.isEmpty(activity.discountTitle.getText().toString()) && activity.discountTitleSpinner.getSelectedItemPosition() == 1) {
            price2 = calulatedPercentage(activity.discountTitle.getText().toString());
        }

        if (!TextUtils.isEmpty(activity.taxTitle.getText().toString()) && activity.taxTitleSpinner.getSelectedItemPosition() == 1) {
            price3 = calulatedPercentageForFloat(activity.taxTitle.getText().toString(),totalAmount-price2);
        }

        return totalAmount - price1 - price2 + price3;

    }

    private long calulatedPercentage(String enterData) {

        return (totalAmount * Long.parseLong(enterData)) / 100;

    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
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

        if (activity.currency.getSelectedItemPosition() == 0)
            activity.totalAmountTextView.setText("Total Amount  ₹ " + formatter.format(totalAmount));
        else
            activity.totalAmountTextView.setText("Total Amount " + activity.currency.getSelectedItem().toString() + " " + formatter.format(totalAmount));

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


}