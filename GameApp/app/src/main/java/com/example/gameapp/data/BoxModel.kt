package com.example.gameapp.data


import androidx.annotation.IntDef


@IntDef(BoxStatus.IDLE,BoxStatus.ACTIVE, BoxStatus.MATCHED)
annotation class BoxStatus {
    companion object {
        const val IDLE = 0 // hidden status of text
        const val ACTIVE = 1 //  display text
        const val MATCHED = 2 // remove the box
    }
}

data class BoxModel(@BoxStatus var Status: Int, var letter: Char)