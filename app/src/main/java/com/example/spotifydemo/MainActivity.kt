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
            text = "歌曲:${mainViewModel.title}，作者:${mainViewModel.singer}，专辑:${mainViewModel.album}",
            textAlign = TextAlign.Center
        )
        Text(text = "是否暂停:${mainViewModel.isPaused},是否随机:${mainViewModel.isShuffling},重复模式:${mainViewModel.repeatMode}")
        Text(text = "位置：${mainViewModel.playbackPosition},速度:${mainViewModel.playbackSpeed}")
        Text(text = "时长:${mainViewModel.duration / 1000}s,插曲:${mainViewModel.isEpisode},播客:${mainViewModel.isPodcast}")
        Text(
            text = "播放信息:${mainViewModel.playerContextTitle}\n${mainViewModel.playerContextSubtitle},${mainViewModel.playerContextType}",
            textAlign = TextAlign.Center
        )
        Text(text = "uri: ${mainViewModel.trackUri}")
        Text(text = "已添加到库：${mainViewModel.isAdded},可添加: ${mainViewModel.canAdd}")
        Text(
            text = "限制：可上一首${mainViewModel.canSkipNext},可下一首${mainViewModel.canSkipNext},\n" +
                    "可随机${mainViewModel.canToggleShuffle},可重复单曲${mainViewModel.canRepeatTrack},可重复内容${mainViewModel.canRepeatContext}," +
                    "可拖动进度:${mainViewModel.canSeek}", textAlign = TextAlign.Center
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