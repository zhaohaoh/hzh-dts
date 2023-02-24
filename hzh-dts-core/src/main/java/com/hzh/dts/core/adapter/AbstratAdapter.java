package com.hzh.dts.core.adapter;

import com.hzh.dts.properties.TargetMapping;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstratAdapter implements OuterAdapter {

    @Override
    public void save(TargetMapping targetMapping, Collection<Map<String, Object>> objs) {
        renameTargetColumn(objs, targetMapping);
        doSave(targetMapping, objs);
    }

    public abstract void doSave(TargetMapping targetMapping, Collection<Map<String, Object>> objs);

    /**
     * 重命名目标列
     *
     * @param collection    集合
     * @param targetMapping 目标映射
     */
    private void renameTargetColumn(Collection<Map<String, Object>> collection, TargetMapping targetMapping) {
        if (targetMapping.isToCamelCase()) {
            for (Map<String, Object> map : collection) {
                //集合替换否则有并发异常
                Set<String> keySet = new HashSet<>(map.keySet());
                for (String k : keySet) {
                    map.put(toCamelCase(k), map.remove(k));
                }
            }
        }

        Map<String, String> targetColumns = targetMapping.getTargetColumns();

        if (CollectionUtils.isEmpty(targetColumns)) {
            return;
        }

        for (Map<String, Object> map : collection) {
            targetColumns.forEach((k, v) -> {
                if (map.containsKey(k)) {
                    map.put(v, map.remove(k));
                }
            });
        }
    }

    private String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
