package com.hedgemen.fx.util.tuples;

import java.io.Serializable;
import java.util.*;

public abstract class Tuple implements Iterable<Object>, Serializable, Comparable<Tuple> {
	private static final long serialVersionUID = 5431085632328343101L;

	private final List<Object> valueList;

	protected Tuple(final Object... values) {
		this.valueList = Arrays.asList(values);
	}

	public abstract int getSize();

	public final Object getValue(final int index) {
		if (index >= getSize()) throw new ArrayIndexOutOfBoundsException(index);
		return valueList.get(index);
	}

	public final Iterator<Object> iterator() {
		return this.valueList.iterator();
	}

	@Override
	public final String toString() {
		return this.valueList.toString();
	}

	public final boolean contains(final Object value) {
		for (final Object val : this.valueList) {
			if (val == null) {
				if (value == null) {
					return true;
				}
			} else {
				if (val.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public final boolean containsAll(final Collection<?> collection) {
		for (final Object value : collection) {
			if (!contains(value)) {
				return false;
			}
		}
		return true;
	}

	public final boolean containsAll(final Object... values) {
		if (values == null) {
			throw new IllegalArgumentException("Values array cannot be null");
		}
		for (final Object value : values) {
			if (!contains(value)) {
				return false;
			}
		}
		return true;
	}

	public final int indexOf(final Object value) {
		int i = 0;
		for (final Object val : this.valueList) {
			if (val == null) {
				if (value == null) {
					return i;
				}
			} else {
				if (val.equals(value)) {
					return i;
				}
			}
			i++;
		}
		return -1;
	}

	public final int lastIndexOf(final Object value) {
		for (int i = getSize() - 1; i >= 0; i--) {
			final Object val = this.valueList.get(i);
			if (val == null) {
				if (value == null) {
					return i;
				}
			} else {
				if (val.equals(value)) {
					return i;
				}
			}
		}
		return -1;
	}

	public final List<Object> toList() {
		return Collections.unmodifiableList(new ArrayList<Object>(this.valueList));
	}

	public final Object[] toArray() {
		return valueList.toArray();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.valueList == null) ? 0 : this.valueList.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Tuple other = (Tuple) obj;
		return this.valueList.equals(other.valueList);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compareTo(final Tuple o) {

		final int tLen = valueList.size();
		final List<Object> oValues = o.valueList;
		final int oLen = o.valueList.size();

		for (int i = 0; i < tLen && i < oLen; i++) {

			final Comparable tElement = (Comparable)valueList.get(i);
			final Comparable oElement = (Comparable)oValues.get(i);

			final int comparison = tElement.compareTo(oElement);
			if (comparison != 0) return comparison;
		}

		return (Integer.valueOf(tLen)).compareTo(Integer.valueOf(oLen));
	}
}
