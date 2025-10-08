package com.woocommerce.android.ui.bookings.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.woocommerce.android.ui.base.BaseFragment
import com.woocommerce.android.ui.compose.composeView
import com.woocommerce.android.ui.main.AppBarStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class BookingDetailsFragment : BaseFragment() {

    private val viewModel: BookingDetailsViewModel by viewModels()

    override val activityAppBarStatus: AppBarStatus
        get() = AppBarStatus.Hidden

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FrameLayout(requireContext()).apply {
            addView(
                composeView {
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
            )
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
