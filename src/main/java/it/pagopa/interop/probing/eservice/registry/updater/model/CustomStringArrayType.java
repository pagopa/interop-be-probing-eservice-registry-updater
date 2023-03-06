/**************************************************************************
*
* Copyright 2023 (C) DXC
*
* Created on  : 6 Mar 2023
* Author      : dxc technology
* Project Name: interop-be-probing-eservice-registry-updater 
* Package     : it.pagopa.interop.probing.eservice.registry.updater.model
* File Name   : CustomStringArrayType.java
*
*-----------------------------------------------------------------------------
* Revision History (Release )
*-----------------------------------------------------------------------------
* VERSION     DESCRIPTION OF CHANGE
*-----------------------------------------------------------------------------
** --/1.0  |  Initial Create.
**---------|------------------------------------------------------------------
***************************************************************************/
package it.pagopa.interop.probing.eservice.registry.updater.model;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * The Class CustomStringArrayType.
 */
public class CustomStringArrayType implements UserType {

	/**
	 * Sql types.
	 *
	 * @return the int[]
	 */
	@Override
	public int[] sqlTypes() {
		return new int[] { Types.ARRAY };
	}

	/**
	 * Returned class.
	 *
	 * @return the class
	 */
	@Override
	public Class<String[]> returnedClass() {
		return String[].class;
	}

	/**
	 * Equals.
	 *
	 * @param o  the o
	 * @param o1 the o 1
	 * @return true, if successful
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public boolean equals(Object o, Object o1) throws HibernateException {
		return Objects.equals(o, o1);
	}

	/**
	 * Hash code.
	 *
	 * @param o the o
	 * @return the int
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public int hashCode(Object o) throws HibernateException {
		return o.hashCode();
	}

	/**
	 * Null safe get.
	 *
	 * @param rs      the rs
	 * @param names   the names
	 * @param session the session
	 * @param owner   the owner
	 * @return the object
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException       the SQL exception
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		Array array = rs.getArray(names[0]);
		return Objects.nonNull(array) ? array.getArray() : null;
	}

	/**
	 * Null safe set.
	 *
	 * @param st      the st
	 * @param value   the value
	 * @param index   the index
	 * @param session the session
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException       the SQL exception
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (Objects.nonNull(st)) {
			if (Objects.nonNull(value)) {
				Array array = session.connection().createArrayOf("varchar", (String[]) value);
				st.setArray(index, array);
			} else {
				st.setNull(index, sqlTypes()[0]);
			}
		}
	}

	/**
	 * Deep copy.
	 *
	 * @param o the o
	 * @return the object
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public Object deepCopy(Object o) throws HibernateException {
		return SerializationUtils.clone((String[]) o);
	}

	/**
	 * Checks if is mutable.
	 *
	 * @return true, if is mutable
	 */
	@Override
	public boolean isMutable() {
		return false;
	}

	/**
	 * Disassemble.
	 *
	 * @param o the o
	 * @return the serializable
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public Serializable disassemble(Object o) throws HibernateException {
		return (Serializable) o;
	}

	/**
	 * Assemble.
	 *
	 * @param serializable the serializable
	 * @param o            the o
	 * @return the object
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public Object assemble(Serializable serializable, Object o) throws HibernateException {
		return serializable;
	}

	/**
	 * Replace.
	 *
	 * @param o  the o
	 * @param o1 the o 1
	 * @param o2 the o 2
	 * @return the object
	 * @throws HibernateException the hibernate exception
	 */
	@Override
	public Object replace(Object o, Object o1, Object o2) throws HibernateException {
		return o;
	}
}
