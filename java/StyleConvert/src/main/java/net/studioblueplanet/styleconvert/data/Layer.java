/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.styleconvert.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonProperty("id")
    private String      id;
    @JsonProperty("type")
    private String      type;
    @JsonProperty("source")
    private String      source;
    @JsonProperty("source-layer")
    private String      sourceLayer;
    @JsonProperty("minzoom")
    private Float       minzoom;
    @JsonProperty("maxzoom")
    private Float       maxzoom;
    @JsonProperty("filter")
    private JsonNode    filter;
    @JsonProperty("layout")
    private Layout      layout;
    @JsonProperty("paint")
    private Paint       paint;

    @JsonIgnore
    private String      comment;
    
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

    public Float getMinzoom() {
        return minzoom;
    }

    public void setMinzoom(Float minzoom) {
        this.minzoom = minzoom;
    }

    public Float getMaxzoom() {
        return maxzoom;
    }

    public void setMaxzoom(Float maxzoom) {
        this.maxzoom = maxzoom;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
