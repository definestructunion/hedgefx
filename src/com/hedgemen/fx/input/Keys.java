package com.hedgemen.fx.input;

public enum Keys{
	Unknown      (-1),
	Space        (32),
	Apostrophe   (39),
	Comma        (44),
	Minus        (45),
	Period       (46),
	Slash        (47),
	N0           (48),
	N1           (49),
	N2           (50),
	N3           (51),
	N4           (52),
	N5           (53),
	N6           (54),
	N7           (55),
	N8           (56),
	N9           (57),
	Semicolon    (59),
	Equal        (61),
	A            (65),
	B            (66),
	C            (67),
	D            (68),
	E            (69),
	F            (70),
	G            (71),
	H            (72),
	I            (73),
	J            (74),
	K            (75),
	L            (76),
	M            (77),
	N            (78),
	O            (79),
	P            (80),
	Q            (81),
	R            (82),
	S            (83),
	T            (84),
	U            (85),
	V            (86),
	W            (87),
	X            (88),
	Y            (89),
	Z            (90),
	BracketLeft  (91),
	Backslash    (92),
	BracketRight (93),
	Grave        (96),
	World1       (161),
	World2       (162),
	
	Escape       (256),
	Enter        (257),
	Tab          (258),
	Backspace    (259),
	Insert       (260),
	Delete       (261),
	Right        (262),
	Left         (263),
	Down         (264),
	Up           (265),
	PageUp       (266),
	PageDown     (267),
	Home         (268),
	End          (269),
	CapsLock     (280),
	ScrollLock   (281),
	NumLock      (282),
	PrintScreen  (283),
	Pause        (284),
	F1           (290),
	F2           (291),
	F3           (292),
	F4           (293),
	F5           (294),
	F6           (295),
	F7           (296),
	F8           (297),
	F9           (298),
	F10          (299),
	F11          (300),
	F12          (301),
	F13          (302),
	F14          (303),
	F15          (304),
	F16          (305),
	F17          (306),
	F18          (307),
	F19          (308),
	F20          (309),
	F21          (310),
	F22          (311),
	F23          (312),
	F24          (313),
	F25          (314),
	KP0          (320),
	KP1          (321),
	KP2          (322),
	KP3          (323),
	KP4          (324),
	KP5          (325),
	KP6          (326),
	KP7          (327),
	KP8          (328),
	KP9          (329),
	KPDec        (330),
	KPDiv        (331),
	KPMult       (332),
	KPSub        (333),
	KPAdd        (334),
	KPEnter      (335),
	KPEqual      (336),
	LeftShift    (340),
	LeftCtrl     (341),
	LeftAlt      (342),
	LeftSuper    (343),
	RightShift   (344),
	RightCtrl    (345),
	RightAlt     (346),
	RightSuper   (347),
	Menu         (348);
	//LENGTH       (119);
	
	public final int value;
	public final char lowChar;
	public final char upChar;
	
	// noShiftChar = ''; shiftChar = ''; break;
	
	Keys(int value)
	{
		this.value = value;
		switch(value)
		{
			case -1: lowChar = '\u0000'; upChar = '\u0000'; break;
			case 32: lowChar = ' '; upChar = ' '; break;
			case 39: lowChar = '\''; upChar = '\"'; break;
			case 44: lowChar = ','; upChar = '<'; break;
			case 45: lowChar = '-'; upChar = '_'; break;
			case 46: lowChar = '.'; upChar = '>'; break;
			case 47: lowChar = '/'; upChar = '?'; break;
			case 48: lowChar = '0'; upChar = ')'; break;
			case 49: lowChar = '1'; upChar = '!'; break;
			case 50: lowChar = '2'; upChar = '@'; break;
			case 51: lowChar = '3'; upChar = '#'; break;
			case 52: lowChar = '4'; upChar = '$'; break;
			case 53: lowChar = '5'; upChar = '%'; break;
			case 54: lowChar = '6'; upChar = '^'; break;
			case 55: lowChar = '7'; upChar = '&'; break;
			case 56: lowChar = '8'; upChar = '*'; break;
			case 57: lowChar = '9'; upChar = '('; break;
			case 59: lowChar = ';'; upChar = ':'; break;
			case 61: lowChar = '='; upChar = '+'; break;
			case 65: lowChar = 'a'; upChar = 'A'; break;
			case 66: lowChar = 'b'; upChar = 'B'; break;
			case 67: lowChar = 'c'; upChar = 'C'; break;
			case 68: lowChar = 'd'; upChar = 'D'; break;
			case 69: lowChar = 'e'; upChar = 'E'; break;
			case 70: lowChar = 'f'; upChar = 'F'; break;
			case 71: lowChar = 'g'; upChar = 'G'; break;
			case 72: lowChar = 'h'; upChar = 'H'; break;
			case 73: lowChar = 'i'; upChar = 'I'; break;
			case 74: lowChar = 'j'; upChar = 'J'; break;
			case 75: lowChar = 'k'; upChar = 'K'; break;
			case 76: lowChar = 'l'; upChar = 'L'; break;
			case 77: lowChar = 'm'; upChar = 'M'; break;
			case 78: lowChar = 'n'; upChar = 'N'; break;
			case 79: lowChar = 'o'; upChar = 'O'; break;
			case 80: lowChar = 'p'; upChar = 'P'; break;
			case 81: lowChar = 'q'; upChar = 'Q'; break;
			case 82: lowChar = 'r'; upChar = 'R'; break;
			case 83: lowChar = 's'; upChar = 'S'; break;
			case 84: lowChar = 't'; upChar = 'T'; break;
			case 85: lowChar = 'u'; upChar = 'U'; break;
			case 86: lowChar = 'v'; upChar = 'V'; break;
			case 87: lowChar = 'w'; upChar = 'W'; break;
			case 88: lowChar = 'x'; upChar = 'X'; break;
			case 89: lowChar = 'y'; upChar = 'Y'; break;
			case 90: lowChar = 'z'; upChar = 'Z'; break;
			case 91: lowChar = '['; upChar = '{'; break;
			case 92: lowChar = '\\'; upChar = '|'; break;
			case 93: lowChar = ']'; upChar = '}'; break;
			case 96: lowChar = '`'; upChar = '~'; break;
			case 257: lowChar = '\n'; upChar = '\n'; break;
			//case 258: noShiftChar = '\b'; shiftChar = '\b'; break; // will almost certainly not work as expected of a backspace
			case 320: lowChar = '0'; upChar = '0'; break;
			case 321: lowChar = '1'; upChar = '1'; break;
			case 322: lowChar = '2'; upChar = '2'; break;
			case 323: lowChar = '3'; upChar = '3'; break;
			case 324: lowChar = '4'; upChar = '4'; break;
			case 325: lowChar = '5'; upChar = '5'; break;
			case 326: lowChar = '6'; upChar = '6'; break;
			case 327: lowChar = '7'; upChar = '7'; break;
			case 328: lowChar = '8'; upChar = '8'; break;
			case 329: lowChar = '9'; upChar = '9'; break;
			case 330: lowChar = '0'; upChar = '0'; break;
			default: lowChar = '\u0000'; upChar = '\u0000'; break;
		}
	}
}