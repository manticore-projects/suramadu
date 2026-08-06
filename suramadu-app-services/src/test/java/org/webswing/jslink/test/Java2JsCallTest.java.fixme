package org.webswing.jslink.test;

import static org.junit.Assert.*;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.junit.Test;

public class Java2JsCallTest extends AbstractJsLinkTest {

	@Override
	public void specificSetUp() {
	}

	@Test
	public void testEvalPrimitive() {
		Number n = (Number) root.eval("1+1");
		assertEquals(2, n);
		Boolean b = (Boolean) root.eval("true");
		assertEquals(true, b);
		String s = (String) root.eval("'a'+'b'");
		assertEquals("ab", s);
	}

	@Test
	public void testEvalArray() {
		JSObject array = (JSObject) root.eval("['a1','a2']");
		assertEquals("a1", array.getSlot(0));
		assertEquals("a2", array.getSlot(1));
		array.setSlot(2, "a3");
		assertEquals("a3", array.getSlot(2));
	}

	@Test
	public void testEvalJsObject() {
		JSObject obj = (JSObject) root.eval("var x={memberA:'a',member6:6};x");
		assertEquals("a", obj.getMember("memberA"));
		assertEquals(6, obj.getMember("member6"));
		obj.removeMember("memberA");
		assertEquals(null, obj.getMember("memberA"));
		obj.setMember("MemberTrue", true);
		assertEquals(true, obj.getMember("MemberTrue"));
	}

	@Test
	public void testJSObjectReferences() {
		JSObject obj = (JSObject) root.eval("var x={memberA:'a',member6:6};x");
		JSObject obj2 = (JSObject) root.eval("var z={memberF:'f'};z");
		obj.setMember("Memberz", obj2);
		assertEquals("f", ((JSObject) obj.getMember("Memberz")).getMember("memberF"));
	}

	@Test
	public void testJavaObjectReferences() {
		Object javaObj = new Object();
		JSObject obj = (JSObject) root.eval("var z={memberF:'f'};z");
		obj.setMember("MemberJ", javaObj);
		assertEquals(javaObj, obj.getMember("MemberJ"));
	}

	@Test
	public void testFunctionReference() {
		JSObject func = (JSObject) root.eval("var z=function(x){return 'done'+x;};z");
		String s = (String) func.call("call", new Object[] { func, "aaa" });
		assertEquals("doneaaa", s);
	}

	@Test
	public void testErrorInEval() {
		try {
			root.eval("this is not executable");
			assertTrue(false);
		} catch (JSException e) {
			assertTrue(true);
		}
	}
}
