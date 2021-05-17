package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.itextpdf.text.PageSize;
import com.likesby.bludoc.BuildConfig;
import com.likesby.bludoc.Fragment.GeneratePres;
import com.likesby.bludoc.ModelLayer.Entities.BottomSheetItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> implements Filterable {

    private ArrayList<BottomSheetItem> mArryCountries,mFilteredList;
    private Context mContext;
    int[] image_array = new int[] { R.drawable.mail, R.drawable.whatsapp ,R.drawable.ic_share__,R.drawable.ic_download__   };
    SessionManager manager;
    FrameLayout fl_progress_bar;
    private ArrayList<AbstractViewRenderer> pages;

    public BottomSheetAdapter(Context mContext,
                              ArrayList<BottomSheetItem> objects, FrameLayout fl_progress_bar) {
        mArryCountries = objects;
        mFilteredList = mArryCountries;
        this.mContext = mContext;
        manager = new SessionManager();
        this.fl_progress_bar = fl_progress_bar;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_bottomsheet,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.id.setText(mFilteredList.get(i).getMenuId());
        viewHolder.Name.setText(mFilteredList.get(i).getMenuName());



        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int heightt = size.y;
        //Log.e("width   =   ", "" + width);
        //Log.e("height   =   ", "" + heightt);

        int width_custom = width / 2;
        int heightt_custom = heightt / 2;
        // Log.e("width_custom   =   ", "" + width_custom);
        //Log.e("heightt_custom   =   ", "" + heightt_custom);
        //  hid = mFilteredList.get(i).getHid();
        String drawable_path = mFilteredList.get(i).getMenuImage();
        if (width > 800) {
            // Log.e("DIMENSIONS", "  >>  800");

            //if (mFilteredList.get(i).getImagePath() == null || mFilteredList.get(i).getImagePath().equals("")) {
            Picasso.with(mContext).
                    load(/*ServerConnect.IMAGE_URL + */image_array[i])
                    //.resize(width_custom-100 , width_custom-100)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    // .centerCrop()
                    .placeholder(image_array[i])
                    .into(viewHolder.bottom_sheet_iv, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
               /* } else {
                    Picasso.with(ctx).
                            load(mFilteredList.get(i).getImagePath())
                            .resize(heightt_custom - 30, heightt_custom - 15)
                            //.resize(width_custom,heightt_custom)
                            //.centerInside()
                            .into(viewHolder.pic, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    viewHolder.pb.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }
            else {
                if (mFilteredList.get(i).getImagePath() == null || mFilteredList.get(i).getImagePath().equals("")) {*/

        } else {
            //Log.e("DIMENSIONS", "  <<  800");

            Picasso.with(mContext)
                    .load(/*ServerConnect.IMAGE_URL + */image_array[i])
                    // .resize(width_custom-50 , width_custom-50)
                    // .centerCrop()
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    //.centerInside()
                    .placeholder(image_array[i])
                    .into(viewHolder.bottom_sheet_iv, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

                /*} else {
                    Picasso.with(ctx).
                            load(mFilteredList.get(i).getImagePath())
                            .resize(heightt_custom - 20, heightt_custom - 10)
                            //.resize(width_custom,heightt_custom)
                            //.centerInside()
                            .into(viewHolder.pic, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    viewHolder.pb.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }*/
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

                    mFilteredList = mArryCountries;
                } else {

                    ArrayList<BottomSheetItem> filteredList = new ArrayList<>();

                    for (BottomSheetItem categories : mArryCountries) {

                        if (categories.getMenuName().toLowerCase().contains(charString))
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
                mFilteredList = (ArrayList<BottomSheetItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setData(ArrayList<AbstractViewRenderer> page) {
        this.pages=page;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView id, Name,expiry;
        LinearLayout ll_bottom_sheet;
        ImageView bottom_sheet_iv;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view)
        {
            super(view);
            mContext = view.getContext();

            id        = view.findViewById(R.id.__bottom_sheet_id);
            Name      = view.findViewById(R.id.__bottom_sheet_name);
            expiry      = view.findViewById(R.id.bottomsheet_sub_expiry);
            ll_bottom_sheet = view.findViewById(R.id.ll_bottom_sheet);
            bottom_sheet_iv = view.findViewById(R.id.bottom_sheet_iv);

            ll_bottom_sheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, ""+Name.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                    if(getAdapterPosition()==0)
                    {
                           /* Intent intent = new Intent(mContext, InviteMembers.class);
                            mContext.startActivity(intent);*/
                      //  Toast.makeText(mContext, "Working on Email ", Toast.LENGTH_SHORT).show();

                          /*  if (Utils.isConnectingToInternet(mContext)) {

                                fl_progress_bar.setVisibility(View.VISIBLE);
                                Retrofit retrofit = RetrofitClient.getInstance();;
                                final WebServices request = retrofit.create(WebServices.class);;
                                RequestBody EMAIL_ID = RequestBody.create(MediaType.parse("text/plain"),  GeneratePres.patient_item.getPEmail() );
                                RequestBody SUBJECT = RequestBody.create(MediaType.parse("text/plain"),  "E-prescription from "+ manager.getPreferences(mContext, "name"));
                                RequestBody MESSAGE = RequestBody.create(MediaType.parse("text/plain"),  "Dear "+ GeneratePres.patient_item.getPName() + ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc" );

                                ArrayList<MultipartBody.Part> multipartBody = new ArrayList<>();
                                Call<ResponseRegister> call;
                                // File file = new File(file_name_tv.getText().toString().trim());
                                if(GeneratePres.getFiles()!=null)
                                {
                                    for (Uri urii: GeneratePres.getFiles()
                                         ) {

                                        String realPathFromURI = FileUtil.getRealPathFromURI(mContext,urii);

                                        String path=""+ FileUtil.getRealPathFromURI(mContext,urii);
                                        // it contains your image path...I'm using a temp string...
                                        String filename=path.substring(path.lastIndexOf("/")+1);

                                        assert realPathFromURI != null;
                                        File imageUp = new File((realPathFromURI));
                                        RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), imageUp);
                                        multipartBody.add( MultipartBody.Part.createFormData("doc_file", filename,fbody));
                                    }
                                }
                                call = request.sendEmailPrescription(EMAIL_ID, SUBJECT,MESSAGE, multipartBody);

                                call.enqueue(new Callback<ResponseRegister>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseRegister> call, @NonNull retrofit2.Response<ResponseRegister> response) {
                                        ResponseRegister jsonResponse = response.body();

                                        if(jsonResponse!=null)
                                        {
                                            fl_progress_bar.setVisibility(View.GONE);
                                            if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                                                    jsonResponse.getMessage().equalsIgnoreCase("Profile Updated")) {
                                                Toast.makeText(mContext, "Email Sent", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "Email Sent Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {

                                        }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseRegister> call, @NonNull Throwable t) {
                                        fl_progress_bar.setVisibility(View.GONE);
                                        Log.e("Error  ***", t.getMessage());
                                        Toast.makeText(mContext, "Email Sent Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
*/

//                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//                        emailIntent.setType("application/pdf");
//                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""+GeneratePres.patient_item.getPEmail()});
//                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
//                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear "+ GeneratePres.patient_item.getPName() + ", "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription / Certificate via BluDoc");
//                        ArrayList<Uri> files = GeneratePres.getFiles();
//                        File pdfWithMultipleImage = createPDFWithMultipleImage(files);
//                        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pdfWithMultipleImage.getPath()));
//                        mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                        ArrayList<Uri> files = GeneratePres.getFiles();
                        File pdfWithMultipleImage = createPDFWithMultipleImage(files);
                        Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", pdfWithMultipleImage);
                        sendMail(contentUri);

                        /*Intent intent = new Intent();
                      //  intent.setType("message/rfc822");
                        intent.setAction(Intent.ACTION_SEND);
                        // intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{""+GeneratePres.patient_item.getPEmail()});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from "+ manager.getPreferences(mContext, "name"));
                        intent.putExtra(Intent.EXTRA_TEXT, "Dear "+ GeneratePres.patient_item.getPName() + ", Dr. "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription via BluDoc");
                       // intent.setType("image/jpeg");
                        ArrayList<Uri> files = GeneratePres.getFiles();
                        //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                        intent.setDataAndType(Uri.parse("mailto:"+GeneratePres.patient_item.getPEmail()),"message/rfc822"); // or just "mailto:" for blank

                        Intent shareIntent = Intent.createChooser(intent, "Send mail...");
                        mContext.startActivity(shareIntent);*/

                       /* Intent i = new Intent(Intent.ACTION_SEND);


                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                           mContext. startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }*/


                    }
                    else if(getAdapterPosition()==1)
                    {
                        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                        if(isWhatsappInstalled)
                        {


                            //======================================================================
                            PackageManager packageManager = mContext.getPackageManager();
                            // Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                            try {
                                ArrayList<Uri> files = GeneratePres.getFiles();

                                String smsNumber = GeneratePres.patient_item.getPMobile().trim();
                                smsNumber = smsNumber.replace("+","").trim(); // E164 format without '+' sign
                                if(!(smsNumber.contains("91")))
                                    smsNumber = "91"+smsNumber;
                                Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                sendIntent.setType("application/pdf");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Dear "+ GeneratePres.patient_item.getPName() + ", "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription / Certificate via BluDoc");
                                File pdfWithMultipleImage = createPDFWithMultipleImage(files);
                                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pdfWithMultipleImage.getPath()));
                                sendIntent.putExtra("jid", ""+smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                                sendIntent.setPackage("com.whatsapp");
                                if (sendIntent.resolveActivity(mContext.getPackageManager()) == null) {
                                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (sendIntent.resolveActivity(packageManager) != null) {
                                    mContext.startActivity(sendIntent);
                                }
                                else
                                    Toast.makeText(mContext, "Resolve activity Null", Toast.LENGTH_SHORT).show();
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        else
                            openBusinessWhatsUpAndShare();

                    }
                    else if(getAdapterPosition()==2)
                    {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        // intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{""+GeneratePres.patient_item.getPEmail()});
                        intent.putExtra(Intent.EXTRA_TEXT, "Dear "+ GeneratePres.patient_item.getPName() + ", "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription / Certificate via BluDoc");
                        intent.setType("*/*");
                        ArrayList<Uri> files = GeneratePres.getFiles();
                        File pdfWithMultipleImage = createPDFWithMultipleImage(files);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pdfWithMultipleImage.getPath()));
                        Intent shareIntent = Intent.createChooser(intent, "Send...");
                        mContext.startActivity(shareIntent);

                    } else if(getAdapterPosition()==3){

                    }

                    //AppRater.app_launched(mContext);
                }
            });
        }

        private void sendMail(Uri URI) {
            try {

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("application/pdf");
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{""+GeneratePres.patient_item.getPEmail()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-prescription from " + manager.getPreferences(mContext, "name"));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear " + GeneratePres.patient_item.getPName() + ", " + manager.getPreferences(mContext, "name") + " has sent you an E-prescription / Certificate via BluDoc");

                emailIntent.setPackage("com.google.android.gm");
                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                }

                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                mContext.startActivity(Intent.createChooser(emailIntent,"Sending email..."));

            } catch (Throwable t) {

                Toast.makeText(getApplicationContext(),

                        "Request failed try again: " + t.toString(),

                        Toast.LENGTH_LONG).show();

            }

        }

        private void openBusinessWhatsUpAndShare() {

            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp.w4b");
            if(isWhatsappInstalled)
            {
                //======================================================================
                PackageManager packageManager = mContext.getPackageManager();
                // Intent sendIntent = new Intent(Intent.ACTION_VIEW);

                try {
                    ArrayList<Uri> files = GeneratePres.getFiles();

                    String smsNumber = GeneratePres.patient_item.getPMobile().trim();
                    smsNumber = smsNumber.replace("+","").trim(); // E164 format without '+' sign
                    if(!(smsNumber.contains("91")))
                        smsNumber = "91"+smsNumber;
                    Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    sendIntent.setType("application/pdf");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Dear "+ GeneratePres.patient_item.getPName() + ", "+ manager.getPreferences(mContext, "name")+ " has sent you an E-prescription / Certificate via BluDoc");
                    File pdfWithMultipleImage = createPDFWithMultipleImage(files);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pdfWithMultipleImage.getPath()));
                    sendIntent.putExtra("jid", ""+smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                    sendIntent.setPackage("com.whatsapp.w4b");
                    if (sendIntent.resolveActivity(mContext.getPackageManager()) == null) {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (sendIntent.resolveActivity(packageManager) != null) {
                        mContext.startActivity(sendIntent);
                    }
                    else
                        Toast.makeText(mContext, "Resolve activity Null", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
            else
                Toast.makeText(mContext, "WhatsApp Not installed", Toast.LENGTH_SHORT).show();

        }

        private File createPDFWithMultipleImage(ArrayList<Uri> files){

            File file = getOutputFile();
            if (file != null){
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    PdfDocument pdfDocument = new PdfDocument();

                    for (int i = 0; i < files.size(); i++){
                        Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getPath(mContext,files.get(i)));
                         PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(), (i + 1)).create();
                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        canvas.drawPaint(paint);
                        canvas.drawBitmap(bitmap, 0f, 0f, null);
                        pdfDocument.finishPage(page);
                        bitmap.recycle();
                    }
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file;
        }

        private File getOutputFile(){
            File root = new File(mContext.getExternalFilesDir(null),"My PDF Folder");

            boolean isFolderCreated = true;

            if (!root.exists()){
                isFolderCreated = root.mkdir();
            }

            if (isFolderCreated) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
                String imageFileName = "PDF_" + timeStamp;

                return new File(root, imageFileName + ".pdf");
            }
            else {
                Toast.makeText(mContext, "Folder is not created", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        private boolean whatsappInstalledOrNot(String uri) {
            PackageManager pm = mContext.getPackageManager();
            boolean app_installed = false;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
            } catch (PackageManager.NameNotFoundException e) {
            }
            return app_installed;
        }

    }
}
