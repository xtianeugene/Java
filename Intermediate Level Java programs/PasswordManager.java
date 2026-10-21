import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.*;

public class PasswordManager {

    static final String FILE = "vault.dat";
    static final SecureRandom random = new SecureRandom();
    static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    static final String DIGITS = "0123456789";
    static final String SYMBOLS = "!@#$%^&*()-_=+[]{};:,.<>?";

    static Map<String, String> vault = new LinkedHashMap<>();

    public static void main(String[] args) throws Exception {
        load();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("\n PASSWORD MANAGER");
            System.out.println("1. Generate password");
            System.out.println("2. Save credentials");
            System.out.println("3. Retrieve credentials");
            System.out.println("4. List sites");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            if (!sc.hasNextInt()) { sc.next(); continue; }
            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1 -> {
                    System.out.print("Length (min 8): ");
                    int len = Math.max(8, sc.nextInt());
                    System.out.print("Include symbols? (y/n): ");
                    boolean sym = sc.next().equalsIgnoreCase("y");
                    String pwd = generate(len, sym);
                    System.out.println("\uD83D\uDD11" + pwd);
                }
                case 2 -> {
                    System.out.print("Site/App: ");
                    String site = sc.nextLine();
                    System.out.print("Password (blank to generate): ");
                    String pwd = sc.nextLine();
                    if (pwd.isBlank()){
                        pwd = generate(16, true);
                        System.out.println("\uD83D\uDD11 Generated: " + pwd);
                    }
                    vault.put(site, pwd);
                    save();
                    System.out.println("Saved.");
                }
                case 3 -> {
                    System.out.print("Site: ");
                    String site = sc.nextLine();
                    if (vault.containsKey(site)) System.out.println("\uD83D\uDD11" + vault.get(site));
                    else System.out.println("Not found.");
                }
                case 4 -> {
                    if (vault.isEmpty()) System.out.println("(empty)");
                    else vault.keySet().forEach(k -> System.out.println(" • " + k));
                }
                case 5 -> { sc.close(); return; }
                default -> System.out.println("Invalid.");

            }
        }
    }

    static String generate(int length, boolean symbols){
        String chars = UPPER + LOWER + DIGITS + (symbols ? SYMBOLS : "");
        StringBuilder sb = new StringBuilder();
        //ensure at least one of each required category
        sb.append(UPPER.charAt(random.nextInt(UPPER.length())));
        sb.append(LOWER.charAt(random.nextInt(LOWER.length())));
        sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        if (symbols) sb.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        while (sb.length() < length) sb.append(chars.charAt(random.nextInt(chars.length())));
        // shuffle
        char[] arr = sb.toString().toCharArray();
        for (int i = arr.length - 1; i > 0; i--){
            int j = random.nextInt(i + 1);
            char tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        return new String(arr);
    }

    static void save() throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(FILE))){
            for (var e : vault.entrySet()) {
                String enc = Base64.getEncoder().encodeToString(e.getValue().getBytes(StandardCharsets.UTF_8));
                bw.write(e.getKey() + "=" + enc);
                bw.newLine();
            }
        }
    }

    static void load() throws IOException {
        Path p = Paths.get(FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p)){
            String line;
            while ((line = br.readLine()) != null){
                int eq = line.indexOf('=');
                if (eq <= 0) continue;
                String site = line.substring(0, eq);
                String pwd = new String(Base64.getDecoder().decode(line.substring(eq + 1)), StandardCharsets.UTF_8);
                vault.put(site, pwd);
            }
        }
    }
}