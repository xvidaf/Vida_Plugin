package models;

import java.util.ArrayList;

public class MyObject extends Element{
    private ArrayList<MyObject> children;

    public MyObject(String name) {
        super(name);
        this.children = new ArrayList<>();
    }

    public void addChild(MyObject child) {
        children.add(child);
    }

    public ArrayList<MyObject> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
  
}
