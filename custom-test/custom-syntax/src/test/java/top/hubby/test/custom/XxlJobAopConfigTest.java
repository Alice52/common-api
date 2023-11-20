package top.hubby.test.custom;

import static common.logging.trace.TraceIdConstants.TRACE_ID_NAME;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import common.logging.trace.aspect.XxlJobTraceAspect;

import io.github.alice52.test.BootTestContext;

import lombok.val;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.MDC;

/**
 * @author alice52
 * @date 2023/11/20
 * @project traffic-counter-service
 */
public class XxlJobAopConfigTest extends BootTestContext {

    @InjectMocks private XxlJobTraceAspect xxlJobTraceAspect;

    @Mock private ProceedingJoinPoint proceedingJoinPoint;

    @Test
    public void testAround1() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn("mockedReturnValue");

        Object result = xxlJobTraceAspect.around(proceedingJoinPoint);

        Mockito.verify(proceedingJoinPoint, times(1)).proceed();
        assertNull(MDC.get(TRACE_ID_NAME));
        assertEquals("mockedReturnValue", result);
    }

    @Test
    public void testAround2() throws Throwable {

        try (val mockStatic = Mockito.mockStatic(MDC.class)) {

            Object result = xxlJobTraceAspect.around(proceedingJoinPoint);
            mockStatic.verify(() -> MDC.put(any(String.class), any(String.class)), times(1));
            mockStatic.verify(() -> MDC.remove(any(String.class)), times(1));
        }
    }
}
