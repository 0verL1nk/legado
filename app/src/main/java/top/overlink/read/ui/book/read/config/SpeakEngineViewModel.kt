package top.overlink.read.ui.book.read.config

import android.app.Application
import android.speech.tts.TextToSpeech
import top.overlink.read.base.BaseViewModel
import top.overlink.read.help.DefaultData

class SpeakEngineViewModel(application: Application) : BaseViewModel(application) {

    val sysEngines: List<TextToSpeech.EngineInfo> by lazy {
        val tts = TextToSpeech(context, null)
        val engines = tts.engines
        tts.shutdown()
        engines
    }

    fun importDefault() {
        execute {
            DefaultData.importDefaultHttpTTS()
        }
    }

}