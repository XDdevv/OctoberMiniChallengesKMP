package rainxch.zed.cctoberminichallengeskmp.cursed_countdown.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCountdown(
    targetDate: LocalDateTime,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    var timeRemaining by remember { mutableStateOf(calculateTimeRemaining(targetDate)) }

    LaunchedEffect(targetDate) {
        while (true) {
            timeRemaining = calculateTimeRemaining(targetDate)
            if (timeRemaining.isExpired) break
            delay(1000L)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountdownUnit(timeRemaining.days, "D", style)
        Text("  ", style = style)
        CountdownUnit(timeRemaining.hours, "", style)
        Text(" : ", style = style)
        CountdownUnit(timeRemaining.minutes, "", style)
        Text(" : ", style = style)
        CountdownUnit(timeRemaining.seconds, "", style)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CountdownUnit(
    value: Int,
    label: String,
    style: TextStyle,
) {
    val valueString = value.toString().padStart(2, '0')

    var oldValue by remember { mutableStateOf(value) }

    SideEffect {
        oldValue = value
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in valueString.indices) {
            val oldString = oldValue.toString().padStart(2, '0')
            val oldChar = oldString.getOrNull(i)
            val newChar = valueString[i]

            AnimatedContent(
                targetState = newChar,
                transitionSpec = {
                    slideInVertically { height -> height } with
                            slideOutVertically { height -> -height }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }

        Text(
            text = label,
            style = style,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

data class TimeRemaining(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val isExpired: Boolean = false,
)

@OptIn(ExperimentalTime::class)
private fun calculateTimeRemaining(targetDate: LocalDateTime): TimeRemaining {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val target = targetDate.toInstant(TimeZone.currentSystemDefault())
    val currentInstant = now.toInstant(TimeZone.currentSystemDefault())

    val duration = target - currentInstant

    if (duration.isNegative() || duration == Duration.ZERO) {
        return TimeRemaining(0, 0, 0, 0, isExpired = true)
    }

    val days = duration.inWholeDays.toInt()
    val hours = (duration.inWholeHours % 24).toInt()
    val minutes = (duration.inWholeMinutes % 60).toInt()
    val seconds = (duration.inWholeSeconds % 60).toInt()

    return TimeRemaining(days, hours, minutes, seconds)
}