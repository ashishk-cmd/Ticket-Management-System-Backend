package aiims.cf.tms_api.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseBackupTask {

	@Value("${backup.location}")
	private String backupLocation;
	
	@Value("${database.Name}")
	private String dbName;

	@Value("${spring.datasource.username}")
    private String datasourceUsername;
	
	@Value("${spring.datasource.password}")
    private String datasourcePassword;
	
	@Value("${postgres.bin.path}")
	private String postgresBinPath;


	@Scheduled(cron = "0 55 16 * * *")
    public void performBackup() {
		
		String backupDir = backupLocation; 
        String databaseName = dbName; 
        String username = datasourceUsername; 
        String password = datasourcePassword;
	        
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String backupFileName = String.format("%s_%s.backup", databaseName, timeStamp);
//        String command = String.format("\"C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump\" -U %s -F c -b -v -f \"%s\\%s\" %s", 
//                username, backupDir, backupFileName, databaseName);
        
        String command = String.format("\"%s\\pg_dump\" -U %s -F c -b -v -f \"%s\\%s\" %s", 
        		postgresBinPath, username, backupDir, backupFileName, databaseName);

        System.out.println("Command: " + command);  // Debug print

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.environment().put("PGPASSWORD", password);

        try {
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.err.println("Backup failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
