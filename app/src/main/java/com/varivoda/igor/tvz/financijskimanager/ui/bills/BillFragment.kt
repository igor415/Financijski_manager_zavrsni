package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.*
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.fragment_bill.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BillFragment : Fragment() {

    private val billViewModel by viewModels<BillViewModel> {
        BillViewModelFactory((requireContext().applicationContext as App).billRepository,
            (requireContext().applicationContext as App).employeeRepository, (requireContext().applicationContext as App).productRepository, requireContext())
    }
    private lateinit var argsText: String
    private var billAdapter: BillAdapter = BillAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = BillFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)

        argsText = args.text
        val view = inflater.inflate(R.layout.fragment_bill, container, false)
        when(args.text){
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                adjustLayout(view)
                monthAndYear(view)
            }
            "Prodavač koji je najviše dana u godini izdao račun" -> {
                adjustLayout(view)
                onlyYear(view)
                observeEmployeeMostDaysIssuedInvoice()
            }
            "Račun sa najviše stavki po godini" -> {
                adjustLayout(view)
                onlyYear(view)
                observeMostItemsOnBill()
                billViewModel.yearSelected.value = getCurrentYear()
            }
            "Proizvod koji ima najmanji udio u ukupnoj prodaji po mjesecu" -> {
                adjustLayout(view)
                monthAndYear(view)
                observeProductSmallestShare()
                billViewModel.productSmallestShare(getCurrentMonth(), getCurrentYear())
            }

        }
        view.billsRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billAdapter
        }
        return view
    }

    private fun observeMostItemsOnBill() {
        billViewModel.mostItemsOnBill.observe(viewLifecycleOwner, Observer {
            if(it==null){
                resultTextView.text = getString(R.string.no_data_for_year)
            }else{
                resultTextView.text = it
            }
        })
    }

    private fun observeProductSmallestShare(){
        billViewModel.productSmallestShareResult.observe(viewLifecycleOwner, Observer {
            if(it==null) {
                resultTextView.text = getString(R.string.no_data_for_year)
            }
            resultTextView.text = it
        })
    }
    private fun observeEmployeeMostDaysIssuedInvoice() {
        billViewModel.employeeInvoiceNumberOfDays.observe(viewLifecycleOwner, Observer {
            if(it==null) {
                resultTextView.text = getString(R.string.no_data_for_year)
            }
            resultTextView.text = it
        })
    }

    private fun adjustLayout(view: View) {
        view.billsRecyclerview.visibility = View.GONE
        view.header.visibility = View.GONE
        view.resultTextView.visibility = View.VISIBLE
    }

    private fun monthAndYear(view: View){
        view.timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        view.changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }
    }

    private fun onlyYear(view: View){
        view.timePeriod.text = getString(R.string.time_period, getCurrentYear())
        view.changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity,changeYear)
        }
    }


    private val changeDate: (month: Int, year: Int) -> Unit = {
        month, year ->
        when(argsText) {
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = billViewModel.getEmployeeTotalPerMonthAndYear(getMonthWithZero(month),year.toString())
                    if(result == null) {
                        resultTextView.text = getString(R.string.no_data_for_month_and_year)
                    }else{
                        resultTextView.text = result
                    }
                }
            }
            "Popis računa" ->{
                billViewModel.getBills(getMonthWithZero(month),year.toString()).observe(viewLifecycleOwner,
                    Observer {
                        if(it==null) return@Observer
                        billAdapter.setItems(it)
                    })
            }
            "Proizvod koji ima najmanji udio u ukupnoj prodaji po mjesecu" -> {
                billViewModel.productSmallestShare(getMonthWithZero(month),year.toString())
            }


        }

        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted(month,year))
    }

    private val changeYear: (year: Int) -> Unit = {
        year ->
        when(argsText){
            "Prodavač koji je najviše dana u godini izdao račun" -> {
                billViewModel.getEmployeeMostDaysIssuedInvoice(year.toString())
            }
            "Račun sa najviše stavki po godini" -> {
                billViewModel.yearSelected.value = year.toString()
            }
        }
        timePeriod.text = getString(R.string.time_period,year.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

}