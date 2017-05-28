package com.ustadmobile.port.sharedse.networkmanager;

import com.ustadmobile.core.controller.CatalogController;
import com.ustadmobile.core.controller.CatalogEntryInfo;
import com.ustadmobile.core.impl.AcquisitionManager;
import com.ustadmobile.core.impl.UstadMobileSystemImpl;
import com.ustadmobile.core.opds.UstadJSOPDSEntry;
import com.ustadmobile.core.opds.UstadJSOPDSFeed;
import com.ustadmobile.core.util.UMFileUtil;
import com.ustadmobile.core.util.UMIOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ustadmobile.port.sharedse.networkmanager.BluetoothServer.CMD_SEPARATOR;
import static com.ustadmobile.port.sharedse.networkmanager.NetworkManager.DOWNLOAD_FROM_CLOUD;
import static com.ustadmobile.port.sharedse.networkmanager.NetworkManager.DOWNLOAD_FROM_PEER_ON_DIFFERENT_NETWORK;
import static com.ustadmobile.port.sharedse.networkmanager.NetworkManager.DOWNLOAD_FROM_PEER_ON_SAME_NETWORK;
import static com.ustadmobile.port.sharedse.networkmanager.NetworkManager.NOTIFICATION_TYPE_ACQUISITION;

/**
 * Created by kileha3 on 09/05/2017.
 */

public class AcquisitionTask extends NetworkTask implements BluetoothConnectionHandler,NetworkManagerListener{

    private UstadJSOPDSFeed feed;
    protected NetworkManagerTaskListener listener;
    private static final int FILE_DESTINATION_INDEX=1;
    private static final int FILE_DOWNLOAD_URL_INDEX=0;
    private static final int DOWNLOAD_TASK_UPDATE_TIME=500;
    private int currentEntryAcquisitionTaskStatus =-1;
    private int currentEntryIdIndex;
    private static final String INFO_FILE_EXTENSION=".dlinfo";
    private static final String UNFINISHED_FILE_EXTENSION=".part";
    private static final String HEADER_RANGE="Range";
    private static final String HEADER_LAST_MODIFIED ="Last-Modified";
    private static final String DOWNLOAD_REQUEST_METHOD="GET";
    private String currentGroupIPAddress;
    private String currentGroupSSID;
    private static long TIME_PASSED_FOR_PROGRESS_UPDATE = Calendar.getInstance().getTimeInMillis();
    private long currentDownloadId=0L;
    private long totalBytesDownloadedSoFar =0L;
    private long totalBytesToDownload =0L;
    private Timer updateTimer=null;
    private Timer retryTimer=null;
    private Thread entryAcquisitionThread =null;
    private String message=null;
    private EntryCheckResponse entryCheckResponse;
    private static final int MAXIMUM_RETRY_COUNT = 5;
    private static final int WAITING_TIME_BEFORE_RETRY =10 * 1000;
    private long TIME_PASSED_FOR_DOWNLOAD_RETRY=0L;
    private int retryCount=0;
    private boolean isEntryAcquisitionTaskCancelled =false;

    /**
     * Monitor file acquisition task progress and report it.
     */
    private TimerTask updateTimerTask =new TimerTask() {
        @Override
        public void run() {
            if(totalBytesToDownload>0L){
                int progress=(int)((totalBytesDownloadedSoFar*100)/ totalBytesToDownload);
                long currentTime = Calendar.getInstance().getTimeInMillis();
                int progressLimit=100;
                if(((currentTime - TIME_PASSED_FOR_PROGRESS_UPDATE) < DOWNLOAD_TASK_UPDATE_TIME) ||
                        (progress < 0 && progress > progressLimit)) {
                    return;
                }
                TIME_PASSED_FOR_PROGRESS_UPDATE = currentTime;
                currentEntryAcquisitionTaskStatus =UstadMobileSystemImpl.DLSTATUS_RUNNING;
                networkManager.updateNotification(NOTIFICATION_TYPE_ACQUISITION,progress,
                        getFeed().entries[currentEntryIdIndex].title,message);

            }
        }
    };

    private TimerTask retryEntryDownload=new TimerTask() {
        @Override
        public void run() {
            long timeToRetry=Calendar.getInstance().getTimeInMillis();
            if(retryCount > MAXIMUM_RETRY_COUNT && (timeToRetry-TIME_PASSED_FOR_DOWNLOAD_RETRY) < WAITING_TIME_BEFORE_RETRY
                    && currentEntryAcquisitionTaskStatus !=UstadMobileSystemImpl.DLSTATUS_RETRYING){
                return;
            }
            retryCount++;
            acquireNextFile();
        }
    };

    public AcquisitionTask(UstadJSOPDSFeed feed,NetworkManager networkManager){
        super(networkManager);
        this.feed=feed;
        networkManager.addNetworkManagerListener(this);
    }

    /**
     * Start file acquisition task
     */
    public synchronized void start() {
        currentEntryIdIndex =0;
        synchronized (this){
            if(updateTimer==null){
                updateTimer=new Timer();
                updateTimer.scheduleAtFixedRate(updateTimerTask,DOWNLOAD_TASK_UPDATE_TIME,
                        DOWNLOAD_TASK_UPDATE_TIME);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                acquireNextFile();
            }
        }).start();
    }

    /**
     * Determine where to download the file from
     * (Cloud, Peer on the same network or Peer on different network)
     *
     * if is from Cloud: initiate download task
     *            Peer on the same network: initiate download task
     *            Peer on different network: initiate bluetooth connection in order to trigger
     *            WiFi-Direct group creation on the host device
     */
    private void acquireNextFile(){
      currentEntryAcquisitionTaskStatus =UstadMobileSystemImpl.DLSTATUS_PENDING;
        //TODO: Move hardcoded strings to locale constants
        message=getFeed().entries.length>1 ? "Downloading "+(currentEntryIdIndex+1)+" of "
                +getFeed().entries.length+" files":"Downloading file";
        networkManager.addNotification(NOTIFICATION_TYPE_ACQUISITION,getFeed().entries[currentEntryIdIndex].title,message);
        if(currentEntryIdIndex < feed.entries.length) {
            currentDownloadId= new AtomicInteger().incrementAndGet();
            String entryId = feed.entries[currentEntryIdIndex].id;
            entryCheckResponse=networkManager.getEntryResponseWithLocalFile(entryId);

            if(entryCheckResponse != null){
                if(Calendar.getInstance().getTimeInMillis() - entryCheckResponse.getNetworkNode().getNetworkServiceLastUpdated() < NetworkManager.ALLOWABLE_DISCOVERY_RANGE_LIMIT){
                    networkManager.handleFileAcquisitionInformationAvailable(entryId,currentDownloadId,
                            DOWNLOAD_FROM_PEER_ON_SAME_NETWORK);
                    String fileURI="http://"+entryCheckResponse.getNetworkNode().getDeviceIpAddress()+":"
                            +entryCheckResponse.getNetworkNode().getPort()+"/catalog/entry/"+entryId;
                    downloadCurrentFile(fileURI);
                }else{
                    networkManager.handleFileAcquisitionInformationAvailable(entryId,currentDownloadId,
                            DOWNLOAD_FROM_PEER_ON_DIFFERENT_NETWORK);
                    networkManager.connectBluetooth(entryCheckResponse.getNetworkNode().getDeviceBluetoothMacAddress()
                            ,this);
                }
            }else{
                networkManager.handleFileAcquisitionInformationAvailable(entryId,
                        currentDownloadId,DOWNLOAD_FROM_CLOUD);
                downloadCurrentFile(getFileURIs()[FILE_DOWNLOAD_URL_INDEX]);
            }

        }else{
            networkManager.handleTaskCompleted(this);
        }
    }


    private void downloadCurrentFile(final String fileUrl) {
        entryAcquisitionThread =new Thread(new Runnable() {
            @Override
            public void run() {

                File fileDestination = new File(getFileURIs()[FILE_DESTINATION_INDEX],
                        UMFileUtil.getFilename(fileUrl) + UNFINISHED_FILE_EXTENSION);


                try {
                    ResumableHttpDownload httpDownload=new ResumableHttpDownload("https://wolperapp.hartmanntrader.com/files/videos/Video_Test.mp4",fileDestination.getAbsolutePath());
                    boolean isDownloadCompleted=httpDownload.download();
                    if(isDownloadCompleted){
                        currentEntryAcquisitionTaskStatus =UstadMobileSystemImpl.DLSTATUS_SUCCESSFUL;

                        if(currentEntryIdIndex==(getFeed().entries.length-1)){
                            networkManager.removeNotification(NOTIFICATION_TYPE_ACQUISITION);
                            if(updateTimer!=null){
                                updateTimer.cancel();
                                updateTimerTask.cancel();
                                updateTimerTask =null;
                                updateTimer=null;
                            }

                            if(retryTimer!=null){
                                retryEntryDownload.cancel();
                                retryTimer.cancel();
                                retryEntryDownload=null;
                                retryTimer=null;
                            }
                        }else{
                            networkManager.updateNotification(NOTIFICATION_TYPE_ACQUISITION,0,
                                    getFeed().entries[currentEntryIdIndex].title,message);
                        }
                        totalBytesToDownload=0L;
                        totalBytesDownloadedSoFar=0L;
                        currentEntryIdIndex++;
                        entryAcquisitionThread =null;

                        acquireNextFile();
                    }
                } catch (IOException e) {
                    currentEntryAcquisitionTaskStatus=UstadMobileSystemImpl.DLSTATUS_FAILED;
                    if(updateTimer!=null){
                        updateTimer.cancel();
                        updateTimerTask.cancel();
                        updateTimerTask =null;
                        updateTimer=null;
                    }
                }
               if(currentEntryAcquisitionTaskStatus==UstadMobileSystemImpl.DLSTATUS_FAILED && retryCount < MAXIMUM_RETRY_COUNT){
                   TIME_PASSED_FOR_DOWNLOAD_RETRY=Calendar.getInstance().getTimeInMillis();
                   synchronized (this){
                       if(retryTimer==null){
                           currentEntryAcquisitionTaskStatus=UstadMobileSystemImpl.DLSTATUS_RETRYING;
                           retryTimer=new Timer();
                           retryTimer.scheduleAtFixedRate(retryEntryDownload,WAITING_TIME_BEFORE_RETRY,
                                   WAITING_TIME_BEFORE_RETRY);
                       }
                   }
               }else{
                   if(!isEntryAcquisitionTaskCancelled &&
                           currentEntryAcquisitionTaskStatus==UstadMobileSystemImpl.DLSTATUS_FAILED){
                       cancel();
                   }
               }

            }
        });
        entryAcquisitionThread.start();
    }

    /**
     * Get file download absolute URI's (Download destination & File URL)
     * @return
     */
    private String []  getFileURIs(){
        Vector downloadDestVector = getFeed().getLinks(AcquisitionManager.LINK_REL_DOWNLOAD_DESTINATION,
                null);
        if(downloadDestVector.isEmpty()) {
            throw new IllegalArgumentException("No download destination in acquisition feed for acquireCatalogEntries");
        }
        File downloadDestDir = new File(((String[])downloadDestVector.get(0))[UstadJSOPDSEntry.LINK_HREF]);
        String[] selfLink = getFeed().getAbsoluteSelfLink();

        if(selfLink == null)
            throw new IllegalArgumentException("No absolute self link on feed - required to resolve links");

        String feedHref = selfLink[UstadJSOPDSEntry.LINK_HREF];
        String [] acquisitionLink=getFeed().entries[currentEntryIdIndex].getFirstAcquisitionLink(null);
        return new String[]{UMFileUtil.resolveLink(feedHref,acquisitionLink[UstadJSOPDSEntry.LINK_HREF]),
                downloadDestDir.getAbsolutePath()};
    }



    @Override
    public void onConnected(final InputStream inputStream, final OutputStream outputStream) {
        String acquireCommand = BluetoothServer.CMD_ACQUIRE_ENTRY +" "+networkManager.getDeviceIPAddress()+"\n";
        String response=null;
        try {
            outputStream.write(acquireCommand.getBytes());
            outputStream.flush();
            System.out.print("AcquisitionTask: Sending Command "+acquireCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            response = reader.readLine();
            if(response.startsWith(BluetoothServer.CMD_ACQUIRE_ENTRY_FEEDBACK)) {
                System.out.print("AcquisitionTask: Receive Response "+response);
                String [] groupInfo=response.substring((BluetoothServer.CMD_ACQUIRE_ENTRY_FEEDBACK.length()+1),
                        response.length()).split(CMD_SEPARATOR);
                currentGroupIPAddress =groupInfo[2].replace("/","");
                currentGroupSSID =groupInfo[0];
                networkManager.connectWifi(groupInfo[0],groupInfo[1]);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                UMIOUtils.closeInputStream(inputStream);
                UMIOUtils.closeOutputStream(outputStream);
                networkManager.disconnectBluetooth();
            }
        }
    }

    @Override
    public void cancel() {
        entryAcquisitionThread.interrupt();
        if(entryAcquisitionThread.isInterrupted()){
            isEntryAcquisitionTaskCancelled =true;
            currentEntryAcquisitionTaskStatus =UstadMobileSystemImpl.DLSTATUS_FAILED;
            networkManager.removeNotification(NOTIFICATION_TYPE_ACQUISITION);
            if(updateTimer!=null){
                updateTimer.cancel();
                updateTimerTask.cancel();
                updateTimerTask =null;
                updateTimer=null;
            }
            if(retryTimer!=null){
                retryEntryDownload.cancel();
                retryTimer.cancel();
                retryEntryDownload=null;
                retryTimer=null;
            }
        }
    }

    @Override
    public int getQueueId() {
        return this.queueId;
    }

    @Override
    public int getTaskId() {
        return this.taskId;
    }

    @Override
    public int getTaskType() {
        return this.taskType;
    }

    public UstadJSOPDSFeed getFeed() {
        return feed;
    }

    public void setFeed(UstadJSOPDSFeed feed) {
        this.feed = feed;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AcquisitionTask && getFeed().equals(this.feed);
    }

    @Override
    public void fileStatusCheckInformationAvailable(List<String> fileIds) {

    }

    @Override
    public void entryStatusCheckCompleted(NetworkTask task) {

    }

    @Override
    public void networkNodeDiscovered(NetworkNode node) {

    }

    @Override
    public void networkNodeUpdated(NetworkNode node) {

    }

    @Override
    public void fileAcquisitionInformationAvailable(String entryId, long downloadId, int downloadSource) {

    }

    @Override
    public void wifiConnectionChanged(String ssid) {
        if(currentGroupSSID.equals(ssid)){
            String fileUrl="http://"+ currentGroupIPAddress +":"+
                    entryCheckResponse.getNetworkNode().getPort()+"/catalog/entry/"
                    +getFeed().entries[currentEntryIdIndex].id;
            downloadCurrentFile(fileUrl);
        }
    }

}
