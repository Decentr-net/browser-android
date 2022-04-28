package net.decentr.module_decentr.presentation.utils

import android.text.method.PasswordTransformationMethod
import android.view.View

class PasswordTransform : PasswordTransformationMethod() {

    companion object {
        private const val DOT = '\u2022'
    }

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {

        override val length: Int
            get() = source.length

        override fun get(index: Int): Char {
            return if (source[index] == ' ') ' ' else DOT
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(startIndex, endIndex)
        }
    }
}