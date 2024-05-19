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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorgen
 */
public class FileProcessor
{
    private final static Logger LOGGER = LogManager.getLogger(FileProcessor.class);
    private ObjectMapper        objectMapper;
    
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
            LOGGER.error("Error reading JSON: {}", e.getMessage());
        }
        return style;
    }
    
    /**
     * Write a map style to json file. null values are not written
     * @param writer Writer to use to write the file
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
            LOGGER.error("Error reading JSON: {}", e.getMessage());
        }
    }

    /**
     * Write layers in the style file to CSV
     * @param writer Writer to use to write the file
     * @param style Style of which the layers to write
     */
    public void writeCsvFile(Writer writer, Style style)
    {
        LayerProcessor processor=new LayerProcessor(Settings.getInstance().getCsvSeparator());
        processor.setLayers(style.getLayers());
        processor.writeToCsvFile(writer);   
    }

    /**
     * Read layers from the CSV file and add them to the style passed
     * @param fileName File to read layers from
     * @param style Style to add the layers to; existing layers are overwritten
     */
    public void readCsvFile(String fileName, Style style)
    {
        List<Layer> layers;
        
        LayerProcessor processor=new LayerProcessor(Settings.getInstance().getCsvSeparator());
        processor.readCsv(fileName);
        layers=processor.getLayers();
        style.setLayers(layers);
    }

    /**
     * Read layers from the Excel file and add them to the style passed
     * @param fileName File to read layers from
     * @param style Style to add the layers to; existing layers are overwritten
     */    
    public void readExcelFile(String fileName, Style style)
    {
        List<Layer> layers;
        
        LayerProcessor processor=new LayerProcessor(Settings.getInstance().getCsvSeparator());
        processor.readExcel(fileName);
        layers=processor.getLayers();
        style.setLayers(layers);
    }
        
}
