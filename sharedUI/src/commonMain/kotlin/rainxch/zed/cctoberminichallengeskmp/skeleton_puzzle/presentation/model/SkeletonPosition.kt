package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.jetbrains.compose.resources.DrawableResource

data class SkeletonPosition(
    val puzzleId: Int,
    val position: Offset = Offset(0f, 0f),
    val positionRes: DrawableResource,
    val rect: Rect = Rect.Zero,
)