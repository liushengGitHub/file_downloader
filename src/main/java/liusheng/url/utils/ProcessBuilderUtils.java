package liusheng.url.utils;


import java.util.Scanner;

public class ProcessBuilderUtils {
    public static void executeAndDiscardOuput(String ... commands) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(commands);
        Process process = builder
                .redirectErrorStream(true)
                .start();
        try {


            Scanner scanner = new Scanner(process.getInputStream());

            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            process.waitFor();

        }catch (Exception e) {
            throw  e;
        }finally {
            process.destroy();
        }
    }
}
