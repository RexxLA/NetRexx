for filename in *.nrx
do 
sed 's/* <sgml>/** /g' $filename >$filename'.new'
done