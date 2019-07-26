package my.springboot.cacheservice;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FetchDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FetchDataExpensiveService expensiveService;

    @Test
    public void fetchData() throws Exception {
        // thenAnswer is used for that muliptle call on mock function returns different results
        when(expensiveService.fetchData("test")).thenAnswer(new Answer(){
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return ++count;
            }
        });
        mvc.perform(get("/fetchdata")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
//                .andDo(MockMvcResultHandlers.print())
                ;

        mvc.perform(get("/fetchdata")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(expensiveService, times(1)).fetchData("test");
    }

    @Test
    public void clearCache() throws Exception {
        when(expensiveService.fetchData("test")).thenAnswer(new Answer(){
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return ++count;
            }
        });
        mvc.perform(get("/fetchdata")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mvc.perform(get("/clearCache")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        mvc.perform(get("/fetchdata")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(expensiveService, times(2)).fetchData("test");
    }

    @Test
    public void fetchNonCacheData() throws Exception {
        when(expensiveService.fetchData(any())).thenAnswer(new Answer(){
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return ++count;
            }
        });

        mvc.perform(get("/fetchdata?name=test")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        mvc.perform(get("/fetchdata?name=test1")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        mvc.perform(get("/fetchNonCacheData?name=test")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        mvc.perform(get("/fetchdata?name=test1")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        mvc.perform(get("/fetchdata?name=test")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}