/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.elsquatrecaps.portada.boatfactextractor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        String[] args = {"extract", "-c", "config/conf_db/init.properties", "-d", "text_data_db", "-o", "resultats/dadesVaixells"};
        BoatFactExtractorMainClass.main(args);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
