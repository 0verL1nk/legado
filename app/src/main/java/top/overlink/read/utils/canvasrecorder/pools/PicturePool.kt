package top.overlink.read.utils.canvasrecorder.pools

import android.graphics.Picture
import top.overlink.read.utils.objectpool.BaseObjectPool

class PicturePool : BaseObjectPool<Picture>(64) {

    override fun create(): Picture = Picture()

}
