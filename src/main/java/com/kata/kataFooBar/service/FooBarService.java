package com.kata.kataFooBar.service;

import com.kata.kataFooBar.exception.BadArgumentException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiFunction;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class FooBarService {
    private static final String THREE = "3";
    private static final String FIVE = "5";
    private static final String SEVEN = "7";
    private static final String FOO = "FOO";
    private static final String BAR = "BAR";
    private static final String EMPTY = "";
    private static final String QUIX = "QUIX";

    private Map<String, String> resultBySearchedValue = Map.of(THREE, FOO, FIVE, BAR, SEVEN, QUIX);

    BiFunction<String, String, String> containsValue = (number, searchedValue) -> number.equals(searchedValue) ? resultBySearchedValue.get(searchedValue) : EMPTY;
    BiFunction<Long, Long, String> divisibleBy = (number, divider) -> number % divider == 0 ? resultBySearchedValue.get(Long.toString(divider)) : EMPTY;

    public String foobar(Long number) {
        StringBuilder result = new StringBuilder();
        sanityCheckOnInput(number);
        result.append(printOnDivisible(number));
        result.append(printValueOnContaining(number));

        return result.isEmpty() ? Long.toString(number) : result.toString();
    }

    public String foobarFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            StringBuilder result = new StringBuilder();

            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .forEach(s -> result.append(foobar(Long.parseLong(s))).append(String.format("%n")));

            return result.toString();
        } catch (IOException e) {
            throw new BadArgumentException(String.format("Could not open file %s", e));
        } catch (NumberFormatException e) {
            throw new BadArgumentException("File should only contains numbers.");
        }
    }

    private String printOnDivisible(Long number) {
        return String.join(EMPTY, divisibleBy.apply(number, 3L), divisibleBy.apply(number, 5L));
    }

    private String printValueOnContaining(Long number) {
        char[] numberAsCharArray = Long.toString(number).toCharArray();
        StringBuilder result = new StringBuilder();

        for (char c : numberAsCharArray) {
            String currentChar = String.valueOf(c);
            result.append(containsValue.apply(currentChar, THREE));
            result.append(containsValue.apply(currentChar, FIVE));
            result.append(containsValue.apply(currentChar, SEVEN));
        }

        return result.toString();
    }

    private void sanityCheckOnInput(Long number) {
        if (number == null || number < 0 || number > 100) {
            throw new BadArgumentException("Invalid number. Rule: Only accept numbers between 0 ans 100 included.");
        }
    }
}
