package com.example.template.ui.language

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.template.R
import com.example.template.data.models.AppLanguage
interface IClickLanguage {
    fun onClickLanguage(language: AppLanguage, position: Int)
}

class LanguageAdapter(
    private val languages: MutableList<AppLanguage>,
    private val listener: IClickLanguage
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private var languageCode = ""

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vItem: LinearLayout = itemView.findViewById(R.id.vItemLanguage)
        private val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)
        private val imgLanguage: ImageView = itemView.findViewById(R.id.imgLanguage)
        private val imgSelect: ImageView = itemView.findViewById(R.id.imgSelect)

        @SuppressLint("NotifyDataSetChanged")
        fun bind(language: AppLanguage) {
            tvLabel.text = language.name
            imgLanguage.setImageResource(language.flag)

            if (language.code == languageCode) {
                imgSelect.setImageResource(R.drawable.ic_selected)
            } else {
                imgSelect.setImageResource(R.drawable.ic_un_selected)
            }

            itemView.setOnClickListener {
                listener.onClickLanguage(language, adapterPosition)
                languageCode = language.code
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_language,
            parent,
            false
        )
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.size
}