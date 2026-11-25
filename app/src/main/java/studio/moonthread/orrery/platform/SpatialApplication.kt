package studio.moonthread.orrery.platform

import android.app.Application
import com.pico.spatial.ui.foundation.dsl.launch
import studio.moonthread.orrery.mainApp

class SpatialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        launch(::mainApp)
    }
}
