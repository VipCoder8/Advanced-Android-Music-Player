package bee.corp.kbmplayer.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.utility.Constants
import bee.corp.kbmplayer.view.fragments.HomeFragment
import bee.corp.kbmplayer.view.fragments.PlaylistsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var playlistsFragment: PlaylistsFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            initViews()
        }
    }

    private fun requestPermissions() {
        if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constants.RequestCodes.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
        if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.RequestCodes.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    private fun initViews() {
        homeFragment = HomeFragment()
        playlistsFragment = PlaylistsFragment()
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
        bottomNavigationView.setOnItemSelectedListener { p0 ->
            when (p0.itemId) {
                R.id.home_item -> supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    homeFragment
                ).commit()

                R.id.playlist_item -> supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    playlistsFragment
                ).commit()
            }
            true
        }
    }

}