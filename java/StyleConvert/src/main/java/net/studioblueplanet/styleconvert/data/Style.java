/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Iterator;
/**
 *
 * @author jorgen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Style
{
    private Integer     version;
    private String      name;
    private String      glyphs;
    private JsonNode    sources;
    private String      sprite;
    private Layer[]     layers;

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGlyphs()
    {
        return glyphs;
    }

    public void setGlyphs(String glyphs)
    {
        this.glyphs = glyphs;
    }

    public String getSprite()
    {
        return sprite;
    }

    public void setSprite(String sprite)
    {
        this.sprite = sprite;
    }

    public Layer[] getLayers()
    {
        return layers;
    }

    public void setLayers(Layer[] layers)
    {
        this.layers = layers;
    }
    
    public JsonNode getSources()
    {
        return sources;
    }

    public void setSources(JsonNode sources)
    {
        this.sources = sources;
    }
    
    
    /**
     * Return the JSON node as list of sources
     * @return The source list. 
     */
    @JsonIgnore
    public Source[] getSourceList()
    {
        Source[] sourceList;
        JsonNode source;
        int n;
        int i;
        
        
        n=sources.size();
        i=0;
        
        sourceList=new Source[n];
        Iterator<String> stringIt=sources.fieldNames();
        while (stringIt.hasNext())
        {
            String name=stringIt.next();
            JsonNode node=sources.get(name);
            String type=node.get("type").asText();
            String url =node.get("url").asText();

            sourceList[i]=new Source();
            sourceList[i].setName(name);
            sourceList[i].setType(type);
            sourceList[i].setUrl(url);
            i++;
        }
        
        return sourceList;
    }
    
}
