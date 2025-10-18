package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.bg
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components.PuzzleSlot
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.components.SkeletonPositionItem
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.locals.LocalGameFinished
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors
import rainxch.zed.cctoberminichallengeskmp.theme.appTypography
import rainxch.zed.cctoberminichallengeskmp.utils.ObserveAsEvents

@Composable
fun SkeletonPuzzleRoot(
    viewModel: SkeletonPuzzleViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SkeletonPuzzleEvents.OnMessage -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Box(
                        modifier = Modifier
                            .background(SkeletonPuzzleColors.toastBg)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = data.visuals.message,
                            style = appTypography().displayMedium.copy(
                                color = SkeletonPuzzleColors.bg,
                                fontSize = 22.sp
                            )
                        )
                    }
                }
            )
        },
        containerColor = SkeletonPuzzleColors.bg
    ) { innerPadding ->
        SkeletonPuzzleScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
fun SkeletonPuzzleScreen(
    state: SkeletonPuzzleState,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val skullRotation = remember { Animatable(0f) }
    val leftArmRotation = remember { Animatable(0f) }
    val rightArmRotation = remember { Animatable(0f) }

    LaunchedEffect(state.isGameFinished) {
        if (state.isGameFinished) {
            launch { skullRotation.animateTo(10f, spring(Spring.DampingRatioMediumBouncy)) }
            launch { leftArmRotation.animateTo(8f, spring(Spring.DampingRatioMediumBouncy)) }
            launch { rightArmRotation.animateTo(-8f, spring(Spring.DampingRatioMediumBouncy)) }

            delay(1000)

            launch { skullRotation.animateTo(0f, spring(Spring.DampingRatioHighBouncy)) }
            launch { leftArmRotation.animateTo(0f, spring(Spring.DampingRatioHighBouncy)) }
            launch { rightArmRotation.animateTo(0f, spring(Spring.DampingRatioHighBouncy)) }
        } else {
            launch { skullRotation.animateTo(0f) }
            launch { leftArmRotation.animateTo(0f) }
            launch { rightArmRotation.animateTo(0f) }
        }
    }

    CompositionLocalProvider(
        LocalGameFinished provides state.isGameFinished
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            ) {
                val screenWidth = maxWidth
                val screenHeight = maxHeight

                val columns = 4f
                val spacingPercent = 0.015f
                val spacing = screenWidth * spacingPercent

                val totalSpacing = spacing * (columns - 1)
                val cellWidth = (screenWidth - totalSpacing) / columns
                val cellHeight = cellWidth * 1.3f

                fun getX(column: Float): Dp = cellWidth * column + spacing * column
                fun getY(row: Float): Dp = cellHeight * row + spacing * row

                val skeletonHeight = cellHeight * 3.5f + spacing * 2.5f
                val topOffset =
                    (screenHeight - skeletonHeight - cellHeight * 2.5f - spacing * 4) / 3

                Box(modifier = Modifier.fillMaxSize()) {

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == SKULL_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == SKULL_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(1.5f) - cellWidth * 0.25f, y = topOffset + getY(0f))
                            .size(cellWidth * 1.5f, cellHeight * 0.9f)
                            .graphicsLayer {
                                rotationZ = skullRotation.value
                            }
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == LEFT_ARM_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == LEFT_ARM_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(.6f), y = topOffset + getY(0.6f))
                            .size(cellWidth * 0.7f, cellHeight * 2f + spacing)
                            .graphicsLayer {
                                rotationZ = leftArmRotation.value
                            }
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == RIGHT_ARM_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == RIGHT_ARM_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(2.4f) + cellWidth * 0.3f, y = topOffset + getY(0.6f))
                            .size(cellWidth * 0.7f, cellHeight * 2f + spacing)
                            .graphicsLayer {
                                rotationZ = rightArmRotation.value
                            }
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == RIBCAGE_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == RIBCAGE_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(1.4f) - cellWidth * 0.15f, y = topOffset + getY(.9f))
                            .size(cellWidth * 1.5f, cellHeight * 1f)
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == PELVIS_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == PELVIS_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(1.4f) - cellWidth * 0.15f, y = topOffset + getY(1.85f))
                            .size(cellWidth * 1.5f, cellHeight * 0.75f)
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == LEFT_LEG_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == LEFT_LEG_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(1.2f), y = topOffset + getY(2.55f))
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    SkeletonPositionItem(
                        position = state.positions.find { it.puzzleId == RIGHT_LEG_ID },
                        onAction = onAction,
                        isPlaced = state.puzzles.find { it.id == RIGHT_LEG_ID }?.isPlaced == true,
                        modifier = Modifier
                            .offset(x = getX(2.1f), y = topOffset + getY(2.55f))
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    val puzzleAreaTop = screenHeight - cellHeight * 2.5f - spacing * 2

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == LEFT_ARM_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(0f), y = puzzleAreaTop)
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == LEFT_LEG_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(0.8f), y = puzzleAreaTop)
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == RIBCAGE_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(1.7f), y = puzzleAreaTop)
                            .size(cellWidth * 1.1f, cellHeight * 0.8f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == SPINE_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(1.8f), y = puzzleAreaTop + cellHeight * 0.9f)
                            .size(cellWidth * 0.9f, cellHeight * 1.5f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == RIGHT_ARM_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(2.9f), y = puzzleAreaTop)
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == RIGHT_LEG_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(1.8f), y = puzzleAreaTop + cellHeight * .8f + spacing)
                            .size(cellWidth * 0.7f, cellHeight * 1.8f)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == SKULL_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(0f), y = puzzleAreaTop + cellHeight * 1.8f + spacing)
                            .size(cellWidth * 1.6f, cellHeight)
                    )

                    PuzzleSlot(
                        puzzle = state.puzzles.find { it.id == PELVIS_ID },
                        onAction = onAction,
                        modifier = Modifier
                            .offset(x = getX(2.8f), y = puzzleAreaTop + cellHeight * 1.8f + spacing)
                            .size(cellWidth * 1.2f, cellHeight * 0.7f)
                    )
                }
            }

            val density = LocalDensity.current
            state.puzzles.filter { it.isDragging && !it.isPlaced }.forEach { puzzle ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = with(density) { puzzle.currentPosition.x.toDp() },
                            y = with(density) { puzzle.currentPosition.y.toDp() }
                        )
                        .size(
                            width = with(density) { puzzle.size.width.toDp() },
                            height = with(density) { puzzle.size.height.toDp() }
                        )
                        .rotate(-8f)
                        .border(2.dp, SkeletonPuzzleColors.outlineError)
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
        }
    }
}



@Preview
@Composable
private fun Preview() {
    SkeletonPuzzleScreen(
        state = SkeletonPuzzleState(),
        onAction = {}
    )
}