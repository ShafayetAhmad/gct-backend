package com.example.auth;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.example.auth.config.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
public abstract class BaseControllerTest {
    protected static final String TEST_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.ZXhYX5CpkxVMzYlpfdAkPLR6IRKGzqeoVLxnj5UJqPI";
}