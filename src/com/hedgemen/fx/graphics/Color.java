package com.hedgemen.fx.graphics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hedgemen.fx.util.copying.Copy;

import java.text.DecimalFormat;

public class Color implements Copy<Color> {
	
	public static Color red() { return new Color(0.902f, 0.098f, 0.294f, 1.0f); }
	public static Color green() { return new Color(0.23529412f, 0.7058824f, 0.29411766f, 1.0f); }
	public static Color yellow() { return new Color(1.0f, 0.88235295f, 0.09803922f, 1.0f); }
	public static Color blue() { return new Color(0.0f, 0.50980395f, 0.78431374f, 1.0f); }
	public static Color orange() { return new Color(0.9607843f, 0.50980395f, 0.1882353f, 1.0f); }
	public static Color purple() { return new Color(0.5686275f, 0.11764706f, 0.7058824f, 1.0f); }
	public static Color cyan() { return new Color(0.27450982f, 0.9411765f, 0.9411765f, 1.0f); }
	public static Color magenta() { return new Color(0.9411765f, 0.19607843f, 0.9019608f, 1.0f); }
	public static Color lime() { return new Color(0.8235294f, 0.9607843f, 0.23529412f, 1.0f); }
	public static Color pink() { return new Color(0.98039216f, 0.74509805f, 0.74509805f, 1.0f); }
	public static Color teal() { return new Color(0.0f, 0.5019608f, 0.5019608f, 1.0f); }
	public static Color lavender() { return new Color(0.9019608f, 0.74509805f, 1.0f, 1.0f); }
	public static Color brown() { return new Color(0.6666667f, 0.43137255f, 0.15686275f, 1.0f); }
	public static Color beige() { return new Color(1.0f, 0.98039216f, 0.78431374f, 1.0f); }
	public static Color maroon() { return new Color(0.5019608f, 0.0f, 0.0f, 1.0f); }
	public static Color mint() { return new Color(0.6666667f, 1.0f, 0.7647059f, 1.0f); }
	public static Color olive() { return new Color(0.5019608f, 0.5019608f, 0.0f, 1.0f); }
	public static Color apricot() { return new Color(1.0f, 0.84313726f, 0.7058824f, 1.0f); }
	public static Color navy() { return new Color(0.0f, 0.0f, 0.5019608f, 1.0f); }
	public static Color grey() { return new Color(0.5019608f, 0.5019608f, 0.5019608f, 1.0f); }
	public static Color white() { return new Color(1.0f, 1.0f, 1.0f, 1.0f); }
	public static Color black() { return new Color(0.0f, 0.0f, 0.0f, 1.0f); }
	public static Color darkGrey() { return new Color(0.15f, 0.15f, 0.15f, 1.0f); }
	
	/*@JsonIgnore
	public float r;
	@JsonIgnore
	public float g;
	@JsonIgnore
	public float b;
	@JsonIgnore
	public float a;
	@JsonProperty("hex")
	public float hex;*/
	
	@JsonIgnore
	public float r;
	@JsonIgnore
	public float g;
	@JsonIgnore
	public float b;
	@JsonIgnore
	public float a;
	@JsonProperty("hex")
	public float hex;
	
	@JsonCreator
	public Color(@JsonProperty("hex") int rgbaHex) {
		hex = Float.intBitsToFloat(rgbaHex);
		r = ((rgbaHex & 0xff000000) >>> 24) / 255f;
		g = ((rgbaHex & 0x00ff0000) >>> 16) / 255f;
		b = ((rgbaHex & 0x0000ff00) >>> 8) / 255f;
		a = ((rgbaHex & 0x000000ff)) / 255f;
	}
	
	public Color(float r, float g, float b, float a) {
		r = clamp(r);
		g = clamp(g);
		b = clamp(b);
		a = clamp(a);
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		hex = Float.intBitsToFloat(((int)(255 * a) << 24) | ((int)(255 * b) << 16) | ((int)(255 * g) << 8) | ((int)(255 * r)));
		//hex = Float.intBitsToFloat(((int)(255 * r) << 24) | ((int)(255 * g) << 16) | ((int)(255 * b) << 8) | ((int)(255 * a)));
	}
	
	public int hexInt() {
		return Float.floatToIntBits(hex);
	}
	
	public String hexNotation() {
		return Integer.toHexString(hexInt());
	}
	
	private float clamp(float value) {
		if(value < 0.0f) return 0.0f;
		if(value > 1.0f) return 1.0f;
		return value;
	}
	
	@Override
	public int hashCode() {
		int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
		result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
		result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
		result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Color)) return false;
		Color color = (Color)obj;
		return hex == color.hex;
	}
	
	@Override
	public String toString() {
		// maybe make this static to avoid allocations
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(3);
		df.setMaximumFractionDigits(3);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<color,hex:")
				.append(hexNotation())
				.append(",r:")
				.append(df.format(r))
				.append(",g:")
				.append(df.format(g))
				.append(",b:")
				.append(df.format(b))
				.append(",a:")
				.append(df.format(a))
				.append('>');
		return builder.toString();
	}
	
	@Override
	public Color deepCopy() {
		return new Color(r, g, b, a);
	}
	
	@Override
	public Color shallowCopy() {
		return new Color(r, g, b, a);
	}
}