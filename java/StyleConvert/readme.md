# StyleConvert conversion program

## Introduction
One of the main challenges in this project is to create a JSON style file for the rendering the maps. In order to simplifate this, this tool can

* Extract the layers out the style file and write it as CSV, so the layers can be edited in Excel. Each line comprises one layer, which makes it easier to edit.
* Insert the edited layers in CSV back into the JSON style file

The tool is **quick and dirty**. That means, it is not complete. It only covers part of the [MapBox style specification](https://docs.mapbox.com/mapbox-gl-js/style-spec/): only the parts I have used.
Do you want better compliancy? Please edit the code and recompile :-)

## Usage
Compile the code. Then execute

```
java -jar StyleConvert.jar EXTRACTLAYERS [StyleJsonInFile] [LayerCsvOutFile]
```
Extracts the layers out the [StyleJsonInFile] and writes them to [LayerCsvOutFile]
```
java -jar StyleConvert.jar INSERTLAYERS [StyleJsonInFile] [LayerCsvInFile] [StyleJsonOutFile]
```
Inserts the layers from [LayerCsvInFile] into [StyleJsonInFile] and writes it to a new style file [StyleJsonOutFile].

Of course the [...] are the names of the files. Real commands are for example:

```
java -jar StyleConvert-1.0-SNAPSHOT.jar EXTRACTLAYERS style_topnl.json layers.csv
```

```
 java -jar StyleConvert-1.0-SNAPSHOT.jar INSTERTLAYERS style_topnl.json layers.csv style_topnl_edited.json
```

### Adapting the code
* The package *net.studioblueplanet.data* contains the layer data specification. You can add fields. Note that the JsonNode data type is used for pieces of JSON to be read and written to CSV. For example fill-outline-color can be *"rgb(0, 0, 0)"* but also *"["interpolate",["linear"],["zoom"],10,"#FFFFFF",11,"#CCCCAA"]"*
* The mapping to and from CSV takes place manually *net.studioblueplanet.styleconvert.layerprocessor*. Conversion to and from JSON is automatic via Jackson.


