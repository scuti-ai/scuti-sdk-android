package com.mindtrust.scutinativesdk

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout

class ButtonTest @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle:Int = 0,
    defStyleRes:Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    /*private val scutiBtn:ImageButton;
    private val notification:ViewGroup;
    private val newItem:ImageView;*/

    init {
        LayoutInflater.from(context).inflate(R.layout.mybutton, this, true)

        Log.d("INFO", "<******0 Button test 0******> ");
        /*scutiBtn  = findViewById<ImageButton>(R.id.scutibtn);
        notification  = findViewById<ViewGroup>(R.id.notification);
        newItem  = findViewById<ImageView>(R.id.newitem);*/

        //scutiBtn.setOnClickListener{ Toast.makeText(context,"Scuti button clicked!!", Toast.LENGTH_LONG).show() }
    }

    /*public fun showNewItemImage(show:Boolean)
    {
        newItem.isVisible = show
    }

    public fun showNotificationIcon(show:Boolean)
    {
        notification.isVisible = show
    }*/
}