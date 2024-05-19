/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.studioblueplanet.styleconvert.data.Layer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorgen
 */
public class FilterExtension
{

    private final static Logger             LOGGER = LogManager.getLogger(FilterExtension.class);
    private Map<String, String>             tables;
    private final Map<String, List<String>> distinctValueCache;

    /**
     * Constructor, makes an inventory of the source GPGK files
     */
    public FilterExtension()
    {
        distinctValueCache  =new HashMap<>();
        String sourceDir    = Settings.getInstance().getGpkgSourceDir();
        LOGGER.debug("Processing source dir {}", sourceDir);
        if (sourceDir!=null)
        {
            File sourceDirFile=new File(sourceDir);
            if (sourceDirFile.exists() && sourceDirFile.isDirectory() && sourceDirFile.list().length>0)
            {
                readGpgkLayerInventory(sourceDir);
                LOGGER.info("Made inventory of all GPKG files in source dir: {} tables found", tables.size());
            }
            else
            {
                LOGGER.error("No valid GPKG directory defined");
            }
        }
        else
        {
                LOGGER.error("No GPKG source directory defined (gpkgSourceDir)");
        }
    }

    /**
     * Given the database file, this method adds the table names to the tables
     * map, including the file name.
     *
     * @param file Database (sqlite) file to process.
     */
    private void readGpkgLayers(File file)
    {
        Connection connection = null;
        ResultSet resultSet = null;

        try
        {
            Class.forName("org.sqlite.JDBC");
            connection  = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            resultSet   = connection.getMetaData().getTables(null, null, null, null);
            while (resultSet.next())
            {
                String tableName = resultSet.getString("TABLE_NAME");
                if (tableName.startsWith("top"))
                {
                    LOGGER.debug("Table added: {}: {}", file.getAbsolutePath(), tableName);
                    tables.put(tableName, file.getAbsolutePath());
                }
            }
            
        } 
        catch (Exception e)
        {
            LOGGER.error("Error reading database: {}", e.getMessage());
        } 
        finally
        {
            try
            {
                resultSet.close();
                connection.close();
            } 
            catch (Exception e)
            {
                LOGGER.error("Error closing database connection: {}", e.getMessage());
            }
        }
    }

    /**
     * Creates an inventory of GPKG layers/TABLES and the file they are in
     */
    private void readGpgkLayerInventory(String sourceDir)
    {
        tables = new HashMap<>();
        File dir = new File(sourceDir);
        try
        {
            if (dir.getCanonicalFile().isDirectory())
            {
                for (File file : dir.getCanonicalFile().listFiles())
                {
                    if (file.isFile() && file.getAbsolutePath().toLowerCase().endsWith(".gpkg"))
                    {
                        LOGGER.debug("Gpkg inventory, processing {}", file.getName());
                        readGpkgLayers(file);
                    } else
                    {
                        LOGGER.debug("Skipping {} during gpkg inventory: doesn't appear to be a .gpkg file", file.getName());
                    }
                }
            } 
            else
            {
                LOGGER.error("The source directory {} doesn't appear to be a directory", sourceDir);
            }
        }
        catch (Exception e)
        {
            
        }
    }

    /**
     * This method returns a list of distinct values for the given attribute
     * in the given table in the given database file.
     * Since retrieving a list of distinct values is an expensive operation,
     * the result is stored in cache. On entry, first the cache is checked if the
     * for given attribute the values have been requested before. In that situation
     * the cached value is returned. Otherwise the distinct values are retrieved
     * from the database, stored in cache and returned.
     * @param filename Database file
     * @param table Table name
     * @param attribute Attribute to look for
     * @return A list of distinct values
     */
    private List<String> getDistinctAttributeValues(String filename, String table, String attribute)
    {
        List<String> distinctValues =null;
        List<String> cachedValue    =distinctValueCache.get(table+"|"+attribute);
        if (cachedValue!=null)
        {
            LOGGER.debug("Distinct values: get array from cache for table '{}' attribute '{}'", table, attribute);
            distinctValues=cachedValue;
        }
        else
        {
            LOGGER.debug("Distinct values: get array from database for table '{}' attribute '{}'", table, attribute);
            distinctValues          =new ArrayList<>();

            Connection connection   = null;
            ResultSet resultSet     = null;
            Statement statement     = null;
            try
            {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
                statement = connection.createStatement();
                String query="SELECT DISTINCT " + attribute + " FROM " + table + " ORDER BY " + attribute;
                LOGGER.debug("Execting query. Database '{}' Query '{}'", filename, query);
                resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    String attributeValue=resultSet.getString(attribute);
                    LOGGER.debug("Distinct attribute value : {}", attributeValue);
                    distinctValues.add(attributeValue);
                }
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            } 
            finally
            {
                try
                {
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            distinctValueCache.put(table+"|"+attribute, distinctValues);
            LOGGER.debug("Distinct values: array added to cache for table '{}' attribute '{}'", table, attribute);
        }
        return distinctValues;
    }

    /**
     * This method filters the list of distinct values and returns only the values
     * that contain one or more of the filter values as substring
     * @param distinctValues List of distinct values
     * @param filterValues List of values to be filtered upon
     * @return Sorted list of distinct values containing filter values
     */
    public List<String> filterDistinctValues(List<String> distinctValues, List<String> filterValues)
    {
        List<String> filtered=new ArrayList<>();
        
        for (String filter : filterValues)
        {
            for (String value : distinctValues)
            {
                String parts[]=value.split("[|]");
                for (String part : parts)
                {
                    if (part.equals(filter))
                    {
                        if (!filtered.contains(value))
                        {
                            filtered.add(value);
                        }                        
                    }
                }
/*                
                if (value.contains(filter))
                {

                }
*/                
            }
        }
        filtered.sort(String::compareTo);
        return filtered;
    }
    
    /**
     * This method offers a replacement for the filter
     *
     * @param gpkgLayer Layer to use
     * @param gpkgAttribute Attribute to process
     * @param filterValues Values to look for
     * @return The filter replacement, as JSON node
     */
    public JsonNode filterReplacementIn(String gpkgLayer, String gpkgAttribute, List<String> filterValues, boolean negate)
    {
        JsonNode newFilter = null;

        if (tables==null)
        {
            LOGGER.fatal("Unable to replace filter");
            System.exit(0);
        }
        String filename = tables.get(gpkgLayer);

        if (filename != null)
        {
            List<String> distinctAttributeValues=getDistinctAttributeValues(filename, gpkgLayer, gpkgAttribute);
            List<String> filteredValues=filterDistinctValues(distinctAttributeValues, filterValues);
            if (filteredValues.size()>0)
            {
                List<String> result=new ArrayList<>();
                if (negate)
                {
                    result.add("!in");
                }
                else
                {
                    result.add("in");
                }
                result.add(gpkgAttribute);
                result.addAll(filteredValues);
                ObjectMapper mapper=new ObjectMapper();
                newFilter = mapper.valueToTree(result);
                LOGGER.debug("Filter processed: {}", newFilter.toString());                
            }
            else
            {
                LOGGER.fatal("Processing _IN filter results in empty filter. Layer: {} Attribute: {} Values: {}", 
                             gpkgLayer, gpkgAttribute, filterValues);
                System.exit(0);
            }
        } 
        else
        {
            LOGGER.error("Layer {} not found!", gpkgLayer);
        }

        return newFilter;
    }

    /**
     * 
     * @param gpkgLayer
     * @param layerId
     * @param filter
     * @return 
     */
    public JsonNode processFilter(Layer layer, String gpkgLayer, JsonNode filter)
    {
        JsonNode newFilter=null;
        String filterCommand = filter.get(0).asText();
        if ("_IN".equals(filterCommand))
        {
            String attribute = filter.get(1).asText();
            List<String> values = new ArrayList<>();
            for (int i = 2; i < filter.size(); i++)
            {
                String value = filter.get(i).asText();
                values.add(value);
            }
            newFilter=filterReplacementIn(gpkgLayer, attribute, values, false);
        }
        else if ("!_IN".equals(filterCommand))
        {
            String attribute = filter.get(1).asText();
            List<String> values = new ArrayList<>();
            for (int i = 2; i < filter.size(); i++)
            {
                String value = filter.get(i).asText();
                values.add(value);
            }
            newFilter=filterReplacementIn(gpkgLayer, attribute, values, true);
        }
        return newFilter;
    }
    
    
    
    /**
     * This method processes all filters. If a filter has the format ["_IN",
     * "attribute", "value1","value2",...] it replaces it by ["in", "attribute",
     * "distinctvalue1", "distinctvalue2", ...]. distinctvalues are values of
     * the attribute that contains one of the values.
     *
     * @param layers
     */
    public void processFilters(List<Layer> layers)
    {
        LOGGER.info("Processing custom filters");
        for (Layer layer : layers)
        {
            JsonNode filter = layer.getFilter();

            if (filter != null && filter.isArray())
            {

                String filterCommand = filter.get(0).asText();
                String gpkgLayer = layer.getSourceLayer();
                if ("all".equals(filterCommand) || "any".equals(filterCommand))
                {
                    boolean changed=false;
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayNode convertedFilter = mapper.createArrayNode();  
                    convertedFilter.add(filter.get(0));
                    for(int i=1; i<filter.size(); i++)
                    {
                        JsonNode subFilter=filter.get(i);
                        JsonNode newFilter=processFilter(layer, gpkgLayer, subFilter);
                        if (newFilter!=null)
                        {
                            convertedFilter.add(newFilter);
                            changed=true;
                        }
                        else
                        {
                            convertedFilter.add(subFilter);
                        }
                    }
                    if (changed)
                    {
                        layer.setFilter(convertedFilter);
                        LOGGER.info("Layer ID '{}': filter '{}' replaced by '{}'", layer.getId(), filter.toString(), convertedFilter.toString());
                    }
                }
                else
                {
                    JsonNode newFilter=processFilter(layer, gpkgLayer, filter);
                    if (newFilter!=null)
                    {
                        layer.setFilter(newFilter);
                        LOGGER.info("Layer ID '{}': filter '{}' replaced by '{}'", layer.getId(), filter.toString(), newFilter.toString());
                    }
                }
            }

        }
    }

}
