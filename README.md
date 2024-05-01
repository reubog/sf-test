# Uppgift/Test

Lös följande [uppgift](uppgiften.md)

## Bygg
* Installera Java 21 LTS, kontrollera  med ```java -version```
* Klona gitrepot ```git@github.com:reubog/sf-test.git```
* Bygg med ```./mvnw clean install```
* 
### Körning
Exekvera med ```./mvnw exec:java -Dexec.args="--infil src/test/resources/infil.txt --utfil utfil.xml```
