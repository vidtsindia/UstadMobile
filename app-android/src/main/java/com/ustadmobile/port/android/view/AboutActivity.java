package com.ustadmobile.port.android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.toughra.ustadmobile.R;
import com.ustadmobile.core.MessageIDConstants;
import com.ustadmobile.core.controller.AboutController;
import com.ustadmobile.core.controller.UstadController;
import com.ustadmobile.core.impl.UstadMobileSystemImpl;
import com.ustadmobile.core.view.AboutView;
import com.ustadmobile.port.android.util.UMAndroidUtil;
import com.ustadmobile.core.controller.ControllerReadyListener;

import java.util.Hashtable;

public class AboutActivity extends UstadBaseActivity implements AboutView, ControllerReadyListener {

    private AboutController mAboutController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setUMToolbar();
        setTitle(UstadMobileSystemImpl.getInstance().getString(MessageIDConstants.about));

        AboutController.makeControllerForView(new Hashtable(),
                this, this);
    }

    @Override
    public void controllerReady(UstadController controller, int flags) {
        mAboutController = (AboutController)controller;
        runOnUiThread(new Runnable() {
            public void run() {
                mAboutController.setUIStrings();
            }
        });
    }

    @Override
    public void setVersionInfo(final String versionInfo) {
        runOnUiThread(new Runnable(){
            public void run(){
                ((TextView)findViewById(R.id.about_version_text)).setText(versionInfo);
            }
        });
    }

    @Override
    public void setAboutHTML(final String aboutHTML) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((WebView)findViewById(R.id.about_html)).loadData(aboutHTML, "text/html", "UTF-8");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_leavecontainer:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
