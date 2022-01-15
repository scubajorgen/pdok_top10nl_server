echo off
REM ###################################################################################################################################
REM #
REM # Convert the merged geopackage into mbtiles
REM #
REM ###################################################################################################################################

set GDAL_DATA=c:\program files\GDAL\gdal-data
set GDAL_DRIVER_PATH=C:\Program Files\GDAL\gdalplugins
set PROJ_LIB=C:\Program Files\GDAL\projlib
set PYTHONPATH=C:\Program Files\GDAL\

SET PATH=c:\program files\gdal\;%PATH%

gdalinfo --version

echo start
date /t
time /t



echo Processing top1000nl
SET MBTILESFILE="..\maps\mbtiles\top1000nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=01 -dsco MAXZOOM=04 -dsco MAX_SIZE=400000  -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge1000.gpkg > ..\logs\log1000.txt 2>&1

echo Processing top500nl
SET MBTILESFILE="..\maps\mbtiles\top0500nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=05 -dsco MAXZOOM=06 -dsco MAX_SIZE=800000  -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0500.gpkg > ..\logs\log0500.txt 2>&1

echo Processing top250nl
SET MBTILESFILE="..\maps\mbtiles\top0250nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=07 -dsco MAXZOOM=08 -dsco MAX_SIZE=800000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0250.gpkg > ..\logs\log0250.txt 2>&1

echo Processing top100nl
SET MBTILESFILE="..\maps\mbtiles\top0100nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=09 -dsco MAXZOOM=10 -dsco MAX_SIZE=2000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0100.gpkg > ..\logs\log0100.txt 2>&1

echo Processing top50nl
SET MBTILESFILE="..\maps\mbtiles\top0050nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=11 -dsco MAXZOOM=12 -dsco MAX_SIZE=2000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0050.gpkg > ..\logs\log0050.txt 2>&1

echo Processing top10nl
SET MBTILESFILE="..\maps\mbtiles\top0010nl.mbtiles"
IF EXIST %MBTILESFILE% DEL /F %MBTILESFILE%
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %MBTILESFILE% -dsco FORMAT=MBTILES  -dsco MINZOOM=13 -dsco MAXZOOM=15 -dsco MAX_SIZE=1000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0010.gpkg > ..\logs\log0010.txt 2>&1

echo end
date /t
time /t
