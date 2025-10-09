package rainxch.zed.cctoberminichallengeskmp

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.cursed_countdown.presentation.CursedCountdownRoot
import rainxch.zed.cctoberminichallengeskmp.theme.AppTheme

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    CursedCountdownRoot()
}
