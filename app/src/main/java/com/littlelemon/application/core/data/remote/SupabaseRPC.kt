package com.littlelemon.application.core.data.remote

object SupabaseRPC {

    object UpsertAddress {
        const val RPC_NAME = "upsert_address"
        const val P_LOCATION = "arg_location"
        const val P_CREATED_AT = "arg_created_at"
        const val P_IS_DEFAULT = "arg_is_default"
        const val P_ID = "arg_id"
        const val P_LABEL = "arg_label"
        const val P_BUILDING_NAME = "arg_building_name"
        const val P_STREET_ADDRESS = "arg_street_address"
        const val P_CITY = "arg_city"
        const val P_STATE = "arg_state"
        const val P_PIN_CODE = "arg_pin_code"
    }

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

    object UpdateCart {
        const val RPC_NAME = "upsert_cart_item"
        const val P_DISH_ID = "p_dish_id"
        const val P_QUANTITY = "p_quantity"
    }

    object ClearCart {
        const val RPC_NAME = "clear_cart"
    }

    object GetCartSummary {
        const val RPC_NAME = "get_cart_summary"
    }


}