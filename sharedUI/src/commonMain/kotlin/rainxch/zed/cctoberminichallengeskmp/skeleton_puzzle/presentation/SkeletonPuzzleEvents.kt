package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

sealed interface SkeletonPuzzleEvents {
    data class OnMessage(val message: String) : SkeletonPuzzleEvents
}