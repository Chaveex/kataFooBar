package com.kata.kataFooBar.controller;

import com.kata.kataFooBar.KataFooBarApplication;
import com.kata.kataFooBar.exception.BadArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KataController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {KataFooBarApplication.class})
public class KataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void controllerShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/foobar").param("number", String.valueOf(2L)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void controllerShouldReturn500WhenInputIsNegative() throws Exception {
        mockMvc.perform(post("/api/v1/foobar").param("number", String.valueOf(-2L)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is5xxServerError())
                .andExpect(result -> Assertions.assertInstanceOf(BadArgumentException.class, result.getResolvedException()));
    }


    @Test
    public void controllerShouldReturn500WhenInputIsOver100() throws Exception {
        mockMvc.perform(post("/api/v1/foobar").param("number", String.valueOf(104L)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is5xxServerError())
                .andExpect(result -> Assertions.assertInstanceOf(BadArgumentException.class, result.getResolvedException()));
    }

    @Test
    public void controllerFoobarFileShouldReturn200() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "input.txt", MediaType.TEXT_PLAIN_VALUE, "1".getBytes());
        mockMvc.perform(multipart("/api/v1/foobar/file").file(file))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


    @Test
    public void controllerFoobarFileShouldReturn500() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "input.txt", MediaType.TEXT_PLAIN_VALUE, "a".getBytes());
        mockMvc.perform(multipart("/api/v1/foobar/file").file(file))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is5xxServerError());
    }
}
