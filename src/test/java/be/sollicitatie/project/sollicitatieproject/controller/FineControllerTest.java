package be.sollicitatie.project.sollicitatieproject.controller;

import be.sollicitatie.project.sollicitatieproject.domain.Fine;
import be.sollicitatie.project.sollicitatieproject.domain.FineStatus;
import be.sollicitatie.project.sollicitatieproject.domain.Person;
import be.sollicitatie.project.sollicitatieproject.Service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FineController.class)
@AutoConfigureMockMvc
class FineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FineService fineService;

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
    void shouldReturnAllFinesForUserAndAdmin() throws Exception {
        Fine fine1 = new Fine();
        fine1.setPerson(person);
        fine1.setId(1L);
        fine1.setCity("Hasselt");
        fine1.setStatus(FineStatus.OPEN);
        fine1.setAmount(99);

        Fine fine2 = new Fine();
        fine2.setPerson(person);
        fine2.setId(2L);
        fine2.setCity("Hasselt");
        fine2.setStatus(FineStatus.OPEN);
        fine2.setAmount(99);

        List<Fine> fines = new ArrayList<>();
        fines.add(fine1);
        fines.add(fine2);

        when(fineService.findAll()).thenReturn(fines);

        mockMvc.perform(get("/api/fines")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].city").value("Hasselt"))
                .andExpect(jsonPath("$[1].amount").value(99));

        mockMvc.perform(get("/api/fines")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].city").value("Hasselt"))
                .andExpect(jsonPath("$[1].amount").value(99));
    }

    @Test
    void shouldReturnFineById() throws Exception {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(1L);
        fine.setCity("Hasselt");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        when(fineService.findById(1L)).thenReturn(fine);

        mockMvc.perform(get("/api/fines/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Id").value(1))
                .andExpect(jsonPath("$.city").value("Hasselt"));
    }

    @Test
    void shouldReturnFinesByPerson() throws Exception {
        Fine fine1 = new Fine();
        fine1.setPerson(person);
        fine1.setId(1L);
        fine1.setCity("Hasselt");
        fine1.setStatus(FineStatus.OPEN);
        fine1.setAmount(99);

        Fine fine2 = new Fine();
        fine2.setPerson(person);
        fine2.setId(2L);
        fine2.setCity("Hasselt");
        fine2.setStatus(FineStatus.OPEN);
        fine2.setAmount(99);

        List<Fine> fines = new ArrayList<>();
        fines.add(fine1);
        fines.add(fine2);

        when(fineService.findByPerson(1L)).thenReturn(fines);

        mockMvc.perform(get("/api/fines/person/1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturnTotalAmountPerCity() throws Exception {
        Map<String, Integer> totals = Map.of(
                "Hasselt", 80,
                "Genk", 30
        );

        when(fineService.totalAmountPerCity()).thenReturn(totals);

        mockMvc.perform(get("/api/fines/total-per-city")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hasselt").value(80))
                .andExpect(jsonPath("$.Genk").value(30));
    }

    @Test
    void shouldReturnForbiddenWhenUserCreatesFine() throws Exception {
        mockMvc.perform(post("/api/fines")
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "city": "Sint-Truiden",
                  "amount": 50
                }
            """)).andExpect(status().isForbidden());
    }

    @Test
    void shouldCreateFineAsAdmin() throws Exception {
        Fine fine = new Fine();
        fine.setPerson(person);
        fine.setId(1L);
        fine.setCity("Hasselt");
        fine.setStatus(FineStatus.OPEN);
        fine.setAmount(99);

        when(fineService.create(any(Fine.class))).thenReturn(fine);

        mockMvc.perform(post("/api/fines")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "city": "Hasselt",
                  "amount": 50
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Id").value(1L))
                .andExpect(jsonPath("$.city").value("Hasselt"));
    }

    @Test
    void shouldReturnForbiddenWhenUserUpdateFine() throws Exception {
        mockMvc.perform(put("/api/fines")
                .with(user("user").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "city": "Sint-Truiden",
                  "amount": 50
                }
            """)).andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdateFineAsAdmin() throws Exception {
        Fine updated = new Fine();
        updated.setPerson(person);
        updated.setId(1L);
        updated.setCity("Landen");
        updated.setStatus(FineStatus.OPEN);
        updated.setAmount(100);

        when(fineService.update(eq(1L), any(Fine.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/fines/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "city": "Landen",
                  "amount": 100
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Landen"))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void shouldDeleteFineAsAdmin() throws Exception {
        mockMvc.perform(delete("/api/fines/1")
                        .with(user("admin").roles("ADMIN")).with(csrf()))
                .andExpect(status().isNoContent());

        verify(fineService).deleteById(1L);
    }
}
