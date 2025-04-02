/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package org.elsquatrecaps.autonewsextractor.tools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author josep
 */
public class RegexBuilderTest {
    static AutoNewsExtractorConfiguration configuration = new AutoNewsExtractorConfiguration();
    
    public RegexBuilderTest() {
    }
    
    public static void main(String[] args){
        setUpClass();
        RegexBuilderTest test = new RegexBuilderTest();
        
        test.setUp();
        test.testGetInstance_RegexConfiguration();
        test.tearDown();
                
        tearDownClass();
    }
    
    
    @BeforeAll
    public static void setUpClass() {
        String[] args ={
//            "-r",
//            "/home/josep/Dropbox/feinesJordi/github/autoNewsExtractorApp/regex",
//            "-f",
//            "sdl.boatfacts",
//            "-n",
//            "db.arca.txt",
//            "-p",
//            "db.boatfact.parser,db.boatcounter.parser",
            "-c",
            "config/conf_db/init.properties"
        };
        try {
            configuration.parseArgumentsAndConfigure(args);
        } catch (IOException ex) {
            Logger.getLogger(RegexBuilderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class RegexBuilder.
     */
    @Test
    public void testGetInstance_RegexConfiguration() {
        System.out.println("getInstance");
        RegexBuilder builder = RegexBuilder.getInstance(configuration);
        String p = builder.buildRegex("flag").pattern();
        assertNotNull(p);
        p = builder.buildRegex("is_page_number").pattern();
        assertNotNull(p);
//        assertEquals("^\\s*(?:[\\d(IlOoSt»]){0,4}\\n(.*)", p);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class RegexBuilder.
     */
    @Test
    public void testGetInstance_RegexConfiguration_int() {
//        System.out.println("getInstance");
//        RegexConfiguration conf = null;
//        int parserid = 0;
//        RegexBuilder expResult = null;
//        RegexBuilder result = RegexBuilder.getInstance(conf, parserid);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class RegexBuilder.
     */
    @Test
    public void testGetInstance_String_String() {
//        System.out.println("getInstance");
//        String basePath = "";
//        String searchPath = "";
//        RegexBuilder expResult = null;
//        RegexBuilder result = RegexBuilder.getInstance(basePath, searchPath);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class RegexBuilder.
     */
    @Test
    public void testGetInstance_3args() {
//        System.out.println("getInstance");
//        String basePath = "";
//        String searchPath = "";
//        String variant = "";
//        RegexBuilder expResult = null;
//        RegexBuilder result = RegexBuilder.getInstance(basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of buildRegex method, of class RegexBuilder.
     */
    @Test
    public void testBuildRegex() {
//        System.out.println("buildRegex");
//        String cfgregex = "";
//        RegexBuilder instance = null;
//        Pattern expResult = null;
//        Pattern result = instance.buildRegex(cfgregex);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlagsFromFile method, of class RegexBuilder.
     */
    @Test
    public void testGetFlagsFromFile_String() {
//        System.out.println("getFlagsFromFile");
//        String file = "";
//        RegexBuilder instance = null;
//        int expResult = 0;
//        int result = instance.getFlagsFromFile(file);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlagsFromFile method, of class RegexBuilder.
     */
    @Test
    public void testGetFlagsFromFile_File() {
//        System.out.println("getFlagsFromFile");
//        File file = null;
//        int expResult = 0;
//        int result = RegexBuilder.getFlagsFromFile(file);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlagsFromUri method, of class RegexBuilder.
     */
    @Test
    public void testGetFlagsFromUri() {
//        System.out.println("getFlagsFromUri");
//        URI uri = null;
//        int expResult = 0;
//        int result = RegexBuilder.getFlagsFromUri(uri);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlagsFromPath method, of class RegexBuilder.
     */
    @Test
    public void testGetFlagsFromPath() {
//        System.out.println("getFlagsFromPath");
//        Path path = null;
//        int expResult = 0;
//        int result = RegexBuilder.getFlagsFromPath(path);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getStrPatternFromFile method, of class RegexBuilder.
     */
    @Test
    public void testGetStrPatternFromFile_String() {
//        System.out.println("getStrPatternFromFile");
//        String file = "";
//        RegexBuilder instance = null;
//        String expResult = "";
//        String result = instance.getStrPatternFromFile(file);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getStrPatternFromFile method, of class RegexBuilder.
     */
    @Test
    public void testGetStrPatternFromFile_StringArr() {
//        System.out.println("getStrPatternFromFile");
//        String[] filename = null;
//        String expResult = "";
//        String result = RegexBuilder.getStrPatternFromFile(filename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getStrPatternFromFile method, of class RegexBuilder.
     */
    @Test
    public void testGetStrPatternFromFile_3args() {
//        System.out.println("getStrPatternFromFile");
//        String filename = "";
//        File basePath = null;
//        String searchPath = "";
//        String expResult = "";
//        String result = RegexBuilder.getStrPatternFromFile(filename, basePath, searchPath);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetFileFromName() {
//        System.out.println("getFileFromName");
//        String name = "";
//        int type = 0;
//        File basePath = null;
//        String searchPath = "";
//        String variant = "";
//        File expResult = null;
//        File result = RegexBuilder.getFileFromName(name, type, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getPathFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetPathFromName() {
//        System.out.println("getPathFromName");
//        String name = "";
//        int type = 0;
//        String basePath = "";
//        String searchPath = "";
//        String variant = "";
//        Path expResult = null;
//        Path result = RegexBuilder.getPathFromName(name, type, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexPathFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetRegexPathFromName_4args() {
//        System.out.println("getRegexPathFromName");
//        String name = "";
//        String basePath = "";
//        String searchPath = "";
//        String variant = "";
//        Path expResult = null;
//        Path result = RegexBuilder.getRegexPathFromName(name, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexPathFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetRegexPathFromName_3args() {
//        System.out.println("getRegexPathFromName");
//        String name = "";
//        String basePath = "";
//        String variant = "";
//        Path expResult = null;
//        Path result = RegexBuilder.getRegexPathFromName(name, basePath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFileFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetRegexFileFromName_4args() {
//        System.out.println("getRegexFileFromName");
//        String name = "";
//        File basePath = null;
//        String searchPath = "";
//        String variant = "";
//        File expResult = null;
//        File result = RegexBuilder.getRegexFileFromName(name, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegexFileFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetRegexFileFromName_3args() {
//        System.out.println("getRegexFileFromName");
//        String name = "";
//        File basePath = null;
//        String variant = "";
//        File expResult = null;
//        File result = RegexBuilder.getRegexFileFromName(name, basePath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getOptionsPathFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetOptionsPathFromName_3args() {
//        System.out.println("getOptionsPathFromName");
//        String name = "";
//        String basePath = "";
//        String variant = "";
//        Path expResult = null;
//        Path result = RegexBuilder.getOptionsPathFromName(name, basePath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getOptionsPathFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetOptionsPathFromName_4args() {
//        System.out.println("getOptionsPathFromName");
//        String name = "";
//        String basePath = "";
//        String searchPath = "";
//        String variant = "";
//        Path expResult = null;
//        Path result = RegexBuilder.getOptionsPathFromName(name, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getOptionsFileFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetOptionsFileFromName_4args() {
//        System.out.println("getOptionsFileFromName");
//        String name = "";
//        File basePath = null;
//        String searchPath = "";
//        String variant = "";
//        File expResult = null;
//        File result = RegexBuilder.getOptionsFileFromName(name, basePath, searchPath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getOptionsFileFromName method, of class RegexBuilder.
     */
    @Test
    public void testGetOptionsFileFromName_3args() {
//        System.out.println("getOptionsFileFromName");
//        String name = "";
//        File basePath = null;
//        String variant = "";
//        File expResult = null;
//        File result = RegexBuilder.getOptionsFileFromName(name, basePath, variant);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
