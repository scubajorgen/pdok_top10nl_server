echo off
REM ###################################################################################################################################
REM #
REM # Merge a selection of layers from the TOP10NL geopackages into one geopackage using the GDAL tools
REM #
REM ###################################################################################################################################

call 01_merge_gpkg0010.bat
call 01_merge_gpkg0050.bat
call 01_merge_gpkg0100.bat
call 01_merge_gpkg0250.bat
call 01_merge_gpkg0500.bat
call 01_merge_gpkg1000.bat

echo DONE







