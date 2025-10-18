package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.SkeletonPuzzleAction
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors

@Composable
fun PuzzleSlot(
    puzzle: Puzzle?,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (puzzle == null) return

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                if (puzzle.initialPosition == Offset.Zero) {
                    val positionInRoot = coordinates.positionInRoot()
                    val size = coordinates.size

                    onAction(
                        SkeletonPuzzleAction.OnPuzzleInitialized(
                            puzzle = puzzle,
                            positionInRoot = positionInRoot,
                            size = size
                        )
                    )
                }
            }
            .pointerInput(puzzle.id) {
                detectDragGestures(
                    onDragStart = {
                        onAction(SkeletonPuzzleAction.OnDragStart(puzzle))
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onAction(
                            SkeletonPuzzleAction.OnDrag(
                                puzzle = puzzle,
                                dragAmount = dragAmount
                            )
                        )
                    },
                    onDragEnd = {
                        onAction(SkeletonPuzzleAction.OnDragEnd(puzzle))
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (!puzzle.isDragging && !puzzle.isPlaced) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, SkeletonPuzzleColors.outlineActive)
                    .background(SkeletonPuzzleColors.bg)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(puzzle.puzzleRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}