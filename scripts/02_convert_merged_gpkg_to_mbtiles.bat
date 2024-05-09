@echo off
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

call:convert ..\maps\mbtiles\top1000nl.mbtiles              1  7 ../maps/merged_gpkg/merge1000.gpkg             ..\logs\log1000.txt
call:convert ..\maps\mbtiles\top0500nl.mbtiles              8  8 ../maps/merged_gpkg/merge0500.gpkg             ..\logs\log0500.txt
call:convert ..\maps\mbtiles\top0250nl.mbtiles              9  9 ../maps/merged_gpkg/merge0250.gpkg             ..\logs\log0250.txt
call:convert ..\maps\mbtiles\top0100nl.mbtiles              10 10 ../maps/merged_gpkg/merge0100.gpkg            ..\logs\log0100.txt
call:convert ..\maps\mbtiles\top0050nl.mbtiles              11 12 ../maps/merged_gpkg/merge0050.gpkg            ..\logs\log0050.txt
call:convert ..\maps\mbtiles\top0010nl.mbtiles              13 15 ../maps/merged_gpkg/merge0010.gpkg            ..\logs\log0010.txt
call:convert ..\maps\mbtiles\top0250nl_plaats.mbtiles       10 12 ../maps/merged_gpkg/merge0250_plaats.gpkg     ..\logs\log0250_plaats.txt
call:convert ..\maps\mbtiles\top0250nl_geoggebied.mbtiles   10 12 ../maps/merged_gpkg/merge0250_geoggebied.gpkg ..\logs\log0250_geoggebied.txt

echo end
date /t
time /t
echo.&pause&goto:eof

:convert
echo Converting %~4 into %~1, zoom levels %~2 to %~3
IF EXIST %~1 DEL /F %~1
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  %~1 -dsco FORMAT=MBTILES  -dsco MINZOOM=%~2 -dsco MAXZOOM=%~3 -dsco MAX_SIZE=2000000 -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL %~4 > %~5 2>&1
goto:eof
