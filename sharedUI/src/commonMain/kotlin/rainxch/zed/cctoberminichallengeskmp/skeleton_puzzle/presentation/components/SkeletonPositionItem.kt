package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.SkeletonPuzzleAction
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.locals.LocalGameFinished
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.SkeletonPosition
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors

@Composable
fun SkeletonPositionItem(
    position: SkeletonPosition?,
    onAction: (SkeletonPuzzleAction) -> Unit,
    isPlaced: Boolean = false,
    isHovered: Boolean = false, // NEW
    modifier: Modifier = Modifier,
) {
    val isGameFinished = LocalGameFinished.current
    if (position == null) return

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isGameFinished -> Color.Transparent
            isPlaced -> SkeletonPuzzleColors.bg // Darker green when placed
            isHovered -> SkeletonPuzzleColors.slotActive // Lighter when hovered
            else -> SkeletonPuzzleColors.surfaceHigher // Normal bg
        },
        label = "bgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isGameFinished -> Color.Transparent
            isHovered -> SkeletonPuzzleColors.outlineActive // Brighter border when hovered
            else -> SkeletonPuzzleColors.outlineInactive // Normal border
        },
        label = "borderColor"
    )

    Image(
        painter = painterResource(position.positionRes),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            if (isPlaced) {
                SkeletonPuzzleColors.boneColor
            } else SkeletonPuzzleColors.slot
        ),
        modifier = modifier
            .border(2.dp, borderColor)
            .background(backgroundColor)
            .onGloballyPositioned { coordinates ->
                val positionInRoot = coordinates.positionInRoot()
                val size = coordinates.size

                val rect = Rect(
                    left = positionInRoot.x,
                    top = positionInRoot.y,
                    right = positionInRoot.x + size.width,
                    bottom = positionInRoot.y + size.height
                )

                onAction(
                    SkeletonPuzzleAction.OnSkeletonPositionInitialized(
                        puzzleId = position.puzzleId,
                        positionInRoot = positionInRoot,
                        rect = rect
                    )
                )
            }
    )
}