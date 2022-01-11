/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author jorgen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Layer
{
    private String      id;
    private String      type;
    private String      source;
    @JsonProperty("source-layer")
    private String      sourceLayer;
    private JsonNode    filter;
    private Paint       paint;
    private Layout      layout;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public Layout getLayout()
    {
        return layout;
    }

    public void setLayout(Layout layout)
    {
        this.layout = layout;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSourceLayer()
    {
        return sourceLayer;
    }

    public void setSourceLayer(String sourceLayer)
    {
        this.sourceLayer = sourceLayer;
    }

    public JsonNode getFilter()
    {
        return filter;
    }

    public void setFilter(JsonNode filter)
    {
        this.filter = filter;
    }
}
