package com.oscd.ecas;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class JavaScriptInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    JavaScriptInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    
    public String getResponse()
    {
        String jsonResponse = null;
        //call Android APIs to get contacts
        //serialize to JSON and assign to jsonResponse
        Log.d("DEBUG", jsonResponse);
        return jsonResponse;
    }

    
    
}

