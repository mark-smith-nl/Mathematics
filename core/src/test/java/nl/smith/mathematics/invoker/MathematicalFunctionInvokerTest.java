package nl.smith.mathematics.invoker;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

//import java.lang.reflect.Method;
//import java.util.AbstractMap.SimpleEntry;
//import java.util.HashMap;

//TODO Uncomment tests
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MathematicalFunctionInvokerTest {

	// @Mock
	// private ApplicationContext ctx;
	//
	// @SuppressWarnings("unchecked")
	// @Test(expected = IllegalArgumentException.class)
	// public void constructUsingNullMathematicalFunctionContainerMap() {
	// new MathematicalFunctionInvoker(null, (Class<? extends NumberOperations<?>>) NumberOperations.class);
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void constructUsingApplicationContextWithNullNumberClass() {
	// when(ctx.getBean(Class.class)).thenReturn(null);
	// new MathematicalFunctionInvoker(new HashMap()<String, Object>(), null);
	// }
	//
	// @Test
	// public void makeProxiesForBeans() {
	// List<Object> beans = Arrays.asList(
	// new MathematicalFunction_1(),
	// new MathematicalFunction_2(),
	// new MathematicalFunction_3());
	//
	// Map<Class<?>, Object> proxies = MathematicalFunctionInvoker.makeProxiesForBeans(beans);
	//
	// assertEquals(3, proxies.size());
	// assertTrue(proxies.containsKey(MathematicalFunction_1.class));
	// assertTrue(proxies.containsKey(MathematicalFunction_2.class));
	// assertTrue(proxies.containsKey(MathematicalFunction_3.class));
	//
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void makeProxiesForBeansUsingDuplicateBeanClasses() {
	// List<Object> beans = Arrays.asList(
	// new MathematicalFunction_1(),
	// new MathematicalFunction_2(),
	// new MathematicalFunction_2());
	//
	// MathematicalFunctionInvoker.makeProxiesForBeans(beans);
	// }
	//
	// @Test()
	// public void makeAvailableMethodAliasMap() {
	// List<Object> beans = Arrays.asList(
	// new MathematicalFunction_1(),
	// new MathematicalFunction_2(),
	// new MathematicalFunction_3());
	//
	// Map<Class<?>, Object> proxies = new HashMap<>();
	// for (Object bean : beans) {
	// proxies.put(bean.getClass(), null);
	// }
	//
	// Map<SimpleEntry<String, Integer>, Method> availableMethodAliasMap = MathematicalFunctionInvoker.makeAvailableMethodAliasMap(proxies);
	//
	// assertEquals(7, availableMethodAliasMap.size());
	//
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItOne", 1)));
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItTwo", 1)));
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItThree", 1)));
	//
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItOne_class_2", 1)));
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItTwo_class_2", 1)));
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItThree_class_2", 1)));
	//
	// assertTrue(availableMethodAliasMap.containsKey(new SimpleEntry<String, Integer>("doItThree", 2)));
	//
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void makeAvailableMethodAliasMapUsingPrivateMethod() {
	// List<Object> beans = Arrays.asList((Object) new MathematicalFunction_4());
	//
	// Map<Class<?>, Object> proxies = new HashMap<>();
	// for (Object bean : beans) {
	// proxies.put(bean.getClass(), null);
	// }
	//
	// MathematicalFunctionInvoker.makeAvailableMethodAliasMap(proxies);
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void makeAvailableMethodAliasMapUsingStaticMethod() {
	// List<Object> beans = Arrays.asList((Object) new MathematicalFunction_5());
	//
	// Map<Class<?>, Object> proxies = new HashMap<>();
	// for (Object bean : beans) {
	// proxies.put(bean.getClass(), null);
	// }
	//
	// MathematicalFunctionInvoker.makeAvailableMethodAliasMap(proxies);
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void makeAvailableMethodAliasMapUsingMethodWithWrongReturnType() {
	// List<Object> beans = Arrays.asList((Object) new MathematicalFunction_6());
	//
	// Map<Class<?>, Object> proxies = new HashMap<>();
	// for (Object bean : beans) {
	// proxies.put(bean.getClass(), null);
	// }
	//
	// MathematicalFunctionInvoker.makeAvailableMethodAliasMap(proxies);
	// }
	//
	// @Test(expected = IllegalStateException.class)
	// public void makeAvailableMethodAliasMapUsingMethodWithWrongArgumentTypes() {
	// List<Object> beans = Arrays.asList((Object) new MathematicalFunction_7());
	//
	// Map<Class<?>, Object> proxies = new HashMap<>();
	// for (Object bean : beans) {
	// proxies.put(bean.getClass(), null);
	// }
	//
	// MathematicalFunctionInvoker.makeAvailableMethodAliasMap(proxies);
	// }
}
