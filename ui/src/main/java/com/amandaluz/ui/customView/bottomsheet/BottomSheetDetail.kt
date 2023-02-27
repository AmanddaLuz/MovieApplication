package com.amandaluz.ui.customView.bottomsheet

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.amandaluz.ui.R
import com.amandaluz.ui.databinding.BottomSheetDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDetail : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDetailBinding
    private var ivIcon: String? = null
    private var title: String? = null
    private var nota: String? = null
    private var description: String? = null
    private var detail: String? = null
    private var imageButton: Int? = null
    private lateinit var onClickButton: (BottomSheetDialogFragment) -> Unit?
    private lateinit var onClickButtonClose: (BottomSheetDialogFragment) -> Unit?
    private lateinit var onClickButtonFavorite: (BottomSheetDialogFragment) -> Unit?

    override fun getTheme(): Int = R.style.DialogAnimation

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isHideable = false
        dialog.behavior.skipCollapsed = true
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        ivIcon?.let {
            viewTargetDetail(it)
            viewTargetPoster(it)
        }
        setView()
        setActionButton()
        setCloseButton()
        setFavoriteButton()
    }

    private fun setView() {
        binding.run {
            tvNota.text = nota
            tvTitle.text = title.toString()
            tvDescription.text = description
            tvDetails.text = detail
            btnFavorite.setImageDrawable(
                imageButton?.let {
                    ContextCompat.getDrawable(
                        requireContext(),
                        it
                    )
                }
            )
        }
    }

    private fun setSelected() {
        binding.btnFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_favorite_button_selected
            )
        )
    }

    private fun setUnSelected(){
        binding.btnFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_favorite_button_unselected
            )
        )
    }

    fun isFavorite(checked: Boolean){
        if (checked) setSelected()
        else setUnSelected()
    }

    private fun setCloseButton() {
        binding.bottomSheetClose.setOnClickListener {
            try {
                onClickButtonClose.invoke(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            try {
                onClickButtonFavorite.invoke(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setActionButton() {
        binding.btnTrailer.isEnabled = true
        binding.btnTrailer.setOnClickListener {
            try {
                onClickButton.invoke(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun viewTargetDetail(poster_path: String): ViewTarget<ImageView, Drawable>? {
        this.ivIcon = poster_path
        val initPath = "https://image.tmdb.org/t/p/w500"

        return activity?.baseContext?.let {
            Glide.with(it)
                .load(if (poster_path.isEmpty()) R.drawable.placeholder else initPath.plus(poster_path))
                .centerCrop()
                .into(binding.imgDetail)
        }
    }

    fun viewTargetPoster(poster_path: String): ViewTarget<ImageView, Drawable>? {
        this.ivIcon = poster_path
        val initPath = "https://image.tmdb.org/t/p/w500"
        return activity?.baseContext?.let {
            Glide.with(it)
                .load(if (poster_path.isEmpty()) R.drawable.placeholder else initPath.plus(poster_path))
                .centerCrop()
                .into(binding.imgPoster)
        }
    }

    fun setNota(nota: String): BottomSheetDetail {
        this.nota = nota
        return this
    }

    fun setTitle(title: String): BottomSheetDetail {
        this.title = title
        return this
    }

    fun setDescription(description: String): BottomSheetDetail {
        this.description = description
        return this
    }

    fun setDetail(detail: String): BottomSheetDetail {
        this.detail = detail
        return this
    }

    fun setImageButton(Image: Int): BottomSheetDetail {
        this.imageButton = Image
        return this
    }

    fun buttonConfirmAction(onClick: ((BottomSheetDialogFragment) -> Unit?)): BottomSheetDetail {
        this.onClickButton = onClick
        return this
    }

    fun buttonCloseAction(onClick: ((BottomSheetDialogFragment) -> Unit?)): BottomSheetDetail {
        this.onClickButtonClose = onClick
        return this
    }

    fun buttonFavoriteAction(
        onClick: ((BottomSheetDialogFragment) -> Unit?)?): BottomSheetDetail {
        if (onClick != null) {
            this.onClickButtonFavorite = onClick
        }
        return this
    }
}