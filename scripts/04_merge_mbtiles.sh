#!/bin/bash
date
../tippecanoe-master/tile-join --no-tile-size-limit -o ../maps/mbtiles_result/topnl.mbtiles ../maps/mbtiles/*.mbtiles
date