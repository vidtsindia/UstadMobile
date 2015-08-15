/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustadmobile.app.tests;

import com.sun.lwuit.Form;
import com.ustadmobile.app.forms.TestForm3;
import com.ustadmobile.app.forms.TestForm4;
import j2meunit.framework.TestCase;


/**
 *
 * @author varuna
 */
public class TestCustomLWUIT extends TestCase {
    public TestCustomLWUIT(){
        setName("Test Custom LWUIT Test");
    }
    
    public void runTest() throws Throwable{
        assertEquals("Simple Test OK", 2, 1+1);
        TestForm4 form = new TestForm4();
        form.show();
        form.setTitle("Custom LWUIT Test..");
        int count = 0;
        while(count < 101){
            //update progress
            form.setTitle("Custom LWUIT Test ("+count+"%)..");
            count = count + 10;
            try {
                form.updateProgress(count);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        Thread.sleep(1000);
        assertTrue("Completed first callSerially and wait", true);
    }
}