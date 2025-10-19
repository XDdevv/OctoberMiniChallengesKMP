package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors

@Composable
fun ErrorPuzzlePiece(
    puzzle: Puzzle,
    modifier: Modifier = Modifier
) {
    // Shake animation for error state
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shake by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = shake
                rotationZ = -8f
            }
            .border(2.dp, SkeletonPuzzleColors.outlineError) // RED border for error
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