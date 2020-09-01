package com.hedgemen.fx.util.tuples;

import com.hedgemen.fx.util.tuples.values.IValue0;
import com.hedgemen.fx.util.tuples.values.IValue1;
import com.hedgemen.fx.util.tuples.values.IValue2;
import com.hedgemen.fx.util.tuples.values.IValue3;

public class Quartet<A, B, C, D> extends Tuple implements IValue0<A>, IValue1<B>, IValue2<C>, IValue3<D> {

	private static final long serialVersionUID = 4025L;
	private static final int SIZE = 4;
	private final A val0;
	private final B val1;
	private final C val2;
	private final D val3;

	public Quartet(A value0, B value1, C value2, D value3) {
		super(value0, value1, value2, value3);
		val0 = value0;
		val1 = value1;
		val2 = value2;
		val3 = value3;
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public A getValue0() {
		return val0;
	}

	@Override
	public B getValue1() {
		return val1;
	}

	@Override
	public C getValue2() {
		return val2;
	}

	@Override
	public D getValue3() {
		return val3;
	}
}