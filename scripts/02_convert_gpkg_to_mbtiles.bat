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

REM DEL ..\maps\mbtiles\top10nl.mbtiles

REM "c:\program files\gdal\ogr2ogr.exe"  -fieldTypeToString Date -gt 65536 -f MVT  "f:/maps/top10nl/nederland2020-10nl-limited.mbtiles" -dsco MAXZOOM=10 -dsco MINZOOM=0 -dsco FORMAT=MBTILES -dsco MAX_SIZE=1500000 -dsco NAME=test "nederland2020-10nl-limited.gpkg"

REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  "f:/maps/top10nl/nederland2020-10nl-limited2.mbtiles" -dsco FORMAT=MBTILES -dsco MAXZOOM=10 -dsco MINZOOM=0 -dsco MAX_SIZE=5000000 -dsco MAX_FEATURES=2000000 -dsco NAME=Nederland2020 "nederland2020-10nl-limited.gpkg" > j:\temp\gdal.txt 2>&1


REM zoomlevel 0-12, limited layer set (7 iso 13 layers)
REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  f:/maps/top10nl/nederland2020-10nl-limited3.mbtiles -dsco FORMAT=MBTILES -dsco MAXZOOM=12 -dsco MINZOOM=0 -dsco MAX_SIZE=50000000 -dsco MAX_FEATURES=20000000 -dsco NAME=Nederland2020 nederland2020-10nl-limited.gpkg > debuglog.txt 2>&1


REM zoomlevel 6-14, all 13 layers
REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  g:/maps/top10nl/tiles/nederland2020-10nl5.mbtiles -dsco FORMAT=MBTILES -dsco MAXZOOM=14 -dsco MINZOOM=6 -dsco MAX_SIZE=50000000 -dsco MAX_FEATURES=20000000 -dsco NAME=Nederland2020 nederland2020-10nl.gpkg > debuglog.txt 2>&1

REM zoomlevel 6-15, all 13 layers
REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  g:/maps/top10nl/tiles/nederland2020-10nl5.mbtiles -dsco FORMAT=MBTILES -dsco MAXZOOM=15 -dsco MINZOOM=6 -dsco MAX_SIZE=100000000 -dsco MAX_FEATURES=20000000 -dsco NAME=Nederland2020 nederland2020-10nl.gpkg > j:\temp\debuglog5.txt 2>&1

REM zoomlevel 6-15, all 13 layers but reduced attributes
REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  g:/maps/top10nl/tiles/nederland2020-10nl6.mbtiles -dsco FORMAT=MBTILES -dsco MAXZOOM=15 -dsco MINZOOM=6 -dsco MAX_SIZE=100000000 -dsco MAX_FEATURES=20000000 -dsco NAME=Nederland2020 nederland2020-10nl-select.gpkg > j:\temp\debuglog6.txt 2>&1

echo start
date /t
time /t

REM zoomlevel 0-15, all 13 layers
REM ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ../maps/mbtiles/top10nl.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=13 -dsco MAXZOOM=15 -dsco MAX_SIZE=100000000 -dsco MAX_FEATURES=20000000 -dsco NAME=Nederland2020 ../maps/merged_gpkg/merge10.gpkg > ..\logs\log.txt 2>&1

echo Processing top100nl
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ../maps/mbtiles/top0100nl.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=1  -dsco MAXZOOM=10 -dsco MAX_SIZE=7000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0100.gpkg > ..\logs\log0100.txt 2>&1
echo Processing top50nl
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ../maps/mbtiles/top0050nl.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=11 -dsco MAXZOOM=12 -dsco MAX_SIZE=2000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0050.gpkg > ..\logs\log0050.txt 2>&1
echo Processing top10nl
rem ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ../maps/mbtiles/top0010nl.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=13 -dsco MAXZOOM=15 -dsco MAX_SIZE=1000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ../maps/merged_gpkg/merge0010.gpkg > ..\logs\log0010.txt 2>&1

echo end
date /t
time /t
