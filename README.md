# Skumring

## En prosjektoppgave i IN2000 ved Universitet i Oslo

### Gruppemedlemmer

- Adrian Emil Chambe-Eng
- Mari Svennevik Notland
- Viljar Drevland Hardersen
- Anna Hansgård
- Synnøve Nordvik Helgesen


## Dokumentasjon
Dokumentasjonen til appen finnes i ARCHIECUTRE.md, og er også beskrevet i rapporten tilhørende prosjektarbeidet.

## Aksessere appen
Appen kan hentes under “releases” i github-repoet hvor man kan laste ned en APK fil som kan installeres rett på en Androidtelefon.
All kode og ressurser er lastet opp i github-repoet, slik at om en ønsker kan man klone prosjektet, åpne det i Android Studio, og kompilere appen selv.

## Kjøre appen
Ved å laste ned appen, kan den kjøres i emulator i Android Studio. Emulatoren må kjøpe på emulator med API-nivå minst 31.

## Biblioteket

Vi benytter oss av standardbiblioteket som følger med Android. Core, Navigation, Viewmodel og Testing. 
I tillegg bruker vi Ktor til håndtering av API-kall og -responser som anbefalt i kurset. 
Mapbox brukes til å håndtere posisjonsdata, i tillegg til:
- Kart
- Lokasjoner
- Geocoding
- Avstandsberegning
- Reisetidsberegning

Ellers bruker vi følgende:
<br>Room - brukes som intern database 
<br> Location - brukes til å håndetere brukerlokasjon.
<br>SplashScreen - brukes for å lage splashscreen. 
