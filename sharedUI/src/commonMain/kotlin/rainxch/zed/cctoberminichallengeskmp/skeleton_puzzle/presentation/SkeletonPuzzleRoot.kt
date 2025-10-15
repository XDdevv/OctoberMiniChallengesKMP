package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SkeletonPuzzleRoot(
    viewModel: SkeletonPuzzleViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SkeletonPuzzleScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SkeletonPuzzleScreen(
    state: SkeletonPuzzleState,
    onAction: (SkeletonPuzzleAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    SkeletonPuzzleScreen(
        state = SkeletonPuzzleState(),
        onAction = {}
    )
}