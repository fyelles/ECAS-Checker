package com.oscd.ecas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.preference.PreferenceManager;

public class ECASCheckerActivity extends Activity {

	WebView myWebView;
	ProgressDialog dialog;
	String URL = "file:///android_asset/main.html";

	SharedPreferences prefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(initWebView());
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putBoolean("saved", false);
		editor.commit();

	}

	public void SaveJson(String json) {
		Log.d("JSON", json);
		if (json.matches("remember.*")) {
			Log.d("JSON", "I selected remember");

			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = prefs.edit();
			
			editor.putString("identifierType", GetMatch("identifierType", json));
			editor.putString("identifier", GetMatch("identifier", json));
			editor.putString("surname", GetMatch("surname", json));
			
			editor.putString("dobDay", GetMatch("dobDay", json));
			editor.putString("dobYear", GetMatch("dobYear", json));
			editor.putString("dobMonth", GetMatch("dobMonth", json));
		
			editor.putString("countryOfBirth", GetMatch("countryOfBirth", json));
			editor.putBoolean("saved", true);
			editor.commit();

		}
	}

	public String GetMatch(String match, String str) {
		String myreturn = "-1";
		String Regxp;
		if (match.equals("countryOfBirth")) {
			Regxp = match + "=(\\d+)";

		} else {
			Regxp = match + "=(.*?)\\&";
		}
		Log.d("Regxp ", Regxp);

		Pattern patt = Pattern.compile(Regxp);
		Matcher m = patt.matcher(str);
		if (m.find()) {
			myreturn = m.group(1);
		}
		return myreturn;

	}

	private WebView initWebView() {
		myWebView = new WebView(this);

		myWebView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (prefs.getBoolean("saved", false)) {
					// In here we know that the customer has saved
					
					Log.d("DEBUG SHARED", "Value is  set");
				if (url.toString().equals(URL)) {
					view.loadUrl("javascript:$('#idNumberLabel').val(\"5198-7383\");");
					view.loadUrl("javascript:$('#surnameLabel').val(\"Yelles\");");
					view.loadUrl("javascript:$(\"#idTypeLabel option[value='1']\").prop('selected',true);");

					// day of birth
					view.loadUrl("javascript:$(\"#dobDay option[value='16']\").prop('selected',true);");
					view.loadUrl("javascript:$(\"#dobYear option[value='1982']\").prop('selected',true);");
					view.loadUrl("javascript:$(\"#dobMonth option[value='05']\").prop('selected',true);");
					// place of birth
					view.loadUrl("javascript:$(\"#cobLabel option[value='131']\").prop('selected',true);");
				}
				
				
				} else {
					Log.d("DEBUG SHARED", "Value is NOT  set");
				}


			}

		});

		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setSavePassword(false);
		myWebView.getSettings().setPluginsEnabled(false);
		myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		myWebView.addJavascriptInterface(this, "activity");

		// Scale
		myWebView.setInitialScale(0);
		myWebView.setWebChromeClient(new MyJavaScriptChromeClient());

		// Define the JS-Java interaction object
		// mWebView.addJavascriptInterface(new JSImp(), "native_java");

		// load content
		myWebView.loadUrl(URL);

		return myWebView;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyJavaScriptChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			// handle Alert event, here we are showing AlertDialog
			new AlertDialog.Builder(ECASCheckerActivity.this)
					.setTitle("JavaScript Alert !")
					.setMessage(message)
					.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do your stuff
									result.confirm();
								}
							}).setCancelable(false).create().show();
			return true;
		}
	}

}