/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ustadmobile.app.tests;
import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;

/**
 *
 * @author varuna
 */
public class AllTestCases extends TestCase {
    
    public AllTestCases(){
        setName("All Test Caes");
    }

    public Test suite() {
        
        try {
            TestUtils.getInstance().loadTestSettingsResource();
            //TestUtils.loadTestSettingsFile();
            System.out.println("Test Settings gotten!");
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Could not load TestSettings.");
            throw new RuntimeException(e.toString());
        }
        
        System.out.println("Starting Tests..");
        System.out.println("--Testing : " + 
                TestUtils.testSettings.get("appDataURI"));
        TestSuite allTestSuite = new TestSuite("AlltestSuites");
        
        allTestSuite.addTest(new TestFormShow());

        
        allTestSuite.addTest(new TestSimple());
        allTestSuite.addTest(new TestXmlParse());
        allTestSuite.addTest(new TestOPDSParse());
        allTestSuite.addTest(new TestOPFParse());
        allTestSuite.addTest(new TestDownloadURLToFile());
        allTestSuite.addTest(new TestRMS());
        allTestSuite.addTest(new TestSerializedHashtable());
        //allTestSuite.addTest(new TestRename());
        allTestSuite.addTest(new TestSystemimplJ2ME());
        allTestSuite.addTest(new TestUnzip());
        //allTestSuite.addTest(new TestBigUnzip());
        allTestSuite.addTest(new TestAppPref());
        allTestSuite.addTest(new TestUserPref());
        
        allTestSuite.addTest(new TestLoginView());
        allTestSuite.addTest(new TestCatalogView());
        
        return allTestSuite;

    }
}