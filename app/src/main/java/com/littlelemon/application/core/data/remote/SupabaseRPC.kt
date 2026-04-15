package com.littlelemon.application.core.data.remote

object SupabaseRPC {

    object MakeReservation {
        const val RPC_NAME = "make_reservation"
        const val P_TIME = "p_reservation_time"
        const val P_SIZE = "p_party_size"
        const val P_INSTRUCTIONS = "p_special_instructions"
    }

    object CancelReservation {
        const val RPC_NAME = "cancel_reservation"
        const val P_ID = "p_reservation_id"
    }


}