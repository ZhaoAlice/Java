package lang.java15;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/9/23
 * @since 1.0.0
 */
public sealed class SealedClassTest permits SubClass1, SubClass2, SubClass3 {
}

// 解除密封限制
non-sealed class SubClass1 extends SealedClassTest {

}

// 继续限制为密封类
sealed class SubClass2 extends SealedClassTest permits SubClass2.SubClass4 {
    static final class SubClass4 extends SubClass2 {

    }
}

// 声明为不可再被继承的类
final class SubClass3 extends SealedClassTest {

}