/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import net.studioblueplanet.styleconvert.data.Layer;
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
public class FilterExtensionTest
{
    
    public FilterExtensionTest()
    {
        Settings.setUserDefinedPropertyFile("src/test/resources/StyleConvert.properties");
    }
    
    @BeforeClass
    public static void setUpClass()
    {
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
     * Test of processFilters method, of class FilterExtension.
     */
    @Test
    public void testFilterDistinctValues()
    {
        System.out.println("filterDistinctValues");
        FilterExtension instance = new FilterExtension();
        List<String> distinctValues=new ArrayList<>();
        distinctValues.add("test");
        distinctValues.add("politiebureau|kerk|toren");
        distinctValues.add("iets");
        distinctValues.add("kerk|moskee");
        distinctValues.add("toren|kerk");
        distinctValues.add("bibliotheek|moskee");
        distinctValues.add("test12");
        distinctValues.add("kerruk");
        distinctValues.add("kertoren|shouldnottrigger");
        distinctValues.add("test|kertoren|shouldnotrigger");
        distinctValues.add("mosk");
        distinctValues.add("moskeetje|shouldnottrigger");
        distinctValues.add(" kerk|shouldnottrigger");
        distinctValues.add("kerk |shouldnottrigger");
        List<String> filters=new ArrayList();
        filters.add("kerk");
        filters.add("moskee");
        filters.add("synagoge");
        List<String> result=instance.filterDistinctValues(distinctValues, filters);
        assertEquals(4, result.size());
        assertEquals("bibliotheek|moskee", result.get(0));
        assertEquals("kerk|moskee", result.get(1));
        assertEquals("politiebureau|kerk|toren", result.get(2));
        assertEquals("toren|kerk", result.get(3));
    }    

    /**
     * Test of filterReplacementIn method, of class FilterExtension.
     */
    @Test
    public void testFilterReplacementIn()
    {
        System.out.println("filterReplacement");
        String gpkgLayer = "top10nl_gebouw_vlak";
        String gpkgAttribute = "typegebouw";
        List<String> values = new ArrayList<>();
        values.add("kerk");
        values.add("moskee");
        FilterExtension instance = new FilterExtension();
        JsonNode expResult = null;
        // Negate=false
        JsonNode result = instance.filterReplacementIn(gpkgLayer, gpkgAttribute, values, false);
        assertEquals(7, result.size());
        assertEquals("[\"in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"kerk\","+
                     "\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"toren|kerk\"]", result.toString());
        // Negate=true
        result = instance.filterReplacementIn(gpkgLayer, gpkgAttribute, values, true);
        assertEquals(7, result.size());
        assertEquals("[\"!in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"kerk\","+
                     "\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"toren|kerk\"]", result.toString());

        // Second call: should be fetched from cache
        values = new ArrayList<>();
        values.add("brandweerkazerne");
        values.add("school");
        result = instance.filterReplacementIn(gpkgLayer, gpkgAttribute, values, false);
        assertEquals(5, result.size());
        assertEquals("[\"in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"school|synagoge|kerk|moskee\","+
                     "\"school|synagoge|zwembad\"]", result.toString());

    }

    /**
     * Test of processFilters method, of class FilterExtension.
     */
    @Test
    public void testProcessFilters()
    {
        System.out.println("processFilters");
        // Read layers from test file 
        String fileName = "src/test/resources/test05.xlsx";
        LayerProcessor processor = new LayerProcessor(";");
        processor.readExcel(fileName);
        List<Layer> layers=processor.getLayers();

        assertNull(layers.get(0).getFilter());
        assertEquals("[\"_IN\",\"typegebouw\",\"kerk\",\"moskee\",\"synagoge\"]", layers.get(1).getFilter().toString());
        assertEquals("[\"in\",\"typegebouw\",\"toren\"]", layers.get(2).getFilter().toString());
        assertEquals("[\"all\",[\"_IN\",\"typegebouw\",\"klokkentoren\",\"toren\"],"+
                     "[\"!_IN\",\"typegebouw\",\"kerk\",\"moskee\",\"synagoge\"]]", layers.get(3).getFilter().toString());
        assertEquals("[\"in\",\"typeweg\",\"parkeerplaats\",\"parkeerplaats: carpool\",\"parkeerplaats: P+R\"]", layers.get(5).getFilter().toString());        
        
        FilterExtension instance = new FilterExtension();
        instance.processFilters(layers);
        // Altered
        assertEquals("[\"in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"kerk\","+
                     "\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"school|synagoge|zwembad\",\"toren|kerk\"]", layers.get(1).getFilter().toString());
        assertEquals("[\"all\",[\"in\",\"typegebouw\",\"toren\",\"toren|kerk\"],"+
                              "[\"!in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"kerk\",\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"school|synagoge|zwembad\",\"toren|kerk\"]]", layers.get(3).getFilter().toString());        
        
        // Unaltered
        assertNull(layers.get(0).getFilter());
        assertEquals("[\"in\",\"typegebouw\",\"toren\"]", layers.get(2).getFilter().toString());
        assertEquals("[\"in\",\"typeweg\",\"parkeerplaats\",\"parkeerplaats: carpool\",\"parkeerplaats: P+R\"]", layers.get(5).getFilter().toString());

    }
}
