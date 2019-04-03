package com.simlab.simiyu.playwithgooglesignin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * Simple list-based Activity to redirect to one of the other Activities. The code here is
 * uninteresting, [SignInActivity] is a good place to start if you are curious about
 * `GoogleSignInApi`.
 */
class ChooserActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        // Set up ListView and Adapter
        val listView = findViewById(R.id.list_view)

        val adapter = MyArrayAdapter(this, android.R.layout.simple_list_item_2, CLASSES)
        adapter.setDescriptionIds(DESCRIPTION_IDS)

        listView.setAdapter(adapter)
        listView.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val clicked = CLASSES[position]
        startActivity(Intent(this, clicked))
    }

    class MyArrayAdapter(private val mContext: Context, resource: Int, private val mClasses: Array<Class<*>>) :
        ArrayAdapter<Class<*>>(mContext, resource, mClasses) {
        private var mDescriptionIds: IntArray? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView

            if (convertView == null) {
                val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(android.R.layout.simple_list_item_2, null)
            }

            (view!!.findViewById<View>(android.R.id.text1) as TextView).text = mClasses[position].simpleName
            (view.findViewById<View>(android.R.id.text2) as TextView).setText(mDescriptionIds!![position])

            return view
        }

        fun setDescriptionIds(descriptionIds: IntArray) {
            mDescriptionIds = descriptionIds
        }
    }

    companion object {

        private val CLASSES = arrayOf(
            SignInActivity::class.java,
            SignInActivityWithDrive::class.java,
            IdTokenActivity::class.java,
            ServerAuthCodeActivity::class.java,
            RestApiActivity::class.java
        )

        private val DESCRIPTION_IDS = intArrayOf(
            R.string.desc_sign_in_activity,
            R.string.desc_sign_in_activity_scopes,
            R.string.desc_id_token_activity,
            R.string.desc_auth_code_activity,
            R.string.desc_rest_activity
        )
    }
}