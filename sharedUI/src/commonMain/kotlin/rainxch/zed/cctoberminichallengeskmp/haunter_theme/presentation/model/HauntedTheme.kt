package rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation.model

enum class HauntedTheme {
    DARK, LIGHT;

    fun nextTheme(): HauntedTheme {
        return when (this) {
            DARK -> LIGHT
            LIGHT -> DARK
        }
    }
}