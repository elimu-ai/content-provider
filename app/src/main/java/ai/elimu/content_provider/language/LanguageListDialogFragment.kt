package ai.elimu.content_provider.language

import ai.elimu.content_provider.MainActivity
import ai.elimu.content_provider.R
import ai.elimu.content_provider.util.SharedPreferencesHelper
import ai.elimu.model.v2.enums.Language
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 * LanguageListDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
</pre> *
 */
class LanguageListDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_language_list_dialog_list_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = LanguageAdapter()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        isCancelable = false
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }


    private inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.fragment_language_list_dialog_list_dialog_item,
                parent,
                false
            )
        ) {
        val text: TextView =
            itemView.findViewById(R.id.text)
    }


    private inner class LanguageAdapter : RecyclerView.Adapter<ViewHolder>() {
        private val languages = Language.entries.toTypedArray()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val language = languages[position]
            holder.text.text = language.englishName + " (" + language.nativeName + ")"
            holder.text.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    Log.i(javaClass.name, "onClick")
                    Log.i(javaClass.name, "language: $language")
                    SharedPreferencesHelper.storeLanguage(context, language)
                    val mainActivityIntent = Intent(
                        context,
                        MainActivity::class.java
                    )
                    startActivity(mainActivityIntent)
                    activity?.finish()
                }
            })
        }

        override fun getItemCount(): Int {
            return languages.size
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): LanguageListDialogFragment {
            val fragment = LanguageListDialogFragment()
            return fragment
        }
    }
}
