package br.com.jrr.apiTest.App.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class CpfUtil {

    public String format(final String value) {
        final var val = extractNumbers(value);
        if (val.length() == 11) {
            return val.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return val;
    }

    public boolean isValid(final String cpf) {
        final List<Integer> digits = extractNumbersToList(cpf);
        if (digits.size() == 11 && digits.stream().distinct().count() > 1) {
            return getCpfValid(digits.subList(0, 9)).equals(extractNumbers(cpf));
        }
        return false;
    }

    private String getCpfValid(final List<Integer> digits) {
        digits.add(mod11(digits, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        digits.add(mod11(digits, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        return listToString(digits);
    }

    private int mod11(final List<Integer> digits, final int... multipliers) {
        final var i = new AtomicInteger(0);
        final var rest = digits.stream().reduce(0, (p, e) -> p + e * multipliers[i.getAndIncrement()]) % 11;
        return rest > 9 ? 0 : rest;
    }

    private String extractNumbers(final String s) {
        return Objects.nonNull(s) ? s.replaceAll("\\D+", "") : "";
    }

    private List<Integer> extractNumbersToList(final String value) {
        final var digits = new ArrayList<Integer>();
        for (char item : extractNumbers(value).toCharArray()) {
            digits.add(Integer.parseInt(String.valueOf(item)));
        }
        return digits;
    }

    private String listToString(final List<Integer> list) {
        return list.stream().map(Object::toString).reduce("", (p, e) -> p.concat(e));
    }
}
