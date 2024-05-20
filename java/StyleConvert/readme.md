# StyleConvert conversion program

## Introduction
One of the main challenges in this project is to create a JSON style file for the rendering the maps. In order to simplifate this, this tool can

* Extract the layers out the style file and write it as CSV, so the layers can be edited in Excel. Each line comprises one layer, which makes it easier to edit.
* Insert the edited layers in _CSV or Excel_ back into the JSON style file

The tool is **quick and dirty**. That means, it is not complete. It only covers part of the [MapBox style specification](https://docs.mapbox.com/mapbox-gl-js/style-spec/): only the parts I have used.
Do you want better compliancy? Please edit the code and recompile :-)

## Usage
Compile the code. Then execute

```
java -jar StyleConvert.jar [userDefinedStyleConvert.properties]
```

or

```
java -jar StyleConvert.jar
```

Configuration is fully done from the properties file. If you don't pass a properties file, it looks for ```StyleConvert.properties``` in the resource directory.

The included properties file explains the configuration:

```
###############################################################################
# StyleConvert Properties File - configures the usage of the application
###############################################################################
###############################################################################
#
# mode=insertLayers
# Inserts the [inputLaysersExcel] into [templateJson] and writes result
# to [configJson]
# mode=extractLayers
# Extracts the layers from [configJson] and writes it to [outputLayersCsv]
#
#
mode=insertLayers

###############################################################################
#
# Indicates that custom filters ("_IN" and "!_IN") are processed in mode 
# 'insertLayers'.
# This requires the TopNL GPKG files to be present in the gpkgSourceDir
# If set to 'false' filters are not processed
enableFilterProcessing=true

###############################################################################
#
# Defines the release which is being processed [YYYY-MM] 
#
topnlRelease=2024-04


###############################################################################
#
# CSV seperator; should be ';'
#
csvSeparator=;

###############################################################################
#
# If the "_IN" filter conversion is used with 'insertLayers', we need the 
# geopackages of the TopNL release in this directory
#
gpkgSourceDir=gpkgSourceDir=../../downloads/2024-04

###############################################################################
#
# Template JSON. Should contain at least everything but the layers.
# Used for mode 'insertLayers'
#
templateJson=template.json

###############################################################################
#
# The MapBox GL config JSON.
# Used for mode 'insertLayers' and 'extractLayers'
#
configJson=style_topnl.json

###############################################################################
#
# The layers Excel file. Note that the extension must be .xlsx or .csv
# Used for mode 'insertLayers'
#
inputLayersExcel=layers.xlsx


###############################################################################
#
# The output layers CSV that are extracted from the [configJson]
# Used for mode 'extractLayers'
#
outputLayersCsv=layers.csv
```

Though all possible insert and extact combinations are supported, you usually use the method in the properties file above: Combining ```template.json``` with ```layers.xlsx``` into ```style_topnl.json```.

### Special feature: filter conversion
#### The issue
One of the main issues with the TopNL gpkg is that often for attributes which have multiple values, these values are concatenated in one attribute value. The values are separated by the pipe '|' symbol.

For example: if you look in the ```top10nl_gebouw_vlak``` table and you look for a building with ```typegebouw``` 'kerk', you find a whole lot of combinations in which 'kerk' occurs: 'bezoekerscentrum|kerk','brandweerkazerne|kerk','gemeentehuis|kerk','gemeentehuis|kerk|school','huizenblok|kerk', etc, etc. What's even worse: the possible combinations ('distinct values') differ from PDOK release to PDOK release, which makes the ```layers.xlsx``` layers file hard to maintain.

Often you filter on this kind of attributes. Unfortunatelly, MapBox GL Styling filtering does not support wildcards or LIKE '%kerk%'. Basically you have to list all combinations in which 'kerk' occurs.

#### Solution
To make things easier, we introduced in the Excel/CSV layers file a new type of filter:

```
["_IN", "typegebouw", "kerk", "moskee", "synagoge"]
```
When StyleConvert encounters such a filter, it transforms it in a regular "in" filter in which all attribute value combinations are filled in containing the words "kerk", "moskee" and "synagoge" (in this example).

```
["in", "typegebouw", "bezoekerscentrum|kerk","brandweerkazerne|kerk","gemeentehuis|kerk","gemeentehuis|kerk|school","huizenblok|kerk", ...]
```
It does this by getting all distinct values from the GPKG database and selecting these values that contain (at least one of) the indicated filter values. It implicates that you need the PDOK TopNL .gpkg files!

Note that these special filters may also be used in "all" or "any" filters. For example

```
["all", ["_IN", "typegebouw", "kerk"],["!IN", "typegebouw", "toren"]]
```
Which selects all churches without a tower.

Example output:
```
FILTER REPLACEMENT:
Layer ID    'gebouw_vlak_ruine'
Filter      '["_IN","typegebouw","ruïne"]'
Replaced by '["in","typegebouw","kasteel|ruïne","kasteel|ruïne|toren","ruïne","ruïne|toren"]'

```

#### Advantages:
* Excel ```layers.xlsx``` is better readable/understandable
* You can use the ```layers.xlsx``` sheet for subsequent releases of the TopNL, resulting in a ```style_pdok.json``` for that particular release

#### Prerequisite

> 📝You have to provide the [PDOK TopNL Geopackages](https://service.pdok.nl/brt/topnl/atom/index.xml) in the location stated in the properties file. Do you want to use StyleConvert without this filter extension or without the Geopackages, you can disable the feature in the properties file, property ```enableFilterProcessing=false```


## License
This software is published under the MIT license:

Copyright (c) 2024 Jörgen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

