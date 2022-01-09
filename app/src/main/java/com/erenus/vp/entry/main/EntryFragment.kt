package com.erenus.vp.entry.main

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erenus.vp.R
import com.erenus.vp.databinding.EntryFragmentBinding
import com.erenus.vp.imkb.MainActivity
import com.erenus.vp.network.models.HandshakeIncoming
import com.erenus.vp.network.models.HandshakeOutgoing
import com.erenus.vp.utils.*
import java.lang.Exception

class EntryFragment : Fragment() {

    companion object {
        fun newInstance() = EntryFragment()
    }

    private lateinit var viewModel: EntryViewModel
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private lateinit var entryFragmentBinding: EntryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[EntryViewModel::class.java]
        entryFragmentBinding = EntryFragmentBinding.inflate(inflater, container, false)
        entryFragmentBinding.lifecycleOwner = this
        entryFragmentBinding.vm = viewModel
        return entryFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        entryFragmentBinding.button.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

        handshakeRefresh()
        prepareDeviceInfo()
    }

    fun handshakeRefresh() {
        viewModel.setLoading(true)
        viewModel.getHandshake(prepareDeviceInfo())?.observe(this, {
            when {
                it == null -> {
                    viewModel.setLoading(false)
                    notificationUtil(R.string.err_UzakSunucuBaglantisiYok, context!!)
                }
                it.status?.isSuccess == false -> {
                    viewModel.setLoading(false)
                    context?.longToast(it.status.error?.message)
                }
                else -> {
                    prepareAuth(it)
                }
            }
        })
    }

    fun prepareDeviceInfo(): HandshakeOutgoing {
        val deviceInfo = HandshakeOutgoing()
        try {
            deviceInfo.systemVersion = VERSION.RELEASE
            deviceInfo.deviceId = Settings.Secure.ANDROID_ID
            deviceInfo.platformName = "Android"
            deviceInfo.deviceModel = Build.MODEL
            deviceInfo.manifacturer = Build.BRAND
        } catch (ex: Exception) {
            notificationUtilDirect(ex.localizedMessage, context!!)
        }
        return deviceInfo
    }

    fun prepareAuth(handshakeIncoming: HandshakeIncoming) {
        try {
            prefRepository.putString(Constraints.AES_KEY, handshakeIncoming.aesKey)
            prefRepository.putString(Constraints.AES_IV, handshakeIncoming.aesIV)
            prefRepository.putString(Constraints.AUTHORIZATION, handshakeIncoming.authorization)
            entryFragmentBinding.button.isEnabled = true
            viewModel.setLoading(false)
        } catch (ex: Exception) {
            notificationUtilDirect(ex.localizedMessage, context!!)
        }
    }
}