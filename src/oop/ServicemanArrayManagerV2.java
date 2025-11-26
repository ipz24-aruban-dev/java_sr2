package oop;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.text.Collator;

public class ServicemanArrayManagerV2 extends ServicemanArrayManager {

    // Сортування за ПІБ (name) за зростанням
    public void sortByNameAscending() {
        Serviceman[] staff = getStaff();
        if (staff == null) return;
        Collator collator = Collator.getInstance(new Locale("uk", "UA"));
        collator.setStrength(Collator.PRIMARY);

        Arrays.sort(staff, (a, b) ->
                collator.compare(a.getName(), b.getName())
        );
    }

    // Сортування за ПІБ (name) за спаданням
    public void sortByNameDescending() {
        Serviceman[] staff = getStaff();
        if (staff == null) return;

        Collator collator = Collator.getInstance(new Locale("uk", "UA"));
        collator.setStrength(Collator.PRIMARY);

        Arrays.sort(staff, (a, b) ->
                collator.compare(b.getName(), a.getName())
        );
    }

    // Фільтрація за регулярним виразом по полю name
    public String buildFilteredByNameRegex(String regex) throws PatternSyntaxException {
        Serviceman[] staff = getStaff();
        if (staff == null || staff.length == 0) {
            return "Масив порожній.";
        }

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        StringBuilder sb = new StringBuilder();
        sb.append("Військовослужбовці, для яких ПІБ відповідає шаблону \"")
                .append(regex).append("\":\n");

        boolean any = false;
        for (int i = 0; i < staff.length; i++) {
            Serviceman s = staff[i];
            if (pattern.matcher(s.getName()).find()) {
                any = true;
                sb.append("[").append(i).append("] ")
                        .append(s.getName())
                        .append(", ").append(s.getRank())
                        .append(", стаж: ").append(s.getYearsOfService())
                        .append(" років\n");
            }
        }

        if (!any) {
            sb.append("  Немає елементів, що задовольняють умову.\n");
        }

        return sb.toString();
    }

    // Інфо про мінімальний та максимальний стаж
    public String buildMinMaxYearsInfo() {
        Serviceman[] staff = getStaff();
        if (staff == null || staff.length == 0) {
            return "Масив порожній.";
        }

        Serviceman min = staff[0];
        Serviceman max = staff[0];

        for (Serviceman s : staff) {
            if (s.getYearsOfService() < min.getYearsOfService()) {
                min = s;
            }
            if (s.getYearsOfService() > max.getYearsOfService()) {
                max = s;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Найменший стаж має: ")
                .append(min.getName())
                .append(" (").append(min.getYearsOfService()).append(" років, звання: ")
                .append(min.getRank()).append(").\n");

        sb.append("Найбільший стаж має: ")
                .append(max.getName())
                .append(" (").append(max.getYearsOfService()).append(" років, звання: ")
                .append(max.getRank()).append(").\n");

        return sb.toString();
    }
}
