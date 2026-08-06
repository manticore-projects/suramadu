package org.webswing.jslink.test;

import static org.junit.Assert.assertEquals;

import javax.script.ScriptException;

import netscape.javascript.JSObject;

import org.junit.Test;
import org.webswing.model.c2s.KeyboardEventMsgIn;
import org.webswing.model.c2s.KeyboardEventMsgIn.KeyEventType;

public class Js2JavaCallTest extends AbstractJsLinkTest {
	TestObject testObject = new TestObject();

	@Override
	public void specificSetUp() {
		root.setMember("testObject", testObject);
	}

	@Test
	public void testBasicMethodCall() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.echo('test').then(function(r){evalValue= r;})");
		assertEquals("test", engine.eval("evalValue"));
	}

	@Test
	public void testNullParam() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.echo(null).then(function(r){evalValue= r;})");
		assertEquals(null, engine.eval("evalValue"));
	}

	@Test
	public void testJSObjectRefReturn() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.echoObj(window).then(function(r){evalValue= r;})");
		assertEquals(engine.eval("window"), engine.eval("evalValue"));
	}

	@Test
	public void testJavaRefReturn() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.getObject().then(function(r){evalValue= r;})");
		engine.eval("evalValue.hashCode().then(function(r){evalValue= r;})");
		assertEquals(testObject.getObject().hashCode(), engine.eval("evalValue"));
	}

	@Test
	public void testBasicArrayReturn() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.getIntArray().then(function(r){window.evalValue= r;})");
		JSObject e = (JSObject) root.getMember("evalValue");
		assertEquals(testObject.getIntArray()[1], e.getSlot(1));
	}

	@Test
	public void testCyclicRefArray() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.getCyclicArray().then(function(r){evalValue= r;},function(r){print(r)})");
		assertEquals(testObject.getCyclicArray()[0], engine.eval("evalValue[0]"));
	}

	@Test
	public void testComplexObjectParam() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.complexObjectParamTest(JSON.stringify({type:'keypress',character:11})).then(function(r){evalValue= r;})");
	}

	@Test
	public void testMethodThrowException() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.exceptionTest().then(function(r){},function(e){evalValue= e;})");
		assertEquals("[object Error]", engine.eval("Object.prototype.toString.call(evalValue)"));
	}

	@Test
	public void testWrongNumberOfParams() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.echo().then(function(r){},function(e){evalValue= e;})");
		assertEquals("[object Error]", engine.eval("Object.prototype.toString.call(evalValue)"));
	}

	@Test
	public void testWrongTypeOfParams() throws ScriptException, InterruptedException {
		engine.eval("window.testObject.complexObjectParamTest(JSON.stringify({type:'keypress',random:'value'})).then(function(r){},function(e){evalValue= e;})");
		assertEquals("[object Error]", engine.eval("Object.prototype.toString.call(evalValue)"));
	}

	public class TestObject {
		Object javaObject = new Object();
		Integer[] arrayResult = new Integer[] { 1, 2, 3, 4, 5 };
		Object[] cyclicRefArray = new Object[] { "asdf", null };

		public String echo(String echo) {
			return echo;
		};

		public JSObject echoObj(JSObject echo) {
			return echo;
		};

		public Object getObject() {
			return javaObject;
		}

		public Integer[] getIntArray() {
			return arrayResult;
		}

		public Object[] getCyclicArray() {
			cyclicRefArray[1] = cyclicRefArray;
			return cyclicRefArray;
		}

		public void complexObjectParamTest(KeyboardEventMsgIn object) {
			assertEquals(KeyEventType.keypress, object.getType());
			assertEquals(11, object.getCharacter());
		}

		public void exceptionTest() throws Exception {
			throw new RuntimeException("runtimeError");
		}
	}

}
