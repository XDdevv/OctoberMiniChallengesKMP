package rainxch.zed.cctoberminichallengeskmp

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation.HaunterThemeRoot
import rainxch.zed.cctoberminichallengeskmp.theme.AppTheme

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    HaunterThemeRoot()
}
