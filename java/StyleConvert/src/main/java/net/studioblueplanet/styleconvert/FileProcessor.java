/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Style;

/**
 *
 * @author jorgen
 */
public class FileProcessor
{
    private ObjectMapper        objectMapper;
    private final static String PROPERTY_FILE_NAME="StyleConvert.properties";
    private static final String DEFAULT_CSV_SEPARATOR=";";   
    private String              csvSeparator=DEFAULT_CSV_SEPARATOR;
    
    /**
     * Read a map style from JSON file
     * @param fileName Name of the Json file
     * @return The style read or null if failed
     */
    public Style readJsonStyleFile(String fileName)
    {
        Style style;
        style=null;
        try
        {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            style=objectMapper.readValue(new File(fileName), Style.class);
        }
        catch(IOException e)
        {
            System.err.println("Error reading JSON: "+e.getMessage());
        }
        return style;
    }
    
    /**
     * Write a map style to json file. null values are not written
     * @param fileName Name of the JSON file to write to
     * @param style The style to write
     */
    public void writeJsonStyleFile(Writer writer, Style style)
    {
        try
        {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String json=objectMapper.writer(new JsonPrettyPrinter()).writeValueAsString(style);
            writer.write(json);
        }
        catch(IOException e)
        {
            System.err.println("Error reading JSON: "+e.getMessage());
        }
    }

    /**
     * Write layers in the style file to CSV
     * @param fileName Name of the CSV file
     * @param style Style of which the layers to write
     */
    public void writeCsvFile(Writer writer, Style style)
    {
        LayerProcessor processor=new LayerProcessor(csvSeparator);
        processor.setLayers(style.getLayers());
        processor.writeToCsvFile(writer);   
    }

    public void readCsvFile(String fileName, Style style)
    {
        List<Layer> layers;
        
        LayerProcessor processor=new LayerProcessor(csvSeparator);
        processor.readCsv(fileName);
        layers=processor.getLayers();
        style.setLayers(layers);
    }
        
}
