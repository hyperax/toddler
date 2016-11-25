package ru.toddler.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.annimon.stream.Optional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {

    private static final String HTTP = "http://";
    private static final Random random = new Random();

    @NonNull
    public static <T> T getFirst(@Nullable List<T> list, @NonNull T ifEmptyValue) {
        T firstItem = null;
        if (!NpeUtils.isEmpty(list)) {
            firstItem = list.get(0);
        }
        return NpeUtils.getNonNull(firstItem, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getFirst(@Nullable List<T> list) {
        T firstItem = null;
        if (!NpeUtils.isEmpty(list)) {
            firstItem = list.get(0);
        }
        return Optional.ofNullable(firstItem);
    }

    @NonNull
    public static <T> T getLast(@Nullable List<T> list, @NonNull T ifEmptyValue) {
        T lastItem = null;
        if (!NpeUtils.isEmpty(list)) {
            lastItem = list.get(list.size() - 1);
        }
        return NpeUtils.getNonNull(lastItem, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getLast(@Nullable List<T> list) {
        T lastItem = null;
        if (!NpeUtils.isEmpty(list)) {
            lastItem = list.get(list.size() - 1);
        }
        return Optional.ofNullable(lastItem);
    }

    @NonNull
    public static <T> T getRandom(@Nullable T[] array, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(array)) {
            result = array[random.nextInt(array.length)];
        }
        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(@Nullable T[] array) {
        T result = null;

        if (!NpeUtils.isEmpty(array)) {
            result = array[random.nextInt(array.length)];
        }
        return Optional.ofNullable(result);
    }

    @NonNull
    public static Optional<Integer> getRandom(@Nullable int[] array) {
        Integer result = null;

        if (!NpeUtils.isEmpty(array)) {
            result = array[random.nextInt(array.length)];
        }

        return Optional.ofNullable(result);
    }

    @NonNull
    public static <T> T getRandom(@Nullable Collection<T> collection, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(collection)) {
            int randomIndex = random.nextInt(collection.size());
            int counter = 0;
            for (T item: collection) {
                if (counter == randomIndex) {
                    result = item;
                    break;
                }
                ++counter;
            }
        }
        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(@Nullable Collection<T> collection) {
        T result = null;

        if (!NpeUtils.isEmpty(collection)) {
            int randomIndex = random.nextInt(collection.size());
            int counter = 0;
            for (T item: collection) {
                if (counter == randomIndex) {
                    result = item;
                    break;
                }
                ++counter;
            }
        }
        return Optional.ofNullable(result);
    }

    @NonNull
    public static <T> T getRandom(@Nullable List<T> list, @NonNull T ifEmptyValue) {
        T result = null;

        if (!NpeUtils.isEmpty(list)) {
            result = list.get(random.nextInt(list.size()));
        }

        return NpeUtils.getNonNull(result, ifEmptyValue);
    }

    @NonNull
    public static <T> Optional<T> getRandom(@Nullable List<T> list) {
        T result = null;

        if (!NpeUtils.isEmpty(list)) {
            result = list.get(random.nextInt(list.size()));
        }

        return Optional.ofNullable(result);
    }

    @NonNull
    public static <T> List<T> getRandomList(@Nullable List<T> list) {
        List<T> result = null;

        if (!NpeUtils.isEmpty(list)) {
            int expectedSize = random.nextInt(list.size());
            result = new ArrayList<>(expectedSize);
            result.addAll(list);
            while (result.size() > expectedSize) {
                result.remove(random.nextInt(result.size()));
            }
        }

        return result != null? result : Collections.emptyList();
    }

    @NonNull
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean validateIp(final String ip){
        String PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static boolean validateUrl(final String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean validateUrlOrIp(String baseUrl){
        String urlWithoutScheme  = baseUrl.startsWith(HTTP) ?
                baseUrl.substring(HTTP.length(), baseUrl.length()) : baseUrl;

        return (validateUrl(urlWithoutScheme) || validateIp(urlWithoutScheme));
    }

    public static String getSimpleIpOrDomain(String baseUrl){
        String urlWithoutScheme  = baseUrl.startsWith(HTTP) ?
                baseUrl.substring(HTTP.length(), baseUrl.length()) : baseUrl;
        if (urlWithoutScheme.contains("/")){
            urlWithoutScheme = urlWithoutScheme.substring(0, urlWithoutScheme.indexOf("/"));
        }
        return urlWithoutScheme;
    }

    public static boolean contains(@Nullable int[] array, int toFind) {
        if (array == null) {
            return false;
        }

        for (int item : array) {
            if (toFind == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(@Nullable long[] array, long toFind) {
        if (array == null) {
            return false;
        }

        for (long item : array) {
            if (toFind == item) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(@Nullable T[] array, T toFind) {
        if (array == null) {
            return false;
        }

        for (T item : array) {
            if (NpeUtils.equals(toFind, item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> Optional<T> getModulus(@Nullable List<T> list, int index) {
        if (NpeUtils.isEmpty(list)) {
            return Optional.empty();
        }
        final int size = list.size();

        final int location = (index >= 0 ? index : -index) % size;
        return Optional.ofNullable(list.get(location));
    }

    public static <T> Optional<T> getModulus(@Nullable T[] array, int index) {
        if (NpeUtils.isEmpty(array)) {
            return Optional.empty();
        }
        final int size = array.length;
        final int location = (index >= 0 ? index : -index) % size;
        return Optional.ofNullable(array[location]);
    }

}
