package io.micro.kotlin_module.util

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Views utility extension
 *
 * @author act262@gmail.com
 */

/**
 * Inflate Layout，默认attachToRoot=false
 */
fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}

/**
 * new 一个匿名内部类的ViewHolder
 */
fun viewHolder(parent: ViewGroup, @LayoutRes resource: Int): RecyclerView.ViewHolder {
    return object : RecyclerView.ViewHolder(parent.inflate(resource)) {}
}