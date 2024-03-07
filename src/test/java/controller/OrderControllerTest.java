package controller;

import com.codpole.order.OrderApplication;
import com.codpole.order.model.OrderResponse;
import com.codpole.order.util.DataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateOrder() throws Exception {
        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest()))
                .andExpect(status().isOk());
        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, String.class);
        assertNotNull(response);
        assertTrue(Long.parseLong(response) > 0);
    }

    @Test
    void testGetOrders() throws Exception {
        //create 2 orders for userId 1, and 1 order for userId 2
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest1()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest2()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID_2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest3()))
                .andExpect(status().isOk());
        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/order/all")
                        .header("userId", DataProvider.USER_ID_1))
                .andExpect(status().isOk());
        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var response = objectMapper.readValue(json, OrderResponse.class);
        assertNotNull(response.getOrders());
        assertEquals(2, response.getOrders().size());
    }

    @Test
    void testGetTotalPrice() throws Exception {
        //create 2 orders for userId 3
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID_3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest4()))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .header("userId", DataProvider.USER_ID_3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getOrderRequest5()))
                .andExpect(status().isOk());

        var resultActions1 = mockMvc.perform(MockMvcRequestBuilders.get("/order/totalPrice")
                        .header("userId", DataProvider.USER_ID_3)
                        .param("from", "2024-03-01T00:00:00Z")
                        .param("to", "2124-03-05T00:00:00Z"))
                .andExpect(status().isOk());
        var mvcResult1 = resultActions1.andReturn();
        assertNotNull(mvcResult1.getResponse());
        var json1 = mvcResult1.getResponse().getContentAsString();
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var response1 = objectMapper.readValue(json1, String.class);
        assertNotNull(response1);
        assertEquals("283.925", response1);

        //if the date goes out of range
        var resultActions2 = mockMvc.perform(MockMvcRequestBuilders.get("/order/totalPrice")
                        .header("userId", DataProvider.USER_ID_3)
                        .param("from", "2023-03-01T00:00:00Z")
                        .param("to", "2024-03-01T00:00:00Z"))
                .andExpect(status().isOk());
        var mvcResult2 = resultActions2.andReturn();
        assertNotNull(mvcResult2.getResponse());
        var json2 = mvcResult2.getResponse().getContentAsString();
        var response2 = objectMapper.readValue(json2, String.class);
        assertNotNull(response2);
        assertEquals("0.0", response2);
    }
}
