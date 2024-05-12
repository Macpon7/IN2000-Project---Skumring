package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

@Composable
fun UserInstructionsDialog (
closeDialog: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
           modifier = Modifier.padding(5.dp)

        ) {
            NewPlaceDialogInstructions()
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, top = 10.dp)
            )
            {
                TextButton(
                    onClick = {
                        closeDialog()
                    },
                    contentPadding = PaddingValues(10.dp),
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Button(
                    onClick = {
                        closeDialog()
                    },
                    contentPadding = PaddingValues(top = 8.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
                    modifier = Modifier.padding(start = 15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),

                    ) {
                    Text(
                        text = "neste",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.bodyLarge,
                        )
                }

            }
        }
    }
}


@Composable
fun HomeScreenInstructions() {
    Text(
        text = "Det første du møter er hjemskjermen, her ser du værvarselet for solnedgang på din lokasjon i dag",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1.Tid for solnedgang",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2. Mer informasjon om \n" + "værforholdene",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3. Temperaturen ved \n"+"solnedgang",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "4. Tid for gylne \n"+"-og blåtime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Icon(
            painterResource(id = R.drawable.homescreeninstructions_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun HomeScreenInstructionsFavourite() {
    Text(
        text = "Legg til dine favorittsteder og få de opp på hjemskjermen!",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1.Alle kort kan trykkes for \n" +
                        "mer informasjon",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )

        }
        Icon(
            painterResource(id = R.drawable.homescreeninstructions_favourite_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun MapListInstructions() {
    Text(
        text = "På kartskjermen kan du se din posisjon og fine steder for solnedgang i nærheten ",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1. Trykk for liste istedenfor kart"
                ,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2. Blå runding viser din posisjon",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3. Røde pins viser lagrede steder",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Icon(
            painterResource(id = R.drawable.maplistinstructions_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun MapListInstructionsPopUp() {
    Text(
        text = "Trykk på pinsene for mer informasjon",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1. Ved å trykke på pinsene \n" + "kan du få informasjon om \n" + "værforhold i dag"
                ,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2. Legg til som favoritt",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3. Lyst på mer informasjon \n" + "om stedet? Trykk deg videre!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Icon(
            painterResource(id = R.drawable.maplistpopupinstructions_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}



@Composable
fun PlaceInfoInstructions() {
    Text(
        text = "Trykk på kortene for mer informasjon om stedet og distanse fra din posisjon ",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1. Se ditt lagrede bilde \n og les din egen beskrivelse \nav stedet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2. Se avstand fra din \n posisjon",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3. Scroll ned for \n flerdagersvarsel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Icon(
            painterResource(id = R.drawable.placeinfoscreeninstructions_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}


@Composable
fun NewPlaceDialogInstructions() {
    Text(
        text = "På “min side” kan du legge til dine steder og endre på innstillinger",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
    Row(
    )
    {
        Column (modifier = Modifier.padding(start = 5.dp)) {
            Text(
                text = "1. PS! Få samme “popup” \n på kartet ved å trykke lenge \n på stedet du ønsker å \n legge til bilde\n",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2. Instillinger",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3. Velg å bruke telefonens \n posisjon",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Icon(
            painterResource(id = R.drawable.newplacedialoginstructions_norwegian),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}


@Preview
@Composable
fun UserInstructionsDialogTest(
) {
    val closeDialog: () -> Unit = {}
    UserInstructionsDialog(closeDialog)
}