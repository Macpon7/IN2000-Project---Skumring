# Arkitektur

## MVVM

Vi har basert oss på “MVVM”-arkitektur i at vi har lagt vekt på et tydelig skille mellom datalaget, logikken som virker på dem, logikken som holder styr på tilstanden deres og funksjonene som bringer de til skjermen. Lagret i forskjellige pakker og med en oversiktlig mappestruktur er det meget enkelt å navigere seg frem til delene av appen man ønsker å jobbe med, enten det er logikken som bestemmer hvorvidt været er bra eller hvordan det presenteres på skjermen. Alle Views har sin egen ViewModel, og en ViewModel kan kalle på flere repositories. Repositories kan kalle på flere DataSources som lager klasser basert på Models, og lager sine egne klasser ved å kombinere dataen den tar inn.
Cluet med MVVM for oss var at vi satte opp interfaces og tomme klasser slik at vi kunne kode parallelt i alle lag da vi hadde laget klassene med hva de skulle inneholde og tomme funksjoner med parametre og returverdi. Da trenger ikke logikken skrives med én gang, men man kan fortsatt skrive Views som displayer dummydata.

## Model

I modell-/datalaget blir respons fra API-ene lagret og bearbeidet til egendefinerte objekter som kun har informasjonen vi er ute etter. Alle API-ene har egne klasser for responsdata, og disse har blitt generert av en Android Studio-plugin “JSON To Kotlin Class”. Vi valgte å samle alle responsklassene for hver API i en enkel fil *ResponseData.kt, men lagret i samme pakke som vår egen klasse med relevant info. Skjelettene til alle klassene ligger i en “models”-pakke og lages av *DataSource.kt i “data”-pakken.
I tillegg har vi implementert et domenelag i form av repositories som henter informasjon fra *DataSource-filer, bearbeider dem og samler informasjonen appen er laget for å vise. De kombinerer og kryssjekker objekter vi får fra datalaget og lagrer dem i en intern database, implementert via Room. Funksjonene for å hente ut denne informasjonen blir da brukt av flere ViewModels, og takket være oppsettet jobber alle ViewModels på samme data slik at informasjonen bruker blir presentert med er aktuell og konsekvent på tvers av skjermer. All data som blir lagt inn i databasen har et timestamp, og hvis tidspunktet på siste innlegg er over en time gammelt vil den kalle på API-ene for alle lagrede lokasjoner og oppdatere dataen.

## ViewModel

ViewModels holder styr på og oppdaterer informasjon fra database/repository. Deres oppgave er å hoiste denne informasjonen derfra og til Views, i tillegg til å plukke opp feilmeldinger. Mottar den en exception fra datalaget vil den be Views vise en Snackbar med en forståelig feilmelding, og gi bruker mulighet til å krysse den ut for å fortsette eller oppdatere informasjonen på nytt. Slik som koden er nå har alle views sin egen snackbar, heller enn å dele denne som en felles ressurs.

## View

Views sin oppgave er å følge med på tilstandene lagret i ViewModels og vise de på en presentabel og forståelig måte for brukeren. De lar også brukeren interagere med appen og sende forespørsler nedover i lagene som henter informasjonen frem. I forskjellige views er det noe duplisert kode og kall på variabler som ikke er lagt inn helt korrekt. Skulle disse måtte endres vil det føre til noe ekstra manuelt arbeid.

## Kohesjon og kobling

I det store og hele har vi forsøkt å holde objektene så spesifikke som mulig, dette for å sikre høy kohesjon slik at de ikke blir for store og vanskelige å vedlikeholde i tilfelle vi skulle ønske å endre informasjonen de bærer. Den lave koblingen er noe diskuterbar - Som nevnt er noen av objektene som inneholder informasjonen vi ønsker å vise sammensatte av reponsdataen fra forskjellige API-er og noe mer komplekse, men dette er av nødvendighet i at appens oppgave er å presentere brukeren en tolkning av sammensatt data. Vi vil fortsatt si at vi har klart å holde koblingen så lav som praktisk mulig siden appen fortsatt funker, kjører og displayer det den kan selv om den skulle mangle informasjon.

## Kommentering

Docstrings og kommentering av kode er delvis mangelfull da vi mot slutten av prosjektet måtte legge 100 % fokus på å få kodeoppgavene vi var tildelte til å fungere som de skulle. Vi har også kjørt autoformatter på alle filer. Vi er ikke enige i alt den har foreslått, men har gått med på det meste da dette er anbefalt at vi gjør.

## Androidversjon

Skumring kjører på minimum API level 31 da dette kreves for stabilitet i MapBox og måten vi løste slpashscreen på. MapBox ble brukt for å løse lokasjonstjenester brukerne våre ønsket.Mesteparten av testingen har foregått på API level 31 med Android Version 12.0.

# Errors og Warnings

Her har vi benyttet oss av Android Studio sin "Problems" for å finne og håndtere.

## Errors

Det er et par errors, men disse er suppressed da vi har drevet med eksperimentelle API-er.

## Warnings

### Android Lint

- **Redundant Label:** Lar vi stå fordi vi liker den
- **Long Vector Paths:** Disse er autogenererte og skal ikke endres i kode, vi lar de stå.
- **Obsolete SDKs:** Vi måtte bruke et eldre bibliotek på for å håndtere info om appen lokalt, for eksempel når den starter for første gang og hvilket språk bruker vil ha. Sistnevnte endte vi med å ikke implementere.
- **Static Field Leaks:** MyPageViewModel trenger context, så denne blir ignorert. Design feature.
- **Image defined in density-independent drawable folder:** Vi la alle ikoner i drawable-mappen selv om de er PNG-er. Vi anser dette som trygt nok for nå da vi antar at DPI holder seg jevnt på tvers av bruk, og ikke vil bli utsatt for store sprik.
- **Monochrome icon is not defined:** Vi har bare en variant av ikonet, og den er ikke monokrom.

### JVM Languages

- **Possibly blocking call in non-blocking context:** Vi prøvde å justere på denne uten suksess. Den dukket opp helt til slutt, og vi har ikke rukket å fikse den.

### Java

Feilene her er ikke feil og ignoreres. Den forstår ikke event handling parametre og hevder noen funksjoner ikke krever suspend når de gjør det. I tillegg tror den at diverse andre parametre og variable ikke blir brukt når de gjør det. "Empty methods" er dummyfunksjoner som brukes i previews til composables som krever event handlers.

### Kotlin

Naming conventions: Den klager på dårlig navnekonvensjon i responsklassene til API. Disse blir ignorert da disse klasenavnene må være identiske til det de tilsvarer i JSON-filen vi får.

### Proofreading

Android Studio liker ikke norsk, pluss et par stavefeil i noen variabler.
