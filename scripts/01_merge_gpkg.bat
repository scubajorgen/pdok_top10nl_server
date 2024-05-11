@echo off
REM ###################################################################################################################################
REM #
REM # Merge a selection of layers from the TOP10NL geopackages into one geopackage using the GDAL tools
REM #
REM ###################################################################################################################################

REM Older releases have filename postfixes, like top10nl_Compleet-2021.gpkg. Pass -2021 as postfix or leave empty for current release
SET POSTFIX=

call 01_merge_gpkg0010.bat %POSTFIX%
call 01_merge_gpkg0050.bat %POSTFIX%
call 01_merge_gpkg0100.bat %POSTFIX%
call 01_merge_gpkg0250.bat %POSTFIX%
call 01_merge_gpkg0500.bat %POSTFIX%
call 01_merge_gpkg1000.bat %POSTFIX%

echo DONE







