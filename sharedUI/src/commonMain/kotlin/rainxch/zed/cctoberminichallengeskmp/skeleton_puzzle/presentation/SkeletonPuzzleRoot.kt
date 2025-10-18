package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.bg
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.SkeletonPosition
import rainxch.zed.cctoberminichallengeskmp.theme.SkeletonPuzzleColors
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
            SnackbarHost(snackbarHostState)
        },
        containerColor = SkeletonPuzzleColors.bg
    ) { innerPadding ->
        SkeletonPuzzleScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SkeletonPuzzleScreen(
    state: SkeletonPuzzleState,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(Res.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Use BoxWithConstraints for responsive layout
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            // Calculate grid parameters
            val columns = 4f
            val spacingPercent = 0.015f  // Reduced from 0.025f to 0.015f
            val spacing = screenWidth * spacingPercent

            val totalSpacing = spacing * (columns - 1)
            val cellWidth = (screenWidth - totalSpacing) / columns
            val cellHeight = cellWidth * 1.3f  // Reduced from 1.4f to 1.3f

            // Helper functions for positioning (return Dp)
            fun getX(column: Float): Dp = cellWidth * column + spacing * column
            fun getY(row: Float): Dp = cellHeight * row + spacing * row

            // Calculate vertical offset to center skeleton area
            val skeletonHeight = cellHeight * 3.5f + spacing * 2.5f
            val topOffset = (screenHeight - skeletonHeight - cellHeight * 2.5f - spacing * 4) / 3

            Box(modifier = Modifier.fillMaxSize()) {

                // ============ SKELETON POSITIONS (Top Area) ============

                // Row 0 - Top
                // Skull (centered, column 1.5, spans wider)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == SKULL_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.5f) - cellWidth * 0.25f, y = topOffset + getY(0f))
                        .size(cellWidth * 1.5f, cellHeight * 0.9f)
                )

                // Left Arm (column 0, spans 2 rows)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == LEFT_ARM_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(0f), y = topOffset + getY(0.9f))
                        .size(cellWidth * 0.7f, cellHeight * 2f + spacing)
                )

                // Right Arm (column 3, spans 2 rows)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == RIGHT_ARM_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(3f) + cellWidth * 0.3f, y = topOffset + getY(0.9f))
                        .size(cellWidth * 0.7f, cellHeight * 2f + spacing)
                )

                // Ribcage (column 1.5, centered under skull)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == RIBCAGE_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.5f) - cellWidth * 0.15f, y = topOffset + getY(1f))
                        .size(cellWidth * 1.3f, cellHeight * 0.8f)
                )

                // Pelvis/Hip (column 1.5, centered)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == PELVIS_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.5f) - cellWidth * 0.15f, y = topOffset + getY(1.9f))
                        .size(cellWidth * 1.3f, cellHeight * 0.7f)
                )

                // Left Leg (column 1, spans 2 rows)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == LEFT_LEG_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.2f), y = topOffset + getY(2.7f))
                        .size(cellWidth * 0.7f, cellHeight * 1.8f)
                )

                // Right Leg (column 2, spans 2 rows)
                SkeletonPosition(
                    position = state.positions.find { it.puzzleId == RIGHT_LEG_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(2.1f), y = topOffset + getY(2.7f))
                        .size(cellWidth * 0.7f, cellHeight * 1.8f)
                )

                // ============ PUZZLE PIECES (Bottom Area) ============
                val puzzleAreaTop = screenHeight - cellHeight * 2.5f - spacing * 2

                // Bottom row with irregular grid
                // Left Arm piece (column 0)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == LEFT_ARM_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(0f), y = puzzleAreaTop)
                        .size(cellWidth * 0.7f, cellHeight * 1.8f)
                )

                // Left Leg piece (column 0.8)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == LEFT_LEG_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(0.8f), y = puzzleAreaTop)
                        .size(cellWidth * 0.7f, cellHeight * 1.8f)
                )

                // Ribcage piece (column 1.6, top position)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == RIBCAGE_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.7f), y = puzzleAreaTop)
                        .size(cellWidth * 1.1f, cellHeight * 0.8f)
                )

                // Spine piece (column 1.7, bottom position)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == SPINE_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(1.8f), y = puzzleAreaTop + cellHeight * 0.9f)
                        .size(cellWidth * 0.9f, cellHeight * 1.5f)
                )

                // Right Arm piece (column 2.9)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == RIGHT_ARM_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(2.9f), y = puzzleAreaTop)
                        .size(cellWidth * 0.7f, cellHeight * 1.8f)
                )

                // Skull piece (bottom left, large)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == SKULL_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(0f), y = puzzleAreaTop + cellHeight * 1.9f + spacing)
                        .size(cellWidth * 1.6f, cellHeight)
                )

                // Pelvis/Mask piece (bottom right)
                PuzzleSlot(
                    puzzle = state.puzzles.find { it.id == PELVIS_ID },
                    onAction = onAction,
                    modifier = Modifier
                        .offset(x = getX(2.8f), y = puzzleAreaTop + cellHeight * 1.9f + spacing)
                        .size(cellWidth * 1.2f, cellHeight * 0.7f)
                )
            }
        }

        // Draggable overlay
        val density = LocalDensity.current
        state.puzzles.filter { it.isDragging || it.isPlaced }.forEach { puzzle ->
            Image(
                painter = painterResource(puzzle.puzzleRes),
                contentDescription = null,
                modifier = Modifier
                    .offset(
                        x = with(density) { puzzle.currentPosition.x.toDp() },
                        y = with(density) { puzzle.currentPosition.y.toDp() }
                    )
                    .size(with(density) { puzzle.size.width.toDp() })
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
                    }
            )
        }
    }
}

@Composable
fun SkeletonPosition(
    position: SkeletonPosition?,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (position == null) return

    Image(
        painter = painterResource(position.positionRes),
        contentDescription = null,
        modifier = modifier
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

@Composable
fun PuzzleSlot(
    puzzle: Puzzle?,
    onAction: (SkeletonPuzzleAction) -> Unit,
    modifier: Modifier = Modifier
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
            }
    ) {
        if (!puzzle.isDragging && !puzzle.isPlaced) {
            Image(
                painter = painterResource(puzzle.puzzleRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
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