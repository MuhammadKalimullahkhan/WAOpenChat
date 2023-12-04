package pk.wazar.waopenchat

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.util.Locale

import com.google.i18n.phonenumbers.PhoneNumberUtil


class PhoneNumberParser(private val context: Context) {

    fun getCountryCode(): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // Get the country code based on the user's SIM card or network
        val countryCode: String =
            if (telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
                // For GSM or CDMA networks
                val networkCountryIso = telephonyManager.networkCountryIso
                if (!networkCountryIso.isNullOrEmpty()) {
                    val locale = Locale("", networkCountryIso)
                    locale.displayCountry
                } else {
                    // Default to the system locale if network country ISO is not available
                    Locale.getDefault().country
                }
            } else {
                // For CDMA networks, use the system locale
                Locale.getDefault().country
            }

        return countryCode
    }

    fun parsePhoneNumber(phoneNumber: String): String? {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        return phoneNumberUtil.format(
            phoneNumberUtil.parse(phoneNumber, getCountryCode()),
            PhoneNumberUtil.PhoneNumberFormat.E164
        )
    }

    fun isValidInt(text:String): Boolean{
        return try {
            text.toInt()
            true
        }catch (e:Exception){
            Log.d("ERROR", "invalid Int $text")
            false
        }
    }

    fun isValid(phoneNumber: String): Boolean {
        val pn:String = phoneNumber
        if (!(pn.isNotEmpty() && isValidInt(pn.replace("+", "").replace("-",""))))
            return false

        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        return try {
            val parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, getCountryCode())
            phoneNumberUtil.isValidNumber(parsedPhoneNumber)
            true
        } catch (e: Exception) {
            false
        }
    }
}

class Utils(context: Context) {
    var phoneNumberParser: PhoneNumberParser = PhoneNumberParser(context)
}