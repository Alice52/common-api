// package top.hubby.test.custom;
//
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.times;
// import static org.powermock.api.mockito.PowerMockito.verifyStatic;
//
// import common.logging.trace.aspect.XxlJobTraceAspect;
//
// import org.aspectj.lang.ProceedingJoinPoint;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.powermock.api.mockito.PowerMockito;
// import org.powermock.core.classloader.annotations.PrepareForTest;
// import org.powermock.modules.junit4.PowerMockRunner;
// import org.slf4j.MDC;
//
// /**
// * 需要 powermock 才能运行: 不推荐使用(社区已经不维护了)
// * @author alice52
// * @author alice52
// * @date 2023/11/20
// * @project traffic-counter-service
// */
// @RunWith(PowerMockRunner.class)
// @PrepareForTest({MDC.class})
// public class XxlJobAopConfigCounterTest {
//
//    @InjectMocks private XxlJobTraceAspect xxlJobTraceAspect;
//
//    @Mock private ProceedingJoinPoint proceedingJoinPoint;
//
//    @Test
//    public void testAround2() throws Throwable {
//        // Mock static methods
//        PowerMockito.mockStatic(MDC.class);
//
//        // Perform the test
//        xxlJobTraceAspect.around(proceedingJoinPoint);
//
//        // Verify that MDC.put was called with specific arguments
//        verifyStatic(MDC.class);
//        MDC.put(any(String.class), any(String.class));
//
//        // Verify the number of invocations
//        verifyStatic(MDC.class, times(1));
//        MDC.put(any(String.class), any(String.class));
//    }
// }
