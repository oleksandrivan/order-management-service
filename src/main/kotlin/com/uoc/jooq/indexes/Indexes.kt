/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq.indexes


import com.uoc.jooq.tables.Order

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val ORDER_CUSTOMERID: Index = Internal.createIndex(DSL.name("customerId"), Order.ORDER, arrayOf(Order.ORDER.CUSTOMERID), false)
val ORDER_SHIPPINGADDRESS: Index = Internal.createIndex(DSL.name("shippingAddress"), Order.ORDER, arrayOf(Order.ORDER.SHIPPINGADDRESS), false)
