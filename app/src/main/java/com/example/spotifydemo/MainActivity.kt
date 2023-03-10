package com.example.spotifydemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifydemo.ui.theme.SpotifyDemoTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val mainViewModel: MainViewModel = viewModel()
                    if (mainViewModel.isConnect) {
                        ConnectedScreen(mainViewModel = mainViewModel)
                    } else {
                        Button(onClick = { mainViewModel.connect() }) {
                            Text(text = "connect", Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ConnectedScreen(mainViewModel: MainViewModel) {
    Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (mainViewModel.imageBitmap.value != null) {
            Image(
                bitmap = mainViewModel.imageBitmap.value!!.asImageBitmap(),
                contentDescription = ""
            )
        }

        Text(
            text = "??????:${mainViewModel.title}?????????:${mainViewModel.singer}?????????:${mainViewModel.album}",
            textAlign = TextAlign.Center
        )
        Text(text = "????????????:${mainViewModel.isPaused},????????????:${mainViewModel.isShuffling},????????????:${mainViewModel.repeatMode}")
        Text(text = "?????????${mainViewModel.playbackPosition},??????:${mainViewModel.playbackSpeed}")
        Text(text = "??????:${mainViewModel.duration / 1000}s,??????:${mainViewModel.isEpisode},??????:${mainViewModel.isPodcast}")
        Text(
            text = "????????????:${mainViewModel.playerContextTitle}\n${mainViewModel.playerContextSubtitle},${mainViewModel.playerContextType}",
            textAlign = TextAlign.Center
        )
        Text(text = "uri: ${mainViewModel.trackUri}")
        Text(text = "??????????????????${mainViewModel.isAdded},?????????: ${mainViewModel.canAdd}")
        Text(
            text = "?????????????????????${mainViewModel.canSkipNext},????????????${mainViewModel.canSkipNext},\n" +
                    "?????????${mainViewModel.canToggleShuffle},???????????????${mainViewModel.canRepeatTrack},???????????????${mainViewModel.canRepeatContext}," +
                    "???????????????:${mainViewModel.canSeek}", textAlign = TextAlign.Center
        )

        Row {
            Button(onClick = {
                mainViewModel.play()
            }) {
                Text(text = "play")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                mainViewModel.pause()
            }) {
                Text(text = "pause")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                mainViewModel.resume()
            }) {
                Text(text = "resume")
            }
        }

        Row {
            Button(onClick = {
                mainViewModel.skipPrevious()
            }) {
                Text(text = "skipPrevious")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                mainViewModel.skipNext()
            }) {
                Text(text = "skipNext")
            }
        }

        Row {
            Button(onClick = {
                mainViewModel.toggleShuffle()
            }) {
                Text(text = "toggleShuffle")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                mainViewModel.toggleRepeat()
            }) {
                Text(text = "toggleRepeat")
            }
        }


        Row {
            Button(onClick = {
                mainViewModel.addToLibrary()
            }) {
                Text(text = "addToLibrary")
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = {
                mainViewModel.removeFromLibrary()
            }) {
                Text(text = "removeFromLibrary")
            }
        }

//        Button(onClick = {
//            mainViewModel.setPodcastPlaybackSpeed()
//        }) {
//            Text(text = "setPodcastPlaybackSpeed")
//        }

        Button(onClick = {
            mainViewModel.disconnect()
        }) {
            Text(text = "disConnect")
        }
    }
}