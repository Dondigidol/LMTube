package application.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;



    @Override
    public String toString(){
        return "Log{" +
                "" + id +
                "" + name +
                "}";
    }

}
