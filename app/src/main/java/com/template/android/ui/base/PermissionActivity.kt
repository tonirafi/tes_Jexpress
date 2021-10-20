package com.template.android.ui.base

import android.Manifest
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.template.android.R
import com.template.android.utils.LogUtil
import com.walkermanx.permission.AndPermission
import com.walkermanx.permission.PermissionListener
import com.walkermanx.permission.Rationale
import com.walkermanx.permission.RationaleListener
import java.util.*


abstract class PermissionActivity : BaseActivity(), PermissionListener, RationaleListener {

    private var isCalling = false
    private var mPermissions: Array<out String>? = null
    private var mRequestCode: Int = 0

    fun hasPermissions(vararg permissions: String): Boolean =
        AndPermission.hasPermission(this, *permissions)

    fun requestPermissions(requestCode: Int, vararg permissions: String) {
        if (isCalling)
            return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.isEmpty())
                return
        }

        this.mRequestCode = requestCode
        this.isCalling = true
        this.mPermissions = permissions
        AndPermission.with(this)
            .requestCode(requestCode)
            .permission(permissions)
            .rationale(this)
            .callback(this)
            .start()
    }

    fun requestPermissions(requestCode: Int, vararg permissionsArray: Array<String>) {
        val permissionList = ArrayList<String>()
        for (permissions in permissionsArray) {
            for (permission in permissions) {
                permissionList.add(permission)
            }
        }

        val array: Array<String> = permissionList.toArray(arrayOfNulls<String>(permissionList.size))
        this.requestPermissions(requestCode, *array)
    }

    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
        if (requestCode == this.mRequestCode) {
            handleResults(grantPermissions)
        }
    }

    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
        if (requestCode == this.mRequestCode) {
            handleResults(deniedPermissions)
        }
    }

    override fun showRequestPermissionRationale(requestCode: Int, rationale: Rationale?) {
        if (requestCode == this.mRequestCode) {
            // Successfully.
//            AndPermission.rationaleDialog(this, rationale)
//                    .setTitle("Tips")
//                    .setMessage("Request permission to recommend content for you.")
//                    .setPositiveButton("Request Now")
//                    .setNegativeButton("Next time") { dialog, _ ->
//                        rationale?.cancel()
//                        dialog.cancel()
//                    }
//                    .show()

            AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle("Tips")
                .setMessage("Request permission to recommend content for you.")
                .setPositiveButton("Okay") { _, _ -> rationale?.resume() }
                .setNegativeButton("Nope") { _, _ -> rationale?.cancel() }
                .show()
        }
    }

    abstract fun permissionGranted(requestCode: Int)


    private fun handleResults(permissions: List<String>) {
        isCalling = false
        if (AndPermission.hasPermission(this, permissions)) {
            permissionGranted(this.mRequestCode)
        } else {
            // To judge whether to check the [Never ask again].
            if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                // First type: with AndPermission default prompt.
//                AndPermission.defaultSettingDialog(this, 400)
//                        .setTitle("Permission Failure")
//                        .setMessage("Have you denied some of the necessary permissions, the operation can not continue, is it reauthorized?")
//                        .setPositiveButton("Okay")
//                        .setNegativeButton("Nope") { dialog, _ -> dialog.cancel() }
//                        .show()

                val settingService = AndPermission.defineSettingDialog(this, 400)
                AlertDialog.Builder(this, R.style.DialogTheme)
                    .setTitle("Permission Failure")
                    .setMessage("Have you denied some of the necessary permissions, the operation can not continue, is it reauthorized?")
                    .setPositiveButton("Okay") { _, _ -> settingService.execute() }
                    .setNegativeButton("Nope") { _, _ -> settingService.cancel() }
                    .show()

            } else {
                LogUtil.logE("AndPermission", "请求权限:权限获取失败 ")
                val snackbar = Snackbar.make(
                    this.window.decorView,
                    getHint(permissions[0]),
                    BaseTransientBottomBar.LENGTH_INDEFINITE
                )
//                snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.black))
                snackbar.view.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.black
                    )
                )
                snackbar.setAction("Settings") {
                    AndPermission.defineSettingDialog(this, 400).execute()
                }
                snackbar.show()

            }
        }
    }


    private fun getHint(permissions: String): String {
        return when (permissions) {
            //CALENDAR
            Manifest.permission.READ_CALENDAR -> "READ_CALENDAR Permission lacks"
            Manifest.permission.WRITE_CALENDAR -> "WRITE_CALENDAR Permission lacks"

            //CONTACTS
            Manifest.permission.READ_CONTACTS -> "READ_CONTACTS Permissions lacks"
            Manifest.permission.WRITE_CONTACTS -> "WRITE_CONTACTS Permissions lacks"
            Manifest.permission.GET_ACCOUNTS -> "GET_ACCOUNTS Permissions lacks"

            //LOCATION
            Manifest.permission.ACCESS_FINE_LOCATION -> "ACCESS_FINE_LOCATION Permissions lacks"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "ACCESS_COARSE_LOCATION Permissions lacks"

            //CAMERA
            Manifest.permission.CAMERA -> "CAMERA Permission lacks"

            //MICROPHONE
            Manifest.permission.RECORD_AUDIO -> "RECORD_AUDIO Permission lacks"

            //PHONE
            Manifest.permission.READ_PHONE_STATE -> "READ_PHONE_STATE Permission lacks"
            Manifest.permission.CALL_PHONE -> "CALL_PHONE Permission lacks"
            Manifest.permission.READ_CALL_LOG -> "READ_CALL_LOG Permission lacks"
            Manifest.permission.WRITE_CALL_LOG -> "WRITE_CALL_LOG Permission lacks"
            Manifest.permission.USE_SIP -> "USE_SIP Permission lacks"
            Manifest.permission.PROCESS_OUTGOING_CALLS -> "PROCESS_OUTGOING_CALLS Permission lacks"

            //SENSORS
            Manifest.permission.BODY_SENSORS -> "BODY_SENSORS Permission lacks"

            //SMS
            Manifest.permission.SEND_SMS -> "SEND_SMS Permission lacks"
            Manifest.permission.RECEIVE_SMS -> "RECEIVE_SMS Permission lacks"
            Manifest.permission.READ_SMS -> "READ_SMS Permission lacks"
            Manifest.permission.RECEIVE_WAP_PUSH -> "RECEIVE_WAP_PUSH Permission lacks"
            Manifest.permission.RECEIVE_MMS -> "RECEIVE_MMS Permission lacks"

            //STORAGE
            Manifest.permission.READ_EXTERNAL_STORAGE -> "READ_EXTERNAL_STORAGE Permission lacks"
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "WRITE_EXTERNAL_STORAGE Permission lacks"

            else -> "Permissions lack"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 400) {
            Log.e("AndPermission", "请求权限:onActivityResult ")
            // This is the 400 you are above the number of incoming.
            // Check the permissions, and make the appropriate operation.
            if (mPermissions != null && AndPermission.hasPermission(
                    this,
                    Arrays.asList(*mPermissions!!)
                )
            )
                permissionGranted(this.mRequestCode)
//            else {
//                isCalling = false
//            }
        }


        var haveAGirlFriend = false //默人没有女票
        try {
            //执行高高高兴回家过年操作
            goHomeHappilyForCelebratingLunarNewYear()
            //标记为有女盆
            haveAGirlFriend = true

        } catch (e: GirlFriendNotFoundException) { //异常：找不到女票回不了家
            //TODO : Find A Girl...

        } finally {

            if (haveAGirlFriend) { //有女盆
                spendALovelyFamilyTime()  //可以过一个舒服的年了
            } else {
                //TODO 去淘宝买个防爆耳塞
            }
        }


    }


    class GirlFriend(val name: String)

    private fun becomeVeryNBAmongAllProgrammers() {}

    private fun spendALovelyFamilyTime() {}
    private fun findAGirlFriendSuccessfully(): Boolean = true

    @Throws(GirlFriendNotFoundException::class)
    private fun goHomeHappilyForCelebratingLunarNewYear() {

    }


    class GirlFriendNotFoundException : Exception()

}