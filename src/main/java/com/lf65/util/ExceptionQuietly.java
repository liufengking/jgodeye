package com.lf65.util;

import com.lf65.api.MtCall;
import com.lf65.api.MtInvoke;

public class ExceptionQuietly {

    public static void call(MtCall c1) {
        call(c1, null, null);
    }

    public static void call(MtCall c1, MtCall c2, MtCall c3) {
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

    public static <T> T invoke(MtInvoke invoke) {
        try {
            return (T) invoke.invoke();
        } catch (Exception e) {
        }

        return null;
    }
}