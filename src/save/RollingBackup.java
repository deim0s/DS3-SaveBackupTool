package save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class RollingBackup {
    public static Path source;
    public static Path destination;
    public static String prefix;
    public static String extension;
    public static int mIteration;
    public static int cIteration;
    public static int delay;
    
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        File executionPath = new File(RollingBackup.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        final File configuration = new File(executionPath.getParentFile(), "config.properties");
        final Properties properties = new Properties();
        try (InputStream in = new FileInputStream(configuration)) {
            properties.load(in);
            source      = Paths.get(properties.getProperty("save_file"));
            destination = Paths.get(properties.getProperty("backup_path"));
            prefix     = properties.getProperty("backup_prefix", "DS30000");
            extension   = properties.getProperty("backup_extension", ".sl2");
            mIteration  = Integer.parseInt(properties.getProperty("max_saves", "10"));
            cIteration  = Integer.parseInt(properties.getProperty("last_backup", "1"));
            delay       = Integer.parseInt(properties.getProperty("delay", "10"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (OutputStream out = new FileOutputStream(configuration)) {
                properties.setProperty("last_backup", "" + cIteration);
                properties.store(out, null);
            } catch (IOException e) { e.printStackTrace(); }
        }));
        
        System.out.println("Creating backup directory if necessary...");
        if(!Files.exists(destination))
            Files.createDirectory(destination);
        
        System.out.printf("Resuming from last backup [%d]...\n", cIteration++);
        while(true) {
            if(cIteration > mIteration || cIteration < 1)
                cIteration = 1;
            
            String backup = prefix + "_" + String.format("%0" + ((int)Math.log10(mIteration) + 1) + "d", cIteration) + extension;
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            Files.copy(source, destination.resolve(backup), StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("Backing up to %s at %s...\n", backup, timeStamp);
            
            Thread.sleep(TimeUnit.MINUTES.toMillis(delay));
            cIteration++;
        }
    }
}
