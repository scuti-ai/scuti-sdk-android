package com.mindtrust.scutinativesdk

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class ScutiButton : Fragment()  {

    private var callback: ScutiInterface? = null
    private lateinit var scutiBtn:ImageButton;
    private lateinit var notification:ViewGroup;
    private lateinit var newItem:ImageView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.scutibutton, container, false)

        if (context is ScutiInterface) {
            callback = context as ScutiInterface
        }

        scutiBtn  = view.findViewById(R.id.scutibtn);
        notification  = view.findViewById(R.id.notification);
        newItem  = view.findViewById(R.id.newitem);

        scutiBtn.setOnClickListener{
            callback?.onScutiButtonClicked()

        }
        callback?.onButtonLoadCompleted()
        return view;
    }

    fun showNewItemImage(show:Boolean)
    {
        newItem.isVisible = show
    }

    fun showNotificationIcon(show:Boolean)
    {
        notification.isVisible = show
    }
}