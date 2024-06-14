/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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

        // Values that should create an empty filter
        values = new ArrayList<>();
        values.add("bullshit");
        values.add("more bullshit");
        result = instance.filterReplacementIn(gpkgLayer, gpkgAttribute, values, false);
        assertEquals(4, result.size());
        assertEquals("[\"in\",\"typegebouw\",\"bullshit\",\"more bullshit\"]", result.toString());
        result = instance.filterReplacementIn(gpkgLayer, gpkgAttribute, values, true);
        assertEquals(4, result.size());
        assertEquals("[\"!in\",\"typegebouw\",\"bullshit\",\"more bullshit\"]", result.toString());
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
        assertEquals("[\"in\",\"typegebouw\",\"toren\",\"watertoren\"]", layers.get(2).getFilter().toString());
        assertEquals("[\"all\",[\"_IN\",\"typegebouw\",\"klokkentoren\",\"toren\"],"+
                     "[\"!_IN\",\"typegebouw\",\"kerk\",\"moskee\",\"synagoge\"]]", layers.get(3).getFilter().toString());
        assertEquals("[\"in\",\"typeweg\",\"parkeerplaats\",\"parkeerplaats: carpool\",\"parkeerplaats: P+R\"]", layers.get(5).getFilter().toString());        
        assertEquals("[\"in\",\"typegebouw\",\"vuurtoren\"]", layers.get(6).getFilter().toString());        
        assertNull(layers.get(7).getFilter());
        
        FilterExtension instance = new FilterExtension();
        instance.processFilters(layers);
        // Altered
        assertEquals("[\"in\",\"typegebouw\",\"brandweerkazerne|moskee\",\"kerk\","+
                     "\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"school|synagoge|zwembad\",\"toren|kerk\"]", layers.get(1).getFilter().toString());
        assertEquals("[\"==\",\"typegebouw\",\"toren\"]", layers.get(3).getFilter().toString());        
        assertEquals("[\"==\",\"typegebouw\",\"vuurtoren\"]", layers.get(6).getFilter().toString());        
        
        // Unaltered
        assertNull(layers.get(0).getFilter());
        assertEquals("[\"in\",\"typegebouw\",\"toren\",\"watertoren\"]", layers.get(2).getFilter().toString());
        assertEquals("[\"in\",\"typeweg\",\"parkeerplaats\",\"parkeerplaats: carpool\",\"parkeerplaats: P+R\"]", layers.get(5).getFilter().toString());
        assertNull(layers.get(7).getFilter());
        
    }

    /**
     * Test of processFilter method, of class FilterExtension.
     */
    @Test
    public void testProcessFilter()
    {
        System.out.println("processFilter");
        ObjectMapper mapper=new ObjectMapper();
        FilterExtension instance = new FilterExtension();
        String gpkgLayer = "top10nl_gebouw_vlak";
        Layer layer = new Layer();
        
        ArrayNode filter = mapper.createArrayNode();
        filter.add("_IN");
        filter.add("typegebouw");
        filter.add("toren");
        filter.add("klokkentoren");
        layer.setFilter(filter);
        JsonNode result = instance.processFilter(layer, gpkgLayer, filter);
        assertEquals("[\"in\",\"typegebouw\",\"toren\",\"toren|kerk\"]", result.toString());
        
        filter.removeAll();
        filter.add("!_IN");
        filter.add("typegebouw");
        filter.add("toren");
        filter.add("klokkentoren");
        layer.setFilter(filter);
        result = instance.processFilter(layer, gpkgLayer, filter);
        assertEquals("[\"!in\",\"typegebouw\",\"toren\",\"toren|kerk\"]", result.toString());        
        
        filter.removeAll();
        filter.add("!in");
        filter.add("typegebouw");
        filter.add("toren");
        filter.add("klokkentoren");
        layer.setFilter(filter);
        result = instance.processFilter(layer, gpkgLayer, filter);
        assertNull(result);        
    }

    /**
     * Test of processSubfilters method, of class FilterExtension.
     */
    @Test
    public void testProcessSubfiltersl()
    {
        System.out.println("processSubfilters");
        ObjectMapper mapper=new ObjectMapper();
        FilterExtension instance = new FilterExtension();
        String gpkgLayer = "top10nl_gebouw_vlak";
        Layer layer = new Layer();
        
        ArrayNode filter = mapper.createArrayNode();
        filter.add("all");
        ArrayNode subfilter1=mapper.createArrayNode();
        subfilter1.add("_IN");
        subfilter1.add("typegebouw");
        subfilter1.add("toren");
        ArrayNode subfilter2=mapper.createArrayNode();
        filter.add(subfilter1);
        subfilter2.add("_IN");
        subfilter2.add("typegebouw");
        subfilter2.add("kerk");
        filter.add(subfilter2);
        layer.setFilter(filter);
        JsonNode result = instance.processSubfilters(layer, gpkgLayer);
        assertEquals("[\"all\",[\"in\",\"typegebouw\",\"toren\",\"toren|kerk\"],"+
                     "[\"in\",\"typegebouw\",\"kerk\",\"politiebureau|kerk\",\"school|synagoge|kerk|moskee\",\"toren|kerk\"]]", result.toString());
    }

    /**
     * Test of optimizeSubfilters method, of class FilterExtension.
     */
    @Test
    public void testOptimizeSubfilters()
    {
        System.out.println("optimizeSubfilters");
        ObjectMapper mapper=new ObjectMapper();
        FilterExtension instance = new FilterExtension();
        String gpkgLayer = "top10nl_gebouw_vlak";
        Layer layer = new Layer();
        
        // ["all", ["in", "attribute", ...],["!in", "attribute", ...]]
        ArrayNode filter = mapper.createArrayNode();
        filter.add("all");
        ArrayNode subfilter1=mapper.createArrayNode();
        subfilter1.add("in");
        subfilter1.add("typegebouw");
        subfilter1.add("toren");
        subfilter1.add("toren|bunker");
        subfilter1.add("kerk|toren");
        ArrayNode subfilter2=mapper.createArrayNode();
        filter.add(subfilter1);
        subfilter2.add("!in");
        subfilter2.add("typegebouw");
        subfilter2.add("kerk");
        subfilter2.add("kerk|school");
        subfilter2.add("kerk|toren");
        filter.add(subfilter2);
        layer.setFilter(filter);
        JsonNode result=instance.optimizeSubfilters(layer, gpkgLayer);
        assertEquals("[\"in\",\"typegebouw\",\"toren\",\"toren|bunker\"]", result.toString());
        
        // ["all", ["in", "attribute", ...],["!in", "attribute", ...],["thirdfilter"]]
        filter.removeAll();
        filter.add("all");
        subfilter1.removeAll();
        subfilter1.add("in");
        subfilter1.add("typegebouw");
        subfilter1.add("toren");
        subfilter1.add("kerk|toren");
        filter.add(subfilter1);
        subfilter2.removeAll();
        subfilter2.add("!in");
        subfilter2.add("typegebouw");
        subfilter2.add("kerk");
        subfilter2.add("kerk|school");
        subfilter2.add("kerk|toren");
        filter.add(subfilter2);
        ArrayNode subfilter3=mapper.createArrayNode();
        subfilter3.add("!in");
        subfilter3.add("fysiekvoorkomen");
        subfilter3.add("test");
        filter.add(subfilter3);
        layer.setFilter(filter);
        result=instance.optimizeSubfilters(layer, gpkgLayer);
        assertEquals("[\"all\",[\"in\",\"typegebouw\",\"toren\"],[\"!in\",\"fysiekvoorkomen\",\"test\"]]", result.toString());        

        // ["all", ["in", "attribute", ...],["!in", "otherattribute", ...]] -> no optimisation
        filter.removeAll();
        filter.add("any");
        subfilter1.removeAll();
        subfilter1.add("in");
        subfilter1.add("typegebouw");
        subfilter1.add("toren");
        subfilter1.add("kerk|toren");
        filter.add(subfilter1);
        subfilter3.removeAll();
        subfilter3.add("!in");
        subfilter3.add("fysiekvoorkomen");
        subfilter3.add("test");
        filter.add(subfilter3);
        layer.setFilter(filter);
        result=instance.optimizeSubfilters(layer, gpkgLayer);
        assertNull(result);        
    }

    /**
     * Test of processSubfilters method, of class FilterExtension.
     */
    @Test
    public void testProcessSubfilters()
    {
        System.out.println("processSubfilters");
        FilterExtension instance = new FilterExtension();
        String gpkgLayer = "top10nl_gebouw_vlak";
        Layer layer = new Layer();
        ObjectMapper mapper=new ObjectMapper();
        // ["all", ["in", "attribute", ...],["!in", "attribute", ...]]
        ArrayNode filter = mapper.createArrayNode();
        filter.add("all");
        ArrayNode subfilter1=mapper.createArrayNode();
        subfilter1.add("_IN");
        subfilter1.add("typegebouw");
        subfilter1.add("toren");
        ArrayNode subfilter2=mapper.createArrayNode();
        filter.add(subfilter1);
        subfilter2.add("!_IN");
        subfilter2.add("typegebouw");
        subfilter2.add("kerk");
        filter.add(subfilter2);
        layer.setFilter(filter);
        JsonNode result=instance.processSubfilters(layer, gpkgLayer);
        assertEquals("[\"all\",[\"in\",\"typegebouw\",\"toren\",\"toren|kerk\"],"+
                     "[\"!in\",\"typegebouw\",\"kerk\",\"politiebureau|kerk\","+
                              "\"school|synagoge|kerk|moskee\",\"toren|kerk\"]]", result.toString());
        filter.removeAll();
        filter.add("test");  
        filter.add("blah");
        result=instance.processSubfilters(layer, gpkgLayer);
        assertNull(result);
    }

    /**
     * Test of optimizeSubfilters2 method, of class FilterExtension.
     */
    @Test
    public void testOptimizeSubfilters2()
    {
        System.out.println("optimizeSubfilters2");
        ObjectMapper mapper=new ObjectMapper();
        FilterExtension instance = new FilterExtension();
        Layer layer = new Layer();
        
        // ["all", ["in", "attribute", "val"],["in", "attribute2","val1","val2"]]
        ArrayNode filter = mapper.createArrayNode();
        filter.add("all");
        ArrayNode subfilter1=mapper.createArrayNode();
        subfilter1.add("in");
        subfilter1.add("attribute");
        subfilter1.add("val");
        ArrayNode subfilter2=mapper.createArrayNode();
        filter.add(subfilter1);
        subfilter2.add("in");
        subfilter2.add("attribute2");
        subfilter2.add("val1");
        subfilter2.add("val2");
        filter.add(subfilter2);
        JsonNode result=instance.optimizeSubfilters2(filter);
        assertEquals("[\"all\",[\"==\",\"attribute\",\"val\"],"+
                     "[\"in\",\"attribute2\",\"val1\",\"val2\"]]", result.toString());
        
        // ["all", ["!in", "attribute", "val"],["!in", "attribute2","val1","val2"]]
        filter.removeAll();
        filter.add("all");
        subfilter1.removeAll();
        subfilter1.add("!in");
        subfilter1.add("attribute");
        subfilter1.add("val");
        filter.add(subfilter1);
        subfilter2.removeAll();
        subfilter2.add("!in");
        subfilter2.add("attribute2");
        subfilter2.add("val1");
        subfilter2.add("val2");
        filter.add(subfilter2);
        result=instance.optimizeSubfilters2(filter);
        assertEquals("[\"all\",[\"!=\",\"attribute\",\"val\"],"+
                     "[\"!in\",\"attribute2\",\"val1\",\"val2\"]]", result.toString());
        
        // ["all", ["!in", "attribute", "val", "val3"],["!in", "attribute2","val1","val2"]]
        filter.removeAll();
        filter.add("all");
        subfilter1.removeAll();
        subfilter1.add("!in");
        subfilter1.add("attribute");
        subfilter1.add("val");
        subfilter1.add("val3");
        filter.add(subfilter1);
        subfilter2.removeAll();
        subfilter2.add("!in");
        subfilter2.add("attribute2");
        subfilter2.add("val1");
        subfilter2.add("val2");
        filter.add(subfilter2);
        result=instance.optimizeSubfilters2(filter);
        assertNull(result);

        // ["in", "attribute", "val"]
        filter.removeAll();
        filter.add("in");
        filter.add("attribute");
        filter.add("val");
        result=instance.optimizeSubfilters2(filter);
        assertEquals("[\"==\",\"attribute\",\"val\"]", result.toString());

        // ["!in", "attribute", "val"]
        filter.removeAll();
        filter.add("!in");
        filter.add("attribute");
        filter.add("val");
        result=instance.optimizeSubfilters2(filter);
        assertEquals("[\"!=\",\"attribute\",\"val\"]", result.toString());

        // ["!in", "attribute", "val", "val3"]
        filter.removeAll();
        filter.add("!in");
        filter.add("attribute");
        filter.add("val");
        filter.add("val3");
        result=instance.optimizeSubfilters2(filter);
        assertNull(result);
    }
}
