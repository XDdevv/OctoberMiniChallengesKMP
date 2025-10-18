package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
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
    modifier: Modifier = Modifier,
) {
    if (position == null) return
    val isGameFinished = LocalGameFinished.current

    Image(
        painter = painterResource(position.positionRes),
        contentDescription = null,
        colorFilter = ColorFilter.tint(
            if (isPlaced) {
                SkeletonPuzzleColors.boneColor
            } else {
                SkeletonPuzzleColors.slot
            }
        ),
        modifier = modifier
            .then(
                if (!isGameFinished) {
                    Modifier.border(
                        2.dp, SkeletonPuzzleColors.outlineActive
                    )
                } else Modifier
            )
            .then(
                if (!isGameFinished) {
                    Modifier.background(
                        if (isPlaced) {
                            SkeletonPuzzleColors.surfaceHigher
                        } else SkeletonPuzzleColors.bg
                    )
                } else Modifier
            )
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
            },
    )
}