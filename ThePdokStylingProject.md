# The PDOK BRT TopNL Styling Project
## Introduction
One of the challenges of serving PDOK BRT vector maps is to create a suitable styling. For this, we wanted to create a style that matches the PDOK BRT TopRaster maps as close as possible.

![](images/Legenda25d.png)

Most of the items could be implemented using MapBox styling, however a few items had to be modified.

An advantage is that the vector maps contain more information than is shown in the raster maps, so we can add these as new features like bunkers and viewpoints.

An advantage of vector maps of course is that we can zoom in without the map becoming pixelated.

## Workflow
To facilitate work, following workflow was adopted:
![](images/workflow.png)

Basically we worked from the Excel file ```layers.xslx```. Each line represents a layer. Using StyleConvert.java the layers were inserted into a template, resulting in the style file ```style_topnl.json```.

The Excel file uses a number of rendering parameters (a subset, not all) which we can use to control the rendering. Each line corresponds with one layer. So it is easy to copy layers and adapt them.

The ```Check``` column defines a coordinate at which an example of the layer is shown. It can be used for testing.

## Layers
The style file is a JSON file that contains information about how a vector map is rendered. Refer to the [Mapbox Style Specification](https://docs.mapbox.com/style-spec/guides/) for details. Next shows our template file containing just one layer that draws the background color.

```
{
  "version": 8,
  "name": "PDOK BRT TopNL",
  "glyphs": "{fontstack}/{range}.pbf",
  "sources": 
  {
    "brt_topnl": 
    {
      "type": "vector",
      "url": "mbtiles://{PDOK BRT TopNL}"
    }
  },
  "sprite": "{styleJsonFolder}/sprites/sprites",
  "layers": 
  [
    {
      "id": "background",
      "type": "background",
      "paint": 
      {
        "background-color": "#000000"
      }
    }
  ]
}
```

The ```sources``` section defines the map data, ```glyphs``` the font files, ```sprite``` the graphical symbol location and layer the rules or ```layers``` defining the rendering of features.

The layer definition is what defines the rendering of the map. An important rule is that layers are rendered in the order in which they are defined. Hence the order is the 'z order'. A layer later in the file will overlap a layer earlier in the file.

As described in the [readme](readme.md), we use various PDOK BRT TopNL maps at various zoomlevels. This means, for each map/zoomlevel selection we have to define a layer that controls the rendering of a particular feature. Subsequent maps use slightly different attribute names. For example the top10nl map uses no underscores in the the attribute names like ```typeweg``` whereas the other maps use underscores, like ```type_weg```. Attribute values can also differ. For example in top10nl we have for forrests ```typelandgebruik``` 'bos: loofbos', 'bos: naaldbos', 'bos: gemengd bos' and 'bos: griend', whereas in top250nl we only have ```type_landgebruik``` 'bos'.


## Examples
![](images/example01.jpg)

![](images/example02.jpg)

![](images/example03.jpg)

![](images/example04.jpg)

## Known issues
* We don't have roads with separate lanes on higher scales ('gescheiden rijbanen'). Zooming in to scale 1:10.000 shows lanes seperately.
* Culverts ('duikers') cannot be rendered, since MapBox does not allow symbol placement at the end of a line. Rendered as red dashed line.
* Typelandgebruik 'Overig' is white with red dots
* Typelandgebruik 'Spoorbaanlichaam' is grey i.s.o. white
