public class OtherTest {

    public static void main(String[] args) {
        String file = "assdd.dfd";
        int i = file.lastIndexOf(".");
        String suffix = file.substring(i + 1);
        System.out.println("data." + suffix);

        System.out.println(file.substring(0,i));
    }
}
