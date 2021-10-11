package com.tes.frezzmart.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.format.Formatter
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.util.Preconditions
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tes.frezzmart.adapter.BaseCard
import com.tes.frezzmart.adapter.CardAdapter
import com.tes.frezzmart.adapter.ItemViewProvider
import com.tes.frezzmart.router.RouterConstants
import com.tes.frezzmart.view.GridSpacingItemDecoration
import com.tes.frezzmart.widget.UploadImageViewGroup
import java.lang.reflect.Type
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class AppUtilNew {

    companion object {
        fun parseIntent(intent: Intent?): Uri? {
            val extra = intent?.extras ?: return intent?.data

            return extra.keySet()?.asSequence()?.find {
                extra[it]?.toString()?.startsWith(RouterConstants.Common.SCHEME) ?: false
            }?.let { Uri.parse(extra[it]?.toString()) } ?: intent?.data
        }

        inline fun <reified T> jsonToClass(jsonObject: JsonObject): T {
            val type: Type = object : TypeToken<T>() {}.type
            return Gson().fromJson(jsonObject, type)
        }

        fun persentase(nominal: Int, persentse: Int = 0):Int{
            return (nominal*persentse/100)
        }


        fun getIpWifi(context: Context):String{
            val context = context.applicationContext
            val wm: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        }

         fun getIpAddress(): String? {
            var ip = ""
            try {
                val enumNetworkInterfaces: Enumeration<NetworkInterface> = NetworkInterface
                        .getNetworkInterfaces()
                while (enumNetworkInterfaces.hasMoreElements()) {
                    val networkInterface: NetworkInterface = enumNetworkInterfaces
                            .nextElement()
                    val enumInetAddress: Enumeration<InetAddress> = networkInterface
                            .inetAddresses
                    while (enumInetAddress.hasMoreElements()) {
                        val inetAddress: InetAddress = enumInetAddress.nextElement()
                        if (inetAddress.isSiteLocalAddress) {
                            ip += inetAddress.hostAddress
                        }
                    }
                }
            } catch (e: SocketException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                ip += """
            Something Wrong! ${e.toString().toString()}
            
            """.trimIndent()
            }
            return ip
        }
        @JvmStatic
        fun setRecycleViewGlobal(context: Context, rv: RecyclerView, provider: ItemViewProvider<*, *>, listArrayBaseCard: List<BaseCard>, spanCount:Int, devidertWithdDp:Int):CardAdapter{
            var adapter: CardAdapter? = null
            val gridLayoutManager: GridLayoutManager = UploadImageViewGroup.InnerGridLayoutManager(context, spanCount)
            gridLayoutManager.isSmoothScrollbarEnabled = true
            gridLayoutManager.isAutoMeasureEnabled = true
            rv.layoutManager = gridLayoutManager
            rv.addItemDecoration(GridSpacingItemDecoration(spanCount, devidertWithdDp))
            rv.setHasFixedSize(true)
            rv.itemAnimator = DefaultItemAnimator()
            rv.isNestedScrollingEnabled = false
            adapter = CardAdapter(context)
            adapter!!.setWithoutProvider(provider)
//            adapter.setOnItemClickListener(this);
            //            adapter.setOnItemClickListener(this);
            rv.adapter = adapter
            adapter!!.clearList()
            adapter!!.addAll(listArrayBaseCard)

            return adapter
        }

        fun blokChangeBold(editText: EditText) {

            if ( editText.selectionStart !=0 && editText.selectionEnd !=0){
                val spannableString: Spannable = SpannableStringBuilder(editText.text)
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    editText.selectionStart,
                    editText.selectionEnd,
                    0)
                editText.setText(spannableString)
                editText.setSelection(editText.length())
            }

        }

        fun blokChangeItalics(editText: EditText) {
            if ( editText.selectionStart !=0 && editText.selectionEnd !=0) {
                val spannableString: Spannable = SpannableStringBuilder(editText.text)
                spannableString.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    editText.selectionStart,
                    editText.selectionEnd,
                    0)
                editText.setText(spannableString)
                editText.setSelection(editText.length())
            }
        }

        fun blokChangeUnderline(editText: EditText) {
            if ( editText.selectionStart !=0 && editText.selectionEnd !=0) {
                val spannableString: Spannable = SpannableStringBuilder(editText.text)
                spannableString.setSpan(
                    UnderlineSpan(),
                    editText.selectionStart,
                    editText.selectionEnd,
                    0)
                editText.setText(spannableString)
                editText.setSelection(editText.length())
            }

        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.getActiveNetworkInfo()
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


        fun formatDate(strDate:String): String {
            val sdf = SimpleDateFormat("yyyy-mm-dd")
            val date = sdf.parse(strDate)
            val cal = Calendar.getInstance()
            cal.time = date
            val formatter =  SimpleDateFormat("MMM dd'"+getDayOfMonthSuffix(cal.get(Calendar.DAY_OF_MONTH))+"', yyyy")
            return formatter.format(date)
        }
        @SuppressLint("RestrictedApi")
        private fun getDayOfMonthSuffix(n: Int): String {
            Preconditions.checkArgument(n in 1..31, "illegal day of month: $n")
            return if (n in 11..13) {
                "th"
            } else when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }

    }







}