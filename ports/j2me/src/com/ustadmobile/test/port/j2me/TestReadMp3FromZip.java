/*
    This file is part of Ustad Mobile.

    Ustad Mobile Copyright (C) 2011-2014 UstadMobile Inc.

    Ustad Mobile is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version with the following additional terms:

    All names, links, and logos of Ustad Mobile and Toughra Technologies FZ
    LLC must be kept as they are in the original distribution.  If any new
    screens are added you must include the Ustad Mobile logo as it has been
    used in the original distribution.  You may not create any new
    functionality whose purpose is to diminish or remove the Ustad Mobile
    Logo.  You must leave the Ustad Mobile logo as the logo for the
    application to be used with any launcher (e.g. the mobile app launcher).

    If you want a commercial license to remove the above restriction you must
    contact us.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    Ustad Mobile is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

 */
package com.ustadmobile.test.port.j2me;

import com.ustadmobile.core.impl.UstadMobileSystemImpl;
import com.ustadmobile.core.impl.ZipFileHandle;
import com.ustadmobile.port.j2me.app.FileUtils;
import com.ustadmobile.port.j2me.app.HTTPUtils;
import com.ustadmobile.port.j2me.app.ZipEPUBRequestHandler;
import com.ustadmobile.port.j2me.app.controller.UstadMobileAppController;
import com.ustadmobile.port.j2me.impl.UstadMobileSystemImplJ2ME;
import j2meunit.framework.TestCase;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.*;

/**
 *
 * @author varuna
 */
public class TestReadMp3FromZip extends TestCase {
    public TestReadMp3FromZip(){
        setName("Test Mp3 Read From Zip Test");
    }
    
    public void runTest() throws Throwable{
        assertEquals("Simple Test OK", 2, 1+1);
        
        //download and get Zip 
        String contentDir = 
                UstadMobileSystemImpl.getInstance().getSharedContentDir();
        String mp3ZipTestFile = FileUtils.joinPath(contentDir, "mp3.zip");
         if (!FileUtils.checkFile(mp3ZipTestFile)){
             //download the file
             String mp3ZipTestFileUrl = 
                     "http://umcloud1.ustadmobile.com/media/eXeUpload/mp3.zip";
             HTTPUtils.downloadURLToFile(mp3ZipTestFileUrl, contentDir, "");
             
         }
         
         if (!FileUtils.checkFile(mp3ZipTestFile)){
             assertTrue(false);
         }else{
             assertTrue(true);
         }
        
         //download and get mp3 
        String mp3TestFile = FileUtils.joinPath(contentDir, "bensound-cute.mp3");
        if (!FileUtils.checkFile(mp3TestFile)){
            //download the file
            String mp3TestFileUrl = 
           "http://umcloud1.ustadmobile.com/media/eXeUpload/bensound-cute.mp3";
            HTTPUtils.downloadURLToFile(mp3TestFileUrl, contentDir, "");

        }

        if (!FileUtils.checkFile(mp3TestFile)){
            assertTrue(false);
        }else{
            assertTrue(true);
        }
        
        String smallMp3TestFile = FileUtils.joinPath(contentDir, "53.mp3");
        if (!FileUtils.checkFile(smallMp3TestFile)){
            //download the file
            String smallMp3TestFileUrl =
                    "http://umcloud1.ustadmobile.com/media/eXeUpload/53.mp3";
            HTTPUtils.downloadURLToFile(smallMp3TestFileUrl, contentDir, "");
        }
        
        if (!FileUtils.checkDir(smallMp3TestFile)){
            assertTrue(false);
        }else{
            assertTrue(true);
        }
        

        String mp3EPUBTestFile = FileUtils.joinPath(contentDir, "forasportal.epub");
         if (!FileUtils.checkFile(mp3EPUBTestFile)){
             //download the file
             String mp3EPUBTestFileURL = 
                     "http://umcloud1.ustadmobile.com/media/eXeUpload/forasportal.epub";
             HTTPUtils.downloadURLToFile(mp3EPUBTestFileURL, contentDir, "");
             
         }
         
         if (!FileUtils.checkFile(mp3EPUBTestFile)){
             assertTrue(false);
         }else{
             HTTPUtils.httpDebug("forasportal.epub exists..");
             assertTrue(true);
         }

        HTTPUtils.httpDebug("Starting to play the mp3..");
        UstadMobileSystemImplJ2ME impl = (UstadMobileSystemImplJ2ME)UstadMobileSystemImpl.getInstance();
        
        String supportedTypes[] = Manager.getSupportedContentTypes(null);
        for (int i = 0; i < supportedTypes.length; i++) {
           if (supportedTypes[i].startsWith("audio")) {
              HTTPUtils.httpDebug("Device supports " + supportedTypes[i]);
           }
        }
   
        //Read it man
        InputStream is = null;
        is = UstadMobileAppController.getInputStreamReaderByURL(smallMp3TestFile);
        String contentType = 
                UstadMobileAppController.getContentType(smallMp3TestFile);
        
        InputStream isMp3Epub = null;
        ZipFileHandle mp3ZipFile = null;
        
        try{
            mp3ZipFile = impl.openZip(mp3EPUBTestFile);        
            isMp3Epub = mp3ZipFile.openInputStream("EPUB/53.mp3");


            impl.playMedia(is, contentType);
            HTTPUtils.httpDebug("the mp3 is playing?");

            Thread.sleep(15000); //sleep for 15 seconds.
        }catch(Exception e){
            HTTPUtils.httpDebug("Problem in zip stuff");
            HTTPUtils.httpDebug(e.getMessage() + " : " + e.toString());
            
        }finally{
            if(isMp3Epub != null){
                isMp3Epub.close();
            }
            if (mp3ZipFile != null){
                mp3ZipFile.close();
            }
            if (is != null){
                is.close();
            }
        }
        
        assertTrue(true);
        
        
         
         
    }
}