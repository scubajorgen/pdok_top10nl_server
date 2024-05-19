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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.List;
import net.studioblueplanet.styleconvert.data.Layer;
import net.studioblueplanet.styleconvert.data.Layout;
import net.studioblueplanet.styleconvert.data.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author jorgen
 */
public class LayerProcessor
{   
    private final static Logger LOGGER = LogManager.getLogger(LayerProcessor.class);
    private static final int    MAXFIELDS=41;
    
    private static final int    COLUMN_ID              =0;
    private static final int    COLUMN_TYPE            =1;
    private static final int    COLUMN_SOURCE          =2;
    private static final int    COLUMN_SOURCELAYER     =3;
    private static final int    COLUMN_FILTER          =4;
    private static final int    COLUMN_MINZOOM         =5;
    private static final int    COLUMN_MAXZOOM         =6;
    
    // Paint
    private static final int    COLUMN_FILLPATTERN          =7;
    private static final int    COLUMN_FILLCOLOR            =8;
    private static final int    COLUMN_FILLOUTLINECOLOR     =9;
    private static final int    COLUMN_FILLOPACITY          =10;
    private static final int    COLUMN_LINECOLOR            =16;
    private static final int    COLUMN_LINEWIDTH            =17;
    private static final int    COLUMN_LINEGAPWIDTH         =18;
    private static final int    COLUMN_LINEOPACITY          =19;
    private static final int    COLUMN_LINEDASHARRAY        =20;
    private static final int    COLUMN_TEXTCOLOR            =36;
    private static final int    COLUMN_TEXTHALOCOLOR        =37;
    private static final int    COLUMN_TEXTHALOWIDTH        =38;
    private static final int    COLUMN_TEXTHALOBLUR         =39;
    private static final int    COLUMN_BACKGROUNDCOLOR      =40;
    
    // Layout
    private static final int    COLUMN_SYMBOLPLACEMENT      =11;
    private static final int    COLUMN_SYMBOLAVOIDEDGES     =12;
    private static final int    COLUMN_SYMBOLSPACING        =13;
    private static final int    COLUMN_LINECAP              =14;
    private static final int    COLUMN_LINEJOIN             =15;
    private static final int    COLUMN_ICONIMAGE            =21;
    private static final int    COLUMN_ICONALLOWOVERLAP     =22;
    private static final int    COLUMN_ICONOFFSET           =23;
    private static final int    COLUMN_ICONTEXTFIT          =24;
    private static final int    COLUMN_ICONTEXTFITPADDING   =25;

    private static final int    COLUMN_TEXTFIELD            =26;
    private static final int    COLUMN_TEXTFONT             =27;
    private static final int    COLUMN_TEXTSIZE             =28;
    private static final int    COLUMN_TEXTOFFSET           =29;
    private static final int    COLUMN_TEXTANCHOR           =30;
    private static final int    COLUMN_TEXTMAXWIDTH         =31;
    private static final int    COLUMN_TEXTTRANSFORM        =32;
    private static final int    COLUMN_TEXTALLOWOVERLAP     =33;
    private static final int    COLUMN_TEXTLINEHEIGHT       =34;
    private static final int    COLUMN_TEXTLETTERSPACING    =35;
    
    private final String        csvSeparator;
    private List<Layer>         layers;
    
    /**
     * Constructor
     * @param csvSeparator Separator character to use for CSV files
     */
    public LayerProcessor(String csvSeparator)
    {
        this.csvSeparator=csvSeparator;
    }

    /**
     * Returns the list of layers
     * @return The list of layers
     */
    public List<Layer>  getLayers()
    {
        return layers;
    }

    /**
     * Defines the list of layers; existing layers are scratched
     * @param layers The new list of layers
     */
    public void setLayers(List<Layer>layers)
    {
        this.layers = layers;
    }

    /**
     * Convert string to string
     * @param str String to convert
     * @return The string
     */
    private String convertToString(String str)
    {
        return str;
    }

    /**
     * Converts boolean to string
     * @param bool Boolean to convert
     * @return The converted boolean or null 
     */
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

    /**
     * Convert float to string
     * @param fl Float to convert
     * @return The string or null
     */
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

    /**
     * Convert integer to string
     * @param i Integer to convert
     * @return The string or null
     */
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

    
    /**
     * Convert JSON node to string
     * @param node The node to convert
     * @return The node as string or null if the node was null
     */
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
            LOGGER.error("Error parsing string for JsonNode: {}", e.getMessage());
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
        line+="id"+csvSeparator;
        line+="type"+csvSeparator;
        line+="source"+csvSeparator;
        line+="source-layer"+csvSeparator;
        line+="filter"+csvSeparator;
        line+="minzoom"+csvSeparator;
        line+="maxzoom"+csvSeparator;

        line+="fill-pattern"+csvSeparator;
        line+="fill-color"+csvSeparator;
        line+="fill-outline-color"+csvSeparator;
        line+="fill-opacity"+csvSeparator;

        line+="symbol-placement"+csvSeparator;
        line+="symbol-avoid-edges"+csvSeparator;
        line+="symbol-spacing"+csvSeparator;

        line+="line-cap"+csvSeparator;
        line+="line-join"+csvSeparator;
        line+="line-color"+csvSeparator;
        line+="line-width"+csvSeparator;
        line+="line-gap-width"+csvSeparator;
        line+="line-opacity"+csvSeparator;
        line+="line-dasharray"+csvSeparator;

        line+="icon-image"+csvSeparator;
        line+="icon-allow-overlap"+csvSeparator;
        line+="icon-offset"+csvSeparator;
        line+="icon-text-fit"+csvSeparator;
        line+="icon-text-fit-padding"+csvSeparator;

        line+="text-field"+csvSeparator;
        line+="text-font"+csvSeparator;
        line+="text-size"+csvSeparator;
        line+="text-offset"+csvSeparator;
        line+="text-anchor"+csvSeparator;
        line+="text-max-width"+csvSeparator;
        line+="text-transform"+csvSeparator;
        line+="text-allow-overlap"+csvSeparator;
        line+="text-line-height"+csvSeparator;
        line+="text-letter-spacing"+csvSeparator;
        line+="text-color"+csvSeparator;
        line+="text-halo-color"+csvSeparator;
        line+="text-halo-width"+csvSeparator;
        line+="text-halo-blur"+csvSeparator;

        line+="background-color";
        return line;
    }
    
    /**
     * Write the layers to CSV file, so they can be edited in Excel
     * @param file The filename of the file write to
     */
    public void writeToCsvFile(Writer writer)
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
            writer.write(constructHeader()+"\n");

            it=layers.iterator();
            while (it.hasNext())
            {
                layer=it.next();
                String[] items=new String[MAXFIELDS];
                String comment=layer.getComment();
                if (comment!=null)
                {
                    items[0]=comment;
                }
                else
                {
                    items[COLUMN_ID]            =layer.getId();

                    items[COLUMN_TYPE]          =layer.getType();
                    items[COLUMN_SOURCE]        =convertToString(layer.getSource());
                    items[COLUMN_SOURCELAYER]   =convertToString(layer.getSourceLayer());
                    items[COLUMN_FILTER]        =convertToString(layer.getFilter());
                    items[COLUMN_MINZOOM]       =convertToString(layer.getMinzoom());
                    items[COLUMN_MAXZOOM]       =convertToString(layer.getMaxzoom());

                    layout=layer.getLayout();
                    if (layout!=null)
                    {
                        items[COLUMN_SYMBOLPLACEMENT]   =convertToString(layout.getSymbolPlacement());
                        items[COLUMN_SYMBOLAVOIDEDGES]  =convertToString(layout.getSymbolAvoidEdges());
                        items[COLUMN_SYMBOLSPACING]     =convertToString(layout.getSymbolSpacing());

                        items[COLUMN_LINECAP]           =convertToString(layout.getLineCap());
                        items[COLUMN_LINEJOIN]          =convertToString(layout.getLineJoin());

                        items[COLUMN_ICONIMAGE]         =convertToString(layout.getIconImage());
                        items[COLUMN_ICONALLOWOVERLAP]  =convertToString(layout.getIconAllowOverlap());
                        items[COLUMN_ICONOFFSET]        =convertToString(layout.getIconOffset());
                        items[COLUMN_ICONTEXTFIT]       =convertToString(layout.getIconTextFit());
                        items[COLUMN_ICONTEXTFITPADDING]=convertToString(layout.getIconTextFitPadding());

                        items[COLUMN_TEXTFIELD]         =convertToString(layout.getTextField());
                        items[COLUMN_TEXTFONT]          =convertToString(layout.getTextFont());
                        items[COLUMN_TEXTSIZE]          =convertToString(layout.getTextSize());
                        items[COLUMN_TEXTOFFSET]        =convertToString(layout.getTextOffset());
                        items[COLUMN_TEXTANCHOR]        =convertToString(layout.getTextAnchor());
                        items[COLUMN_TEXTMAXWIDTH]      =convertToString(layout.getTextMaxWidth());
                        items[COLUMN_TEXTTRANSFORM]     =convertToString(layout.getTextTransform());
                        items[COLUMN_TEXTALLOWOVERLAP]  =convertToString(layout.getTextAllowOverlap());
                        items[COLUMN_TEXTLINEHEIGHT]    =convertToString(layout.getTextLineHeight());
                        items[COLUMN_TEXTLETTERSPACING] =convertToString(layout.getTextLetterSpacing());
                    }


                    paint=layer.getPaint();
                    if (paint!=null)
                    {
                        items[COLUMN_FILLPATTERN]       =convertToString(paint.getFillPattern());
                        items[COLUMN_FILLCOLOR]         =convertToString(paint.getFillColor());
                        items[COLUMN_FILLOUTLINECOLOR]  =convertToString(paint.getFillOutlineColor());
                        items[COLUMN_FILLOPACITY]       =convertToString(paint.getFillOpacity());

                        items[COLUMN_LINECOLOR]         =convertToString(paint.getLineColor());
                        items[COLUMN_LINEWIDTH]         =convertToString(paint.getLineWidth());
                        items[COLUMN_LINEGAPWIDTH]      =convertToString(paint.getLineGapWidth());
                        items[COLUMN_LINEOPACITY]       =convertToString(paint.getLineOpacity());
                        items[COLUMN_LINEDASHARRAY]     =convertToString(paint.getLineDasharray());

                        items[COLUMN_TEXTCOLOR]         =convertToString(paint.getTextColor());
                        items[COLUMN_TEXTHALOCOLOR]     =convertToString(paint.getTextHaloColor());
                        items[COLUMN_TEXTHALOWIDTH]     =convertToString(paint.getTextHaloWidth());
                        items[COLUMN_TEXTHALOBLUR]      =convertToString(paint.getTextHaloBlur());

                        items[COLUMN_BACKGROUNDCOLOR]   =convertToString(paint.getBackgroundColor());
                    }
                }
                j=0;
                while (j<MAXFIELDS)
                {
                    if (items[j]!=null)
                    {
                        writer.write(items[j]);
                    }
                    if (j<MAXFIELDS-1)
                    {
                        writer.write(csvSeparator);
                    }
                    j++;
                }
                writer.write("\n");
            }
            LOGGER.info("Successfully wrote to the file.");
        } 
        catch (IOException e) 
        {
            LOGGER.error("An error occurred: {}", e.getMessage());
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

    private boolean setBoolean(Boolean value, Consumer<Boolean> setter)
    {
        boolean isSet;
        isSet=false;
        if (value!=null)
        {
            setter.accept(value);
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

    private boolean setFloat(Float value, Consumer<Float> setter)
    {
        boolean isSet;
        isSet=false;
        if (value!=null)
        {
            setter.accept(value);
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
            CSVParser csvParser = new CSVParserBuilder().withSeparator(csvSeparator.charAt(0)).build(); // custom separator
            
            // Apparently we need ISO_8859_1 to read Excel generated CSVs...
            Reader fileReader=new InputStreamReader(new FileInputStream(fileName), StandardCharsets.ISO_8859_1);
            CSVReader reader    = new CSVReaderBuilder(fileReader)
                    .withCSVParser(csvParser)   // custom CSV parser
                    .withSkipLines(1)           // skip the first line, header info
                    .build();
             
            List<String[]> r = reader.readAll();
         
            layers=new ArrayList<>();
            
            i=0;
            while (i<r.size())
            {
                layerStrings=r.get(i);
                
                if (!layerStrings[COLUMN_ID].equals(""))
                {
                    
                    if (layerStrings[COLUMN_ID].startsWith("#"))
                    {
                        // TO DO: Do something sensible with comment
                        LOGGER.info(layerStrings[COLUMN_ID]);
                    }
                    else
                    {
                        layer=new Layer();
                        layer.setId(layerStrings[COLUMN_ID]);
                        layer.setType(layerStrings[COLUMN_TYPE]);
                        setString  (layerStrings[COLUMN_SOURCE]         , layer::setSource);
                        setString  (layerStrings[COLUMN_SOURCELAYER]    , layer::setSourceLayer);
                        setJsonNode(layerStrings[COLUMN_FILTER]         , layer::setFilter);
                        setFloat   (layerStrings[COLUMN_MINZOOM]        , layer::setMinzoom);
                        setFloat   (layerStrings[COLUMN_MAXZOOM]        , layer::setMaxzoom);

                        layout=new Layout();
                        isNotNull=false;
                        isNotNull |= setString  (layerStrings[COLUMN_SYMBOLPLACEMENT]   , layout::setSymbolPlacement);
                        isNotNull |= setBoolean (layerStrings[COLUMN_SYMBOLAVOIDEDGES]  , layout::setSymbolAvoidEdges);
                        isNotNull |= setFloat   (layerStrings[COLUMN_SYMBOLSPACING]     , layout::setSymbolSpacing);

                        isNotNull |= setString  (layerStrings[COLUMN_LINECAP]           , layout::setLineCap);
                        isNotNull |= setString  (layerStrings[COLUMN_LINEJOIN]          , layout::setLineJoin);

                        isNotNull |= setString  (layerStrings[COLUMN_ICONIMAGE]         , layout::setIconImage);
                        isNotNull |= setBoolean (layerStrings[COLUMN_ICONALLOWOVERLAP]  , layout::setIconAllowOverlap);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_ICONOFFSET]        , layout::setIconOffset);
                        isNotNull |= setString  (layerStrings[COLUMN_ICONTEXTFIT]       , layout::setIconTextFit);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_ICONTEXTFITPADDING], layout::setIconTextFitPadding);

                        isNotNull |= setString  (layerStrings[COLUMN_TEXTFIELD]         , layout::setTextField);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTFONT]          , layout::setTextFont);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTSIZE]          , layout::setTextSize);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTOFFSET]        , layout::setTextOffset);
                        isNotNull |= setString  (layerStrings[COLUMN_TEXTANCHOR]        , layout::setTextAnchor);
                        isNotNull |= setFloat   (layerStrings[COLUMN_TEXTMAXWIDTH]      , layout::setTextMaxWidth);
                        isNotNull |= setString  (layerStrings[COLUMN_TEXTTRANSFORM]     , layout::setTextTransform);
                        isNotNull |= setBoolean (layerStrings[COLUMN_TEXTALLOWOVERLAP]  , layout::setTextAllowOverlap);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTLINEHEIGHT]    , layout::setTextLineHeight);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTLETTERSPACING] , layout::setTextLetterSpacing);

                        if (isNotNull)
                        {
                            layer.setLayout(layout);
                        }

                        paint=new Paint();
                        isNotNull=false;
                        isNotNull |= setString  (layerStrings[COLUMN_FILLPATTERN]       , paint::setFillPattern);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_FILLCOLOR]         , paint::setFillColor);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_FILLOUTLINECOLOR]  , paint::setFillOutlineColor);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_FILLOPACITY]       , paint::setFillOpacity);

                        isNotNull |= setJsonNode(layerStrings[COLUMN_LINECOLOR]         , paint::setLineColor);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_LINEWIDTH]         , paint::setLineWidth);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_LINEGAPWIDTH]      , paint::setLineGapWidth);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_LINEOPACITY]       , paint::setLineOpacity);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_LINEDASHARRAY]     , paint::setLineDasharray);

                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTCOLOR]         , paint::setTextColor);
                        isNotNull |= setJsonNode(layerStrings[COLUMN_TEXTHALOCOLOR]     , paint::setTextHaloColor);
                        isNotNull |= setFloat   (layerStrings[COLUMN_TEXTHALOWIDTH]     , paint::setTextHaloWidth);
                        isNotNull |= setFloat   (layerStrings[COLUMN_TEXTHALOBLUR]      , paint::setTextHaloBlur);

                        isNotNull |= setJsonNode(layerStrings[COLUMN_BACKGROUNDCOLOR]   , paint::setBackgroundColor);

                        if (isNotNull)
                        {
                            layer.setPaint(paint);
                        }
                        layers.add(layer);
                    }
                }
                i++;
            }
            LOGGER.info("Lines read: {}, layers extracted: {}", i, layers.size());
        }
        catch (Exception e)
        {
            LOGGER.error("An error occurred while importing CSV: {}", e.getMessage());
        }
    }
    
    
    
    /**
     * Helper, gets cell value as string
     * @param cell The cell to process
     * @return The value as string or null if an error occurred
     */
    private String getCellStringValue(Cell cell)
    {
        String returnValue="";
        
        if (cell!=null && cell.getCellType()==CellType.STRING && cell.getStringCellValue().length()>0)
        {
            returnValue=cell.getStringCellValue();
            if (returnValue==null)
            {
                LOGGER.error("Null string");
            }
            returnValue=returnValue.trim();
            if (returnValue==null)
            {
                LOGGER.error("Null string after trim");
            }
        }       
        else if (cell!=null && cell.getCellType()==CellType.NUMERIC)
        {
            returnValue=Double.toString(cell.getNumericCellValue());
        }
        else
        {
            if (cell!=null)
            {
                LOGGER.debug("Cell {} is not a string or is empty", cell.getColumnIndex());
            }
            else
            {
                LOGGER.debug("Cell is null");
            }
        }
        return returnValue;
    }
    
    /**
     * Helper, gets cell value as int value
     * @param cell The cell to process
     * @return The value as int or null if an error occurred
     */
    private Long    getCellIntValue(Cell cell)
    {
        Long    returnValue=null;
        
        if (cell!=null && cell.getCellType()==CellType.NUMERIC)
        {
            returnValue=Math.round(cell.getNumericCellValue());
        }        
        else
        {
            if (cell!=null)
            {
                LOGGER.debug("Cell {} is not a numerical", cell.getColumnIndex());
            }
            else
            {
                LOGGER.debug("Cell is null");
            }
        }
        return returnValue;
    }

    /**
     * Helper, gets cell value as int value
     * @param cell The cell to process
     * @return The value as int or null if an error occurred
     */
    private Boolean    getCellBooleanValue(Cell cell)
    {
        Boolean    returnValue=null;
        
        if (cell!=null && cell.getCellType()==CellType.BOOLEAN)
        {
            returnValue=cell.getBooleanCellValue();
        }        
        else
        {
            if (cell!=null)
            {
                LOGGER.debug("Cell {} is not a boolean", cell.getColumnIndex());
            }
            else
            {
                LOGGER.debug("Cell is null");
            }
        }
        return returnValue;
    }
    
    /**
     * Helper, gets cell value as float value
     * @param cell The cell to process
     * @return The value as float or null if an error occurred
     */
    private Float    getCellFloatValue(Cell cell)
    {
        Float    returnValue=null;
        
        if (cell!=null && cell.getCellType()==CellType.NUMERIC)
        {
            returnValue=(float)cell.getNumericCellValue();
        }        
        else if (cell!=null && cell.getCellType()==CellType.STRING && cell.getStringCellValue().length()>0)
        {
            returnValue=Float.parseFloat(cell.getStringCellValue());
        }
        else
        {
            if (cell!=null)
            {
                LOGGER.debug("Cell {} is not a numerical or something like it", cell.getColumnIndex());
            }
            else
            {
                LOGGER.debug("Cell is null");
            }
        }
        return returnValue;
    }    
    
    /**
     * Process one valid row of the layers excel file
     * @param row Row to process
     */
    private void processRow(Row row)
    {
        String id=getCellStringValue(row.getCell(COLUMN_ID));
        if (id.startsWith("#"))
        {
            // TO DO: Do something sensible with comment
            LOGGER.info(id);
        }
        else if (!id.equals(""))
        {
            Layer layer=new Layer();
            layer.setId(id);
            layer.setType(getCellStringValue(row.getCell(COLUMN_TYPE)));
            setString  (getCellStringValue(row.getCell(COLUMN_SOURCE))      , layer::setSource);
            setString  (getCellStringValue(row.getCell(COLUMN_SOURCELAYER)) , layer::setSourceLayer);
            setJsonNode(getCellStringValue(row.getCell(COLUMN_FILTER))      , layer::setFilter);
            layer.setMinzoom(getCellFloatValue(row.getCell(COLUMN_MINZOOM)));
            layer.setMaxzoom(getCellFloatValue(row.getCell(COLUMN_MAXZOOM)));

            Layout layout=new Layout();
            boolean isNotNull=false;
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_SYMBOLPLACEMENT))    , layout::setSymbolPlacement);
            isNotNull |= setBoolean (getCellBooleanValue(row.getCell(COLUMN_SYMBOLAVOIDEDGES))  , layout::setSymbolAvoidEdges);
            isNotNull |= setFloat   (getCellFloatValue(row.getCell(COLUMN_SYMBOLSPACING))       , layout::setSymbolSpacing);

            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_LINECAP))            , layout::setLineCap);
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_LINEJOIN))           , layout::setLineJoin);

            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_ICONIMAGE))          , layout::setIconImage);
            isNotNull |= setBoolean (getCellBooleanValue(row.getCell(COLUMN_ICONALLOWOVERLAP))  , layout::setIconAllowOverlap);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_ICONOFFSET))         , layout::setIconOffset);
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_ICONTEXTFIT))        , layout::setIconTextFit);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_ICONTEXTFITPADDING)) , layout::setIconTextFitPadding);

            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_TEXTFIELD))          , layout::setTextField);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTFONT))           , layout::setTextFont);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTSIZE))           , layout::setTextSize);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTOFFSET))         , layout::setTextOffset);
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_TEXTANCHOR))         , layout::setTextAnchor);
            isNotNull |= setFloat   (getCellFloatValue (row.getCell(COLUMN_TEXTMAXWIDTH))       , layout::setTextMaxWidth);
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_TEXTTRANSFORM))      , layout::setTextTransform);
            isNotNull |= setBoolean (getCellBooleanValue(row.getCell(COLUMN_TEXTALLOWOVERLAP))  , layout::setTextAllowOverlap);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTLINEHEIGHT))     , layout::setTextLineHeight);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTLETTERSPACING))  , layout::setTextLetterSpacing);

            if (isNotNull)
            {
                layer.setLayout(layout);
            }
            Paint paint=new Paint();
            isNotNull=false;
            isNotNull |= setString  (getCellStringValue(row.getCell(COLUMN_FILLPATTERN))        , paint::setFillPattern);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_FILLCOLOR))          , paint::setFillColor);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_FILLOUTLINECOLOR))   , paint::setFillOutlineColor);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_FILLOPACITY))        , paint::setFillOpacity);

            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_LINECOLOR))          , paint::setLineColor);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_LINEWIDTH))          , paint::setLineWidth);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_LINEGAPWIDTH))       , paint::setLineGapWidth);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_LINEOPACITY))        , paint::setLineOpacity);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_LINEDASHARRAY))      , paint::setLineDasharray);

            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTCOLOR))          , paint::setTextColor);
            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_TEXTHALOCOLOR))      , paint::setTextHaloColor);
            isNotNull |= setFloat   (getCellFloatValue(row.getCell(COLUMN_TEXTHALOWIDTH))       , paint::setTextHaloWidth);
            isNotNull |= setFloat   (getCellFloatValue(row.getCell(COLUMN_TEXTHALOBLUR))        , paint::setTextHaloBlur);

            isNotNull |= setJsonNode(getCellStringValue(row.getCell(COLUMN_BACKGROUNDCOLOR))    , paint::setBackgroundColor);

            if (isNotNull)
            {
                layer.setPaint(paint);
            }

            layers.add(layer);
        }
    }
    
    /**
     * Read the layers excel file
     * @param fileName Filename of the excel
     */
    public void readExcel(String fileName)
    {
        layers=new ArrayList<>();
        try
        {
            InputStream file = new FileInputStream(fileName);
            if (file!=null)
            {
                Workbook workbook   = new XSSFWorkbook(file);
                
                Sheet sheet         =workbook.getSheetAt(0);
                int maxRow          =sheet.getLastRowNum();
                int errorCount      =0;
                for (int i=1; i<=maxRow; i++) 
                {
                    Row row=sheet.getRow(i);
                    if (row!=null)
                    {
                        processRow(row);
                    }
                    else
                    {
                        LOGGER.debug("Empty row: {}",i);
                    }
                }
                LOGGER.info("Lines read: {}, layers extracted: {}", maxRow, layers.size());
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("File {} not found: {}", fileName, e.getMessage());
        }
        catch (IOException e)
        {
            LOGGER.error("Error reading file {}: {}", fileName, e.getMessage());
        }
        
    }
}
