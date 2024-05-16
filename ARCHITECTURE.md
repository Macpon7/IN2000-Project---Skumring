# Arkitektur

## MVVM

Appens utviklingsprosess er basert på “MVVM”-arkitektur i at det har lagt vekt på et tydelig skille mellom datalaget, logikken som virker på det, logikken som holder styr på tilstanden deres og funksjonene som bringer de til skjermen. Lagret i forskjellige pakker og med en oversiktlig mappestruktur er det meget enkelt å navigere seg frem til delene av appen man ønsker å jobbe med, enten det er logikken som bestemmer hvorvidt været er bra, eller hvordan det presenteres på skjermen. Alle Views har sin egen ViewModel, og en ViewModel kan kalle på flere repositories. Repositories kan kalle på flere DataSources som lager klasser basert på Models, og lager sine egne klasser ved å kombinere dataen den tar inn.
Med MVVM som grunnlag får man satt opp interfaces og tomme klasser slik at man kan kode parallelt i alle lag da man kan lage klasser med hva de skal inneholde og tomme funksjoner med parametre og returverdi. Da trenger ikke logikken skrives med én gang, men man kan fortsatt skrive Views som displayer dummydata.

## Model

I modell-/datalaget blir respons fra API-ene lagret og bearbeidet til egendefinerte objekter som kun har relevant informasjon. Alle API-ene har egne klasser for responsdata, og disse har blitt generert av en Android Studio-plugin “JSON To Kotlin Class”. Alle responsklassene for hver API er samlet i en enkel fil *ResponseData.kt, men lagret i samme pakke som egendefinerte egen klasser med kun relevant info. Skjelettene til alle klassene ligger i en “models”-pakke og lages av *DataSource.kt i “data”-pakken.
I tillegg er det et domenelag i form av repositories som henter informasjon fra *DataSource-filer, bearbeider dem og samler informasjonen appen er laget for å vise. De kombinerer og kryssjekker objekter fra datalaget og lagrer dem i en intern database, implementert via Room. Funksjonene for å hente ut denne informasjonen blir da brukt av flere ViewModels, og takket være oppsettet jobber alle ViewModels på samme data slik at informasjonen bruker blir presentert med er aktuell og konsekvent på tvers av skjermer. All data som blir lagt inn i databasen har et timestamp, og hvis tidspunktet på siste innlegg er over en time gammelt vil den kalle på API-ene for alle lagrede lokasjoner og oppdatere dataen.

## ViewModel

ViewModels holder styr på og oppdaterer informasjon fra database/repository. Deres oppgave er å hoiste denne informasjonen derfra og til Views, i tillegg til å plukke opp feilmeldinger. Mottar den en exception fra datalaget vil den be Views vise en Snackbar med en forståelig feilmelding, og gi bruker mulighet til å krysse den ut for å fortsette eller oppdatere informasjonen på nytt. Slik som koden er nå har alle views sin egen snackbar, heller enn å dele denne som en felles ressurs.

## View

Views sin oppgave er å følge med på tilstandene lagret i ViewModels og vise de på en presentabel og forståelig måte for brukeren. De lar også brukeren interagere med appen og sende forespørsler nedover i lagene som henter informasjonen frem. I forskjellige views er det noe duplisert kode og kall på variabler som ikke er lagt inn helt korrekt. Skulle disse måtte endres vil det føre til noe ekstra manuelt arbeid.

## Kohesjon og kobling

Objektene er stort sett holdt så spesifikke som mulig, dette for å sikre høy kohesjon slik at de ikke blir for store og vanskelige å vedlikeholde i tilfelle en skulle ønske å endre informasjonen de bærer. Den lave koblingen er noe diskuterbar - Som nevnt er noen av objektene sammensatte av reponsdataen fra forskjellige API-er og noe mer komplekse, men dette er av nødvendighet i at appens oppgave er å presentere brukeren en tolkning av sammensatt data. Koblingen er ellers så lav som praktisk mulig siden appen fortsatt funker, kjører og displayer det den kan selv om den skulle mangle informasjon.

## Kommentering

Docstrings og kommentering av kode er delvis mangelfull da det mot slutten av prosjektet ble lagt 100 % fokus på å få å fungere som de skulle. De fleste filer har blitt gått over med autoformatter. Selv om alle forslagene ikke er ansett som helt idéelle har dette fortsatt blitt gjennomført da det er anbefalt. 

## Androidversjon

Skumring kjører på minimum API level 31 da dette kreves for stabilitet i MapBox og måten vi løste slpashscreen på. MapBox ble brukt for å løse lokasjonstjenester brukerne våre ønsket.Mesteparten av testingen har foregått på API level 31 med Android Version 12.0.

# Errors og Warnings

Her har Android Studio sin "Problems" blitt benyttet for å finne og håndtere errors og warnings. 

## Errors

Android Studio har rapportert om et par errors, men disse er suppressed da vi har jobbet med eksperimentelle API-er.

## Warnings

### Android Lint

- **Redundant Label:** Ignoreres.
- **Long Vector Paths:** Disse er autogenererte og skal ikke endres i kode.
- **Obsolete SDKs/Gradle Dependency:** Vi måtte bruke et eldre bibliotek på for å håndtere info om appen lokalt, for eksempel når den starter for første gang og hvilket språk bruker vil ha. Sistnevnte endte vi med å ikke implementere.
- **Vector Image Generation:** Ikoner liker ikke å være større enn 200x200 pga tiden det tar å laste de. Skumring sitt ikon er litt større, men dette er ikke av konsekvens da den bare vises med alle andre appene på enheten og på splash screen.
- **Static Field Leaks:** MyPageViewModel trenger context, så warningen ignoreres.
- **Image defined in density-independent drawable folder:** Alle ikoner ligger i drawable-mappen selv om de er PNG-er. Det anses som trygt nok for nå da det antas at DPI holder seg jevnt på tvers av bruk, og ikke vil bli utsatt for store sprik.
- **Monochrome icon is not defined:** Det er bare en variant av ikonet, og den er ikke monokrom.

### JVM Languages

- **Possibly blocking call in non-blocking context:** Denne ble forsøkt håndtert uten suksess. Den dukket opp helt til slutt, og ble ikke fikset på grunn av tidspress.

### Java

Feilene her er ikke feil og ignoreres. Den forstår ikke event handling parametre og hevder noen funksjoner ikke krever suspend når de gjør det. I tillegg tror den at diverse andre parametre og variable ikke blir brukt når de gjør det. "Empty methods" er dummyfunksjoner som brukes i previews til composables som krever event handlers.

### Kotlin

Naming conventions: Den klager på dårlig navnekonvensjon i responsklassene til API. Disse blir ignorert da disse klassenavnene må være identiske til det de tilsvarer i JSON-filen vi får.

### Proofreading

Norsk dukker opp som stavefeil i Android Studio. Noen variabelnavn har kanskje ord som er feilstavet, men disse har ikke vært av konsekvens for vår arbeidsflyt eller appens funksjonalitet.
