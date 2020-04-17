package iti.intake40.covistics.view.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import iti.intake40.covistics.R
import iti.intake40.covistics.core.CovidSharedPreferences
import iti.intake40.covistics.data.model.SingleCountryStats
import kotlinx.android.synthetic.main.country_stats_item.view.*

class CountryStatsAdapter(val context: Context) :
    RecyclerView.Adapter<CountryStatsAdapter.ViewHolder>() {

    var countriesList: List<SingleCountryStats> = ArrayList()
    var isSubscribed: Boolean = CovidSharedPreferences.isCountrySubscribed
    var countryName: String? = CovidSharedPreferences.countryName


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_stats_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = countriesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.country_tv.text = countriesList.get(position).countryName
        holder.itemView.confirmed_tv.text = countriesList.get(position).cases
        holder.itemView.new_cases_tv.text = countriesList.get(position).newCases
        holder.itemView.recovered_tv.text = countriesList.get(position).totalRecovered
        holder.itemView.deaths_tv.text = countriesList.get(position).deaths
        if (countriesList.get(position).countryName == countryName){
            holder.itemView.subscribe_fab.setBackgroundTintList(
                ColorStateList.valueOf(
                    context.resources.getColor(
                        R.color.death_color
                    )
                )
            )
        }else{
            holder.itemView.subscribe_fab.setBackgroundTintList(
                ColorStateList.valueOf(
                    context.resources.getColor(
                        android.R.color.transparent
                    )
                )
            )
        }

        val pathString =
            "flags/".plus((countriesList.get(position).countryName).toLowerCase()).plus(".png")
        val ref = FirebaseStorage.getInstance().reference.child(pathString)
        Log.d("adapter", pathString)
        ref.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .centerCrop()
                .placeholder(R.drawable.ic_refresh)
                .into(holder.itemView.flag_iv)
        }


        holder.itemView.subscribe_fab.setOnClickListener(View.OnClickListener {
            Log.d(
                "TAG",
                countriesList.get(position).countryName
            )

            if(isSubscribed){
                if(countriesList.get(position).countryName.equals(countryName)){
                    holder.itemView.subscribe_fab.setBackgroundTintList(
                        ColorStateList.valueOf(
                            context.resources.getColor(
                                android.R.color.transparent
                            )
                        )
                    )
                    CovidSharedPreferences.isCountrySubscribed = false
                    CovidSharedPreferences.countryName = null
                    isSubscribed = CovidSharedPreferences.isCountrySubscribed
                    countryName = CovidSharedPreferences.countryName
                }else{
                    print("bla")
                    //TODO Alert Dialog
                }
            }else {
                holder.itemView.subscribe_fab.setBackgroundTintList(
                    ColorStateList.valueOf(
                        context.resources.getColor(
                            R.color.death_color
                        )
                    )
                )
                CovidSharedPreferences.isCountrySubscribed = true
                CovidSharedPreferences.countryName = countriesList.get(position).countryName
                isSubscribed = CovidSharedPreferences.isCountrySubscribed
                countryName = CovidSharedPreferences.countryName
            }
        })
    }

    fun setCountryData(data: List<SingleCountryStats>) {
        this.countriesList = data
        notifyDataSetChanged()
    }
}