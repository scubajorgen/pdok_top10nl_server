/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.FileWriter;
import java.io.FileReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;

import java.io.IOException;
import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Layout;
import net.studioblueplanet.styleconvert.data.Paint;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author jorgen
 */
public class LayerProcessor
{
    private static final String SEP=";";
    private Layer[] layers;
    
    public LayerProcessor()
    {
        
    }

    public Layer[] getLayers()
    {
        return layers;
    }

    public void setLayers(Layer[] layers)
    {
        this.layers = layers;
    }

    private String addIfNotNull(String line, String str)
    {
        if (str!=null)
        {
            line+=str+SEP;
        }
        else
        {
            line+=SEP;
        }
        return line;
    }

    private String addIfNotNull(String line, Boolean bool)
    {
        if (bool!=null)
        {
            line+=bool.toString()+SEP;
        }
        else
        {
            line+=SEP;
        }
        return line;
    }

    private String addIfNotNull(String line, Float fl)
    {
        if (fl!=null)
        {
            line+=fl.toString()+SEP;
        }
        else
        {
            line+=SEP;
        }
        return line;
    }

    private String addIfNotNull(String line, Integer i)
    {
        if (i!=null)
        {
            line+=i.toString()+SEP;
        }
        else
        {
            line+=SEP;
        }
        return line;
    }

    
    
    private String addIfNotNull(String line, JsonNode node)
    {
        if (node!=null)
        {
            line+=node.toString()+SEP;
        }
        else
        {
            line+=SEP;
        }
        return line;
    }
    
    /** 
     * Create JsonNode from string
     * @param string String to parse
     * @return The JsonNode or null if error.
     */
    private JsonNode jsonNodeFromString(String string)
    {
        JsonNode actualObj;
        actualObj=null;
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            actualObj = mapper.readTree(string.replaceAll("^([a-zA-Z#].*)", "\"$1\""));
        }
        catch (JsonProcessingException e)
        {
            System.err.println("Error parsing string for JsonNode: "+e.getMessage());
        }   
        return actualObj;
    }

    /**
     * Creates the header, the first line in the CSV
     * @return The CSV first line/header
     */
    private String constructHeader()
    {
        String line;
        
        line=new String();
        line+="id"+SEP;
        line+="type"+SEP;
        line+="source"+SEP;
        line+="source-layer"+SEP;
        line+="filter"+SEP;
        line+="line-cap"+SEP;
        line+="line-join"+SEP;
        line+="symbol-placement"+SEP;
        line+="symbol-avoid-edges"+SEP;
        line+="symbol-spacing"+SEP;
        line+="icon-image"+SEP;
        line+="icon-allow-overlap"+SEP;
        line+="text-field"+SEP;
        line+="text-font"+SEP;
        line+="background-color"+SEP;
        line+="fill-pattern"+SEP;
        line+="fill-color"+SEP;
        line+="fill-outline-color"+SEP;
        line+="line-color"+SEP;
        line+="line-width"+SEP;
        line+="line-opacity"+SEP;
        line+="text-color"+SEP;
        line+="text-halo-color"+SEP;
        line+="text-halo-width"+SEP;
        line+="text-halo-blur";
        return line;
    }
    
    /**
     * Write the layers to CSV file, so they can be edited in Excel
     * @param file The filename of the file write to
     */
    public void writeToCsvFile(String file)
    {
        int         i;
        String      line;
        JsonNode    node;
        Layout      layout;
        Paint       paint;
        
        
        try 
        {
            FileWriter myWriter = new FileWriter(file);
            
            myWriter.write(constructHeader()+"\n");

            i=0;
            while (i<layers.length)
            {
                line="";
                line+=layers[i].getId()+SEP;
                line+=layers[i].getType()+SEP;
                line=addIfNotNull(line, layers[i].getSource());
                line=addIfNotNull(line, layers[i].getSourceLayer());
                line=addIfNotNull(line, layers[i].getFilter());

                layout=layers[i].getLayout();
                if (layout!=null)
                {
                    line=addIfNotNull(line, layout.getLineCap());
                    line=addIfNotNull(line, layout.getLineJoin());
                    line=addIfNotNull(line, layout.getSymbolPlacement());
                    line=addIfNotNull(line, layout.getSymbolAvoidEdges());
                    line=addIfNotNull(line, layout.getSymbolSpacing());
                    line=addIfNotNull(line, layout.getIconImage());
                    line=addIfNotNull(line, layout.getIconAllowOverlap());
                    line=addIfNotNull(line, layout.getTextField());
                    line=addIfNotNull(line, layout.getTextFont());
                }
                else
                {
                    line+=SEP+SEP+SEP+SEP+SEP+SEP+SEP+SEP+SEP;
                }

                paint=layers[i].getPaint();
                if (paint!=null)
                {
                    line=addIfNotNull(line, paint.getBackgroundColor());
                    line=addIfNotNull(line, paint.getFillPattern());
                    line=addIfNotNull(line, paint.getFillColor());
                    line=addIfNotNull(line, paint.getFillOutlineColor());
                    line=addIfNotNull(line, paint.getLineColor());
                    line=addIfNotNull(line, paint.getLineWidth());
                    line=addIfNotNull(line, paint.getLineOpacity());
                    line=addIfNotNull(line, paint.getTextColor());
                    line=addIfNotNull(line, paint.getTextHaloColor());
                    line=addIfNotNull(line, paint.getTextHaloWidth());
                    line=addIfNotNull(line, paint.getTextHaloBlur());
                }
                else
                {
                    line+=SEP+SEP+SEP+SEP+SEP+SEP+SEP+SEP+SEP+SEP;
                }

                
                myWriter.write(line+"\n");
                i++;
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } 
        catch (IOException e) 
        {
            System.err.println("An error occurred: "+e.getMessage());
        }        
            
    }
    
    private boolean setString(String string, Consumer<String> setter)
    {
        boolean isSet;
        isSet=false;
        if (!string.equals(""))
        {
            setter.accept(string);
            isSet=true;
        }
        return isSet;
    }

    private boolean setBoolean(String string, Consumer<Boolean> setter)
    {
        boolean isSet;
        isSet=false;
        if (!string.equals(""))
        {
            setter.accept(Boolean.valueOf(string));
            isSet=true;
        }
        return isSet;
    }

    private boolean setFloat(String string, Consumer<Float> setter)
    {
        boolean isSet;
        isSet=false;
        if (!string.equals(""))
        {
            setter.accept(Float.valueOf(string));
            isSet=true;
        }
        return isSet;
    }

    private boolean setInt(String string, Consumer<Integer> setter)
    {
        boolean isSet;
        isSet=false;
        if (!string.equals(""))
        {
            setter.accept(Integer.valueOf(string));
            isSet=true;
        }
        return isSet;
    }

    private boolean setJsonNode(String string, Consumer<JsonNode> setter)
    {
        boolean isSet;
        isSet=false;
        if (!string.equals(""))
        {
            setter.accept(jsonNodeFromString(string));
            isSet=true;
        }
        return isSet;
    }
    
    public void readCsv(String fileName)
    {
        Layer       layer;
        int         i;
        String[]    layerStrings;
        boolean     isNotNull;
        Paint       paint;
        Layout      layout;
        
        try 
        {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName))
                    .withCSVParser(csvParser)   // custom CSV parser
                    .withSkipLines(1)           // skip the first line, header info
                    .build();
             
            List<String[]> r = reader.readAll();
            
            layers=new Layer[r.size()];
            
            i=0;
            while (i<r.size())
            {
                layer=new Layer();
                layerStrings=r.get(i);
                
                layer.setId(layerStrings[0]);
                layer.setType(layerStrings[1]);
                setString  (layerStrings[2], layer::setSource);
                setString  (layerStrings[3], layer::setSourceLayer);
                setJsonNode(layerStrings[4], layer::setFilter);
                
                layout=new Layout();
                isNotNull=false;
                isNotNull |= setString  (layerStrings[5], layout::setLineCap);
                isNotNull |= setString  (layerStrings[6], layout::setLineJoin);
                isNotNull |= setString  (layerStrings[7], layout::setSymbolPlacement);
                isNotNull |= setBoolean (layerStrings[8], layout::setSymbolAvoidEdges);
                isNotNull |= setFloat   (layerStrings[9], layout::setSymbolSpacing);
                isNotNull |= setString  (layerStrings[10], layout::setIconImage);
                isNotNull |= setBoolean (layerStrings[11], layout::setIconAllowOverlap);
                isNotNull |= setString  (layerStrings[12], layout::setTextField);
                isNotNull |= setJsonNode(layerStrings[13], layout::setTextFont);

                if (isNotNull)
                {
                    layer.setLayout(layout);
                }
                
                paint=new Paint();
                isNotNull=false;
                isNotNull |= setJsonNode(layerStrings[14], paint::setBackgroundColor);
                isNotNull |= setString  (layerStrings[15], paint::setFillPattern);
                isNotNull |= setJsonNode(layerStrings[16], paint::setFillColor);
                isNotNull |= setJsonNode(layerStrings[17], paint::setFillOutlineColor);
                isNotNull |= setJsonNode(layerStrings[18], paint::setLineColor);
                isNotNull |= setJsonNode(layerStrings[19], paint::setLineWidth);
                isNotNull |= setJsonNode(layerStrings[20], paint::setLineOpacity);
                isNotNull |= setJsonNode(layerStrings[21], paint::setTextColor);
                isNotNull |= setJsonNode(layerStrings[22], paint::setTextHaloColor);
                isNotNull |= setFloat   (layerStrings[23], paint::setTextHaloWidth);
                isNotNull |= setFloat   (layerStrings[24], paint::setTextHaloBlur);

                if (isNotNull)
                {
                    layer.setPaint(paint);
                }
                
                
                layers[i]=layer;
                i++;
            }
        }
        catch (Exception e)
        {
            System.err.println("An error occurred while importing CSV: "+e.getMessage());
        }
    }
}
