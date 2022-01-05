# PDOK TOP10 Tileserver Project
## Introduction
This project prepares the [PDOK TOP10NL topographic maps of the Netherlands](pdok.nl/downloads/-/article/basisregistratie-topografie-brt-topnl) for serving with the the [Tileserver GL](https://tileserver.org/) mapserver. Tileserver GL is an [open-source mapserver project on github](https://github.com/maptiler/tileserver-gl) and can be run as [docker container](https://hub.docker.com/r/maptiler/tileserver-gl). 

Refer to [my blog page](https://blog.studioblueplanet.net/?p=781) on this subject for more and detailed information.

This project offers
* scrips for converting the PDOK TOP10NL Geopackage into mbtiles
* scripts for getting the Tileserver GL up and running as docker container
* the configuration files for Tileserver GL
* style files for rendering the TOP10NL content as close as possible to the rasterized TOP25NL maps

The scripts use the [GDAL tools](https://gdal.org/).

This project does not offer
* the PDOK files; these must be downloaded from the [PDOK site](https://www.pdok.nl/).
* the converted mbtiles file itself
* the Tileserver GL software
* The GDAL tools. These can be downloaded from [GisInternals](https://www.gisinternals.com/query.html?content=filelist&file=release-1911-x64-gdal-mapserver.zip). We use [gdal-302-1911-x64-core.msi](https://download.gisinternals.com/sdk/downloads/release-1911-x64-gdal-mapserver/gdal-302-1911-x64-core.msi). If you use other releases you might need to change paths in the scripts. Alternatively [gdal-303-1916-core.msi](https://download.gisinternals.com/sdk/downloads/release-1916-gdal-3-3-3-mapserver-7-6-4/gdal-303-1916-core.msi) can be used.

![](images/vectors2.png)
_Resulting map rendering_

The project assumes preparing the maps (mbtile files) on a Windows machine, while running Tileserver GL on a Linux machine.

## Prerequisite
The processing is very data and disk intensive. Therefore performance is greatly enhanced when using a Solid State Disk (SSD) instead of a Hard Drive (HDD). The data size is tens of GByte, so a 250 GByte SDD suffices. 

## Converting PDOK maps
1. Download the geopackage PDOK TOP10 maps (top10nl.zip) on the [PDOK site](https://service.pdok.nl/brt/top10nl/atom/v1_0/index.xml) put them in the _/downloads_ directory
1. Extract the gpkg files to _/maps/gpkg_. Note that this requires about 10 GByte of space
1. Start a DOS cmd window
1. Enter the _/scripts_ directory
1. Run the script **01_merge_gpkg.bat**. This script merges a number of layers from the gpkg files into one file _/maps/merged_gpkg/merge.gpkg_ file. The operation takes about 10 minutes on an I7 processor with SSD
1. Run the script **02_convert_gpkg_to_mbtiles.bat**. This script convers the merged gpkg file into a mbtiles file _/maps/mbtiles/top10nl.mbtiles_. This operation takes hours.

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