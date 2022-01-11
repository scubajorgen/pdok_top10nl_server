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
public class Layout
{
    @JsonProperty("line-cap")
    private String      lineCap;
    @JsonProperty("line-join")
    private String      lineJoin;
    @JsonProperty("symbol-placement")
    private String      symbolPlacement;
    @JsonProperty("symbol-avoid-edges")
    private Boolean     symbolAvoidEdges;
    @JsonProperty("symbol-spacing")
    private Float       symbolSpacing;
    @JsonProperty("icon-image")
    private String      iconImage;
    @JsonProperty("icon-allow-overlap")
    private Boolean     iconAllowOverlap;
    @JsonProperty("text-field")
    private String      textField;
    @JsonProperty("text-font")
    private JsonNode    textFont;

    public String getLineCap() {
        return lineCap;
    }

    public void setLineCap(String lineCap) {
        this.lineCap = lineCap;
    }

    public String getLineJoin() {
        return lineJoin;
    }

    public void setLineJoin(String lineJoin) {
        this.lineJoin = lineJoin;
    }

    public String getSymbolPlacement() {
        return symbolPlacement;
    }

    public void setSymbolPlacement(String symbolPlacement) {
        this.symbolPlacement = symbolPlacement;
    }

    public Boolean getSymbolAvoidEdges() {
        return symbolAvoidEdges;
    }

    public void setSymbolAvoidEdges(Boolean symbolAvoidEdges) {
        this.symbolAvoidEdges = symbolAvoidEdges;
    }

    public Float getSymbolSpacing() {
        return symbolSpacing;
    }

    public void setSymbolSpacing(Float symbolSpacing) {
        this.symbolSpacing = symbolSpacing;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public Boolean getIconAllowOverlap() {
        return iconAllowOverlap;
    }

    public void setIconAllowOverlap(Boolean iconAllowOverlap) {
        this.iconAllowOverlap = iconAllowOverlap;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public JsonNode getTextFont() {
        return textFont;
    }

    public void setTextFont(JsonNode textFont) {
        this.textFont = textFont;
    }

}
