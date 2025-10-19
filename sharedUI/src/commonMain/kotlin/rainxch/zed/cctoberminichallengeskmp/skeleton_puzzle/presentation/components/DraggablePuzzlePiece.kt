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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.SkeletonPuzzleAction
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors

@Composable
fun DraggablePuzzlePiece(
    puzzle: Puzzle,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .graphicsLayer { rotationZ = -8f }
            .border(2.dp, SkeletonPuzzleColors.outlineActive)
            .background(SkeletonPuzzleColors.bg)
            .padding(4.dp)
            .pointerInput(puzzle.id) {
                detectDragGestures(
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
        Image(
            painter = painterResource(puzzle.puzzleRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}