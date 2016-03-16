clear
export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH

fileName="lindenallee_kranichweg_v1.gml"

fileText=$(cat $fileName)

#echo "$fileText"

#all lines with postlist
lcoor="$(grep "gml:posList" $fileName) \n"



convertingToWGS84(){


i=$(printf "$lcoor" |
 wc -l
 )

printf "$lcoor" |
while read l; do

	i=$(($i-1))

	#just the numbers
	initialLine=$(
		printf "$l" |
		sed 's/<gml:posList>//g' |
		sed 's/<\/gml:posList>//g'
	)

	#1 line per number
  	coor1Col=$(
		printf "$initialLine" |
		sed 's/ /\'$'\n/g'
	)

	#3 numbers per line
	coor3Col=$(
		printf "$coor1Col" |
		while read a; read b; read c; do
		  echo $a $b $c
		done 
	)

	#WGS84 Transformation
	coorWGS84=$(
		printf "$coor3Col" | 
		gdaltransform -s_srs EPSG:31463 -t_srs EPSG:4326
	)

	#printf "\n...... $coorWGS84\n"

	#all in the same line again
	finalLine=$(
		echo "$coorWGS84" |
		tr "\n" " "
	)

	#printf "$initialLine -> $finalLine \n\n\n\n"

	fileText=$(printf "$fileText" |
	 sed "s/$initialLine/$finalLine/g"
	)
	
	#Return
	if [ "$i" -eq 0 ]; then
		printf "$fileText"
	fi
done  

}

fileText2=$(convertingToWGS84)

printf "$fileText2" > "$fileName _WGS84.gml"