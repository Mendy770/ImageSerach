package com.ynachon.gridimagesearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {
	
	private final String googleImagesUrl = "https://ajax.googleapis.com/ajax/services/search/images?";
	private final String googleImagesVersion = "1.0";
	private final int resultsLimit = 8;
	private final int maxPageLimit = 8;
	
	private final int SETTINGS_ACTIVITY_REQUEST_CODE = 20;
	
	int currentResultsIndex = 0;
	
	HashMap<String, String> queryParameters = new HashMap<String,String>() {{
	    put("imageSize", "");
	    put("colorFilter", "");
	    put("imageType", "");
	    put("siteFilter", "");
	    put("v", googleImagesVersion);
	    put("rsz", Integer.toString(resultsLimit));
	    put("start", Integer.toString(currentResultsIndex));
	    put("q", "");
	}};
	
	
	EditText etQuery = null;
	Button btnSearch = null;
	GridView gvResults = null;
	Button btnLoadMore = null;
	ArrayList<ImageResult> results = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        initialize();
        imageAdapter = new ImageResultArrayAdapter(this, results);
        gvResults.setAdapter(imageAdapter);
        gvResults.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
        		Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
        		ImageResult imageResult = results.get(position);
        		i.putExtra("result", imageResult);
        		startActivity(i);
        	}
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {
    		
    		queryParameters.put("imageSize", data.getExtras().getString("imageSize").toString());
    		queryParameters.put("imageType", data.getExtras().getString("imageType").toString());
    		queryParameters.put("colorFilter", data.getExtras().getString("colorFilter").toString());
    		queryParameters.put("siteFilter", data.getExtras().getString("siteFilter").toString());
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }
    
    public void onSettingsButtonClick(MenuItem mi) {
    	Intent i = new Intent(ImageSearchActivity.this, SettingsActivity.class);
    	startActivityForResult(i, SETTINGS_ACTIVITY_REQUEST_CODE);
    }
    
    private void initialize() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	btnSearch = (Button) findViewById(R.id.btnSearch);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
    }
    
    public void onSearchButtonClick(View v) {
    	currentResultsIndex = 0;
    	results.clear();
    	getResults();
    }
    
    public void onLoadMoreButtonClick(View v) {
    	getResults();
    }
    
    private void getResults() {
    	if (maxPageLimit <= currentResultsIndex) {
    		Toast.makeText(getApplicationContext(), R.string.page_request_exceeding_warning, Toast.LENGTH_LONG).show();
    		return;
    	}
    	String query = etQuery.getText().toString();
    	AsyncHttpClient client = new AsyncHttpClient();
    	String testUrl = generateUrl(query);
    	Log.d("DEBUG", testUrl);
    	client.get(generateUrl(query), 
    			new JsonHttpResponseHandler() {
    		
    			public void onSuccess(JSONObject response) {
    				JSONArray imageJsonResults = null;
    				try {
    					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
    					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
    				
    				if (results.size() > 0) {
    		    		gvResults.smoothScrollToPosition(results.size() - 1);
    		    	}
    			}
    		
    	});
    }
    
    private String generateUrl(String query) {
    	String params = generateParamsString(query);
    	return googleImagesUrl + params;
    }
    
    private String generateParamsString(String query) {
    	String params = "";
    	queryParameters.put("start", Integer.toString(currentResultsIndex * resultsLimit));
    	queryParameters.put("q", Uri.encode(query));
    	Iterator it = queryParameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if (pairs.getKey() != null && pairs.getValue() != null && pairs.getValue() != "") {
            	params += pairs.getKey() + "=" + pairs.getValue() + "&";
            }
        }
        currentResultsIndex++;
        return params;
    }
    
}
