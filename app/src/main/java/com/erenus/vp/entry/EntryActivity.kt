package com.erenus.vp.entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erenus.vp.R
import com.erenus.vp.entry.main.EntryFragment

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_activity)
        title = resources.getString(R.string.IMKBHisseVeEndeskler)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EntryFragment.newInstance())
                .commitNow()
        }
    }
}