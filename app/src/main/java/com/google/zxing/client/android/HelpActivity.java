package com.google.zxing.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.example.user.lip.R;

/**
 * An HTML-based help screen.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class HelpActivity extends Activity {

  private static final String BASE_URL =
      "file:///android_asset/html-" + LocaleManager.getTranslatedAssetLanguage() + '/';

  private WebView webView;

  @Override
  protected void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.help);

    webView = (WebView) findViewById(R.id.help_contents);

    if (icicle == null) {
      webView.loadUrl(BASE_URL + "index.html");
    } else {
      webView.restoreState(icicle);
    }
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
      webView.goBack();
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onSaveInstanceState(Bundle icicle) {
    super.onSaveInstanceState(icicle);
    webView.saveState(icicle);
  }
}
