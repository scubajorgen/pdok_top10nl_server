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
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Quick and especially DIRTY conversion tool. Note that it was intended for
 * own use. Not all style fields and attributes are supported. Only a subset
 * @author jorgen
 */
public class StyleConvert
{
    private final static Logger LOGGER = LogManager.getLogger(LayerProcessor.class);
    private final static String PROPERTY_FILE_NAME      ="StyleConvert.properties";
    private String              csvSeparator;
    private static final String DEFAULT_CSV_SEPARATOR   =";";
    
    private void readSettings()
    {
        Properties properties=new Properties();
        try
        {
            properties.load(new FileInputStream(PROPERTY_FILE_NAME      )); 
            csvSeparator=properties.getProperty("csvSeparator");
            if (csvSeparator==null)
            {
                csvSeparator=DEFAULT_CSV_SEPARATOR;
            }
        }
        catch(IOException e)
        {
            csvSeparator=DEFAULT_CSV_SEPARATOR;
        }
    }

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
        instance.readSettings();

        error=false;
        if (args.length>0)
        {
            if (args[0].equals("INSERTLAYERS"))
            {
                if (args.length==4)
                {
                    LOGGER.info("Inserting layers from {} into {}, write result to {}", args[2], args[1], args[3]);
                    instance.insertLayersToStyleFile(args[1], args[2], args[3]);
                }
                else
                {
                    error=true;
                }
            }
            else if (args[0].equals("EXTRACTLAYERS"))
            {
                if (args.length==3)
                {
                    LOGGER.info("Exporting layers from {} to {}", args[1], args[2]);
                    instance.exportLayersToCsvFile(args[1], args[2]);
                }
                else
                {
                    error=true;
                }                
            }
            else 
            {
                error=true;
            }
        }
        else
        {
            error=true;
        }
        
        if (error)
        {
            LOGGER.info("Usage:");
            LOGGER.info("java -jar StyleConvert EXTRACTLAYERS [StyleJsonInFile] [LayerCsvOutFile]");
            LOGGER.info("Extracts the layers out the [StyleJsonInFile] and writes them to [LayerCsvOutFile]");
            LOGGER.info("java -jar StyleConvert INSERTLAYERS [StyleJsonInFile] [LayerInFile] [StyleJsonOutFile]");
            LOGGER.info("Inserts the layers from [LayerInFile] into [StyleJsonInFile] and writes it to a new style file [StyleJsonOutFile]");
        }
        LOGGER.info("done");
    }
}
