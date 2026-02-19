package com.appbuilder.template;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;

    // --- CONFIGURATION PLACEHOLDERS ---
    // These values are replaced by GitHub Actions during the build process.
    // DO NOT CHANGE THE VARIABLE NAMES OR DEFAULT VALUES MANUALLY HERE.
    
    // 1. The Website URL to load
    private static final String TARGET_URL = "https://google.com"; 

    // 2. User Agent Suffix (for analytics tracking)
    private static final String USER_AGENT_SUFFIX = "CUSTOM_UA_PLACEHOLDER"; 

    // 3. Toolbar Visibility Flag
    // The build script looks for "false; // FLAG_TOOLBAR_PLACEHOLDER"
    private static final boolean SHOW_TOOLBAR = false; // FLAG_TOOLBAR_PLACEHOLDER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 1. TOOLBAR CONFIGURATION ---
        if (!SHOW_TOOLBAR) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
             // Set the title to the App Name (defined in strings.xml)
             setTitle(getString(R.string.app_name));
        }

        // --- 2. WEBVIEW SETUP ---
        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        
        // Enable JavaScript (Required for almost all modern sites)
        webSettings.setJavaScriptEnabled(true);
        
        // Enable Local Storage (Required for login sessions, carts, etc.)
        webSettings.setDomStorageEnabled(true);
        
        // Optimize for Mobile
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        
        // Caching (Improve performance)
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // --- 3. USER AGENT CONFIGURATION ---
        // We append the custom suffix so the server knows it's the app
        if (!USER_AGENT_SUFFIX.equals("CUSTOM_UA_PLACEHOLDER")) {
            String defaultUA = webSettings.getUserAgentString();
            webSettings.setUserAgentString(defaultUA + " " + USER_AGENT_SUFFIX);
        }

        // --- 4. NAVIGATION LOGIC ---
        // Force links to open inside the WebView instead of Chrome
        myWebView.setWebViewClient(new WebViewClient());

        // Load the Target URL
        myWebView.loadUrl(TARGET_URL);

        // --- 5. PULL-TO-REFRESH ---
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
                // Stop the spinning animation after reload triggers
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // --- 6. BACK BUTTON HANDLING ---
    // If the user presses "Back", go back in browser history.
    // If no history, close the app.
    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
