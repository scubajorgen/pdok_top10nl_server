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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Quick and especially DIRTY conversion tool. Note that it was intended for
 * own use. Not all style fields and attributes are supported. Only a subset
 * @author jorgen
 */
public class StyleConvert
{
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
            System.err.println("Error creating file "+csvLayerFile+": "+e.getMessage());
        }
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
        FileProcessor processor=new FileProcessor();
        
        Style style=processor.readJsonStyleFile(jsonStyleFile);
        processor.readCsvFile(csvLayerFile, style);
        
        try
        {
            Writer writer=new OutputStreamWriter(new FileOutputStream(outputJsonStyleFile), StandardCharsets.UTF_8);
            processor.writeJsonStyleFile(writer, style);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            System.err.println("Error creating file "+csvLayerFile+": "+e.getMessage());
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
        
        System.out.println("LAYER CONVERSION TOOL");
        
        StyleConvert instance=new StyleConvert();
        instance.readSettings();
      
/*        
        style=instance.readJsonStyleFile("style_topnl_org.json");
        instance.writeJsonStyleFile("style_topnl_org_rewrite.json", style);
       
        instance.exportLayersToCsvFile("style_topnl_org.json", "testlayers.csv");
        instance.insertLayersToStyleFile("style_topnl_org.json", "testlayers.csv", "style_topnl_test.json");
*/       

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
    }
}
