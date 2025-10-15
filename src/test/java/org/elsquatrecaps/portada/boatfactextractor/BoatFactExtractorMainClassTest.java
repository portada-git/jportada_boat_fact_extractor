package org.elsquatrecaps.portada.boatfactextractor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author josep
 */
public class BoatFactExtractorMainClassTest {
    
     public static void main(String[] args){
        setUpClass();
        BoatFactExtractorMainClassTest test = new BoatFactExtractorMainClassTest();
        
        test.setUp();
        test.testMain();
        test.tearDown();
                
        tearDownClass();
    }
    
    public BoatFactExtractorMainClassTest() {
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
     * Test of main method, of class BoatFactExtractorMainClass.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = {
//            "cut_test", 
//            "information_unit_test",
            "extract",
            "-c", "config/conf_sm/init.properties", 
//            "-c", "config/conf_db/init.properties", 
//            "-d", "resultats/iu", 
//            "-d", "text_data_db/p1",
            "-d", "../../dades/tests/text",
//            "-o", "resultats/cut"};
            "-o", "resultats/prova_sm_nikos",
            "-cc", "bcn"};
//        String[] args = {"field_info", "-i", "VC"};
        BoatFactExtractorMainClass.main(args);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
