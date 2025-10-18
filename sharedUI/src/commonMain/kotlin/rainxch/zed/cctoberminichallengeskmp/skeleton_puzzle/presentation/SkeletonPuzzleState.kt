package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.SkeletonPosition
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle

data class SkeletonPuzzleState(
    val puzzles: List<Puzzle> = emptyList(),
    val positions: List<SkeletonPosition> = emptyList()
)