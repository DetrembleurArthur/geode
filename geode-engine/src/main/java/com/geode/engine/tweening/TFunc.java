package com.geode.engine.tweening;

import static java.lang.Math.*;

public class TFunc
{

	public static final TweenFunction LINEAR = (x) -> {
		return x;
	};
	public static final TweenFunction EASE_IN_SINE = (x) -> {
		return 1 - (float)Math.cos((x * PI) / 2);
	};

	public static final TweenFunction EASE_OUT_SINE = (x) -> {
		return (float)Math.sin((x * PI) / 2);
	};

	public static final TweenFunction EASE_IN_CUBIC = (x) -> {
		return x * x * x;
	};

	public static final TweenFunction EASE_OUT_CUBIC = (x) -> {
		return 1 - (float)pow(1 - x, 3);
	};

	public static final TweenFunction EASE_IN_OUT_CUBIC = (x) -> {
		return x < 0.5 ? 4 * x * x * x : 1 - (float)pow(-2 * x + 2, 3) / 2;
	};

	public static final TweenFunction EASE_IN_QUINT = (x) -> {
		return x * x * x * x * x;
	};

	public static final TweenFunction EASE_OUT_QUINT = (x) -> {
		return 1 - (float)pow(1 - x, 5);
	};

	public static final TweenFunction EASE_IN_OUT_QUINT = (x) -> {
		return x < 0.5 ? 16 * x * x * x * x * x : 1 - (float) pow(-2 * x + 2, 5) / 2;
	};

	public static final TweenFunction EASE_IN_CIRC = (x) -> {
		return 1 - (float)sqrt(1 - pow(x, 2));
	};

	public static final TweenFunction EASE_OUT_CIRC = (x) -> {
		return (float)sqrt(1 - pow(x - 1, 2));
	};

	public static final TweenFunction EASE_IN_OUT_CIRC = (x) -> {
		return x < 0.5
				? (float)(1 - sqrt(1 - pow(2 * x, 2))) / 2
				: (float)(sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2;
	};

	public static final TweenFunction EASE_IN_ELASTIC = (x) -> {
		final float c4 = (float)(2 * Math.PI) / 3;
		return x == 0
				? 0
				: x == 1
				? 1
				: (float)-pow(2, 10 * x - 10) * (float)sin((x * 10 - 10.75) * c4);
	};

	public static final TweenFunction EASE_OUT_ELASTIC = (x) -> {
		final float c4 = (float)(2 * Math.PI) / 3;
		return x == 0
				? 0
				: x == 1
				? 1
				: (float)pow(2, -10 * x) * (float)sin((x * 10 - 0.75) * c4) + 1;
	};

	public static final TweenFunction EASE_IN_OUT_ELASTIC = (x) -> {
		final float c5 = (float)(2 * Math.PI) / 4.5f;
		return x == 0
				? 0
				: x == 1
				? 1
				: x < 0.5
				? -(float)(pow(2, 20 * x - 10) * sin((20 * x - 11.125) * c5)) / 2
				: (float)(pow(2, -20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2 + 1;
	};

	public static final TweenFunction EASE_IN_QUAD = (x) -> {
		return x * x;
	};

	public static final TweenFunction EASE_OUT_QUAD = (x) -> {
		return 1 - (1 - x) * (1 - x);
	};

	public static final TweenFunction EASE_IN_OUT_QUAD = (x) -> {
		return x < 0.5 ? 2 * x * x : 1 - (float)pow(-2 * x + 2, 2) / 2;
	};

	public static final TweenFunction EASE_IN_QUART = (x) -> {
		return x * x * x * x;
	};

	public static final TweenFunction EASE_OUT_QUART = (x) -> {
		return 1 - (float)pow(1 - x, 4);
	};

	public static final TweenFunction EASE_IN_OUT_QUART = (x) -> {
		return x < 0.5 ? 8 * x * x * x * x : 1 - (float)pow(-2 * x + 2, 4) / 2;
	};

	public static final TweenFunction EASE_IN_EXPO = (x) -> {
		return x == 0 ? 0 : (float)pow(2, 10 * x - 10);
	};

	public static final TweenFunction EASE_OUT_EXPO = (x) -> {
		return x == 1 ? 1 : 1 - (float)pow(2, -10 * x);
	};

	public static final TweenFunction EASE_IN_OUT_EXPO = (x) -> {
		return x == 0
				? 0
				: x == 1
				? 1
				: x < 0.5 ? (float)pow(2, 20 * x - 10) / 2
				: (float)(2 - pow(2, -20 * x + 10)) / 2;
	};

	public static final TweenFunction EASE_IN_BACK = (x) -> {
		final float c1 = 1.70158f;
		final float c3 = c1 + 1;
		return c3 * x * x * x - c1 * x * x;
	};

	public static final TweenFunction EASE_OUT_BACK = (x) -> {
		final float c1 = 1.70158f;
		final float c3 = c1 + 1;
		return 1 + c3 * (float)pow(x - 1, 3) + c1 * (float)pow(x - 1, 2);
	};

	public static final TweenFunction EASE_IN_OUT_BACK = (x) -> {
		final float c1 = 1.70158f;
		final float c2 = c1 + 1.525f;
		return x < 0.5
				? (float)(pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
				: (float)(pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
	};



	public static final TweenFunction EASE_OUT_BOUNCE = (x) -> {
		final float n1 = 7.5625f;
		final float d1 = 2.75f;
		if (x < 1 / d1) {
			return n1 * x * x;
		} else if (x < 2 / d1) {
			return n1 * (x -= 1.5 / d1) * x + 0.75f;
		} else if (x < 2.5 / d1) {
			return n1 * (x -= 2.25 / d1) * x + 0.9375f;
		} else {
			return n1 * (x -= 2.625 / d1) * x + 0.984375f;
		}
	};

	public static final TweenFunction EASE_IN_BOUNCE = (x) -> {
		return 1 - (float)EASE_OUT_BOUNCE.f(1 - x);
	};

	public static final TweenFunction EASE_IN_OUT_BOUNCE = (x) -> {
		return x < 0.5
				? (1 - EASE_OUT_BOUNCE.f(1 - 2 * x)) / 2
				: (1 + EASE_OUT_BOUNCE.f(2 * x - 1)) / 2;
	};

	public static final TweenFunction TEST = (x) -> {
		return x;
	};
}
