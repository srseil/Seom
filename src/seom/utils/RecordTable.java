package seom.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordTable {
    private final Map<String, List<Object>> table;
    private final List<String> columnOrder;

    public RecordTable() {
        table = new HashMap<>();
        columnOrder = new ArrayList<>();
    }

    public boolean isEmpty() {
        return table.values().stream().filter(x -> !x.isEmpty()).findAny().isEmpty();
    }

    public void addColumn(String name) {
        assert !table.containsKey(name) : "Column '" + name + "' already exists";

        table.put(name, new ArrayList<>());
        columnOrder.add(name);
    }

    public void addRecord(Object... values) {
        assert values.length <= columnOrder.size() : "Table does not have enough columns";

        for (int i = 0; i < columnOrder.size(); i++) {
            String column = columnOrder.get(i);
            Object value = i < values.length ? values[i] : null;
            table.get(column).add(value);
        }
    }

    public String toCSVString() {
        return toCSVString(',');
    }

    public String toCSVString(char separator) {
        if (table.keySet().isEmpty()) {
            return "";
        }

        var builder = new StringBuilder();
        for (int i = 0; i < columnOrder.size(); i++) {
            builder.append(columnOrder.get(i));
            if (i < columnOrder.size() - 1) {
                builder.append(separator);
            }
        }
        builder.append("\n");

        int numRecords = table.get(columnOrder.get(0)).size();
        for (int i = 0; i < numRecords; i++) {
            for (int j = 0; j < columnOrder.size(); j++) {
                String column = columnOrder.get(j);
                List<Object> values = table.get(column);
                if (values.get(i) != null) {
                    builder.append(values.get(i).toString());
                }
                if (j < columnOrder.size() - 1) {
                    builder.append(separator);
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public void writeCSVTo(String filePath) {
        writeCSVTo(filePath, ',');
    }

    public void writeCSVTo(String filePath, char separator) {
        try {
            Files.writeString(Path.of(filePath), toCSVString(separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
