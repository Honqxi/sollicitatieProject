package be.sollicitatie.project.sollicitatieproject.controller;

import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.Service.PersonService;
import be.sollicitatie.project.sollicitatieproject.domain.dto.PersonRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFirstName("Joakim");
        person.setLastName("Libioulle");
        person.setEmail("libioullejoakim@gmail.com");
    }

    @Test
    void shouldReturnAllPersons() throws Exception {
        when(personService.findAll()).thenReturn(List.of(person));

        mockMvc.perform(get("/api/persons")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Joakim"));
    }

    @Test
    void shouldReturnPersonById() throws Exception {
        when(personService.findById(1L)).thenReturn(person);

        mockMvc.perform(get("/api/persons/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("libioullejoakim@gmail.com"));
    }

    @Test
    void shouldCreatePersonAsAdmin() throws Exception {
        when(personService.create(any(PersonRequest.class))).thenReturn(person);

        mockMvc.perform(post("/api/persons")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "firstName": "Joakim",
                  "lastName": "Libioulle",
                  "email": "libioullejoakim@gmail.com"
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Joakim"));
    }

    @Test
    void shouldReturn403WhenCreatingPersonAsUser() throws Exception {
        mockMvc.perform(post("/api/persons")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "firstName": "Joakim",
                  "lastName": "Libioulle",
                  "email": "libioullejoakim@gmail.com"
                }
            """))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdatePersonAsAdmin() throws Exception {
        Person updated = new Person();
        updated.setId(1L);
        updated.setFirstName("John");
        updated.setLastName("Doe");
        updated.setEmail("john.doe@gmail.com");

        when(personService.update(eq(1L), any(Person.class))).thenReturn(updated);

        mockMvc.perform(put("/api/persons/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@gmail.com"
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldDeletePersonAsAdmin() throws Exception {
        doNothing().when(personService).deleteById(1L);

        mockMvc.perform(delete("/api/persons/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
