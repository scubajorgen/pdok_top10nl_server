/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorgen
 */
public class Settings
{
    private final static Logger     LOGGER = LogManager.getLogger(Settings.class);
    private static Settings         theInstance;
    private static String           userDefinedPropertyFile =null;
    private final static String     PROPERTY_FILE_NAME      ="/StyleConvert.properties";
    private String                  csvSeparator;
    private String                  gpkgSourceDir;
    private String                  topnlRelease;
    private String                  templateJson;
    private String                  inputLayersExcel;
    private String                  configJson;
    private String                  outputLayersCsv;
    private String                  mode;
    private Boolean                 enableFilterProcessing  =true;
    private static final String     DEFAULT_CSV_SEPARATOR   =";";   
    
    /**
     * Constructor, reads the property file
     */
    private Settings()
    {
        readSettings();
        dumpSettings();
    }
    
    public static void setUserDefinedPropertyFile(String filename)
    {
        userDefinedPropertyFile=filename;
        theInstance=null;
    }
    
    /**
     * Returns the one and only instance of this class (Singleton pattern)
     * @return The instance
     */
    public static Settings getInstance()
    {
        if (theInstance==null)
        {
            theInstance=new Settings();
        }
        return theInstance;
    }
    
    /**
     * Read the settings from the property file
     */
    private void readSettings()
    {
        InputStream inputStream;
        Properties properties=new Properties();
        Properties prop = new Properties();
        try  
        {
            if (userDefinedPropertyFile==null)
            {
                inputStream = new FileInputStream(PROPERTY_FILE_NAME);
            }
            else
            {
                inputStream=new FileInputStream(userDefinedPropertyFile);
            }
            prop.load(inputStream);
            csvSeparator            =prop.getProperty("csvSeparator");
            if (csvSeparator==null)
            {
                csvSeparator        =DEFAULT_CSV_SEPARATOR;
            }
            gpkgSourceDir           =prop.getProperty("gpkgSourceDir");
            topnlRelease            =prop.getProperty("topnlRelease");
            inputLayersExcel        =prop.getProperty("inputLayersExcel");
            templateJson            =prop.getProperty("templateJson");
            configJson              =prop.getProperty("configJson");
            outputLayersCsv         =prop.getProperty("outputLayersCsv");
            mode                    =prop.getProperty("mode");
            enableFilterProcessing  =Boolean.parseBoolean(prop.getProperty("enableFilterProcessing"));

        } 
        catch (FileNotFoundException e) 
        {
            csvSeparator=DEFAULT_CSV_SEPARATOR;
            LOGGER.error("Properties file {} not found in resource dir: {}", PROPERTY_FILE_NAME, e.getMessage());
        } 
        catch (IOException e) 
        {
            csvSeparator=DEFAULT_CSV_SEPARATOR;
            LOGGER.error("Error reading Properties file {}: {}", PROPERTY_FILE_NAME, e.getMessage());
        }        
    }
    
    /**
     * Dump an overview of the settings
     */
    private void dumpSettings()
    {
        LOGGER.info("topnlRelease       : {}", topnlRelease);
        LOGGER.info("GPKG Source        : {}", gpkgSourceDir);
        LOGGER.info("CSV Separator      : {}", csvSeparator);
        LOGGER.info("Template JSON      : {}", templateJson);
        LOGGER.info("Output JSON        : {}", configJson);
        LOGGER.info("Output CSV         : {}", outputLayersCsv);
        LOGGER.info("Mode               : {}", mode);
        LOGGER.info("Filter processing  : {}", enableFilterProcessing);
        LOGGER.info("Layers Excel       : {}", inputLayersExcel);
    }

    public String getCsvSeparator()
    {
        return csvSeparator;
    }

    public String getGpkgSourceDir()
    {
        return gpkgSourceDir;
    }

    public String getTopnlRelease()
    {
        return topnlRelease;
    }

    public String getTemplateJson()
    {
        return templateJson;
    }

    public String getInputLayersExcel()
    {
        return inputLayersExcel;
    }

    public String getConfigJson()
    {
        return configJson;
    }

    public String getOutputLayersCsv()
    {
        return outputLayersCsv;
    }

    public String getMode()
    {
        return mode;
    }

    public Boolean getEnableFilterProcessing()
    {
        return enableFilterProcessing;
    }
}
