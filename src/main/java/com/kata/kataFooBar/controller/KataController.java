package com.kata.kataFooBar.controller;

import com.kata.kataFooBar.service.FooBarService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/foobar")
@RequiredArgsConstructor
public class KataController {
    private final FooBarService fooBarService;

    @PostMapping
    public String foobar(Long number) {
        return fooBarService.foobar(number);
    }

    @PostMapping("/file")
    public @ResponseBody
    void foobar(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) throws IOException {
        String result = fooBarService.foobarFile(file);

        MediaType.valueOf("text/plain;charset=UTF-8");
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=result.txt");

        ServletOutputStream out = response.getOutputStream();

        out.println(result);
        out.flush();
        out.close();
    }
}
