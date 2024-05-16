# Modeling

I use-caset har vi valgt: se informasjon om solnedgang og skydekke på lokasjon x

Det finnes mange måter å modellere denne casen.
Når bruker ønsker å se detaljer på en gitt lokasjon, må en navigere til en PlaceInfoScreen/detaljskjerm. Dette kan gjøres på følgende måter:
- Bruker kan navigere via Kart (MapListScreen) i navigasjonsbar. i denne skjermen kan bruker både se lokasjoner som pins på et kart,
og som en liste ved å trykke på togglebaren øverst på skjermen. Appen vil huske hvilken av disse to brukeren var på sist.
  - Det vil si: var bruker på kartet i MapListScreen, navigerer til Favoritter og så tilbake, 
  vil bruker se kart i MapListscreen. Var bruker i listen, navigerer til Favoritter og tilbake til MapListScreen,
  vil bruker se listen. Det er derfor vi har spesifisert dette i use-caset.
  - Bruker kan trykke på en pin. Da popper et lite kort opp, og bruker kan trykke på dette for å komme til en PlaceInfoScreen.
  - Bruker kan velge listen i togglebaren og vil da kunne trykke på ønsket lokasjon i listen. Da komm bruker til MapListScreen.
- Dersom bruker har lagt lokasjonen til som favoritt, kan bruker nå detaljer ved å trykke på Favoritter i navigasjonsbaren.
  Da vil bruker se disse stedene i en liste, og kan trykke på lokasjonen og bli før til PlaceInfoScreen.
  - Favoritter syns også på hjemskjermen, og bruker kan bla i denne listen og trykke på ønsket lokasjon. Da kommer
  bruker til detaljskjermen.
- Dersom bruker har lagt til denne lokasjonen selv, kan bruker navigere til Min side. Her er lokasjonene presentert 
i en liste. Bruker kan trykke på én av disse og så komme til detaljskjermen.

For å begrense caset har vi dermed følgende forhåndsbetingelser:
- bruker er på en vilkårlig skjerm i appen og vil navigere fra denne til en detaljskjerm/PlaceInfoScreen
- sist bruker var på MapListScreen, valgte bruker å se lokasjonene i listeformen, ikke i kartformat
- det er ikke første gang bruker har brukt appen.
- lokasjonen som bruker vil se eksisterer i appen, enten som en lokasjon de selv har lagt til eller lokasjonene
som fulgte med appen.

Følgende diagrammer beskriver use caset fra ulike perspektiver.

## Use case diagram
![img.png](Modeling/Usecasediagram.png)

## Klassediagram

![img.png](Modeling/Klassediagram.png)

## Sekvensdiagram
![img.png](Modeling/Sekvensdiagram.png)

## Flytdiagram
![img.png](Modeling/Aktivitetsdiagram.png)
