# Run the script from the tileserver directory
docker run --rm -d -p 8080:80 -v $(pwd):/data maptiler/tileserver-gl -c config.json -p 80

