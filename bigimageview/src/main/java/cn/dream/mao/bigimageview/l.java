package cn.dream.mao.bigimageview;

import android.util.Log;

import java.util.List;

/**
 * 可以跳转的log类
 * Created by mao on 16-8-5.
 */

public class l {
    public static String s = "";
    private static int i = 3;
    private static final boolean IS_DEBUGGABLE = true;

    private enum LogState {
        INFO, ERROR, ALL;
    }

    public static final LogState CURRENT_STATE = LogState.ALL;

    public static void i(final String msg) {

        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        if (elements.length < 5) {
            Log.v("test", "myLog error elements.length < 5");
        } else {
            s = "at " + elements[i].getClassName() + "." + elements[i].getMethodName() + "(" + elements[i].getClassName().substring(elements[i].getClassName().lastIndexOf(".") + 1, elements[i].getClassName().length()) +
                    ".java:"
                    + elements[i].getLineNumber() + ")";
        }
        switch (CURRENT_STATE) {
            case ALL:
            case INFO:

                Log.v("test", s + "    " + msg);
        }
    }

    public static boolean isDebuggable() {
        return IS_DEBUGGABLE;
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUGGABLE) {
            Log.i(tag, msg);
        }
    }

    public static <T> void printList(String tag, List<T> list) {
        if (IS_DEBUGGABLE) {
            if (list == null || list.isEmpty()) {
                Log.i(tag, "list is empty.");
            } else {
                final int size = list.size();
                for (int i = 0; i < size; i++) {
                    Log.i(tag, "item " + i + ": " + list.get(i));
                }
            }
        }
    }

    public static void printStackTrace(Throwable e) {
        if (IS_DEBUGGABLE) {
            e.printStackTrace();
        }
    }
}

