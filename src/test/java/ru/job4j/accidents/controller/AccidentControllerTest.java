package ru.job4j.accidents.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ru.job4j.Main;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;


@ActiveProfiles("test")
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccidentService accidentService;

    @MockBean
    private AccidentTypeService accidentTypeService;

    @MockBean
    private RuleService ruleService;

    private static List<AccidentType> accidentTypes;

    private static Accident expectedAccident;

    private static List<Rule> rules;

    @BeforeAll
    public static void init() {

        accidentTypes = List.of(
                new AccidentType(1, "at 1"),
                new AccidentType(2, "at 2"));

        expectedAccident = new Accident();
        expectedAccident.setId(1);
        expectedAccident.setName("test1");
        expectedAccident.setType(accidentTypes.get(0));

        rules = List.of(
                new Rule(1, "r1"),
                new Rule(2, "r2"));

    }

    @Test
    @WithMockUser
    @Transactional
    void whenGetByIdForUpdateThenReturn1() throws Exception {

        Mockito.when(this.accidentService.read(1)).thenReturn(Optional.of(expectedAccident));
        Mockito.when(this.accidentTypeService.getAll()).thenReturn(accidentTypes);

        mockMvc.perform(get("/accidents/update?id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents/edit"))
                .andExpect(model().attribute("accident", expectedAccident))
                .andExpect(model().attribute("types", accidentTypes));
    }

    @Test
    @WithMockUser
    @Transactional
    void whenGetByIdForUpdateThenReturn404() throws Exception {
        Mockito.when(this.accidentService.read(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/accidents/update?id=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

    @Test
    @WithMockUser
    @Transactional
    void whenGetCreateAccidentViewThenReturnCreateView() throws Exception {
        Mockito.when(this.accidentTypeService.getAll()).thenReturn(accidentTypes);
        Mockito.when(this.ruleService.getAll()).thenReturn(rules);

        mockMvc.perform(get("/accidents/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents/create"))
                .andExpect(model().attribute("types", accidentTypes))
                .andExpect(model().attribute("rules", rules));

    }

    @Test
    @WithMockUser
    public void whenCreateThenCreateAccident() throws Exception {
        String[] rIds = {"1", "2"};

        this.mockMvc.perform(post("/accidents/save")
                .flashAttr("accident", expectedAccident)
                .param("rIds", rIds))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Accident> accidentArg = ArgumentCaptor.forClass(Accident.class);
        ArgumentCaptor<String[]> rIdsArg = ArgumentCaptor.forClass(String[].class);
        verify(accidentService).create(accidentArg.capture(), rIdsArg.capture());

        assertThat(accidentArg.getValue()).isEqualTo(expectedAccident);
        assertThat(rIdsArg.getValue())
                .containsExactlyInAnyOrder("1", "2");
    }

    @Test
    @WithMockUser
    public void whenUpdateThenUpdateAccident() throws Exception {
        Mockito.when(this.accidentService.update(expectedAccident)).thenReturn(true);

        this.mockMvc.perform(post("/accidents/update")
                .flashAttr("accident", expectedAccident))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Accident> accidentArg = ArgumentCaptor.forClass(Accident.class);
        verify(accidentService).update(accidentArg.capture());

        assertThat(accidentArg.getValue()).isEqualTo(expectedAccident);
    }

    @Test
    @WithMockUser
    public void whenFailedUpdateThenErrorPage() throws Exception {
        Mockito.when(this.accidentService.update(expectedAccident)).thenReturn(false);

        this.mockMvc.perform(post("/accidents/update")
                .flashAttr("accident", expectedAccident))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"));
    }

}
