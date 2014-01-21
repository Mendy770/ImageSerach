package com.ynachon.gridimagesearch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	
	Spinner spnrImageSize = null;
	Spinner spnrColorFilter = null;
	Spinner spnrImageType = null;
	EditText etSiteFilter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.putExtra("imageSize", spnrImageSize.getSelectedItem().toString());
		i.putExtra("colorFilter", spnrColorFilter.getSelectedItem().toString());
		i.putExtra("imageType", spnrImageType.getSelectedItem().toString());
		i.putExtra("siteFilter", etSiteFilter.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
	
	private void initialize() {
		spnrImageSize = (Spinner) findViewById(R.id.spnrImageSize);
		spnrColorFilter = (Spinner) findViewById(R.id.spnrColorFilter);
		spnrImageType = (Spinner) findViewById(R.id.spnrImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
	}
}
