package rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.theme.AppTheme

@Composable
fun HaunterThemeRoot(
    viewModel: HauntedThemeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HaunterThemeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HaunterThemeScreen(
    state: HauntedThemeState,
    onAction: (HauntedThemeAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    AppTheme(onThemeChanged = { }) {
        HaunterThemeScreen(
            state = HauntedThemeState(),
            onAction = {}
        )
    }
}