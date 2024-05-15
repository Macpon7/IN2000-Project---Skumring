# Arkitektur

## MVVM
Vi har basert oss på “MVVM”-arkitektur i at vi har lagt vekt på et tydelig skille mellom datalaget, logikken som virker på dem, logikken som holder styr på tilstanden deres og funksjonene som bringer de til skjermen. Lagret i forskjellige pakker og med en oversiktlig mappestruktur er det meget enkelt å navigere seg frem til delene av appen man ønsker å jobbe med, enten det er logikken som bestemmer hvorvidt været er bra eller hvordan det presenteres på skjermen. Alle Views har sin egen ViewModel, og en ViewModel kan kalle på flere repositories. Repositories kan kalle på flere DataSources som lager klasser basert på Models, og lage sine egne klasser ut av å kombinere dataen den tar inn.
Cluet med MVVM for oss var at vi satte opp interfaces og tomme klasser slik at vi kunne kode parallelt i alle lag da vi hadde laget klassene med hva de skulle inneholde og tomme funksjoner med parametre og returverdi. Da trenger ikke logikken skrives med én gang, men man kan fortsatt skrive Views som displayer dummydata.

## Model
I modell-/datalaget blir respons fra API-ene lagret og bearbeidet til egendefinerte objekter som kun har informasjonen vi er ute etter. Alle API-ene har egne klasser for responsdata, og disse har blitt generert av en Android Studio-plugin “JSON To Kotlin Class”. Vi valgte å samle alle responsklassene i en enkel klasse *ResponseData.kt, men lagret i samme pakke som vår egen klasse med relevant info. Skjelettene til alle klassene ligger i en “models”-pakke og lages av *DataSource.kt i “data”-pakken.
I tillegg har vi implementert et domenelag i form av repositories som henter informasjon fra *DataSource-filer, bearbeider dem og samler informasjonen appen er laget for å vise. De kombinerer og kryssjekker objekter vi får fra datalaget og lagrer dem i en intern database, implementert via Room. Funksjonene for å hente ut denne informasjonen blir da brukt av flere ViewModels, og takket være oppsettet jobber alle ViewModels på samme data slik at informasjonen bruker blir presentert med er aktuell og konsekvent på tvers av skjermer. All data som blir lagt inn i databasen har et timestamp, og hvis tidspunktet på siste innlegg er over en time gammelt vil den kalle på API-ene for alle lagrede lokasjoner og oppdatere dataen.

## ViewModel
ViewModels holder styr på og oppdaterer informasjon fra database/repository. Deres oppgave er å hoiste denne informasjonen derfra og til Views, i tillegg til å plukke opp feilmeldinger. Mottar den en exception fra datalaget vil den be Views vise en Snackbar med en forståelig feilmelding, og gi bruker mulighet til å krysse den ut for å fortsette eller oppdatere informasjonen på nytt. Slik som koden er nå har alle views sin egen snackbar, og ikke en felles ressurs.

## View
Views sin oppgave er å følge med på tilstandene lagret i ViewModels og vise de på en presentabel og forståelig måte for brukeren. De lar også brukeren interagere med appen og sende forespørsler nedover i lagene som henter informasjonen frem. I forskjellige views er det også noe duplisert kode og kall på variable som ikke er lagt inn helt korrekt idéelt, så skulle disse måtte endres for noen grunn vil det føre til noe ekstra manuelt arbeid.

## Kohesjon og kobling
I det store og hele har vi forsøkt å holde objektene så spesifikke som mulig, dette for å sikre høy kohesjon slik at de ikke blir for store og vanskelige å vedlikeholde i tilfelle vi skulle ønske å endre informasjonen de bærer. Den lave koblingen er noe diskuterbar - Som nevnt er noen av objektene som inneholder informasjonen vi ønsker å vise sammensatte av reponsdataen fra forskjellige API-er og noe mer komplekse, men dette er av nødvendighet i at appens oppgave er å presentere brukeren er en tolkning av veldig sammensatt data. Vi vil fortsatt si at vi har klart å holde koblingen så lav som praktisk mulig i at appen fortsatt funker, kjører og displayer det den kan selv om den skulle mangle informasjon.

## Androidversjon
Skumring kjører på minimum API level 26 da dette kreves for brukerlokasjon, en sentral funksjonalitet for løsningen. Mesteparten av testingen har foregått på API level 28 og 31 med Android Version 12.0. 

