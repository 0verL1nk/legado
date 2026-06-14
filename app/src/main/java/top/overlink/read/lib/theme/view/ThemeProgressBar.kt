package top.overlink.read.lib.theme.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import top.overlink.read.lib.theme.accentColor
import top.overlink.read.utils.applyTint

class ThemeProgressBar(context: Context, attrs: AttributeSet) : ProgressBar(context, attrs) {

    init {
        if (!isInEditMode) {
            applyTint(context.accentColor)
        }
    }
}