package rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.arm_left_slot
import octoberminichallengeskmp.sharedui.generated.resources.arm_right_slot
import octoberminichallengeskmp.sharedui.generated.resources.head_slot
import octoberminichallengeskmp.sharedui.generated.resources.leg_left_slot
import octoberminichallengeskmp.sharedui.generated.resources.leg_right_slot
import octoberminichallengeskmp.sharedui.generated.resources.pelvis_slot
import octoberminichallengeskmp.sharedui.generated.resources.rib_cage_slot
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.SkeletonPosition
import rainxch.zed.cctoberminichallengeskmp.skeleton_puzzle.presentation.model.Puzzle

class SkeletonPuzzleViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SkeletonPuzzleState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SkeletonPuzzleState()
        )

    private fun loadInitialData() {
        viewModelScope.launch {
            val puzzles = listOf(
                Puzzle(
                    id = SKULL_ID,
                    puzzleRes = Res.drawable.head_slot,
                ),
                Puzzle(
                    id = LEFT_ARM_ID,
                    puzzleRes = Res.drawable.arm_left_slot,
                ),
                Puzzle(
                    id = RIGHT_ARM_ID,
                    puzzleRes = Res.drawable.arm_right_slot,
                ),
                Puzzle(
                    id = RIBCAGE_ID,
                    puzzleRes = Res.drawable.rib_cage_slot,
                ),
                Puzzle(
                    id = PELVIS_ID,
                    puzzleRes = Res.drawable.pelvis_slot,
                ),
                Puzzle(
                    id = LEFT_LEG_ID,
                    puzzleRes = Res.drawable.leg_left_slot,
                ),
                Puzzle(
                    id = RIGHT_LEG_ID,
                    puzzleRes = Res.drawable.leg_right_slot,
                )
            )

            val skeletonPositions = listOf(
                SkeletonPosition(
                    puzzleId = SKULL_ID,
                    positionRes = Res.drawable.head_slot
                ),
                SkeletonPosition(
                    puzzleId = LEFT_ARM_ID,
                    positionRes = Res.drawable.arm_left_slot
                ),
                SkeletonPosition(
                    puzzleId = RIGHT_ARM_ID,
                    positionRes = Res.drawable.arm_right_slot
                ),
                SkeletonPosition(
                    puzzleId = RIBCAGE_ID,
                    positionRes = Res.drawable.rib_cage_slot
                ),
                SkeletonPosition(
                    puzzleId = PELVIS_ID,
                    positionRes = Res.drawable.pelvis_slot
                ),
                SkeletonPosition(
                    puzzleId = LEFT_LEG_ID,
                    positionRes = Res.drawable.leg_left_slot
                ),
                SkeletonPosition(
                    puzzleId = RIGHT_LEG_ID,
                    positionRes = Res.drawable.leg_right_slot
                )
            )

            _state.update {
                it.copy(
                    puzzles = puzzles,
                    positions = skeletonPositions,
                )
            }
        }
    }

    private val _events = Channel<SkeletonPuzzleEvents>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SkeletonPuzzleAction) {
        when (action) {
            is SkeletonPuzzleAction.OnSkeletonPositionInitialized -> {
                _state.update { currentState ->
                    currentState.copy(
                        positions = currentState.positions.map { position ->
                            if (position.puzzleId == action.puzzleId) {
                                position.copy(
                                    position = action.positionInRoot,
                                    rect = action.rect
                                )
                            } else position
                        }
                    )
                }
            }

            is SkeletonPuzzleAction.OnPuzzleInitialized -> {
                _state.update { currentState ->
                    currentState.copy(
                        puzzles = currentState.puzzles.map { puzzle ->
                            if (puzzle.id == action.puzzle.id && puzzle.initialPosition == Offset.Zero) {
                                puzzle.copy(
                                    initialPosition = action.positionInRoot,
                                    currentPosition = action.positionInRoot,
                                    size = action.size
                                )
                            } else puzzle
                        }
                    )
                }
            }

            is SkeletonPuzzleAction.OnDragStart -> {
                _state.update { currentState ->
                    currentState.copy(
                        puzzles = currentState.puzzles.map { puzzle ->
                            if (puzzle.id == action.puzzle.id) {
                                puzzle.copy(isDragging = true)
                            } else puzzle
                        }
                    )
                }
            }

            is SkeletonPuzzleAction.OnDrag -> {
                _state.update { currentState ->
                    currentState.copy(
                        puzzles = currentState.puzzles.map { puzzle ->
                            if (puzzle.id == action.puzzle.id) {
                                puzzle.copy(
                                    currentPosition = Offset(
                                        x = puzzle.currentPosition.x + action.dragAmount.x,
                                        y = puzzle.currentPosition.y + action.dragAmount.y
                                    )
                                )
                            } else puzzle
                        }
                    )
                }
            }

            is SkeletonPuzzleAction.OnDragEnd -> {
                val state = _state.value
                val puzzle = state.puzzles.find { it.id == action.puzzle.id } ?: return

                val puzzleCenter = Offset(
                    x = puzzle.currentPosition.x + puzzle.size.width / 2f,
                    y = puzzle.currentPosition.y + puzzle.size.height / 2f
                )

                val matchingPosition = state.positions.find { position ->
                    position.puzzleId == puzzle.id && position.rect.contains(puzzleCenter)
                }

                _state.update { currentState ->
                    val updatedPuzzles = currentState.puzzles.map { p ->
                        if (p.id == puzzle.id) {
                            if (matchingPosition != null) {
                                // Correct placement
                                p.copy(
                                    isDragging = false,
                                    isPlaced = true,
                                    showError = false
                                )
                            } else {
                                // Wrong placement - show error
                                p.copy(
                                    currentPosition = p.initialPosition,
                                    isDragging = false,
                                    showError = true
                                )
                            }
                        } else p
                    }

                    // Check if game is finished AFTER updating
                    val isGameFinished = updatedPuzzles.all { it.isPlaced }

                    currentState.copy(
                        hoveredSlotId = null,
                        puzzles = updatedPuzzles,
                        isGameFinished = isGameFinished
                    ).also {
                        if(it.isGameFinished) {
                            viewModelScope.launch {
                                _events.send(SkeletonPuzzleEvents.OnMessage("He's back... and he remembers everything!"))
                            }
                        }
                    }
                }

                // Clear error after 1 second
                if (matchingPosition == null) {
                    viewModelScope.launch {
                        delay(1000)
                        _state.update { currentState ->
                            currentState.copy(
                                puzzles = currentState.puzzles.map { p ->
                                    if (p.id == puzzle.id) p.copy(showError = false) else p
                                }
                            )
                        }
                    }
                }
            }

            is SkeletonPuzzleAction.OnHoverSlot -> {
                _state.update { it.copy(hoveredSlotId = action.slotId) }
            }
        }
    }

}

const val SKULL_ID = 1
const val LEFT_ARM_ID = 2
const val RIGHT_ARM_ID = 3
const val RIBCAGE_ID = 4
const val PELVIS_ID = 5
const val LEFT_LEG_ID = 6
const val RIGHT_LEG_ID = 7
const val SPINE_ID = 8