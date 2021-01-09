package ru.kmvinvest;


import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class AppIot {
    public static void main(String[] args) throws PlatformAlreadyAssignedException {
        // Указываем платформу Orange Pi
        PlatformManager.setPlatform(Platform.ORANGEPI);

        SpringApplication.run(AppIot.class);
    }
}