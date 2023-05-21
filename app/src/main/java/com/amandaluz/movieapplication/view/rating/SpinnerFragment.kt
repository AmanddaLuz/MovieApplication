package com.amandaluz.movieapplication.view.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amandaluz.movieapplication.R
import com.amandaluz.movieapplication.data.database.ratingdomain.*
import com.amandaluz.movieapplication.databinding.FragmentSpinnerBinding
import com.amandaluz.ui.listener.CustomerHistoryListener
import com.amandaluz.ui.listener.CustomerHistoryListenerImpl

class SpinnerFragment : Fragment() {
    private lateinit var binding : FragmentSpinnerBinding
    private var selectedReason : ReasonDomain? = null
    private var selectedOption : String? = null
    private lateinit var listener : CustomerHistoryListener

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentSpinnerBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        listener = CustomerHistoryListenerImpl(requireContext())
        setupButton()
        setupViews(
            CustomerDomain(
                "01" ,
                OptionDomain(
                    listOf(
                        CustomerButtonDomain(
                            "Enviar" ,
                            "" ,
                            false ,
                            "" ,
                            ""
                        )
                    ) ,
                    "" ,
                    "Classifique o filme" ,
                    InputsDomain(
                        listOf(
                            DropdownBoxDomain(
                                1 ,
                                "Crítica" ,
                                listOf(
                                    ReasonDomain(
                                        "1" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "2" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "3" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "4" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "5" ,
                                        "" ,
                                        ""
                                    ) ,
                                )
                            ) ,
                            DropdownBoxDomain(
                                1 ,
                                "Elogio" ,
                                listOf(
                                    ReasonDomain(
                                        "6" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "7" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "8" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "9" ,
                                        "" ,
                                        ""
                                    ) ,
                                    ReasonDomain(
                                        "10" ,
                                        "" ,
                                        ""
                                    ) ,
                                )
                            )
                        ) ,
                        DropdownBox1Domain(
                            "O que deseja fazer?" ,
                            "Selecione uma opção."
                        ) ,
                        DropdownBox2Domain(
                            "Dê a sua nota!" ,
                            "Selecione uma opção."
                        ) ,
                        CustomerTextBoxDomain(
                            "Nos conte um pouco mais sobre sua experiência" ,
                            7 ,
                            1 ,
                            "Escreva aqui..."
                        ) ,
                    ) ,
                    "Sua avaliação é muito importante para nós!" ,
                    "" ,
                    "Aqui você pode classificar nos enviando uma crítica ou elogio." ,
                    "url"
                ) ,
                2
            )
        )
    }

    private fun setupViews(
        response : CustomerDomain
    ) = with(binding.includeCustomerLayout) {
        setupToolbar(response)
        setupTitleEndDescription(response)
        setupTitleSpinners(response)
        setupOptionSpinner(response)
        setupTitleHistory(response)
        setupButtonName(response)
    }

    private fun setupToolbar(response : CustomerDomain) = with(binding.includeCustomerLayout.toolbar){
        title = response.option.headerTitle
        setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        textAlignment= View.TEXT_ALIGNMENT_CENTER
        setOnClickListener {
            this@SpinnerFragment.findNavController().popBackStack()
        }
    }

    private fun setupButton() = with(binding.includeCustomerLayout) {
        listener.setupHistory(
            editTextHistory ,
            btnContinue
        )

        btnContinue.setProgressButtonClickListener {
            btnContinue.setDisabled()
        }
    }


    private fun setupOptionSpinner(
        response : CustomerDomain
    ) {
        val labels = response.option.inputs.dropdownBox.map { it.label }.sorted()
        val reasons = response.option.inputs.dropdownBox.map { it.reason }
        with(binding.includeCustomerLayout.optionSpinner) {
            setList(labels , R.string.hint_spinner)
            getSpinnerView().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent : AdapterView<*>? ,
                    view : View? ,
                    position : Int ,
                    id : Long
                ) {
                    if (position > 0) {
                        selectedOption = labels[position - 1]
                        setupCategorySpinner(reasons[position - 1])
                    } else {
                        emptyCategorySpinner()
                    }
                }

                override fun onNothingSelected(p0 : AdapterView<*>?) {}
            }
        }
    }

    private fun setupCategorySpinner(reasons : List<ReasonDomain>) {
        val categories = reasons.map { it.label }
        with(binding.includeCustomerLayout.categorySpinner) {
            setList(categories , R.string.hint_spinner)
            getSpinnerView().onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent : AdapterView<*>? ,
                        view : View? ,
                        position : Int ,
                        id : Long
                    ) {
                        if (position > 0) {
                            val categorySelected = categories[position - 1]
                            selectedReason = reasons.firstOrNull { it.label == categorySelected }
                            listener.setReasonSelected()
                        } else {
                            listener.unsetReasonSelected()
                        }
                    }

                    override fun onNothingSelected(p0 : AdapterView<*>?) {}
                }
        }
    }

    private fun emptyCategorySpinner() {
        binding.includeCustomerLayout.categorySpinner.emptyList()
        selectedReason = null
        selectedOption = null
        listener.unsetReasonSelected()

    }

    private fun setupButtonName(
        response : CustomerDomain
    ) = with(binding.includeCustomerLayout) {
        val title = response.option.buttons.firstOrNull()?.name ?: ""
        btnContinue.setLayout(title , title , title)
        btnContinue.setDisabled()
    }

    private fun setupTitleHistory(
        response : CustomerDomain
    ) = with(binding.includeCustomerLayout) {
        titleHistoryCustomer.text = response.option.inputs.textBox.label
        textInputHistory.hint = response.option.inputs.textBox.placeholder
    }

    private fun setupTitleSpinners(
        response : CustomerDomain
    ) = with(binding.includeCustomerLayout) {
        titleSpinnerCategories.text = response.option.inputs.dropdownBox1.label
        titleSpinnerOptions.text = response.option.inputs.dropdownBox2.label
    }

    private fun setupTitleEndDescription(
        response : CustomerDomain
    ) = with(binding.includeCustomerLayout) {
        tvCustomerTitle.text = response.option.name
        tvCustomerDescription.text = response.option.text
    }

/*    private fun setShimmerVisibility(visibility : Boolean) {
        binding.includeShimmer.customerServiceShimmer.run {
            isVisible = visibility
            if (visibility) {
                startShimmerAnimation()
            } else {
                stopShimmerAnimation()
                binding.flipper.displayedChild = 1
            }
        }
    }*/
}