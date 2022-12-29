package com.example.spotifydemo

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.spotify.android.appremote.api.*
import com.spotify.protocol.client.Subscription.EventCallback
import com.spotify.protocol.types.*

const val CLIENT_ID = "53da6c57e00b4b1b9ccedddf153dcd14"
const val REDIRECT_URI = "com.example.spotifydemo://callback"

class MainViewModel : ViewModel() {
    private val app = SpotifyDemoApp.getInstance()

    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private val playerApi: PlayerApi?
        get() = mSpotifyAppRemote?.playerApi
    private val userApi: UserApi?
        get() = mSpotifyAppRemote?.userApi
    private val imagesApi: ImagesApi?
        get() = mSpotifyAppRemote?.imagesApi
    private val contentApi: ContentApi?
        get() = mSpotifyAppRemote?.contentApi


    var title by mutableStateOf("title")
    var singer by mutableStateOf("singer")
    var album by mutableStateOf("album")
    var playerContextTitle by mutableStateOf("playerTitle")
    var playerContextSubtitle by mutableStateOf("playerSubtitle")
    var playerContextType by mutableStateOf("playerType")
    var duration by mutableStateOf(0L)
    var trackUri by mutableStateOf("")
    var isConnect by mutableStateOf(false)
    var isEpisode by mutableStateOf(false)
    var isPodcast by mutableStateOf(false)
    var isPaused by mutableStateOf(false)
    var isShuffling by mutableStateOf(false)
    var repeatMode by mutableStateOf(0)
    var playbackSpeed by mutableStateOf(0f)
    var playbackPosition by mutableStateOf(0L)
    var canRepeatContext by mutableStateOf(false)
    var canRepeatTrack by mutableStateOf(false)
    var canSeek by mutableStateOf(false)
    var canSkipNext by mutableStateOf(false)
    var canSkipPrev by mutableStateOf(false)
    var canToggleShuffle by mutableStateOf(false)

    var canAdd by mutableStateOf(false)
    var isAdded by mutableStateOf(false)

    private var _imageBitmap: MutableState<Bitmap?> = mutableStateOf(null)
    val imageBitmap: State<Bitmap?> = _imageBitmap


    // mSpotifyAppRemote.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

    init {
        connect()
    }

    private fun subscribeToPlayerState() {
        playerApi?.let { playerApi ->
            playerApi.subscribeToPlayerState().setEventCallback { playerState: PlayerState ->
                val track: Track? = playerState.track
                isPaused = playerState.isPaused
                playbackSpeed = playerState.playbackSpeed
                playbackPosition = playerState.playbackPosition

                isShuffling = playerState.playbackOptions.isShuffling
                repeatMode = playerState.playbackOptions.repeatMode
                canRepeatContext = playerState.playbackRestrictions.canRepeatContext
                canRepeatTrack = playerState.playbackRestrictions.canRepeatTrack
                canSeek = playerState.playbackRestrictions.canSeek
                canSkipNext = playerState.playbackRestrictions.canSkipNext
                canSkipPrev = playerState.playbackRestrictions.canSkipPrev
                canToggleShuffle = playerState.playbackRestrictions.canToggleShuffle

                if (track != null) {
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                    title = track.name
                    singer = track.artist.name
                    album = track.album.name
                    trackUri = track.uri
                    duration = track.duration
                    isEpisode = track.isEpisode
                    isPodcast = track.isPodcast

                    imagesApi?.getImage(track.imageUri, Image.Dimension.MEDIUM)?.setResultCallback {
                        Log.i("MainViewModel", "subscribeToPlayerState: $it")
                        _imageBitmap.value = it
                    }

                    userApi?.getLibraryState(trackUri)?.setResultCallback {
                        canAdd = it.canAdd
                        isAdded = it.isAdded
                    }
                }
            }

            playerApi.subscribeToPlayerContext().setEventCallback {
                playerContextTitle = it.title
                playerContextSubtitle = it.subtitle
                playerContextType = it.type
            }
        }

    }

    fun play() {
        playerApi?.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
    }

    fun pause() {
        playerApi?.pause()
    }

    fun resume() {
        playerApi?.resume()
    }

    fun skipPrevious() {
        playerApi?.skipPrevious()
    }

    fun skipNext() {
        playerApi?.skipNext()
    }

    fun toggleShuffle() {
        playerApi?.toggleShuffle()
    }

    fun toggleRepeat() {
        playerApi?.toggleRepeat()
    }

    fun setPodcastPlaybackSpeed(){
        playerApi?.setPodcastPlaybackSpeed(PlaybackSpeed.PodcastPlaybackSpeed.PLAYBACK_SPEED_200)
    }

    fun connect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)

        val connectionParams =
            ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(app, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    isConnect = true
                    Log.d("MainActivity", "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    subscribeToPlayerState()

                    userApi?.subscribeToUserStatus()?.setEventCallback {
                        EventCallback<UserStatus> { data -> data?.isLoggedIn }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MyActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    fun disconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
        isConnect = false
    }

    fun addToLibrary() {
        if (trackUri.isNotEmpty()) {
            userApi?.addToLibrary(trackUri)
        }
    }

    fun removeFromLibrary() {
        if (trackUri.isNotEmpty()) {
            userApi?.removeFromLibrary(trackUri)
        }
    }
}