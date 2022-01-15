echo off
REM ###################################################################################################################################
REM #
REM # Merge a selection of layers from the top250NL geopackages into one geopackage using the GDAL tools
REM #
REM ###################################################################################################################################

set GDAL_DATA=c:\program files\GDAL\gdal-data
set GDAL_DRIVER_PATH=C:\Program Files\GDAL\gdalplugins
set PROJ_LIB=C:\Program Files\GDAL\projlib
set PYTHONPATH=C:\Program Files\GDAL\


SET PATH=c:\program files\gdal\;%PATH%

gdalinfo --version

SET GPKGFILE="..\maps\merged_gpkg\merge0250.gpkg"
IF EXIST %GPKGFILE% DEL /F %GPKGFILE%

echo #####################################################
echo # top250NL
echo #####################################################
echo start %date% %time%


echo 1 - FunctioneelGebied
ogr2ogr         -f GPKG -select functioneel_gebied,soortnaam,nederlandse_naam ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_functioneel_gebied_punt
echo 2 - Gebouw
ogr2ogr -update -f GPKG -select naam,type_gebouw,fysiek_voorkomen,hoogteklasse,hoogte,status,soortnaam ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_gebouw_punt
echo 3 - GeografischGebied
ogr2ogr -update -f GPKG -select type_geoggebied,nederlandse_naam ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_geografisch_gebied_vlak
echo 4 - Hoogte
ogr2ogr -update -f GPKG -select type_hoogte,referentie_vlak ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_hoogte_lijn
echo 5 - InrichtingsElement
ogr2ogr -update -f GPKG -select naam,type_element,soortnaam ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_inrichtingselement_punt
ogr2ogr -update -f GPKG -select naam,type_element,soortnaam ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_inrichtingselement_lijn
echo 6 - Plaats
ogr2ogr -update -f GPKG -select nederlandse_naam,naam_officieel,type_gebied ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_plaats_punt
ogr2ogr -update -f GPKG -select nederlandse_naam,naam_officieel,type_gebied ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_plaats_vlak
echo 7 - Plantopografie
echo 8 - RegistratiefGebied
ogr2ogr -update -f GPKG -select nederlandse_naam,naam_officieel,nummer,type_regigebied ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_registratief_gebied_vlak
echo 9 - Relief
ogr2ogr -update -f GPKG -select type_relief,hoogteklasse ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_relief_lijn
echo 10 - Spoorbaandeel
ogr2ogr -update -f GPKG -select elektrificatie,fysiekvoorkomen,spoorbreedte,status,type_infrastructuur,type_spoorbaan,vervoerfunctie,brugnaam,tunnelnaam,baanvaknaam,aantal_sporen ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_spoorbaandeel_lijn
echo 11 - Terrein
ogr2ogr -update -f GPKG -select naam,type_landgebruik,voorkomen ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_terrein_vlak
echo 12 - Waterdeel
ogr2ogr -update -f GPKG -select type_water,breedteklasse,fysiek_voorkomen ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_waterdeel_lijn
ogr2ogr -update -f GPKG -select type_water,breedteklasse,fysiek_voorkomen ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_waterdeel_vlak
echo 13 - Wegdeel
ogr2ogr -update -f GPKG -select afritnummer,afritnaam,awegnummer,ewegnummer,fysiek_voorkomen,gescheidenrijbaan,hoofdverkeersgebruik,knooppuntnaam,nwegnummer,status,naam,swegnummer,type_infrastructuur,type_weg,verhardings_type,bag_naam_ind,tunnelnaam,brugnaam,verhardingsbreedteklasse ..\maps\merged_gpkg\merge0250.gpkg ..\maps\gpkg\top250nl\top250nl_Compleet.gpkg top250nl_wegdeel_lijn

echo done %date% %time%