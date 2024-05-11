#!/bin/bash

#declare -a dir="/home/downloads/2021-11"
#declare -a ext="-2021"
#declare -a release="2021-11"

#declare -a dir="/home/downloads/2023-09"
#declare -a ext="-2023"
#declare -a release="2023-09"

#declare -a dir="/home/downloads/2024-02"
#declare -a ext=""
#declare -a release="2024-02"

declare -a dir="/home/downloads/2024-04"
declare -a ext=""
declare -a release="2024-04"

header(){
  echo -e "\n"
  echo "$1"
  echo -e "\n"
}

showTables(){
  header "## FILE: $1"
  # List tables
  ogrinfo -sql "select table_name from gpkg_contents order by table_name" $1 \
  | grep ' table_name' \
  | sed 's/table_name (String) = /* /g' \
  | while read layer; do 
    echo "$layer"
  done  
}

showTableDefinitions(){
  header "## FILE: $1"
  ## Table definitions
  ogrinfo -sql "select table_name from gpkg_contents order by table_name" $1 \
  | grep ' table_name' \
  | sed 's/table_name (String) = //g' \
  | while read layer; do 
    processLayer "$1" "$layer"; 
  done
}

processLayer(){
 header "### FILE: $1 TABLE: $2"
  ogrinfo -sql "PRAGMA table_info($2)" $1 \
  | grep '  name\|  type' \
  | sed 's/  name (String) = /* /g' \
  | sed -z 's/\n  type (String) = / - /g' 
}

distinct(){
  header "### Layer: $2 Attribute: $3"
  ogrinfo -sql "SELECT DISTINCT $3 FROM $2 ORDER BY $3" $1 \
  | grep "  $3" | sed 's/.* = /* /g'
}

# MAIN FUNCTIONS
header "# LAYERS/TABLES $release"
showTables "$dir/top10nl_Compleet$ext.gpkg"
showTables "$dir/top50nl_Compleet$ext.gpkg"
showTables "$dir/top100nl_Compleet$ext.gpkg"
showTables "$dir/top250nl_Compleet$ext.gpkg"
showTables "$dir/top500nl_Compleet$ext.gpkg"
showTables "$dir/top1000nl_Compleet$ext.gpkg"

header "# INFO $release"
header "## GEBOUW"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_gebouw_punt typegebouw
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_gebouw_vlak typegebouw

header "## FUNCTIONEEL GEBIED"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_functioneel_gebied_punt typefunctioneelgebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_functioneel_gebied_vlak typefunctioneelgebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_functioneel_gebied_multivlak typefunctioneelgebied

header "## GEOGRAFISCH GEBIED"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_geografisch_gebied_vlak typegeografischgebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_geografisch_gebied_multivlak typegeografischgebied
distinct $dir/top250nl_Compleet$ext.gpkg top250nl_geografisch_gebied_punt type_geoggebied
distinct $dir/top250nl_Compleet$ext.gpkg top250nl_geografisch_gebied_vlak type_geoggebied

header "## HOOGTE"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_hoogte_punt typehoogte
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_hoogte_lijn typehoogte

header "## INRICHTINGSELEMENTEN"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_inrichtingselement_punt typeinrichtingselement
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_inrichtingselement_lijn typeinrichtingselement

header "## PLAATS"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_plaats_punt typegebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_plaats_vlak typegebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_plaats_multivlak typegebied

header "## REGISTRATIEF GEBIED"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_registratief_gebied_vlak typeregistratiefgebied
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_registratief_gebied_multivlak typeregistratiefgebied

header "## SPOORBAANDEEL"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_spoorbaandeel_punt typespoorbaan
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_spoorbaandeel_lijn typespoorbaan
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_spoorbaandeel_punt fysiekvoorkomen
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_spoorbaandeel_punt typeinfrastructuur
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_spoorbaandeel_punt aantalsporen

header "## TERREIN"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_terrein_vlak typelandgebruik
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_terrein_vlak voorkomen
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_terrein_vlak fysiekvoorkomen

distinct $dir/top50nl_Compleet$ext.gpkg top50nl_terrein_vlak type_landgebruik

distinct $dir/top100nl_Compleet$ext.gpkg top100nl_terrein_vlak type_landgebruik

distinct $dir/top250nl_Compleet$ext.gpkg top250nl_terrein_vlak type_landgebruik

distinct $dir/top500nl_Compleet$ext.gpkg top500nl_terrein_vlak type_landgebruik

distinct $dir/top1000nl_Compleet$ext.gpkg top1000nl_terrein_vlak type_landgebruik

header "## WATER"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_punt typewater
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_lijn typewater
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_vlak typewater
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_lijn breedteklasse
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_lijn voorkomen
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_lijn fysiekvoorkomen
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_waterdeel_lijn functie

header "## WEGDEEL"
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_punt typeweg
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_lijn typeweg
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn typeweg
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn fysiekvoorkomen
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn status
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn verhardingstype
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn hoofdverkeersgebruik
distinct $dir/top10nl_Compleet$ext.gpkg top10nl_wegdeel_hartlijn typeinfrastructuur


header "# LAYER TABLE DEFINITION $release"
showTableDefinitions "$dir/top10nl_Compleet$ext.gpkg"
showTableDefinitions "$dir/top50nl_Compleet$ext.gpkg"
showTableDefinitions "$dir/top100nl_Compleet$ext.gpkg"
showTableDefinitions "$dir/top250nl_Compleet$ext.gpkg"
showTableDefinitions "$dir/top500nl_Compleet$ext.gpkg"
showTableDefinitions "$dir/top1000nl_Compleet$ext.gpkg"

