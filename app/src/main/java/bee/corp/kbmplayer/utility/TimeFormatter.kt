package bee.corp.kbmplayer.utility

class TimeFormatter {
    companion object {
        fun millisToSeconds(m: Long) : Long {
            return m/10000
        }
        fun millisToMinutes(m: Long) : Long {
            return m/60000
        }
        fun millisToHours(m: Long) : Long {
            return m/3600000
        }

        fun convertMillisToStringTime(m: Long) : String {
            return "${String.format("%02d", millisToHours(m))}:${String.format("%02d", millisToMinutes(m))}:${String.format("%02d", millisToSeconds(m))}"
        }
    }
}