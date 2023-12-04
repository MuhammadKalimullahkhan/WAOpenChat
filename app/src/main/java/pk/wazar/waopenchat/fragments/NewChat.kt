package pk.wazar.waopenchat.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.wazar.waopenchat.R
import pk.wazar.waopenchat.Utils

class NewChat : Fragment() {
    private lateinit var utils: Utils
    private lateinit var sendBTN: Button
    private lateinit var pasteBTN: Button
    private lateinit var clearBTN: Button

    private lateinit var phoneNumberET: EditText

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        utils = Utils(requireContext())
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_chat, container, false)

        sendBTN = view.findViewById(R.id.sendBTN)
        pasteBTN = view.findViewById(R.id.pasteBTN)
        clearBTN = view.findViewById(R.id.clearBTN)
        phoneNumberET = view.findViewById(R.id.phoneNumberET)

        sendBTN.setOnClickListener { sendWhatsAppMessage(phoneNumberET.text.toString()) }
        pasteBTN.setOnClickListener { onPaste() }
        clearBTN.setOnClickListener { onClear() }



        phoneNumberET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called during the text changes
                if (s.toString().isNotEmpty()) {
                    pasteBTN.visibility = View.GONE
                    clearBTN.visibility = View.VISIBLE
                }else{
                    pasteBTN.visibility = View.VISIBLE
                    clearBTN.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        onPaste()
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val validatePhoneNumber = utils.phoneNumberParser.isValid(phoneNumber)
        if (!validatePhoneNumber) {
            Toast.makeText(requireContext(), "invalid phone number", Toast.LENGTH_SHORT).show()
        }
        return validatePhoneNumber
    }

    private fun setPhoneNumber(widget: EditText, phoneNumber: String) {
        widget.setText(utils.phoneNumberParser.parsePhoneNumber(phoneNumber))

        pasteBTN.visibility = View.GONE
        clearBTN.visibility = View.VISIBLE
    }

    private fun sendWhatsAppMessage(phoneNumber: String) {
        if(!validatePhoneNumber(phoneNumber))
            return
        Toast.makeText(requireContext(), "sending Whatsapp message", Toast.LENGTH_SHORT).show()
    }

    private fun onClear() {
        phoneNumberET.setText("")
        pasteBTN.visibility = View.VISIBLE
        clearBTN.visibility = View.GONE
    }

    private fun onPaste() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        if (clipboardManager!!.hasPrimaryClip()) {
            // Get the primary clip from the clipboard
            val clipData: ClipData? = clipboardManager.primaryClip
            val item: ClipData.Item? = clipData?.getItemAt(0)

            // Extract the text from the clipboard
            val clipboardText = item?.text.toString()

            // Set the text to the EditText
            if(!validatePhoneNumber(clipboardText))
                return
            setPhoneNumber(phoneNumberET, clipboardText)

        } else {
            // Show a message indicating the clipboard is empty
            Toast.makeText(requireContext(), "Please! copy phone number.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}