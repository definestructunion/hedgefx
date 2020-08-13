package hedge.fx.util.tuples;

import hedge.fx.util.tuples.values.IValue0;
import hedge.fx.util.tuples.values.IValue1;

public class Pair<A, B> extends Tuple implements IValue0<A>, IValue1<B> {

	private static final long serialVersionUID = 2025L;
	private static final int SIZE = 2;
	private final A val0;
	private final B val1;

	public Pair(A value0, B value1) {
		super(value0, value1);
		val0 = value0;
		val1 = value1;
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
}
