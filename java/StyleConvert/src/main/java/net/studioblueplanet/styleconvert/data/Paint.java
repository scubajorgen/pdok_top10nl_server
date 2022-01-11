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
public class Paint
{
    @JsonProperty("background-color")
    private JsonNode backgroundColor;
    @JsonProperty("fill-pattern")
    private String fillPattern;
    @JsonProperty("fill-color")
    private JsonNode fillColor;
    @JsonProperty("fill-outline-color")
    private JsonNode fillOutlineColor;
    @JsonProperty("line-color")
    private JsonNode lineColor;
    @JsonProperty("line-width")
    private JsonNode lineWidth;
    @JsonProperty("line-opacity")
    private JsonNode lineOpacity;
    @JsonProperty("text-color")
    private JsonNode textColor;
    @JsonProperty("text-halo-color")
    private JsonNode textHaloColor;
    @JsonProperty("text-halo-width")
    private Float    textHaloWidth;
    @JsonProperty("text-halo-blur")
    private Float    textHaloBlur;
    

    public JsonNode getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(JsonNode backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public JsonNode getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(JsonNode fillColor)
    {
        this.fillColor = fillColor;
    }

    public JsonNode getFillOutlineColor()
    {
        return fillOutlineColor;
    }

    public void setFillOutlineColor(JsonNode fillOutlineColor)
    {
        this.fillOutlineColor = fillOutlineColor;
    }

    public JsonNode getLineColor()
    {
        return lineColor;
    }

    public void setLineColor(JsonNode lineColor)
    {
        this.lineColor = lineColor;
    }

    public JsonNode getLineWidth()
    {
        return lineWidth;
    }

    public void setLineWidth(JsonNode lineWidth)
    {
        this.lineWidth = lineWidth;
    }

    public JsonNode getLineOpacity()
    {
        return lineOpacity;
    }

    public void setLineOpacity(JsonNode lineOpacity)
    {
        this.lineOpacity = lineOpacity;
    }

    public JsonNode getTextColor()
    {
        return textColor;
    }

    public void setTextColor(JsonNode textColor)
    {
        this.textColor = textColor;
    }

    public JsonNode getTextHaloColor()
    {
        return textHaloColor;
    }

    public void setTextHaloColor(JsonNode textHaloColor)
    {
        this.textHaloColor = textHaloColor;
    }

    public Float getTextHaloWidth()
    {
        return textHaloWidth;
    }

    public void setTextHaloWidth(Float textHaloWidth)
    {
        this.textHaloWidth = textHaloWidth;
    }

    public Float getTextHaloBlur()
    {
        return textHaloBlur;
    }

    public void setTextHaloBlur(Float textHaloBlur)
    {
        this.textHaloBlur = textHaloBlur;
    }

    public String getFillPattern() {
        return fillPattern;
    }

    public void setFillPattern(String fillPattern) {
        this.fillPattern = fillPattern;
    }
}
