package com.jamesfchen.vpn

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jamesfchen.vpn.MyVpnService.Companion.ALLOW_PKG_NAME_LIST
import com.jamesfchen.vpn.client.C_TAG
import com.jamesfchen.vpn.client.Connection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString
import java.nio.ByteBuffer

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    companion object {
        const val VPN_REQUEST_CODE = 0x0f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    lateinit var myClient: Connection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        activity?.let {
            val vpnIntent = VpnService.prepare(it)
            if (vpnIntent != null) {
                startActivityForResult(vpnIntent, VPN_REQUEST_CODE)
            } else {
                onActivityResult(VPN_REQUEST_CODE, Activity.RESULT_OK, null)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            activity?.let {
                val realIntent = Intent(activity, MyVpnService::class.java)
                realIntent.putExtra(
                    ALLOW_PKG_NAME_LIST,
                    arrayListOf(
                        "com.netease.cloudmusic",
                        "com.hawksjamesf.spacecraft.debug",
                        "com.netease.cloudmusic.lite"
//                        "com.sankuai.meituan",
//                        "com.dianping.v1"
                    )
                )
                MyVpnService.start(it, realIntent)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.let {
            MyVpnService.stop(it)
        }
    }
}