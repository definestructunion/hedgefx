package com.hgm.fx.util.tuples;

import com.hgm.fx.util.tuples.values.IValue0;
import com.hgm.fx.util.tuples.values.IValue1;
import com.hgm.fx.util.tuples.values.IValue2;

public class Triplet<A, B, C> extends Tuple implements IValue0<A>, IValue1<B>, IValue2<C> {
	
	private static final long serialVersionUID = 3025L;
	private static final int SIZE = 3;
	private final A val0;
	private final B val1;
	private final C val2;
	
	public Triplet(A value0, B value1, C value2) {
		super(value0, value1, value2);
		val0 = value0;
		val1 = value1;
		val2 = value2;
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
}