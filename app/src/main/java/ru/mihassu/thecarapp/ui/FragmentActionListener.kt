package ru.mihassu.thecarapp.ui

interface FragmentActionListener {
    fun setFabListener(action: () -> Unit)
    fun hideFab()
    fun showFab()
    fun setHomeAction(isShow: Boolean, action:() -> Boolean)
}