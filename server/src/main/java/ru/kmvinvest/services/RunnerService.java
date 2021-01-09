package ru.kmvinvest.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.kmvinvest.dto.ServoDto;
import ru.kmvinvest.entities.ServoEntity;


@Service
@RequiredArgsConstructor
public class RunnerService {

    private final LogService logService;
    private final ConfigService configService;


    private RestTemplate restTemplate = new RestTemplate();
//    private ObjectMapper objectMapper = new ObjectMapper();


    // REST Запрос на выполнение программы манипулятору
    public String run(Integer pid) {

        logService.add("Передача данных программмы pid=" + pid);
        String result = "";
        String url = configService.getValue("manipulator_host", "http://localhost:8082") + "/run/" + pid;
        logService.add("Старт запроса по адресу " + url);
        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                result = objectMapper.readValue(responseEntity.getBody(), String.class);
                result = responseEntity.getBody();
                logService.add("RunnerService.run Поучен ответ на запрос: " + result);
                return result;
            }

        } catch (HttpStatusCodeException ex) {
            logService.add("Ошибка запроса RunnerService.run: " + ex.getResponseBodyAsString());
            HttpStatus statusCode = ex.getStatusCode();
            String responseBodyAsString = ex.getResponseBodyAsString();
            System.out.println(statusCode);
            System.out.println(responseBodyAsString);
            System.out.println("END responseEntity");

        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса RunnerService.run: " + ex.getMessage());
        }
        return result;
    }


    // REST Запрос на управление сервоприводами манипулятора
    public String rotation(Integer pin, Integer rotation) {

        logService.add("Передача данных поворота Pin=" + pin + " в позицию " + rotation);
        String result = "";
        String url = configService.getValue("manipulator_host", "http://localhost:8082")
                + String.format("/rotation/%d/%d", pin, rotation);
        logService.add("Старт запроса по адресу " + url);
        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                result = responseEntity.getBody();
                logService.add("Поучен ответ на запрос RunnerService.rotation: " + result);
                return result;
            }

        } catch (HttpStatusCodeException ex) {
            logService.addAndExeption("Ошибка запроса RunnerService.rotation: " + ex.getResponseBodyAsString());
            HttpStatus statusCode = ex.getStatusCode();
            String responseBodyAsString = ex.getResponseBodyAsString();
            System.out.println(statusCode);
            System.out.println(responseBodyAsString);
            System.out.println("END responseEntity");
        } catch (Exception ex) {
            logService.addAndExeption("Ошибка запроса RunnerService.rotation: " + ex.getMessage());
        }
        return result;
    }


    private ServoEntity mapToEntity(ServoDto dto) {
        ServoEntity entity = new ServoEntity();
        entity.setId(dto.getId());
        entity.setPid(dto.getPid());
        entity.setOrdering(dto.getOrdering());
        entity.setState(dto.getState());
        entity.setPeriod(dto.getPeriod());
        entity.setPin00(dto.getPin00());
        entity.setPin01(dto.getPin01());
        entity.setPin02(dto.getPin02());
        entity.setPin03(dto.getPin03());
        entity.setPin04(dto.getPin04());
        return entity;
    }


    private ServoDto mapToDto(ServoEntity servoEntity) {
        ServoDto dto = new ServoDto();
        dto.setId(servoEntity.getId());
        dto.setPid(servoEntity.getPid());
        dto.setOrdering(servoEntity.getOrdering());
        dto.setState(servoEntity.getState());
        dto.setPeriod(servoEntity.getPeriod());
        dto.setPin00(servoEntity.getPin00());
        dto.setPin01(servoEntity.getPin01());
        dto.setPin02(servoEntity.getPin02());
        dto.setPin03(servoEntity.getPin03());
        dto.setPin04(servoEntity.getPin04());
        return dto;
    }
}
