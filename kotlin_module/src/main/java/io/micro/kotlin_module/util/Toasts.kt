package io.micro.kotlin_module.util

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * Toast utility extension
 *
 * like anko's library
 *
 * @author act262@gmail.com
 */

/**
 * 短Toast
 */
fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/**
 * 长Toast
 */
fun Context.longToast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/**
 * 短Toast,使用字符串资源
 */
fun Context.toast(@StringRes resId: Int) = toast(getText(resId))

/**
 * 长Toast,使用字符串资源
 */
fun Context.longToast(@StringRes resId: Int) = longToast(getText(resId))

fun android.support.v4.app.Fragment.toast(msg: CharSequence) = activity?.toast(msg)
fun android.support.v4.app.Fragment.longToast(msg: CharSequence) = activity?.longToast(msg)
fun android.support.v4.app.Fragment.toast(@StringRes resId: Int) = activity?.toast(getText(resId))
fun android.support.v4.app.Fragment.longToast(@StringRes resId: Int) = activity?.longToast(getText(resId))