package ru.job4j.accidents.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.Main;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.UserRepository;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class RegControlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private static User expectedUser;

    @BeforeAll
    public static void init() {
        expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setUsername("test1");
        expectedUser.setPassword("123456");
    }

    @Test
    void whenRegPageThenReturnRegPage() throws Exception {

        mockMvc.perform(get("/reg"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("reg"));

    }

    @Test
    public void whenRegFailedThenReturnRegPage() throws Exception {

        Mockito.when(this.userRepository.save(expectedUser)).thenThrow(DataIntegrityViolationException.class);

        this.mockMvc.perform(post("/reg")
                .flashAttr("user", expectedUser))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("reg"))
                .andExpect(model().attribute("errorMessage",
                            "User with this login already exists, select a different login"));
    }


    @Test
    public void whenRegThenSaveUser() throws Exception {
        Mockito.when(this.userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/reg")
                .flashAttr("user", expectedUser))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArg.capture());
        assertThat(encoder.matches("123456", userArg.getValue().getPassword())).isTrue();
        assertThat(userArg.getValue().getAuthority().getAuthority()).isEqualTo("ROLE_USER");

    }

}
