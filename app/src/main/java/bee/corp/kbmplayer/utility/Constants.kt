package bee.corp.kbmplayer.utility

class Constants {
    object RequestCodes {
        const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0
        const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    }
    object IntentStringExtrasNames {
        const val MUSIC_PATH_EXTRA_NAME = "music_path"
        const val MUSIC_NAME_EXTRA_NAME = "music_name"
        const val MUSIC_AUTHOR_EXTRA_NAME = "music_author"
        const val MUSIC_DURATION_EXTRA_NAME = "music_duration"
        const val SONGS_ARRAY_EXTRA_NAME = "songs_array"
        const val MUSIC_TAB_DATA_INDEX_EXTRA_NAME = "music_tab_data_index"
    }
    object ErrorStrings {
        const val PLAYLIST_CREATION_ERROR = "Couldn't create playlist"
    }
    object DialogStrings {
        const val CREATE_PLAYLIST_DIALOG_MESSAGE = ""
        const val CREATE_PLAYLIST_DIALOG_TITLE = "Playlist creation"

        const val DELETE_PLAYLIST_DIALOG_MESSAGE = "Are you sure you want to delete"
        const val DELETE_PLAYLIST_DIALOG_TITLE = "Playlist Deletion Dialog"
        const val DELETE_PLAYLIST_POSITIVE_BUTTON_TEXT = "Ok"
        const val DELETE_PLAYLIST_NEGATIVE_BUTTON_TEXT = "Cancel"

        const val EDIT_PLAYLIST_DIALOG_MESSAGE = ""
        const val EDIT_PLAYLIST_DIALOG_TITLE = "Edit Playlist"
        const val EDIT_PLAYLIST_POSITIVE_BUTTON_TEXT = "Ok"
        const val EDIT_PLAYLIST_NEGATIVE_BUTTON_TEXT = "Cancel"

        object ButtonTexts {
            const val CREATE_PLAYLIST_DIALOG_POSITIVE_BUTTON_TEXT = "Ok"
        }
    }
    object PlaylistViewTypes {
        const val DELETE_PLAYLIST_BUTTON_VIEW_TYPE = 0
        const val EDIT_PLAYLIST_BUTTON_VIEW_TYPE = 1
        const val ROOT_VIEW_TYPE = 2
    }
    object MusicViewTypes {
        const val MORE_BUTTON_VIEW_TYPE = 0
        const val ROOT_VIEW_TYPE = 1
    }
    object ViewsDefaultValues {
        const val RECYCLER_VIEW_ITEM_DIVIDE_DISTANCE = 12
    }
}