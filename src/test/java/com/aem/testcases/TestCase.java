package com.aem.testcases;



/**
 * 
 * @author mkarthik
 * 
 *
 *
 */

public class TestCase {
	public static void main(String a[]){
		A aa = new A();
		aa.print();
	}
}

class A{
	public void print(){
		System.out.println("Inside Print");
	}
	private void test(){
		System.out.println("Inside Test");
	}
}
