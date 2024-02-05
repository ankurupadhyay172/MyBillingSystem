package com.billing.mybilling.utils

import androidx.annotation.StringRes
import com.billing.mybilling.R
import com.billing.mybilling.data.model.AppError
import com.google.gson.GsonBuilder
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException


/**
 * Convert Throwable to AppError
 */
fun Throwable?.toAppError(): AppError? {
    return when (this) {
        null -> null
        is AppError -> this
        is AppError.ApiException.NoConnectivityException -> AppError.ApiException.NoConnectivityException(this)
        is TimeoutCancellationException -> AppError.ApiException.NetworkException(this)
        else -> AppError.UnknownException(this)
    }
}



/**
 * Convert AppError to String Resources
 */
@StringRes
fun AppError.stringRes() = when (this) {
    is AppError.ApiException.NetworkException -> R.string.error_network
    is AppError.ApiException.NoConnectivityException -> R.string.error_no_internet_connection
    is AppError.ApiException.ServerException -> R.string.error_server
    is AppError.ApiException.SessionNotFoundException -> R.string.error_unknown
    is AppError.ApiException.UnknownException -> R.string.error_unknown
    is AppError.UnknownException -> R.string.error_unknown
    is AppError.ExternalIntegrationError.NoEmailFoundException -> R.string.no_email_found_error
    else -> R.string.error_unknown
}
