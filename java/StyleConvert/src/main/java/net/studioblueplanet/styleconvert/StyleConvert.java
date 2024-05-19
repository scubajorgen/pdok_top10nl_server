/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;


import net.studioblueplanet.styleconvert.data.Style;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Conversion tool. Note that it was intended for
 * own use. Not all style fields and attributes are supported. Only a subset
 * @author jorgen
 */
public class StyleConvert
{
    private final static Logger LOGGER = LogManager.getLogger(LayerProcessor.class);

    /**
     * Reads the JSON style file and exports the layers in it to CSV
     * @param jsonStyleFile Input file
     * @param csvLayerFile Output file
     */
    private void exportLayersToCsvFile(String jsonStyleFile, String csvLayerFile)
    {
        FileProcessor processor=new FileProcessor();
        
        Style style=processor.readJsonStyleFile(jsonStyleFile);
        try
        {
            Writer writer=new OutputStreamWriter(new FileOutputStream(csvLayerFile), StandardCharsets.UTF_8);
            processor.writeCsvFile(writer, style);    
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            LOGGER.error("Error creating file {}: {} ", csvLayerFile, e.getMessage());
        }
    }
    
    /**
     * This method inserts layers read from CSV into a JSON style file.
     * Existing layers are replaced
     * @param jsonStyleFile Input JSON style file
     * @param layerFile Input layers CSV file
     * @param outputJsonStyleFile Output file
     */
    private void insertLayersToStyleFile(String jsonStyleFile, String layerFile, String outputJsonStyleFile)
    {
        FileProcessor processor=new FileProcessor();
        
        Style style=processor.readJsonStyleFile(jsonStyleFile);
        
        if (layerFile.toLowerCase().endsWith(".csv"))
        {
            processor.readCsvFile(layerFile, style);
        }
        else if (layerFile.toLowerCase().endsWith(".xlsx"))
        {
            processor.readExcelFile(layerFile, style);
        }
        else
        {
            LOGGER.error("Unknown file format, please provide .xlsx of .csv file");
        }
           
        // Process the filter and convert custom filters ("_IN") to MapBox GL filters
        Settings settings=Settings.getInstance();
        Boolean enabled=settings.getEnableFilterProcessing();
        if (enabled!=null && enabled)
        {
            FilterExtension filterExtension=new FilterExtension();
            filterExtension.processFilters(style.getLayers());
        }
        
        try
        {
            Writer writer=new OutputStreamWriter(new FileOutputStream(outputJsonStyleFile), StandardCharsets.UTF_8);
            processor.writeJsonStyleFile(writer, style);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            LOGGER.error("Error creating file {}: {} ", layerFile, e.getMessage());
        }
    }
    
    
    /**
     * Main program
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        Style   style;
        boolean error;
        
        LOGGER.info("LAYER CONVERSION TOOL");
        
        StyleConvert instance=new StyleConvert();

        error=false;
        // By default the software looks for the properties file in the 
        // resources dir, unless the user specified his own.
        if (args.length==1)
        {
            String propertyFileName=args[0];
            Settings.setUserDefinedPropertyFile(propertyFileName);
        }
        else if (args.length>1)
        {
            error=true;
        }

        Settings settings=Settings.getInstance();
        
        String mode=settings.getMode();
        if (mode!=null && "extractlayers".equals(settings.getMode().toLowerCase()))
        {
            String configFile=settings.getConfigJson();
            String outputFile=settings.getOutputLayersCsv();
            if (configFile!=null && outputFile!=null)
            {
                LOGGER.info("Exporting layers from {} to {}", configFile, outputFile);
                instance.exportLayersToCsvFile(configFile, outputFile);
            }
            else
            {
                error=true;
            }
        }
        else
        {
            String configFile   =settings.getInputLayersExcel();
            String template     =settings.getTemplateJson();
            String outputFile   =settings.getConfigJson();
            if (configFile!=null && outputFile!=null && template!=null)
            {
                LOGGER.info("Inserting layers from {} into {}, write result to {}", configFile, template, outputFile);
                instance.insertLayersToStyleFile(template, configFile, outputFile);
            }
            else
            {
                error=true;
            }
        }
        
        if (error)
        {
            LOGGER.info("Usage:");
            LOGGER.info("java -jar StyleConvert");
            LOGGER.info("(assumes the properties file in the resource directory) or");
            LOGGER.info("java -jar StyleConvert [propertiesfile.properties]");
            LOGGER.info("Refer to documentation in the properties file");
        }
        LOGGER.info("done");
    }
}
