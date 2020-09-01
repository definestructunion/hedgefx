package com.hedgemen.fx.input;

public enum Buttons {
	B1           (0),
	B2           (1),
	B3           (2),
	B4           (3),
	B5           (4),
	B6           (5),
	B7           (6),
	B8           (7),
	Last         (7), // B8
	Left         (0), // B1
	Right        (1), // B2
	Middle       (2); // B3
	//LENGTH       (12);
	
	public final int value;
	
	Buttons(int value) {
		this.value = value;
	}
}