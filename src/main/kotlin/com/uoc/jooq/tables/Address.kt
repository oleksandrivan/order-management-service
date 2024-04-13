/*
 * This file is generated by jOOQ.
 */
package com.uoc.jooq.tables


import com.uoc.jooq.Db
import com.uoc.jooq.keys.KEY_ADDRESS_PRIMARY
import com.uoc.jooq.keys.ORDER_IBFK_2
import com.uoc.jooq.tables.Order.OrderPath
import com.uoc.jooq.tables.records.AddressRecord

import kotlin.collections.Collection

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Address(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, AddressRecord>?,
    parentPath: InverseForeignKey<out Record, AddressRecord>?,
    aliased: Table<AddressRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<AddressRecord>(
    alias,
    Db.DB,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>db.Address</code>
         */
        val ADDRESS: Address = Address()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<AddressRecord> = AddressRecord::class.java

    /**
     * The column <code>db.Address.id</code>.
     */
    val ID: TableField<AddressRecord, Int?> = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>db.Address.street</code>.
     */
    val STREET: TableField<AddressRecord, String?> = createField(DSL.name("street"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>db.Address.city</code>.
     */
    val CITY: TableField<AddressRecord, String?> = createField(DSL.name("city"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>db.Address.state</code>.
     */
    val STATE: TableField<AddressRecord, String?> = createField(DSL.name("state"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>db.Address.postalCode</code>.
     */
    val POSTALCODE: TableField<AddressRecord, String?> = createField(DSL.name("postalCode"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    /**
     * The column <code>db.Address.country</code>.
     */
    val COUNTRY: TableField<AddressRecord, String?> = createField(DSL.name("country"), SQLDataType.VARCHAR(255).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<AddressRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<AddressRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<AddressRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>db.Address</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>db.Address</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>db.Address</code> table reference
     */
    constructor(): this(DSL.name("Address"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, AddressRecord>?, parentPath: InverseForeignKey<out Record, AddressRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, ADDRESS, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class AddressPath : Address, Path<AddressRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, AddressRecord>?, parentPath: InverseForeignKey<out Record, AddressRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<AddressRecord>): super(alias, aliased)
        override fun `as`(alias: String): AddressPath = AddressPath(DSL.name(alias), this)
        override fun `as`(alias: Name): AddressPath = AddressPath(alias, this)
        override fun `as`(alias: Table<*>): AddressPath = AddressPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Db.DB
    override fun getPrimaryKey(): UniqueKey<AddressRecord> = KEY_ADDRESS_PRIMARY

    private lateinit var _order: OrderPath

    /**
     * Get the implicit to-many join path to the <code>db.Order</code> table
     */
    fun order(): OrderPath {
        if (!this::_order.isInitialized)
            _order = OrderPath(this, null, ORDER_IBFK_2.inverseKey)

        return _order;
    }

    val order: OrderPath
        get(): OrderPath = order()
    override fun `as`(alias: String): Address = Address(DSL.name(alias), this)
    override fun `as`(alias: Name): Address = Address(alias, this)
    override fun `as`(alias: Table<*>): Address = Address(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Address = Address(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Address = Address(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Address = Address(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Address = Address(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Address = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Address = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Address = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Address = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Address = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Address = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Address = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Address = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Address = where(DSL.notExists(select))
}
