package cn.j.netstorage.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashCodeUtilTest {

    @Test
    void getHashCode() {
        String path="C:\\Users\\Shinelon\\Desktop\\netstorage\\pom.xml";
        System.out.println(HashCodeUtil.getHashCode(path));
    }

    @Test
    void getHashCode1() {

        String path="C:\\Users\\Shinelon\\Desktop\\netstorage\\pom.xml";

        System.out.println(HashCodeUtil.getHashCode(path));
    }
}