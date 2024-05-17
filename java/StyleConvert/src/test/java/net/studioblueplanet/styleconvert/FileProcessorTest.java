/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Style;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author jorgen
 */
@RunWith(MockitoJUnitRunner.class)
public class FileProcessorTest
{
    @Mock private Writer writer;
    private Style testStyle1;
    
    public FileProcessorTest()
    {
        testStyle1 = new Style();
        List<Layer> layers=new ArrayList<>();
        Layer layer = new Layer();
        layer.setId("id");
        layer.setSource("söurce");
        layer.setType("test ruïne");
        layer.setMinzoom(1.0F);
        layer.setMaxzoom(2.0F);
        layers.add(layer);
        layer = new Layer();
        layer.setComment("# test ruïne");
        testStyle1.setLayers(layers);
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
     * Test of readJsonStyleFile method, of class FileProcessor.
     */
    @Test
    public void testReadJsonStyleFile()
    {
        System.out.println("readJsonStyleFile");
        String fileName = "src/test/resources/test01.json";
        FileProcessor instance = new FileProcessor();
        Style result = instance.readJsonStyleFile(fileName);
        assertEquals(8, result.getVersion().intValue());
        assertEquals("PDOK BRT TopNL DEVELOP", result.getName());
        assertEquals(2, result.getLayers().size());
        Layer layer=result.getLayers().get(1);
        assertEquals("[\"==\",\"typelandgebruik\",\"aanlegsteïger\"]", layer.getFilter().toString());
    }

    /**
     * Test of writeJsonStyleFile method, of class FileProcessor.
     */
    @Test
    public void testWriteJsonStyleFile() throws IOException
    {
        System.out.println("writeJsonStyleFile");
        reset(writer);
        ArgumentCaptor<String> stringCaptor=ArgumentCaptor.forClass(String.class);

        FileProcessor instance = new FileProcessor();
        instance.writeJsonStyleFile(writer, testStyle1);
        verify(writer, times(1)).write(stringCaptor.capture());
        
        String result = "{\r\n" +
                        "  \"layers\": \r\n" +
                        "  [\r\n" +
                        "    {\r\n" +
                        "      \"id\": \"id\",\r\n" +
                        "      \"type\": \"test ruïne\",\r\n" +
                        "      \"source\": \"söurce\",\r\n" +
                        "      \"minzoom\": 1.0,\r\n" +
                        "      \"maxzoom\": 2.0\r\n" +
                        "    }\r\n" +
                        "  ]\r\n" +
                        "}";
        
        List<String> values=stringCaptor.getAllValues();
        
        assertEquals(result, values.get(0));
        for (String value:values)
        {
            System.out.println(value);
        }
    }

    /**
     * Test of writeCsvFile method, of class FileProcessor.
     */
    @Test
    public void testWriteCsvFile() throws IOException
    {
        System.out.println("writeCsvFile");

        reset(writer);
        ArgumentCaptor<String> stringCaptor=ArgumentCaptor.forClass(String.class);
        FileProcessor instance = new FileProcessor();
        instance.writeCsvFile(writer, testStyle1);
        verify(writer, times(47)).write(stringCaptor.capture());

        assertEquals("id;type;source;source-layer;filter;minzoom;maxzoom;fill-pattern;fill-color;"+
                     "fill-outline-color;fill-opacity;symbol-placement;symbol-avoid-edges;"+
                     "symbol-spacing;line-cap;line-join;line-color;line-width;line-gap-width;"+
                     "line-opacity;line-dasharray;icon-image;icon-allow-overlap;icon-offset;"+
                     "icon-text-fit;"+"icon-text-fit-padding;"+
                     "text-field;text-font;text-size;text-offset;text-anchor;text-max-width;"+
                     "text-transform;text-allow-overlap;text-line-height;text-letter-spacing;text-color;text-halo-color;"+
                     "text-halo-width;text-halo-blur;background-color\n", stringCaptor.getAllValues().get(0));
        assertEquals("test ruïne", stringCaptor.getAllValues().get(3));

        List<String> values=stringCaptor.getAllValues();
        for (String value:values)
        {
            System.out.println(value);
        }
    }

    /**
     * Test of readCsvFile method, of class FileProcessor.
     */
    @Test
    public void testReadCsvFile()
    {
        System.out.println("readCsvFile");
        String fileName = "src/test/resources/test04.csv";
        Style style = new Style();
        FileProcessor instance = new FileProcessor();
        instance.readCsvFile(fileName, style);
        assertEquals(392, style.getLayers().size());
        Layer layer=style.getLayers().get(268);
        assertEquals("[\"in\",\"typegebouw\",\"ruïne\"]", layer.getFilter().toString());
    }
}
