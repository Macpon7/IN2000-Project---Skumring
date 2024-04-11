package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.ScopeType
import com.google.relay.compose.relayDropShadow
import com.google.relay.compose.tappable
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.homescreeninfoscreen.inter
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.sunSetColor
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.sunSetColor2

/**
 * This composable was generated from the UI Package 'home_screen_info_screen'.
 * Generated code; do not edit directly
 */
@Composable
fun SunsetInfoScreen(
    modifier: Modifier = Modifier,
    sunsetTodayText: String = "",
    conditionsText: String = "",
    sunsetTimeText: String = "",
    goldenHourText: String = "",
    goldenHourTimeText: String = "",
    blueHourText: String = "",
    blueHourTimeText: String = "",
    degreesText: String = "",
    weatherConditionsText: String = "",
    moreDetailsText: String = "",
    onClick: () -> Unit = {}
) {
    TopLevel(modifier = modifier) {
        Box(
            modifier = Modifier.boxAlign( alignment = Alignment.TopCenter,
            offset = DpOffset(
                x = 0.dp,
                 y = 11.0.dp
            )
            )
        ) {
            SunsetToday(
                sunsetTodayText = sunsetTodayText,
                )
        }
        Box(
            modifier = Modifier.boxAlign( alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = 0.dp,
                    y = 50.dp
                )
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = null, // Provide appropriate content description
                modifier = Modifier.size(120.dp) // Adjust size as needed
                   // .background(brush = Brush.verticalGradient(
                    //colors = listOf(
                    //    sunSetColor,
                    //    sunSetColor2
                   // )
                   // )
                    , tint = sunSetColor2
            )
              //  tint = androidx.compose.material.MaterialTheme.colors.sunSetColor


        }
        SunsetConditions(
            conditionsText = conditionsText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 220.0.dp
                )
            )
        )
        Class2005(
            sunsetTimeText = sunsetTimeText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 163.0.dp
                )
            )
        )
        GoldenHour(
            goldenHourText = goldenHourText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 33.0.dp,
                    y = 391.0.dp
                )
            )
        )
        Class20312105(
            goldenHourTimeText = goldenHourTimeText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 51.0.dp,
                    y = 413.0.dp
                )
            )
        )
        BlueHour(
            blueHourText = blueHourText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 214.0.dp,
                    y = 391.0.dp
                )
            )
        )
        Class19092031(
            blueHourTimeText = blueHourTimeText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 225.0.dp,
                    y = 413.0.dp
                )
            )
        )
        Class18C(
            degreesText = degreesText,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = -1.0.dp,
                    y = 336.0.dp
                )
            )
        )
        Component58(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopCenter,
                offset = DpOffset(
                    x = -5.5.dp,
                    y = 367.0.dp
                )
            )
        ) {
            WeatherConditionsPoor(
                weatherConditionsText = weatherConditionsText,
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
        MoreDetailsButton(
            onClick = onClick,
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 0.0.dp,
                    y = 446.0.dp
                )
            )
        ) {
            MoreDetails(
                moreDetailsText = moreDetailsText,
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopCenter,
                    offset = DpOffset(
                        x = 0.5.dp,
                        y = 6.0.dp
                    )
                )
            )
        }
    }
}

@Preview(widthDp = 324, heightDp = 478)
@Composable
private fun SunsetInfoScreenPreview() {
    MaterialTheme {
        RelayContainer {
            SunsetInfoScreen(
                sunsetTodayText = "Sunset today",
                conditionsText = "Sunset conditions",
                sunsetTimeText = "20:05",
                goldenHourText = "Golden Hour",
                goldenHourTimeText = "20:31-21:05",
                blueHourText = "Blue Hour",
                blueHourTimeText = "19:09-20:31",
                degreesText = "18C",
                onClick = {},
                weatherConditionsText = "Weather conditions: poor",
                moreDetailsText = "More details",
                modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)
            )
        }
    }
}

@Composable
fun SunsetToday(
    sunsetTodayText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = sunsetTodayText,
        fontSize = 22.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272727272727.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun SunsetConditions(
    conditionsText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = conditionsText,
        fontSize = 18.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272245619032.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun Class2005(
    sunsetTimeText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = sunsetTimeText,
        fontSize = 25.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.210227279663086.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun GoldenHour(
    goldenHourText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = goldenHourText,
        fontSize = 15.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun Class20312105(
    goldenHourTimeText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = goldenHourTimeText,
        fontSize = 12.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun BlueHour(
    blueHourText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = blueHourText,
        fontSize = 15.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272033691406.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun Class19092031(
    blueHourTimeText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = blueHourTimeText,
        fontSize = 12.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier
    )
}

@Composable
fun Class18C(
    degreesText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = degreesText,
        fontSize = 21.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272396995908.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun WeatherConditionsPoor(
    weatherConditionsText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = weatherConditionsText,
        fontSize = 12.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        height = 1.2102272510528564.em,
        fontWeight = FontWeight(700.0.toInt()),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 0.0.dp,
                top = 3.0.dp,
                end = 0.0.dp,
                bottom = 0.18181800842285156.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun Component58(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.requiredWidth(105.0.dp).requiredHeight(18.18181800842285.dp)
    )
}

@Composable
fun MoreDetails(
    moreDetailsText: String,
    modifier: Modifier = Modifier
) {
    RelayText(
        content = moreDetailsText,
        fontSize = 16.0.sp,
        fontFamily = inter,
        color = Color(
            alpha = 255,
            red = 30,
            green = 30,
            blue = 30
        ),
        height = 1.2102272510528564.em,
        textAlign = TextAlign.Left,
        modifier = modifier
    )
}

@Composable
fun MoreDetailsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 148,
            green = 117,
            blue = 207
        ),
        isStructured = false,
        content = content,
        modifier = modifier.tappable(onTap = onClick).requiredWidth(333.0.dp).requiredHeight(32.0.dp)
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        radius = 10.0,
        content = content,
        modifier = modifier.background(Color.Transparent).drawWithContent(
            onDraw = {
                drawRect(
                    brush = Brush.linearGradient(
                        0.08f to Color(
                            alpha = 204,
                            red = 27,
                            green = 27,
                            blue = 31
                        ),
                        0.44f to Color(
                            alpha = 204,
                            red = 148,
                            green = 104,
                            blue = 175
                        ),
                        Float.POSITIVE_INFINITY to Color(
                            alpha = 204,
                            red = 96,
                            green = 84,
                            blue = 233
                        ),
                        start = Offset(
                            0.5f,
                            0.0f
                        ),
                        end = Offset(
                            0.5f,
                            Float.POSITIVE_INFINITY
                        )
                    )
                )
                drawContent()
            }
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f).relayDropShadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            borderRadius = 10.0.dp,
            blur = 4.0.dp,
            offsetX = 0.0.dp,
            offsetY = 4.0.dp,
            spread = 0.0.dp
        )
    )
}