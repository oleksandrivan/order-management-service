/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq.keys


import com.uoc.jooq.tables.Address
import com.uoc.jooq.tables.Customer
import com.uoc.jooq.tables.Order
import com.uoc.jooq.tables.Orderitem
import com.uoc.jooq.tables.records.AddressRecord
import com.uoc.jooq.tables.records.CustomerRecord
import com.uoc.jooq.tables.records.OrderRecord
import com.uoc.jooq.tables.records.OrderitemRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val KEY_ADDRESS_PRIMARY: UniqueKey<AddressRecord> = Internal.createUniqueKey(Address.ADDRESS, DSL.name("KEY_Address_PRIMARY"), arrayOf(Address.ADDRESS.ID), true)
val KEY_CUSTOMER_PRIMARY: UniqueKey<CustomerRecord> = Internal.createUniqueKey(Customer.CUSTOMER, DSL.name("KEY_Customer_PRIMARY"), arrayOf(Customer.CUSTOMER.ID), true)
val KEY_ORDER_PRIMARY: UniqueKey<OrderRecord> = Internal.createUniqueKey(Order.ORDER, DSL.name("KEY_Order_PRIMARY"), arrayOf(Order.ORDER.ID), true)
val KEY_ORDERITEM_PRIMARY: UniqueKey<OrderitemRecord> = Internal.createUniqueKey(Orderitem.ORDERITEM, DSL.name("KEY_OrderItem_PRIMARY"), arrayOf(Orderitem.ORDERITEM.ORDERID, Orderitem.ORDERITEM.PRODUCTID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val ORDER_IBFK_1: ForeignKey<OrderRecord, CustomerRecord> = Internal.createForeignKey(Order.ORDER, DSL.name("Order_ibfk_1"), arrayOf(Order.ORDER.CUSTOMERID), com.uoc.jooq.keys.KEY_CUSTOMER_PRIMARY, arrayOf(Customer.CUSTOMER.ID), true)
val ORDER_IBFK_2: ForeignKey<OrderRecord, AddressRecord> = Internal.createForeignKey(Order.ORDER, DSL.name("Order_ibfk_2"), arrayOf(Order.ORDER.SHIPPINGADDRESS), com.uoc.jooq.keys.KEY_ADDRESS_PRIMARY, arrayOf(Address.ADDRESS.ID), true)
val ORDERITEM_IBFK_1: ForeignKey<OrderitemRecord, OrderRecord> = Internal.createForeignKey(Orderitem.ORDERITEM, DSL.name("OrderItem_ibfk_1"), arrayOf(Orderitem.ORDERITEM.ORDERID), com.uoc.jooq.keys.KEY_ORDER_PRIMARY, arrayOf(Order.ORDER.ID), true)
