package lang.serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 〈使用该关键字字段将不进行序列化〉<br>
 *
 * @author Carrie
 * @create 2020/1/14
 * @since 1.0.0
 */
public class TransientDemo implements Serializable {

    private String firstName;
    // 序列化时字段有改关键字修饰/静态变量将不被序列化
    private transient String middleName;
    private String lastName;


    public TransientDemo(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " " + "name is " + firstName + " " + middleName + " "  + lastName;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        TransientDemo transientDemo = new TransientDemo("lingge", "no", "zhao");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("tansientdemo"));
        objectOutputStream.writeObject(transientDemo);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("tansientdemo"));
        TransientDemo transientDe = (TransientDemo) objectInputStream.readObject();
        System.out.println(transientDe); //name is lingge null zhao

    }
}