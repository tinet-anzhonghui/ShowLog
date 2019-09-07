package mytest;

import org.junit.Test;

public class StringTest {

    @Test
    public void fun1(){
        String str1 = "E:\\hxInvoicingSystemLogs\\test2\\test1.txt";
        System.out.println(str1);
        String str2 = "test.txt";
        System.out.println(str1.lastIndexOf(str2));
    }
}
