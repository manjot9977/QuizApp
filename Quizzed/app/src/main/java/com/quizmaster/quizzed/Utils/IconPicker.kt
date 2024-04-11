package com.quizmaster.quizzed.Utils

import com.quizmaster.quizzed.R

object IconPicker {
    var icons = arrayOf(R.drawable.ic_icon_1,
        R.drawable.ic_icon_2,
        R.drawable.ic_icon_3,
        R.drawable.ic_icon_4,
        R.drawable.ic_icon_5,
        R.drawable.ic_icon_6,
        R.drawable.ic_icon_7,
        R.drawable.ic_icon_8,
    )
    var currenIcon = 0

    fun getIcon (): Int {
        currenIcon =  (currenIcon+1)% icons.size
        return icons[currenIcon]
    }
}