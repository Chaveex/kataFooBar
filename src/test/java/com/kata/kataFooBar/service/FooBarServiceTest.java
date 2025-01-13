package com.kata.kataFooBar.service;

import com.kata.kataFooBar.exception.BadArgumentException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FooBarServiceTest {
    @InjectMocks
    private FooBarService fooBarService;

    @Test
    public void foobarShouldReturn1AsStringWhenCallingWiht1() {
        String foobar = fooBarService.foobar(1L);
        Assertions.assertThat(foobar).isEqualTo("1");
    }

    @Test
    public void foobarShouldReturnFOOBARAsStringWhenCallingWithNumber() {
        String foobar = fooBarService.foobar(3L);
        Assertions.assertThat(foobar).isEqualTo("FOOFOO");
        foobar = fooBarService.foobar(5L);
        Assertions.assertThat(foobar).isEqualTo("BARBAR");
        foobar = fooBarService.foobar(7L);
        Assertions.assertThat(foobar).isEqualTo("QUIX");
    }

    @Test
    public void foobarShouldReturnBARFOOAsStringWhenCallingWithNumber53() {
        String foobar = fooBarService.foobar(53L);
        Assertions.assertThat(foobar).isEqualTo("BARFOO");
    }

    @Test
    public void foobarShouldThrowExceptionWhenInputIsNegativeOrOver100() {
        Assertions.assertThatThrownBy(() -> fooBarService.foobar(-4L)).isInstanceOf(BadArgumentException.class);
        Assertions.assertThatThrownBy(() -> fooBarService.foobar(104L)).isInstanceOf(BadArgumentException.class);
        Assertions.assertThatThrownBy(() -> fooBarService.foobar(null)).isInstanceOf(BadArgumentException.class);
    }
}
