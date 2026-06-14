package top.overlink.read.ui.config

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import top.overlink.read.R
import top.overlink.read.constant.PreferKey
import top.overlink.read.help.config.AppConfig
import top.overlink.read.lib.dialogs.selector
import top.overlink.read.lib.prefs.SwitchPreference
import top.overlink.read.lib.prefs.fragment.PreferenceFragment
import top.overlink.read.lib.theme.primaryColor
import top.overlink.read.model.BookCover
import top.overlink.read.ui.file.HandleFileContract
import top.overlink.read.utils.FileUtils
import top.overlink.read.utils.MD5Utils
import top.overlink.read.utils.externalFiles
import top.overlink.read.utils.getPrefString
import top.overlink.read.utils.inputStream
import top.overlink.read.utils.putPrefString
import top.overlink.read.utils.readUri
import top.overlink.read.utils.removePref
import top.overlink.read.utils.setEdgeEffectColor
import top.overlink.read.utils.toastOnUi
import splitties.init.appCtx
import java.io.FileOutputStream

class WelcomeConfigFragment : PreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val requestWelcomeImage = 221
    private val requestWelcomeImageDark = 222
    private val selectImage = registerForActivityResult(HandleFileContract()) {
        it.uri?.let { uri ->
            when (it.requestCode) {
                requestWelcomeImage -> setCoverFromUri(PreferKey.welcomeImage, uri)
                requestWelcomeImageDark -> setCoverFromUri(PreferKey.welcomeImageDark, uri)
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_config_welcome)
        val welcomeImage = AppConfig.welcomeImage
        val welcomeImageDark = AppConfig.welcomeImageDark
        upPreferenceSummary(PreferKey.welcomeImage, welcomeImage)
        upPreferenceSummary(PreferKey.welcomeImageDark, welcomeImageDark)
        findPreference<SwitchPreference>(PreferKey.welcomeShowText)?.let {
            it.isEnabled = !welcomeImage.isNullOrEmpty()
        }
        findPreference<SwitchPreference>(PreferKey.welcomeShowIcon)?.let {
            it.isEnabled = !welcomeImage.isNullOrEmpty()
        }
        findPreference<SwitchPreference>(PreferKey.welcomeShowTextDark)?.let {
            it.isEnabled = !welcomeImageDark.isNullOrEmpty()
        }
        findPreference<SwitchPreference>(PreferKey.welcomeShowIconDark)?.let {
            it.isEnabled = !welcomeImageDark.isNullOrEmpty()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setTitle(R.string.welcome_style)
        listView.setEdgeEffectColor(primaryColor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        when (key) {
            PreferKey.welcomeImage -> {
                val welcomeImage = getPrefString(key)
                upPreferenceSummary(key, welcomeImage)
                findPreference<SwitchPreference>(PreferKey.welcomeShowText)?.let {
                    it.isEnabled = !welcomeImage.isNullOrEmpty()
                }
                findPreference<SwitchPreference>(PreferKey.welcomeShowIcon)?.let {
                    it.isEnabled = !welcomeImage.isNullOrEmpty()
                }
            }

            PreferKey.welcomeImageDark -> {
                val welcomeImageDark = getPrefString(key)
                upPreferenceSummary(key, welcomeImageDark)
                findPreference<SwitchPreference>(PreferKey.welcomeShowTextDark)?.let {
                    it.isEnabled = !welcomeImageDark.isNullOrEmpty()
                }
                findPreference<SwitchPreference>(PreferKey.welcomeShowIconDark)?.let {
                    it.isEnabled = !welcomeImageDark.isNullOrEmpty()
                }
            }
        }
    }

    @SuppressLint("PrivateResource")
    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            PreferKey.welcomeImage ->
                if (getPrefString(preference.key).isNullOrEmpty()) {
                    selectImage.launch {
                        requestCode = requestWelcomeImage
                        mode = HandleFileContract.IMAGE
                    }
                } else {
                    context?.selector(
                        items = arrayListOf(
                            getString(R.string.delete),
                            getString(R.string.select_image)
                        )
                    ) { _, i ->
                        if (i == 0) {
                            removePref(preference.key)
                            AppConfig.welcomeShowText = true
                            AppConfig.welcomeShowIcon = true
                            findPreference<SwitchPreference>(PreferKey.welcomeShowText)?.let {
                                it.isChecked = true
                            }
                            findPreference<SwitchPreference>(PreferKey.welcomeShowIcon)?.let {
                                it.isChecked = true
                            }
                            BookCover.upDefaultCover()
                        } else {
                            selectImage.launch {
                                requestCode = requestWelcomeImage
                                mode = HandleFileContract.IMAGE
                            }
                        }
                    }
                }

            PreferKey.welcomeImageDark ->
                if (getPrefString(preference.key).isNullOrEmpty()) {
                    selectImage.launch {
                        requestCode = requestWelcomeImageDark
                        mode = HandleFileContract.IMAGE
                    }
                } else {
                    context?.selector(
                        items = arrayListOf(
                            getString(R.string.delete),
                            getString(R.string.select_image)
                        )
                    ) { _, i ->
                        if (i == 0) {
                            removePref(preference.key)
                            AppConfig.welcomeShowTextDark = true
                            AppConfig.welcomeShowIconDark = true
                            findPreference<SwitchPreference>(PreferKey.welcomeShowTextDark)?.let {
                                it.isChecked = true
                            }
                            findPreference<SwitchPreference>(PreferKey.welcomeShowIconDark)?.let {
                                it.isChecked = true
                            }
                            BookCover.upDefaultCover()
                        } else {
                            selectImage.launch {
                                requestCode = requestWelcomeImageDark
                                mode = HandleFileContract.IMAGE
                            }
                        }
                    }
                }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun upPreferenceSummary(preferenceKey: String, value: String?) {
        val preference = findPreference<Preference>(preferenceKey) ?: return
        when (preferenceKey) {
            PreferKey.welcomeImage,
            PreferKey.welcomeImageDark -> preference.summary = if (value.isNullOrBlank()) {
                getString(R.string.select_image)
            } else {
                value
            }

            else -> preference.summary = value
        }
    }

    private fun setCoverFromUri(preferenceKey: String, uri: Uri) {
        readUri(uri) { fileDoc, inputStream ->
            kotlin.runCatching {
                var file = requireContext().externalFiles
                val suffix = fileDoc.name.substringAfterLast(".")
                val fileName = uri.inputStream(requireContext()).getOrThrow().use {
                    MD5Utils.md5Encode(it) + ".$suffix"
                }
                file = FileUtils.createFileIfNotExist(file, "covers", fileName)
                FileOutputStream(file).use {
                    inputStream.copyTo(it)
                }
                putPrefString(preferenceKey, file.absolutePath)
            }.onFailure {
                appCtx.toastOnUi(it.localizedMessage)
            }
        }
    }

}