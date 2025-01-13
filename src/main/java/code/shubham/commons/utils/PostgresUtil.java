package code.shubham.commons.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PostgresUtil {

    public static void log(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; ++i) {
                row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
            }
            rows.add(row);
        }

        log.info(String.format("ResultSet: %s", rows));
    }

}
