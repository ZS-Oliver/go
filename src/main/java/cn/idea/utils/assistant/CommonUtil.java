package cn.idea.utils.assistant;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import com.google.common.collect.Streams;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 自己写的一些工具
 */
@Log4j2
public final class CommonUtil {
    // 冒号分隔器
    private static final Splitter colonSplitter = Splitter.on(':').trimResults().omitEmptyStrings();

    // 日期校验
    private static final Predicate<String> dtChecker = Pattern.compile("2[0-9]{3}(([0][1-9])|([1][0-2]))((0[1-9]|[1-2][0-9]|3[0-1]))").asPredicate();

    private static final int SECOND_OF_DAY = 24 * 60 * 60;

    private static final int BEIJING_DELTA = 28800; // 北京早于unix标准时间的秒数

    private static final ThreadLocal<SimpleDateFormat> dateFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    private static final ThreadLocal<SimpleDateFormat> dateFormatter2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/M/d"));
    private static final ThreadLocal<SimpleDateFormat> timeFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));

    private CommonUtil() {
    }

    /**
     * 判断所有传入的参数的值是否均为null
     *
     * @param params 可变参数项
     * @return true - 均为null； false - 有其中的一项不为null
     */
    public static boolean isAllNull(Object... params) {
        for (Object param : params) {
            if (param != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断传入的参数是否存在null
     *
     * @param params 可变参数项
     * @return true - 有其中一项为null； false - 都不为null
     */
    public static boolean isOneNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将传入的以":"符号分割的串切割为intSet
     *
     * @param checker 校验器
     * @param str     字符串
     * @return 若str为空，则返回null，若不符合规则或出现异常则返回空set
     */
    public static Set<Integer> str2IntSet(Predicate<String> checker, @Nullable String str) {
        return splitStrByColon(checker, str, Integer::valueOf);
    }

    /**
     * 将传入的以":"符号分割的串切割为byteSet
     *
     * @param checker 校验器
     * @param str     字符串
     * @return 若str为空，则返回null，若不符合规则或出现异常则返回空set
     */
    public static Set<Byte> str2ByteSet(Predicate<String> checker, @Nullable String str) {
        return splitStrByColon(checker, str, Byte::valueOf);
    }

    /**
     * 将传入的以":"符号分割的串切割为StringSet
     *
     * @param str     字符串
     * @return 分割后的串StringSet
     */
    public static Set<String> str2StringSet(@Nullable String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        Set<String> set = new HashSet<>(Splitter.on(":").omitEmptyStrings().trimResults().splitToList(str));
        return set;
    }

    /**
     * 根据冒号来划分字符串，并转换为集合
     */
    private static <T extends Number> Set<T> splitStrByColon(Predicate<String> checker, @Nullable String str, Function<String, T> converter) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }

        Set<T> set = Collections.emptySet();

        try {
            if (checker.test(str)) {
                Iterable<String> strings = colonSplitter.split(str);
                set = Streams.stream(strings).map(converter).collect(Collectors.<T>toSet());
            }
        } catch (Exception e) {
            log.info("字符串转换失败", e);
        }
        return set;
    }

    public static boolean isDt(String dt) {
        return dtChecker.test(dt);
    }

    public static int dt2UnixTime(String dt) {
        try {
            return (int) (dateFormatter.get().parse(dt).getTime() / 1000L);
        } catch (ParseException e) {
            log.error("日期解析失败", e);
        }
        return -1;
    }

    public static int dt2UnixTime2(String dt) {
        try {
            return (int) (dateFormatter2.get().parse(dt).getTime() / 1000L);
        } catch (ParseException e) {
            log.error("日期解析失败", e);
        }
        return -1;
    }

    /**
     * 将字符串转换为BigDecimal，并屏蔽错误
     *
     * @param str        字符串
     * @param defaultVal 默认值，用于异常抛出时返回
     * @return 若str为空，则返回null，若出现异常返回默认值
     */
    public static BigDecimal str2Decimal(@Nullable String str, BigDecimal defaultVal) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }

        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            log.info("转化为BigDecimal失败");
            return defaultVal;
        }

    }

    /**
     * 比较两个BigDecimal的大小，出现null即为false
     */
    public static boolean firstBigger(@Nullable BigDecimal first, @Nullable BigDecimal second) {
        return first != null && second != null && first.compareTo(second) > 0;
    }

    /**
     * 比较两个Integer的大小，出现null即为false
     */
    public static boolean firstBigger(@Nullable Integer first, @Nullable Integer second) {
        return first != null && second != null && first.compareTo(second) > 0;
    }

    /**
     * 比较两个Byte的大小，出现null即为false
     */
    public static boolean firstBigger(@Nullable Byte first, @Nullable Byte second) {
        return first != null && second != null && first.compareTo(second) >= 0;
    }

    /**
     * 比较两个Double的大小，出现null即为false
     */
    public static boolean firstBigger(@NotNull Double first, @Nullable Double second) {
        return first != null && second != null && first.compareTo(second) >= 0;
    }

    /**
     * 判断时间范围是否在同一天内
     */
    public static boolean isSameDay(int beginTime, int endTime) {
        beginTime += BEIJING_DELTA;
        endTime += BEIJING_DELTA - 1; // 避免结束时间如 2018-02-08 00:00:00 的情况
        return (beginTime / SECOND_OF_DAY == endTime / SECOND_OF_DAY);
    }

    /**
     * 返回当前unixTime所处的日期范围
     *
     * @param unixTime Unix时间戳
     * @return unix日期范围
     */
    public static Range<Integer> getDayTimeRange(int unixTime) {
        int a = ((unixTime + BEIJING_DELTA) / SECOND_OF_DAY) * SECOND_OF_DAY - BEIJING_DELTA;
        return Range.closed(a, a + SECOND_OF_DAY);
    }

    /**
     * 返回unix时间的字符串形式
     *
     * @param unixTime unix时间戳
     * @return 字符串，格式为YYYYMMDD
     */
    public static String toDateStr(int unixTime) {
        return dateFormatter.get().format(new Date(unixTime * 1000L));
    }

    /**
     * 返回unix时间的字符串形式
     *
     * @param unixTime unix时间戳
     * @return 字符串，格式为yyyy/M/d
     */
    public static String toDateStr2(int unixTime) {
        return dateFormatter2.get().format(new Date(unixTime * 1000L));
    }

    /**
     * 返回当前时间的秒值
     */
    public static int curSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 判断当前时间是否为半点的整数倍
     *
     * @param time unix时间戳
     */
    public static boolean isBaseHalfHour(Integer time) {
        return time % 1800 == 0;
    }

    /**
     * 返回unix时间戳的时间格式
     *
     * @return 字符串，格式为yyyy-MM-dd HH:mm:ss
     */
    public static String toTimeStr(int unixTime) {
        return timeFormatter.get().format(new Date(unixTime * 1000L));
    }


    /**
     * 返回当前时间的年月日
     *
     * @return 字符串, 格式为 yyyyMMdd
     */
    public static String curTimeStr() {
        long l = System.currentTimeMillis();
        return dateFormatter.get().format(new Date(l));
    }

    /**
     * 根据日期返回年龄
     */
    public static int getAgeByBirth(String birthday) {
        int age;
        try {
            Date _birthday = dateFormatter.get().parse(birthday);
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(_birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }
}
