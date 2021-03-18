//package com.likesby.bludoc;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.SearchView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.pi.grozmart.DBHelper.MyDB;
//import com.pi.grozmart.NetworkCheck.CheckNetwork;
//import com.pi.grozmart.RetrofitClients.RetrofitClientGM;
//import com.pi.grozmart.adapters.___SearchAdapter;
//import com.pi.grozmart.m3.PPP;
//import com.pi.grozmart.m3.ProductsItem;
//import com.pi.grozmart.models.ServiesItem;
//
//import java.util.ArrayList;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//
//public class SearchActivity extends AppCompatActivity
//{
//    private static final String TAG = "SearchActivity";
//    private ArrayList<ProductsItem> mArrayListAllProducts,mArrayListAllProductsFinal;
//    MyDB myDB;
//    SearchView searchView;
//    Boolean expanded = false;
//    ProgressDialog mProgressDialog;
//    Context context;
//    private ArrayList<ServiesItem> mArrayListSearch;
//
//    private RecyclerView mRecyclerViewSearch, mRecyclerViewRecommendedVideos,
//            mRecyclerViewPopularVideos;
//    private ___SearchAdapter mAdapterSearch;
//    TextView tv_viewmore_latest,tv_viewmore_recommended,tv_viewmore_popular;
//    //Peogress Dialog Attributes
//    LinearLayout ll_pb,ll_search_main;
//    View mLoadingView;
//    TextView tv_internet;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        context = SearchActivity.this;
//        myDB = new MyDB(context);
//        //Progress Dialog
//        mLoadingView = findViewById(R.id.pb_main);
//        ll_pb = findViewById(R.id.ll_pb);
//        tv_internet = findViewById(R.id.tv_internet_main);
//        ll_search_main = findViewById(R.id.ll_search_main);
//
//
//        /*new SearchViewFormatter()
//                .setSearchBackGroundResource(R.color.colorWhite)
//                .setSearchIconResource(R.drawable.search_colorful, true, false) //true to icon inside edittext, false to outside
//                .setSearchVoiceIconResource(R.drawable.closebtn)
//                .setSearchTextColorResource(R.color.colorGreen)
//                .setSearchHintColorResource(R.color.colorDarkGrey)
//                .setSearchCloseIconResource(R.drawable.closebtn)
//                .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
//                .format(mSearchView);*/
//
//        // Get search view and modify it to our needs
//        searchView = (SearchView) findViewById(R.id.search_view);
//        searchView.onActionViewExpanded();
//        //expanded = true;
//        //Utils.hideKeyboard(context);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search(searchView);
//                //Utils.showKeyboard(context);
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e(TAG,"onQueryTextSubmit = "+query);
//
//                return  false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.e(TAG,"onQueryTextChange = "+newText);
//                if(newText.length()>1)
//                {
//                    mRecyclerViewSearch.setVisibility(View.VISIBLE);
//                    mAdapterSearch.getFilter().filter(newText);
//                }
//                else
//                    mRecyclerViewSearch.setVisibility(View.GONE);
//
//
//                if(newText.equals(""))
//                    mRecyclerViewSearch.setVisibility(View.GONE);
//
//
//                return true;
//            }
//        });
//
//        int searchFrameId = searchView.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
//        View searchFrame = searchView.findViewById(searchFrameId);
//        searchFrame.setBackgroundResource(R.drawable.bg_white_background);
//
//        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
//        View searchPlate = findViewById(searchPlateId);
//        searchPlate.setBackgroundResource(R.drawable.bg_white_background);
//
//        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
//        View searchBar = findViewById(searchBarId);
//        searchBar.setBackgroundResource(R.drawable.bg_white_background);
//        searchView.setQueryHint("Search for a product");
//        //progress();
//        initRecyclerViewCategories();
//
//    }
//
//    //================     Main  Categories       ==============================================
//    private void initRecyclerViewCategories()
//    {
//        mRecyclerViewSearch = findViewById(R.id.card_recycler_view_search);
//        //Create new GridLayoutManager
//        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
//                1,//span count no of items in single row
//                GridLayoutManager.VERTICAL,//Orientation
//                false);//reverse scrolling of recyclerview
//        //set layout manager as gridLayoutManager
//
//        mRecyclerViewSearch.setLayoutManager(gridLayoutManager);
//        mRecyclerViewSearch.setNestedScrollingEnabled(false);
//        loadJSONTAllProducts();
//    }
//
//    //=========================================================================================================
//
//    private void loadJSONTAllProducts()
//    {
//        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
//            /*// Create a progressdialog
//            mProgressDialog = new ProgressDialog(mContext);
//            // Set progressdialog title
//            mProgressDialog.setTitle("Fetching Categories");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();*/
//
//            Retrofit retrofit = RetrofitClientGM.getInstance();
//
//            final Apis request =  retrofit.create(Apis.class);
//            Call<PPP> call = request.getAllProducts();
//            call.enqueue(new Callback<PPP>()
//            {
//                @Override
//                public void onResponse(@NonNull Call<PPP> call,
//                                       @NonNull Response<PPP> response) {
//
//                    PPP jsonResponse = response.body();
//                    /*Log.e("P JSON BODY  ===>>>>>", "" + response.body());
//                    assert jsonResponse != null;*/
//                    //mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getProduct()));
//                    //Log.e("P JSON RESPONSE ==>>>>>",""+mArrayList.get(0).getPname());
//
//                    assert jsonResponse != null;
//                    progress();
//
//
//                    mArrayListAllProducts = new ArrayList<>(jsonResponse.getProducts());
//                    mArrayListAllProductsFinal = new ArrayList<>();
//                    for (ProductsItem m: mArrayListAllProducts) {
//                        if(m.getDetail() != null && m.getDetail().size() > 0)
//                        {
//                            mArrayListAllProductsFinal.add(m);
//                        }
//                    }
//                    if(myDB.getProductList().size()!=0)
//                    {
//                        if(myDB.deleteProductsList())
//                            myDB.createProductList(mArrayListAllProductsFinal);
//                    }
//                    else
//                    {
//                        myDB.createProductList(mArrayListAllProductsFinal);
//                    }
//
//                    mAdapterSearch = new ___SearchAdapter(mArrayListAllProductsFinal);
//                    mRecyclerViewSearch.setAdapter(mAdapterSearch);
//                    mRecyclerViewSearch.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<PPP> call, @NonNull Throwable t)
//                {
//                    loadJSONTAllProducts();
//                    Log.e("Error", t.getMessage());
//                    //mProgressDialog.dismiss();
//                }
//            });
//
//        } else
//        {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//
//
//    /*@Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        // MenuItem search = menu.findItem(R.id.search);
//        return  true;
//
//        //==============================================================
//        // Inflate the menu; this adds items to the action bar if it is present.
//        *//*getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem search = menu.findItem(R.id.search);
//        search.setIcon(R.drawable.ic_media_search);
//        //   MenuItem cart = menu.findItem(R.id.cart);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
//        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_media_search));       // MenuItemCompat.getActionView(cart);
//
//        //searchView.setQueryHint("Search Here.....");
//        //getMenuInflater().inflate(R.menu.main,menu);
//        search(searchView);
//
//
//        //Log.e("<<<<<<<<<<<<<<< ","3");
//        return true;*//*
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//
//            case R.id.search_Main:
//                //Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show();
//
//                break;
//        }
//        return true;
//    }*/
//
//
//    private void search(SearchView searchView)
//    {
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                Log.e(TAG,"onQueryTextSubmit = "+query);
//                //mRecyclerViewSearch.setVisibility(View.VISIBLE);
//               // mAdapterSearch.getFilter().filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                Log.e(TAG,"onQueryTextChange = "+newText);
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    public void backButton(View view)
//    {
//        super.onBackPressed();
//    }
//
//
//    public void progress()
//    {
//
//        new Thread(new Runnable() {
//            public void run() {
//
//                while (!CheckNetwork.isInternetAvailable(getApplicationContext()))
//                {
//                    tv_internet.setVisibility(View.VISIBLE);
//                    // performing operation
//
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                // performing operation if file is downloaded,
//                if (CheckNetwork.isInternetAvailable(getApplicationContext())) {
//                    try {
//                        Thread.sleep(1000);
//
//
//                        SearchActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                if(mArrayListAllProducts.size()!=0)
//                                {
//                                    tv_internet.setVisibility(View.GONE);
//                                    mLoadingView.setVisibility(View.GONE);
//                                    ll_pb.setVisibility(View.GONE);
//                                    ll_search_main.setVisibility(View.VISIBLE);
//                                }
//
//
//                                //progressBar.dismiss();
////                        Thread.currentThread().stop();
//                            }
//
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    // close the progress bar dialog
//
//                }
//                else
//                {
//
//                }
//            }
//
//
//
//        }).start();
//    }
//}
