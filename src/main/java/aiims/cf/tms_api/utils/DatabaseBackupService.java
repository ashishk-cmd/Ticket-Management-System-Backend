package aiims.cf.tms_api.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseBackupService {
	
	@Value("${backup.location}")
    private static String backupLocation;

	
    static DateFormat dateformat = new SimpleDateFormat("yyyymmddhhmm");
	static String strdate = dateformat.format(new Date());
	
//    private static final String BACKUP_LOCATION = "E://TMS//mysql//backup//" + strdate + "_" + "file.sql";
    private static final String BACKUP_LOCATION = backupLocation + strdate + "_" + "file.sql";

    
    @Scheduled(cron = "0 25 14 * * *") // Schedule backup task at 2 AM every day
    public void backupDatabase() {
        try (OutputStream os = new FileOutputStream(BACKUP_LOCATION)) {
            // Use mysqldump command to backup the database
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "D:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump",
                    "-u", "root",
                    "-psystem",
                    "aiims_app_tms"
            );
            Process process = processBuilder.start();

            // Read the output of mysqldump and write it to the backup file
            process.getInputStream().transferTo(os);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Database backup completed successfully at: " + LocalDateTime.now());
            } else {
                System.err.println("Error occurred while backing up the database");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Error occurred while backing up the database");
        }
    }
    
}
