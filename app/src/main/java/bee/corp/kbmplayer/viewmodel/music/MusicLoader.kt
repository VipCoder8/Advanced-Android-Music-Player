package bee.corp.kbmplayer.viewmodel.music

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.SongSearcher
import bee.corp.kbmplayer.utility.TimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MusicLoader(application: Application) : AndroidViewModel(application) {

    private var musicsList: ArrayList<MusicTabData> = ArrayList()

    private var songSearcher: SongSearcher = SongSearcher()

    private var _mutableMusiscsListLiveData: MutableLiveData<ArrayList<MusicTabData>> =
        MutableLiveData<ArrayList<MusicTabData>>()
        .apply {value = musicsList}
    val mutableMusicsLiveData: LiveData<ArrayList<MusicTabData>> get() = _mutableMusiscsListLiveData

    private var _mutableSongsFoundLiveData: MutableLiveData<ArrayList<MusicTabData>> = MutableLiveData()
    val mutableSongsFoundLiveData: LiveData<ArrayList<MusicTabData>> get() = _mutableSongsFoundLiveData

    fun loadMusicFiles() {
        viewModelScope.launch {
            findMusicFiles(super.getApplication<Application>().applicationContext)
        }
    }

    fun searchSongs(title: String, arr: ArrayList<MusicTabData>) {
        _mutableSongsFoundLiveData.value = songSearcher.searchForIn(title, arr)
    }

    private suspend fun findMusicFiles(c: Context) {
        //Executing in background in coroutine scope so main thread won't be blocked
        withContext(Dispatchers.IO) {
            //Retrieving ContentResolver for finding music files
            val contentResolver: ContentResolver = c.contentResolver

            //Projection array to query only necessary data about music file
            val projection: Array<String> = arrayOf(
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
            )
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

            //Retrieving indexes of columns where data about music file is stored
            val artistIndex: Int = cursor?.getColumnIndex(projection[0]) ?: -1
            val durationIndex: Int = cursor?.getColumnIndex(projection[1]) ?: -1
            val titleIndex: Int = cursor?.getColumnIndex(projection[2]) ?: -1
            val pathIndex: Int = cursor?.getColumnIndex(projection[3]) ?: -1

            //Getting results while cursor has them
            while (cursor?.moveToNext() == true) {
                val filePath: String = cursor.getString(pathIndex)

                //Checking if found file exists(i.e. visible to user in his file manager)
                //so user won't see music files that they can't see on their file manager
                if (File(filePath).exists()) {
                    val artist: String = cursor.getString(artistIndex)
                    val duration: Long = cursor.getString(durationIndex).toLong()
                    val title: String = cursor.getString(titleIndex)
                    val musicData = MusicTabData(title, artist,
                        TimeFormatter.convertMillisToStringTime(duration), filePath, duration)
                    musicsList += musicData
                }
            }
            cursor?.close()
            _mutableMusiscsListLiveData.postValue(musicsList)
        }
    }

}