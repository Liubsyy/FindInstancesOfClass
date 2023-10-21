import com.liubs.findinstances.jvmti.InstancesOfClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2023/10/20 10:17 PM
 **/

class A{}
class B{}

public class TestInstancesOfClass {

    private static <T> boolean isEqual(List<T> list, Object[] insts){
        if(null == list && null == insts){
            return true;
        }
        if(null == list || null == insts || list.size() != insts.length) {
            return false;
        }

        //先按hashcode排个序
        list.sort(Comparator.comparingInt(Object::hashCode));
        List<Object> list2 = Arrays.stream(insts).sorted(Comparator.comparingInt(Object::hashCode)).collect(Collectors.toList());

        //每一个对象一定是全等
        for(int i = 0,len=list.size();i<len ;i++) {
            if(list.get(i) != list2.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int count = 10;

        List<A> insts1 = new ArrayList<>();
        for(int i = 0; i<count;i++) {
            insts1.add(new A());
        }
        Object[] insts1_find = InstancesOfClass.getInstances(A.class);
        System.out.println(insts1);
        System.out.println(Arrays.asList(insts1_find));
        System.out.println("A的所有对象实例是否一致："+isEqual(insts1,insts1_find));


        List<B> insts2 = new ArrayList<>();
        for(int i = 0; i<count;i++) {
            insts2.add(new B());
        }
        Object[] insts2_find = InstancesOfClass.getInstances(B.class);
        System.out.println(insts2);
        System.out.println(Arrays.asList(insts2_find));
        System.out.println("B的所有对象实例是否一致："+isEqual(insts2,insts2_find));
    }

}
