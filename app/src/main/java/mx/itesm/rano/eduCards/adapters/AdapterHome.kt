package mx.itesm.rano.eduCards.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_statistic.view.*
import mx.itesm.rano.eduCards.Interfaces.ClickListener
import mx.itesm.rano.eduCards.R
import mx.itesm.rano.eduCards.models.CardStatistics

class AdapterHome(var cardStatistics: Array<CardStatistics>) :
    RecyclerView.Adapter<AdapterHome.RowView>() {

    var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_statistic, parent, false)
        return RowView(view)
    }

    override fun onBindViewHolder(holder: RowView, position: Int) {
        val card = cardStatistics[position]
        holder.set(card)
        holder.rowView.setOnClickListener {
            listener?.clicked(position)
        }
    }

    override fun getItemCount(): Int {
        return cardStatistics.size
    }

    class RowView(val rowView: View) : RecyclerView.ViewHolder(rowView) {
        fun set(cardStatistic: CardStatistics) {
            rowView.tvCardColor.text = cardStatistic.cardColor
            rowView.tvCardStatistic.text = cardStatistic.cardStatistic
            rowView.tvCardAmount.text = cardStatistic.cardAmount
            //set color
            println("Hola amigos jeje")
        }
    }
}