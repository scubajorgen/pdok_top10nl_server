@echo off
REM ###################################################################################################################################
REM #
REM # Merge a selection of layers from the TOP10NL geopackages into one geopackage using the GDAL tools
REM #
REM ###################################################################################################################################

set GDAL_DATA=c:\program files\GDAL\gdal-data
set GDAL_DRIVER_PATH=C:\Program Files\GDAL\gdalplugins
set PROJ_LIB=C:\Program Files\GDAL\projlib
set PYTHONPATH=C:\Program Files\GDAL\


SET PATH=c:\program files\gdal\;%PATH%

gdalinfo --version

SET GPKGFILE="..\maps\merged_gpkg\merge0010.gpkg"
IF EXIST %GPKGFILE% DEL /F %GPKGFILE%

SET POSTFIX=%1

echo #####################################################
echo # Top10NL%POSTFIX%
echo #####################################################
echo start %date% %time%

echo 1 - FunctioneelGebied
ogr2ogr         -f GPKG -select typefunctioneelgebied,soortnaam,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_functioneel_gebied_vlak
ogr2ogr -update -f GPKG -select typefunctioneelgebied,soortnaam,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_functioneel_gebied_multivlak
ogr2ogr -update -f GPKG -select typefunctioneelgebied,soortnaam,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_functioneel_gebied_punt
echo 2 - Gebouw
ogr2ogr -update -f GPKG -select typegebouw,fysiekvoorkomen,hoogteklasse,hoogteniveau,hoogte,status,soortnaam,naam,gebruiksdoel ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_gebouw_punt
ogr2ogr -update -f GPKG -select typegebouw,fysiekvoorkomen,hoogteklasse,hoogteniveau,hoogte,status,soortnaam,naam,gebruiksdoel ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_gebouw_vlak
echo 3 - GeografischGebied
ogr2ogr -update -f GPKG -select typegeografischgebied,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_geografisch_gebied_vlak
ogr2ogr -update -f GPKG -select typegeografischgebied,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_geografisch_gebied_multivlak
echo 4 - Hoogte
ogr2ogr -update -f GPKG -select typehoogte,hoogte ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_hoogte_punt
ogr2ogr -update -f GPKG -select typehoogte,hoogte ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_hoogte_lijn
echo 5 - InrichtingsElement
ogr2ogr -update -f GPKG -select typeinrichtingselement,hoogteniveau,hoogte,nummer,breedte,soortnaam,naam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_inrichtingselement_punt
ogr2ogr -update -f GPKG -select typeinrichtingselement,hoogteniveau,hoogte,nummer,breedte,soortnaam,naam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_inrichtingselement_lijn
echo 6 - Plaats
ogr2ogr -update -f GPKG -select typegebied,aantalinwoners,bebouwdekom,naamofficieel,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_plaats_punt
ogr2ogr -update -f GPKG -select typegebied,aantalinwoners,bebouwdekom,naamofficieel,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_plaats_vlak
ogr2ogr -update -f GPKG -select typegebied,aantalinwoners,bebouwdekom,naamofficieel,naamnl ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_plaats_multivlak
echo 7 - Plantopografie
ogr2ogr -update -f GPKG -select typeobject,naam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_plantopografie_vlak
echo 8 - Registratief gebied
ogr2ogr -update -f GPKG -select typeregistratiefgebied,naamofficieel,naamnl,nummer ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_registratief_gebied_vlak
ogr2ogr -update -f GPKG -select typeregistratiefgebied,naamofficieel,naamnl,nummer ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_registratief_gebied_multivlak
echo 9 - Relief
ogr2ogr -update -f GPKG -select typerelief,hoogteklasse,hoogteniveau,functie ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_relief_talud_hoge_zijde_lijn
ogr2ogr -update -f GPKG -select typerelief,hoogteklasse,hoogteniveau,functie ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_relief_lijn
echo 10 - Spoorbaandeel
ogr2ogr -update -f GPKG -select spoorbreedte,aantalsporen,vervoerfunctie,elektrificatie,brugnaam,tunnelnaam,baanvaknaam,typespoorbaan,hoofdspoor,hoogteniveau,typeinfrastructuur,fysiekvoorkomen,status ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_spoorbaandeel_punt
ogr2ogr -update -f GPKG -select spoorbreedte,aantalsporen,vervoerfunctie,elektrificatie,brugnaam,tunnelnaam,baanvaknaam,typespoorbaan,hoofdspoor,hoogteniveau,typeinfrastructuur,fysiekvoorkomen,status ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_spoorbaandeel_lijn
echo 11 - Terrein
ogr2ogr -update -f GPKG -select typelandgebruik,voorkomen,fysiekvoorkomen,naam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_terrein_vlak
echo 12 - Waterdeel
ogr2ogr -update -f GPKG -select typewater,breedteklasse,hoofdafwatering,fysiekvoorkomen,voorkomen,hoogteniveau,functie,getijdeinvloed,vaarwegklasse,naamofficieel,naamnl,sluisnaam,brugnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_waterdeel_punt
ogr2ogr -update -f GPKG -select typewater,breedteklasse,hoofdafwatering,fysiekvoorkomen,voorkomen,hoogteniveau,functie,getijdeinvloed,vaarwegklasse,naamofficieel,naamnl,sluisnaam,brugnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_waterdeel_lijn
ogr2ogr -update -f GPKG -select typewater,breedteklasse,hoofdafwatering,fysiekvoorkomen,voorkomen,hoogteniveau,functie,getijdeinvloed,vaarwegklasse,naamofficieel,naamnl,sluisnaam,brugnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_waterdeel_vlak
echo 13 - Wegdeel
ogr2ogr -update -f GPKG -select typeweg,hoogteniveau,typeinfrastructuur,hoofdverkeersgebruik,fysiekvoorkomen,verhardingsbreedteklasse,verhardingstype,status,naam,brugnaam,tunnelnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_wegdeel_lijn
ogr2ogr -update -f GPKG -select typeweg,hoogteniveau,typeinfrastructuur,hoofdverkeersgebruik,fysiekvoorkomen,verhardingsbreedteklasse,gescheidenrijbaan,verhardingstype,aantalrijstroken,verhardingstype,status,awegnummer,nwegnummer,ewegnummer,swegnummer,afritnummer,afritnaam,naam,knooppuntnaam,brugnaam,tunnelnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_wegdeel_hartlijn
ogr2ogr -update -f GPKG -select typeweg,hoogteniveau,typeinfrastructuur,hoofdverkeersgebruik,fysiekvoorkomen,verhardingsbreedteklasse,verhardingstype,status,naam,brugnaam,tunnelnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_wegdeel_punt
ogr2ogr -update -f GPKG -select typeweg,hoogteniveau,typeinfrastructuur,hoofdverkeersgebruik,fysiekvoorkomen,verhardingsbreedteklasse,gescheidenrijbaan,verhardingstype,aantalrijstroken,verhardingstype,status,awegnummer,nwegnummer,ewegnummer,swegnummer,afritnummer,afritnaam,naam,knooppuntnaam,brugnaam,tunnelnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_wegdeel_hartpunt
ogr2ogr -update -f GPKG -select typeweg,hoogteniveau,typeinfrastructuur,hoofdverkeersgebruik,fysiekvoorkomen,verhardingsbreedteklasse,gescheidenrijbaan,verhardingstype,aantalrijstroken,verhardingstype,status,awegnummer,nwegnummer,ewegnummer,swegnummer,afritnummer,afritnaam,naam,knooppuntnaam,brugnaam,tunnelnaam ..\maps\merged_gpkg\merge0010.gpkg ..\maps\gpkg\top10nl_Compleet%POSTFIX%.gpkg top10nl_wegdeel_vlak

echo done %date% %time%
