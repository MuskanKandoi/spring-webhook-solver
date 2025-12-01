package com.example.webhooks.solver;

import org.springframework.stereotype.Component;

@Component
public class SqlSolver {

    /**
     * Returns the final SQL query (Question 2).
     *
     * This SQL is written for MySQL-compatible databases.
     *
     * It calculates for every department:
     *  - average age of distinct employees who have any payment with amount > 70000
     *  - employee list: up to 10 employee full names (FIRST_NAME + ' ' + LAST_NAME) from that group
     * Results ordered by department_id DESC.
     *
     * Note: age is calculated using TIMESTAMPDIFF(YEAR, dob, CURDATE()).
     */
    public String solveFor(String regNo, int questionId) {
        if (questionId != 2) {
            return "/* Question 1 not implemented in this build. */";
        }

        String sql = "SELECT d.department_name AS DEPARTMENT_NAME,\n" +
                     "       ROUND(AVG(emp_age.AGE),2) AS AVERAGE_AGE,\n" +
                     "       (\n" +
                     "         SELECT GROUP_CONCAT(CONCAT(e2.first_name,' ',e2.last_name) SEPARATOR ', ')\n" +
                     "         FROM (\n" +
                     "           SELECT emp.emp_id\n" +
                     "           FROM employee emp\n" +
                     "           JOIN payments p ON emp.emp_id = p.emp_id\n" +
                     "           WHERE p.amount > 70000 AND emp.department = d.department_id\n" +
                     "           GROUP BY emp.emp_id\n" +
                     "           ORDER BY emp.emp_id\n" +
                     "           LIMIT 10\n" +
                     "         ) AS sub\n" +
                     "         JOIN employee e2 ON sub.emp_id = e2.emp_id\n" +
                     "       ) AS EMPLOYEE_LIST\n" +
                     "FROM department d\n" +
                     "JOIN (\n" +
                     "  SELECT emp.emp_id, emp.department, TIMESTAMPDIFF(YEAR, emp.dob, CURDATE()) AS AGE\n" +
                     "  FROM employee emp\n" +
                     "  JOIN payments p ON emp.emp_id = p.emp_id\n" +
                     "  WHERE p.amount > 70000\n" +
                     "  GROUP BY emp.emp_id, emp.department, emp.dob\n" +
                     ") emp_age ON emp_age.department = d.department_id\n" +
                     "GROUP BY d.department_id, d.department_name\n" +
                     "ORDER BY d.department_id DESC;";

        return sql;
    }
}
