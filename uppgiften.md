# Uppgiften
Lös följande problem:

Integration mellan två gamla system

Det ena systemet levererar ett radbaserat filformat medan det andra kräver XML. Du ska skriva en konverterare som bygger upp rätt XML-struktur.
Filformat:
```
P|förnamn|efternamn
T|mobilnummer|fastnätsnummer
A|gata|stad|postnummer
F|namn|födelseår
P kan följas av T, A och F
F kan följas av T och A
```
Exempel:
```
P|Elof|Sundin
T|073-101801|018-101801
A|S:t Johannesgatan 16|Uppsala|75330
F|Hans|1967
A|Frodegatan 13B|Uppsala|75325
F|Anna|1969
T|073-101802|08-101802
P|Boris|Johnson
A|10 Downing Street|London
```
Ger XML som:

```
<people>
    <person>
        <firstname>Elof</firstname>
        <lastname>Sundin</lastname>
        <address>
            <street>S:t Johannesgatan 16</street>
            <city>Uppsala</city>
            <zip>75330</zip>
        </address>
        <phone>
            <mobile>073-101801</mobile>
            <landline>018-101801</landline>
        </phone>
        <family>
            <name>Hans</name>
            <born>1967</born>
            <address>...</address>
        </family>
        <family>...</family>
    </person>
    <person>...</person>
</people>
```