echo off
REM ###################################################################################################################################
REM #
REM # Info on geopackages using the GDAL tools: it shows the layers
REM #
REM ###################################################################################################################################

set GDAL_DATA=c:\program files\GDAL\gdal-data
set GDAL_DRIVER_PATH=C:\Program Files\GDAL\gdalplugins
set PROJ_LIB=C:\Program Files\GDAL\projlib
set PYTHONPATH=C:\Program Files\GDAL\


SET PATH=c:\program files\gdal\;%PATH%



SET dir=..\maps\gpkg\top50nl

for /r %%i in (%dir%\*) do (
    echo File: %%i
    REM ogrinfo -so %%i
    ogrinfo -al %%i | findstr /R /c:"\w*: [A-Z][a-z]* (" /c:"Layer name" 2> nul
    echo, 
)




