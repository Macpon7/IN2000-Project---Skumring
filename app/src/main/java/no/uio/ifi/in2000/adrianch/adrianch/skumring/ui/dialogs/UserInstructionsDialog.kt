package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme


@Composable
fun UserInstructionsDialog (
closeDialog: () -> Unit
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    val totalScreens = 8

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            when (currentScreen) {
                0 -> FirstScreenInstructions()
                1 -> HomeScreenInstructions()
                2 -> HomeScreenInstructionsFavourite()
                3 -> MapListInstructions()
                4 -> MapListInstructionsPopUp()
                5 -> PlaceInfoInstructions()
                6 -> NewPlaceDialogInstructions()
                7 -> LastScreenInstructions()
            }
        }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(end = 15.dp, top = 10.dp, bottom = 20.dp)
                    .fillMaxSize()
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
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Button(
                    onClick = {
                        currentScreen++
                        if (currentScreen >= totalScreens) {
                            currentScreen = 0
                            closeDialog()
                        }
                    },
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                    modifier = Modifier.padding(start = 15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                ) {
                    Text(
                        text = stringResource(id = R.string.UserInstructionsDialog_next_button),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }
        }
    }

@Composable
fun FirstScreenInstructions() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.firstscreen_instructions_welcome_skumring),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 35.dp, bottom = 35.dp)
        )
        Icon(
            painterResource(id = R.drawable.icon),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(30.dp))
                .size(180.dp)
        )
        Text(
            text = stringResource(R.string.firstscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            text =
            stringResource(R.string.firstscreen_moreinfo_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 65.dp)
        )
    }
}




@Composable
fun HomeScreenInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")
Box(modifier = Modifier
    .fillMaxWidth()
    .heightIn(min = 120.dp)) {
    Text(
        text = stringResource(R.string.homescreen_instructions_skumring),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
    )
}
    Row (
        modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
    ){
        Column (modifier = Modifier.weight(1f).padding(end = 2.dp))  {
            Text(
                text = "1." + stringResource(R.string.homescreen_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.homescreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text =
                "3." + stringResource(R.string.homescreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "4." + stringResource(R.string.homescreen_instructions_4),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),

        ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.homescreen_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.homescreen_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.homescreen_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.homescreen_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "HomeScreenInstructions",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun HomeScreenInstructionsFavourite() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)) {
        Text(
            text = stringResource(R.string.homescreen_favourite_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row( modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    )
    {
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.homescreen_favourite_instruction),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
            ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.homescreen_favourite_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.homescreen_favourite_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.homescreen_favourite_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.homescreen_favourite_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "HomeScreenFavouriteInstructions",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun MapListInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)) {
        Text(
            text = stringResource(R.string.maplistscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row( modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    )
    {
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.maplistscreen_instructions_1)
                ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.maplistscreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.maplistscreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
        ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.maplistscreen_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.maplistscreen_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.maplistscreen_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.maplistscreen_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "MapListScreen instructions",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun MapListInstructionsPopUp() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)) {
        Text(
            text = stringResource(R.string.maplistscreen_popup_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row( modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    )
    {
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.maplist_popup_instructions_1)
                ,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.maplist_popup_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.maplist_popup_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
        ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.maplistpopup_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.maplistpopup_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.maplist_popup_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.mapscreen_popup_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "MapListScreen popup instructions",
                tint = Color.Unspecified
            )
        }

    }
}



@Composable
fun PlaceInfoInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)) {
        Text(
            text = stringResource(R.string.placeinfoscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    )
    {
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.placeinfoscreen_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.placeinfoscreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.placeinfoscreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
        ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.placeinfoscreen_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.placeinfoscreen_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.placeinfoscreen_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.placeinfoscreen_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "Placeinfoscreen instructions",
                tint = Color.Unspecified
            )
        }
    }
}


@Composable
fun NewPlaceDialogInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 120.dp)) {
        Text(
            text = stringResource(R.string.mypagescreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row ( modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ){
        Column (modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.newplacedialog_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.newplacedialog_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.newplacedialog_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(2f),
        ) {
            val instructionPictures = when {
                isEnglish && isSystemInDarkTheme() -> R.drawable.newplacedialog_dark_en
                isNorwegian && isSystemInDarkTheme() -> R.drawable.newplacedialog_dark_no
                isEnglish && !isSystemInDarkTheme() -> R.drawable.newplacedialog_light_en
                isNorwegian && !isSystemInDarkTheme() -> R.drawable.newplacedialog_light_no
                else -> R.drawable.image_not_found
            }
            Icon(
                painterResource(id = instructionPictures),
                contentDescription = "NewPlaceDialog instructions",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun LastScreenInstructions() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.lastdialog_instructions_skumring),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 35.dp, bottom = 35.dp)
        )
        Icon( //TODO change this
            painterResource(id = R.drawable.icon),
            contentDescription = "HomeScreenInstructions",
            tint = Color.Unspecified,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(30.dp))
                .size(180.dp)

        )
    }
}



@Preview
@Composable
fun UserInstructionsDialogTest(
) {
    SkumringTheme(useDarkTheme = true) {
        Surface {
            val closeDialog: () -> Unit = {}
            UserInstructionsDialog(closeDialog)
        }
    }
}