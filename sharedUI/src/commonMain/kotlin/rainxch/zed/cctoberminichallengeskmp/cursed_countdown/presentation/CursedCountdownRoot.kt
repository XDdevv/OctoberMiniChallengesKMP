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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.countdown_web
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.theme.CursedCountdownColors

@Composable
fun CursedCountdownRoot(
    viewModel: CursedCountdownViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CursedCountdownScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CursedCountdownScreen(
    state: CursedCountdownState,
    onAction: (CursedCountdownAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    CursedCountdownScreen(
        state = CursedCountdownState(),
        onAction = {}
    )
}