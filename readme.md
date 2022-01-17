# PDOK BRT TOPNL Tileserver Project
## Introduction
PDOK (Publieke Dienstverlening op Kaart) is the Kadaster organisation in the Netherlands that provides topographic maps. One of their products publicly available is the Basisregistratie Topografie (BRT) TOPNL. PDOK provides these maps on various scales. Maps are provided in two vector formats (GML and GPKG) without styling.
This project prepares the [PDOK TOPNL topographic maps of the Netherlands](https://www.pdok.nl/downloads/-/article/basisregistratie-topografie-brt-topnl) for serving and rendering with the the [Tileserver GL](https://tileserver.org/) mapserver. Tileserver GL is an [open-source mapserver project on github](https://github.com/maptiler/tileserver-gl) and can be run as [docker container](https://hub.docker.com/r/maptiler/tileserver-gl). It is meant to serve vector and raster based maps in the MBTILES format.

Refer to [my blog page](https://blog.studioblueplanet.net/?p=781) on this subject for more and detailed information.

This project offers
* scripts for converting the PDOK TOPNL Geopackages into mbtiles. It uses various scales (top10nl, top50nl and top100nl) vor the various zoomlevels
* scripts for getting the Tileserver GL up and running as docker container
* the configuration files for Tileserver GL
* style files for rendering the TOPNL content as close as possible to the rasterized [TOPraster maps](https://www.pdok.nl/introductie/-/article/dataset-basisregistratie-topografie-brt-topraster).
* A tool (in Java) that allows conversion of the JSON style file to CSV and back so that it can be edited in Excel

The scripts use the excellent [GDAL tools](https://gdal.org/) (Geospatial Data Abstraction Library). This site contains a manual for the used functions. For merging mbtiles file we use [Mapbox Tippercanoe](https://github.com/mapbox/tippecanoe). Both tools are used as command-line tools.

This project does not offer
* the PDOK files; these must be downloaded from the [PDOK site](https://www.pdok.nl/downloads/-/article/basisregistratie-topografie-brt-topnl). We use the geopackage ATOM files.
* the converted mbtiles file itself
* the Tileserver GL software. We use it from the docker repo.
* The GDAL tools. These can be downloaded from [GisInternals](https://www.gisinternals.com/query.html?content=filelist&file=release-1911-x64-gdal-mapserver.zip). We use [gdal-303-1916-core.msi](https://download.gisinternals.com/sdk/downloads/release-1916-gdal-3-3-3-mapserver-7-6-4/gdal-303-1916-core.msi)
* The [Mapbox Tippercanoe](https://github.com/mapbox/tippecanoe) tools. 

![](images/vectors2.png)
_Resulting map rendering_

Why this project? For my own projects I need maps reflecting the current situation as well as the situation a few years back, so I like to have a on-line collection of maps. 

## Prerequisite
### Windows and Linux
The project assumes preparing the maps (mbtile files) on a Windows machine, while running Tileserver GL on a Linux machine.
Preparation can be done on a Linux machine as well by the handy developer. GDAL is available in docker containers as well.

### GDAL
[GDAL](https://download.gisinternals.com/sdk/downloads/release-1916-gdal-3-3-3-mapserver-7-6-4/gdal-303-1916-core.msi) must be installed. Run the gdal-303-1916-core.msi. This installs gdal in _/program files/GDAL_ (assuming 64 bit version). If you encounter error messages that **ogr_MSSQLSpatial.dll** cannot be loaded, just remove or rename this file in _/program files/GDAL/gdalplugins/_. We don't need it anyway.

The handy develop can also use GDAL in a docker container on Linux. However, scripts have to be converted for this.

### Mapbox Tippercanoe
This tool is available on [github](https://github.com/mapbox/tippecanoe). Download and build under Linux. It requires the libsqlite3 libraries (libsqlite3-dev (Debian) or libsqlite3x-devel (Centos)). We only need _tile-join_ for merging the mbtiles files into one large mbtiles file.

### Disk
The processing is very data and disk intensive. Therefore 
 is greatly enhanced when using a Solid State Disk (SSD) instead of a Hard Drive (HDD). The data size is tens of GByte, so a 250 GByte SDD suffices. 

## Converting PDOK maps
### Scales and zoomlevels
MBTiles are generated for various zoomlevels (1-15). MBTiles always have the same size in pixels. However, the smaller the zoomlevel, the larger area is covered by one tile.

PDOK supplies the maps on various detail levels: top10nl (most detail), top50nl, top100nl, top250nl, top500nl, top1000nl (least detail). The numbers suggest regular map scales: top10nl = 1:10.000, ...

We map the various files on the zoomlevels to get tiles with roughly the same size in MBytes:

| map | Minzoom  | Maxzoom  |
|----|---|---|
| top1000nl |  1 |  4 |
| top500nl  |  5 |  6 |
| top250nl  |  7 |  8 |
| top100nl  |  9 | 10 |
| top50nl  |  11 | 12 |
| top10nl  |  13 | 15 |

Note
* MBTiles are generated at discrete zoomlevels. Tiles are rendered at continuous zoomlevels. Hence the zoomlevel 5 tiles are used for rendering levels 5.000-5.999.
* With decreasing detail level features are ommited of course. However, layer and feature names are not the same in each. This complicates the styling: For each map detail level separate styling rules have to be defined.
* On the first tries only the top10nl maps were used. This resulted in extremely large tiles sizes (in MByte) at low zoom levels and slow rendering speeds. A work around is to let GDAL shrink the tiles by ommitting features, however this resulted in badly rendered tiles (with holes and gaps) since features are ommitted randomly.
* It is easy to define your own mapping between detail level and zoom level by adjusting the MINZOOM and MAXZOOM parameters in _02_convert_gpkg_to_mtiles.bat_. The style file defines style layers for each detail level, so styling is not needed to adjust.

### The conversion

1. Download the geopackages ATOM PDOK TOP10NL, TOP50NL and TOP100NL maps (zip) on the [PDOK site](https://www.pdok.nl/downloads/-/article/basisregistratie-topografie-brt-topnl) and put them in the _/downloads_ directory
1. Extract the subsequent gpkg files to _/maps/gpkg/top10nl_, _/maps/gpkg/top50nl_ ... _/maps/gpkg/top1000nl_. Note that this requires about 14 GByte of space (Top10NL: 10 GByte, Top50NL: 3 GByte, Top100NL: 1 GByte)
1. Start a DOS cmd window
1. Enter the _/scripts_ directory
1. Run the script **01_merge_gpkg.bat**. This script merges a number of layers from the gpkg files into a file for each detail level in _/maps/merged_gpkg/_, for example _/maps/merged_gpkg/merge0010.gpkg_ for top10nl. The operation takes about 10 minutes on an I7 processor with SSD
1. Run the script **02_convert_gpkg_to_mbtiles.bat**. This script converts the merged gpkg files into a mbtiles file _/maps/mbtiles/_. For each scale an mbtiles file is generated: _top0010nl.mbtiles_ ... _top01000nl.mbtiles_. This operation takes about 13 hours on an I7 processor with SSD. Logging is written to _/logs/_ in separate log files (check the log files. No 'Recoding tile' should be present. Apparently this is done when the maximum tile size is exceeded and it results in ommiting features). 
1. Run the script **03_merge_mbtiles.sh**. This merges the mbtiles into one file: _/maps/mbtiles/topnl.mbtiles_ (2.2 GByte). Not only all tiles are copied, also the metadata is merged. This command must be run on a Linux machine and requires Tippecanoe.

## Style
The style can be found in _/tileserver/styles/pdok_. The main file is _style_top10nl.json_. The main component is the layers component, containing a layer for each feature (200-300) layers. In order to facilitate editing the layers can be exported to and from CSV using the StyleConvert tool. This enables editing in excel, where each layer is one row (allowing for copying, etc). See the [readme](java/StyleConvert/readme.md).

## Running Tileserver-GL
To run the tileserver proceed as follows, after generating the mbtiles map.
Note: run under Linux host with a Docker installation

1. Enter the _/tileserver_ directory 
2. Run the **start.sh** script
3. The tileserver will start, serving the _/maps/mbtiles/top10nl.mbtiles_ map. 

Expected output:
```
Starting tileserver-gl v3.1.1
Using specified config file from tileserver/config.json
Starting server
Listening at http://[::]:80/
Startup complete
mbgl: { class: 'Image',
  severity: 'WARNING',
  text: 'ImageReader (PNG): iCCP: known incorrect sRGB profile' }
...
```
Ignore the PNG warnings.

Use a browser to connect to port 8080 on the host. You are expected to see following:

![](images/output.png)
_The tileserver-gl landing page_

Use Styles 'Viewer' to watch the rendered vector image using the style enclosed

Use Data 'Inspect' to watch the raw vector data.

## Known issues
* None so far :-)

## Other links
* [Vector Tiles: BRT and BGT](https://github.com/PDOK/vectortiles-bgt-brt)
* [Serving your own PDOK maps](https://blog.studioblueplanet.net/?p=781)
* [TileServer GL](https://github.com/maptiler/tileserver-gl) on github
* [GDAL on docker](https://hub.docker.com/r/osgeo/gdal)
* [Map Style specification](https://docs.mapbox.com/mapbox-gl-js/style-spec/) 