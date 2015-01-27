package app;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Member {
    public String id;
    public String name;

    public Member() { }

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
