package lv.ailab.senie.utils

import java.net.URLEncoder
import java.net.URLDecoder

fun String.urlEncode(): String = URLEncoder.encode(this, Charsets.UTF_8)

fun String.urlDecode(): String = URLDecoder.decode(this, Charsets.UTF_8)