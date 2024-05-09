/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import java.io.Writer;
import java.util.List;
import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Layout;
import net.studioblueplanet.styleconvert.data.Paint;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileWriter;

/**
 *
 * @author jorgen
 */
public class LayerProcessorTest
{
    
    public LayerProcessorTest()
    {
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

    
    private void testLayers(List<Layer> layers)
    {
        Layer layer;
        // Generic
        assertEquals(392, layers.size());

        // Test all values
        layer=layers.get(390);
        assertEquals("test_id", layer.getId());
        assertEquals("test_type", layer.getType());
        assertEquals("test_source", layer.getSource());
        assertEquals("test_sourcelayer", layer.getSourceLayer());
        assertEquals("[\"==\",\"test\",\"testvalue\"]", layer.getFilter().toString());
        assertEquals(1.0, layer.getMinzoom(), 0.001);
        assertEquals(2.0, layer.getMaxzoom(), 0.001);
        Layout layout=layer.getLayout();
        assertNotNull(layer.getLayout());
        assertEquals("test_symbolplacement", layout.getSymbolPlacement());
        assertEquals(true, layout.getSymbolAvoidEdges());
        assertEquals(3.0, layout.getSymbolSpacing(), 0.0001);
        assertEquals("test_linecap", layout.getLineCap());
        assertEquals("test_linejoin", layout.getLineJoin());
        assertEquals("test_iconimage", layout.getIconImage());
        assertEquals(true, layout.getIconAllowOverlap());
        assertEquals("[0,1]", layout.getIconOffset().toString());
        assertEquals("test_textfield", layout.getTextField());
        assertEquals("\"test_textfont\"", layout.getTextFont().toString());
        assertEquals("\"test_textsize\"", layout.getTextSize().toString());
        assertEquals("\"test_textoffset\"", layout.getTextOffset().toString());
        assertEquals("test_textanchor", layout.getTextAnchor());
        assertEquals(8.1, layout.getTextMaxWidth(), 0.0001);
        assertEquals("test_texttransform", layout.getTextTransform());
        assertEquals(true, layout.getTextAllowOverlap());
        assertEquals("8.2", layout.getTextLineHeight().toString());
        
        Paint paint=layer.getPaint();
        assertNotNull(paint);
        assertEquals("test_fillpattern", paint.getFillPattern());
        assertEquals("\"test_fillcolor\"", paint.getFillColor().toString());
        assertEquals("\"test_filloutlinecolor\"", paint.getFillOutlineColor().toString());
        assertEquals("\"test_fillopacity\"", paint.getFillOpacity().toString());
        assertEquals("\"test_linecolor\"", paint.getLineColor().toString());
        assertEquals("\"test_linewidth\"", paint.getLineWidth().toString());
        assertEquals("\"test_linegapwidth\"", paint.getLineGapWidth().toString());
        assertEquals("\"test_lineopacity\"", paint.getLineOpacity().toString());
        assertEquals("\"test_linedasharray\"", paint.getLineDasharray().toString());
        assertEquals("\"test_textcolor\"", paint.getTextColor().toString());
        assertEquals("\"test_texthalocolor\"", paint.getTextHaloColor().toString());
        assertEquals(1.2, paint.getTextHaloWidth(),0.0001);
        assertEquals(2.1, paint.getTextHaloBlur(), 0.0001);
        assertEquals("\"test_backgroundcolor\"", paint.getBackgroundColor().toString());
        
        // Test no paint, no layout
        layer=layers.get(0);
        assertNull(layer.getLayout());
        
        layer=layers.get(391);
        assertNull(layer.getPaint());
        
        // Arbitrary layer
        layer=layers.get(1);
        assertEquals("terrein_vlak_akkerland_10", layer.getId());
        assertEquals("fill", layer.getType());
        assertEquals("brt_topnl", layer.getSource());
        assertEquals("top10nl_terrein_vlak", layer.getSourceLayer());
        assertEquals("[\"==\",\"typelandgebruik\",\"akkerland\"]", layer.getFilter().toString());
        assertNull(layer.getMinzoom());
        assertNull(layer.getMaxzoom());
        
        layer=layers.get(91);
        assertEquals(14, layer.getMinzoom(),0.0001);
        
        layer=layers.get(98);
        assertEquals(15, layer.getMinzoom(), 0.0001);        
    }
    /**
     * Test of getLayers method, of class LayerProcessor.
     */
    @Test
    @org.junit.Ignore
    public void testGetLayers()
    {
        System.out.println("getLayers");
        LayerProcessor instance = null;
        List<Layer> expResult = null;
        List<Layer> result = instance.getLayers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLayers method, of class LayerProcessor.
     */
    @Test
    @org.junit.Ignore
    public void testSetLayers()
    {
        System.out.println("setLayers");
        List<Layer> layers = null;
        LayerProcessor instance = null;
        instance.setLayers(layers);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeToCsvFile method, of class LayerProcessor.
     */
    @Test
    @org.junit.Ignore
    public void testWriteToCsvFile()
    {
        System.out.println("writeToCsvFile");
        Writer writer = null;
        LayerProcessor instance = null;
        instance.writeToCsvFile(writer);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readCsv method, of class LayerProcessor.
     */
    @Test
    public void testReadCsv()
    {
        System.out.println("readCsv");
        String fileName = "src/test/resources/test04.csv";
        LayerProcessor instance = new LayerProcessor(";");
        instance.readCsv(fileName);
        List<Layer> layers=instance.getLayers();

        for(int i=0;i<layers.size();i++)
        {
            System.out.println(i+" "+layers.get(i).getId());
        }
        
        testLayers(layers);
    }

    /**
     * Test of readExcel method, of class LayerProcessor.
     */
    @Test
    public void testReadExcel() throws Exception
    {
        System.out.println("readExcel");
        String fileName = "src/test/resources/test03.xlsx";
        LayerProcessor instance = new LayerProcessor(";");
        instance.readExcel(fileName);
        
        List<Layer> layers=instance.getLayers();
        for(int i=0;i<layers.size();i++)
        {
            System.out.println(i+" "+layers.get(i).getId());
        }
        
        testLayers(layers);
    }
    
}
