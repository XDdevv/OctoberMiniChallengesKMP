package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle

sealed interface SkeletonPuzzleAction {
    data class OnSkeletonPositionInitialized(
        val puzzleId: Int,
        val positionInRoot: Offset,
        val rect: Rect
    ) : SkeletonPuzzleAction

    data class OnPuzzleInitialized(
        val puzzle: Puzzle,
        val positionInRoot: Offset,
        val size: IntSize
    ) : SkeletonPuzzleAction

    data class OnDragStart(val puzzle: Puzzle) : SkeletonPuzzleAction

    data class OnDrag(
        val puzzle: Puzzle,
        val dragAmount: Offset
    ) : SkeletonPuzzleAction

    data class OnDragEnd(val puzzle: Puzzle) : SkeletonPuzzleAction
}