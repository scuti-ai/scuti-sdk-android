package com.mindtrust.scutinativesdk

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible

class ScutiButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle:Int = 0,
    defStyleRes:Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    private val scutiBtn:ImageButton;
    private val notification:ViewGroup;
    private val newItem:ImageView;

    init {
        LayoutInflater.from(context).inflate(R.layout.scutibutton, this, true)

        Log.d("INFO", "<-----<< Button Scuti >>-----> ");
        scutiBtn  = findViewById<ImageButton>(R.id.scutibtn);
        notification  = findViewById<ViewGroup>(R.id.notification);
        newItem  = findViewById<ImageView>(R.id.newitem);

        scutiBtn.setOnClickListener{
            Log.d("INFO", "<-----<< Button Scuti CLICKED!! >>----->");
            Toast.makeText(context,"Scuti button clicked!!", Toast.LENGTH_LONG).show()
        }
    }

    public fun showNewItemImage(show:Boolean)
    {
        newItem.isVisible = show
    }

    public fun showNotificationIcon(show:Boolean)
    {
        notification.isVisible = show
    }
}