package top.overlink.read.help.storage

import cn.hutool.crypto.symmetric.AES
import top.overlink.read.help.config.LocalConfig
import top.overlink.read.utils.MD5Utils

class BackupAES : AES(
    MD5Utils.md5Encode(LocalConfig.password ?: "").encodeToByteArray(0, 16)
)