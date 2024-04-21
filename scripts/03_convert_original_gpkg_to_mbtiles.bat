@echo off
REM ###################################################################################################################################
REM #
REM # Convert the all original PDOK gpkg files into mbtiles format, hence including all information
REM # In top50nl and top100nl the city names are missing. Therefore add the top250nl city info to their zoom levels
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


call:convert ..\maps\gpkg\top1000nl_Compleet.gpkg ..\logs\ ..\maps\mbtiles\ 1 7
call:convert ..\maps\gpkg\top500nl_Compleet.gpkg  ..\logs\ ..\maps\mbtiles\ 8 8
call:convert ..\maps\gpkg\top250nl_Compleet.gpkg  ..\logs\ ..\maps\mbtiles\ 9 9
call:convert ..\maps\gpkg\top100nl_Compleet.gpkg  ..\logs\ ..\maps\mbtiles\ 10 10
call:convert ..\maps\gpkg\top50nl_Compleet.gpkg   ..\logs\ ..\maps\mbtiles\ 11 12
call:convert ..\maps\gpkg\top10nl_Compleet.gpkg   ..\logs\ ..\maps\mbtiles\ 13 15

REM Use top250nl information on plaats namen for top50nl and top100nl where this information is not present
echo Convert top250nl_Compleet.gpkg, layer top250nl_plaats_punt to top250nl_plaats_punt.mbtiles
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ..\maps\mbtiles\top250nl_plaats_punt.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=10 -dsco MAXZOOM=12 -dsco MAX_SIZE=1200000  -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ..\maps\gpkg\top250nl_Compleet.gpkg top250nl_plaats_punt > ..\logs\top250nl_plaats_punt.txt  2>&1
echo Convert top250nl_Compleet.gpkg, layer top250nl_plaats_vlak to top250nl_plaats_vlak.mbtiles
ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  ..\maps\mbtiles\top250nl_plaats_vlak.mbtiles -dsco FORMAT=MBTILES  -dsco MINZOOM=10 -dsco MAXZOOM=12 -dsco MAX_SIZE=1200000  -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL ..\maps\gpkg\top250nl_Compleet.gpkg top250nl_plaats_vlak > ..\logs\top250nl_plaats_punt.txt  2>&1

echo finished
date /t
time /t

echo.&pause&goto:eof

:convert
    echo File       : %~1

    SET "output=%~3%~n1.mbtiles"
    SET "log=%~2%~n1.txt"

    SETLOCAL EnableDelayedExpansion


    ECHO Convert to : !output!
    ECHO Log to     : !log!
    ECHO Zoom levels: %~4 to %~5
 
    IF EXIST !output! DEL /F !output!
    
    ogr2ogr.exe --debug ON -fieldTypeToString Date -gt 65536 -f MVT  !output! -dsco FORMAT=MBTILES  -dsco MINZOOM=%~4 -dsco MAXZOOM=%~5 -dsco MAX_SIZE=1700000  -dsco MAX_FEATURES=2000000 -dsco NAME=TopNL %~1 > !log! 2>&1

    ENDLOCAL
goto:eof


echo end
date /t
time /t