package info.androidhive.bottomnavigation.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.bottomnavigation.Movie;
import info.androidhive.bottomnavigation.app.MyApplication;
import info.androidhive.bottomnavigation.R;

public class StoreFragment extends Fragment {

    private static final String TAG = StoreFragment.class.getSimpleName();

    // url to fetch shopping items
    private static final String URLHIVE = "https://api.androidhive.info/json/movies_2017.json";

    private String URLDOMINIO = "https://api.themoviedb.org/3/account/{account_id}/favorite/movies";
    private String KEY = "?api_key=516adf1e1567058f8ecbf30bf2eb9378&session_id=c8e2bd462c246b141fd19ddba2e5f091f6f8aedb&language=en-US&sort_by=created_at.asc&page=1";
    private String SESION_ID = "&session_id=c8e2bd462c246b141fd19ddba2e5f091f6f8aedb&language=en-US&sort_by=created_at.asc&page=1";
    private String LANGUAGE_SORT = "&language=en-US&sort_by=created_at.asc&page=1";
    private String PAGENUMBER = "&page=";
    private int NUMEROPAGINA;
    private String URL = URLDOMINIO+KEY+SESION_ID+LANGUAGE_SORT+PAGENUMBER;
    //https://api.themoviedb.org/3/account/{account_id}/favorite/movies?api_key=516adf1e1567058f8ecbf30bf2eb9378&session_id=c8e2bd462c246b141fd19ddba2e5f091f6f8aedb&language=en-US&sort_by=created_at.asc&page=1
    //https://api.themoviedb.org/3/list/15570?api_key=516adf1e1567058f8ecbf30bf2eb9378&language=en-US
    private RecyclerView recyclerView;
    private List<Movie> itemsList;
    private StoreAdapter mAdapter;
    private boolean aptoParaCargar;
    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        itemsList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), itemsList);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);
        //final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {

                    if (aptoParaCargar) {

                        aptoParaCargar=false;
                        NUMEROPAGINA++;
                        fetchStoreItems(NUMEROPAGINA);
                    }

                }
            }
        });
        NUMEROPAGINA=1;
        fetchStoreItems(NUMEROPAGINA);

        return view;
    }


    /**
     *  JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
         url, (String) null,
         new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
      */
    /**
     * fetching shopping item by making http call
     */
    private void fetchStoreItems(int NUMEROPAGINA) {
        //itemsList.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL+NUMEROPAGINA, (String) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        aptoParaCargar = true;
                        JSONArray jRoutes = null;
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            jRoutes = response.getJSONArray("results");
                            for (int i = 0; i < jRoutes.length(); i++) {
                                JSONObject jsonObject = jRoutes.getJSONObject(i);
                                Movie movie = new Movie();
                                //String titleIn= (jsonObject.getString("title"));
                                //String title = ((jsonObject.get!=null)? jsonObject.getString("title") : jsonObject.getString("name"));
                                //String title = ("title" || "name");
                                movie.setTitle(jsonObject.getString("title"));

                                movie.setImage("https://image.tmdb.org/t/p/original"+jsonObject.getString("poster_path"));
                                movie.setPrice(jsonObject.getString("release_date"));
                                itemsList.add(movie);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                        //}.getType());

                        //itemsList.clear();
                        //itemsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */
    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Movie> movieList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.title);
                price = view.findViewById(R.id.price);
                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }


        public StoreAdapter(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Movie movie = movieList.get(position);
            holder.name.setText(movie.getTitle());
            holder.price.setText(movie.getPrice());

            RequestOptions requestOptions = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.film_reel)
                    .centerInside()
                    .error(R.drawable.cinema_filled_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();

            Glide.with(context)
                    .load(movie.getImage())
                    .apply(requestOptions)
                    .into(holder.thumbnail);

        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }
}
