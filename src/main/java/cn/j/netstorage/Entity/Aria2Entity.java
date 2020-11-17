package cn.j.netstorage.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedList;

@Getter
@Setter
@ToString
public class Aria2Entity {
    String id;
    String jsonrpc;
    String method;
    Object params;


}
