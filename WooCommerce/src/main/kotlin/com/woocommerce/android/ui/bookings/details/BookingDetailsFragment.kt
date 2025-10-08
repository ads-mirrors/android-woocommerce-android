package com.woocommerce.android.ui.bookings.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.woocommerce.android.R
import com.woocommerce.android.extensions.isTwoPanesShouldBeUsed
import com.woocommerce.android.ui.base.BaseFragment
import com.woocommerce.android.ui.bookings.BookingsCommunicationViewModel
import com.woocommerce.android.ui.compose.composeView
import com.woocommerce.android.ui.main.AppBarStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class BookingDetailsFragment : BaseFragment() {

    private val viewModel: BookingDetailsViewModel by viewModels()
    private val navArgs: BookingDetailsFragmentArgs by navArgs()
    private val bookingsCommunicationViewModel: BookingsCommunicationViewModel by activityViewModels()

    override val activityAppBarStatus: AppBarStatus
        get() = AppBarStatus.Hidden

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeView {
            BookingDetailsScreen(
                viewModel = viewModel,
                onBack = { findNavController().popBackStack() },
                onViewOrder = { orderId ->
                    findNavController().navigate(
                        BookingDetailsFragmentDirections
                            .actionBookingDetailsFragmentToOrderDetailFragment(orderId)
                    )
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleOnePaneToTwoPaneConversion()
    }

    private fun handleOnePaneToTwoPaneConversion() {
        val isScreenLargerThanCompact = requireContext().isTwoPanesShouldBeUsed
        val isBookingListFragmentUpInBackStack =
            findNavController().previousBackStackEntry?.destination?.id == R.id.bookingListFragment
        if (isScreenLargerThanCompact && isBookingListFragmentUpInBackStack) {
            when (val mode = navArgs.mode) {
                is Mode.ShowBooking -> {
                    findNavController().popBackStack()
                    bookingsCommunicationViewModel.pushEvent(
                        BookingsCommunicationViewModel.CommunicationEvent.BookingSelected(mode.bookingId)
                    )
                }

                is Mode.Empty -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    @Parcelize
    sealed class Mode : Parcelable {
        @Parcelize
        data object Empty : Mode()

        @Parcelize
        data class ShowBooking(val bookingId: Long) : Mode()
    }
}
