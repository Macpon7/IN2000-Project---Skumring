@file:Suppress("DEPRECATION")

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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

/**
 * A dialog that shows the user the user how to use the app the first time the user
 * installs it
 */
@Composable
fun UserInstructionsDialog(
    closeDialog: () -> Unit, finishDialog: () -> Unit
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    val totalScreens = 8

    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ), onDismissRequest = closeDialog
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                //goes through each screen
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
            ) {
                TextButton(
                    onClick = if (currentScreen == 0) {
                        { closeDialog() }
                    } else {
                        { currentScreen-- }
                    },
                    contentPadding = PaddingValues(10.dp),
                ) {
                    Text(
                        text = if (currentScreen == 0) {
                            stringResource(id = R.string.close)
                        } else {
                            stringResource(id = R.string.back_button)
                        },
                        color = MaterialTheme.colorScheme.outlineVariant,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                //button for clicking next
                Button(
                    onClick = {
                        if (currentScreen == 7) {
                            currentScreen = 0
                            finishDialog()
                        } else {
                            currentScreen++
                        }

                    },
                    contentPadding = PaddingValues(
                        top = 8.dp, bottom = 10.dp, start = 20.dp, end = 20.dp
                    ),
                    modifier = Modifier.padding(start = 15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                ) {
                    Text(
                        text = if (currentScreen != 7) {
                            stringResource(id = R.string.UserInstructionsDialog_next_button)
                        } else {
                            stringResource(id = R.string.new_user_dialog_finished)
                        },
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }
        }
    }
}

/**
 * First screen in the dialog
 */
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
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(top = 35.dp, bottom = 35.dp)
        )
        Icon(
            painterResource(id = R.drawable.icon),
            contentDescription = stringResource(R.string.app_icon),
            tint = Color.Unspecified,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(30.dp))
                .size(180.dp)
        )
        Text(
            text = stringResource(R.string.firstscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            text = stringResource(R.string.firstscreen_moreinfo_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 65.dp)
        )
    }
}


/**
 * Description of homescreen
 */

@Composable
fun HomeScreenInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.homescreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 2.dp)
        ) {
            Text(
                text = "1." + stringResource(R.string.homescreen_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.homescreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.homescreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "4." + stringResource(R.string.homescreen_instructions_4),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),

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
                contentDescription = stringResource(id = R.string.homescreen_instructions_screen),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Description of favourite function on homescreen
 */
@Composable
fun HomeScreenInstructionsFavourite() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.homescreen_favourite_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.homescreen_favourite_instruction),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),
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
                contentDescription = stringResource(id = R.string.homescreen_favourite_instructions_picture),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Description of maplistscreen, specifically how the map works
 */
@Composable
fun MapListInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.maplistscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.maplistscreen_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.maplistscreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.maplistscreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),
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
                contentDescription = stringResource(id = R.string.maplistscreen_instructions_picture),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Description of the maplist bottomsheet
 */

@Composable
fun MapListInstructionsPopUp() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.maplistscreen_popup_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.maplist_popup_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.maplist_popup_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.maplist_popup_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),
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
                contentDescription = stringResource(id = R.string.maplistscreen_popup_instructions_picture),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Descrpition of placeinfoscreen
 */


@Composable
fun PlaceInfoInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.placeinfoscreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.placeinfoscreen_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.placeinfoscreen_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.placeinfoscreen_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),
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
                contentDescription = stringResource(id = R.string.placeinfoscreen_instructions_picture),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * description of NewPlaceDialog and Settings
 */

@Composable
fun NewPlaceDialogInstructions() {
    val currentLocale = LocalContext.current.resources.configuration.locale
    val isEnglish = currentLocale.language == "en"
    val isNorwegian = currentLocale.language in setOf("nb", "nn")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
    ) {
        Text(
            text = stringResource(R.string.mypagescreen_instructions_skumring),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 25.dp, start = 10.dp, end = 10.dp, bottom = 15.dp)
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "1." + stringResource(R.string.newplacedialog_instructions_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 35.dp)
            )
            Text(
                text = "2." + stringResource(R.string.newplacedialog_instructions_2),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = "3." + stringResource(R.string.newplacedialog_instructions_3),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Box(
            modifier = Modifier.weight(2f),
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
                contentDescription = stringResource(id = R.string.newplacedialog_instructions_picture),
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Last screen of the slide, ends the slideshow
 */

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
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(top = 35.dp, bottom = 35.dp)
        )
        Icon(
            painterResource(id = R.drawable.icon),
            contentDescription = stringResource(R.string.app_icon),
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
            UserInstructionsDialog(closeDialog = {}, finishDialog = {})
        }
    }
}