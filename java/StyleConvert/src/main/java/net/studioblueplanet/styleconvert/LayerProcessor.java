/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.List;
import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Layout;
import net.studioblueplanet.styleconvert.data.Paint;

/**
 *
 * @author jorgen
 */
public class LayerProcessor
{   private static final int    MAXFIELDS=38;
    private static final String SEP=";";
    private List<Layer>         layers;
    
    public LayerProcessor()
    {
        
    }

    public List<Layer>getLayers()
    {
        return layers;
    }

    public void setLayers(List<Layer>layers)
    {
        this.layers = layers;
    }

    private String convertToString(String str)
    {
        return str;
    }

    private String convertToString(Boolean bool)
    {
        String string;
        if (bool!=null)
        {
            string=bool.toString();
        }
        else
        {
            string=null;
        }
        return string;
    }

    private String convertToString(Float fl)
    {
        String string;
        if (fl!=null)
        {
            string=fl.toString();
        }
        else
        {
            string=null;
        }
        return string;
    }

    private String convertToString(Integer i)
    {
        String string;
        if (i!=null)
        {
            string=i.toString();
        }
        else
        {
            string=null;
        }
        return string;
    }

    
    
    private String convertToString(JsonNode node)
    {
        String string;
        if (node!=null)
        {
            string=node.toString();
        }
        else
        {
            string=null;
        }
        return string;
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
        line+="minzoom"+SEP;
        line+="maxzoom"+SEP;

        line+="fill-pattern"+SEP;
        line+="fill-color"+SEP;
        line+="fill-outline-color"+SEP;
        line+="fill-opacity"+SEP;

        line+="symbol-placement"+SEP;
        line+="symbol-avoid-edges"+SEP;
        line+="symbol-spacing"+SEP;

        line+="line-cap"+SEP;
        line+="line-join"+SEP;
        line+="line-color"+SEP;
        line+="line-width"+SEP;
        line+="line-gap-width"+SEP;
        line+="line-opacity"+SEP;
        line+="line-dasharray"+SEP;

        line+="icon-image"+SEP;
        line+="icon-allow-overlap"+SEP;
        line+="icon-offset"+SEP;

        line+="text-field"+SEP;
        line+="text-font"+SEP;
        line+="text-size"+SEP;
        line+="text-offset"+SEP;
        line+="text-anchor"+SEP;
        line+="text-max-width"+SEP;
        line+="text-transform"+SEP;
        line+="text-allow-overlap"+SEP;
        line+="text-line-height"+SEP;
        line+="text-color"+SEP;
        line+="text-halo-color"+SEP;
        line+="text-halo-width"+SEP;
        line+="text-halo-blur"+SEP;

        line+="background-color";
        return line;
    }
    
    /**
     * Write the layers to CSV file, so they can be edited in Excel
     * @param file The filename of the file write to
     */
    public void writeToCsvFile(String file)
    {
        int             j;
        String          line;
        JsonNode        node;
        Layout          layout;
        Paint           paint;
        Iterator<Layer> it;
        Layer           layer;
        
        
        
        
        try 
        {
            FileWriter myWriter = new FileWriter(file);
            
            myWriter.write(constructHeader()+"\n");

            it=layers.iterator();
            while (it.hasNext())
            {
                layer=it.next();
                String[] items=new String[MAXFIELDS];
                items[ 0]=layer.getId();

                items[ 1]=layer.getType();
                items[ 2]=convertToString(layer.getSource());
                items[ 3]=convertToString(layer.getSourceLayer());
                items[ 4]=convertToString(layer.getFilter());
                items[ 5]=convertToString(layer.getMinzoom());
                items[ 6]=convertToString(layer.getMaxzoom());

                layout=layer.getLayout();
                if (layout!=null)
                {
                    items[11]=convertToString(layout.getSymbolPlacement());
                    items[12]=convertToString(layout.getSymbolAvoidEdges());
                    items[13]=convertToString(layout.getSymbolSpacing());
                    
                    items[14]=convertToString(layout.getLineCap());
                    items[15]=convertToString(layout.getLineJoin());
                    
                    items[21]=convertToString(layout.getIconImage());
                    items[22]=convertToString(layout.getIconAllowOverlap());
                    items[23]=convertToString(layout.getIconOffset());
                    
                    items[24]=convertToString(layout.getTextField());
                    items[25]=convertToString(layout.getTextFont());
                    items[26]=convertToString(layout.getTextSize());
                    items[27]=convertToString(layout.getTextOffset());
                    items[28]=convertToString(layout.getTextAnchor());
                    items[29]=convertToString(layout.getTextMaxWidth());
                    items[30]=convertToString(layout.getTextTransform());
                    items[31]=convertToString(layout.getTextAllowOverlap());
                    items[32]=convertToString(layout.getTextLineHeight());
                }


                paint=layer.getPaint();
                if (paint!=null)
                {
                    items[ 7]=convertToString(paint.getFillPattern());
                    items[ 8]=convertToString(paint.getFillColor());
                    items[ 9]=convertToString(paint.getFillOutlineColor());
                    items[10]=convertToString(paint.getFillOpacity());

                    items[16]=convertToString(paint.getLineColor());
                    items[17]=convertToString(paint.getLineWidth());
                    items[18]=convertToString(paint.getLineGapWidth());
                    items[19]=convertToString(paint.getLineOpacity());
                    items[20]=convertToString(paint.getLineDasharray());

                    items[33]=convertToString(paint.getTextColor());
                    items[34]=convertToString(paint.getTextHaloColor());
                    items[35]=convertToString(paint.getTextHaloWidth());
                    items[36]=convertToString(paint.getTextHaloBlur());
                    
                    items[37]=convertToString(paint.getBackgroundColor());
                }

                j=0;
                while (j<MAXFIELDS)
                {
                    if (items[j]!=null)
                    {
                        myWriter.write(items[j]);
                    }
                    if (j<MAXFIELDS-1)
                    {
                        myWriter.write(SEP);
                    }
                    j++;
                }
                
                myWriter.write("\n");
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
            
            layers=new ArrayList<>();
            
            i=0;
            while (i<r.size())
            {
                layerStrings=r.get(i);
                
                if (!layerStrings[0].equals(""))
                {
                    layer=new Layer();
                    layer.setId(layerStrings[0]);
                    layer.setType(layerStrings[1]);
                    setString  (layerStrings[2], layer::setSource);
                    setString  (layerStrings[3], layer::setSourceLayer);
                    setJsonNode(layerStrings[4], layer::setFilter);
                    setFloat   (layerStrings[5], layer::setMinzoom);
                    setFloat   (layerStrings[6], layer::setMaxzoom);

                    layout=new Layout();
                    isNotNull=false;
                    isNotNull |= setString  (layerStrings[11], layout::setSymbolPlacement);
                    isNotNull |= setBoolean (layerStrings[12], layout::setSymbolAvoidEdges);
                    isNotNull |= setFloat   (layerStrings[13], layout::setSymbolSpacing);

                    isNotNull |= setString  (layerStrings[14], layout::setLineCap);
                    isNotNull |= setString  (layerStrings[15], layout::setLineJoin);

                    isNotNull |= setString  (layerStrings[21], layout::setIconImage);
                    isNotNull |= setBoolean (layerStrings[22], layout::setIconAllowOverlap);
                    isNotNull |= setJsonNode(layerStrings[23], layout::setIconOffset);

                    isNotNull |= setString  (layerStrings[24], layout::setTextField);
                    isNotNull |= setJsonNode(layerStrings[25], layout::setTextFont);
                    isNotNull |= setJsonNode(layerStrings[26], layout::setTextSize);
                    isNotNull |= setJsonNode(layerStrings[27], layout::setTextOffset);
                    isNotNull |= setString  (layerStrings[28], layout::setTextAnchor);
                    isNotNull |= setFloat   (layerStrings[29], layout::setTextMaxWidth);
                    isNotNull |= setString  (layerStrings[30], layout::setTextTransform);
                    isNotNull |= setBoolean (layerStrings[31], layout::setTextAllowOverlap);
                    isNotNull |= setJsonNode(layerStrings[32], layout::setTextLineHeight);

                    if (isNotNull)
                    {
                        layer.setLayout(layout);
                    }

                    paint=new Paint();
                    isNotNull=false;
                    isNotNull |= setString  (layerStrings[ 7], paint::setFillPattern);
                    isNotNull |= setJsonNode(layerStrings[ 8], paint::setFillColor);
                    isNotNull |= setJsonNode(layerStrings[ 9], paint::setFillOutlineColor);
                    isNotNull |= setJsonNode(layerStrings[10], paint::setFillOpacity);

                    isNotNull |= setJsonNode(layerStrings[16], paint::setLineColor);
                    isNotNull |= setJsonNode(layerStrings[17], paint::setLineWidth);
                    isNotNull |= setJsonNode(layerStrings[18], paint::setLineGapWidth);
                    isNotNull |= setJsonNode(layerStrings[19], paint::setLineOpacity);
                    isNotNull |= setJsonNode(layerStrings[20], paint::setLineDasharray);

                    isNotNull |= setJsonNode(layerStrings[33], paint::setTextColor);
                    isNotNull |= setJsonNode(layerStrings[34], paint::setTextHaloColor);
                    isNotNull |= setFloat   (layerStrings[35], paint::setTextHaloWidth);
                    isNotNull |= setFloat   (layerStrings[36], paint::setTextHaloBlur);

                    isNotNull |= setJsonNode(layerStrings[37], paint::setBackgroundColor);

                    if (isNotNull)
                    {
                        layer.setPaint(paint);
                    }

                    layers.add(layer);
                }
                i++;
            }
            System.out.println("Lines read: "+i+", layers written: "+layers.size());
        }
        catch (Exception e)
        {
            System.err.println("An error occurred while importing CSV: "+e.getMessage());
        }
    }
}
