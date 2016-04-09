package com.methodica.lizalinto.dummyinbox;



import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import com.methodica.lizalinto.dummyinbox.NewsContent.NewsItem;
public class NewsFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    OnNewsFragmentInteractionListener mListener;
    private NewsRecyclerViewAdapter mRecyclerAdapter;

    public NewsFragment() {
        // Required empty public constructor
        //setRetainInstance(true);
    }
    public static Fragment newInstance(){

        return new NewsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewsFragmentInteractionListener) {
            mListener = (OnNewsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
         // Set the adapter
            if (view instanceof RecyclerView) {

                Context context = view.getContext();
                mRecyclerView = (RecyclerView) view;
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                //NewsContent.addItem("1", "Apple", "This is content", "Liza", "today", "http://...", null, null);
                mRecyclerAdapter = new NewsRecyclerViewAdapter(NewsContent.ITEM_MAP, mListener);
                mRecyclerView.setAdapter(mRecyclerAdapter);
            }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null) {
            // ------Showing progress dialog-------
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            String stringUrl = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=android";
            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadNewsTask().execute(stringUrl, "-1");
            } else {
                Log.d("NetworkingError1", "No network");
            }
        }

    }

    private Object downloadNews(String URL, String mode) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("NetworkingResponse", "The response is: " + response);
            is = conn.getInputStream();

            if (Objects.equals(mode, "image")){// image decode
                return BitmapFactory.decodeStream(is);
            }else {
                // Convert the InputStream into a string
                return readIt(is);

            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException {

        Reader reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder result = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null) {

            result.append(line);
        }
        return result.toString();
    }
    private class DownloadNewsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            NewsContent.ITEMS.clear();
            NewsContent.ITEM_MAP.clear();
            mProgressDialog.setMessage("Downloading News.Please wait...");
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            // params comes from the execute() call: params[0] is the url.
            InputStream is = null;
            try {
                String result = (String) downloadNews(strings[0], strings[1]);

                Log.d("Result", result);

                parseGoogleNewsApiJson(result);
            } catch (IOException e) {
                Log.d("NetworkError", "Unable to retrieve web page. URL may be invalid.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            if(NewsContent.ITEMS.size() > 0) {

                mRecyclerAdapter.notifyDataSetChanged();

            }else{//Google news response is null; So reading from raw Json file stored in raw resource folder

                InputStream is = getResources().openRawResource(R.raw.google_news_sample);
               try {
                    String read = readIt(is);
                    parseGoogleNewsApiJson(read);
                    mRecyclerAdapter.notifyDataSetChanged();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            hideProgressDialog();

            new DownloadNewsImageTask().execute();
        }
    }

    private void parseGoogleNewsApiJson(String result) throws JSONException {

        JSONObject jsonResponseData= new JSONObject(result);
        JSONObject jsonResultsObject = jsonResponseData.getJSONObject("responseData");
        JSONArray jsonResultsArray = jsonResultsObject.getJSONArray("results");

        for (int i =0; i < jsonResultsArray.length(); i++) {

            JSONObject jsonNewsObject = jsonResultsArray.getJSONObject(i);
            String title = jsonNewsObject.getString("titleNoFormatting");
            String content = jsonNewsObject.getString("content");
            String publisher = jsonNewsObject.getString("publisher");
            String published_date = jsonNewsObject.getString("publishedDate");
            String unescapedUrl = jsonNewsObject.getString("unescapedUrl");
            JSONObject imageObject = jsonNewsObject.getJSONObject("image");
            String imageUrl = imageObject.getString("url");

            NewsContent.addItem(String.valueOf(i + 1), title, content, publisher, published_date, unescapedUrl, imageUrl, null);

        }

    }
    private void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private class DownloadNewsImageTask extends AsyncTask<Void, String, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressDialog.setMessage("Downloading Images.Please wait...");
            //showProgressDialog();
        }
        @Override
        protected Void doInBackground(Void... urls) {

            try {
                for(int i=0; i< NewsContent.ITEMS.size(); i++){
                    publishProgress(String.valueOf(i+1)+"/" + String.valueOf(NewsContent.ITEMS.size()));

                    Bitmap bm = (Bitmap) downloadNews(NewsContent.ITEM_MAP.get(String.valueOf(i+1)).imageUrl,"image");
                    NewsContent.ITEM_MAP.get(String.valueOf(i+1)).setImage(bm);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progressMessage) {
            mProgressDialog.setMessage("Downloaded " + progressMessage[0] + "images");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRecyclerAdapter.notifyDataSetChanged();
           // hideProgressDialog();
        }
    }

    public interface OnNewsFragmentInteractionListener {

         void OnNewsFragmentInteraction(NewsItem mItem);
    }
}
