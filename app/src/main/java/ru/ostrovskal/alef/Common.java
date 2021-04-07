package ru.ostrovskal.alef;

/** Java класс, являющийся контейнером для глобальных переменных и констант */
public final class Common {
    private Common() { }

    /** Признак активности отладки */
    public static boolean isDebug       = false;

    /** Тэг для лога, имя базы данных */
    public static String logTag         = "ALEF";

    /** Путь к папке /data/data/package/cache */
    public static String folderCache    = "";

    /** Путь к папке /data/data/package/files */
    public static String folderFiles	= "";

    /** Путь к папке /data/data/package */
    public static String folderData	    = "";

    /** Имя пакета приложения */
    public static String                namePackage;
};
