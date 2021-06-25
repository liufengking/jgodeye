package org.jgodeye.common;

public class ExceptionQuietly {

    public static void call(Call c1) {
        call(c1, null, null);
    }

    public static void call(Call c1, Call c2, Call c3) {
        try {
            c1.call();
        } catch (Exception e) {
            if (null != c2) {
                call(c2, null, null);
            }
        } finally {
            if (null != c3) {
                call(c3, null, null);
            }
        }
    }

    public static <T> T invoke(Invoke invoke) {
        try {
            return (T) invoke.invoke();
        } catch (Exception e) {
        }

        return null;
    }
}