package rainxch.zed.cctoberminichallengeskmp.cursed_countdown.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.countdown_web
import org.jetbrains.compose.resources.painterResource
import rainxch.zed.cctoberminichallengeskmp.cursed_countdown.presentation.components.AnimatedCountdown
import rainxch.zed.cctoberminichallengeskmp.theme.CursedCountdownColors

@Composable
fun CursedCountdownRoot() {
    Scaffold(
        containerColor = CursedCountdownColors.outlineInactive,
        topBar = {
            Image(
                painter = painterResource(Res.drawable.countdown_web),
                contentDescription = null,
                modifier = Modifier.width(240.dp),
                contentScale = ContentScale.Crop
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(CursedCountdownColors.bg)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .background(CursedCountdownColors.toastBg, RoundedCornerShape(24.dp))
                    .padding(vertical = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                val targetDate = LocalDateTime(year = 2025,
                    month = 10,
                    day = 31,
                    hour = 0,
                    minute = 0,
                    second = 0
                )

                AnimatedCountdown(
                    targetDate = targetDate,
                    style = MaterialTheme.typography.displayMedium.copy(color = CursedCountdownColors.bg),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}