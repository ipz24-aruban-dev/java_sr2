package oop;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ServicemanArrayManagerV3 extends ServicemanArrayManagerV2 {

    // ---------- Зчитування з ТЕКСТОВОГО файлу ----------

    /**
     * Формат рядка:
     *  S;Іван Петренко;солдат;3;
     *  O;Олег Сидоренко;капітан;10;командир роти
     *
     * type: S - Soldier, O - Officer
     */
    public void loadFromTextFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Serviceman> list = new ArrayList<>();

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue; // пропускаємо коментарі/порожні рядки
            }

            String[] parts = trimmed.split(";");
            if (parts.length < 4) {
                // мінімум: type;name;rank;years[;position]
                continue;
            }

            String type = parts[0].trim();
            String name = parts[1].trim();
            String rank = parts[2].trim();
            int years = Integer.parseInt(parts[3].trim());
            String position = parts.length >= 5 ? parts[4].trim() : "";

            if (type.equalsIgnoreCase("S")) {
                list.add(new Soldier(name, rank, years));
            } else if (type.equalsIgnoreCase("O")) {
                list.add(new Officer(name, rank, years, position));
            }
        }

        setStaff(list.toArray(new Serviceman[0]));
    }

    // ---------- Запис у БІНАРНИЙ файл ----------

    /**
     * Бінарний формат:
     * int count;
     * для кожного елемента:
     *   byte type (0 - Soldier, 1 - Officer)
     *   UTF name
     *   UTF rank
     *   int years
     *   UTF position (для солдата може бути порожній рядок)
     */
    public void saveToBinaryFile(Path path) throws IOException {
        Serviceman[] arr = getStaff();
        if (arr == null) return;

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(path.toFile()))
        )) {
            out.writeInt(arr.length);
            for (Serviceman s : arr) {
                if (s instanceof Officer o) {
                    out.writeByte(1); // Officer
                    out.writeUTF(o.getName());
                    out.writeUTF(o.getRank());
                    out.writeInt(o.getYearsOfService());
                    out.writeUTF(o.getPosition() == null ? "" : o.getPosition());
                } else {
                    out.writeByte(0); // Soldier (або базовий Serviceman)
                    out.writeUTF(s.getName());
                    out.writeUTF(s.getRank());
                    out.writeInt(s.getYearsOfService());
                    out.writeUTF(""); // немає посади
                }
            }
        }
    }

    // ---------- Зчитування з БІНАРНОГО файлу ----------

    public void loadFromBinaryFile(Path path) throws IOException {
        List<Serviceman> list = new ArrayList<>();

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(path.toFile()))
        )) {
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                byte type = in.readByte();
                String name = in.readUTF();
                String rank = in.readUTF();
                int years = in.readInt();
                String position = in.readUTF();

                if (type == 1) {
                    list.add(new Officer(name, rank, years, position));
                } else {
                    list.add(new Soldier(name, rank, years));
                }
            }
        }

        setStaff(list.toArray(new Serviceman[0]));
    }
}
