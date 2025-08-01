package mission.utility;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import mission.application.domain.exception.DuplicateItemException;

public class Validator {
    public static void duplicateCheck(List<?> items){
        HashSet<Object> itemFilter = new HashSet<>(items);
        if(itemFilter.size()!=items.size()){
            throw new DuplicateItemException();
        }
    }
}
