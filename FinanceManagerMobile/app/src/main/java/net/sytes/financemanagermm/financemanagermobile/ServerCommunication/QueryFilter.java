package net.sytes.financemanagermm.financemanagermobile.ServerCommunication;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class QueryFilter {
    private Map<String, String> criteria;

    public QueryFilter() {
        this.criteria =new LinkedHashMap<>();
    }

    public void addCriteria(String key, String criteria) {
        this.criteria.put(key, criteria);
    }

    public String getFilterString () {
        StringBuilder builder = new StringBuilder();
        criteria.values().forEach(criteria -> builder.append(" ").append(criteria));
        return builder.toString();
    }
}
