package com.elian.computeit.core.domain.util

import com.squareup.okhttp.internal.Util

fun hash(text: String): String = Util.shaBase64(text)