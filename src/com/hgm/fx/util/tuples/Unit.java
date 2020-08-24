package com.hgm.fx.util.tuples;

import com.hgm.fx.util.tuples.values.IValue0;

public final class Unit<A> extends Tuple implements IValue0<A> {

	private static final long serialVersionUID = 1025L;
	private static final int SIZE = 1;
	private final A val0;

	public Unit(final A value0) {
		super(value0);
		this.val0 = value0;
	}

	public A getValue0() {
		return this.val0;
	}

	@Override
	public int getSize() {
		return SIZE;
	}
}