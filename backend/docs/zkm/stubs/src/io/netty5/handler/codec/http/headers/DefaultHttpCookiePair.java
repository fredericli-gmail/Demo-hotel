package io.netty5.handler.codec.http.headers;

/**
 * 簡化版 Cookie Pair 實作，僅保存名稱與數值。
 */
public final class DefaultHttpCookiePair implements HttpCookiePair {

    private final CharSequence name;
    private final CharSequence value;

    public DefaultHttpCookiePair(CharSequence name, CharSequence value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public CharSequence name() {
        return name;
    }

    @Override
    public CharSequence value() {
        return value;
    }
}
