package rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation

sealed interface HauntedThemeAction {
    data object OnThemeToggled : HauntedThemeAction
}