package com.daniil.shevtsov.timetravel.core.navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.daniil.shevtsov.timetravel.R
import com.daniil.shevtsov.timetravel.application.TimeTravelGameApplication
import com.daniil.shevtsov.timetravel.databinding.FragmentMainBinding
import com.google.accompanist.insets.ProvideWindowInsets
import javax.inject.Inject

class ScreenHostFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ScreenHostViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as TimeTravelGameApplication)
            .appComponent
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            composeView.setContent {
                ProvideWindowInsets {
                    ScreenHostComposable(viewModel = viewModel)
                }
            }
        }
    }
}
