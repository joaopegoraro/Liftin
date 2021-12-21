package xyz.joaophp.liftin.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.ItemExerciseBinding
import xyz.joaophp.liftin.ui.adapters.ExerciseAdapter.ExerciseViewHolder

class ExerciseAdapter : ListAdapter<Exercise, ExerciseViewHolder>(DiffCallback) {

    private var clickListener: ((Exercise) -> Unit)? = null
    private var deleteClickListener: ((Exercise) -> Unit)? = null

    fun addOnClickListener(clickListener: (Exercise) -> Unit) {
        this.clickListener = clickListener
    }

    fun addOnDeleteClickListener(clickListener: (Exercise) -> Unit) {
        this.deleteClickListener = clickListener
    }

    inner class ExerciseViewHolder(
        private var binding: ItemExerciseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        // Holder binding
        fun bind(exercise: Exercise) {
            binding.tvExerciseName.text = exercise.nome.toString()
            binding.tvExerciseObservation.text = exercise.observacoes

            // Sets on delete click listener
            if (deleteClickListener != null) {
                binding.ivDelete.setOnClickListener {
                    val exerciseIndex = currentList.indexOf(exercise)
                    deleteClickListener?.invoke(exercise)
                    notifyItemChanged(exerciseIndex)
                }
            }

            // Sets the item click listener
            if (clickListener != null) {
                itemView.setOnClickListener { clickListener?.invoke(exercise) }
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the
     * [List] of [Workout] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(
            oldItem: Exercise,
            newItem: Exercise
        ): Boolean {
            return oldItem.nome == newItem.nome && oldItem.observacoes == newItem.observacoes
        }

        override fun areContentsTheSame(
            oldItem: Exercise,
            newItem: Exercise
        ): Boolean {
            return oldItem.imagemUrl == newItem.imagemUrl
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseViewHolder {
        val view = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(view)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)

        // Calls the holder bind method
        holder.bind(exercise)
    }
}