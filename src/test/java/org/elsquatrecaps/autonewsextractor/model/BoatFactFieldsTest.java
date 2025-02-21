package org.elsquatrecaps.autonewsextractor.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author josep
 */
public class BoatFactFieldsTest {
    
    public static void main(String[] args){
        setUpClass();
        BoatFactFieldsTest test = new BoatFactFieldsTest();
        
        test.setUp();
        test.testGetChangeOfVersions();
        test.tearDown();
                
        tearDownClass();
    }
    
    public BoatFactFieldsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getChangeOfVersions method, of class BoatFactFields.
     */
    @Test
    public void testGetChangeOfVersions() {
        System.out.println("getVersionInfo");
        String result = BoatFactFields.getFieldInformation();
        System.out.println("INFO");
        System.out.println("____");
        System.out.println(result);
    }
}
