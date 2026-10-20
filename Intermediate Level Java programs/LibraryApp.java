import java.util.*;

public class LibraryApp {
    interface Borrowable {
        boolean isAvailable();
        void borrow(String member);
        void returnItem();
    }

    static abstract class LibraryItem implements Borrowable {
        protected String id;
        protected String title;
        protected String borrower;

        LibraryItem(String id, String title){
            this.id = id;
            this.title = title;
        }

        public boolean isAvailable() { return borrower == null; }
        public void borrow(String member) { this.borrower = member; }
        public void returnItem() { this.borrower = null; }
        public  String getTitle() { return title; }
        public String getId() { return id; }

        abstract String type();


        @Override
        public String toString() {
            return String.format("[%s] %s (%s) - %s", id, title, type(), isAvailable() ? "available" : "borrowed by " + borrower);
        }
    }

    static class Book extends LibraryItem {
        Book(String id, String title) { super(id, title); }
        String type() { return "DVD"; }
    }

    static class DVD extends LibraryItem{
        DVD(String id, String title) { super(id, title); }
        @Override
        String type() { return "DVD"; }
    }

    static class Member {
        String name;
        List<LibraryItem> borrowed = new ArrayList<>();
        Member(String name) { this.name = name; }
    }

    static class Library{
        Map<String, LibraryItem> items = new HashMap<>();
        Map<String, Member> members = new HashMap<>();

        void addItem(LibraryItem item) {
            if (items.containsKey(item.getId()))
                throw new IllegalArgumentException("Duplicate ID: " + item.getId());
            items.put(item.getId(), item);
        }

        void addMember(String name){
            if (members.containsKey(name))
                throw new IllegalArgumentException("Member exists: " + name);
            members.put(name, new Member(name));
        }

        void borrowItem(String memberName, String itemId){
            Member m = members.get(memberName);
            if (m == null) throw new IllegalArgumentException("Unknown member; " + memberName);
            LibraryItem item = items.get(itemId);
            if (item == null) throw new IllegalArgumentException("Unkwown item: " + itemId);
            if (!item.isAvailable()) throw new IllegalStateException("Item already borrowed");
            item.borrow(memberName);
            m.borrowed.add(item);
        }

        void returnItem(String memberName, String itemId){
            Member m = members.get(memberName);
            if (m == null) throw new IllegalArgumentException("Unknown member: " + memberName);
            LibraryItem item = items.get(itemId);
            if (item == null) throw new IllegalArgumentException("Unknown item: " + itemId);
            if (item.isAvailable()) throw new IllegalStateException("Item not borrowed");
            item.returnItem();
            m.borrowed.remove(item);
        }

        void listItems(){
            items.values().stream()
                    .sorted(Comparator.comparing(LibraryItem::getId))
                    .forEach(System.out::println);
        }

        void listMembers(){
            members.values().forEach(m -> {
                System.out.println("\uD83D\uDC64" + m.name + "-- borrowed: " + (m.borrowed.isEmpty() ? "nothing" : m.borrowed.stream().map(LibraryItem::getTitle).toList()));
            });
        }
    }

    public static void main(String[] args){
        Library lib = new Library();
        lib.addItem(new Book("B001", "Clean Code"));
        lib.addItem(new Book("B002", "Effective Java"));
        lib.addItem(new DVD("D001", "Inception"));
        lib.addMember("Alice");
        lib.addMember("Bob");

        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("\n LIBRARY");
            System.out.println("1. List items");
            System.out.println("2. List members");
            System.out.println("3. Borrow");
            System.out.println("4. Return");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            if (!sc.hasNextInt()) { sc.next(); continue; }
            int c = sc.nextInt();
            sc.nextLine();

            try {
                switch (c) {
                    case 1 -> lib.listItems();
                    case 2 -> lib.listMembers();
                    case 3 -> {
                        System.out.print("Member name: ");
                        String m = sc.nextLine();
                        System.out.print("Item ID: ");
                        String id = sc.nextLine();
                        lib.borrowItem(m, id);
                        System.out.println("Borrowed.");
                    }
                    case 4 -> {
                        System.out.print("Member name: ");
                        String m = sc.nextLine();
                        System.out.print("Item ID: ");
                        String id = sc.nextLine();
                        lib.returnItem(m, id);
                        System.out.println("Returned.");
                    }
                    case 5 -> { sc.close(); return; }
                    default -> System.out.println("Invalid.");
                }

                } catch (Exception e){
                System.out.println("❌" + e.getMessage());
            }
            }
        }
    }