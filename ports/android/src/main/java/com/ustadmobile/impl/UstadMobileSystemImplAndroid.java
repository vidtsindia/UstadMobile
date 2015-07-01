package com.ustadmobile.impl;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Build;

/**
 * Created by mike on 07/06/15.
 */
public class UstadMobileSystemImplAndroid extends com.ustadmobile.impl.UstadMobileSystemImpl{

    private Activity currentActivity;

    private Context currentContext;

    public static final String PREFS_NAME = "ustadmobilePreferences";

    private List<UstadMobileDownloadJobAndroid> activeDownloads;

    public UstadMobileSystemImplAndroid() {
        activeDownloads = new ArrayList<UstadMobileDownloadJobAndroid>();
    }

    public void init() {
        File sharedContentDir = new File(getSharedContentDir());
        if(!sharedContentDir.exists() && sharedContentDir.isDirectory()) {
            sharedContentDir.mkdirs();
        }
    }

    public void setCurrentActivity(Activity activity) {

    }

    public void setCurrentContext(Context context) {
        this.currentContext = context;
    }

    protected Context getCurrentContext() {
        return this.currentContext;
    }

    @Override
    public String getSharedContentDir() {
        File extStorage = Environment.getExternalStorageDirectory();
        File ustadContentDir = new File(extStorage, "ustadmobileContent");
        return ustadContentDir.getAbsolutePath();
    }

    @Override
    public String getUserContentDirectory(String username) {
        File userDir = new File(Environment.getExternalStorageDirectory(),
            "ustadmobileContent/users/" + username);
        return null;
    }

    /**
     * Will return language_COUNTRY e.g. en_US
     *
     * @return
     */
    @Override
    public String getSystemLocale() {
        return Locale.getDefault().toString();
    }

    @Override
    public Hashtable getSystemInfo() {
        Hashtable ht = new Hashtable();
        ht.put("os", "Android");
        ht.put("osversion", Build.VERSION.RELEASE);
        ht.put("locale", this.getSystemLocale());

        return ht;
    }

    @Override
    public String readFileAsText(String filename, String encoding) throws IOException {
        IOException ioe = null;
        FileInputStream fin = null;
        ByteArrayOutputStream bout = null;

        try {
            fin = new FileInputStream(filename);
            bout = new ByteArrayOutputStream();
            int bytesRead = -1;
            byte[] buf = new byte[1024];
            while((bytesRead = fin.read(buf, 0, buf.length)) != -1) {
                bout.write(buf, 0, bytesRead);
            }
        }catch(IOException e) {
            ioe = e;
        }finally {
            if(fin != null) {
                fin.close();
            }
        }

        if(ioe == null) {
            String retVal = new String(bout.toByteArray(), encoding);
            return retVal;
        }else {
            throw ioe;
        }
    }

    @Override
    public int modTimeDifference(String fileURI1, String fileURI2) {
        return (int)(new File(fileURI2).lastModified() - new File(fileURI1).lastModified());
    }

    public long modTimeDifferenceLong(String fileURI1, String fileURI2) {
        return (int)(new File(fileURI2).lastModified() - new File(fileURI1).lastModified());
    }

    @Override
    public void writeStringToFile(String str, String fileURI, String encoding) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(str.getBytes(encoding));
        FileOutputStream fout = null;
        IOException ioe = null;
        try {
            int bytesRead = -1;
            byte[] buf = new byte[1024];
            fout = new FileOutputStream(fileURI);
            while((bytesRead = bin.read(buf))!= -1) {
                fout.write(buf, 0, bytesRead);
            }
        }catch(IOException e) {
            ioe = e;
        }finally{
            if(fout != null) {
                fout.close();
            }
        }

        if(ioe != null) {
            throw ioe;
        }
    }

    @Override
    public boolean fileExists(String fileURI) throws IOException {
        return new File(fileURI).exists();
    }

    @Override
    public boolean dirExists(String dirURI) throws IOException {
        File dir = new File(dirURI);
        return dir.exists() && dir.isDirectory();
    }

    @Override
    public void removeFile(String fileURI) throws IOException {
        File f = new File(fileURI);
        f.delete();
    }

    @Override
    public String[] listDirectory(String dirURI) throws IOException {
        File dir = new File(dirURI);
        return dir.list();
    }

    @Override
    public UMTransferJob downloadURLToFile(String url, String fileURI, Hashtable headers) {
        DownloadJob job = new DownloadJob(url, fileURI, this);

        return job;
    }

    @Override
    public void renameFile(String s, String s1) {

    }

    @Override
    public int fileSize(String s) {
        return 0;
    }

    @Override
    public void makeDirectory(String s) throws IOException {

    }

    @Override
    public void removeRecursively(String s) {

    }

    @Override
    public UMTransferJob unzipFile(String s, String s1) {
        return null;
    }

    @Override
    public void setActiveUser(String s) {

    }

    @Override
    public void setUserPref(String s, String s1) {

    }

    @Override
    public String getUserPref(String s, String s1) {
        return null;
    }

    @Override
    public String[] getPrefKeyList() {
        return new String[0];
    }

    @Override
    public void saveUserPrefs() {

    }

    public class DownloadJob implements UMTransferJob {

        private String srcURL;

        private UstadMobileSystemImplAndroid hostImpl;

        private long downloadID = -1;

        private Timer timerProgressUpdate = null;

        private String destFileURI;

        public static final int DOWNLOAD_PROGRESS_UPDATE_TIMEOUT = 1000;

        public DownloadJob(String srcURL, String destFileURI, UstadMobileSystemImplAndroid hostImpl) {
            this.hostImpl = hostImpl;
            this.srcURL = srcURL;
            this.destFileURI = destFileURI;
        }

        @Override
        public void start() {
            Context ctx = hostImpl.getCurrentContext();
            DownloadManager mgr = (DownloadManager)ctx.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.srcURL));

            File destFile = new File(destFileURI);
            String destStr = destFile.getAbsolutePath();
            request.setDestinationUri(Uri.fromFile(destFile));

            downloadID = mgr.enqueue(request);
        }

        public long getDownloadID() {
            return this.downloadID;
        }


        private void startProgressTracking(final DownloadJob job) {
            this.timerProgressUpdate = new Timer();
            timerProgressUpdate.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, DOWNLOAD_PROGRESS_UPDATE_TIMEOUT, DOWNLOAD_PROGRESS_UPDATE_TIMEOUT);
        }

        @Override
        public void addProgresListener(UMProgressListener umProgressListener) {

        }
    }

}