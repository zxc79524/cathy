package idv.blake.cathy.util;

public class StringUtil {
	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0 || value.trim().length() == 0;
	}

	public static boolean isEmpty(String... values) {

		for (String value : values) {
			if (isEmpty(value)) {
				return true;
			}

		}

		return false;
	}
}
