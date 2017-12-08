package edu.gvsu.cis.activityapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.gvsu.cis.activityapp.R;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        String urlString = getIntent().getStringExtra("URL");
        if (urlString != null) {
            webview.loadUrl(urlString);
        }
    }
}
