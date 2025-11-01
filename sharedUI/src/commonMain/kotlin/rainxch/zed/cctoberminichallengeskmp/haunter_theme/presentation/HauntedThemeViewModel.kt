package rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation.model.HauntedTheme

class HauntedThemeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HauntedThemeState())
    val state = _state.asStateFlow()

    fun onAction(action: HauntedThemeAction) {
        when (action) {
            HauntedThemeAction.OnThemeToggled -> {
                _state.update { state ->
                    state.copy(
                        currentTheme = state.currentTheme.nextTheme()
                    )
                }
            }
        }
    }

}