/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jorgen
 */
public class SettingsTest
{
    private static Settings instance;
    
    public SettingsTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        Settings.setUserDefinedPropertyFile("src/test/resources/StyleConvert.properties");
        instance=Settings.getInstance();
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getInstance method, of class Settings.
     */
    @Test
    public void testGetInstance()
    {
        System.out.println("getInstance");
        Settings expResult = null;
        Settings result = Settings.getInstance();
        assertNotNull(result);
        
        // Singleton, so the second getInstance must return the same as the first
        Settings result2 = Settings.getInstance();
        assertEquals(result, result2);
    }

    /**
     * Test of getCsvSeparator method, of class Settings.
     */
    @Test
    public void testGetCsvSeparator()
    {
        System.out.println("getCsvSeparator");
        String result = instance.getCsvSeparator();
        assertEquals(";", result);
    }

    /**
     * Test of getGpkgSourceDir method, of class Settings.
     */
    @Test
    public void testGetGpkgSourceDir()
    {
        System.out.println("getGpkgSourceDir");
        String result = instance.getGpkgSourceDir();
        assertEquals("src/test/resources/topnltestdatabases", result);
    }

    /**
     * Test of getTopnlRelease method, of class Settings.
     */
    @Test
    public void testGetTopnlRelease()
    {
        System.out.println("getTopnlRelease");
        String result = instance.getTopnlRelease();
        assertEquals("2024-04", result);
    }


    /**
     * Test of getTemplateJson method, of class Settings.
     */
    @Test
    public void testGetTemplateJson()
    {
        System.out.println("getTemplateJson");
        String result = instance.getTemplateJson();
        assertEquals("template.json", result);
    }

    /**
     * Test of getInputLayersExcel method, of class Settings.
     */
    @Test
    public void testGetInputLayersExcel()
    {
        System.out.println("getInputLayersExcel");
        String result = instance.getInputLayersExcel();
        assertEquals("layers.xslx", result);
    }

    /**
     * Test of getConfigJson method, of class Settings.
     */
    @Test
    public void testGetConfigJson()
    {
        System.out.println("getConfigJson");
        String result = instance.getConfigJson();
        assertEquals("style_topnl.json", result);
    }

    /**
     * Test of getOutputLayersCsv method, of class Settings.
     */
    @Test
    public void testGetOutputLayersCsv()
    {
        System.out.println("getOutputLayersCsv");
        String result = instance.getOutputLayersCsv();
        assertEquals("layers.csv", result);
    }

    /**
     * Test of getMode method, of class Settings.
     */
    @Test
    public void testGetMode()
    {
        System.out.println("getMode");
        String result = instance.getMode();
        assertEquals("insertLayers", result);
    }

    /**
     * Test of getMode method, of class Settings.
     */
    @Test
    public void testSetUserDefinedPropertyFile()
    {
        System.out.println("setUserDefinedPropertyFile");
        Settings.setUserDefinedPropertyFile("src/test/resources/UserDefinedStyleConvert.properties");
        Settings newInstance = Settings.getInstance();
        String result = newInstance.getCsvSeparator();
        assertEquals(",", result);
        result = newInstance.getMode();
        assertEquals("extractLayers", result);
        Boolean booleanResult = newInstance.getEnableFilterProcessing();
        assertEquals(false, booleanResult);
    }    

    /**
     * Test of getEnableFilterProcessing method, of class Settings.
     */
    @Test
    public void testGetEnableFilterProcessing()
    {
        System.out.println("getEnableFilterProcessing");
        Boolean result = instance.getEnableFilterProcessing();
        assertEquals(true, result);
    }
}
