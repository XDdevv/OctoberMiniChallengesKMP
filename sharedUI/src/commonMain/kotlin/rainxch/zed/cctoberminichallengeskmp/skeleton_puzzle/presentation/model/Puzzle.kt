package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.DrawableResource

data class Puzzle(
    val id: Int,
    val puzzleRes: DrawableResource,
    val initialPosition: Offset = Offset.Zero,
    val currentPosition: Offset = Offset.Zero,
    val size: IntSize = IntSize.Zero,
    val isDragging: Boolean = false,
    val isPlaced: Boolean = false,
    val showError: Boolean = false
)
