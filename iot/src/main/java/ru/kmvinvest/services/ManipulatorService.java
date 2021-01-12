package ru.kmvinvest.services;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kmvinvest.dto.ServoDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManipulatorService {
    private final LogService logService;

    private static final BigDecimal FREQUENCY = new BigDecimal("48.828");
    private static final BigDecimal FREQUENCY_CORRECTION_fACTOR = new BigDecimal("1.0578");


    // Передаем адрес драйвера сервоприводом PCA9685 PWM
    private static PCA9685GpioProvider provider;

    static {
        try {
            System.out.println("Запуск StartPCA9685GpioProvider");
            provider = StartPCA9685GpioProvider();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            System.out.println("Ошибка запуска PCA9685GpioProvider");
            e.printStackTrace();

        }
        System.out.println("Запуск StartPCA9685GpioProvider завершился");
    }
    // Определяем выводы сервоприводов


    // Состояние блокировки манипулятора
    private static Boolean stateRun = false;

    public Boolean getStateRun() {
        return stateRun;
    }

    private void setStateRun(Boolean state) {
        stateRun = state;
    }

    //Граничные значения сервоприводов минимум - максимум для пинов от 0 до 5
    private static final int[][] LIMITS = {
            {550, 1750},
            {600, 1800},
            {600, 2400},
            {550, 2500},
            {550, 2500},
            {1200, 2000}
    };

    // Выполнение программы манипулятора
    public void run(List<ServoDto> servoDtos) {
        if (getStateRun()) {
            logService.addAndExeption("Процесс недоступен. Манипулятор заблокирован");
            return;
        }
        try {
            setStateRun(true);
            for (ServoDto servoDto : servoDtos) {
                logService.addAndSout("--------Start id=" + servoDto.getId() + "----------");
                Thread.sleep(servoDto.getPeriod());
                if (converterRange(0, servoDto.getPin00()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_00, converterRange(0, servoDto.getPin00()));
                if (converterRange(1, servoDto.getPin01()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_01, converterRange(1, servoDto.getPin01()));
                if (converterRange(2, servoDto.getPin02()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_02, converterRange(2, servoDto.getPin02()));
                if (converterRange(3, servoDto.getPin03()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_03, converterRange(3, servoDto.getPin03()));
                if (converterRange(4, servoDto.getPin04()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_04, converterRange(4, servoDto.getPin04()));
                if (converterRange(5, servoDto.getPin05()) != 0)
                    provider.setPwm(PCA9685Pin.PWM_05, converterRange(5, servoDto.getPin05()));

                String result = String.format("\nОжидание: %d.\n", servoDto.getPeriod());
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin00(), converterRange(0, servoDto.getPin00()));
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin01(), converterRange(1, servoDto.getPin01()));
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin02(), converterRange(2, servoDto.getPin02()));
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin03(), converterRange(3, servoDto.getPin03()));
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin04(), converterRange(4, servoDto.getPin04()));
                result += String.format("Pin00 = %d (%d).\n", servoDto.getPin05(), converterRange(5, servoDto.getPin05()));

                logService.addAndSout(result);
                logService.addAndSout("--------End----------");
            }
        } catch (Exception exeption) {
            setStateRun(false);
            logService.addAndExeption(exeption.getMessage());
        }
        setStateRun(false);
    }

    // Управление сервоприводом
    public String rotation(Integer pin, Integer angle) {
        if (pin < 0 || pin >= LIMITS.length)
            logService.addAndExeption("Указанный pin отсутствует. Допустимый диапазон 0 - " + (LIMITS.length - 1));
        if (angle < 0 || angle > 100)
            logService.addAndExeption("Указан некорректный угол поворота. Допустимый диапазон 0 - 100");

        if (getStateRun()) return "Процесс недоступен. Манипулятор заблокирован";
        try {
            setStateRun(true);
            logService.addAndSout("--------Start rotation ----------");
            if (converterRange(pin, angle) != 0) provider.setPwm(PCA9685Pin.ALL[pin], converterRange(pin, angle));
            logService.addAndSout("Pin = " + angle + " (" + converterRange(pin, angle) + ")");
            logService.addAndSout("--------End rotation----------");
            setStateRun(false);
        } catch (Exception exeption) {
            setStateRun(false);
            logService.addAndExeption(exeption.getMessage());
        }
        setStateRun(false);
        return "Позиция сервопривода Pin = " + pin + " в позиции " + angle + " (" + converterRange(pin, angle) + ")";
    }

    //     Определение выводов
    static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        return new GpioPinPwmOutput[]{
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Серво 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Серво 01"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Серво 02"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Серво 03"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Серво 04"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Серво 05"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "not used"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
    }


    // Конвертируем в ШИМ диапазон
    private Integer converterRange(Integer pin, Integer value) {
        if (value >= 100) return LIMITS[pin][1];
        if (value <= 0) return 0;
        return (LIMITS[pin][1] - LIMITS[pin][0]) * value / 100 + LIMITS[pin][0];
    }


    private static PCA9685GpioProvider StartPCA9685GpioProvider() throws IOException, I2CFactory.UnsupportedBusNumberException {

        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_0); // Определяем интерфейс I2C - 0
        // Передаем адрес драйвера сервоприводом PCA9685 PWM
        PCA9685GpioProvider provider = new PCA9685GpioProvider(bus, 0x40, FREQUENCY, FREQUENCY_CORRECTION_fACTOR);
        GpioPinPwmOutput[] myOutputs;
        myOutputs = provisionPwmOutputs(provider);
        // Reset outputs
        provider.reset();
        return provider;
    }
}
