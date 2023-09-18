package common.core.test;

import com.google.common.collect.Lists;
import common.core.util.collection.ListUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListUtilTest {

    @Test
    public void testDiff() {

        ArrayList<Person> olds = Lists.newArrayList();
        ArrayList<Person> latest = Lists.newArrayList();

        List<Integer> diff = ListUtil.diff(olds, latest, Person::getId);
    }

    @Data
    @EqualsAndHashCode
    class Person {
        Integer id;
        Integer age;
        String name;

        @EqualsAndHashCode.Exclude Boolean gender;
    }
}
