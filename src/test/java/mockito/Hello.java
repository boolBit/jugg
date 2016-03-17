//package mockito;
//
//import com.google.common.collect.Lists;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.ArgumentMatcher;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.argThat;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
///**
// * @author lorabit
// * @since 16-3-15
// */
//// http://blog.csdn.net/sdyy321/article/details/38757135
//@RunWith(MockitoJUnitRunner.class)
//public class Hello {
//
//
//  @InjectMocks
//  MockService  mockService;
//
//  @Mock
//  MockDao mockDao;
//
//
//
//  @Test
//  public void testInjectMock(){
//    Mockito.when(mockDao.say()).thenAnswer(new Answer<Void>() {
//      @Override
//      public Void answer(InvocationOnMock invocation) throws Throwable {
//        System.out.println("im a mock say");
//        return null;
//      }
//    });
//    mockService.say();
//  }
//
//  @Test
//  public void whenNotUseSpyAnnotation_thenCorrect() {
//    List<String> spyList = Mockito.spy(new ArrayList<String>());
//
//    spyList.add("one");
//    spyList.add("two");
//
//    Mockito.verify(spyList).add("two");
//    Mockito.verify(spyList).add("one");
//
//    assertEquals(2, spyList.size());
//
//    Mockito.doReturn(100).when(spyList).size();
//    assertEquals(100, spyList.size());
//  }
//
//
//
//
//  @Test
//  public void hello(){
//    // you can mock concrete classes, not only interfaces
//    LinkedList mockedList = mock(LinkedList.class);
//
//// stubbing appears before the actual execution
//    when(mockedList.get(0)).thenReturn("first");
//
//// the following prints "first"
//    System.out.println(mockedList.get(0));
//
//// the following prints "null" because get(999) was not stubbed
//    System.out.println(mockedList.get(999));
//  }
//
//  @Test
//  public void when_thenReturn(){
//    //mock一个Iterator类
//    Iterator iterator = mock(Iterator.class);
//    //预设当iterator调用next()时第一次返回hello，第n次都返回world
//    when(iterator.next()).thenReturn("hello");
//    //使用mock的对象
//    String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
//    //验证结果
//    assertEquals("hello hello hello",result);
//  }
//
//
//  @Test(expected = IOException.class)
//  public void when_thenThrow() throws IOException {
//    OutputStream outputStream = mock(OutputStream.class);
//    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
//    //预设当流关闭时抛出异常
//    doThrow(new IOException()).when(outputStream).close();
//    outputStream.close();
//  }
//
//  @Test
//  public void with_unspecified_arguments(){
//    List list = mock(List.class);
//    //匹配任意参数
//    when(list.get(anyInt())).thenReturn(1);
//    when(list.contains(argThat(new IsValid()))).thenReturn(true);
//    assertEquals(1, list.get(1));
//    assertEquals(1, list.get(999));
//    assertTrue(list.contains(1));
//    assertTrue(!list.contains(3));
//  }
//
//  private class IsValid extends ArgumentMatcher<List> {
//    @Override
//    public boolean matches(Object o) {
//      return (Integer)o == 1 || (Integer)o == 2;
//    }
//  }
//
//
//  @Mock
//  private List mockList;
//
//
//  @Test
//  public void shorthand(){
//    mockList.add(1);
//    verify(mockList).add(1);
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void consecutive_calls(){
//    //模拟连续调用返回期望值，如果分开，则只有最后一个有效
//    when(mockList.get(0)).thenReturn(0);
//    when(mockList.get(0)).thenReturn(1);
//    when(mockList.get(0)).thenReturn(2);
//    when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
//    assertEquals(2,mockList.get(0));
//    assertEquals(2,mockList.get(0));
//    assertEquals(0,mockList.get(1));
//    assertEquals(1,mockList.get(1));
//    //第三次或更多调用都会抛出异常
//    mockList.get(1);
//  }
////12、监控真实对象
//  @Test(expected = IndexOutOfBoundsException.class)
//  public void spy_on_real_objects(){
//    List list = new LinkedList();
//    List sspy = spy(list);
//    //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
//    //when(spy.get(0)).thenReturn(3);
//
//    //使用doReturn-when可以避免when-thenReturn调用真实对象api
//    doReturn(999).when(sspy).get(999);
//    //预设size()期望值
//    when(sspy.size()).thenReturn(100);
//    //调用真实对象的api
//    sspy.add(1);
//    sspy.add(2);
//    assertEquals(100,sspy.size());
//    assertEquals(1,sspy.get(0));
//    assertEquals(2,sspy.get(1));
//    verify(sspy).add(1);
//    verify(sspy).add(2);
//    assertEquals(999,sspy.get(999));
//    sspy.get(2);
//  }
//
//  @Mock
//  List mockedList;
//
//  @Captor
//  ArgumentCaptor argCaptor;
//
//  @Test
//  public void whenUseCaptorAnnotation_thenTheSam() {
//    mockedList.addAll(Lists.newArrayList(1,2,3));
//    Mockito.verify(mockedList).addAll(argCaptor.capture());
////    assertEquals("one", argCaptor.getValue());
//    System.out.println(argCaptor.getValue());
//  }
//
//
//}
//
