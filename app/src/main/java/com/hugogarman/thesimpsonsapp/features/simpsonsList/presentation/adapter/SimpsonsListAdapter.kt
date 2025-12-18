import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hugogarman.thesimpsonsapp.core.presentation.ext.loadUrl
import com.hugogarman.thesimpsonsapp.databinding.ViewItemSimpsonBinding
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson
import com.hugogarman.thesimpsonsapp.features.simpsonsList.presentation.adapter.SimpsonsDiffUtil

class SimpsonsListAdapter : ListAdapter<Simpson, SimpsonsListAdapter.ViewHolder>(SimpsonsDiffUtil()) {

    private var onSimpsonClick: ((Int) -> Unit)? = null

    fun setEvent(onClick: (Int) -> Unit) {
        onSimpsonClick = onClick
    }

    class ViewHolder(private val binding: ViewItemSimpsonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(simpson: Simpson, onSimpsonClick: ((Int) -> Unit)?) {
            binding.apply {
                ivSimpson.loadUrl(simpson.portraitPath)
                tvSimpsonName.text = simpson.name
                tvSimpsonOccupation.text = simpson.occupation

                btChoose.setOnClickListener {
                    onSimpsonClick?.invoke(simpson.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewItemSimpsonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], onSimpsonClick)
    }
}