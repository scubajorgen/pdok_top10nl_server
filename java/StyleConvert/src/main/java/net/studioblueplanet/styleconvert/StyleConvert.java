/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import net.studioblueplanet.styleconvert.data.Style;
import net.studioblueplanet.styleconvert.data.Layer;
import java.io.File;
import java.io.IOException;

/**
 * Quick and especially DIRTY conversion tool. Note that it was intended for
 * own use. Not all style fields and attributes are supported. Only a subset
 * @author jorgen
 */
public class StyleConvert
{
    private ObjectMapper            objectMapper;
    JsonNode jsonNode;
    
    /**
     * Read a map style from JSON file
     * @param fileName Name of the Json file
     * @return The style read or null if failed
     */
    private Style readJsonStyleFile(String fileName)
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
    private void writeJsonStyleFile(String fileName, Style style)
    {
        try
        {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.writer(new JsonPrettyPrinter()).writeValue(new File(fileName), style);
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
    private void writeCsvFile(String fileName, Style style)
    {
        LayerProcessor processor=new LayerProcessor();
        processor.setLayers(style.getLayers());
        processor.writeToCsvFile(fileName);        
    }

    private void readCsvFile(String fileName, Style style)
    {
        Layer[] layers;
        
        LayerProcessor processor=new LayerProcessor();
        processor.readCsv(fileName);
        layers=processor.getLayers();
        style.setLayers(layers);
    }
    
    /**
     * Reads the JSON style file and exports the layers in it to CSV
     * @param jsonStyleFile Input file
     * @param csvLayerFile Output file
     */
    private void exportLayersToCsvFile(String jsonStyleFile, String csvLayerFile)
    {
        Style style;
        
        style=readJsonStyleFile(jsonStyleFile);
        writeCsvFile(csvLayerFile, style);        
    }
    
    /**
     * This method inserts layers read from CSV into a JSON style file.
     * Existing layers are replaced
     * @param jsonStyleFile Input JSON style file
     * @param csvLayerFile Input layers CSV file
     * @param outputJsonStyleFile Output file
     */
    private void insertLayersToStyleFile(String jsonStyleFile, String csvLayerFile, String outputJsonStyleFile)
    {
        Style style;
        
        style=readJsonStyleFile(jsonStyleFile);
        readCsvFile(csvLayerFile, style);

        writeJsonStyleFile(outputJsonStyleFile, style);        
    }
    
    
    /**
     * Main program
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        Style   style;
        boolean error;
        
        System.out.println("LAYER CONVERSION TOOL");
        
        StyleConvert instance=new StyleConvert();
        
        style=instance.readJsonStyleFile("style_topnl_org.json");
        instance.writeJsonStyleFile("style_topnl_org_rewrite.json", style);
        
        
        
        instance.exportLayersToCsvFile("style_topnl_org.json", "testlayers.csv");
        instance.insertLayersToStyleFile("style_topnl_org.json", "testlayers.csv", "style_topnl_test.json");
/*        

        error=false;
        if (args.length>0)
        {
            if (args[0].equals("INSERTLAYERS"))
            {
                if (args.length==4)
                {
                    System.out.println("Inserting layers from "+args[2]+" into "+args[1]+", write result to "+args[3]);
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
                    System.out.println("Exporting layers from "+args[1]+" to "+args[2]);
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
            System.out.println("Usage:");
            System.out.println("java -jar StyleConvert EXTRACTLAYERS [StyleJsonInFile] [LayerCsvOutFile]");
            System.out.println("Extracts the layers out the [StyleJsonInFile] and writes them to [LayerCsvOutFile]");
            System.out.println("java -jar StyleConvert INSERTLAYERS [StyleJsonInFile] [LayerCsvInFile] [StyleJsonOutFile]");
            System.out.println("Inserts the layers from [LayerCsvInFile] into [StyleJsonInFile] and writes it to a new style file [StyleJsonOutFile]");
        }
        System.out.println("done");
*/
    }
}
