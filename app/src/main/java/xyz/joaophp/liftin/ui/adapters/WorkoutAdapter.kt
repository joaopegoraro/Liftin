package xyz.joaophp.liftin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.ItemWorkoutBinding
import xyz.joaophp.liftin.ui.adapters.WorkoutAdapter.WorkoutViewHolder

class WorkoutAdapter : ListAdapter<Workout, WorkoutViewHolder>(DiffCallback) {

    private var clickListener: ((Workout) -> Unit)? = null
    private var deleteClickListener: ((Workout) -> Unit)? = null

    fun addOnClickListener(clickListener: (Workout) -> Unit) {
        this.clickListener = clickListener
    }

    fun addOnDeleteClickListener(clickListener: (Workout) -> Unit) {
        this.deleteClickListener = clickListener
    }

    inner class WorkoutViewHolder(
        private var binding: ItemWorkoutBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        // Holder binding
        fun bind(workout: Workout) {
            binding.tvWorkoutName.text = workout.nome.toString()
            binding.tvWorkoutDescription.text = workout.descricao

            // Sets on delete click listener
            if (deleteClickListener != null) {
                binding.ivDelete.setOnClickListener {
                    val workoutIndex = currentList.indexOf(workout)
                    deleteClickListener?.invoke(workout)
                    notifyItemChanged(workoutIndex)
                }
            }

            // Sets the item click listener
            if (clickListener != null) {
                itemView.setOnClickListener { clickListener?.invoke(workout) }
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the
     * [List] of [Workout] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(
            oldItem: Workout,
            newItem: Workout
        ): Boolean {
            return oldItem.nome == newItem.nome && oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(
            oldItem: Workout,
            newItem: Workout
        ): Boolean {
            return oldItem.descricao == newItem.descricao
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {
        val view = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(view)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)

        // Calls the holder bind method
        holder.bind(workout)
    }
}