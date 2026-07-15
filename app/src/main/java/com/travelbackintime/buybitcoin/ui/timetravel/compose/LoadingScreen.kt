@file:Suppress("DEPRECATION")

package com.travelbackintime.buybitcoin.ui.timetravel.compose

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Movie
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import bitcoin.backintime.com.backintimebuybitcoin.R
import kotlinx.coroutines.isActive

@Composable
@SuppressLint("ResourceType")
internal fun LoadingScreen(onFinished: () -> Unit, animationEnabled: Boolean = true) {
    val context = LocalContext.current
    val movie = remember {
        context.resources.openRawResource(R.drawable.car_animation).use(Movie::decodeStream)
    }
    val fallbackFrame = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.car_animation)?.asImageBitmap()
    }
    val movieDuration = remember(movie) {
        movie?.duration()?.takeIf { it > 0 } ?: DEFAULT_MOVIE_DURATION_MILLIS
    }
    var movieTimeMillis by remember { mutableIntStateOf(0) }

    LaunchedEffect(movie, animationEnabled) {
        if (!animationEnabled) return@LaunchedEffect
        val startNanos = withFrameNanos { it }
        while (isActive && movieTimeMillis < movieDuration) {
            withFrameNanos { frameNanos ->
                movieTimeMillis = (
                    ((frameNanos - startNanos) / NANOS_PER_MILLISECOND) * PLAYBACK_SPEED
                    ).toInt().coerceAtMost(movieDuration)
            }
        }
        if (isActive) onFinished()
    }

    if (movie != null) {
        Canvas(modifier = Modifier.fillMaxSize().background(RetroBlack)) {
            movie.setTime(movieTimeMillis.coerceAtMost(movieDuration - 1))
            val scaleX = size.width / movie.width().coerceAtLeast(1)
            val scaleY = size.height / movie.height().coerceAtLeast(1)
            drawIntoCanvas { canvas ->
                val nativeCanvas = canvas.nativeCanvas
                nativeCanvas.save()
                nativeCanvas.scale(scaleX, scaleY)
                movie.draw(nativeCanvas, 0f, 0f)
                nativeCanvas.restore()
            }
        }
    } else if (fallbackFrame != null) {
        Image(
            bitmap = fallbackFrame,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().background(RetroBlack),
        )
    } else {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.fillMaxSize().background(RetroBlack),
        )
    }
}

private const val PLAYBACK_SPEED = 0.8f
private const val NANOS_PER_MILLISECOND = 1_000_000L
private const val DEFAULT_MOVIE_DURATION_MILLIS = 1_000
