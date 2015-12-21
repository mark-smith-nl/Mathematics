package nl.smith.mathematics.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodHandler;
import nl.smith.mathematics.constraint.AbstractNumberConstraintParameterChecker;
import nl.smith.mathematics.constraint.annotation.MethodConstraint;
import nl.smith.mathematics.functions.annotation.MathematicalFunction;
import nl.smith.mathematics.number.NumberOperations;

/**
 * {@link MethodHandler} for handling {@link MathematicalFunction} annotated methods
 * Method checked if constraint annotated arguments apply and if so invoked the method
 * 
 * @author mark
 *
 */
public class MathematicalFunctionHandler implements MethodHandler {

	AbstractNumberConstraintParameterChecker abstractNumberConstraintParameterChecker = AbstractNumberConstraintParameterChecker.getInstance();

	/**
	 * Method to convert string properties defined in an annotation into an {@link NumberOperations}
	 */
	private final Method numberFactoryMethod;

	public MathematicalFunctionHandler(Method numberFactoryMethod) {
		this.numberFactoryMethod = numberFactoryMethod;
	}

	/**
	 * Map of method argument annotations
	 * The key is the method
	 * The value is a method for creating instances of type {@link NumberOperations}
	 */
	private Map<Method, Method> methodNumberFactoryMethodMap = new HashMap<>();

	private Map<Method, Annotation[][]> methodParameterAnnotationMap = new HashMap<>();

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		Method numberFactoryMethod = getNumberFactoryMethod(thisMethod);

		Annotation[][] parameterAnnotations = methodParameterAnnotationMap.get(thisMethod);
		if (parameterAnnotations == null) {
			parameterAnnotations = thisMethod.getParameterAnnotations();
			methodParameterAnnotationMap.put(thisMethod, parameterAnnotations);
		}

		abstractNumberConstraintParameterChecker.allAssertionsApplyToAllArguments(numberFactoryMethod, parameterAnnotations, args);

		return proceed.invoke(self, args);
	}

	private Method getNumberFactoryMethod(Method method) {
		Method numberFactoryMethod = methodNumberFactoryMethodMap.get(method);
		if (numberFactoryMethod == null) {
			if (this.numberFactoryMethod == null) {
				MethodConstraint methodAnnotation = method.getAnnotation(MethodConstraint.class);
				if (methodAnnotation != null) {
					Class<?> numberFactoryContainingClass = methodAnnotation.numberType();
					String methodname = methodAnnotation.valueOf();
					try {
						numberFactoryMethod = numberFactoryContainingClass.getDeclaredMethod(methodname, String.class);
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				numberFactoryMethod = this.numberFactoryMethod;
			}
			methodNumberFactoryMethodMap.put(method, numberFactoryMethod);
		}

		return numberFactoryMethod;
	}

}
