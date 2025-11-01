package rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import octoberminichallengeskmp.sharedui.generated.resources.Res
import octoberminichallengeskmp.sharedui.generated.resources.cloud_01
import octoberminichallengeskmp.sharedui.generated.resources.cloud_02
import octoberminichallengeskmp.sharedui.generated.resources.cloud_03
import octoberminichallengeskmp.sharedui.generated.resources.ghost
import octoberminichallengeskmp.sharedui.generated.resources.graveyard
import octoberminichallengeskmp.sharedui.generated.resources.moon
import octoberminichallengeskmp.sharedui.generated.resources.pumpkin
import octoberminichallengeskmp.sharedui.generated.resources.stars
import octoberminichallengeskmp.sharedui.generated.resources.sun
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rainxch.zed.cctoberminichallengeskmp.haunter_theme.presentation.model.HauntedTheme
import rainxch.zed.cctoberminichallengeskmp.theme.AppTheme
import rainxch.zed.cctoberminichallengeskmp.theme.HauntedThemeColors

@Composable
fun HaunterThemeRoot(
    viewModel: HauntedThemeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HaunterThemeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HaunterThemeScreen(
    state: HauntedThemeState,
    onAction: (HauntedThemeAction) -> Unit,
) {
    val background = remember { Animatable(HauntedThemeColors.day) }

    LaunchedEffect(state.currentTheme) {
        when (state.currentTheme) {
            HauntedTheme.DARK -> {
                background.animateTo(
                    HauntedThemeColors.night,
                    animationSpec = tween(1000, easing = LinearEasing)
                )
            }

            HauntedTheme.LIGHT -> {
                background.animateTo(
                    HauntedThemeColors.day,
                    animationSpec = tween(1000, easing = LinearEasing)
                )
            }
        }
    }

    Scaffold(
        containerColor = background.value
    ) {
        Box(Modifier.fillMaxSize()) {
            // Bottom bar
            BottomBar(state.currentTheme)

            // Darkish overlay
            AnimatedVisibility(
                visible = state.currentTheme == HauntedTheme.DARK,
                enter = fadeIn(tween(1000)),
                exit = fadeOut(tween(1000))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }

            // Top bar (Sun / Moon)
            TopBar(state.currentTheme)

            // Switcher
            Switcher(state.currentTheme, onAction)

            // Decoration (Clouds / Stars)
            Decoration(state.currentTheme)

            // Decoration (Clouds / Stars)
            Ghost(state.currentTheme)
        }
    }
}

@Composable
private fun BoxScope.TopBar(theme: HauntedTheme) {
    val window = LocalWindowInfo.current
    val windowWidth = window.containerSize.width

    val sunX = remember { Animatable(windowWidth.toFloat() / 2 - 200f) }
    val sunY = remember { Animatable(0f) }
    val sunRotate = remember { Animatable(0f) }

    val moonX = remember { Animatable(-windowWidth.toFloat() / 2 + 100f) }
    val moonY = remember { Animatable(windowWidth - 200f) }
    val moonRotate = remember { Animatable(-45f) }

    LaunchedEffect(theme) {
        when (theme) {
            HauntedTheme.LIGHT -> {
                launch {
                    sunX.animateTo(
                        windowWidth.toFloat() / 2 - 200f,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch { sunY.animateTo(0f, animationSpec = tween(1000, easing = EaseIn)) }
                launch { sunRotate.animateTo(0f, animationSpec = tween(1000, easing = EaseIn)) }

                launch {
                    moonX.animateTo(
                        -windowWidth.toFloat() / 2 + 100f,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch {
                    moonY.animateTo(
                        windowWidth - 200f,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch { moonRotate.animateTo(-45f, animationSpec = tween(1000, easing = EaseIn)) }
            }

            HauntedTheme.DARK -> {
                launch {
                    moonX.animateTo(
                        windowWidth.toFloat() / 2,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch { moonY.animateTo(0f, animationSpec = tween(1000, easing = EaseIn)) }
                launch { moonRotate.animateTo(0f, animationSpec = tween(1000, easing = EaseIn)) }

                launch {
                    sunX.animateTo(
                        windowWidth.toFloat() + 100f,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch {
                    sunY.animateTo(
                        windowWidth - 200f,
                        animationSpec = tween(1000, easing = EaseIn)
                    )
                }
                launch { sunRotate.animateTo(-45f, animationSpec = tween(1000, easing = EaseIn)) }
            }
        }
    }

    Box(
        Modifier.padding(top = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.moon),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                translationX = moonX.value
                translationY = moonY.value
                rotationZ = moonRotate.value
            }
        )

        Image(
            painter = painterResource(Res.drawable.sun),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                translationX = sunX.value
                translationY = sunY.value
                rotationZ = sunRotate.value
            }
        )
    }
}

@Composable
private fun BoxScope.Decoration(theme: HauntedTheme) {
    val density = LocalDensity.current
    val starsAlpha = remember { Animatable(0f) }
    val cloudsAlpha = remember { Animatable(1f) }
    val window = LocalWindowInfo.current
    val cloud1X = remember {
        Animatable(
            -230f
        )
    }
    val cloud2X = remember {
        Animatable(
            with(density) { 93.dp.toPx() }
        )
    }
    val cloud3X = remember {
        Animatable(
            window.containerSize.width.toFloat() - with(density) { 175.dp.toPx() }
        )
    }

    LaunchedEffect(theme) {
        when (theme) {
            HauntedTheme.DARK -> {
                launch {
                    starsAlpha.animateTo(1f, animationSpec = tween(1000, easing = LinearEasing))
                }
                launch {
                    cloudsAlpha.animateTo(0f, animationSpec = tween(1000, easing = LinearEasing))
                }
                launch {
                    cloud1X.animateTo(
                        -with(density) { 262.dp.toPx() },
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
                launch {
                    cloud2X.animateTo(
                        with(density) { -180.dp.toPx() },
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
                launch {
                    cloud3X.animateTo(
                        window.containerSize.width.toFloat() + with(density) { 175.dp.toPx() },
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
            }

            HauntedTheme.LIGHT -> {
                launch {
                    starsAlpha.animateTo(0f, animationSpec = tween(1000, easing = LinearEasing))
                }
                launch {
                    cloudsAlpha.animateTo(1f, animationSpec = tween(1000, easing = LinearEasing))
                }
                launch {
                    cloud1X.animateTo(
                        -230f,
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
                launch {
                    cloud2X.animateTo(
                        with(density) { 93.dp.toPx() },
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
                launch {
                    cloud3X.animateTo(
                        window.containerSize.width.toFloat() - with(density) { 175.dp.toPx() },
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
            }
        }
    }

    Image(
        painter = painterResource(Res.drawable.stars),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(starsAlpha.value),
    )

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painterResource(Res.drawable.cloud_01),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    translationX = cloud1X.value
                    translationY = window.containerSize.height * .25f
                    alpha = cloudsAlpha.value
                }
        )

        Image(
            painter = painterResource(Res.drawable.cloud_02),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    translationX = cloud2X.value
                    alpha = cloudsAlpha.value
                }
        )

        Image(
            painter = painterResource(Res.drawable.cloud_03),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    translationX = cloud3X.value
                    translationY = window.containerSize.height * .1f
                    alpha = cloudsAlpha.value
                }
        )
    }
}

@Composable
private fun BoxScope.Ghost(theme: HauntedTheme) {
    val ghostAlpha = remember { Animatable(0f) }
    val ghostScale = remember { Animatable(0f) }
    val window = LocalWindowInfo.current

    LaunchedEffect(theme) {
        when (theme) {
            HauntedTheme.DARK -> {
                launch {
                    ghostScale.animateTo(2.5f, animationSpec = tween(1000))
                }
                launch {
                    ghostAlpha.animateTo(1f, animationSpec = tween(1000))
                }
            }

            HauntedTheme.LIGHT -> {
                launch {
                    ghostScale.animateTo(0f, animationSpec = tween(1000))
                }
                launch {
                    ghostAlpha.animateTo(0f, animationSpec = tween(1000))
                }
            }
        }
    }

    Image(
        painter = painterResource(Res.drawable.ghost),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                translationX = 250f
                translationY = window.containerSize.height.toFloat() / 2.5f
                scaleX = ghostScale.value
                scaleY = ghostScale.value
                alpha = ghostAlpha.value
            }
    )

}

@Composable
private fun BoxScope.Switcher(
    theme: HauntedTheme,
    onAction: (HauntedThemeAction) -> Unit,
) {
    val pumpkinX = remember { Animatable(8f) }
    val density = LocalDensity.current

    LaunchedEffect(theme) {
        when (theme) {
            HauntedTheme.DARK -> {
                val target = with(density) {
                    120.dp.toPx() - 54.dp.toPx() - 8f
                }
                pumpkinX.animateTo(target, animationSpec = tween(1000, easing = LinearEasing))
            }

            HauntedTheme.LIGHT -> {
                pumpkinX.animateTo(8f, animationSpec = tween(1000, easing = LinearEasing))
            }
        }
    }

    Box(
        Modifier
            .width(120.dp)
            .height(72.dp)
            .padding(bottom = 20.dp)
            .background(color = Color.White, shape = CircleShape)
            .align(Alignment.BottomCenter),
    ) {
        Image(
            painter = painterResource(Res.drawable.pumpkin),
            contentDescription = null,
            modifier = Modifier
                .height(56.dp)
                .width(54.dp)
                .graphicsLayer {
                    clip = false
                    translationX = pumpkinX.value
                    translationY = -12f
                }
                .clip(CircleShape)
                .clickable {
                    onAction(HauntedThemeAction.OnThemeToggled)
                },
        )
    }
}

@Composable
private fun BoxScope.BottomBar(theme: HauntedTheme) {
    val scroll = rememberScrollState(initial = Int.MAX_VALUE)

    LaunchedEffect(theme) {
        when (theme) {
            HauntedTheme.DARK -> {
                scroll.animateScrollTo(
                    0,
                    animationSpec = tween(durationMillis = 1000, easing = EaseIn)
                )
            }

            HauntedTheme.LIGHT -> {
                scroll.animateScrollTo(
                    scroll.maxValue,
                    animationSpec = tween(durationMillis = 1000, easing = EaseIn)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .height(700.dp)
            .fillMaxWidth()
            .horizontalScroll(state = scroll, enabled = false)
            .align(Alignment.BottomCenter)
    ) {
        Image(
            painter = painterResource(Res.drawable.graveyard),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme(onThemeChanged = { }) {
        HaunterThemeScreen(
            state = HauntedThemeState(),
            onAction = {}
        )
    }
}