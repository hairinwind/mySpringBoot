package my.spring.junit.staticmethod;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClassHaveStaticMethodTest {

    @Test
    public void testMockStaticMethod() {
        LocalDateTime today = LocalDateTime.of(2022,6,28,16,0,0);
        ClassHaveStaticMethod classHaveStaticMethod = new ClassHaveStaticMethod();
        try(MockedStatic<LocalDateTime> localDateTimeMockedStatic = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS)) {
            //CALLS_REAL_METHODS 表示没有 mock 的方法就调用真实的方法，如果不加这个参数，默认是所有方法都需要mock
            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(today);
            LocalDateTime now = classHaveStaticMethod.getNow();
            assertEquals(today,now);
        }
    }
}